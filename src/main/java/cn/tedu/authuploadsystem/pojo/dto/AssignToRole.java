package cn.tedu.authuploadsystem.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 分配角色实体类
 */
@Data
public class AssignToRole implements Serializable {
    private String username;
    private Long[] roleIds;
}
