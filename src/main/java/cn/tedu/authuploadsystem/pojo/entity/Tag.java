package cn.tedu.authuploadsystem.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 存储空间的标签实体类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.23
 */
@Data
public class Tag implements Serializable {
    private String key;
    private String value;
    public Tag(String key,String value){
        this.key = key;
        this.value = value;
    }
}
