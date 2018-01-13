package com.sxkl.cloudnote.lexicon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.common.service.BaseService;
import com.sxkl.cloudnote.lexicon.dao.ExtLexiconDao;
import com.sxkl.cloudnote.lexicon.entity.ExtLexicon;

/**
 * @author wangyao
 * @date 2018年1月13日 下午1:16:07
 * @description: 扩展词库服务层
 */
@Service
public class ExtLexiconService extends BaseService<ExtLexicon>{

	@Autowired
	private ExtLexiconDao extLexiconDao;
	
	@Override
	protected BaseDao<ExtLexicon> getDao() {
		return extLexiconDao;
	}

}
