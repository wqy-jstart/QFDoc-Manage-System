package cn.tedu.authuploadsystem.controller;

import cn.tedu.authuploadsystem.pojo.dto.UserAddNewDTO;
import cn.tedu.authuploadsystem.pojo.dto.UserLoginDTO;
import cn.tedu.authuploadsystem.pojo.dto.UserUpdateDTO;
import cn.tedu.authuploadsystem.pojo.entity.User;
import cn.tedu.authuploadsystem.service.IUserService;
import cn.tedu.authuploadsystem.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户的控制器类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.16
 */
@Api(tags = "用户管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    // 注入用户的业务层接口类
    @Autowired
    private IUserService userService;

    public UserController() {
        log.debug("创建控制器对象：UserController");
    }

    /**
     * 用户注册的请求
     *
     * @param user 用户的注册信息
     * @return 返回结果集
     */
    @ApiOperation("用户注册")
    @ApiOperationSupport(order = 100)
    @PostMapping("/insert")
    public JsonResult<Void> insert(@Valid User user) {
        log.debug("开始处理用户注册的功能！参数：{}", user);
        userService.insert(user);
        return JsonResult.ok();
    }

    /**
     * 后台添加用户
     *
     * @param userAddNewDTO 添加的用户信息
     * @return 返回结果集
     */
    @ApiOperation("后台添加用户")
    @ApiOperationSupport(order = 101)
    @PreAuthorize("hasAuthority('/user/insert')")
    @PostMapping("/insertToAdmin")
    public JsonResult<Void> insert(UserAddNewDTO userAddNewDTO) {
        log.debug("开始处理添加用户的功能，参数：{}", userAddNewDTO);
        userService.insert(userAddNewDTO);
        return JsonResult.ok();
    }

    /**
     * 处理用户登录的请求
     *
     * @param userLoginDTO 用户登录的信息
     * @return 返回JWT
     */
    @ApiOperation("用户登录")
    @ApiOperationSupport(order = 200)
    @PostMapping("/login")
    public JsonResult<String> login(@Valid UserLoginDTO userLoginDTO) {
        log.debug("开始处理用户登录的功能，用户登录的信息：{}", userLoginDTO);
        String login = userService.login(userLoginDTO);
        return JsonResult.ok(login);
    }

    /**
     * 根据id删除用户信息
     *
     * @param id 用户id
     * @return 返回结果集
     */
    @ApiOperation("根据id删除用户信息")
    @ApiOperationSupport(order = 300)
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "long")
    @PreAuthorize("hasAuthority('/user/delete')")
    @PostMapping("/{id:[0-9]+}/deleteById")
    public JsonResult<Void> deleteById(@Range(min = 1, message = "删除失败,参数无效!")
                                       @PathVariable Long id) {
        log.debug("开始处理根据id{}删除用户信息的功能", id);
        userService.deleteById(id);
        return JsonResult.ok();
    }

    /**
     * 开始处理根据用户id修改用户的请求
     *
     * @param userUpdateDTO 用户信息
     * @return 返回结果集
     */
    @ApiOperation("根据id修改用户")
    @ApiOperationSupport(order = 400)
    @PreAuthorize("hasAuthority('/user/update')")
    @PostMapping("/update")
    public JsonResult<Void> update(@Valid UserUpdateDTO userUpdateDTO) {
        log.debug("开始处理根据id{}修改用户信息的请求", userUpdateDTO.getId());
        userService.update(userUpdateDTO);
        return JsonResult.ok();
    }

    /**
     * 根据id查询用户信息的请求
     *
     * @param id 用户id
     * @return 返回用户信息
     */
    @ApiOperation("根据id查询用户")
    @ApiOperationSupport(order = 500)
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "long")
    @GetMapping("/{id:[0-9]+}/selectById")
    public JsonResult<User> selectById(@Range(min = 1, message = "查询失败,参数无效!")
                                       @PathVariable Long id) {
        log.debug("开始处理查询id为{}的用户业务", id);
        User user = userService.selectById(id);
        return JsonResult.ok(user);
    }

    /**
     * 开始处理查询用户列表的请求
     *
     * @return 返回查询的用户列表信息
     */
    @ApiOperation("查询用户列表")
    @ApiOperationSupport(order = 501)
    @PreAuthorize("hasAuthority('/user/read')")
    @GetMapping("/selectList")
    public JsonResult<List<User>> selectList() {
        log.debug("开始处理查询用户列表的功能,无参!");
        List<User> users = userService.selectList();
        return JsonResult.ok(users);
    }

    /**
     * 处理启用管理员的业务
     *
     * @param id 要启用的管理员id
     * @return 返回JsonResult
     */
    @ApiOperation("启用管理员")
    @ApiOperationSupport(order = 310)
    @ApiImplicitParam(name = "id", value = "启用的管理员id", required = true, dataType = "long")
    @PostMapping("/{id:[0-9]+}/enable")
    public JsonResult<Void> setEnable(@Range(min = 1, message = "启用管理员失败,尝试启用的id无效!")
                                      @PathVariable Long id) {
        log.debug("开始将id为{}的管理员设置为启用状态", id);
        userService.setEnable(id);
        return JsonResult.ok();
    }

    /**
     * 处理禁用管理员的业务
     *
     * @param id 要禁用的管理员id
     * @return 返回JsonResult
     */
    @ApiOperation("禁用管理员")
    @ApiOperationSupport(order = 311)
    @ApiImplicitParam(name = "id", value = "禁用的管理员id", required = true, dataType = "long")
    @PostMapping("/{id:[0-9]+}/disable")
    public JsonResult<Void> setDisable(@Range(min = 1, message = "禁用管理员失败,尝试启用的id无效!")
                                       @PathVariable Long id) {
        log.debug("开始将id为{}的管理员设置为禁用状态", id);
        userService.setDisable(id);
        return JsonResult.ok();
    }
}
