package com.sxkl.cloudnote.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by wangyao
 * Date 2017/12/4.
 */
public class DESUtil {

    private String sKey;

    public DESUtil() {
        //默认构造函数提供默认密钥
        sKey = "des@#$12";
    }

    public DESUtil(String securityKey) {
        if (securityKey.length() < 8) {
            throw new IllegalArgumentException("密钥长度至少8位");
        }
        this.sKey = securityKey;
    }

    private Cipher makeCipher() throws Exception {
        return Cipher.getInstance("DES");
    }

    private SecretKey makeKeyFactory() throws Exception {
        SecretKeyFactory des = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = des.generateSecret(new DESKeySpec(sKey.getBytes()));
        return secretKey;
    }

    public String encrypt(String text) {
        try {
            Cipher cipher = makeCipher();
            SecretKey secretKey = makeKeyFactory();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return new String(Base64.encodeBase64(cipher.doFinal(text.getBytes())));
        } catch (Exception e) {
            return "";
        }
    }

    public String decrypt(String text) {
        try {
            Cipher cipher = makeCipher();
            SecretKey secretKey = makeKeyFactory();
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.decodeBase64(text.getBytes())));
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] args) {
        DESUtil tool = new DESUtil();
        String content = "wy";
        System.out.println("原文内容：" + content);
        String encrpt = null;
        try {
            encrpt = tool.encrypt(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("加密后：" + encrpt + ", 长度=" + encrpt.length());

        String descript = null;

        descript = tool.decrypt(encrpt);

        System.out.println("解密后：" + descript);
    }
}
