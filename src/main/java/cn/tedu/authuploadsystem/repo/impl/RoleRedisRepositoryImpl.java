package cn.tedu.authuploadsystem.repo.impl;

import cn.tedu.authuploadsystem.pojo.entity.Role;
import cn.tedu.authuploadsystem.pojo.vo.RoleStandardVO;
import cn.tedu.authuploadsystem.repo.IRoleRedisRepository;
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
 * 角色的Redis缓存接口实现类
 * 
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.29
 */
@Slf4j
@Repository
public class RoleRedisRepositoryImpl implements IRoleRedisRepository {

    /**
     * 注入redisTemplate
     */
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    public RoleRedisRepositoryImpl() {
        log.debug("创建处理缓存的数据访问实现类对象:RoleRedisRepositoryImpl");
    }

    /**
     * 向Redis中添加Role的一条记录
     * @param roleStandardVO 需要向Redis中存储的角色详情VO类
     */
    @Override
    public void save(Role roleStandardVO) {
        String key = ROLE_ITEM_KEY_PREFIX + roleStandardVO.getId();// 这样存储便于在Redis中归类呈现
        // ★向Redis的角色中的brand:item-keys里,添加该次添加的角色key值,为了删除时直接遍历里面item的key值作删除
        log.debug("向Set集合中存入此次查找的Key值");
        redisTemplate.opsForSet().add(ROLE_ITEM_KEYS_KEY, key);// 将key单独写到一个key中
        redisTemplate.opsForValue().set(key, roleStandardVO);// 将对应的角色数据放到指定key中
        log.debug("向缓存中存入角色详情成功!");
    }

    /**
     * 向Redis中添加Role的列表
     * @param roles 要向Redis中存储的角色List集合
     */
    @Override
    public void save(List<Role> roles) {
        String key = ROLE_LIST_KEY;// 用来存放角色列表的key
        ListOperations<String, Serializable> ops = redisTemplate.opsForList();// 获取ListOperations
        for (Role role : roles) {
            ops.rightPush(key, role);// 调用rightPush()方法向Redis中存入角色列表
        }
    }

    /**
     * 删除Redis中存储的所有Role数据
     * @return 返回删除的个数
     */
    @Override
    public Long deleteAll() {
        // 获取到brand:item-keys中所有的item的key
        Set<Serializable> members = redisTemplate.opsForSet().members(ROLE_ITEM_KEYS_KEY);
        Set<String> keys = new HashSet<>();// 创建一个Set集合
        for (Serializable member : members) {
            keys.add((String) member);// 将获取的所有item的key放到Set集合中,例:brand:item1
        }
        // 将List集合和保存Key的Set的Key也添加到集合中
        keys.add(ROLE_LIST_KEY);// brand:list
        keys.add(ROLE_ITEM_KEYS_KEY);// brand:item-keys
        return redisTemplate.delete(keys);// 调用delete()方法来删除集合中的元素
    }

    /**
     * 根据id获取一条Role的数据
     * @param id 角色id
     * @return 返回Role数据
     */
    @Override
    public Role get(Long id) {
        Serializable serializable = redisTemplate.opsForValue().get(ROLE_ITEM_KEY_PREFIX + id);// 传入id与brand:item拼接成key
        Role role = null;// 预先声明一个角色详情的引用
        if (serializable != null) { // 判断根据角色key返回的数据是否为null?
            if (serializable instanceof Role) { // 判断类型是否存在可转换的关系
                role = (Role) serializable;// 将返回的Serializable强转为BrandStandardVO角色详情
            }
        }
        return role;// 最终作出返回
    }

    /**
     * 获取全部的RoleList列表
     * @return 返回List列表
     */
    @Override
    public List<Role> list() {
        long start = 0;
        long end = -1;
        return list(start, end);// 调用list()方法传入起始和末尾下标,返回所有角色列表
    }

    /**
     * 根据下标查询Redis中Role的列表
     * @param start 起始下标
     * @param end   末尾下标
     * @return 返回List
     */
    @Override
    public List<Role> list(long start, long end) {
        String key = ROLE_LIST_KEY;// 拿到角色列表的key值
        ListOperations<String, Serializable> ops = redisTemplate.opsForList();// 获取ListOperations
        List<Serializable> list = ops.range(key, start, end);// 调用range()方法传入下标,返回该下标范围的角色数据
        List<Role> roles = new ArrayList<>();// 因为集合中的泛型不同,所以创建一个List集合
        for (Serializable item : list) {// 遍历获取的指定下标范围的角色数据
            roles.add((Role) item);// 遍历的同时将每一个角色数据放到List集合中
        }
        return roles;// 装满后作出返回
    }
}
