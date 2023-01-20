package cn.tedu.authuploadsystem.pojo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 存储空间返回列表的实体类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.20
 */
@Data
public class Bucket implements Serializable {

    /**
     * 文件名
     */
    @ApiModelProperty(value = "文件名")
    private String key;

    /**
     * 文件哈希值
     */
    @ApiModelProperty(value = "文件唯一哈希值")
    private String hash;

    /**
     * 文件大小
     */
    @ApiModelProperty(value = "文件大小")
    private long fsize;

    /**
     * 文件上传时间
     */
    @ApiModelProperty(value = "文件上传时间")
    private long putTime;

    /**
     * 文件MIME类型
     */
    @ApiModelProperty(value = "文件MIME类型")
    private String mimeType;

    /**
     * 文件访问权限(0=公开；1=私有)
     */
    @ApiModelProperty(value = "文件访问权限(0=公开；1=私有)")
    private Integer type;
}
