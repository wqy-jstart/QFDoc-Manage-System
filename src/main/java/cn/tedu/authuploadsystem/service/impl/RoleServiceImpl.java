package cn.tedu.authuploadsystem.service.impl;

import cn.tedu.authuploadsystem.ex.ServiceException;
import cn.tedu.authuploadsystem.mapper.RoleMapper;
import cn.tedu.authuploadsystem.mapper.UserMapper;
import cn.tedu.authuploadsystem.pojo.entity.Role;
import cn.tedu.authuploadsystem.pojo.entity.User;
import cn.tedu.authuploadsystem.service.IRoleService;
import cn.tedu.authuploadsystem.web.ServiceCode;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色的业务层接口实现类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.25
 */
@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    // 注入持久层接口
    @Autowired
    private RoleMapper roleMapper;

    // 注入用户持久层接口
    @Autowired
    private UserMapper userMapper;

    public RoleServiceImpl(){
        log.debug("创建业务层实现类：RoleServiceImpl");
    }

    /**
     * 处理查询角色列表的功能
     * @return 返回列表
     */
    @Override
    public List<Role> selectList() {
        log.debug("开始处理查询角色列表的功能！无参");
        return roleMapper.selectList(null);
    }

    /**
     * 根据角色id查询角色详情信息
     * @param roleId 角色Id
     * @return 返回角色详情类
     */
    @Override
    public Role selectById(Long roleId) {
        log.debug("开始处理查询id为：{}的角色详情",roleId);
        Role queryRole = roleMapper.selectById(roleId);
        if (queryRole == null){
            String message = "查询失败，该角色不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,message);
        }

        return queryRole;
    }

    /**
     * 根据用户id查询角色的名称列表
     * @param userId 用户id
     * @return 返回角色列表
     */
    @Override
    public List<Long> selectToUserId(Long userId) {
        log.debug("开始处理根据用户id查询角色名称列表，参数:{}",userId);
        User queryUser = userMapper.selectById(userId);
        if (queryUser == null){
            String message = "查询失败，该用户不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,message);
        }

        return roleMapper.selectToUserId(userId);
    }

    /**
     * 查询所有角色Id
     * @return 返回列表
     */
    @Override
    public List<Long> selectRoleId() {
        log.debug("开始处理查询所有角色Id的功能，无参！");
        List<Role> roles = roleMapper.selectList(null);
        List<Long> roleIds = new ArrayList<>();
        for (Role role : roles) {
            roleIds.add(role.getId());
        }
        return roleIds;
    }
}
