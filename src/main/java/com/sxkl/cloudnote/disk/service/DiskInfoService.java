package com.sxkl.cloudnote.disk.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.disk.dao.DiskInfoDao;
import com.sxkl.cloudnote.disk.entity.DiskInfo;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.main.entity.TreeNode;

/**
 * @author: wangyao
 * @date:2018年1月4日 下午5:04:25
 */
@Service
public class DiskInfoService {
	
	@Autowired
	private DiskInfoDao diskInfoDao;
	
	@Logger(message="保存云盘")
	public void save(DiskInfo diskInfo){
		diskInfoDao.save(diskInfo);
	}
	
	@Logger(message="删除云盘")
	public void delete(String id){
		diskInfoDao.delete(diskInfoDao.fetch(id));
	}
	
	@Logger(message="删除云盘")
	public void delete(DiskInfo diskInfo){
		diskInfoDao.delete(diskInfo);
	}
	
	@Logger(message="更新云盘")
	public void update(DiskInfo diskInfo){
		diskInfoDao.update(diskInfo);
	}
	
	@Logger(message="获取云盘")
	public DiskInfo fetch(String id){
		return diskInfoDao.fetch(id);
	}
	
	@Logger(message="获取云盘菜单树")
	public List<TreeNode> getTree() {
		List<TreeNode> tree = Lists.newArrayList();
		TreeNode home = new TreeNode();
		home.setId("home");
		home.setText("主页");
		home.setPid("0");
		home.setIsleaf(true);
//		home.setIconCls("icon-disk-home");
		tree.add(home);
		
		TreeNode image = new TreeNode();
		image.setId("image");
		image.setText("图片");
		image.setPid("0");
		image.setIsleaf(true);
//		image.setIconCls("icon-disk-image");
		tree.add(image);
		
		TreeNode document = new TreeNode();
		document.setId("document");
		document.setText("文档");
		document.setPid("0");
		document.setIsleaf(true);
//		document.setIconCls("icon-disk-document");
		tree.add(document);
		
		TreeNode vidoe = new TreeNode();
		vidoe.setId("vidoe");
		vidoe.setText("视频");
		vidoe.setPid("0");
		vidoe.setIsleaf(true);
//		vidoe.setIconCls("icon-disk-vidoe");
		tree.add(vidoe);
		
		TreeNode other = new TreeNode();
		other.setId("other");
		other.setText("其他");
		other.setPid("0");
		other.setIsleaf(true);
//		other.setIconCls("icon-disk-other");
		tree.add(other);
		
		TreeNode share = new TreeNode();
		share.setId("share");
		share.setText("分享");
		share.setPid("0");
		share.setIsleaf(true);
//		share.setIconCls("icon-disk-share");
		tree.add(share);
		
		return tree;
	}
}
