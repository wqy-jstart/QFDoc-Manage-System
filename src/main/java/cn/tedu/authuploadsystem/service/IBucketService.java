package cn.tedu.authuploadsystem.service;

import cn.tedu.authuploadsystem.pojo.entity.Bucket;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 存储空间的业务层接口类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.19
 */
@Transactional
public interface IBucketService {

    /**
     * 创建存储空间
     * @param bucketName 空间名
     * @return 返回创建的状态码
     */
    String createBucket(String bucketName);

    /**
     * 删除存储空间
     * @param bucketName 存储空间的名称
     * @return 返回操作返回的状态码
     */
    String dropBucket(String bucketName);

    /**
     * 根据指定的存储空间查询该空间内所有的文件
     * @param bucketName 存储空间名
     * @return 返回该空间内所有的文件列表
     */
    List<Bucket> bucketList(String bucketName);

    /**
     * 设置公开Bucket存储权限
     * @param bucketName 存储空间名
     * @return 返回状态码
     */
    String setBucketPublic(String bucketName);

    /**
     * 设置私有Bucket存储权限
     * @param bucketName 存储空间名
     * @return 返回状态码
     */
    String setBucketPrivate(String bucketName);
}
