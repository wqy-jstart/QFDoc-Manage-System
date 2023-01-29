package cn.tedu.authuploadsystem.preload;

import cn.tedu.authuploadsystem.mapper.UserMapper;
import cn.tedu.authuploadsystem.mapper.RoleMapper;
import cn.tedu.authuploadsystem.pojo.entity.Bucket;
import cn.tedu.authuploadsystem.pojo.entity.User;
import cn.tedu.authuploadsystem.repo.IBucketRedisRepository;
import cn.tedu.authuploadsystem.repo.IUserRedisRepository;
import cn.tedu.authuploadsystem.service.IBucketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 一.该组件类用于在项目启动之前从数据库将列表信息加载到Redis中
 *
 * @author java.@Wqy
 * @version 0.0.1
 * @since 2023.1.28
 */
@Slf4j
@Component // 声明是一个组件类,在项目启动时被加载一次
public class CachePreload implements ApplicationRunner {

    // 注入bucket文件的获取接口
    @Autowired
    private IBucketService bucketService;

    // 注入用户的Mapper层接口
    @Autowired
    private UserMapper userMapper;

    // 注入角色的Mapper层接口
    @Autowired
    private RoleMapper roleMapper;

    // 注入用户的Redis缓存接口
    @Autowired
    private IUserRedisRepository userRedisRepository;

    // 注入Bucket的Redis缓存接口
    @Autowired
    private IBucketRedisRepository bucketRedisRepository;

    // 构造方法,使得启动项目时自动加载该组件类
    public CachePreload() {
        log.debug("创建开机自动执行的组件对象: CachePreload");
    }

    // ApplicationRunner中的run()方法会在项目启动成功之后自动执行
    @Override
    public void run(ApplicationArguments args){
        log.debug("CacheSchedule.run()");
        // 重建bucket空间的缓存
        log.debug("准备删除Redis缓存中的Bucket数据...");
        Long countToBucket = bucketRedisRepository.deleteAll();
        log.debug("删除Redis缓存中的Bucket数据,完成,数量为：{}",countToBucket);

        log.debug("准备从数据库中读取Bucket列表...");
        List<Bucket> listToBuckets = bucketService.bucketList("jstart");
        log.debug("从数据库中读取Bucket列表，完成！");

        log.debug("准备将Bucket列表写入到Redis缓存...");
        bucketRedisRepository.save(listToBuckets);
        log.debug("将Bucket列表写入到Redis缓存，完成！");

        // 重建用户的缓存
        log.debug("准备删除Redis缓存中的用户数据...");
        Long countToUser = userRedisRepository.deleteAll();// 清除缓存中的数据,防止缓存堆积过多,显示的列表数据冗余
        log.debug("删除Redis缓存中的用户数据,完成,数量为：{}",countToUser);

        log.debug("准备从数据库中读取用户列表...");
        List<User> list = userMapper.selectList(null);
        log.debug("从数据库中读取用户列表，完成！");

        log.debug("准备将用户列表写入到Redis缓存...");
        userRedisRepository.save(list);
        log.debug("将用户列表写入到Redis缓存，完成！");
    }
}
