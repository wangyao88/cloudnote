package com.sxkl.cloudnote.quicktext.service;

import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.quicktext.dao.QuickTextDao;
import com.sxkl.cloudnote.quicktext.entity.QuickText;
import com.sxkl.cloudnote.user.dao.UserDao;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.StringUtils;
import com.sxkl.cloudnote.utils.UUIDUtil;
import com.sxkl.cloudnote.utils.UserUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class QuickTextService {

    private static final String DEFAULT_TITLE = "新建QuickText";

    @Autowired
    private QuickTextDao quickTextDao;

    @Autowired
    private UserDao userDao;

    @Logger(message = "保存quickText")
    public String insert(HttpServletRequest request) {
        String content = request.getParameter("content");
        QuickText quickText = new QuickText();
        quickText.setContent(content);
        User sessionUser = UserUtil.getSessionUser(request);
        User user = userDao.selectUser(sessionUser);
        quickText.setUserId(user.getId());
        quickText.setCreateDateTime(new Date());
        quickText.setUpdateDateTime(new Date());
        String title = configureTitle(content);
        quickText.setTitle(title);
        String uuid = UUIDUtil.getUUID();
        quickText.setId(uuid);
        quickTextDao.save(quickText);
        return uuid;
    }

    private String configureTitle(String content) {
        Document document= Jsoup.parse(content);
        String text = document.text();
        if(StringUtils.isBlank(text)) {
            return DEFAULT_TITLE;
        }
        if(text.length() > 10) {
            return text.substring(0, 9);
        }
        return text;
    }


    @Logger(message = "删除指定quickText")
    public void delete(HttpServletRequest request) {
        String id = request.getParameter("id");
        QuickText quickText = quickTextDao.findOne(id);
        quickTextDao.delete(quickText);
    }

    @Logger(message = "更新指定quickText")
    public String update(HttpServletRequest request) {
        String id = request.getParameter("id");
        String content = request.getParameter("content");
        QuickText quickTextInDB = quickTextDao.findOne(id);
        quickTextInDB.setContent(content);
        quickTextInDB.setUpdateDateTime(new Date());
        String title = configureTitle(content);
        quickTextInDB.setTitle(title);
        quickTextDao.update(quickTextInDB);
        return id;
    }

    @Logger(message = "根据主键获取quickText")
    public QuickText findOne(HttpServletRequest request) {
        String id = request.getParameter("id");
        return quickTextDao.findOne(id);
    }

    @Logger(message = "获取用户关联的所有quickText")
    public List<QuickText> findAll(HttpServletRequest request) {
        User sessionUser = UserUtil.getSessionUser(request);
        User user = userDao.selectUser(sessionUser);
        return quickTextDao.findAll(user.getId());
    }
}