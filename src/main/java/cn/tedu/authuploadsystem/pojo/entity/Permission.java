package cn.tedu.authuploadsystem.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限的实体类
 */
@Data
@TableName("a_permission")
public class Permission implements Serializable {

    /**
     * 数据id
     */
    @ApiModelProperty(value = "权限id")
    private Long id;

    /**
     * 权限名称
     */
    @ApiModelProperty(value = "权限名称")
    private String name;

    /**
     * 权限值
     */
    @ApiModelProperty(value = "权限值")
    private String value;

    /**
     * 权限描述
     */
    @ApiModelProperty(value = "权限描述")
    private String description;

    /**
     * 权限排序
     */
    @ApiModelProperty(value = "权限排序")
    private Integer sort;

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
