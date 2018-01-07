package com.sxkl.cloudnote.webchat.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.TextMessage;

import com.google.gson.GsonBuilder;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.UserUtil;
import com.sxkl.cloudnote.webchat.entity.Message;
import com.sxkl.cloudnote.webchat.service.MessageService;
import com.sxkl.cloudnote.webchat.websocket.MyWebSocketHandler;

@Controller
@RequestMapping("/msg")
public class MsgController {

	@Resource
	MyWebSocketHandler handler;
	@Autowired
	private MessageService messageService;

	// 跳转到发布广播页面
	@RequestMapping(value = "broadcast", method = RequestMethod.GET)
	public ModelAndView broadcast() {
		return new ModelAndView("broadcast");
	}

	// 发布系统广播（群发）
	@ResponseBody
	@RequestMapping(value = "broadcast", method = RequestMethod.POST)
	public void broadcast(String text) throws IOException {
		Message msg = new Message();
		msg.setDate(new Date());
//		msg.setFrom(-1L);
//		msg.setFromName("系统广播");
//		msg.setTo(0L);
		msg.setText(text);
		handler.broadcast(new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
	}
	
	@RequestMapping(value = "/getHistory", method = RequestMethod.POST)
	@ResponseBody
	public List<Message> getHistory(String userTo, HttpServletRequest request){
		User user = UserUtil.getSessionUser(request);
		return messageService.getHistory(user.getId(),userTo);
	}
}