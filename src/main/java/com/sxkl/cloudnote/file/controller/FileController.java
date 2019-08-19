package com.sxkl.cloudnote.file.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sxkl.cloudnote.backup.entity.DataBaseInfo;
import com.sxkl.cloudnote.backup.service.MysqlBackupService;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.utils.DateUtils;
import com.sxkl.cloudnote.utils.FileUtils;
import com.sxkl.cloudnote.utils.StringAppendUtils;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private MysqlBackupService mysqlBackupService;

//	@Logger(message="下载文件数据库备份文件")
//	@RequestMapping(value = "/downloadDB", method = RequestMethod.GET)
//	public ResponseEntity<byte[]> downloadDB(String filePath){
//		try {
//			DataBaseInfo dataBaseInfo = mysqlBackupService.getDataBaseInfo();
//			filePath = StringAppendUtils.append(dataBaseInfo.getPath(),File.separator,filePath);
//			String fileName = StringAppendUtils.append(DateUtils.formatDate3(),"备份数据库文件.sql");
//			return FileUtils.downloadFile(fileName, filePath);
//		} catch (Exception e) {
//			log.error("数据库备份文件下载失败！错误信息：{}",e.getMessage());
//		}
//		return null;
//	}

    @Logger(message = "下载文件数据库备份文件")
    @RequestMapping(value = "/downloadDB", method = RequestMethod.GET)
    public void downloadDB(String filePath, HttpServletResponse response) throws Exception {
        DataBaseInfo dataBaseInfo = mysqlBackupService.getDataBaseInfo();
        filePath = StringAppendUtils.append(dataBaseInfo.getPath(), File.separator, filePath);
        String fileName = StringAppendUtils.append(DateUtils.formatDate3(), "备份数据库文件.sql");
        FileUtils.downloadBigFile(fileName, filePath, response);
    }

    @Logger(message = "下载笔记附件")
    @RequestMapping(value = "/downloadDraft", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadDraft(String filepath, String title, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        String projectPath = StringAppendUtils.append(request.getSession().getServletContext().getRealPath(File.separator), Constant.DRAFT_PATH_PREFIX);
        filepath = filepath.replaceAll(Constant.SLASH, Constant.FILE_SEPARATOR);
        String realPath = StringAppendUtils.append(projectPath, filepath);
        return FileUtils.downloadFile(title, realPath);
    }
}
