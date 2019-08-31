package com.sxkl.cloudnote.lexicon.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sxkl.cloudnote.log.annotation.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
 *  * @author wangyao
 *  * @date 2018年1月13日 下午5:34:47
 *  * @description: 词库控制层
 *  
 */
@Controller
@RequestMapping("/lexicon")
public class LexiconController {

    @Autowired
    private LexiconService lexiconService;

    @Logger(message = "跳转到词库主页")
    @RequestMapping("/lexicon")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("lexicon/lexicon");
        return modelAndView;
    }

    @Logger(message = "保存扩展词")
    @RequestMapping("/saveExtLexicon")
    @ResponseBody
    public void saveExtLexicon(String name, HttpServletRequest request) {
        User user = UserUtil.getSessionUser(request);
        Lexicon lexicon = new ExtLexicon();
        lexicon.setName(name);
        lexicon.setUserId(user.getId());
        lexiconService.save(lexicon);
    }

    @Logger(message = "保存停用词")
    @RequestMapping("/saveStopLexicon")
    @ResponseBody
    public void saveStopLexicon(String name, HttpServletRequest request) {
        User user = UserUtil.getSessionUser(request);
        Lexicon lexicon = new StopLexicon();
        lexicon.setName(name);
        lexicon.setUserId(user.getId());
        lexiconService.save(lexicon);
    }

    @Logger(message = "保存关键字")
    @RequestMapping("/saveKeyLexicon")
    @ResponseBody
    public void saveKeyLexicon(String name, HttpServletRequest request) {
        User user = UserUtil.getSessionUser(request);
        Lexicon lexicon = new KeyLexicon();
        lexicon.setName(name);
        lexicon.setUserId(user.getId());
        lexiconService.save(lexicon);
    }

    @Logger(message = "删除词库")
    @RequestMapping("/delete")
    @ResponseBody
    public void delete(String id) {
        lexiconService.deleteById(id);
    }

    @Logger(message = "分页查询扩展词列表")
    @RequestMapping("/findAllExt")
    @ResponseBody
    public List<Lexicon> findAllExt(HttpServletRequest request) {
        return findPage("ExtLexicon", request);
    }

    @Logger(message = "分页查询停用词列表")
    @RequestMapping("/findAllStop")
    @ResponseBody
    public List<Lexicon> findAllStop(HttpServletRequest request) {
        return findPage("StopLexicon", request);
    }

    @Logger(message = "分页查询关键字列表")
    @RequestMapping("/findAllKey")
    @ResponseBody
    public List<Lexicon> findAllKey(HttpServletRequest request) {
        return findPage("KeyLexicon", request);
    }

    private List<Lexicon> findPage(String entityName, HttpServletRequest request) {
        Page page = lexiconService.getPage(request);
        String hql = StringAppendUtils.append("from ", entityName, " e where e.userId='", page.getUserId(), "'");
        page.setHql(hql);
        return lexiconService.findPage(page);
    }

    @Logger(message = "停用关键词")
    @RequestMapping("/changeToStop")
    @ResponseBody
    public void changeToStop(String id, String name, HttpServletRequest request) {
        User user = UserUtil.getSessionUser(request);
        Lexicon lexicon = new StopLexicon();
        lexicon.setName(name);
        lexicon.setUserId(user.getId());
        lexiconService.changeToStop(id, lexicon);
    }

    @Logger(message = "初始化关键字")
    @RequestMapping("/initKey")
    @ResponseBody
    public void initKey(HttpServletRequest request) {
        User user = UserUtil.getSessionUser(request);
        lexiconService.initKey(user.getId());
    }
}