package com.sxkl.cloudnote.accountsystem.tally.dao;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.accountsystem.category.dao.CategoryDao;
import com.sxkl.cloudnote.accountsystem.category.entity.Category;
import com.sxkl.cloudnote.accountsystem.tally.entity.Tally;
import com.sxkl.cloudnote.common.dao.BaseDao;

/**
 * @author: wangyao
 * @date: 2018年5月8日 下午2:00:12
 * @description: 
 */
@Repository
public class TallyDao extends BaseDao<String,Tally> {
	
	@Autowired
	private CategoryDao categoryDao;

	public int getTallyListCount(Tally tally) {
		Criteria criteria = this.getSession().createCriteria(Tally.class);
		configurateCondition(tally, criteria);
		criteria.setProjection(Projections.rowCount());  
		return Integer.parseInt(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	public List<Tally> getTallyList(int pageIndex, int pageSize, Tally tally) {
		Criteria criteria = this.getSession().createCriteria(Tally.class);
		configurateCondition(tally, criteria);
		criteria.setFirstResult(pageIndex*pageSize);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}

	private void configurateCondition(Tally tally, Criteria criteria) {
		if(!StringUtils.isEmpty(tally.getMark())){
			criteria.add(Restrictions.like("mark",'%'+tally.getMark()+'%'));
		}
		if(!Objects.isNull(tally.getBeginDate())){
			criteria.add(Restrictions.ge("createDate", tally.getBeginDate()));
		}
		if(!Objects.isNull(tally.getEndDate())){
			criteria.add(Restrictions.le("createDate", tally.getEndDate()));
		}
		if(!StringUtils.isEmpty(tally.getAccountBook())){
			criteria.add(Restrictions.eq("accountBook",tally.getAccountBook()));
		}
		if(!StringUtils.isEmpty(tally.getType())){
			criteria.add(Restrictions.eq("type",tally.getType()));
		}
		if(!StringUtils.isEmpty(tally.getCategoryId())){
			String categoryId = tally.getCategoryId();
			Category category = categoryDao.findOne(categoryId);
			criteria.add(Restrictions.eq("category",category));
		}
		if(!StringUtils.isEmpty(tally.getUserId())){
			criteria.add(Restrictions.eq("userId",tally.getUserId()));
		}
	}
}
