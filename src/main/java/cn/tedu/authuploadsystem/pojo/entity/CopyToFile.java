package cn.tedu.authuploadsystem.pojo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 指定存储空间的文件复制实体类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.22
 */
@Data
public class CopyToFile implements Serializable {

    /**
     * 存储空间的名称
     */
    @ApiModelProperty(value = "存储空间名称")
    @NotNull
    private String bucketName;

    /**
     * 当前复制的文件
     */
    @ApiModelProperty(value = "当前复制的文件名称")
    @NotNull
    private String nowFileKey;

    /**
     * 复制后的目标文件
     */
    @ApiModelProperty(value = "复制后的目标文件名称")
    @NotNull
    private String lastFileKey;

    /**
     * 是否覆盖文件
     * (默认 false。如果目标文件名已被占用，则返回错误码 614，且不做任何覆盖操作；如果指定为 true，会强制覆盖目标文件)
     */
    @ApiModelProperty(value = "是否覆盖文件")
    @NotNull
    private String isCover;
}
