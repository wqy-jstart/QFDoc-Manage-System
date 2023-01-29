package cn.tedu.authuploadsystem.pojo.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色的详情实体类
 */
@Data
public class RoleStandardVO implements Serializable {


    /**
     * 角色id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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

    /**
     * 数据起始时间
     */
    @ApiModelProperty(value = "插入时间")
    private Date gmtCreate;

    /**
     * 数据修改时间
     */
    private Date gmtModified;
}
