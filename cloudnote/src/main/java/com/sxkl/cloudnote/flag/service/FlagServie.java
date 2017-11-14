package com.sxkl.cloudnote.flag.service;

import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.flag.entity.Flag;
import com.sxkl.cloudnote.main.entity.TreeNode;
import com.sxkl.cloudnote.utils.UUIDUtil;

@Service
public class FlagServie {

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
		treeNode.setText(flag.getName());
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
}
