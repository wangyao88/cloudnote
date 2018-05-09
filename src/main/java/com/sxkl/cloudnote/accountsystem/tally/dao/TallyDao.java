package com.sxkl.cloudnote.accountsystem.tally.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.accountsystem.tally.entity.Tally;
import com.sxkl.cloudnote.common.dao.BaseDao;

/**
 * @author: wangyao
 * @date: 2018年5月8日 下午2:00:12
 * @description: 
 */
@Repository
public class TallyDao extends BaseDao<String,Tally> {

//	@SuppressWarnings("rawtypes")
//	public List findAllByAccountBookId(String accountBookId, String type) {
//		StringBuilder sql = new StringBuilder();
//		sql.append("select f.id,f.name,f.parent_id from cn_category f ")
//		   .append("where f.account_book_id = :accountBookId and f.type=:type");
//		Session session = this.getSessionFactory().getCurrentSession();
//		SQLQuery query = session.createSQLQuery(sql.toString());
//		query.setString("accountBookId", accountBookId);
//		query.setString("type", type);
//		return query.list();
//	}
}
