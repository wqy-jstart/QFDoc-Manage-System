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
public interface IFileService {

    /**
     * 上传图片的抽象方法
     * @param fileName 图片名称
     * @param bucketName 空间名称
     * @return 返回最终的图片名称
     */
    String uploadImage(String fileName,String bucketName);

    /**
     * 删除文件的功能
     * @param bucketName 存储空间的名称
     * @param key 文件名
     * @return 返回结果状态码
     */
    String deleteToFile(String bucketName,String key);

    /**
     * 修改文件的存储状态为启用(0:启用 / 1:禁用)
     * @param bucketName 存储空间名
     * @param key 文件名
     * @return 返回结果状态码
     */
    String setFileStatusToEnable(String bucketName,String key);

    /**
     * 修改文件的存储状态为禁用(0:启用 / 1:禁用)
     * @param bucketName 存储空间名
     * @param key 文件名
     * @return 返回结果状态码
     */
    String setFileStatusToDisable(String bucketName,String key);
}
