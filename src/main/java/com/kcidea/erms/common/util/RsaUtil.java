package com.kcidea.erms.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
@Slf4j
@Component
public class RsaUtil {

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 128;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * RSA秘钥对大小
     */
    private static final int MAX_KEY = 1024;


    private static String filePath;


    @Value("${my-config.key-dir}")
    public void setFilePath(String filePath) {
        RsaUtil.filePath = filePath;
    }


    /**
     * 获取密钥对
     * @return 秘钥
     * @author huxubin
     * @date 2021/11/10 9:22
     */
    public static KeyPair getKeyPair() {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(MAX_KEY);
            keyPair = generator.generateKeyPair();
        } catch (Exception e) {
            log.error("获取秘钥对失败：" + e.getMessage(), e);
        }
        return keyPair;
    }

    /**
     * 获取私钥
     * @param privateKeyStr 私钥字符串
     * @return 私钥
     * @author huxubin
     * @date 2021/11/10 9:23
     */
    public static PrivateKey getPrivateKey(String privateKeyStr) {
        PrivateKey privateKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] decodedKey = Base64.decodeBase64(privateKeyStr.getBytes());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            log.error("获取秘钥对失败：" + e.getMessage(), e);
        }
        return privateKey;
    }

    /**
     * 获取公钥
     * @param publicKeyStr 公钥字符串
     * @return 公钥
     * @author huxubin
     * @date 2021/11/10 9:23
     */
    public static PublicKey getPublicKey(String publicKeyStr) {
        PublicKey publicKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] decodedKey = Base64.decodeBase64(publicKeyStr.getBytes());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            log.error("获取秘钥对失败：" + e.getMessage(), e);
        }
        return publicKey;

    }

    /**
     * RSA加密，使用默认的公钥
     * @param data 待加密的数据
     * @return 加密后的秘钥
     * @author huxubin
     * @date 2021/11/10 9:23
     */
    public static String encryptDefaultKey(String data) {
        //获取公钥
        PublicKey publicKey = readPublicKey();
        if (null != publicKey) {
            //加密
            return encrypt(data, publicKey);
        }
        return  "";
    }

    /**
     * RSA解密，使用默认的公钥
     * @param data 加密的数据
     * @return 解密内容
     * @author huxubin
     * @date 2021/11/10 9:24
     */
    public static String decryptDefaultKey(String data) {
        //获取私钥
        PrivateKey privateKey = readPrivateKey();
        if (null != privateKey) {
            //解密
            return decrypt(data, privateKey);
        }
        return  "";
    }

    /**
     * RSA加密
     * @param data 待加密数据
     * @param publicKey 公钥
     * @return RSA加密后的数据
     * @author huxubin
     * @date 2021/11/10 9:24
     */
    public static String encrypt(String data, PublicKey publicKey) {
        String encryptStr = "";
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int inputLen = data.getBytes().length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offset = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offset > 0) {
                if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
            // 加密后的字符串
            encryptStr = new String(Base64.encodeBase64String(encryptedData));
        } catch (Exception e) {
            log.error("获取秘钥对失败：" + e.getMessage(), e);
        }
        return encryptStr;

    }

    /**
     * RSA解密
     * @param data 待解密数据
     * @param privateKey 私钥
     * @return 解密后的数据
     * @author huxubin
     * @date 2021/11/10 9:25
     */
    public static String decrypt(String data, PrivateKey privateKey) {
        String decryptStr = "";
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] dataBytes = Base64.decodeBase64(data);
            int inputLen = dataBytes.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offset = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offset > 0) {
                if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            // 解密后的内容
            decryptStr = new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("RSA解密失败：" + e.getMessage(), e);
        }
        return decryptStr;
    }

    /**
     * 签名
     * @param data 待签名数据
     * @param privateKey 私钥
     * @return 签名后的数据
     * @author huxubin
     * @date 2021/11/10 9:25
     */
    public static String sign(String data, PrivateKey privateKey) {
        String sign = "";
        try {
            byte[] keyBytes = privateKey.getEncoded();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey key = keyFactory.generatePrivate(keySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(key);
            signature.update(data.getBytes());
            sign = new String(Base64.encodeBase64(signature.sign()));
        } catch (Exception e) {
            log.error("获取秘钥对失败：" + e.getMessage(), e);
        }
        return sign;
    }

    /**
     * 验签
     * @param srcData 原始字符串
     * @param publicKey 公钥
     * @param sign 签名
     * @return 是否验签通过
     * @author huxubin
     * @date 2021/11/10 9:26
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        boolean verifyFlag = false;
        try {
            byte[] keyBytes = publicKey.getEncoded();
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey key = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(key);
            signature.update(srcData.getBytes());
            verifyFlag = signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception e) {
            log.error("获取秘钥对失败：" + e.getMessage(), e);
        }
        return verifyFlag;
    }

    /**
     * 读取公钥
     * @return 公钥
     * @author huxubin
     * @date 2021/11/10 9:26
     */
    public static PublicKey readPublicKey() {
        BASE64Decoder base64decoder = new BASE64Decoder();
        PublicKey publicKey = null;
        try {
            //读取pem证书
            BufferedReader br = new BufferedReader(new FileReader(filePath + "key.pem"));
            String s = br.readLine();
            StringBuilder publickey = new StringBuilder();
            while (s!= null && s.charAt(0) != '-') {
                publickey.append(s).append("\r");
                s = br.readLine();
            }
            byte[] publicKeybyte = base64decoder.decodeBuffer(publickey.toString());
            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeybyte);
            publicKey = kf.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    /**
     * 读取私钥
     * @return 私钥
     * @author huxubin
     * @date 2021/11/10 9:27
     */
    public static PrivateKey readPrivateKey() {
        BASE64Decoder base64decoder = new BASE64Decoder();
        PrivateKey privateKey = null;
        try {
            //读取pem证书
            BufferedReader br = new BufferedReader(new FileReader(filePath + "pKey.pem"));
            String s = br.readLine();
            StringBuilder stringBuilder = new StringBuilder();
            while (s!= null && s.charAt(0) != '-') {
                stringBuilder.append(s).append("\r");
                s = br.readLine();
            }
            byte[] privateKeyByte = base64decoder.decodeBuffer(stringBuilder.toString());
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyByte);
            privateKey = kf.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }

}
