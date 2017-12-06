package com.sxkl.cloudnote.mail.service;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.sun.mail.util.MailSSLSocketFactory;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.mail.entity.Mail;
import com.sxkl.cloudnote.mail.entity.MailMessage;
import com.sxkl.cloudnote.mail.entity.MailUser;
import com.sxkl.cloudnote.utils.DESUtil;
import com.sxkl.cloudnote.utils.DateUtils;
import com.sxkl.cloudnote.utils.MapToBeanUtils;
import com.sxkl.cloudnote.utils.PropertyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

/**
 * Created by wangyao
 * Date 2017/12/6.
 */
@Slf4j
@Service
public class MailService {

    public void sendMail(Mail mail){
        try{
            MailUser toUser = mail.getToUser();
            MailUser fromUser = mail.getFromUser();
            MailMessage mailMessage = mail.getMessage();
            String host = "smtp.qq.com"; //QQ 邮件服务器
            // 获取系统属性
            Properties properties = System.getProperties();
            // 设置邮件服务器
            properties.setProperty("mail.smtp.host", host);
            properties.put("mail.smtp.auth", "true");
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);
            // 获取默认session对象
            Session session = Session.getDefaultInstance(properties,new Authenticator(){
                public PasswordAuthentication getPasswordAuthentication(){
                    DESUtil desUtil = new DESUtil();
                    //qq邮箱服务器账户、第三方登录授权码
                    try{
                        return new PasswordAuthentication(fromUser.getUsername(), desUtil.decrypt(fromUser.getPassword())); //发件人邮件用户名、密码
                    }catch (Exception e){
                        log.error("创建PasswordAuthentication失败！错误信息:{}",e.getMessage());
                    }
                    return null;
                }
            });
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);
            // Set From: 头部头字段
            message.setFrom(new InternetAddress(fromUser.getUsername()));
            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser.getUsername()));
            // Set Subject: 主题文字
            message.setSubject(mailMessage.getSubject());
            // 创建消息部分
            BodyPart messageBodyPart = new MimeBodyPart();
            // 消息
            messageBodyPart.setText(mailMessage.getContent());
            // 创建多重消息
            Multipart multipart = new MimeMultipart();
            // 设置文本消息部分
            multipart.addBodyPart(messageBodyPart);
            for(String draft : mailMessage.getDrafts()){
                // 附件部分
                messageBodyPart = new MimeBodyPart();
                //设置要发送附件的文件路径
                String filename = draft;
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                //messageBodyPart.setFileName(filename);
                //处理附件名称中文（附带文件路径）乱码问题
                messageBodyPart.setFileName(MimeUtility.encodeText(filename));
                multipart.addBodyPart(messageBodyPart);
            }
            // 发送完整消息
            message.setContent(multipart );
            // 发送消息
            Transport.send(message);
            log.info("发送邮件完成！");
        }catch (Exception mex) {
            log.error("发送邮件失败！错误信息:{}",mex.getMessage());
        }
    }

    public MailUser getSystemMailFromUser(){
        MailUser fromuser = new MailUser();
        MapToBeanUtils<MailUser> mapToBeanUtils = new MapToBeanUtils<MailUser>();
        mapToBeanUtils.mapToBean(fromuser,"mail.from.");
        try{
            DESUtil desUtil = new DESUtil();
            fromuser.setPassword(desUtil.decrypt(fromuser.getPassword()));
        }catch (Exception e){
            log.error("解密用户密码失败！错误信息:{}",e.getMessage());
        }
        return fromuser;
    }

    public MailUser getSystemMailToUser(){
        MailUser touser = new MailUser();
        MapToBeanUtils<MailUser> mapToBeanUtils = new MapToBeanUtils<MailUser>();
        mapToBeanUtils.mapToBean(touser,"mail.from.");
        return touser;
    }
}
