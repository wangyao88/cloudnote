package com.sxkl.cloudnote.lexicon.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.lexicon.entity.Lexicon;
import com.sxkl.cloudnote.sessionfactory.SessionFactory;
import com.sxkl.cloudnote.utils.UUIDUtil;

import lombok.Cleanup;

/**
 * @author wangyao
 * @date 2018年1月13日 下午1:11:30
 * @description: 停用词库持久层
 */
@SessionFactory(Constant.CVM_SESSION_FACTORY)
@Repository
public class LexiconDao extends BaseDao<String,Lexicon>{

	public void batchSave(Set<Object> keys, String userId) throws SQLException {
		String sql = "insert into cn_lexicon(id,name,uId,discriminator) values (?,?,?,?)";
		@Cleanup
		Connection conn = getConnection();
		PreparedStatement statement = conn.prepareStatement(sql);
		conn.setAutoCommit(false);
		List<Object> tempKeys = Lists.newArrayList(keys);
		int size = tempKeys.size();
		Object key = null;
		for(int i = 0; i < size; i++){
			key = tempKeys.get(i);
			statement.setString(1, UUIDUtil.getUUID());
			statement.setString(2, key.toString());
			statement.setString(3, userId);
			statement.setString(4, "keyLexicon");
			statement.addBatch();
			if(i%1000 == 0){
				statement.executeBatch();
				conn.commit();
			}
		}
		statement.executeBatch();
		conn.commit();
	}

}
