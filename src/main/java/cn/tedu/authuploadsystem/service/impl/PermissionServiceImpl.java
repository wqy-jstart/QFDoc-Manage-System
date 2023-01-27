package cn.tedu.authuploadsystem.service.impl;

import cn.tedu.authuploadsystem.ex.ServiceException;
import cn.tedu.authuploadsystem.mapper.PermissionMapper;
import cn.tedu.authuploadsystem.mapper.RoleMapper;
import cn.tedu.authuploadsystem.mapper.RolePermissionMapper;
import cn.tedu.authuploadsystem.pojo.dto.AssignToPermission;
import cn.tedu.authuploadsystem.pojo.entity.*;
import cn.tedu.authuploadsystem.service.IPermissionService;
import cn.tedu.authuploadsystem.web.ServiceCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限的业务层接口实现类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.26
 */
@Slf4j
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

    // 注入权限的持久层接口
    @Autowired
    private PermissionMapper permissionMapper;

    // 注入角色权限的持久层接口
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    // 注入角色持久层接口
    @Autowired
    private RoleMapper roleMapper;

    public PermissionServiceImpl() {
        log.debug("创建业务层接口实现类：PermissionServiceImpl");
    }

    @Override
    public void assignToPermission(AssignToPermission assignToPermission) {
        log.debug("开始处理给角色：{}分配权限的功能", assignToPermission.getName());

    }

    /**
     * 查询权限列表
     *
     * @return 返回列表
     */
    @Override
    public List<Permission> selectList() {
        log.debug("开始处理查询权限列表的功能,无参！");
        return permissionMapper.selectList(null);
    }

    /**
     * 查询所有权限id
     *
     * @return 返回列表
     */
    @Override
    public List<Long> selectToId() {
        log.debug("处理查询所有权限id的功能，无参！");
        List<Permission> permissions = permissionMapper.selectList(null);
        List<Long> ids = new ArrayList<>();
        for (Permission permission : permissions) {
            ids.add(permission.getId());
        }
        return ids;
    }

    /**
     * 根据角色id查询权限列表
     *
     * @param roleId 角色id
     * @return 返回列表
     */
    @Override
    public List<Long> selectToRoleId(Long roleId) {
        log.debug("开始处理根据角色id：{}查询权限id的功能", roleId);
        Role queryRole = roleMapper.selectById(roleId);
        if (queryRole == null) {
            String message = "查询失败，该角色不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }

        return rolePermissionMapper.selectToRoleId(roleId);
    }

    /**
     * 处理分配权限时删除原来的权限与添加新权限的功能
     *
     * @param assignToPermission 分配权限的信息
     */
    @Override
    public void insertBatch(AssignToPermission assignToPermission) {
        log.debug("开始处理给角色：{}分配权限的功能", assignToPermission.getName());
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.eq("name", assignToPermission.getName());
        Role queryRole = roleMapper.selectOne(wrapper);
        if (queryRole == null) {
            String message = "查询失败，该角色不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }
        RolePermission[] rolePermissions = null;

        // 批量删除用户角色关联信息
        Long[] oldPermissionIds = assignToPermission.getOldPermissionIds();
        rolePermissions = new RolePermission[oldPermissionIds.length];
        for (int i = 0; i < oldPermissionIds.length; i++) {
            RolePermission permission = new RolePermission();
            permission.setRoleId(queryRole.getId());
            permission.setPermissionId(oldPermissionIds[i]);
            rolePermissions[i] = permission;
        }
        int rows1 = rolePermissionMapper.deleteBatch(queryRole.getId(), rolePermissions);
        if (rows1 > oldPermissionIds.length) {
            String message = "分配失败，服务器忙，请稍后再试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }

        Long[] permissionIds = assignToPermission.getPermissionIds();
        rolePermissions = new RolePermission[permissionIds.length];
        for (int i = 0; i < permissionIds.length; i++) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(queryRole.getId());
            rolePermission.setPermissionId(permissionIds[i]);
            rolePermissions[i] = rolePermission;
        }
        int rows = rolePermissionMapper.insertBatch(rolePermissions);
        if (rows > permissionIds.length) {
            String message = "添加失败，服务器忙，请稍后再试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_INSERT, message);
        }
    }
}
