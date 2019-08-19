package com.sxkl.cloudnote.disk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sxkl.cloudnote.disk.service.DiskInfoService;
import com.sxkl.cloudnote.disk.service.FileInfoService;

@Controller
@RequestMapping("/disk")
public class DiskController {

    @Autowired
    private DiskInfoService disInfokService;
    @Autowired
    private FileInfoService fileInfoService;

}
