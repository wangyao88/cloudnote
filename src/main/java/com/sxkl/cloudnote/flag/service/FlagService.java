package com.sxkl.cloudnote.flag.service;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sxkl.cloudnote.article.dao.ArticleDao;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.cache.annotation.RedisCachable;
import com.sxkl.cloudnote.cache.annotation.RedisDisCachable;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.flag.dao.FlagDao;
import com.sxkl.cloudnote.flag.entity.Flag;
import com.sxkl.cloudnote.main.entity.TreeNode;
import com.sxkl.cloudnote.user.dao.UserDao;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.user.service.UserService;
import com.sxkl.cloudnote.utils.UUIDUtil;
import com.sxkl.cloudnote.utils.UserUtil;

@Service
public class FlagService {
	
	@Autowired
	private FlagDao flagDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private UserService userService;

	public TreeNode getRootTreeNode() {
		TreeNode flag = new TreeNode();
		flag.setId(Constant.TREE_MENU_FLAG_ID_PREFIX+UUIDUtil.getUUID());
		flag.setText("标签");
		flag.setIsleaf(false);
		return flag;
	}

	public TreeNode convertToTreeNode(TreeNode rootFlag, Flag flag) {
		TreeNode treeNode = new TreeNode();
		treeNode.setId(Constant.TREE_MENU_FLAG_ID_PREFIX+flag.getId());
		if(flag.getChildren().size() > 0){
			treeNode.setText(flag.getName());
		}else{
			treeNode.setText(flag.getName()+"("+flag.getArticles().size()+")");
		}
		if(flag.getChildren() == null || flag.getChildren().size() == 0){
			treeNode.setIsleaf(true);
		}
		if(flag.getParent() == null){
			treeNode.setPid(rootFlag.getId());
		}else{
			treeNode.setPid(Constant.TREE_MENU_FLAG_ID_PREFIX+flag.getParent().getId());
		}
		return treeNode;
	}

	@RedisDisCachable(key={Constant.TREE_MENU_KEY_IN_REDIS,Constant.TREE_FOR_ARTICLE_KEY_IN_REDIS,})
	public void addFlag(HttpServletRequest request) {
		String id = getFlagIdFromFront(request);
		String name = request.getParameter("name");
		int level = Integer.parseInt(request.getParameter("level"));
		
		Flag flag = new Flag();
		flag.setName(name);
		
		User user = UserUtil.getSessionUser(request);
		flag.setUser(user);
		
		
		//根节点
		if(isRootFlag(level)){
			flagDao.saveFlag(flag);
			return;
		}
		Flag parent = flagDao.selectFlagById(id);
		flag.setParent(parent);
		parent.getChildren().add(flag);
		flagDao.saveFlag(parent);
	}
	
	@RedisDisCachable(key={Constant.TREE_MENU_KEY_IN_REDIS,Constant.TREE_FOR_ARTICLE_KEY_IN_REDIS,})
	public void updateFlag(HttpServletRequest request) {
		String id = getFlagIdFromFront(request);
		String name = request.getParameter("name");
		Flag flag = flagDao.selectFlagById(id);
		flag.setName(name);
		flagDao.updateFlag(flag);
	}
	
	@RedisDisCachable(key={Constant.TREE_MENU_KEY_IN_REDIS,Constant.TREE_FOR_ARTICLE_KEY_IN_REDIS,})
	public void deleteFlag(HttpServletRequest request) {
		try {
			String id = getFlagIdFromFront(request);
			Flag flag = flagDao.selectFlagById(id);
			Flag parentTemp = flag.getParent();
			if(parentTemp != null){
				Flag parent = flagDao.selectFlagById(parentTemp.getId());
				flag.setParent(null);
				System.out.println(parent.getChildren().remove(flag));
				System.out.println(parent.getChildren().size());
				flagDao.updateFlag(parent);
			}
			User sessionUser = UserUtil.getSessionUser(request);
			User user = userDao.selectUser(sessionUser);
			user.getFlags().remove(flag);
			flag.setUser(null);
			userDao.updateUser(user);
			flag.setArticles(null);
			flagDao.deleteFlag(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean isRootFlag(int level) {
		return level == 0;
	}
	
	private String getFlagIdFromFront(HttpServletRequest request) {
		String frontId = request.getParameter("id");
		return frontId.substring(Constant.TREE_MENU_FLAG_ID_PREFIX.length());
	}

	@RedisCachable(key=Constant.TREE_FOR_ARTICLE_KEY_IN_REDIS,dateTime=60)
	public String getCheckFlagTree(HttpServletRequest request) {
		User sessionUser = UserUtil.getSessionUser(request);
		User user = userService.selectUser(sessionUser);
		Set<Flag> flags = user.getFlags();
		TreeNode rootFlag = getRootTreeNode();
		
		Gson gson = new Gson();
		StringBuilder treeJson = new StringBuilder();
		treeJson.append(Constant.TREE_MENU_PREFIX);
		treeJson.append(gson.toJson(rootFlag));
		treeJson.append(Constant.COMMA);
		
		for(Flag flag : flags){
			TreeNode treeNode = convertToTreeNode(rootFlag,flag);
			treeJson.append(gson.toJson(treeNode));
			treeJson.append(Constant.COMMA);
		}
		
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

	public List<Flag> selectFlagsByIds(String[] flags) {
		return flagDao.selectFlagsByIds(flags);
	}

	public Flag getFlagByArticleId(HttpServletRequest request) {
		String articleId = request.getParameter("articleId");
		Article article = articleDao.selectArticleById(articleId);
		Set<Flag> flags = article.getFlags();
		String flagIds = "";
		String flagNames = "";
		for(Flag flag : flags){
			flagIds += Constant.TREE_MENU_FLAG_ID_PREFIX + flag.getId() + ",";
			flagNames += flag.getName() + ",";
		}
		if(flagIds.endsWith(Constant.COMMA)){
			flagIds = flagIds.substring(0, flagIds.length()-1);
		}
		if(flagNames.endsWith(Constant.COMMA)){
			flagNames = flagNames.substring(0, flagNames.length()-1);
		}
		Flag result = new Flag();
		result.setId(flagIds);
		result.setName(flagNames);
		return result;
	}
}
