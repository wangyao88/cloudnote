
package com.sxkl.cloudnote.webchat.websocket;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import lombok.extern.slf4j.Slf4j;


/**
 * Socket建立连接（握手）和断开
 * 
 * @author Goofy
 * @Date 2015年6月11日 下午2:23:09
 */
@Slf4j
@Component
public class MyWebsocketInterceptor implements HandshakeInterceptor {

	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
		String uid = ((ServletServerHttpRequest) request).getServletRequest().getParameter("uid");
		log.info("Websocket:用户[ID:" + uid + "]已经建立连接");
		if (request instanceof ServletServerHttpRequest) {
			if(StringUtils.isEmpty(uid)){
			    return false;
			}
			attributes.put("uid", uid);
		}
		return true;
	}

	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
		
	}

}
