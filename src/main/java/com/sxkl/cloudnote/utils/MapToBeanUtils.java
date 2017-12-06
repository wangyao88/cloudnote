package com.sxkl.cloudnote.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by wangyao
 * Date 2017/12/6.
 */
@Slf4j
public class MapToBeanUtils<T> {

    public void mapToBean(T t, String prefix){
        Map<String,String> map = PropertyUtil.getPropertiesAllValue("init.properties");
        Predicate<String> filter = new Predicate<String>() {
            @Override
            public boolean apply(String key) {
                return key.startsWith(prefix);
            }
        };
        Map<String,String> filteredMap = Maps.filterKeys(map, filter);
        Method[] methods = t.getClass().getDeclaredMethods();
        try{
            for (Method method : methods){
                if (method.getName().startsWith("set")){
                    String field = method.getName();
                    field = field.substring(field.indexOf("set") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    method.invoke(t, new Object[]{filteredMap.get(prefix+field)});
                }
            }
        }catch (Exception e){
            log.error("mapToBean失败！错误信息:{}",e.getMessage());
        }
    }
}
