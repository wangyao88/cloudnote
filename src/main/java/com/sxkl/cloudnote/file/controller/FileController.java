package com.sxkl.cloudnote.file.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sxkl.cloudnote.backup.entity.DataBaseInfo;
import com.sxkl.cloudnote.backup.service.MysqlBackupService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.utils.DateUtils;
import com.sxkl.cloudnote.utils.StringAppendUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/file")
public class FileController {
	
	@Autowired
	private MysqlBackupService mysqlBackupService;
	
	@Logger(message="下载文件数据库备份文件")
	@RequestMapping(value = "/downloadDB", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadDB(String filePath, HttpServletRequest request, HttpServletResponse response){
		try {
			DataBaseInfo dataBaseInfo = mysqlBackupService.getDataBaseInfo();
			filePath = StringAppendUtils.append(dataBaseInfo.getPath(),File.separator,filePath);
			File file = new File(filePath);
			HttpHeaders header = new HttpHeaders();
			String fileName = new String(StringAppendUtils.append(DateUtils.formatDate3(),"备份数据库文件.sql").getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
			header.setContentDispositionFormData("attachment",fileName);
			header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),header,HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("数据库备份文件下载失败！错误信息：{}",e.getMessage());
		}
		return null;
	}

}
