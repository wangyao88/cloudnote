package com.sxkl.cloudnote.accountsystem.tally.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sxkl.cloudnote.accountsystem.tally.dao.TallyDao;
import com.sxkl.cloudnote.log.annotation.Logger;

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

	@Logger(message="分页查询账目列表")
	public String getTallyList(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
