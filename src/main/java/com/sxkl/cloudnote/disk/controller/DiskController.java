package com.sxkl.cloudnote.disk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sxkl.cloudnote.disk.service.DiskInfoService;
import com.sxkl.cloudnote.disk.service.FileInfoService;
import com.sxkl.cloudnote.main.entity.TreeNode;

@Controller
@RequestMapping("/disk")
public class DiskController {
	
	@Autowired
	private DiskInfoService disInfokService;
	@Autowired
	private FileInfoService fileInfoService;
	
	@RequestMapping("/index")
	public ModelAndView index(){
		ModelAndView modelAndView = new ModelAndView("disk/index");
		return modelAndView;
	}
	
	@RequestMapping("/getTree")
	@ResponseBody
	public List<TreeNode> getTree(){
		return disInfokService.getTree();
	}
	
}
