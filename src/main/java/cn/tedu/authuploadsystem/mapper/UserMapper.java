package cn.tedu.authuploadsystem.mapper;

import cn.tedu.authuploadsystem.pojo.entity.User;
import cn.tedu.authuploadsystem.pojo.vo.UserLoginInfoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 用户的数据访问层接口对象
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.16
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询登录的信息
     * @param username 用户名
     * @return 返回登录的VO类信息(id,username,password,enable,permissions)
     */
    UserLoginInfoVO getLoginInfoByUsername(String username);
}
