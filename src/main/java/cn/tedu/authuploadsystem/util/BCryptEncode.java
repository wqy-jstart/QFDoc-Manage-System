package cn.tedu.authuploadsystem.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * 利用BCrypt解码的工具类
 *
 * @author java@Wqy
 * @version 0.0.1
 */
@Slf4j
public class BCryptEncode {
    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encryptionPassword(String rawPassword){
        log.debug("开始对传入的明文进行加密!");
        String encode = passwordEncoder.encode(rawPassword);
        return encode;
    }
}
