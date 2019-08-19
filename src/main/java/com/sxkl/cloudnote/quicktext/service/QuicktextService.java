package com.sxkl.cloudnote.quicktext.service;

import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.quicktext.dao.QuicktextDao;
import com.sxkl.cloudnote.quicktext.entity.Quicktext;
import com.sxkl.cloudnote.user.dao.UserDao;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class QuicktextService {

    @Autowired
    private QuicktextDao quicktextDao;

    @Autowired
    private UserDao userDao;

    @Logger(message = "保存quicktext")
    public void insert(HttpServletRequest request, Quicktext quicktext) {
        User sessionUser = UserUtil.getSessionUser(request);
        User user = userDao.selectUser(sessionUser);
        quicktext.setUserId(user.getId());
        quicktext.setCreateDateTime(new Date());
        quicktextDao.save(quicktext);
    }

    @Logger(message = "删除指定quicktext")
    public void delete(HttpServletRequest request) {
        String id = request.getParameter("id");
        quicktextDao.deleteById(id);
    }

    @Logger(message = "更新指定quicktext")
    public void update(HttpServletRequest request, Quicktext quicktext) {
        Quicktext quicktextInDB = quicktextDao.findOne(quicktext.getId());
        quicktextInDB.setContent(quicktext.getContent());
        quicktextInDB.setUpdateDateTime(new Date());
        quicktextDao.update(quicktextInDB);
    }

    @Logger(message = "根据主键获取quicktext")
    public Quicktext findOne(HttpServletRequest request) {
        String id = request.getParameter("id");
        return quicktextDao.findOne(id);
    }

    @Logger(message = "获取用户关联的所有quicktext")
    public List<Quicktext> findAll(HttpServletRequest request) {
        User sessionUser = UserUtil.getSessionUser(request);
        User user = userDao.selectUser(sessionUser);
        return quicktextDao.findAll(user.getId());
    }
}