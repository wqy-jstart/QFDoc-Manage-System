package cn.tedu.authuploadsystem.mapper;

import cn.tedu.authuploadsystem.pojo.entity.RolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色权限关联表的持久层接口
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.26
 */
@Repository
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    /**
     * 根据角色id查询权限id
     * @param roleId 角色id
     * @return 返回权限id列表
     */
    List<Long> selectToRoleId(Long roleId);

    /**
     * 根据角色id批量插入角色权限关联表
     * @param rolePermissions 批量插入关联表信息需要的数组
     */
    int insertBatch(RolePermission[] rolePermissions);

    /**
     * 批量删除分配前的角色id
     * @param roleId 角色id
     * @param rolePermissions 角色权限数组
     * @return 返回删除的数量
     */
    int deleteBatch(Long roleId,RolePermission[] rolePermissions);
}
