package com.sxkl.cloudnote.log.service;

import com.google.common.base.Charsets;
import com.sxkl.cloudnote.log.dao.LogDao;
import com.sxkl.cloudnote.log.entity.Log;
import com.sxkl.cloudnote.utils.JSONObjectUtils;
import net.sf.json.JSONObject;
import org.springframework.amqp.core.Message;

public abstract class BaseLogCosumer {

    protected void onMessage(Message message) {
        try {
            String logStr = new String(message.getBody(), Charsets.UTF_8);
            JSONObject json = JSONObjectUtils.toJson(logStr);
            Log log = (Log) JSONObject.toBean(json, Log.class);
            getLogDao().save(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract LogDao getLogDao();
}
