package com.sxkl.cloudnote.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sxkl.cloudnote.config.service.ConfigService;

/**
 * @author: wangyao
 * @date: 2018年5月15日 下午3:06:46
 * @description: 
 */
@Controller
@RequestMapping("/config")
public class ConfigController {

	@Autowired
	private ConfigService configService;
	
}
