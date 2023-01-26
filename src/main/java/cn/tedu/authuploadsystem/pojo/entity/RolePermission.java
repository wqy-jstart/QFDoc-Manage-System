package cn.tedu.authuploadsystem.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色权限实体类
 */
@Data
@TableName("a_role_permission")
public class RolePermission implements Serializable {

    /**
     * 数据id
     */
    @ApiModelProperty(value = "数据id")
    private Long id;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private Long roleId;

    /**
     * 权限id
     */
    @ApiModelProperty(value = "权限id")
    private Long permissionId;
    /**
     * 数据起始时间
     */
    @ApiModelProperty(value = "插入时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    /**
     * 数据修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}
