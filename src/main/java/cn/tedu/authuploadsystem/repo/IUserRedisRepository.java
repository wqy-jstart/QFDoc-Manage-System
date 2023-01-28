package cn.tedu.authuploadsystem.repo;

import cn.tedu.authuploadsystem.pojo.entity.User;
import cn.tedu.authuploadsystem.pojo.vo.UserStandardVO;

import java.util.List;

/**
 * 用来缓存Redis中用户数据的接口类
 *
 * @author java.@Wqy
 * @version 0.0.1
 * @since 2023.1.28
 */
public interface IUserRedisRepository {

    String USER_ITEM_KEY_PREFIX = "user:item";// 表示用户->item数据(多级的效果)

    String USER_LIST_KEY = "user:list";// 用来存放用户中的list列表的key

    String USER_ITEM_KEYS_KEY = "user:item-keys";// 用来标记用户的中item中的key成员

    /**
     * 该方法用来存储一条用户数据,不做返回
     *
     * @param userStandardVO 需要向Redis中存储的用户详情VO类
     */
    void save(UserStandardVO userStandardVO);

    /**
     * 该方法用来存储多条用户数据,空返回
     *
     * @param users 要向Redis中存储的用户List集合
     */
    void save(List<User> users);

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
     * @param id 用户id
     * @return 返回用户详情VO类
     */
    UserStandardVO get(Long id);

    /**
     * 该方法用来取出所有用户列表,无参
     *
     * @return 返回用户列表的List集合
     */
    List<User> list();

    /**
     * 该方法用来按指定下标范围取出用户列表
     *
     * @param start 起始下标
     * @param end   末尾下标
     * @return 返回列表List集合
     */
    List<User> list(long start, long end);
}
