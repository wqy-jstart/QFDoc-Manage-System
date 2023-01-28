package cn.tedu.authuploadsystem.service.impl;


import cn.tedu.authuploadsystem.ex.ServiceException;
import cn.tedu.authuploadsystem.mapper.RoleMapper;
import cn.tedu.authuploadsystem.mapper.UserMapper;
import cn.tedu.authuploadsystem.mapper.UserRoleMapper;
import cn.tedu.authuploadsystem.pojo.dto.UserAddNewDTO;
import cn.tedu.authuploadsystem.pojo.dto.UserLoginDTO;
import cn.tedu.authuploadsystem.pojo.dto.UserUpdateDTO;
import cn.tedu.authuploadsystem.pojo.entity.User;
import cn.tedu.authuploadsystem.pojo.entity.UserRole;
import cn.tedu.authuploadsystem.pojo.vo.UserStandardVO;
import cn.tedu.authuploadsystem.repo.IUserRedisRepository;
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

import java.time.LocalDateTime;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    // 读取配置文件application-dev.yml中的自定义配置
    @Value("${auth.jwt.secret-key}")
    private String secretKey;
    @Value("${auth.jwt.duration-in-minute}")
    private long durationInMinute;

    public UserServiceImpl() {
        log.debug("创建业务层接口实现类:UserServiceImpl");
    }

    // 注入用户的持久层接口
    @Autowired
    private UserMapper userMapper;

    // 注入角色的持久层接口
    @Autowired
    private UserRoleMapper userRoleMapper;

    // 注入认证器接口对象
    @Autowired
    private AuthenticationManager authenticationManager;

    // 注入用户缓存的接口
    @Autowired
    private IUserRedisRepository userRedisRepository;

    /**
     * 处理用户注册的功能
     *
     * @param user 要注册的用户信息
     */
    @Override
    public void insert(User user) {
        log.debug("开始处理用户注册的功能！参数：{}", user);
        User queryUser = userMapper.selectById(user.getId());
        if (queryUser != null) {
            String message = "用户注册失败，该用户已存在!";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }
        log.debug("开始向用户表中插入信息！");
        user.setPassword(BCryptEncode.encryptionPassword(user.getPassword()));
        int rows = userMapper.insert(user);
        if (rows > 1) {
            String message = "用户注册失败，服务器忙，请稍后再试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_INSERT, message);
        }
    }

    /**
     * 后台添加用户的功能
     *
     * @param userAddNewDTO 添加的用户数据
     */
    @Override
    public void insert(UserAddNewDTO userAddNewDTO) {
        log.debug("开始处理后台添加用户的功能，参数：{}", userAddNewDTO);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", userAddNewDTO.getUsername());
        User user = userMapper.selectOne(wrapper);
        if (user != null) {
            String message = "添加失败，该用户名已存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }
        User u = new User();
        BeanUtils.copyProperties(userAddNewDTO, u);
        u.setPassword(BCryptEncode.encryptionPassword(userAddNewDTO.getPassword()));
        int rows = userMapper.insert(u);
        if (rows > 1) {
            String message = "添加用户失败，服务器忙，请稍后再试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_INSERT, message);
        }

        Long[] roleIds = userAddNewDTO.getRoleIds();
        UserRole[] userRoles = new UserRole[roleIds.length];
        for (int i = 0; i < roleIds.length; i++) {
            UserRole userRole = new UserRole();
            userRole.setAdminId(u.getId());
            userRole.setRoleId(roleIds[i]);
            userRoles[i] = userRole;
        }
        int row = userRoleMapper.insertBatch(userRoles);
        if (row > roleIds.length) {
            String message = "添加关联表失败，服务器忙，请稍后再试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_INSERT, message);
        }
    }

    /**
     * 处理用户登录的认证功能
     *
     * @param userLoginDTO 用户的登录信息
     * @return 返回生成的JWT
     */
    @Override
    public String login(UserLoginDTO userLoginDTO) {
        log.debug("开始处理用户登录的功能！，参数：{}", userLoginDTO);
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
     *
     * @param id 用户id
     */
    @Override
    public void deleteById(Long id) {
        log.debug("开始处理删除id为{}的用户信息", id);
        User queryUser = userMapper.selectById(id);
        if (queryUser == null) {
            String message = "删除失败，该用户不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }

        log.debug("开始执行删除用户的功能！");
        int rows = userMapper.deleteById(id);
        if (rows > 1) {
            String message = "删除失败，该用户id不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_DELETE, message);
        }
    }

    /**
     * 处理修改用户信息的功能
     *
     * @param userUpdateDTO 要修改的用户信息
     */
    @Override
    public void update(UserUpdateDTO userUpdateDTO) {
        log.debug("开始处理修改id为{}的用户信息", userUpdateDTO.getId());
        User queryUser = userMapper.selectById(userUpdateDTO.getId());
        if (queryUser == null) {
            String message = "用户修改失败，该用户不存在!";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }
        if (!userUpdateDTO.getUsername().equals(queryUser.getUsername())) {
            // 检查用户名是否存在
            QueryWrapper<User> wrapperToUpdate = new QueryWrapper<>();
            wrapperToUpdate.eq("username", userUpdateDTO.getUsername());
            User queryUserToUpdate = userMapper.selectOne(wrapperToUpdate);
            if (queryUserToUpdate != null) {
                String message = "用户修改失败，该用户名已经存在！";
                log.debug(message);
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
            }
        }
        log.debug("即将修改用户信息...");
        User user = new User();
        BeanUtils.copyProperties(userUpdateDTO, user);
        user.setPassword(BCryptEncode.encryptionPassword(userUpdateDTO.getPassword()));
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", user.getId());
        int rows = userMapper.update(user, wrapper);
        if (rows > 1) {
            String message = "修改失败，服务器忙，请稍后再试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_UPDATE, message);
        }
    }

    /**
     * 开始处理根据用户id查询一条记录的功能
     *
     * @param id 用户id
     * @return 返回用户的一条记录
     */
    @Override
    public User selectById(Long id) {
        log.debug("开始处理根据id{}查询用户的一条数据", id);
        log.debug("将从Redis中查询数据！");
        User user = userRedisRepository.get(id);
        if (user != null) {
            log.debug("命中缓存,即将返回:{}", user);
            return user;
        }
        log.debug("未命中缓存,即将向数据库中查询数据");
        user = userMapper.selectById(id);
        if (user == null) {
            String message = "查询失败，该用户信息不存在";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }
        // 将查询结果写入到缓存,并返回
        log.debug("从数据库中查询到有效结果,将查询结果存入到Redis中:{}", user);
        userRedisRepository.save(user);
        log.debug("开始返回结果!");
        return user;
    }

    /**
     * 开始处理查询用户列表的功能
     *
     * @return 返回用户列表
     */
    @Override
    public List<User> selectList() {
        log.debug("开始处理查询用户列表的功能，无参！");
        return userRedisRepository.list();
    }

    /**
     * 处理启用管理员的业务
     * 通过调用启用或禁用的方法,传入id和值,
     * (1).判断管理员id是否为1
     * (2).该id下是否有数据
     * (3).enable的值是否不同最终进行设置
     * (4).最终改变的数据是否正确
     *
     * @param id 启用的管理员id
     */
    @Override
    public void setEnable(Long id) {
        updateEnableById(id, 1);// 调用该方法传入id,设置为启用状态
    }

    /**
     * 处理禁用管理员的业务
     *
     * @param id 禁用的管理员id
     */
    @Override
    public void setDisable(Long id) {
        updateEnableById(id, 0);// 调用该方法传入id,设置为禁用状态
    }

    /**
     * 该方法用来处理启用与禁用的逻辑
     *
     * @param id     id
     * @param enable 是否启用或禁用
     */
    private void updateEnableById(Long id, Integer enable) {
        String[] tips = {"禁用", "启用"};
        log.debug("开始处理【{}管理员】的业务，id参数：{}", tips[enable], id);
        // 判断id是否为1(系统管理员)
        if (id == 1) {
            String message = tips[enable] + "管理员失败，尝试访问的数据不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }
        // 根据id查询管理员详情
        User queryUser = userMapper.selectById(id);
        if (queryUser == null) {
            String message = tips[enable] + "管理员失败,尝试访问的数据不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }
        // 判断查询结果中的enable与方法参数enable是否相同
        if (enable.equals(queryUser.getEnable())) {
            String message = tips[enable] + "管理员失败，管理员账号已经处于" + tips[enable] + "状态！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }
        // 创建admin对象,并封装id和enable这2个属性的值,并进行修改
        User user = new User();
        user.setId(id);
        user.setEnable(enable);
        int rows = userMapper.updateById(user);
        if (rows != 1) {
            String message = tips[enable] + "管理员失败，服务器忙，请稍后再次尝试！";
            throw new ServiceException(ServiceCode.ERR_UPDATE, message);
        }
        log.debug("修改成功!");
    }
}
