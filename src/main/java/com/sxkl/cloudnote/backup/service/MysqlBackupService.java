package com.sxkl.cloudnote.backup.service;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.sxkl.cloudnote.backup.entity.DataBaseInfo;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.mail.entity.Mail;
import com.sxkl.cloudnote.mail.entity.MailMessage;
import com.sxkl.cloudnote.mail.entity.MailUser;
import com.sxkl.cloudnote.mail.service.MailService;
import com.sxkl.cloudnote.utils.*;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangyao
 * Date 2017/12/6.
 */
@Slf4j
@Service
public class MysqlBackupService implements DataBaseBackService {

    @Autowired
    private MailService mailService;

    @Override
    public boolean backup(DataBaseInfo dataBaseInfo) {
        String savePath = dataBaseInfo.getPath();
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if(!savePath.endsWith(File.separator)){
            savePath = savePath + File.separator;
        }
        try {
            DESUtil desUtil = new DESUtil();
            @Cleanup
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(savePath + dataBaseInfo.getFilename()), "utf8"));
            String comand = " mysqldump -h " + dataBaseInfo.getIp() + " -u " + dataBaseInfo.getUsername() + " -p " + desUtil.decrypt(dataBaseInfo.getPassword()) + " --set-charset=UTF8 " + dataBaseInfo.getSchema();
            System.out.println(comand);
            Process process = Runtime.getRuntime().exec(comand);
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
            @Cleanup
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while((line = bufferedReader.readLine())!= null){
                printWriter.println(line);
            }
            printWriter.flush();
            if(process.waitFor() == 0){//0 表示线程正常终止。
                return true;
            }
            sendMail(savePath + dataBaseInfo.getFilename());
        }catch (Exception e) {
            log.error("备份数据库失败！错误信息:{}",e.getMessage());
        }
        return false;
    }

    private void sendMail(String draft) {
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

    @Override
    public DataBaseInfo getDataBaseInfo() {
        DataBaseInfo dataBaseInfo = new DataBaseInfo();
        MapToBeanUtils<DataBaseInfo> mapToBeanUtils = new MapToBeanUtils<DataBaseInfo>();
        mapToBeanUtils.mapToBean(dataBaseInfo,"jdbc.");
        dataBaseInfo.setFilename(DateUtils.formatDate4()+ Constant.SQL_FILE_EXTENSION);
        return dataBaseInfo;
    }
}
