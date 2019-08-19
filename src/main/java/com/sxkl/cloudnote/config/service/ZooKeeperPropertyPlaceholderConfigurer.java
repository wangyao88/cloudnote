package com.sxkl.cloudnote.config.service;

import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.sxkl.cloudnote.config.zkclient.ZKClientConfig;
import com.sxkl.cloudnote.utils.DESUtil;

/**
 * @author: wangyao
 * @date: 2018年5月15日 下午3:35:21
 * @description:
 */
public class ZooKeeperPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
            throws BeansException {
        DESUtil desUtil = new DESUtil();
        Map<String, Object> map = ZKClientConfig.getChildrenData(ZKClientConfig.BASE_PATH);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (isEncryptPropertyVal(key)) {
                value = desUtil.decrypt(value.toString());
            }
            props.put(key, value);
        }
        super.processProperties(beanFactoryToProcess, props);
    }

    private boolean isEncryptPropertyVal(String propertyName) {
        return propertyName.contains("password");
    }
}
