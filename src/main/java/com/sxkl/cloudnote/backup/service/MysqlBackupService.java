package com.sxkl.cloudnote.backup.service;

import com.sxkl.cloudnote.backup.entity.DataBaseInfo;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.mail.entity.Mail;
import com.sxkl.cloudnote.mail.entity.MailMessage;
import com.sxkl.cloudnote.mail.entity.MailUser;
import com.sxkl.cloudnote.mail.service.MailService;
import com.sxkl.cloudnote.utils.DESUtil;
import com.sxkl.cloudnote.utils.DateUtils;
import com.sxkl.cloudnote.utils.MapToBeanUtils;
import com.sxkl.cloudnote.utils.StringAppendUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

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
//            mysqldump --login-path=mysql_key cloudnote > /home/wy/backup/cloudnote2.sql
            StringBuilder sb = new StringBuilder();
            sb.append("mysqldump ");
            sb.append("--login-path=mysql_key ");
            sb.append(dataBaseInfo.getSchema());
            sb.append(" ");
            sb.append("> ");
            sb.append(savePath+dataBaseInfo.getFilename());
            Runtime cmd = Runtime.getRuntime();
            try {
                Process p = cmd.exec(sb.toString());
                if(p.waitFor() == 0){
                    p.destroy();
                }
            } catch (IOException e) {
                e.printStackTrace();
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
