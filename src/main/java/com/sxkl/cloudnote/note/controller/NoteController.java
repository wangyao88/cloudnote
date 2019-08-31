package com.sxkl.cloudnote.note.controller;

import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.note.entity.Note;
import com.sxkl.cloudnote.note.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Logger(message = "添加笔记本")
    @RequestMapping(value = "/addNote", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String addNote(HttpServletRequest request) {
        try {
            noteService.insertNote(request);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "删除笔记本")
    @RequestMapping(value = "/deleteNote", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String deleteNote(HttpServletRequest request) {
        try {
            noteService.deleteNote(request);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "更新笔记本")
    @RequestMapping(value = "/updateNote", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String updateNote(HttpServletRequest request) {
        try {
            noteService.updateNote(request);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "获取笔记本信息")
    @RequestMapping(value = "/getNoteDataFromCombo", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getNoteDataFromCombo(HttpServletRequest request) {
        try {
            return noteService.getNoteDataFromCombo(request);
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "根据笔记主键查询笔记本")
    @RequestMapping(value = "/getNoteByArticleId", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getNoteByArticleId(HttpServletRequest request) {
        try {
            Note note = noteService.getNoteByArticleId(request);
            return OperateResultService.configurateSuccessResult(note);
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }
}