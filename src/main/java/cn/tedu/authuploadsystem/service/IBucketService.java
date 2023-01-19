package cn.tedu.authuploadsystem.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

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
}
