package cn.tedu.authuploadsystem.service.impl;

import cn.tedu.authuploadsystem.ex.ServiceException;
import cn.tedu.authuploadsystem.pojo.entity.CopyToFile;
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
import org.springframework.web.multipart.MultipartFile;

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
     * @param file       图片名称
     * @param bucketName 空间名称
     * @return 返回文件名
     */
    @Override
    public String uploadImage(MultipartFile file, String fileName, String bucketName) {
        log.debug("开始处理上传图片文件的功能，参数：{}", file);
        String result = null;
        try {
            result = uploadFile(file.getBytes(),fileName, bucketName);
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
     * 复制文件(如果isCover为false，目标文件存在时会抛出614;如果为true，目标文件会直接被覆盖)
     *
     * @param copyToFile 复制的文件实体类
     * @return 返回结果状态码
     */
    @Override
    public String copyToFile(CopyToFile copyToFile) {
        log.debug("处理复制文件的业务,参数：{}", copyToFile);
        // 如果isCover为false，目标文件存在时会抛出614
        String EncodedEntryURISrc = copyToFile.getBucketName() + ":" + copyToFile.getNowFileKey();
        String EncodedEntryURIDest = copyToFile.getBucketName() + ":" + copyToFile.getLastFileKey();
        String isCover = copyToFile.getIsCover();
        Auth auth = Auth.create(accessKey, secretKey);// 将AK和SK传入进行认证
        String path = "/copy/" + BASE64Encoder.encode(EncodedEntryURISrc.getBytes()) + "/" + BASE64Encoder.encode(EncodedEntryURIDest.getBytes()) + "/force/" + isCover + "\n";
        log.debug("请求的路径为：" + path);
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://rs.qiniuapi.com/copy/" + BASE64Encoder.encode(EncodedEntryURISrc.getBytes()) + "/" + BASE64Encoder.encode(EncodedEntryURIDest.getBytes()) + "/force/" + isCover;
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
                if (re.code() == 614) {
                    String message = "设置失败，目标文件名已经存在！";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                } else if (re.code() == 612) {
                    String message = "设置失败，源文件不存在或被删除！";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return re.code() + "";
    }

    /**
     * 修改标准存储类型
     *
     * @param bucketName 存储空间名称
     * @param fileName   文件名
     * @return 返回结果状态码
     */
    @Override
    public String setBucketType0(String bucketName, String fileName) {
        return setBucketType(bucketName, fileName, 0);
    }

    /**
     * 修改低频访问存储
     *
     * @param bucketName 存储空间名称
     * @param fileName   文件名
     * @return 返回结果状态码
     */
    @Override
    public String setBucketType1(String bucketName, String fileName) {
        return setBucketType(bucketName, fileName, 1);
    }

    /**
     * 修改归档存储类型
     *
     * @param bucketName 存储空间名称
     * @param fileName   文件名
     * @return 返回结果状态码
     */
    @Override
    public String setBucketType2(String bucketName, String fileName) {
        return setBucketType(bucketName, fileName, 2);
    }

    /**
     * 修改深度归档存储类型
     *
     * @param bucketName 存储空间名称
     * @param fileName   文件名
     * @return 返回结果状态码
     */
    @Override
    public String setBucketType3(String bucketName, String fileName) {
        return setBucketType(bucketName, fileName, 3);
    }

    /**
     * 解冻归档文件
     *
     * @param bucketName 存储空间名
     * @param fileName   文件名
     * @param time       解冻时间（1~7）
     * @return 返回结果状态码
     */
    @Override
    public String fileToThaw(String bucketName, String fileName, String time) {
        log.debug("开始处理解冻存储空间：{}的文件：{}，解冻有效时间为：{}天", bucketName, fileName, time);
        String EncodedEntryURI = bucketName + ":" + fileName;
        Auth auth = Auth.create(accessKey, secretKey);// 将AK和SK传入进行认证
        String path = "/restoreAr/" + BASE64Encoder.encode(EncodedEntryURI.getBytes()) + "/freezeAfterDays/" + time + "\n";
        log.debug("认证的路径为：" + path);
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://rs.qiniuapi.com/restoreAr/" + BASE64Encoder.encode(EncodedEntryURI.getBytes()) + "/freezeAfterDays/" + time;
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
                System.out.println("错误代码：" + re.code());
                System.out.println(re.toString());
                if (re.code() == 612) {
                    String message = "设置失败，文件不存在或被删除！";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                } else if (re.code() == 631) {
                    String message = "修改失败，该空间不存在！";
                    throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
                } else if (re.code() == 401) {
                    String message = "认证信息有误！";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                } else if (re.code() == 400) {
                    String message = "解冻失败，该文件正在解冻或解冻时间无效！";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                } else if (re.code() == 403) {
                    String message = "解冻失败，该文件为非归档文件！";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return re.code() + "";
    }

    /**
     * 设置文件过期时间
     *
     * @param bucketName 存储空间名称
     * @param fileName   文件名称
     * @param days       过期时间(天)
     * @return 返回结果状态码
     */
    @Override
    public String setOverTime(String bucketName, String fileName, Integer days) {
        log.debug("开始处理设置存储空间:{}的文件:{}过期时间为:{}天", bucketName, fileName, days);
        String EncodedEntryURI = bucketName + ":" + fileName;
        Auth auth = Auth.create(accessKey, secretKey);// 将AK和SK传入进行认证
        String path = "/deleteAfterDays/" + BASE64Encoder.encode(EncodedEntryURI.getBytes()) + "/" + days + "\n";
        log.debug("认证的路径为：" + path);
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://rs.qiniuapi.com/deleteAfterDays/" + BASE64Encoder.encode(EncodedEntryURI.getBytes()) + "/" + days;
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
                System.out.println("错误代码：" + re.code());
                System.out.println(re.toString());
                if (re.code() == 612) {
                    String message = "设置失败，文件不存在或被删除！";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                } else if (re.code() == 631) {
                    String message = "修改失败，该空间不存在！";
                    throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
                } else if (re.code() == 401) {
                    String message = "认证信息有误！";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                } else if (re.code() == 403) {
                    String message = "归档存储文件未解冻完成,请前往解冻!";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return re.code() + "";
    }

    /**
     * 处理修改文件存储类型的功能
     *
     * @param bucketName 空间名称
     * @param fileName   文件名
     * @param typeId     类型Id
     * @return 返回结果状态码
     */
    private String setBucketType(String bucketName, String fileName, Integer typeId) {
        String[] tips = {"标椎存储", "低频访问存储", "归档存储", "深度归档存储"};
        log.debug("开始处理修改存储空间：{}中的文件：{}，类型为：{}", bucketName, fileName, tips[typeId]);
        String EncodedEntryURI = bucketName + ":" + fileName;
        int type = typeId; // 存储类型编号(0 表示标准存储，1 表示低频访问存储，2 表示归档存储，3 表示深度归档存储)
        Auth auth = Auth.create(accessKey, secretKey);// 将AK和SK传入进行认证
        String path = "/chtype/" + BASE64Encoder.encode(EncodedEntryURI.getBytes()) + "/type/" + type + "\n";
        log.debug("认证的路径为：" + path);
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://rs.qiniuapi.com/chtype/" + BASE64Encoder.encode(EncodedEntryURI.getBytes()) + "/type/" + type;
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
                System.out.println("错误代码：" + re.code());
                System.out.println(re.toString());
                if (re.code() == 612) {
                    String message = "设置失败，文件不存在或被删除！";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                } else if (re.code() == 631) {
                    String message = "修改失败，该空间不存在！";
                    throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
                } else if (re.code() == 401) {
                    String message = "认证信息有误！";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                } else if (re.code() == 403) {
                    String message = "归档存储文件未解冻完成,请前往解冻!";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                } else if (re.code() == 400) {
                    String message = "修改失败，当前已经处于" + tips[typeId];
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return re.code() + "";
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
     * @param fileName   文件名
     * @param bucketName 空间名
     * @throws Exception 异常
     */
    private String uploadFile(byte[] file,String fileName, String bucketName) throws Exception {
        File targetFile = new File(dirPath);
        if (!targetFile.exists()) {
            boolean mkdirs = targetFile.mkdirs();
            String message = mkdirs ? "创建文件成功" : "创建文件失败";
            System.out.println(message);
        }
        FileOutputStream out = new FileOutputStream(dirPath + "/" + fileName);// 创建文件输出流，指定需要写入的文件路径
        out.write(file);
        out.flush(); // 刷新文件输出流
        out.close(); // 关闭文件输出流
        Configuration cfg = new Configuration(Zone.zone2()); // 创建配置，传入区域
        UploadManager uploadManager = new UploadManager(cfg); // 创建上传信息，传入区域

        String localFilePath = dirPath + "/" + fileName; // 上传图片路径

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
