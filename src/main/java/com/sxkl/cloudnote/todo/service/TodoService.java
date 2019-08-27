package com.sxkl.cloudnote.todo.service;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.common.entity.Page;
import com.sxkl.cloudnote.common.entity.PageResult;
import com.sxkl.cloudnote.common.service.BaseService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.todo.dao.TodoDao;
import com.sxkl.cloudnote.todo.entity.Todo;
import com.sxkl.cloudnote.todo.entity.TodoDateTreeNode;
import com.sxkl.cloudnote.user.dao.UserDao;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.DateUtils;
import com.sxkl.cloudnote.utils.ObjectUtils;
import com.sxkl.cloudnote.utils.UserUtil;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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

    @Logger(message = "获取日期树")
    public String getDateTree(HttpServletRequest request) {
        Todo todo = todoDao.findEarlestTodo();
        LocalDate earlest = ObjectUtils.isNull(todo) ? LocalDate.now() : DateUtils.convertDateToLocalDate(todo.getCreateDateTime());
        if(ObjectUtils.isNull(earlest)) {
            earlest = LocalDate.now();
        }
        LocalDate nextYearDate = LocalDate.now().plusYears(1);
        List<TodoDateTreeNode> yearTreeNodes;
        if(earlest.isAfter(nextYearDate)) {
            yearTreeNodes = configureRnageYearTree(nextYearDate, earlest);
        }else {
            yearTreeNodes = configureRnageYearTree(earlest, nextYearDate);
        }
        return JSONArray.fromObject(yearTreeNodes).toString();
    }

    private List<TodoDateTreeNode> configureRnageYearTree(LocalDate start, LocalDate end) {
        TodoDateTreeNode rootNode = TodoDateTreeNode.getRootNode();
        List<TodoDateTreeNode> yearTreeNodes = Lists.newArrayList(rootNode);
        int startYear = start.getYear();
        int endYear = end.getYear();
        if(startYear == endYear) {
            yearTreeNodes.addAll(configureOneYearTree(startYear));
        }else {
            for(int year = startYear; year <= endYear; year++) {
                yearTreeNodes.addAll(configureOneYearTree(year));
            }
        }
        return yearTreeNodes;
    }

    private List<TodoDateTreeNode> configureOneYearTree(int year) {
        TodoDateTreeNode rootNode = TodoDateTreeNode.getYearNode(year);
        List<TodoDateTreeNode> yearTreeNodes = Lists.newArrayList(rootNode);
        yearTreeNodes.addAll(configureRangeMonthTree(year));
        return yearTreeNodes;
    }

    private List<TodoDateTreeNode> configureRangeMonthTree(int year) {
        List<TodoDateTreeNode> monthTreeNodes = Lists.newArrayList();
        for (int month = 1; month <= 12; month++) {
            monthTreeNodes.add(TodoDateTreeNode.getMonthNode(year, month));
        }
        return monthTreeNodes;
    }

    @Override
    protected BaseDao<String, Todo> getDao() {
        return todoDao;
    }
}