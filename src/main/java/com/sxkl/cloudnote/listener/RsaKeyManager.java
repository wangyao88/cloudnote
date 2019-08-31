package com.sxkl.cloudnote.listener;

import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.utils.RSACoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: wangyao
 * @date:2018年1月9日 下午1:48:30
 */
@Service
public class RsaKeyManager {

    private static Map<String, String> key = new HashMap<String, String>();
    private static final String PUBLIC_KEY = "publicKey";
    private static final String PRIVATE_KEY = "privateKey";

    @Logger(message = "初始化公钥和秘钥")
    @PostConstruct
    public void initialization() throws Exception {
        Map<String, Key> keyMap = RSACoder.initKey();
        String publicKey = RSACoder.getPublicKey(keyMap);
        String privateKey = RSACoder.getPrivateKey(keyMap);
        key.put(PUBLIC_KEY, publicKey);
        key.put(PRIVATE_KEY, privateKey);
    }

    @Logger(message = "获取公钥")
    public static String getPublickey() {
        return key.get(PUBLIC_KEY);
    }

    @Logger(message = "获取秘钥")
    public static String getPrivateKey() {
        return key.get(PRIVATE_KEY);
    }
}