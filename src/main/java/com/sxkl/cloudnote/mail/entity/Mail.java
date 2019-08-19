package com.sxkl.cloudnote.mail.entity;

import lombok.Data;

/**
 * Created by wangyao
 * Date 2017/12/6.
 */
@Data
public class Mail {

    private MailUser fromUser;
    private MailUser toUser;
    private MailMessage message;
}
