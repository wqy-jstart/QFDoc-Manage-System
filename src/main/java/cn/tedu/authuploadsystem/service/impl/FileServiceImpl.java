package cn.tedu.authuploadsystem.service.impl;

import cn.tedu.authuploadsystem.ex.ServiceException;
import cn.tedu.authuploadsystem.service.IFileService;
import cn.tedu.authuploadsystem.util.BASE64Encoder;
import cn.tedu.authuploadsystem.web.ServiceCode;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 上传文件的业务层接口类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.19
 */
@Slf4j
@Service
public class FileServiceImpl implements IFileService {

    // 图片本地路径
    @Value("${image.dirPath}")
    private String dirPath;
    // AK
    @Value("${auth.key.accessKey}")
    private String accessKey;
    // SK
    @Value("${auth.key.secretKey}")
    private String secretKey;

    public FileServiceImpl() {
        log.debug("创建业务层实现类：UploadServiceImpl");
    }

    /**
     * 上传图片
     *
     * @param fileName   图片名称
     * @param bucketName 空间名称
     * @return 返回文件名
     */
    @Override
    public String uploadImage(String fileName, String bucketName) {
        log.debug("开始处理上传图片文件的功能，参数：{}", fileName);
        String result = null;
        try {
            result = uploadFile(fileName.getBytes(), dirPath, fileName, bucketName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 处理删除文件的功能
     *
     * @param bucketName 存储空间的名称
     * @param key        文件名
     * @return 返回结果状态码
     */
    @Override
    public String deleteToFile(String bucketName, String key) {
        log.debug("开始处理删除存储空间：{}的文件：{}", bucketName, key);
        String entry = bucketName + ":" + key;
        Auth auth = Auth.create(accessKey, secretKey);// 将AK和SK传入进行认证
        String path = "/delete/" + BASE64Encoder.encode(entry.getBytes()) + "\n";
        log.debug("请求的路径为：" + path);
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://rs.qiniuapi.com/delete/" + BASE64Encoder.encode(entry.getBytes());
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "QBox " + access_token).build();
        okhttp3.Response re = null;
        try {
            re = client.newCall(request).execute();
            if (re.isSuccessful()) { // 判断执行结果是否成功！
                System.out.println(re.code());
                System.out.println(re.toString());
            } else {
                System.out.println(re.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return re.code() + "";
    }

    /**
     * 实现启用的功能
     *
     * @param bucketName 存储空间名
     * @param key        文件名
     * @return 返回结果状态码
     */
    @Override
    public String setFileStatusToEnable(String bucketName, String key) {
        return setFileStatus(bucketName, key, 0);
    }

    /**
     * 实现禁用的功能
     *
     * @param bucketName 存储空间名
     * @param key        文件名
     * @return 返回结果状态码
     */
    @Override
    public String setFileStatusToDisable(String bucketName, String key) {
        return setFileStatus(bucketName, key, 1);
    }

    /**
     * 处理修改指定存储空间文件的存储状态---逻辑代码
     *
     * @param bucketName 存储空间名
     * @param key        文件名
     * @param status     (0:启用 / 1:禁用)
     * @return 返回结果状态码
     */
    private String setFileStatus(String bucketName, String key, Integer status) {
        String[] tips = {"启用", "禁用"};
        log.debug("开始处理存储空间：{}的文件：{}存储状态为：{}", bucketName, key, tips[status]);
        String entry = bucketName + ":" + key;
        Auth auth = Auth.create(accessKey, secretKey);// 将AK和SK传入进行认证
        String path = "/chstatus/" + BASE64Encoder.encode(entry.getBytes()) + "/status/" + status + "\n";
        log.debug("请求的路径为：" + path);
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://rs.qiniuapi.com/chstatus/" + BASE64Encoder.encode(entry.getBytes()) + "/status/" + status;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "QBox " + access_token).build();
        okhttp3.Response re = null;
        try {
            re = client.newCall(request).execute();
            if (re.isSuccessful()) { // 判断执行结果是否成功！
                System.out.println(re.code());
                System.out.println(re.toString());
            } else {
                if (re.code() == 400) {
                    String message = "设置失败，该文件已经处于" + tips[status] + "状态";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return re.code() + "";
    }

    /**
     * 该方法是处理上传图片的核心方法---逻辑代码
     *
     * @param file       文件的二进制
     * @param filePath   文件的路径
     * @param fileName   文件名
     * @param bucketName 空间名
     * @throws Exception 异常
     */
    private String uploadFile(byte[] file, String filePath, String fileName, String bucketName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            boolean mkdirs = targetFile.mkdirs();
            String message = mkdirs ? "创建文件成功" : "创建文件失败";
            System.out.println(message);
        }
        FileOutputStream out = new FileOutputStream(filePath + "/" + fileName);// 创建文件输出流，指定需要写入的文件路径
        out.write(file);
        out.flush(); // 刷新文件输出流
        out.close(); // 关闭文件输出流
        Configuration cfg = new Configuration(Zone.zone2()); // 创建配置，传入区域
        UploadManager uploadManager = new UploadManager(cfg); // 创建上传信息，传入区域

        String localFilePath = filePath + "/" + fileName; // 上传图片路径

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucketName); // 上传文件时生成的凭证Token
        String result = null;
        try {
            Response response = uploadManager.put(localFilePath, fileName, upToken);
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key); // 上传成功后的文件名
            result = putRet.key;
            System.out.println(putRet.hash); // 上传成功后的hash值
        } catch (QiniuException ex) { // 手动捕获一个异常
            System.out.println("出现QuniuException异常！");
        }
        return result;
    }
}
