package com.sxkl.cloudnote.log.dao;

import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.log.entity.Log;
import com.sxkl.cloudnote.sessionfactory.SessionFactory;

/**
 *  * @author wangyao
 *  * @date 2018年1月14日 下午3:26:58
 *  * @description:
 *  
 */
@SessionFactory(Constant.CVM_SESSION_FACTORY)
@Repository
public class LogDao extends BaseDao<String, Log> {

}
