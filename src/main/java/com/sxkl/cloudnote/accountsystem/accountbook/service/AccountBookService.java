package com.sxkl.cloudnote.accountsystem.accountbook.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.sxkl.cloudnote.accountsystem.accountbook.dao.AccountBookDao;
import com.sxkl.cloudnote.accountsystem.accountbook.entity.AccountBook;
import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.UserUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author: wangyao
 * @date: 2018年5月8日 下午1:51:52
 * @description: 
 */
@Service
@Transactional(value = "transactionManager")
public class AccountBookService {
	
	@Autowired
	private AccountBookDao accountBookDao;

	@Logger(message="分页查询账本")
	public String getAccountBookList(HttpServletRequest request) {
		try {
			String pageIndex = request.getParameter("pageIndex");
			String pageSize = request.getParameter("pageSize");
			String name = request.getParameter("name");
			User sessionUser = UserUtil.getSessionUser(request);
			String userId = sessionUser.getId();
			List<AccountBook> accountBooks = accountBookDao.getAccountBookList(Integer.parseInt(pageIndex),Integer.parseInt(pageSize),userId,name);
			int total = accountBookDao.getAccountBookListCount(userId,name);
			return OperateResultService.configurateSuccessDataGridResult(accountBooks,total);
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}
	
	@Logger(message="获取所有账本")
	public String getAll(HttpServletRequest request) {
		User sessionUser = UserUtil.getSessionUser(request);
		String userId = sessionUser.getId();
		List<AccountBook> accountBooks = accountBookDao.findAllSimple(userId);
		Gson gson = new Gson();
		return gson.toJson(accountBooks);
	}

	@Logger(message="新增账本")
	public String add(AccountBook accountBook, HttpServletRequest request) {
		try {
			User user = UserUtil.getSessionUser(request);
			accountBook.setUser(user);
			accountBook.setCreateDate(new Date());
			accountBookDao.save(accountBook);
			return OperateResultService.configurateSuccessResult();
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}

	@Logger(message="更新账本")
	@SuppressWarnings("unchecked")
	public String saveChanges(String data, HttpServletRequest request) {
		try {
			JSONArray array = JSONArray.fromObject(data);
			Iterator<JSONObject> it = array.iterator();
			while(it.hasNext()){
				JSONObject json = it.next();
				String id = json.getString("id");
				String name = json.getString("name");
				String mark = json.getString("mark");
				AccountBook accountBook = accountBookDao.findOne(id);
				accountBook.setName(name);
				accountBook.setMark(mark);
				accountBookDao.update(accountBook);
			}
			return OperateResultService.configurateSuccessResult();
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}

	@Logger(message="删除账本")
	public String delete(String id, HttpServletRequest request) {
		try {
			AccountBook accountBook = accountBookDao.findOne(id);
			accountBookDao.delete(accountBook);
			return OperateResultService.configurateSuccessResult();
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}
}
