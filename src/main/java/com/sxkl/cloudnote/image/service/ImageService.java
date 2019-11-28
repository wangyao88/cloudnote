package com.sxkl.cloudnote.image.service;

import com.sxkl.cloudnote.article.dao.ArticleDao;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.common.entity.Page;
import com.sxkl.cloudnote.image.dao.ImageDao;
import com.sxkl.cloudnote.image.entity.Image;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.utils.DateUtils;
import com.sxkl.cloudnote.utils.ObjectUtils;
import com.sxkl.cloudnote.utils.StringUtils;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ImageService {

    @Autowired
    private ImageDao imageDao;
    @Autowired
    private ArticleDao articleDao;

    public void move() {
        File root = new File(Constant.IMAGE_SAVED_PATH);
        File[] files = root.listFiles();
        for (File file : files) {
            String name = file.getName();
            String path;
            Image image = imageDao.getImageByName(name);
            if(ObjectUtils.isNull(image)) {
                path = getDirectory("unused");
            }else {
                path = getDirectory(image.getCreateDate());
            }
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filePath = StringUtils.appendJoinFolderSeparator(path, name);
            File move = new File(filePath);
            file.renameTo(move);
            System.out.println(move.getAbsolutePath());
        }
    }

    @Logger(message = "根据图片名获取图片")
    public Image getImageByName(HttpServletRequest request) {
        try {
            String name = request.getParameter("name");
            Image image = imageDao.getImageByName(name);
            byte[] content = getImageContentFromDisk(image);
            image.setContent(content);
            return image;
        } catch (Exception e) {
            log.error("获取图片失败!" + e.getMessage());
            return null;
        }
    }

    @Logger(message = "根据图片名获取图片")
    public Image getImageByName(String name) {
        try {
            Image image = imageDao.getImageByName(name);
            byte[] content = getImageContentFromDisk(image);
            image.setContent(content);
            return image;
        } catch (Exception e) {
            log.error("获取图片失败!" + e.getMessage());
            return null;
        }
    }

    private byte[] getImageContentFromDisk(Image image) {
        String name = image.getName();
        byte[] buffer = StringUtils.EMPTY.getBytes();
        try {
            if (StringUtils.isEmpty(name)) {
                return buffer;
            }
            String path = getDirectory(image.getCreateDate());
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filePath = StringUtils.appendJoinFolderSeparator(path, name);
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
            image.setCreateDate(new Date());
            imageDao.save(image);
            saveToDisk(image.getName(), content);
        } catch (Exception e) {
            log.error("保存图片失败!" + e.getMessage());
        }
    }

    @Logger(message = "保存图片到磁盘")
    public void saveToDisk(String name, byte[] content) throws IOException {
        String path = getDirectory();
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        path = StringUtils.appendJoinFolderSeparator(path, name);
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

    @Logger(message = "更新指定图片集合")
    public void updateAll(List<Image> updateImages) {
        imageDao.updateAll(updateImages);
    }

    @Logger(message = "获取全部图片")
    public List<Image> getAll() {
        return imageDao.getAll();
    }

    @Logger(message = "获取指定图片")
    public Image getOne(String id) {
        return imageDao.getOne(id);
    }

    private String getDirectory() {
        return getDirectory(new Date());
    }

    private String getDirectory(String name) {
        StringBuilder path = new StringBuilder(Constant.IMAGE_SAVED_PATH);
        path.append(File.separatorChar)
                .append(name);
        return path.toString();
    }

    private String getDirectory(Date date) {
        if(ObjectUtils.isNull(date)) {
            date = new Date();
        }
        LocalDate now = DateUtils.convertDateToLocalDate(date);
        StringBuilder path = new StringBuilder(Constant.IMAGE_SAVED_PATH);
        path.append(File.separatorChar)
                .append(now.getYear())
                .append(File.separatorChar)
                .append(now.getMonth().getValue())
                .append(File.separatorChar)
                .append(now.getDayOfMonth());
        return path.toString();
    }
}