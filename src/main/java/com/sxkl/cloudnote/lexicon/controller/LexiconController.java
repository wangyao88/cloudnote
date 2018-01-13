package com.sxkl.cloudnote.lexicon.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sxkl.cloudnote.common.entity.Page;
import com.sxkl.cloudnote.lexicon.entity.ExtLexicon;
import com.sxkl.cloudnote.lexicon.entity.KeyLexicon;
import com.sxkl.cloudnote.lexicon.entity.Lexicon;
import com.sxkl.cloudnote.lexicon.entity.StopLexicon;
import com.sxkl.cloudnote.lexicon.service.LexiconService;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.StringAppendUtils;
import com.sxkl.cloudnote.utils.UserUtil;

/**
 * @author wangyao
 * @date 2018年1月13日 下午5:34:47
 * @description:
 */
@RestController
@RequestMapping("/lexicon")
public class LexiconController {
	
	@Autowired
	private LexiconService lexiconService;
	
	@RequestMapping("/saveExtLexicon")
	@ResponseBody
	public void saveExtLexicon(String name, HttpServletRequest request){
		User user = UserUtil.getSessionUser(request);
		Lexicon lexicon = new ExtLexicon();
		lexicon.setName(name);
		lexicon.setUserId(user.getId());
		lexiconService.save(lexicon);
	}
	
	@RequestMapping("/saveStopLexicon")
	@ResponseBody
	public void saveStopLexicon(String name, HttpServletRequest request){
		User user = UserUtil.getSessionUser(request);
		Lexicon lexicon = new StopLexicon();
		lexicon.setName(name);
		lexicon.setUserId(user.getId());
		lexiconService.save(lexicon);
	}
	
	@RequestMapping("/saveKeyLexicon")
	@ResponseBody
	public void saveKeyLexicon(String name, HttpServletRequest request){
		User user = UserUtil.getSessionUser(request);
		Lexicon lexicon = new KeyLexicon();
		lexicon.setName(name);
		lexicon.setUserId(user.getId());
		lexiconService.save(lexicon);
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(String id){
		lexiconService.deleteById(id);
	}
	
	@RequestMapping("/findAllExt")
	@ResponseBody
	public List<Lexicon> findAllExt(HttpServletRequest request){
		return findPage("StopLexicon",request);
	}
	
	@RequestMapping("/findAllStop")
	@ResponseBody
	public List<Lexicon> findAllStop(HttpServletRequest request){
		return findPage("StopLexicon",request);
	}
	
	@RequestMapping("/findAllKey")
	@ResponseBody
	public List<Lexicon> findAllKey(HttpServletRequest request){
		return findPage("KeyLexicon",request);
	}

	private List<Lexicon> findPage(String entityName, HttpServletRequest request) {
		Page page = lexiconService.getPage(request);
		String hql = StringAppendUtils.append("from ",entityName," e where e.userId='",page.getUserId(),"'");
		page.setHql(hql);
		return lexiconService.findPage(page);
	}
}
