package com.sxkl.cloudnote.todo.controller;

import com.sxkl.cloudnote.common.entity.Page;
import com.sxkl.cloudnote.common.entity.PageResult;
import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.todo.entity.Todo;
import com.sxkl.cloudnote.todo.service.TodoService;
import com.sxkl.cloudnote.utils.PropertyUtil;
import com.sxkl.cloudnote.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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

    @Logger(message = "跳转到todo编辑页面")
    @RequestMapping(value = "/editPage", method = RequestMethod.GET)
    public String editPage() {
        return StringUtils.appendJoinEmpty("todo/editPage", "_", PropertyUtil.getMode());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String add(HttpServletRequest request, Todo todo) {
        try {
            todoService.insert(request, todo);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

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

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String update(HttpServletRequest request, Todo todo) {
        try {
            todoService.update(request, todo);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @RequestMapping(value = "/findOne", method = RequestMethod.GET)
    @ResponseBody
    public String findOne(HttpServletRequest request) {
        try {
            Todo todo = todoService.findOne(request);
            return OperateResultService.configurateSuccessResult(todo);
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    @ResponseBody
    public String findAll(HttpServletRequest request, Todo todo) {
        try {
            Page page = todoService.getPage(request);
            PageResult<Todo> pageResult = todoService.findPage(page, todo);
            return OperateResultService.configurateSuccessDataGridResult(pageResult.getDatas(), pageResult.getTotal());
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }
}