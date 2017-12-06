package com.sxkl.cloudnote.mail.entity;

import lombok.Data;
/**
 * Created by wangyao
 * Date 2017/12/6.
 */
@Data
public class MailMessage {

    private String subject;
    private String content;
    private String[] drafts;
}
