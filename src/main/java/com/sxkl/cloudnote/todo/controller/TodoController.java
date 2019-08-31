package com.sxkl.cloudnote.todo.controller;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sxkl.cloudnote.common.entity.ComBoxNode;
import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.todo.entity.Todo;
import com.sxkl.cloudnote.todo.service.TodoService;
import com.sxkl.cloudnote.utils.ObjectUtils;
import com.sxkl.cloudnote.utils.PropertyUtil;
import com.sxkl.cloudnote.utils.StringUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Logger(message = "跳转到todo主页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return StringUtils.appendJoinEmpty("todo/index", "_", PropertyUtil.getMode());
    }

    @Logger(message = "保存todo")
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String add(HttpServletRequest request) {
        try {
            String jsonTodo = request.getParameter("todo");
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            Todo todo = gson.fromJson(jsonTodo, Todo.class);
            if(ObjectUtils.isNull(todo)) {
                return OperateResultService.configurateFailureResult("todo为空");
            }
            if(StringUtils.isBlank(todo.getId())) {
                todoService.insert(request, todo);
            }else {
                todoService.update(request, todo);
            }
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "删除todo")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(HttpServletRequest request) {
        try {
            todoService.delete(request);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "获取todo")
    @RequestMapping(value = "/findOne", method = RequestMethod.POST)
    @ResponseBody
    public String findOne(HttpServletRequest request) {
        try {
            Todo todo = todoService.findOne(request);
            return OperateResultService.configurateSuccessResult(todo);
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "获取所有的todo")
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    @ResponseBody
    public String findAll(HttpServletRequest request) {
        try {
            return todoService.findAll(request);
        } catch (Exception e) {
            return "[]";
        }
    }

    @Logger(message = "获取todo首页日期树")
    @RequestMapping(value = "/getDateTree", method = RequestMethod.POST)
    @ResponseBody
    public String getDateTree(HttpServletRequest request) {
        try {
            return todoService.getDateTree(request);
        } catch (Exception e) {
            return "[]";
        }
    }

    @Logger(message = "获取todo的所有可用状态信息")
    @RequestMapping(value = "/getStatus", method = RequestMethod.GET)
    @ResponseBody
    public List<ComBoxNode> getStatus(HttpServletRequest request) {
        try {
            return todoService.getStatus(request);
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }
}