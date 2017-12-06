package com.sxkl.cloudnote.backup.controller;

import com.sxkl.cloudnote.backup.entity.DataBaseInfo;
import com.sxkl.cloudnote.backup.service.DataBaseBackService;
import com.sxkl.cloudnote.common.service.OperateResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wangyao
 * Date 2017/12/6.
 */

@RestController
@RequestMapping("/backup")
public class BackupController {

    @Autowired
    private DataBaseBackService dataBaseBackService;

    @RequestMapping(value = "/backupDB", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String backupDB(){
        try {
            DataBaseInfo dataBaseInfo = dataBaseBackService.getDataBaseInfo();
            dataBaseBackService.backup(dataBaseInfo);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }
}
