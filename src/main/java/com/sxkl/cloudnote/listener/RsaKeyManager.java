package com.sxkl.cloudnote.listener;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.utils.RSACoder;

/**
 * @author: wangyao
 * @date:2018年1月9日 下午1:48:30
 */
@Service
public class RsaKeyManager {
	
	private static Map<String,String> key = new HashMap<String,String>();
	private static final String PUBLIC_KEY = "publicKey";
	private static final String PRIVATE_KEY = "privateKey";
	
	@PostConstruct
	public void initialization() throws Exception{
		 Map<String, Key> keyMap = RSACoder.initKey();
	     String publicKey = RSACoder.getPublicKey(keyMap);
	     String privateKey = RSACoder.getPrivateKey(keyMap);
	     key.put(PUBLIC_KEY, publicKey);
	     key.put(PRIVATE_KEY, privateKey);
	}
	
	public static String getPublickey(){
		return key.get(PUBLIC_KEY);
	}
	
	public static String getPrivateKey(){
		return key.get(PRIVATE_KEY);
	}
}
