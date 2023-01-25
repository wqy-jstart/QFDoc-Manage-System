package cn.tedu.authuploadsystem.controller;

import cn.tedu.authuploadsystem.pojo.entity.Role;
import cn.tedu.authuploadsystem.service.IRoleService;
import cn.tedu.authuploadsystem.web.JsonResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色的控制器类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.25
 */
@Api(tags = "角色管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/roles")
public class RoleController {

    // 注入业务层接口
    @Autowired
    private IRoleService roleService;

    public RoleController() {
        log.debug("创建控制器类：RoleController");
    }

    /**
     * 处理查询角色列表的请求
     *
     * @return 返回列表
     */
    @GetMapping("")
    public JsonResult<List<Role>> selectList() {
        log.debug("开始处理查询角色列表的请求，无参！");
        List<Role> roles = roleService.selectList();
        return JsonResult.ok(roles);
    }
}
