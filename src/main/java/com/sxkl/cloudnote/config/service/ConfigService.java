package com.sxkl.cloudnote.config.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.config.zkclient.ZKClientConfig;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.utils.StringUtils;

/**
 * @author: wangyao
 * @date: 2018年5月15日 下午3:07:39
 * @description:
 */
@Service
public class ConfigService {

    @Logger(message = "更新配置中心数据")
    public String addConfig(String mode, String key, String value, HttpServletRequest request) {
        try {
            String path = StringUtils.appendJoinEmpty("/config/", mode, "/", key);
            String data = value;
            ZKClientConfig.saveOrUpdateNode(path, data);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }

    }

    @Logger(message = "删除配置中心数据")
    public String deleteConfig(String mode, String key, HttpServletRequest request) {
        try {
            String path = StringUtils.appendJoinEmpty("/config/", mode, "/", key);
            ZKClientConfig.deleteNode(path);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    public String getCachePages() {
        String page = StringUtils.EMPTY;
        try {
            String path = StringUtils.appendJoinEmpty(ZKClientConfig.BASE_PATH, "/", "cache.page.keys");
            page = ZKClientConfig.getPathData(path);
        } catch (Exception e) {
        }
        return page;
    }

}
