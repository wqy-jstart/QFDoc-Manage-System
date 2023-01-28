package cn.tedu.authuploadsystem.schdule;

import cn.tedu.authuploadsystem.mapper.RoleMapper;
import cn.tedu.authuploadsystem.service.IBucketService;
import cn.tedu.authuploadsystem.service.IRoleService;
import cn.tedu.authuploadsystem.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 策略二：利用执行计划来完成缓存的加载，计划重建缓存时间
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.28
 */
@Slf4j
//@Component // 声明一个组件类
public class CacheSchedule {

    /*
      bucket文件列表
      用户列表
      角色列表
     */

    // 注入bucket持久层接口
    @Autowired
    private IBucketService bucketService;

    // 注入用户持久层接口
    @Autowired
    private IUserService userService;

    // 注入角色持久层接口
    @Autowired
    private IRoleService roleService;
}
