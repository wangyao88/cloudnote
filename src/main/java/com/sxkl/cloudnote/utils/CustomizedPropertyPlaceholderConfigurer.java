package com.sxkl.cloudnote.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by wangyao
 * Date 2017/12/4.
 */
public class CustomizedPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    @Override
    protected String convertProperty(String propertyName,String propertyValue){
        DESUtil desUtil = new DESUtil();
        if(isEncryptPropertyVal(propertyName)){
            try{
                return desUtil.decrypt(propertyValue);
            }catch (Exception e){
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
