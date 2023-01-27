package cn.tedu.authuploadsystem.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 添加角色的DTO类
 */
@Data
public class RoleAddNewDTO implements Serializable {

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String name;

    /**
     * 角色描述
     */
    @ApiModelProperty(value = "角色描述")
    private String description;

    /**
     * 角色排序
     */
    @ApiModelProperty(value = "角色排序")
    private Integer sort;
}
