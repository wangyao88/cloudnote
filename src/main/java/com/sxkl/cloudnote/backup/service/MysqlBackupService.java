package com.sxkl.cloudnote.backup.service;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.sxkl.cloudnote.backup.entity.DataBaseInfo;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.utils.DESUtil;
import com.sxkl.cloudnote.utils.DateUtils;
import com.sxkl.cloudnote.utils.PropertyUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangyao
 * Date 2017/12/6.
 */
@Slf4j
@Service
public class MysqlBackupService implements DataBaseBackService {

    @Override
    public boolean backup(DataBaseInfo dataBaseInfo) {
        String savePath = dataBaseInfo.getPath();
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if(!savePath.endsWith(File.separator)){
            savePath = savePath + File.separator;
        }
        try {
            DESUtil desUtil = new DESUtil();
            @Cleanup
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(savePath + dataBaseInfo.getFilename()), "utf8"));
            String comand = " mysqldump -h " + dataBaseInfo.getIp() + " -u " + dataBaseInfo.getUsername() + " -p " + desUtil.decrypt(dataBaseInfo.getPassword()) + " --set-charset=UTF8 " + dataBaseInfo.getSchema();
            System.out.println(comand);
            Process process = Runtime.getRuntime().exec(comand);
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
            @Cleanup
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while((line = bufferedReader.readLine())!= null){
                printWriter.println(line);
            }
            printWriter.flush();
            if(process.waitFor() == 0){//0 表示线程正常终止。
                return true;
            }
        }catch (Exception e) {
            log.error("备份数据库失败！错误信息:{}",e.getMessage());
        }
        return false;
    }

    @Override
    public DataBaseInfo getDataBaseInfo() {
        DataBaseInfo dataBaseInfo = new DataBaseInfo();
        Map<String,String> map = PropertyUtil.getPropertiesAllValue("init.properties");
        Predicate<String> filter = new Predicate<String>() {
            @Override
            public boolean apply(String key) {
                return key.startsWith("jdbc");
            }
        };
        Map<String,String> filteredMap = Maps.filterKeys(map, filter);
        Method[] methods = dataBaseInfo.getClass().getDeclaredMethods();
        try{
            for (Method method : methods){
                if (method.getName().startsWith("set")){
                    String field = method.getName();
                    field = field.substring(field.indexOf("set") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    method.invoke(dataBaseInfo, new Object[]{filteredMap.get("jdbc."+field)});
                }
            }
        }catch (Exception e){
            log.error("获取数据库信息失败！错误信息:{}",e.getMessage());
        }
        dataBaseInfo.setFilename(DateUtils.formatDate4()+ Constant.SQL_FILE_EXTENSION);
        return dataBaseInfo;
    }
}
