package cn.tedu.authuploadsystem.mapper;

import cn.tedu.authuploadsystem.pojo.entity.User;
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
}
