package cn.tedu.authuploadsystem.service;

import cn.tedu.authuploadsystem.pojo.entity.CopyToFile;
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

    /**
     * 复制文件
     * @param copyToFile 复制的文件实体类
     * @return 返回结果状态码
     */
    String copyToFile(CopyToFile copyToFile);

    /**
     * 修改文件的存储类型-----标椎存储
     * @param bucketName 存储空间名称
     * @param fileName 文件名
     * @return 返回结果状态码
     */
    String setBucketType0(String bucketName,String fileName);

    /**
     * 修改文件的存储类型-----低频访问存储存储
     * @param bucketName 存储空间名称
     * @param fileName 文件名
     * @return 返回结果状态码
     */
    String setBucketType1(String bucketName,String fileName);

    /**
     * 修改文件的存储类型-----归档存储
     * @param bucketName 存储空间名称
     * @param fileName 文件名
     * @return 返回结果状态码
     */
    String setBucketType2(String bucketName,String fileName);

    /**
     * 修改文件的存储类型-----深度归档存储
     * @param bucketName 存储空间名称
     * @param fileName 文件名
     * @return 返回结果状态码
     */
    String setBucketType3(String bucketName,String fileName);

    /**
     * 归档文件解冻
     * @param bucketName 存储空间名
     * @param fileName 文件名
     * @param time 解冻时间（1~7）
     * @return 返回结果状态码
     */
    String fileToThaw(String bucketName,String fileName,String time);

    /**
     * 设置文件过期时间
     * @param bucketName 存储空间名称
     * @param fileName 文件名称
     * @param days 过期时间(天)
     * @return 返回结果状态码
     */
    String setOverTime(String bucketName,String fileName,Integer days);
}
