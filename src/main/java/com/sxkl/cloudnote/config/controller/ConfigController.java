package com.sxkl.cloudnote.config.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sxkl.cloudnote.config.service.ConfigService;

/**
 * @author: wangyao
 * @date: 2018年5月15日 下午3:06:46
 * @description: 
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

	@Autowired
	private ConfigService configService;
	
	@RequestMapping(value = "/addConfig/{mode}/{key}/{value}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public String addConfig(@PathVariable("mode")String mode,  @PathVariable("key")String key, @PathVariable("value")String value, HttpServletRequest request){
		return configService.addConfig(mode,key,value,request);
	}
	
	@RequestMapping(value = "/deleteConfig/{mode}/{key}/", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public String deleteConfig(@PathVariable("mode")String mode,  @PathVariable("key")String key, HttpServletRequest request){
		return configService.deleteConfig(mode,key,request);
	}
	
}
