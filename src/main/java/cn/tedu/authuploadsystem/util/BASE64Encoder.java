package cn.tedu.authuploadsystem.util;

import lombok.extern.slf4j.Slf4j;

/**
 * BASE64Encoder加密工具类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.19
 */
@Slf4j
public class BASE64Encoder {
    /**
     * 加密的静态方法
     * @param str 传入加密参数
     * @return 返回加密字符串信息
     */
    public static String encode(byte[] str) {
        return new sun.misc.BASE64Encoder().encode(str);
    }
}
