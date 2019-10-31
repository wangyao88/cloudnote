package com.sxkl.cloudnote.todo.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.common.entity.ComBoxNode;
import com.sxkl.cloudnote.common.entity.Page;
import com.sxkl.cloudnote.common.entity.PageResult;
import com.sxkl.cloudnote.common.service.BaseService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.statistic.model.DateRange;
import com.sxkl.cloudnote.statistic.model.KeyValue;
import com.sxkl.cloudnote.todo.dao.TodoDao;
import com.sxkl.cloudnote.todo.entity.Todo;
import com.sxkl.cloudnote.todo.entity.TodoDateTreeNode;
import com.sxkl.cloudnote.user.dao.UserDao;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.*;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
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
        Date date = new Date();
        todo.setCreateDateTime(date);
        todo.setUpdateDateTime(date);
        todoDao.save(todo);
    }

    @Logger(message = "删除指定todo")
    public void delete(HttpServletRequest request) {
        String id = request.getParameter("id");
        Todo todo = todoDao.findOne(id);
        todoDao.delete(todo);
    }

    @Logger(message = "更新指定todo")
    public void update(HttpServletRequest request, Todo todo) {
        Todo todoInDB = todoDao.findOne(todo.getId());
        todoInDB.setStatus(todo.getStatus());
        todoInDB.setContent(todo.getContent());
        todoInDB.setBeginDateTime(todo.getBeginDateTime());
        todoInDB.setEndDateTime(todo.getEndDateTime());
        todoDao.update(todoInDB);
    }

    @Logger(message = "根据主键获取todo")
    public Todo findOne(HttpServletRequest request) {
        String id = request.getParameter("id");
        if(StringUtils.isBlank(id)) {
            return null;
        }
        return todoDao.findOne(id);
    }

    @Logger(message = "分页查询todo")
    @Override
    public PageResult<Todo> findPage(Page page, Todo todo) {
        return getDao().findPage(page, todo);
    }

    @Logger(message = "获取日期树")
    public String getDateTree(HttpServletRequest request) {
        User sessionUser = UserUtil.getSessionUser(request);
        User user = userDao.selectUser(sessionUser);
        Todo todo = todoDao.findEarlestTodo(user.getId());
        LocalDate earlest = ObjectUtils.isNotNull(todo) && ObjectUtils.isNotNull(todo.getBeginDateTime()) ? DateUtils.convertDateToLocalDate(todo.getBeginDateTime()) : LocalDate.now();
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

    @Logger(message = "获取所有todo")
    public String findAll(HttpServletRequest request) {
        Todo todo = RequestUtils.requestToBean(Todo.class, request);
        User sessionUser = UserUtil.getSessionUser(request);
        User user = userDao.selectUser(sessionUser);
        todo.setUserId(user.getId());
        String nodeText = todo.getNodeText();
        String nodeId = todo.getNodeId();
        if(StringUtils.isNotBlank(nodeText) && nodeText.contains("年")) {
            Date startDate = DateUtils.formatStr2Date(nodeId + "-01-01", "yyyy-MM-dd");
            todo.setStartDate(startDate);
            LocalDate localDate = DateUtils.convertDateToLocalDate(startDate);
            Date endDate = DateUtils.convertLocalDateToDate(localDate.plusYears(1));
            todo.setEndDate(endDate);
        }
        if(StringUtils.isNotBlank(nodeText) && nodeText.contains("月")) {
            String date = StringUtils.append("-", nodeId.substring(0, 4), nodeId.substring(4, nodeId.length()), "01");
            Date startDate = DateUtils.formatStr2Date(date, "yyyy-MM-dd");
            todo.setStartDate(startDate);
            LocalDate localDate = DateUtils.convertDateToLocalDate(startDate);
            Date endDate = DateUtils.convertLocalDateToDate(localDate.plusMonths(1));
            todo.setEndDate(endDate);
        }
        List<Todo> todos = todoDao.findAllByExample(todo);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return gson.toJson(todos);

    }

    @Logger(message = "获取Todo的状态信息")
    public List<ComBoxNode> getStatus(HttpServletRequest request) {
        return Lists.newArrayList(new ComBoxNode("未完成", "未完成"), new ComBoxNode("已完成", "已完成"));
    }

    @Override
    protected BaseDao<String, Todo> getDao() {
        return todoDao;
    }

    public List<KeyValue> getLineData(String userId) {
        DateRange dateRange = DateUtils.getCurrentYearDateRange();
        return todoDao.getLineData(userId, dateRange);
    }
}