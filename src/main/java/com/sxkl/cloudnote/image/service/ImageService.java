package com.sxkl.cloudnote.image.service;

import java.io.*;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.utils.ObjectUtils;
import com.sxkl.cloudnote.utils.StringUtils;
import lombok.Cleanup;
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

    @Logger(message = "根据图片名获取图片")
    public Image getImageByName(HttpServletRequest request) {
        try {
            String name = request.getParameter("name");
            Image image = imageDao.getImageByName(name);
//			byte[] content = image.getContent();
//			if(ObjectUtils.isNull(content) || content.length == 0) {
//				content = getImageContentFromDisk(image.getName());
//			}
            byte[] content = getImageContentFromDisk(image.getName());
            image.setContent(content);
            return image;
        } catch (Exception e) {
            log.error("获取图片失败!" + e.getMessage());
            return null;
        }
    }

    private byte[] getImageContentFromDisk(String name) {
        byte[] buffer = StringUtils.EMPTY.getBytes();
        try {
            if (StringUtils.isEmpty(name)) {
                return buffer;
            }
            File dir = new File(Constant.IMAGE_SAVED_PATH);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            String filePath = StringUtils.appendJoinFolderSeparator(Constant.IMAGE_SAVED_PATH, name);
            File file = new File(filePath);
            @Cleanup
            FileInputStream fis = new FileInputStream(file);
            @Cleanup
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (Exception e) {
            log.error("从磁盘获取图片内容失败!" + e.getMessage());
        }
        return buffer;
    }

    @Logger(message = "保存图片")
    public void saveImage(Image image) {
        try {
            byte[] content = image.getContent();
            image.setContent(StringUtils.EMPTY.getBytes());
            imageDao.save(image);
            // 保存到磁盘
            saveToDisk(image.getName(), content);
        } catch (Exception e) {
            log.error("保存图片失败!" + e.getMessage());
        }
    }

    public void saveToDisk(String name, byte[] content) throws IOException {
        String path = StringUtils.appendJoinFolderSeparator(Constant.IMAGE_SAVED_PATH, name);
        File dir = new File(Constant.IMAGE_SAVED_PATH);
        if (!dir.exists() && dir.isDirectory()) {
            dir.mkdirs();
        }
        File file = new File(path);
        @Cleanup
        FileOutputStream fos = new FileOutputStream(file);
        @Cleanup
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(content);
    }

    @Logger(message = "关联文章与图片")
    public void establishLinkagesBetweenArticleAndImage(String articleId, String imageName) {
        int num = imageDao.updateImageArticleId(articleId, imageName);
        log.info("Image 更新" + num + "条数据");
    }

    @Logger(message = "根据文章主键删除图片")
    public void deleteImageByArticleId(String articleId) {
        int num = imageDao.deleteImageByArticleId(articleId);
        log.info("Image 删除" + num + "条数据");
    }

    @Logger(message = "获取图片总数")
    public int getTotal() {
        return imageDao.getTotal();
    }

    @Logger(message = "分页查询图片")
    public List<Image> findPage(int pageIndex, int pageSize) {
        Page page = new Page();
        page.setHql("select new Image(id,name,aId) from Image");
        page.setIndex(pageIndex);
        page.setSize(pageSize);
        page.setUseDefaultHql(false);
        return imageDao.findPage(page);
    }

    @Logger(message = "批量删除图片")
    public void deleteAll(List<Image> results) {
        imageDao.deleteAll(results);
    }

    public void updateAll(List<Image> updateImages) {
        imageDao.updateAll(updateImages);
    }

    @Logger(message = "获取全部图片")
    public List<Image> getAll() {
        return imageDao.getAll();
    }

    public Image getOne(String id) {
        return imageDao.getOne(id);
    }
}
