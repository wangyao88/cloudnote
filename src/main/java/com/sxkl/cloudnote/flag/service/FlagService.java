package com.sxkl.cloudnote.flag.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.gson.Gson;
import com.sxkl.cloudnote.article.dao.ArticleDao;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.cache.annotation.RedisCachable;
import com.sxkl.cloudnote.cache.annotation.RedisDisCachable;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.flag.dao.FlagDao;
import com.sxkl.cloudnote.flag.entity.Flag;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.main.entity.TreeNode;
import com.sxkl.cloudnote.spider.entity.SearchComplete;
import com.sxkl.cloudnote.statistic.model.FlagData;
import com.sxkl.cloudnote.user.dao.UserDao;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.UUIDUtil;
import com.sxkl.cloudnote.utils.UserUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class FlagService {

    @Autowired
    private FlagDao flagDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ArticleDao articleDao;

    @Logger(message = "获取菜单树标签根节点")
    public TreeNode getRootTreeNode() {
        TreeNode flag = new TreeNode();
        flag.setId(Constant.TREE_MENU_FLAG_ID_PREFIX + UUIDUtil.getUUID());
        flag.setText("标签");
        flag.setIsleaf(false);
        return flag;
    }

    @Logger(message = "将标签转换为菜单树节点")
    public TreeNode convertToTreeNode(TreeNode rootFlag, Flag flag) {
        TreeNode treeNode = new TreeNode();
        treeNode.setId(Constant.TREE_MENU_FLAG_ID_PREFIX + flag.getId());
        if (flag.getChildren().size() > 0) {
            treeNode.setText(flag.getName());
        } else {
            treeNode.setText(flag.getName() + "(" + flag.getArticles().size() + ")");
        }
        if (flag.getChildren() == null || flag.getChildren().size() == 0) {
            treeNode.setIsleaf(true);
        }
        if (flag.getParent() == null) {
            treeNode.setPid(rootFlag.getId());
        } else {
            treeNode.setPid(Constant.TREE_MENU_FLAG_ID_PREFIX + flag.getParent().getId());
        }
        return treeNode;
    }

    @Logger(message = "添加标签")
    @RedisDisCachable(key = {Constant.TREE_MENU_KEY_IN_REDIS, Constant.TREE_FOR_ARTICLE_KEY_IN_REDIS,})
    public void addFlag(HttpServletRequest request) {
        String id = getFlagIdFromFront(request);
        String name = request.getParameter("name");
        int level = Integer.parseInt(request.getParameter("level"));

        Flag flag = new Flag();
        flag.setName(name);

        User user = UserUtil.getSessionUser(request);
        flag.setUser(user);


        //根节点
        if (isRootFlag(level)) {
            flagDao.save(flag);
            return;
        }
        Flag parent = flagDao.findOne(id);
        flag.setParent(parent);
        parent.getChildren().add(flag);
        flagDao.save(parent);
    }

    @Logger(message = "更新标签")
    @RedisDisCachable(key = {Constant.TREE_MENU_KEY_IN_REDIS, Constant.TREE_FOR_ARTICLE_KEY_IN_REDIS,})
    public void updateFlag(HttpServletRequest request) {
        String id = getFlagIdFromFront(request);
        String name = request.getParameter("name");
        Flag flag = flagDao.findOne(id);
        flag.setName(name);
        flagDao.update(flag);
    }

    @Logger(message = "删除标签")
    @RedisDisCachable(key = {Constant.TREE_MENU_KEY_IN_REDIS, Constant.TREE_FOR_ARTICLE_KEY_IN_REDIS,})
    public void deleteFlag(HttpServletRequest request) {
        try {
            String id = getFlagIdFromFront(request);
            Flag flag = flagDao.findOne(id);
            Flag parentTemp = flag.getParent();
            if (parentTemp != null) {
                Flag parent = flagDao.findOne(parentTemp.getId());
                flag.setParent(null);
                parent.getChildren().remove(flag);
                flagDao.update(parent);
            }
            User sessionUser = UserUtil.getSessionUser(request);
            User user = userDao.selectUser(sessionUser);
            user.getFlags().remove(flag);
            flag.setUser(null);
            userDao.update(user);
            flag.setArticles(null);
            flagDao.delete(flag);
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

    @Logger(message = "获取添加文章页面的标签树")
    @RedisCachable(key = Constant.TREE_FOR_ARTICLE_KEY_IN_REDIS, dateTime = 60)
    public String getCheckFlagTree(HttpServletRequest request) {
        User sessionUser = UserUtil.getSessionUser(request);
        String flagTreeMenu = getFlagTreeMenu(sessionUser.getId());
        StringBuilder treeJson = new StringBuilder();
        treeJson.append(Constant.TREE_MENU_PREFIX);
        treeJson.append(flagTreeMenu);
        treeJson.append(Constant.TREE_MENU_SUFFIX);
        return treeJson.toString();
    }

    private void validateJson(StringBuilder treeJson) {
        String treeStr = treeJson.toString();
        if (treeStr.substring(treeStr.length() - 1, treeStr.length()).equals(Constant.COMMA)) {
            treeJson.deleteCharAt(treeJson.length() - 1);
        }
    }

    @Logger(message = "根据主键获取标签")
    public List<Flag> selectFlagsByIds(String[] flags) {
        return flagDao.selectFlagsByIds(flags);
    }

    @Logger(message = "根据文章主键获取标签")
    public Flag getFlagByArticleId(HttpServletRequest request) {
        String articleId = request.getParameter("articleId");
        Article article = articleDao.findOne(articleId);
        Set<Flag> flags = article.getFlags();
        String flagIds = "";
        String flagNames = "";
        for (Flag flag : flags) {
            flagIds += Constant.TREE_MENU_FLAG_ID_PREFIX + flag.getId() + ",";
            flagNames += flag.getName() + ",";
        }
        if (flagIds.endsWith(Constant.COMMA)) {
            flagIds = flagIds.substring(0, flagIds.length() - 1);
        }
        if (flagNames.endsWith(Constant.COMMA)) {
            flagNames = flagNames.substring(0, flagNames.length() - 1);
        }
        Flag result = new Flag();
        result.setId(flagIds);
        result.setName(flagNames);
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Logger(message = "获取用户相关联的标签")
    public List getAllFlagByUserId(String userId) {
        List result = flagDao.getAllFlagByUserId(userId);
        return sortFlags(result);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private List sortFlags(List result) {
        Ordering<Object[]> flagOrdering = new Ordering<Object[]>() {
            public int compare(Object[] left, Object[] right) {
                String leftStr = left[1].toString();
                String rightStr = right[1].toString();
                leftStr = leftStr.contains(".") ? leftStr.substring(0, leftStr.lastIndexOf(".")) : "99999";
                rightStr = rightStr.contains(".") ? rightStr.substring(0, rightStr.lastIndexOf(".")) : "99999";
                if (NumberUtils.isNumber(leftStr) && NumberUtils.isNumber(rightStr)) {
                    return NumberUtils.createInteger(leftStr) - NumberUtils.createInteger(rightStr);
                }
                return leftStr.compareTo(rightStr);
            }
        };
        return flagOrdering.sortedCopy(result);
    }

    @SuppressWarnings("rawtypes")
    @Logger(message = "获取标签树")
    public String getFlagTreeMenu(String userId) {
        TreeNode rootFlag = getRootTreeNode();
        Gson gson = new Gson();
        StringBuilder treeJson = new StringBuilder();
        treeJson.append(gson.toJson(rootFlag));
        treeJson.append(Constant.COMMA);
        List result = getAllFlagByUserId(userId);
        for (Iterator iterator = result.iterator(); iterator.hasNext(); ) {
            Object[] objects = (Object[]) iterator.next();
            TreeNode treeNode = new TreeNode();
            treeNode.setId(Constant.TREE_MENU_FLAG_ID_PREFIX + String.valueOf(objects[0]));
            treeNode.setText(String.valueOf(objects[1]));
            Object pId = objects[2];
            if (pId == null) {
                treeNode.setPid(rootFlag.getId());
            } else {
                treeNode.setPid(Constant.TREE_MENU_FLAG_ID_PREFIX + String.valueOf(objects[2]));
            }
            treeJson.append(gson.toJson(treeNode));
            treeJson.append(Constant.COMMA);
        }
        validateJson(treeJson);
        return treeJson.toString();
    }

    @Logger(message = "获取自动补全标签")
    public SearchComplete getAllByName(String name, String userId) {
        List<Flag> flags = flagDao.getAllByName(name, userId);
        int size = flags.size();
        SearchComplete result = new SearchComplete(size);
        for (int i = 0; i < size; i++) {
            Flag flag = flags.get(i);
            result.getSuggestions()[i] = flag.getName();
            result.getData()[i] = flag.getId();
        }
        result.setQuery(name);
        return result;
    }

    @Logger(message = "获取标签主键")
    public String getFlagId(String flagName) {
        Flag flag = flagDao.getByName(flagName);
        return flag.getId();
    }

    public int getFlagNum(String userId) {
        return flagDao.getFlagNum(userId);
    }

    public List<FlagData> getFlagDatas(String userId) {
        List<Flag> flags = flagDao.getFlagDatas(userId);
        List<FlagData> flagDatas = Lists.newArrayListWithCapacity(flags.size());
        for (int i = 0; i < flags.size(); i++) {
            flagDatas.add(FlagData.configure(i+1, flags.get(i)));
        }
        return flagDatas;
    }
}
