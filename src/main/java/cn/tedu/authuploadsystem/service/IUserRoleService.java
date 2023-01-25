package cn.tedu.authuploadsystem.service;

import cn.tedu.authuploadsystem.pojo.dto.AssignToRole;
import cn.tedu.authuploadsystem.pojo.dto.UserAddNewDTO;
import cn.tedu.authuploadsystem.pojo.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户角色关联的业务层接口类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.25
 */
@Transactional
public interface IUserRoleService extends IService<UserRole> {

    /**
     * 批量插入用户角色关联表信息
     * @param assignToRole 分配信息
     */
    void insertBatch(AssignToRole assignToRole);
}
