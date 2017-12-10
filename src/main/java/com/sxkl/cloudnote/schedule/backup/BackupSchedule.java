package com.sxkl.cloudnote.schedule.backup;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.backup.service.MysqlBackupService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BackupSchedule {

	@Autowired
    private MysqlBackupService mysqlBackupService;
	
	@Logger(message="定时备份数据库")
	@Scheduled(cron="0 0 0 * * ?")
	public void backupDB(){
		log.info("{}-定时备份数据库",DateUtils.formatDate2Str(new Date()));
		mysqlBackupService.backupChain();
	}
}
