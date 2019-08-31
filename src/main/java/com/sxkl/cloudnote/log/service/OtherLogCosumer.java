package com.sxkl.cloudnote.log.service;

import com.sxkl.cloudnote.log.dao.LogDao;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: wangyao
 * @date: 2018年5月14日 下午5:12:17
 * @description:
 */
@Service
public class OtherLogCosumer extends BaseLogCosumer implements MessageListener {

    @Autowired
    private LogDao logDao;

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
    }

    @Override
    protected LogDao getLogDao() {
        return logDao;
    }
}