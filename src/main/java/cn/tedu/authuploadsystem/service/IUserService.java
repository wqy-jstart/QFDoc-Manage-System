package cn.tedu.authuploadsystem.service;

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

    // 添加用户的功能
    void insert(User user);

    String login(UserLoginDTO userLoginDTO);

    // 删除用户的功能
    void deleteById(Long id);

    // 修改用户的功能
    void update(UserUpdateDTO userUpdateDTO);

    // 查询一条用户记录的功能
    User selectById(Long id);

    // 查询用户列表
    List<User> selectList();

}
