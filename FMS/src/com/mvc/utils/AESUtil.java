package com.mvc.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

/**
 * 采用MD5加密解密
 * 
 * @author tfq
 * @datetime 2011-10-13
 */
public class AESUtil {
	
	static Logger logger = Logger.getLogger(AESUtil.class);

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static String encrypt(String content, String password) {
		try {
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, generateKey(password));// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return parseByte2HexStr(result); // 加密
		} catch (Exception e) {
			logger.error("encrypt: " + e.toString());
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	public static String decrypt(String content, String password) {
		try {
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, generateKey(password));// 初始化
			byte[] result = cipher.doFinal(parseHexStr2Byte(content));
			return new String(result); // 加密
		} catch (Exception e) {
			logger.error("decrypt: " + e.toString());
		}
		return null;
	}
	
	/** 
	 * 获得秘密密钥 
	 *  
	 * @param secretKey 
	 * @return 
	 * @throws NoSuchAlgorithmException  
	 */  
	private static SecretKey generateKey(String password) throws NoSuchAlgorithmException{  
	    //防止linux下 随机生成key
	    SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");  
	    secureRandom.setSeed(password.getBytes());  
       
	    // 为我们选择的DES算法生成一个KeyGenerator对象  
	    KeyGenerator kg = null;  
	    try {  
	        kg = KeyGenerator.getInstance("AES");  
	    } catch (NoSuchAlgorithmException e) {  
	    }  
	    kg.init(128, secureRandom);  
	    
	    SecretKey secretKey = kg.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
	      
	    // 生成密钥  
	    return key;  
	}  
	
	private static String parseByte2HexStr(byte buf[]) {  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < buf.length; i++) {  
            String hex = Integer.toHexString(buf[i] & 0xFF);  
            if (hex.length() == 1) {  
                hex = '0' + hex;  
            }  
            sb.append(hex.toUpperCase());  
        }  
        return sb.toString();  
    }
	
	private static byte[] parseHexStr2Byte(String hexStr) { 
        if (hexStr.length() < 1) 
            return null; 
        byte[] result = new byte[hexStr.length()/2]; 
        for (int i = 0;i< hexStr.length()/2; i++) { 
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16); 
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16); 
            result[i] = (byte) (high * 16 + low); 
        } 
        return result; 
    }  

	// 测试主函数
	public static void main(String args[]) {

		//String content = "094F6E29DEE76D9754C8F4FD861515BC7A8DC99E4662BBA5cnmobile111.47.9.76";
		//String password = "12345";
		String content="d006637";
		String password = "111111";

		System.out.println("加密前：" + content);
		System.out.println("加密 密码：" + password);
		
		String encryptResult = encrypt(content, password);  
        System.out.println("密文：" + encryptResult);  
          
        String decryptResult = decrypt(encryptResult, password);  
        System.out.println("解密后：" + decryptResult);  
		
	

	}
}
