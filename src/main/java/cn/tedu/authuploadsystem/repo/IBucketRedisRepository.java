package cn.tedu.authuploadsystem.repo;

import cn.tedu.authuploadsystem.pojo.entity.Bucket;

import java.util.List;

/**
 * 用来缓存存储空间文件列表的数据
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.29
 */
public interface IBucketRedisRepository {
    String BUCKET_LIST_KEY = "bucket:list";// 用来存放存储空间中的list列表的key

    /**
     * 该方法用来存储多条bucket数据,空返回
     *
     * @param buckets 要向Redis中存储的List集合
     */
    void save(List<Bucket> buckets);

    /**
     * 删除Redis中的列表数据
     *
     * @return 返回删除的数量
     */
    Long deleteAll();

    /**
     * 该方法用来取出所有存储空间列表,无参
     *
     * @return 返回存储空间列表的List集合
     */
    List<Bucket> list();

    /**
     * 该方法用来按指定下标范围取出存储空间列表
     *
     * @param start 起始下标
     * @param end   末尾下标
     * @return 返回列表List集合
     */
    List<Bucket> list(long start, long end);
}
