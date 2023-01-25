package cn.tedu.authuploadsystem.service;

import cn.tedu.authuploadsystem.pojo.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色的业务层接口类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.25
 */
@Transactional
public interface IRoleService extends IService<Role>{

    /**
     * 查询角色列表
     * @return 返回角色的Lit列表
     */
    List<Role> selectList();

    /**
     * 根据用户id查询对应的角色名称列表
     * @param userId 用户id
     * @return 返回角色列表
     */
    List<Long> selectToUserId(Long userId);

    /**
     * 查询所有角色Id
     * @return 返回Id列表
     */
    List<Long> selectRoleId();
}
