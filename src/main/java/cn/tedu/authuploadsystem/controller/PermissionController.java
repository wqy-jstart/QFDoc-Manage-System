package cn.tedu.authuploadsystem.controller;

import cn.tedu.authuploadsystem.pojo.dto.AssignToPermission;
import cn.tedu.authuploadsystem.pojo.dto.AssignToRole;
import cn.tedu.authuploadsystem.pojo.entity.Permission;
import cn.tedu.authuploadsystem.service.IPermissionService;
import cn.tedu.authuploadsystem.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限的控制器类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.26
 */
@Api(tags = "权限管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/permissions")
public class PermissionController {

    // 注入权限的业务层接口
    @Autowired
    private IPermissionService permissionService;

    /**
     * 查询权限列表
     *
     * @return 返回列表
     */
    @ApiOperation("查询权限列表")
    @ApiOperationSupport(order = 500)
    @PreAuthorize("hasAuthority('/auth/assign')")
    @GetMapping("")
    public JsonResult<List<Permission>> selectList() {
        log.debug("开始处理查询权限列表的功能，无参！");
        List<Permission> permissions = permissionService.selectList();
        return JsonResult.ok(permissions);
    }

    /**
     * 查询所有权限id
     *
     * @return 返回列表
     */
    @ApiOperation("查询所有权限id")
    @ApiOperationSupport(order = 501)
    @GetMapping("/selectPermissionIds")
    public JsonResult<List<Long>> selectIds() {
        log.debug("开始处理查询所有权限id的功能，无参！");
        List<Long> longs = permissionService.selectToId();
        return JsonResult.ok(longs);
    }

    /**
     * 根据角色id查询权限列表
     *
     * @param roleId 角色id
     * @return 返回权限列表
     */
    @ApiOperation("根据角色id查询权限列表")
    @ApiOperationSupport(order = 502)
    @ApiImplicitParam(name = "roleId", value = "角色Id", required = true, dataType = "long")
    @PostMapping("/{roleId:[0-9]+}/selectToRoleId")
    public JsonResult<List<Long>> selectToRoleId(@PathVariable Long roleId) {
        log.debug("开始处理查询角色id：{}的权限列表", roleId);
        List<Long> longs = permissionService.selectToRoleId(roleId);
        return JsonResult.ok(longs);
    }

    /**
     * 分配用户权限信息
     *
     * @param assignToPermission 传递分配的信息
     * @return 返回结果集
     */
    @ApiOperation("分配用户权限信息")
    @ApiOperationSupport(order = 100)
    @PostMapping("/assignToPermission")
    public JsonResult<Void> insertBatch(AssignToPermission assignToPermission) {
        log.debug("开始处理分配角色批量插入角色权限表信息,参数：{}", assignToPermission);
        permissionService.insertBatch(assignToPermission);
        return JsonResult.ok();
    }
}
