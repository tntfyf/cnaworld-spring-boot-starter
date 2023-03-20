package cn.cnaworld.framework.infrastructure.utils.code;


import cn.cnaworld.framework.infrastructure.component.mybatisplus.snowflake.IdGenerator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 编码、ID生成工具
 *
 * @author Lucifer
 * @date 2023/3/8
 * @since 1.0.0
 */
public class CnaCodeUtil {

    private CnaCodeUtil() {

    }

    /**
     * 获取36位UUID:例如
     * 48625b5c-70f8-4f24-9a2a-5a2c808bdcee
     * @author Lucifer
     * @date 2023/3/8
     * @since 1.0.0
     * @return String
     */
    public static String getUuid() {
        return UUID.fastUUID().toString();
    }

    /**
     * 获取32位简化UUID，去掉了横线:例如
     * 4a59e396f07d48a8b258ec0d52551e62
     * @author Lucifer
     * @date 2023/3/8
     * @since 1.0.0
     * @return String
     */
    public static String getSimpleUuid() {
        return UUID.fastUUID().toString(true);
    }

    /**
     * 获取16位数字不可重复ID
     * 6000001740109613
     * @author Lucifer
     * @date 2023/3/8
     * @since 1.0.0
     * @return Long
     */
    public static Long getId() {
        int first = new Random(10).nextInt(8) + 1;
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        //有可能是负数
        if (hashCodeV < 0) {
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0 ，15 代表长度为15 ，d 代表参数为正数型
        return Long.valueOf(first + String.format("%015d", hashCodeV));
    }

    /**
     * 生成N位随机字符串 ，含数字 + 大小写 ,可以用于生成验证码
     * rRXkxunCA4yDBUaS
     * ECFVT0UM8ZXPNL14
     * 9XPRA3
     * @author Lucifer
     * @date 2023/3/8
     * @since 1.0.0
     * @param number 包含数字
     * @param uppercase 包含大写字母
     * @param lowercase 包含小写字母
     * @param repeat 可否重复
     * @param bit 位
     * @return String
     */
    public static String getGuid(boolean number,boolean uppercase,boolean lowercase,boolean repeat,int bit) {
        if (bit < 0 || bit == 0) {
            throw new IllegalArgumentException("位小于或者等于0");
        }
        StringBuilder model = new StringBuilder();
        String numberModel = "0123456789";
        String uppercaseModel = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseModel = "abcdefghijklmnopqrstuvwxyz";
        if (number){
            model.append(numberModel);
        }
        if (uppercase){
            model.append(uppercaseModel);
        }
        if (lowercase){
            model.append(lowercaseModel);
        }
        char[] m = model.toString().toCharArray();

        if (m.length < 0 || m.length==0) {
            throw new IllegalArgumentException("无字符源");
        }

        if (!repeat && m.length < bit) {
            throw new IllegalArgumentException("字符源小于位数");
        }
        List<Character> list = new ArrayList<>() ;
        for (char char1:m) {
            list.add(char1);
        }
        StringBuilder randomcode = new StringBuilder();
        Random rd = new SecureRandom();
        for (int j = 0; j < bit; j++) {
            int i = rd.nextInt(list.size());
            Character c =list.get(i);
            if (!repeat){
                list.remove(i);
            }
            randomcode.append(c);
        }
        return randomcode.toString();
    }

    /**
     * 获取16位雪花ID
     * @author Lucifer
     * @date 2023/3/8
     * @since 1.0.0
     * @return long 16位
     */
    public static long getSnowflakeId(){
        return IdGenerator.generateId();
    }

    /**
     * 获取字符类型16位雪花ID
     * @author Lucifer
     * @date 2023/3/8
     * @since 1.0.0
     * @return String 16位
     */
    public static String getSnowflakeIdString(){
        return String.valueOf(IdGenerator.generateId());
    }

}
