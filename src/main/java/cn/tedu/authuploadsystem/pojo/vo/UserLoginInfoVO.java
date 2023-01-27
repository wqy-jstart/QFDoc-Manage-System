package cn.tedu.authuploadsystem.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * 这是登录相关的管理员VO类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.27
 */
@Data
public class UserLoginInfoVO {

    /**
     * 管理员id
     */
    private Long id;

    /**
     * 管理员用户名
     */
    private String username;

    /**
     * 管理员密码(密文)
     */
    private String password;
    /**
     * 是否启用(1=启用,0=禁用)
     */
    private Integer enable;

    /**
     * 权限列表
     */
    private List<String> permissions;

}
