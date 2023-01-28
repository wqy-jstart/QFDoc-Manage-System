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
 * 查询用户详情返回的实体类
 */
@Data
public class UserStandardVO implements Serializable {


    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密码(密文)
     */
    @ApiModelProperty(value = "密码(密文)")
    private String password;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String gender;

    /**
     * 年龄
     */
    @ApiModelProperty(value = "年龄")
    private Integer age;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 电子邮件
     */
    @ApiModelProperty(value = "电子邮件")
    private String email;

    /**
     * 个性签名
     */
    @ApiModelProperty(value = "个性签名")
    private String sign;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用(1.启用 0.禁用)")
    private Integer enable;

    /**
     * 数据创建时间
     */
    private Date gmtCreate;

    /**
     * 数据修改时间
     */
    private Date gmtModified;
}
