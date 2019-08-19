package com.sxkl.cloudnote.todo.service;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.common.entity.Page;
import com.sxkl.cloudnote.common.entity.PageResult;
import com.sxkl.cloudnote.common.service.BaseService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.todo.dao.TodoDao;
import com.sxkl.cloudnote.todo.entity.Todo;
import com.sxkl.cloudnote.user.dao.UserDao;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class TodoService extends BaseService<String, Todo> {

    @Autowired
    private TodoDao todoDao;

    @Autowired
    private UserDao userDao;

    @Logger(message = "保存todo")
    public void insert(HttpServletRequest request, Todo todo) {
        User sessionUser = UserUtil.getSessionUser(request);
        User user = userDao.selectUser(sessionUser);
        todo.setUserId(user.getId());
        todo.setCreateDateTime(new Date());
        todoDao.save(todo);
    }

    @Logger(message = "删除指定todo")
    public void delete(HttpServletRequest request) {
        String id = request.getParameter("id");
        todoDao.deleteById(id);
    }

    @Logger(message = "更新指定todo")
    public void update(HttpServletRequest request, Todo todo) {
        todoDao.update(todo);
    }

    @Logger(message = "根据主键获取todo")
    public Todo findOne(HttpServletRequest request) {
        String id = request.getParameter("id");
        return todoDao.findOne(id);
    }

    @Logger(message = "分页查询todo")
    @Override
    public PageResult<Todo> findPage(Page page, Todo todo) {
        return getDao().findPage(page, todo);
    }

    @Override
    protected BaseDao<String, Todo> getDao() {
        return todoDao;
    }
}