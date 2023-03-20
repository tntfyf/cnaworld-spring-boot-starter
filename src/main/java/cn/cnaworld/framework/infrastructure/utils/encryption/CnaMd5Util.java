package cn.cnaworld.framework.infrastructure.utils.encryption;

import cn.cnaworld.framework.infrastructure.utils.log.CnaLogUtil;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

/**
 * MD5 加密工具
 * @author Lucifer
 * @date 2023/3/8
 * @since 1.0.0
 */
@Slf4j
public class CnaMd5Util {
	
	private CnaMd5Util() {
	}

	/**
	 * md5 加密
	 * @author Lucifer
	 * @date 2023/3/8
	 * @since 1.0.0
	 * @param str 内容
	 * @return MD5
	 */
	public static String encrypt(String str)  {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] b = md.digest();
			int i;
			StringBuilder buf = new StringBuilder();
			for (byte value : b) {
				i = value;
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
		} catch (Exception e) {
			CnaLogUtil.error(log,"MD5加密失败:{}",str,e);
			throw new RuntimeException("MD5加密失败:"+str);
		}
		return str;
	}

}
