package cn.tedu.authuploadsystem.mapper;

import cn.tedu.authuploadsystem.pojo.dto.RoleUpdateDTO;
import cn.tedu.authuploadsystem.pojo.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色的数据访问层对象
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.16
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id查询角色名称列表
     * @param userId 角色Id
     * @return 返回角色名称列表
     */
    List<Long> selectToUserId(Long userId);

    /**
     * 修改角色的方法
     * @param roleUpdateDTO 修改的角色信息
     * @return 返回影响的行数
     */
    int update(RoleUpdateDTO roleUpdateDTO);
}
