package com.sxkl.cloudnote.webchat.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * WebScoket配置处理器
 * 
 * @author Goofy
 * @Date 2015年6月11日 下午1:15:09
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

	@Autowired
	private MyWebSocketHandler myWebsocketHandler;
	@Autowired
	private MyWebsocketInterceptor myWebsocketInterceptor;

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(myWebsocketHandler, "/mywebsocket").addInterceptors(myWebsocketInterceptor);// .setAllowedOrigins("*")/*不加AllowedOrigins，有可能会全拒绝*/;
		registry.addHandler(myWebsocketHandler, "/mywebsocket/sockjs").addInterceptors(myWebsocketInterceptor).withSockJS(); /* 用于支持SockJS */
	}
	
	@Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }

}
