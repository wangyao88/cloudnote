package com.sxkl.cloudnote.lexicon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.common.service.BaseService;
import com.sxkl.cloudnote.lexicon.dao.StopLexiconDao;
import com.sxkl.cloudnote.lexicon.entity.StopLexicon;

/**
 * @author wangyao
 * @date 2018年1月13日 下午1:16:07
 * @description: 停用词库服务层
 */
@Service
public class StopLexiconService extends BaseService<StopLexicon>{

	@Autowired
	private StopLexiconDao stopLexiconDao;
	
	@Override
	protected BaseDao<StopLexicon> getDao() {
		return stopLexiconDao;
	}

}
