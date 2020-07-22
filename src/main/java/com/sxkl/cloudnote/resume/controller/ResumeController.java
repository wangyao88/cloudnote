package com.sxkl.cloudnote.resume.controller;

import com.mohan.project.easylogger.core.EasyLogger;
import com.sxkl.cloudnote.log.annotation.Logger;
import lombok.Cleanup;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/resume")
public class ResumeController {

    private static final String RESUME_FILE_PATH= "resume/简历-架构师-王曜.docx";
    private static final String RESUME_FILE_NAME = "简历-架构师-王曜.docx";

    @Logger(message = "下载简历")
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ClassPathResource resource = new ClassPathResource(RESUME_FILE_PATH);
        @Cleanup
        InputStream inputStream = resource.getInputStream();
        byte[] body = new byte[inputStream.available()];
        int readNum = inputStream.read(body);
        EasyLogger.info("下载简历，读取{0}个字节", readNum);
        HttpHeaders headers = new HttpHeaders();
        String fileName = URLEncoder.encode(RESUME_FILE_NAME, StandardCharsets.UTF_8.name());
        headers.add("Content-Disposition", "attchement;filename=" + fileName);
        HttpStatus statusCode = HttpStatus.OK;
        return new ResponseEntity<>(body, headers, statusCode);
    }
}