package cn.tedu.authuploadsystem.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
    @ApiModelProperty(value = "用户角色ID")
    private Long id;

    /**
     * 管理员id
     */
    @ApiModelProperty(value = "管理员ID")
    private Long adminId;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色Id")
    private Long roleId;

    /**
     * 数据创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    /**
     * 数据最后修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}
