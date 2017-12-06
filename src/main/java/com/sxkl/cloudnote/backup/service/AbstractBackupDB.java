package com.sxkl.cloudnote.backup.service;

import com.sxkl.cloudnote.backup.entity.DataBaseInfo;

public abstract class AbstractBackupDB implements DataBaseBackService{
	
	public void backupChain(){
		DataBaseInfo dataBaseInfo = getDataBaseInfo();
		String path = backup(dataBaseInfo);
		sendMail(path);
		deleteExpireFile(path);
	}
	
	public abstract DataBaseInfo getDataBaseInfo();
	
	public abstract String backup(DataBaseInfo dataBaseInfo);
	
	public abstract void sendMail(String draft);
	
	public abstract void deleteExpireFile(String path);

}
