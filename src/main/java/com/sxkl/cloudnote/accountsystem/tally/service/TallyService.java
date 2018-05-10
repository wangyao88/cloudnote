package com.sxkl.cloudnote.accountsystem.tally.service;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.accountsystem.category.dao.CategoryDao;
import com.sxkl.cloudnote.accountsystem.category.entity.Category;
import com.sxkl.cloudnote.accountsystem.tally.dao.TallyDao;
import com.sxkl.cloudnote.accountsystem.tally.entity.Tally;
import com.sxkl.cloudnote.accountsystem.tally.entity.TallyFront;
import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.UserUtil;
import com.sxkl.cloudnote.utils.WebUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author: wangyao
 * @date: 2018年5月9日 下午3:37:42
 * @description: 
 */
@Service
@Transactional(value = "transactionManager")
public class TallyService {
	
	@Autowired
	private TallyDao tallyDao;
	@Autowired
	private CategoryDao categoryDao;

	@Logger(message="分页查询账目列表")
	public String getTallyList(HttpServletRequest request) {
		try {
			Tally tally = WebUtils.Requst2Bean(request, Tally.class);
			String pageIndex = request.getParameter("pageIndex");
			String pageSize = request.getParameter("pageSize");
			User sessionUser = UserUtil.getSessionUser(request);
			String userId = sessionUser.getId();
			tally.setUserId(userId);
			List<Tally> tallies = tallyDao.getTallyList(Integer.parseInt(pageIndex),Integer.parseInt(pageSize),tally);
			int total = tallyDao.getTallyListCount(tally);
			List<TallyFront> datas = convert(tallies);
			return OperateResultService.configurateSuccessDataGridResult(datas,total);
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}

	private List<TallyFront> convert(List<Tally> tallies) {
		List<TallyFront> datas = Lists.newArrayList();
		for(Tally tally : tallies){
			TallyFront tallyFront = new TallyFront();
			tallyFront.setAccountBook(tally.getAccountBook());
			tallyFront.setCategory(tally.getCategory().getId());
			tallyFront.setCreateDate(tally.getCreateDate());
			tallyFront.setId(tally.getId());
			tallyFront.setMark(tally.getMark());
			tallyFront.setMoney(tally.getMoney());
			tallyFront.setType(tally.getType());
			datas.add(tallyFront);
		}
		return datas;
	}

	@SuppressWarnings("unchecked")
	@Logger(message="保存账目")
	public String saveChanges(HttpServletRequest request) {
		try {
			String data = request.getParameter("data");
			User sessionUser = UserUtil.getSessionUser(request);
			String userId = sessionUser.getId();
			JSONArray array = JSONArray.fromObject(data);
			Iterator<JSONObject> it = array.iterator();
			while(it.hasNext()){
				JSONObject json = it.next();
				Tally tally = (Tally) JSONObject.toBean(json, Tally.class);
				tally.setUserId(userId);
				String categoryId = json.getString("category");
				Category category = categoryDao.findOne(categoryId);
				tally.setCategory(category);
				tallyDao.saveOrUpdate(tally);
			}
			return OperateResultService.configurateSuccessResult();
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}

	public String delete(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			Tally tally = tallyDao.findOne(id);
			tallyDao.delete(tally);
			return OperateResultService.configurateSuccessResult();
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}

}
