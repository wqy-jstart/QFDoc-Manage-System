package cn.tedu.authuploadsystem.service.impl;

import cn.tedu.authuploadsystem.ex.ServiceException;
import cn.tedu.authuploadsystem.mapper.UserMapper;
import cn.tedu.authuploadsystem.mapper.UserRoleMapper;
import cn.tedu.authuploadsystem.pojo.dto.AssignToRole;
import cn.tedu.authuploadsystem.pojo.dto.UserAddNewDTO;
import cn.tedu.authuploadsystem.pojo.entity.User;
import cn.tedu.authuploadsystem.pojo.entity.UserRole;
import cn.tedu.authuploadsystem.service.IUserRoleService;
import cn.tedu.authuploadsystem.web.ServiceCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户角色关联表的业务层接口
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.25
 */
@Slf4j
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    // 注入用户角色持久层接口
    @Autowired
    private UserRoleMapper userRoleMapper;

    // 注入用户持久层接口
    @Autowired
    private UserMapper userMapper;

    /**
     * 批量将分配的角色信息插入到用户角色关联表中
     *
     * @param assignToRole 用户角色数组
     */
    @Override
    public void insertBatch(AssignToRole assignToRole) {
        log.debug("开始处理批量插入用户角色关联信息");
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", assignToRole.getUsername());
        User queryUser = userMapper.selectOne(wrapper);
        if (queryUser == null) {
            String message = "查询失败，该用户名不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }
        UserRole[] userRoles = null;

        // 批量删除用户角色关联信息
        Long[] oldRoleIds = assignToRole.getOldRoleIds();
        userRoles = new UserRole[oldRoleIds.length];
        for (int i = 0; i < oldRoleIds.length; i++) {
            UserRole userRole = new UserRole();
            userRole.setAdminId(queryUser.getId());
            userRole.setRoleId(oldRoleIds[i]);
            userRoles[i] = userRole;
        }
        int rows1 = userRoleMapper.deleteBatch(queryUser.getId(), userRoles);
        if (rows1 > oldRoleIds.length) {
            String message = "分配失败，服务器忙，请稍后再试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }

        Long[] roleIds = assignToRole.getRoleIds();
        userRoles = new UserRole[roleIds.length];
        for (int i = 0; i < roleIds.length; i++) {
            UserRole userRole = new UserRole();
            userRole.setAdminId(queryUser.getId());
            userRole.setRoleId(roleIds[i]);
            userRoles[i] = userRole;
        }
        int rows = userRoleMapper.insertBatch(userRoles);
        if (rows > roleIds.length) {
            String message = "添加失败，服务器忙，请稍后再试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_INSERT, message);
        }
    }
}
