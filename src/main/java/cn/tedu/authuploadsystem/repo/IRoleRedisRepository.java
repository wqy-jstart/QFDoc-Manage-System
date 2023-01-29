package cn.tedu.authuploadsystem.repo;

import cn.tedu.authuploadsystem.pojo.entity.Role;
import cn.tedu.authuploadsystem.pojo.vo.RoleStandardVO;

import java.util.List;

/**
 * 角色的Redis缓存接口
 * 
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.29
 */
public interface IRoleRedisRepository {


    String ROLE_ITEM_KEY_PREFIX = "role:item";// 表示角色->item数据(多级的效果)

    String ROLE_LIST_KEY = "role:list";// 用来存放角色中的list列表的key

    String ROLE_ITEM_KEYS_KEY = "role:item-keys";// 用来标记角色的中item中的key成员

    /**
     * 该方法用来存储一条角色数据,不做返回
     *
     * @param roleStandardVO 需要向Redis中存储的角色详情VO类
     */
    void save(Role roleStandardVO);

    /**
     * 该方法用来存储多条角色数据,空返回
     *
     * @param roles 要向Redis中存储的角色List集合
     */
    void save(List<Role> roles);

    /**
     * 删除Redis中的所有数据(item数据,list集合数据,brand:list,brand:item-keys)
     *
     * @return 返回删除的数量
     */
    Long deleteAll();

    /**
     * 向Redis中取出需要的item数据
     * 正常若向Redis中取数据需要对应的key值,
     * 这里的key有前缀拼接,为了保证封装性,这里只让调用者传入id即可
     *
     * @param id 角色id
     * @return 返回角色详情VO类
     */
    Role get(Long id);

    /**
     * 该方法用来取出所有角色列表,无参
     *
     * @return 返回角色列表的List集合
     */
    List<Role> list();

    /**
     * 该方法用来按指定下标范围取出角色列表
     *
     * @param start 起始下标
     * @param end   末尾下标
     * @return 返回列表List集合
     */
    List<Role> list(long start, long end);
}
