package com.sxkl.cloudnote.webchat.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.webchat.dao.MessageDao;
import com.sxkl.cloudnote.webchat.entity.Message;

@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;

    @Logger(message = "保存在线聊天消息")
    public void saveMessage(Message message) {
        messageDao.save(message);
    }

    @Logger(message = "获取在线聊天历史消息")
    public List<Message> getHistory(String userFrom, String userTo) {
        List<Message> msgs = messageDao.getHistory(userFrom, userTo);
        Collections.reverse(msgs);
        return msgs;
    }
}
