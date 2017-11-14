package com.sxkl.cloudnote.note.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.note.service.NoteService;

@RestController
@RequestMapping("/note")
public class NoteController {

	@Autowired
	private NoteService noteService;

	
	@RequestMapping(value = "/addNote", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String addNote(HttpServletRequest request){
		try {
			noteService.insertNote(request);
			return OperateResultService.configurateSuccessResult();
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/deleteNote", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String deleteNote(HttpServletRequest request){
		try {
			noteService.deleteNote(request);
			return OperateResultService.configurateSuccessResult();
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}
	
}
