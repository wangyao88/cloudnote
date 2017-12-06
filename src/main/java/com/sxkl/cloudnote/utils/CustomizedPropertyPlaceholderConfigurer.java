package com.sxkl.cloudnote.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * Created by wangyao
 * Date 2017/12/4.
 */
@Slf4j
public class CustomizedPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    @Override
    protected String convertProperty(String propertyName,String propertyValue){
        DESUtil desUtil = new DESUtil();
        if(isEncryptPropertyVal(propertyName)){
            try{
                return desUtil.decrypt(propertyValue);
            }catch (Exception e){
                log.error("解密配置文件密码失败!错误信息：{}", e.getMessage());
                return "";
            }
        }else{
            return propertyValue;
        }
    }
    private boolean isEncryptPropertyVal(String propertyName){
        if(propertyName.contains("password")){
            return true;
        }else{
            return false;
        }
    }
}
