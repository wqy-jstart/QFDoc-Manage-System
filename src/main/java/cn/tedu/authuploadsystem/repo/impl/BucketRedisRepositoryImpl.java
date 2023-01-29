package cn.tedu.authuploadsystem.repo.impl;

import cn.tedu.authuploadsystem.pojo.entity.Bucket;
import cn.tedu.authuploadsystem.repo.IBucketRedisRepository;
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
 * 存储空间Redis接口的实现类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.29
 */
@Slf4j
@Repository
public class BucketRedisRepositoryImpl implements IBucketRedisRepository {

    /**
     * 注入RedisTemplate，以便调用操作Redis
     */
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 实现向Redis中添加Bucket列表数据
     * @param buckets 要向Redis中存储的List集合
     */
    @Override
    public void save(List<Bucket> buckets) {
        String key = BUCKET_LIST_KEY;
        ListOperations<String, Serializable> ops = redisTemplate.opsForList();// 获取ListOperations
        for (Bucket bucket : buckets) {
            ops.rightPush(key, bucket);// 调用rightPush()方法向Redis中存入用户列表
        }
    }

    /**
     * 删除Redis中的Bucket列表
     * @return 返回删除的个数
     */
    @Override
    public Long deleteAll() {
        log.debug("开始处理删除Redis中Bucket的所有数据！");
        Set<String> keys = new HashSet<>();
        keys.add(BUCKET_LIST_KEY);
        return redisTemplate.delete(keys);
    }

    /**
     * 获取全部bucket列表
     * @return 返回全部列表
     */
    @Override
    public List<Bucket> list() {
        long start = 0;
        long end = -1;
        return list(start, end);
    }

    /**
     * 获取指定下标的bucket列表
     * @param start 起始下标
     * @param end   末尾下标
     * @return 返回指定下标的Bucket列表
     */
    @Override
    public List<Bucket> list(long start, long end) {
        String key = BUCKET_LIST_KEY;// 拿到用户列表的key值
        ListOperations<String, Serializable> ops = redisTemplate.opsForList();// 获取ListOperations
        List<Serializable> list = ops.range(key, start, end);// 调用range()方法传入下标,返回该下标范围的用户数据
        List<Bucket> buckets = new ArrayList<>();// 因为集合中的泛型不同,所以创建一个List集合
        for (Serializable item : list) {// 遍历获取的指定下标范围的用户数据
            buckets.add((Bucket) item);// 遍历的同时将每一个用户数据放到List集合中
        }
        return buckets;
    }
}
