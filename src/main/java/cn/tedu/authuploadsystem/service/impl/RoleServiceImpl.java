package cn.tedu.authuploadsystem.service.impl;

import cn.tedu.authuploadsystem.mapper.RoleMapper;
import cn.tedu.authuploadsystem.pojo.entity.Role;
import cn.tedu.authuploadsystem.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

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
}
