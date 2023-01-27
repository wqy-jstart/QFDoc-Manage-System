package cn.tedu.authuploadsystem.controller;

import cn.tedu.authuploadsystem.pojo.dto.RoleAddNewDTO;
import cn.tedu.authuploadsystem.pojo.dto.RoleUpdateDTO;
import cn.tedu.authuploadsystem.pojo.entity.Role;
import cn.tedu.authuploadsystem.service.IRoleService;
import cn.tedu.authuploadsystem.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     * 处理添加角色的请求
     *
     * @param roleAddNewDTO 添加的角色信息
     * @return 返回结果集
     */
    @ApiOperation("添加角色")
    @ApiOperationSupport(order = 100)
    @PostMapping("/insert")
    public JsonResult<Void> insert(RoleAddNewDTO roleAddNewDTO) {
        log.debug("开始处理添加角色的请求，参数：{}", roleAddNewDTO);
        roleService.insert(roleAddNewDTO);
        return JsonResult.ok();
    }

    /**
     * 执行修改角色的请求
     *
     * @param roleUpdateDTO 修改角色的信息
     * @return 返回结果集
     */
    @ApiOperation("根据id修改角色")
    @ApiOperationSupport(order = 100)
    @PostMapping("/update")
    public JsonResult<Void> update(RoleUpdateDTO roleUpdateDTO) {
        log.debug("开始处理修改角色的业务,无参！");
        roleService.update(roleUpdateDTO);
        return JsonResult.ok();
    }

    /**
     * 处理查询角色列表的请求
     *
     * @return 返回列表
     */
    @ApiOperation("查询角色列表")
    @ApiOperationSupport(order = 500)
    @GetMapping("")
    public JsonResult<List<Role>> selectList() {
        log.debug("开始处理查询角色列表的请求，无参！");
        List<Role> roles = roleService.selectList();
        return JsonResult.ok(roles);
    }

    /**
     * 根据id查询角色详情
     *
     * @param roleId 角色id
     * @return 返回角色详情
     */
    @ApiOperation("根据id查询角色详情")
    @ApiOperationSupport(order = 500)
    @ApiImplicitParam(name = "roleId", value = "角色Id", required = true, dataType = "long")
    @PostMapping("/{roleId:[0-9]+}/selectById")
    public JsonResult<Role> selectById(@PathVariable Long roleId) {
        log.debug("开始处理查询id为：{}的角色详情", roleId);
        Role role = roleService.selectById(roleId);
        return JsonResult.ok(role);
    }

    /**
     * 根据用户Id查询角色名称列表
     *
     * @param userId 用户id
     * @return 返回结果集
     */
    @ApiOperation("根据用户Id查询角色ID列表")
    @ApiOperationSupport(order = 501)
    @ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "long")
    @PostMapping("/{userId:[0-9]+}/selectToUserId")
    public JsonResult<List<Long>> selectToUserId(@Range(min = 1, message = "查询失败，参数无效！")
                                                 @PathVariable Long userId) {
        log.debug("开始处理查询用户Id：{}的角色名称列表功能", userId);
        List<Long> strings = roleService.selectToUserId(userId);
        return JsonResult.ok(strings);
    }

    /**
     * 查询所有角色Id
     *
     * @return 返回列表
     */
    @ApiOperation("查询所有角色Id")
    @ApiOperationSupport(order = 502)
    @GetMapping("/selectRoleIds")
    public JsonResult<List<Long>> selectRoleIds() {
        log.debug("开始处理查询所有角色Id的功能");
        List<Long> longs = roleService.selectRoleId();
        return JsonResult.ok(longs);
    }
}
