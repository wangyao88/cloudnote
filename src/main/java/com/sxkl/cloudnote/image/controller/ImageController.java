package com.sxkl.cloudnote.image.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sxkl.cloudnote.image.entity.Image;
import com.sxkl.cloudnote.image.service.ImageService;

import lombok.Cleanup;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @RequestMapping("/getImage")
    public void valicode(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        @Cleanup
        ServletOutputStream out = response.getOutputStream();
        Image image = imageService.getImageByName(request);
        if (image == null || image.getContent() == null) {
            return;
        }
        @Cleanup
        InputStream imageStream = new ByteArrayInputStream(image.getContent());
        response.setContentType("image/*");
        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = imageStream.read(buf, 0, 1024)) != -1) {
            out.write(buf, 0, len);
        }
        out.write(image.getContent());
        out.flush();
    }

}
