package cn.tedu.authuploadsystem.repo.impl;

import cn.tedu.authuploadsystem.pojo.entity.User;
import cn.tedu.authuploadsystem.repo.IUserRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 处理Redis中用户缓存的实现类
 *
 * @author java.@Wqy
 * @version 0.0.1
 * @since 2023.1.28
 */
@Slf4j
@Repository// 声明一个组件
public class UserRedisRepositoryImpl implements IUserRedisRepository {

    /**
     * 注入redisTemplate
     */
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    public UserRedisRepositoryImpl() {
        log.debug("创建处理缓存的数据访问实现类对象:UserRedisRepositoryImpl");
    }

    // 实现向Redis中写入数据的业务
    @Override
    public void save(User userStandardVO) {
        String key = USER_ITEM_KEY_PREFIX + userStandardVO.getId();// 这样存储便于在Redis中归类呈现
        // ★向Redis的用户中的brand:item-keys里,添加该次添加的用户key值,为了删除时直接遍历里面item的key值作删除
        log.debug("向Set集合中存入此次查找的Key值");
        redisTemplate.opsForSet().add(USER_ITEM_KEYS_KEY, key);// 将key单独写到一个key中
        redisTemplate.opsForValue().set(key, userStandardVO);// 将对应的用户数据放到指定key中
        log.debug("向缓存中存入用户详情成功!");
    }

    // 实现向Redis中写入多条用户数据的业务
    @Override
    public void save(List<User> users) {
        String key = USER_LIST_KEY;// 用来存放用户列表的key
        ListOperations<String, Serializable> ops = redisTemplate.opsForList();// 获取ListOperations
        for (User user : users) {
            ops.rightPush(key, user);// 调用rightPush()方法向Redis中存入用户列表
        }
    }

    // 实现删除Brand中所有数据的业务(集合,item,member)
    @Override
    public Long deleteAll() {
        // 获取到brand:item-keys中所有的item的key
        Set<Serializable> members = redisTemplate.opsForSet().members(USER_ITEM_KEYS_KEY);
        Set<String> keys = new HashSet<>();// 创建一个Set集合
        for (Serializable member : members) {
            keys.add((String) member);// 将获取的所有item的key放到Set集合中,例:brand:item1
        }
        // 将List集合和保存Key的Set的Key也添加到集合中
        keys.add(USER_LIST_KEY);// brand:list
        keys.add(USER_ITEM_KEYS_KEY);// brand:item-keys
        return redisTemplate.delete(keys);// 调用delete()方法来删除集合中的元素
    }

    // 实现根据key向Redis中获取一条用户数据的业务
    @Override
    public User get(Long id) {
        Serializable serializable = redisTemplate.opsForValue().get(USER_ITEM_KEY_PREFIX + id);// 传入id与brand:item拼接成key
        User userStandardVO = null;// 预先声明一个用户详情的引用
        if (serializable != null) { // 判断根据用户key返回的数据是否为null?
            if (serializable instanceof User) { // 判断类型是否存在可转换的关系
                userStandardVO = (User) serializable;// 将返回的Serializable强转为BrandStandardVO用户详情
            }
        }
        return userStandardVO;// 最终作出返回
    }

    // 实现向Redis中查询所有用户列表的业务
    @Override
    public List<User> list() {
        long start = 0;
        long end = -1;
        return list(start, end);// 调用list()方法传入起始和末尾下标,返回所有用户列表
    }

    // 实现根据下标向Redis中获取用户数据的方法
    @Override
    public List<User> list(long start, long end) {
        String key = USER_LIST_KEY;// 拿到用户列表的key值
        ListOperations<String, Serializable> ops = redisTemplate.opsForList();// 获取ListOperations
        List<Serializable> list = ops.range(key, start, end);// 调用range()方法传入下标,返回该下标范围的用户数据
        List<User> users = new ArrayList<>();// 因为集合中的泛型不同,所以创建一个List集合
        for (Serializable item : list) {// 遍历获取的指定下标范围的用户数据
            users.add((User) item);// 遍历的同时将每一个用户数据放到List集合中
        }
        return users;// 装满后作出返回
    }
}
