package cn.cnaworld.framework.infrastructure.utils.encryption;

import cn.cnaworld.framework.infrastructure.utils.log.CnaLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * AES加密解密工具类
 * @author Lucifer
 * @date 2023/3/8
 * @since 1.0.0
 */
@Slf4j
public class CnaAesUtil {
	
	private CnaAesUtil() {}
	
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final String KEY_AES = "AES";
	private static final String BINARY = "binary";
	private static final String HEX = "hex";

	/**
	 * 16进制加密
	 * @author Lucifer
	 * @date 2023/3/8
	 * @since 1.0.0
	 * @param data 需要加密的内容
	 * @param key 加密密码
	 * @return String
	 */
	public static String encrypt(String key,String data) {
		return doAes(key, data, Cipher.ENCRYPT_MODE,HEX);
	}

	/**
	 * 16进制解密
	 * @author Lucifer
	 * @date 2023/3/8
	 * @since 1.0.0
	 * @param key 解密密钥
	 * @param data 待解密内容
	 * @return String
	 */
	public static String decrypt(String key,String data) {
		return doAes(key, data, Cipher.DECRYPT_MODE,HEX);
	}

	/**
	 * Base64加密
	 * @author Lucifer
	 * @date 2023/3/8
	 * @since 1.0.0
	 * @param data 需要加密的内容
	 * @param key 加密密码
	 * @return String
	 */
	public static String encryptBase64(String key,String data) {
		return doAes(key, data, Cipher.ENCRYPT_MODE,BINARY);
	}

	/**
	 * Base64解密
	 * @author Lucifer
	 * @date 2023/3/8
	 * @since 1.0.0
	 * @param key 解密密钥
	 * @param data 待解密内容
	 * @return String
	 */
	public static String decryptBase64(String key,String data) {
		return doAes(key, data, Cipher.DECRYPT_MODE,BINARY);
	}

	/**
	 * 加解密实现
	 * @author Lucifer
	 * @date 2023/3/8
	 * @since 1.0.0
	 * @param key 密钥
	 * @param data 待解密内容
	 * @param mode 加密、解密
	 * @return 待解密内容
	 */
	private static String doAes(String key,String data, int mode, String code) {
		try {
			if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
				return null;
			}
			//判断是加密还是解密
			boolean encrypt = mode == Cipher.ENCRYPT_MODE;
			byte[] content = null;
			//true 加密内容 false 解密内容
			if (encrypt) {
					content = data.getBytes(DEFAULT_CHARSET);
			} else {
				if(HEX.equals(code)){
					content = parseHexStr2Byte(data);
				}else{
					content = base64Dncode(data);
				}
			}
			//1.构造密钥生成器，指定为AES算法,不区分大小写
			KeyGenerator kgen = KeyGenerator.getInstance(KEY_AES);
			//2.根据ecnodeRules规则初始化密钥生成器
			//生成一个128位的随机源,根据传入的字节数组
			kgen.init(128, new SecureRandom(key.getBytes()));
			//3.产生原始对称密钥
			SecretKey secretKey = kgen.generateKey();
			//4.获得原始对称密钥的字节数组
			byte[] enCodeFormat = secretKey.getEncoded();
			//5.根据字节数组生成AES密钥
			SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, KEY_AES);
			//6.根据指定算法AES自成密码器
			// 创建密码器
			Cipher cipher = Cipher.getInstance(KEY_AES);
			//7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
			// 初始化
			cipher.init(mode, keySpec);
			byte[] result = cipher.doFinal(content);
			if (encrypt) {
				if(HEX.equals(code)){
					//将二进制转换成16进制
					return parseByte2HexStr(result);
				}else{
					return base64Encode(result);
				}
			} else {
				//将二进制转换成16进制
				return new String(result, DEFAULT_CHARSET);
			}
		} catch (Exception e) {
			CnaLogUtil.error(log,"AES 密文处理异常"+e.getMessage(),e);
		}
		return null;
	}

	/**
	 * 将二进制转换成16进制
	 * @author Lucifer
	 * @date 2023/3/8
	 * @since 1.0.0
	 * @param buf 二进制数据
	 * @return 16进制字符
	 */
	protected static String parseByte2HexStr(byte[] buf) {
		StringBuilder sb = new StringBuilder();
		for (byte b : buf) {
			String hex = Integer.toHexString(b & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * @author Lucifer
	 * @date 2023/3/8
	 * @since 1.0.0
	 * @param hexStr 16进制内容
	 * @return 二进制数组
	 */
	protected static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1) {
			return null;
		}
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * BASE64 编码
	 * @author Lucifer
	 * @date 2023/3/8
	 * @since 1.0.0
	 * @param buf 二进制数组
	 * @return 编码后内容
	 */
	public static String base64Encode(byte[] buf) {
		return new BASE64Encoder().encode(buf);
	}

	/**
	 * BASE64 解码
	 * @author Lucifer
	 * @date 2023/3/8
	 * @since 1.0.0
	 * @param data 待解密内容
	 * @return 二进制数组
	 */
	public static byte[] base64Dncode(String data) {
		byte[] byteContent = null;
		try {
			byteContent = new BASE64Decoder().decodeBuffer(data);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return byteContent;
	}

}
