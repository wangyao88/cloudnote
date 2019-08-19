package com.sxkl.cloudnote.schedule.backup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.backup.service.MysqlBackupService;
import com.sxkl.cloudnote.log.annotation.Logger;

@Service
public class BackupSchedule {

    @Autowired
    private MysqlBackupService mysqlBackupService;

    @Logger(message = "定时备份数据库")
    @Scheduled(cron = "0 0 0 * * ?")
    public void backupDB() {
        mysqlBackupService.backupChain();
    }
}
