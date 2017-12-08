package com.sxkl.cloudnote.image.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
			imageDao.saveImage(image);
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


}
