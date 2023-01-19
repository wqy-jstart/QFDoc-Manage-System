package cn.tedu.authuploadsystem.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * 上载的业务层接口
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.19
 */
@Transactional
public interface IUploadService {

    /**
     * 上传图片的抽象方法
     * @param fileName 图片名称
     * @param bucketName 空间名称
     * @return 返回最终的图片名称
     */
    String uploadImage(String fileName,String bucketName);
}
