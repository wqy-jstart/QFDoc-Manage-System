package cn.tedu.authuploadsystem.mapper;

import cn.tedu.authuploadsystem.pojo.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 用户角色的数据访问层对象
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.16
 */
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 根据用户批量插入用户角色关联表
     * @param userRoles 批量查询关联表信息需要的数组
     */
    int insertBatch(UserRole[] userRoles);

    /**
     * 批量删除分配前的角色id
     * @param userRoles 用户角色信息
     * @return 返回删除的个数
     */
    int deleteBatch(Long userId,UserRole[] userRoles);
}
