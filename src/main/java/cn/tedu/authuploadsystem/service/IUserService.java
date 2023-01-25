package cn.tedu.authuploadsystem.service;

import cn.tedu.authuploadsystem.pojo.dto.UserAddNewDTO;
import cn.tedu.authuploadsystem.pojo.dto.UserLoginDTO;
import cn.tedu.authuploadsystem.pojo.dto.UserUpdateDTO;
import cn.tedu.authuploadsystem.pojo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户的业务层接口类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.16
 */
@Transactional
public interface IUserService extends IService<User> {

    // 用户登录的功能
    void insert(User user);

    // 添加用户的功能
    void insert(UserAddNewDTO userAddNewDTO);

    // 用户登录的功能
    String login(UserLoginDTO userLoginDTO);

    // 删除用户的功能
    void deleteById(Long id);

    // 修改用户的功能
    void update(UserUpdateDTO userUpdateDTO);

    // 查询一条用户记录的功能
    User selectById(Long id);

    // 查询用户列表
    List<User> selectList();

    /**
     * 启用用户
     * @param id 启用的用户id
     */
    void setEnable(Long id);

    /**
     * 禁用用户
     * @param id 禁用的用户id
     */
    void setDisable(Long id);

}
