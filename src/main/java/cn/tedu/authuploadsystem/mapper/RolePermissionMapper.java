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
}
