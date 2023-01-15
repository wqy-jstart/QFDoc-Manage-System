package cn.tedu.authuploadsystem.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户角色的实体类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.16
 */
@Data
@TableName("a_user_role")
public class UserRole implements Serializable {

    /**
     * 数据id
     */
    private Long id;

    /**
     * 管理员id
     */
    private Long adminId;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 数据创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 数据最后修改时间
     */
    private LocalDateTime gmtModified;
}
