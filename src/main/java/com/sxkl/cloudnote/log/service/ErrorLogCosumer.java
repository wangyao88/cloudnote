package com.sxkl.cloudnote.log.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Charsets;
import com.sxkl.cloudnote.log.dao.LogDao;
import com.sxkl.cloudnote.log.entity.Log;
import com.sxkl.cloudnote.utils.JSONObjectUtils;

import net.sf.json.JSONObject;

/**
 * @author: wangyao
 * @date: 2018年5月14日 下午5:12:17
 * @description: 
 */
@Service
public class ErrorLogCosumer implements MessageListener {
	
	@Autowired
	private LogDao logDao;
	
    public void onMessage(Message message) {
    	try {
    		String logStr = new String(message.getBody(),Charsets.UTF_8);
    		JSONObject json = JSONObjectUtils.toJson(logStr);
        	Log log = (Log) JSONObject.toBean(json, Log.class);
        	logDao.save(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}