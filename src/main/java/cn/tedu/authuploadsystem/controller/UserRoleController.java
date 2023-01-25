package cn.tedu.authuploadsystem.controller;

import cn.tedu.authuploadsystem.pojo.dto.AssignToRole;
import cn.tedu.authuploadsystem.pojo.dto.UserAddNewDTO;
import cn.tedu.authuploadsystem.service.IUserRoleService;
import cn.tedu.authuploadsystem.service.impl.UserRoleServiceImpl;
import cn.tedu.authuploadsystem.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户角色表的控制器类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.25
 */
@Api(tags = "用户角色管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/userRoles")
public class UserRoleController {

    // 注入用户角色业务层接口
    @Autowired
    private IUserRoleService userRoleService;

    public UserRoleController(){
        log.debug("创建控制器类：UserRoleController");
    }

    /**
     * 分配用户角色信息
     * @param assignToRole 传递分配的信息
     * @return 返回结果集
     */
    @ApiOperation("分配用户角色信息")
    @ApiOperationSupport(order = 100)
    @PostMapping("/insertBatch")
    public JsonResult<Void> insertBatch(AssignToRole assignToRole){
        log.debug("开始处理分配角色批量查询用户角色表信息,参数：{}",assignToRole);
        userRoleService.insertBatch(assignToRole);
        return JsonResult.ok();
    }
}
