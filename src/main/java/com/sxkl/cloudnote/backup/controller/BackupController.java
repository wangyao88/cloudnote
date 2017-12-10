package com.sxkl.cloudnote.backup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sxkl.cloudnote.backup.service.MysqlBackupService;
import com.sxkl.cloudnote.common.service.OperateResultService;

/**
 * Created by wangyao
 * Date 2017/12/6.
 */

@RestController
@RequestMapping("/backup")
public class BackupController {

    @Autowired
    private MysqlBackupService mysqlBackupService;

    @RequestMapping(value = "/backupDB", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String backupDB(){
        try {
        	String path = mysqlBackupService.backupChain();
            return OperateResultService.configurateSuccessResult(path);
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }
}
