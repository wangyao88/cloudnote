package com.sxkl.cloudnote.image.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.common.entity.Page;
import com.sxkl.cloudnote.image.dao.ImageDao;
import com.sxkl.cloudnote.image.entity.Image;
import com.sxkl.cloudnote.log.annotation.Logger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageService {
	
	@Autowired
	private ImageDao imageDao;

	@Logger(message="根据图片名获取图片")
	public Image getImageByName(HttpServletRequest request) {
		String name = request.getParameter("name");
		return imageDao.getImageByName(name);
	}

	@Logger(message="保存图片")
	public void saveImage(Image image) {
		try {
			imageDao.save(image);
		} catch (Exception e) {
			log.error("保存图片失败!"+e.getMessage());
		}
	}

	@Logger(message="关联文章与图片")
	public void establishLinkagesBetweenArticleAndImage(String articleId, String imageName) {
		int num = imageDao.updateImageArticleId(articleId,imageName);
		log.info("Image 更新"+num+"条数据");
	}

	@Logger(message="根据文章主键删除图片")
	public void deleteImageByArticleId(String articleId) {
		int num = imageDao.deleteImageByArticleId(articleId);
		log.info("Image 删除"+num+"条数据");
	}
	
	@Logger(message="获取图片总数")
	public int getTotal(){
		return imageDao.getTotal();
	}
	
	@Logger(message="分页查询图片")
	public List<Image> findPage(int pageIndex, int pageSize){
		Page page = new Page();
		page.setHql("select new Image(id,name,aId) from Image");
		page.setIndex(pageIndex);
		page.setSize(pageSize);
		page.setUseDefaultHql(false);
		return imageDao.findPage(page);
	}

	@Logger(message="批量删除图片")
	public void deleteAll(List<Image> results) {
		imageDao.deleteAll(results);
	}
}
