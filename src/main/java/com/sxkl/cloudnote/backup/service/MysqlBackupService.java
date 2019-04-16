package com.sxkl.cloudnote.backup.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.sxkl.cloudnote.image.entity.Image;
import com.sxkl.cloudnote.image.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Ordering;
import com.sxkl.cloudnote.backup.entity.DataBaseInfo;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.mail.entity.Mail;
import com.sxkl.cloudnote.mail.entity.MailMessage;
import com.sxkl.cloudnote.mail.entity.MailUser;
import com.sxkl.cloudnote.mail.service.MailService;
import com.sxkl.cloudnote.utils.DateUtils;
import com.sxkl.cloudnote.utils.MapToBeanUtils;
import com.sxkl.cloudnote.utils.StringAppendUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by wangyao
 * Date 2017/12/6.
 */
@Slf4j
@Service
public class MysqlBackupService extends AbstractBackupDB {

    @Autowired
    private MailService mailService;
    @Autowired
    private ImageService imageService;

    @Logger(message="获取数据库信息")
    @Override
    public DataBaseInfo getDataBaseInfo() {
        DataBaseInfo dataBaseInfo = new DataBaseInfo();
        MapToBeanUtils<DataBaseInfo> mapToBeanUtils = new MapToBeanUtils<DataBaseInfo>();
        mapToBeanUtils.mapToBean(dataBaseInfo,"jdbc.");
        dataBaseInfo.setFilename(DateUtils.formatDate4()+ Constant.SQL_FILE_EXTENSION);
        return dataBaseInfo;
    }

    @Logger(message="备份数据库")
	@Override
	public String backup(DataBaseInfo dataBaseInfo) {
//        etlImage();
		String savePath = dataBaseInfo.getPath();
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        if(!savePath.endsWith(File.separator)){
            savePath = savePath + File.separator;
        }
        Process p =  null;
        String path = savePath + dataBaseInfo.getFilename();
        try {
//            mysqldump --login-path=mysql_key cloudnote > /home/wy/backup/cloudnote2.sql
            StringBuilder sb = new StringBuilder();
            sb.append("mysqldump ");
            sb.append("--login-path=mysql_key ");
            sb.append(dataBaseInfo.getSchema());
            sb.append(" ");
            sb.append("> ");
            sb.append(path);
            Runtime cmd = Runtime.getRuntime();
            String[] cmds = new String[]{"sh","-c",sb.toString()};
            p = cmd.exec(cmds);
            p.waitFor();
            log.info("备份数据库成功!");
        }catch (Exception e) {
            log.error("备份数据库失败！错误信息:{}",e.getMessage());
        }finally{
        	p.destroy();
        }
		return path;
	}

    @Logger(message="etlImage")
    public void etlImage() {
        List<Image> images = imageService.getAll();
        images.forEach(image -> {
            try {
                Image temp = imageService.getOne(image.getId());
                imageService.saveToDisk(temp.getName(), temp.getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        log.info("etlImage成功!");
    }

    @Logger(message="调用邮件服务")
	@Override
	public void sendMail(String draft) {
		MailUser fromuser = mailService.getSystemMailFromUser();
        MailUser touser = mailService.getSystemMailToUser();
        MailMessage message = new MailMessage();
        message.setSubject("曼妙云端数据库备份");
        message.setContent(StringAppendUtils.append("于",DateUtils.formatDate2Str(new Date()),"曼妙云端数据库备份成功！","路径为:",draft));
        message.setDrafts(new String[]{draft});
        Mail mail = new Mail();
        mail.setFromUser(fromuser);
        mail.setToUser(touser);
        mail.setMessage(message);
        mailService.sendMail(mail);
	}

    @Logger(message="删除数据库过期的备份文件")
	@Override
	public void deleteExpireFile(String path) {
		path = path.substring(0,path.lastIndexOf("/"));
		File file = new File(path);
		List<File> fileList = new ArrayList<File>();
		if(file.isDirectory()){
			File []files = file.listFiles();
			for(File fileIndex : files){
				fileList.add(fileIndex);
		    }
		}
		Ordering<File> fileOrdering = new Ordering<File>() {
			public int compare(File left, File right) {
				return left.getName().compareTo(right.getName());
			}
		};
		List<File> latestFiles = fileOrdering.greatestOf(fileList, 3);
		fileList.removeAll(latestFiles);
		Iterator<File> files = fileList.iterator();
		while(files.hasNext()){
			File delFile = files.next();
			String filename = delFile.getName();
			if(delFile.delete()){
				log.info("删除备份数据文件:{}",filename);
			}
		}
	}
}
