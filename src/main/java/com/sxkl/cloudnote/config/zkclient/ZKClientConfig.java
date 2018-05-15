package com.sxkl.cloudnote.config.zkclient;

import java.util.List;
import java.util.Map;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.apache.curator.shaded.com.google.common.collect.Maps;

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
	public static final String BASE_PATH = "/config";
	
	public static CuratorFramework getClient(){
		return getClient(DEFAULT_SERVER_HOST,DEFAULT_SESSION_TIMEOUTMS,DEFAULT_CONNECTION_TIMEOUTMS);
	}
	
	public static  CuratorFramework getClient(String host){
		return getClient(host,DEFAULT_SESSION_TIMEOUTMS,DEFAULT_CONNECTION_TIMEOUTMS);
	}
	
	public static  CuratorFramework getClient(String host, int sessionTimeoutMs, int connectionTimeoutMs){
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

	public static void main(String[] args) throws Exception {
        Map<String,Object> map =ZKClientConfig.getChildrenData(ZKClientConfig.BASE_PATH);
        for(Map.Entry<String, Object> entry : map.entrySet()){
    		System.out.println(entry.getKey()+"="+entry.getValue()); 
    	}
	}
	
	public static List<String> getPathChildren(String path){
		@Cleanup
		CuratorFramework client = getClient();
		try {
			return client.getChildren().forPath(path);
		} catch (Exception e) {
			return Lists.newArrayList();
		}  
	}
	
	public static Map<String,Object> getChildrenData(String path){
		Map<String,Object> map = Maps.newHashMap();
		@Cleanup
		CuratorFramework client = getClient();
		try {
			List<String> childrenPath = client.getChildren().forPath(path);
			for(String childPath : childrenPath){
				byte[] datas = client.getData().forPath(StringUtils.appendJoinEmpty(BASE_PATH,"/",childPath));  
		        map.put(childPath, new String(datas));
			}
		} catch (Exception e) {
			
		}  
		return map;
	}
}
