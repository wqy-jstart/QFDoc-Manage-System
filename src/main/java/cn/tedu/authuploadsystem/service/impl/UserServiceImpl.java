package cn.tedu.authuploadsystem.service.impl;


import cn.tedu.authuploadsystem.ex.ServiceException;
import cn.tedu.authuploadsystem.mapper.UserMapper;
import cn.tedu.authuploadsystem.pojo.dto.UserLoginDTO;
import cn.tedu.authuploadsystem.pojo.dto.UserUpdateDTO;
import cn.tedu.authuploadsystem.pojo.entity.User;
import cn.tedu.authuploadsystem.security.AdminDetails;
import cn.tedu.authuploadsystem.service.IUserService;
import cn.tedu.authuploadsystem.util.BCryptEncode;
import cn.tedu.authuploadsystem.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 用户的业务层实现类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.16
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {

    // 读取配置文件application-dev.yml中的自定义配置
    @Value("${auth.jwt.secret-key}")
    private String secretKey;
    @Value("${auth.jwt.duration-in-minute}")
    private long durationInMinute;

    public UserServiceImpl(){
        log.debug("创建业务层接口实现类:UserServiceImpl");
    }

    // 注入用户的持久层接口
    @Autowired
    private UserMapper userMapper;

    // 注入认证器接口对象
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 处理用户注册的功能
     * @param user 要注册的用户信息
     */
    @Override
    public void insert(User user) {
        log.debug("开始处理用户注册的功能！参数：{}",user);
        User queryUser = userMapper.selectById(user.getId());
        if (queryUser != null){
            String message = "用户注册失败，该用户已存在!";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT,message);
        }
        log.debug("开始向用户表中插入信息！");
        user.setPassword(BCryptEncode.encryptionPassword(user.getPassword()));
        int rows = userMapper.insert(user);
        if (rows>1){
            String message = "用户注册失败，服务器忙，请稍后再试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_INSERT,message);
        }
    }

    /**
     * 处理用户登录的认证功能
     * @param userLoginDTO 用户的登录信息
     * @return 返回生成的JWT
     */
    @Override
    public String login(UserLoginDTO userLoginDTO) {
        log.debug("开始处理用户登录的功能！，参数：{}",userLoginDTO);
        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken(
                        userLoginDTO.getUsername(), userLoginDTO.getPassword()
        );
        // 开始认证
        Authentication authenticateResult
                = authenticationManager.authenticate(authentication);
        log.debug("认证通过,认证管理器返回:{}", authenticateResult);

        // 2.认证成功后获取当事人对象
        Object principal = authenticateResult.getPrincipal();
        log.debug("认证结果中的当事人类型:{}", principal.getClass().getName());
        AdminDetails adminDetails = (AdminDetails) principal;

        // 3.获取认证结果
        String username = adminDetails.getUsername();
        Long id = adminDetails.getId();
        Collection<GrantedAuthority> authorities = adminDetails.getAuthorities();
        String authoritiesJsonString = JSON.toJSONString(authorities);

        // 生成JWT数据前将数据填充到Map中
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("id", id);
        claims.put("authoritiesJsonString", authoritiesJsonString);
        log.debug("向JWT中存入用户名:{}", username);
        log.debug("向JWT中存入id:{}", id);
        log.debug("向JWT中存入authoritiesJsonString:{}", authoritiesJsonString);

        // 4.生成JWT数据
        Date date = new Date(System.currentTimeMillis() + durationInMinute * 60 * 1000L);
        String jwt = Jwts.builder() //构建者模式
                // Header
                .setHeaderParam("alg", "HS256") // 指定算法
                .setHeaderParam("trp", "JWT") // 指定类型
                // Payload 载荷
                .setClaims(claims) // 传入Map
                // Signature 签名
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        log.debug("生成的JWT：{}", jwt);
        return jwt;// 最终返回认证后的JWT
    }

    /**
     * 根据id删除用户的功能
     * @param id 用户id
     */
    @Override
    public void deleteById(Long id) {
        log.debug("开始处理删除id为{}的用户信息",id);
        User queryUser = userMapper.selectById(id);
        if (queryUser == null){
            String message = "删除失败，该用户不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,message);
        }

        log.debug("开始执行删除用户的功能！");
        int rows = userMapper.deleteById(id);
        if (rows > 1){
            String message = "删除失败，该用户id不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_DELETE,message);
        }
    }

    /**
     * 处理修改用户信息的功能
     * @param user 要修改的用户信息
     */
    @Override
    public void update(UserUpdateDTO userUpdateDTO) {
        log.debug("开始处理修改id为{}的用户信息",userUpdateDTO.getId());
        User queryUser = userMapper.selectById(userUpdateDTO.getId());
        if (queryUser == null){
            String message = "用户修改失败，该用户不存在!";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT,message);
        }

        log.debug("即将修改用户信息...");
        User user = new User();
        BeanUtils.copyProperties(userUpdateDTO,user);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id",user.getId());
        int rows = userMapper.update(user, wrapper);
        if (rows>1){
            String message = "修改失败，服务器忙，请稍后再试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_UPDATE,message);
        }
    }

    /**
     * 开始处理根据用户id查询一条记录的功能
     * @param id 用户id
     * @return 返回用户的一条记录
     */
    @Override
    public User selectById(Long id) {
        log.debug("开始处理根据id{}查询用户的一条数据",id);
        User queryUser = userMapper.selectById(id);
        if (queryUser == null){
            String message = "查询失败，该用户信息不存在";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,message);
        }
        return queryUser;
    }

    /**
     * 开始处理查询用户列表的功能
     * @return 返回用户列表
     */
    @Override
    public List<User> selectList() {
        log.debug("开始处理查询用户列表的功能，无参！");
        return userMapper.selectList(null);
    }
}
