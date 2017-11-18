package com.sxkl.cloudnote.image.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.image.dao.ImageDao;
import com.sxkl.cloudnote.image.entity.Image;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageService {
	
	@Autowired
	private ImageDao imageDao;

	public Image getImageByName(HttpServletRequest request) {
		String name = request.getParameter("name");
		return imageDao.getImageByName(name);
	}

	public void saveImage(Image image) {
		try {
			imageDao.saveImage(image);
		} catch (Exception e) {
			log.error("保存图片失败!"+e.getMessage());
		}
	}

	public void establishLinkagesBetweenArticleAndImage(String articleId, String imageName) {
		int num = imageDao.updateImageArticleId(articleId,imageName);
		log.info("Image 更新"+num+"条数据");
	}

	public void deleteImageByArticleId(String articleId) {
		int num = imageDao.deleteImageByArticleId(articleId);
		log.info("Image 删除"+num+"条数据");
	}


}
