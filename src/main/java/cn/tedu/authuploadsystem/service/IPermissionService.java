package cn.tedu.authuploadsystem.service;

import cn.tedu.authuploadsystem.pojo.dto.AssignToPermission;
import cn.tedu.authuploadsystem.pojo.dto.AssignToRole;
import cn.tedu.authuploadsystem.pojo.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限的业务层接口
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.26
 */
@Transactional
public interface IPermissionService extends IService<Permission> {

    /**
     * 分配权限的功能
     * @param assignToPermission 分配权限的信息
     */
    void assignToPermission(AssignToPermission assignToPermission);

    /**
     * 查询权限的列表
     * @return 返回列表信息
     */
    List<Permission> selectList();

    /**
     * 查询所有的权限id
     * @return 返回id列表
     */
    List<Long> selectToId();

    /**
     * 根据角色id查询权限id
     * @param roleId 角色id
     * @return 返回列表
     */
    List<Long> selectToRoleId(Long roleId);

    /**
     * 分配权限
     * @param assignToPermission 分配权限的信息
     */
    void insertBatch(AssignToPermission assignToPermission);
}
