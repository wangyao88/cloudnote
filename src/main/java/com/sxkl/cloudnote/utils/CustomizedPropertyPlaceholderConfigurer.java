package com.sxkl.cloudnote.utils;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * Created by wangyao
 * Date 2017/12/4.
 */
public class CustomizedPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    @Override
    protected String convertProperty(String propertyName,String propertyValue){
        DESUtil desUtil = new DESUtil();
        if(isEncryptPropertyVal(propertyName)){
        	 return desUtil.decrypt(propertyValue);
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
