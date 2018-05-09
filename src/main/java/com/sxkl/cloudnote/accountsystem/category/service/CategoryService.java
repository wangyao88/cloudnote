package com.sxkl.cloudnote.accountsystem.category.service;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.sxkl.cloudnote.accountsystem.accountbook.dao.AccountBookDao;
import com.sxkl.cloudnote.accountsystem.accountbook.entity.AccountBook;
import com.sxkl.cloudnote.accountsystem.category.dao.CategoryDao;
import com.sxkl.cloudnote.accountsystem.category.entity.Category;
import com.sxkl.cloudnote.accountsystem.category.entity.CategoryFront;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.main.entity.TreeNode;
import com.sxkl.cloudnote.utils.UUIDUtil;

/**
 * @author: wangyao
 * @date: 2018年5月9日 上午9:25:29
 * @description: 
 */
@Service
@Transactional(value = "transactionManager")
public class CategoryService {
	
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private AccountBookDao accountBookDao;

	@Logger(message="获取收入类别树")
	public String getIncomeCategoryTree(HttpServletRequest request) {
		String accountBookId = request.getParameter("accountBookId");
		return getCategoryTree(accountBookId,"INCOME","收入类别");
	}
	
	@Logger(message="获取支出类别树")
	public String getOutcomeCategoryTree(HttpServletRequest request) {
		String accountBookId = request.getParameter("accountBookId");
		return getCategoryTree(accountBookId,"OUTCOME","支出类别");
	}
	
	@SuppressWarnings("rawtypes")
	private String getCategoryTree(String accountBookId, String type, String name){
		TreeNode rootFlag = getRootTreeNode(name);
		Gson gson = new Gson();
		StringBuilder treeJson = new StringBuilder();
		treeJson.append(gson.toJson(rootFlag));
		treeJson.append(Constant.COMMA);
		List result = categoryDao.findAllByAccountBookId(accountBookId,type);
		for(Iterator iterator = result.iterator();iterator.hasNext();){  
            Object[] objects = (Object[]) iterator.next();
            TreeNode treeNode = new TreeNode();
    		treeNode.setId(String.valueOf(objects[0]));
    		treeNode.setText(String.valueOf(objects[1]));
    		Object pId = objects[2];
    		if(pId == null){
    			treeNode.setPid(rootFlag.getId());
    		}else{
    			treeNode.setPid(String.valueOf(objects[2]));
    		}
    		treeJson.append(gson.toJson(treeNode));
			treeJson.append(Constant.COMMA);
        }  
		validateJson(treeJson);
		return configurateTree(treeJson.toString());
	}
	
	private TreeNode getRootTreeNode(String text) {
		TreeNode flag = new TreeNode();
		flag.setId(Constant.TREE_MENU_FLAG_ID_PREFIX+UUIDUtil.getUUID());
		flag.setText(text);
		flag.setIsleaf(false);
		return flag;
	}

	private String configurateTree(String tree){
		StringBuilder treeJson = new StringBuilder();
		treeJson.append(Constant.TREE_MENU_PREFIX);
		treeJson.append(tree);
		validateJson(treeJson);
		treeJson.append(Constant.TREE_MENU_SUFFIX);
		return treeJson.toString();
	}
	
	private void validateJson(StringBuilder treeJson) {
		String treeStr = treeJson.toString();
		if(treeStr.substring(treeStr.length()-1, treeStr.length()).equals(Constant.COMMA)){
			treeJson.deleteCharAt(treeJson.length()-1);
		}
	}

	@Logger(message="新增类别")
	public String add(CategoryFront categoryFront, HttpServletRequest request) {
		try {
			Category category = convert(categoryFront);
			categoryDao.save(category);
			return OperateResultService.configurateSuccessResult();
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}

	private Category convert(CategoryFront categoryFront) {
		Category category = new Category();
		category.setName(categoryFront.getName());
		category.setType(categoryFront.getType());
		String accountBookId = categoryFront.getAccountBookId();
		AccountBook accountBook = accountBookDao.findOne(accountBookId);
		category.setAccountBook(accountBook);
		if(!isRootCategory(categoryFront.getLevel())){
			String parentId = categoryFront.getParentId();
			Category parent = categoryDao.findOne(parentId);
			category.setParent(parent);
		}
		return category;
	}

	private boolean isRootCategory(int level) {
		return level == 0;
	}

	@Logger(message="更能类别")
	public String update(String id, String name, HttpServletRequest request) {
		try {
			Category category = categoryDao.findOne(id);
			category.setName(name);
			categoryDao.update(category);
			return OperateResultService.configurateSuccessResult();
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}

	@Logger(message="删除类别")
	public String delete(String id, HttpServletRequest request) {
		try {
			Category category = categoryDao.findOne(id);
			if(!category.getChildren().isEmpty()){
				return OperateResultService.configurateFailureResult("拥有子类别，无法删除！");
			}
			Category parentTemp = category.getParent();
			if(parentTemp != null){
				Category parent = categoryDao.findOne(parentTemp.getId());
				category.setParent(null);
				parent.getChildren().remove(category);
				categoryDao.update(parent);
			}
			categoryDao.delete(category);
			return OperateResultService.configurateSuccessResult();
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}
}
