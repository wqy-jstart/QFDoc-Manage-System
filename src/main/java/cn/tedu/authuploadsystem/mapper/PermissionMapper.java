package cn.tedu.authuploadsystem.mapper;

import cn.tedu.authuploadsystem.pojo.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 权限的持久层接口
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.26
 */
@Repository
public interface PermissionMapper extends BaseMapper<Permission> {
}
