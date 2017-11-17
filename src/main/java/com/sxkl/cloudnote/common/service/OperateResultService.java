package com.sxkl.cloudnote.common.service;

import java.util.List;

import com.google.gson.Gson;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.entity.ArticleForHtml;
import com.sxkl.cloudnote.common.entity.OperateResult;

public class OperateResultService {
	
	public static String configurateSuccessResult(){
		Gson gson = new Gson();
		OperateResult operateResult = new OperateResult();
		operateResult.setMsg("操作成功");
		operateResult.setStatus(true);
		return gson.toJson(operateResult);
	}
	
	public static String configurateSuccessResult(Object data){
		Gson gson = new Gson();
		OperateResult operateResult = new OperateResult();
		operateResult.setMsg("操作成功");
		operateResult.setStatus(true);
		operateResult.setData(data);
		return gson.toJson(operateResult);
	}
	
	public static String configurateFailureResult(){
		Gson gson = new Gson();
		OperateResult operateResult = new OperateResult();
		operateResult.setMsg("操作失败");
		operateResult.setStatus(false);
		return gson.toJson(operateResult);
	}
	
	public static String configurateFailureResult(String err){
		Gson gson = new Gson();
		OperateResult operateResult = new OperateResult();
		operateResult.setMsg("操作失败！"+err);
		operateResult.setStatus(false);
		return gson.toJson(operateResult);
	}

	public static String configurateSuccessDataGridResult(List articles, int total) {
		Gson gson = new Gson();
		OperateResult operateResult = new OperateResult();
		operateResult.setMsg("操作成功");
		operateResult.setStatus(true);
		operateResult.setData(articles);
		operateResult.setTotal(total);
		return gson.toJson(operateResult);
	}
}
