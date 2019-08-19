package com.sxkl.cloudnote.config.zkclient;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.apache.curator.shaded.com.google.common.collect.Maps;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.google.common.base.Charsets;
import com.sxkl.cloudnote.utils.ObjectUtils;
import com.sxkl.cloudnote.utils.PropertyUtil;
import com.sxkl.cloudnote.utils.StringUtils;

import lombok.Cleanup;

/**
 * @author: wangyao
 * @date: 2018年5月15日 下午3:08:47
 * @description:
 */
public class ZKClientConfig {

    private static final String DEFAULT_SERVER_HOST = "123.206.20.115:2181";
    private static final int DEFAULT_SESSION_TIMEOUTMS = 30000;
    private static final int DEFAULT_CONNECTION_TIMEOUTMS = 3000;
    public static final String BASE_PATH = StringUtils.appendJoinEmpty("/config/", PropertyUtil.getMode());

    public static CuratorFramework getClient() {
        return getClient(DEFAULT_SERVER_HOST, DEFAULT_SESSION_TIMEOUTMS, DEFAULT_CONNECTION_TIMEOUTMS);
    }

    public static CuratorFramework getClient(String host) {
        return getClient(host, DEFAULT_SESSION_TIMEOUTMS, DEFAULT_CONNECTION_TIMEOUTMS);
    }

    public static CuratorFramework getClient(String host, int sessionTimeoutMs, int connectionTimeoutMs) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(host)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .retryPolicy(retryPolicy)
                .namespace("cloudnote")//命名空间，隔离业务
                .build();
        client.start();
        return client;
    }

    public static List<String> getPathChildren(String path) {
        @Cleanup
        CuratorFramework client = getClient();
        try {
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public static Map<String, Object> getChildrenData(String path) {
        Map<String, Object> map = Maps.newHashMap();
        @Cleanup
        CuratorFramework client = getClient();
        try {
            List<String> childrenPath = client.getChildren().forPath(path);
            for (String childPath : childrenPath) {
                byte[] datas = client.getData().forPath(StringUtils.appendJoinEmpty(BASE_PATH, "/", childPath));
                map.put(childPath, new String(datas));
            }
        } catch (Exception e) {

        }
        return map;
    }

    public static String getDomain() {
        try {
            String mode = PropertyUtil.getMode();
            @Cleanup
            CuratorFramework client = getClient();
            return new String(client.getData().forPath(StringUtils.appendJoinEmpty("/config/", mode, "/cloudnote_domain")));
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    public static void saveOrUpdateNode(String path, String data) throws Exception {
        @Cleanup
        CuratorFramework client = getClient();
        Stat stat = client.checkExists().forPath(path);
        if (ObjectUtils.isNull(stat)) {
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path, data.getBytes(Charsets.UTF_8));
        } else {
            client.setData().forPath(path, data.getBytes(Charsets.UTF_8));
        }
    }

    public static void deleteNode(String path) throws Exception {
        @Cleanup
        CuratorFramework client = getClient();
        Stat stat = client.checkExists().forPath(path);
        if (ObjectUtils.isNotNull(stat)) {
            client.delete().forPath(path);
        }
    }

    public static String getPathData(String path) throws Exception {
        @Cleanup
        CuratorFramework client = getClient();
        Stat stat = client.checkExists().forPath(path);
        String data = StringUtils.EMPTY;
        if (ObjectUtils.isNotNull(stat)) {
            data = new String(client.getData().forPath(path), Charsets.UTF_8);
        }
        return data;
    }

    public static void main(String[] args) throws Exception {
        @Cleanup
        CuratorFramework client = getClient();
        String filePath = "C:\\wangyao\\workspace\\cis\\cloudnote\\src\\main\\resources\\init.properties";
        @Cleanup
        InputStream is = new FileInputStream(filePath);
        Properties properties = new Properties();
        properties.load(is);
        Enumeration<Object> keys = properties.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String path = "/config/produce/" + key;
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path, new String(properties.getProperty(key).getBytes("ISO8859-1"), "UTF-8").getBytes());
        }

    }
}
