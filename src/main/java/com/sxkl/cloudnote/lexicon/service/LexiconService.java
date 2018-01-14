package com.sxkl.cloudnote.lexicon.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.common.service.BaseService;
import com.sxkl.cloudnote.lexicon.dao.LexiconDao;
import com.sxkl.cloudnote.lexicon.entity.Lexicon;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.utils.PropertyUtil;
import com.sxkl.cloudnote.utils.StringAppendUtils;

/**
 * @author wangyao
 * @date 2018年1月13日 下午1:16:07
 * @description: 扩展词库服务层
 */
@Service
public class LexiconService extends BaseService<String,Lexicon>{

	@Autowired
	private LexiconDao lexiconDao;
	@Autowired
    private RedisTemplate<Object, Object> redisTemplate;
	
	@Override
	protected BaseDao<String,Lexicon> getDao() {
		return lexiconDao;
	}
	
	@Logger(message="获取系统默认扩展词")
	public List<String> getCommonLexicons(int pageIndex, int pageSize) throws IOException{
		String path = PropertyUtil.class.getClassLoader().getResource("lucene/ext.dic").getPath();
		List<String> lines = Files.readLines(new File(path), Charsets.UTF_8);
		return lines.subList(pageIndex*pageSize, (pageIndex+1)*pageSize);
	}

	@Logger(message="停用关键词")
	public void changeToStop(String id, Lexicon lexicon) {
		deleteById(id);
		save(lexicon);
	}
	
	public void initKey(String userId){
		String key = StringAppendUtils.append(Constant.WORD_ARTICLE_MAPPING_IN_REDIS,"-",userId);
		Set<Object> keys = redisTemplate.opsForHash().keys(key);
		try {
			lexiconDao.batchSave(keys,userId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

