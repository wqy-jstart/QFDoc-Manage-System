package cn.tedu.authuploadsystem.service.impl;


import cn.tedu.authuploadsystem.mapper.UserMapper;
import cn.tedu.authuploadsystem.pojo.entity.User;
import cn.tedu.authuploadsystem.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;


public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {
    @Override
    public void insert(User user) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public User selectById(Long id) {
        return null;
    }

    @Override
    public List<User> selectList() {
        return null;
    }
}
