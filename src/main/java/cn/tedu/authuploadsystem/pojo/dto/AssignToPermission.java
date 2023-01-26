package cn.tedu.authuploadsystem.pojo.dto;

import lombok.Data;

/**
 * 分配权限的实体类
 */
@Data
public class AssignToPermission {

    private String name;

    private Long[] permissionIds;

    private Long[] oldPermissionIds;
}
