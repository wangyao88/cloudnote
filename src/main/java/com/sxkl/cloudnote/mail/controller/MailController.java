package com.sxkl.cloudnote.mail.controller;

import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.mail.entity.Mail;
import com.sxkl.cloudnote.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wangyao
 * Date 2017/12/6.
 */

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @RequestMapping(value = "/sendMail", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String sendMail(HttpServletRequest request) {
        try {
            Mail mail = new Mail();
            mailService.sendMail(mail);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }
}
