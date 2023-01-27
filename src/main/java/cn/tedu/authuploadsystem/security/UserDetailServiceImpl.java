package cn.tedu.authuploadsystem.security;

import cn.tedu.authuploadsystem.mapper.UserMapper;
import cn.tedu.authuploadsystem.pojo.entity.User;
import cn.tedu.authuploadsystem.pojo.vo.UserLoginInfoVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于Spring Security的登录认证创建的用户登录的(UserDetailsService接口)实现类
 * 最终返回UserDetails,以便进行认证
 *
 * @author java.@Wqy
 * @version 0.0.1
 * @since 2023.1.16
 */
@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    // 注入UserMapper
    @Autowired
    private UserMapper userMapper;

    // 重写loadUserByUsername(String s)"根据用户名加载用户的方法",Spring Security会自动传入用户名进行处理
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.debug("Spring Security框架自动调用UserDetailServiceImpl中的loadUserByUsername方法,参数{}", s);

        // 调用持久层的方法获取登录认证所需要的信息
        UserLoginInfoVO admin = userMapper.getLoginInfoByUsername(s);
        log.debug("从数据库中根据用户名[{}]查询管理员信息,结果:{}", s, admin);

        if (admin == null) {// 判断查询的admin是否为Null?
            log.debug("没有与用户名[{}]匹配的用户信息,即将抛出BadCredentialsException", s);
            String message = "登录失败,用户名不存在!";
            throw new BadCredentialsException(message);// Spring Security提供的异常
        }

        // 添加权限信息
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String permission : admin.getPermissions()) {
            GrantedAuthority authority = new SimpleGrantedAuthority(permission);
            authorities.add(authority);
        }

        // 使用新建的继承Spring Security的类AdminDetails来返回信息便于认证
        AdminDetails adminDetails = new AdminDetails(
                admin.getId(),
                admin.getUsername(),
                admin.getPassword(),
                admin.getEnable()==1,
                authorities
        );

        log.debug("即将向Spring Security框架返回UserDetail对象:{}", adminDetails);
        return adminDetails; // 最终返回登录认证所需要的信息
    }
}
