package cn.tedu.authuploadsystem.service.impl;

import cn.tedu.authuploadsystem.ex.ServiceException;
import cn.tedu.authuploadsystem.mapper.RoleMapper;
import cn.tedu.authuploadsystem.mapper.UserMapper;
import cn.tedu.authuploadsystem.pojo.dto.RoleAddNewDTO;
import cn.tedu.authuploadsystem.pojo.dto.RoleUpdateDTO;
import cn.tedu.authuploadsystem.pojo.entity.Role;
import cn.tedu.authuploadsystem.pojo.entity.User;
import cn.tedu.authuploadsystem.repo.IRoleRedisRepository;
import cn.tedu.authuploadsystem.service.IRoleService;
import cn.tedu.authuploadsystem.web.ServiceCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    // 注入角色Redis缓存接口
    @Autowired
    private IRoleRedisRepository roleRedisRepository;

    public RoleServiceImpl() {
        log.debug("创建业务层实现类：RoleServiceImpl");
    }

    /**
     * 处理添加角色的功能
     *
     * @param roleAddNewDTO 添加角色的信息
     */
    @Override
    public void insert(RoleAddNewDTO roleAddNewDTO) {
        log.debug("开始处理添加角色的业务，参数：{}", roleAddNewDTO);
        // 检查角色名称是否存在
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.eq("name", roleAddNewDTO.getName());
        Role queryRole = roleMapper.selectOne(wrapper);
        if (queryRole != null) {
            String message = "添加失败，该用户名已存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        Role role = new Role();
        BeanUtils.copyProperties(roleAddNewDTO, role);
        int rows = roleMapper.insert(role);
        if (rows > 1) {
            String message = "添加失败，服务器忙，请稍后再试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_INSERT, message);
        }
    }

    /**
     * 执行修改角色的业务
     *
     * @param roleUpdateDTO 修改角色的信息
     */
    @Override
    public void update(RoleUpdateDTO roleUpdateDTO) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.ne("id", roleUpdateDTO.getId());
        wrapper.eq("name", roleUpdateDTO.getName());
        Role queryRole = roleMapper.selectOne(wrapper);
        if (queryRole != null) {
            String message = "修改失败，该角色名称已存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }
        log.debug("开始处理修改角色信息的功能，参数：{}", roleUpdateDTO);
        int rows = roleMapper.update(roleUpdateDTO);
        if (rows > 1) {
            String message = "修改失败，服务器忙，请稍后再试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_UPDATE, message);
        }
    }

    /**
     * 处理查询角色列表的功能
     *
     * @return 返回列表
     */
    @Override
    public List<Role> selectList() {
        log.debug("开始处理查询角色列表的功能！无参");
        return roleRedisRepository.list();
    }

    /**
     * 根据角色id查询角色详情信息
     *
     * @param roleId 角色Id
     * @return 返回角色详情类
     */
    @Override
    public Role selectById(Long roleId) {
        log.debug("开始处理查询id为：{}的角色详情", roleId);
        log.debug("开始查询id为：{}的角色缓存...", roleId);
        Role role = roleRedisRepository.get(roleId);
        if (role != null) {
            log.debug("命中缓存，即将返回！");
            return role;
        }

        log.debug("未命中缓存，开始向数据库中查询id为：{}的角色数据...", roleId);
        role = roleMapper.selectById(roleId);
        if (role == null) {
            String message = "查询失败，该角色不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }

        log.debug("从数据库中查询到id为：{}的角色数据,将查询结果放入缓存中！", roleId);
        roleRedisRepository.save(role);
        log.debug("开始返回结果！");

        return role;
    }

    /**
     * 根据用户id查询角色的名称列表
     *
     * @param userId 用户id
     * @return 返回角色列表
     */
    @Override
    public List<Long> selectToUserId(Long userId) {
        log.debug("开始处理根据用户id查询角色名称列表，参数:{}", userId);
        User queryUser = userMapper.selectById(userId);
        if (queryUser == null) {
            String message = "查询失败，该用户不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }

        return roleMapper.selectToUserId(userId);
    }

    /**
     * 查询所有角色Id
     *
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

    /**
     * 重建角色缓存
     */
    @Override
    public void rebuildCache() {
        // 重建角色的缓存
        log.debug("准备删除Redis缓存中的角色数据...");
        Long countToRole = roleRedisRepository.deleteAll();// 清除缓存中的数据,防止缓存堆积过多,显示的列表数据冗余
        log.debug("删除Redis缓存中的角色数据,完成,数量为：{}", countToRole);

        log.debug("准备从数据库中读取角色列表...");
        List<Role> listToRole = roleMapper.selectList(null);
        log.debug("从数据库中读取角色列表，完成！");

        log.debug("准备将角色列表写入到Redis缓存...");
        roleRedisRepository.save(listToRole);
        log.debug("将角色列表写入到Redis缓存，完成！");

        log.debug("准备将各角色详情写入Redis缓存...");
        for (Role role : listToRole) {
            Long id = role.getId();
            Role roleStandardVO = roleMapper.selectById(id);
            roleRedisRepository.save(roleStandardVO);
        }
        log.debug("将各角色详情写入到Redis缓存中,完成!");
    }
}
