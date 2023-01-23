package cn.tedu.authuploadsystem;

import cn.tedu.authuploadsystem.ex.ServiceException;
import cn.tedu.authuploadsystem.util.BASE64Encoder;
import cn.tedu.authuploadsystem.web.ServiceCode;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;

import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
@SpringBootTest
public class BucketTests {

    // AK
    @Value("${auth.key.accessKey}")
    private String accessKey;
    // SK
    @Value("${auth.key.secretKey}")
    private String secretKey;

    /**
     * 测试查询列表
     */
    @Test
    public void list() {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);

        //文件名前缀
        String prefix = "";
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";

        String bucket = "jstart"; // 某个存储空间的列表
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                System.out.println(item.hash);
                System.out.println(item.fsize);
                System.out.println(item.mimeType);
                System.out.println(item.putTime);
                System.out.println(item.type);
            }
        }
    }

    /**
     * 测试设置存储权限
     */
    @Test
    public void setPrivate() {
        String bucketName = "jstart7";
        int privateId = 1;
        Auth auth = Auth.create(accessKey, secretKey);// 将AK和SK传入进行认证
        String path = "/private?bucket=" + bucketName + "&private=" + privateId + "\n";
        log.debug("请求的路径为：" + path);
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://uc.qiniuapi.com/private?bucket=" + bucketName + "&private=" + privateId;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "QBox " + access_token).build();
        Response re = null;
        try {
            re = client.newCall(request).execute();
            if (re.isSuccessful() == true) { // 判断执行结果是否成功！
                System.out.println(re.code());
                System.out.println(re.toString());
            } else {
                System.out.println(re.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件(已完成！)
     * 612:指定资源不存在或已被删除
     */
    @Test
    public void deleteToFile() {
        String bucketName = "jstart";
        String fileName = "1674115449345tunshi.webp";
        String entry = bucketName + ":" + fileName;
        Auth auth = Auth.create(accessKey, secretKey);// 将AK和SK传入进行认证
        String path = "/delete/" + BASE64Encoder.encode(entry.getBytes()) + "\n";
        log.debug("请求的路径为：" + path);
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://rs.qiniuapi.com/delete/" + BASE64Encoder.encode(entry.getBytes());
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "QBox " + access_token).build();
        Response re = null;
        try {
            re = client.newCall(request).execute();
            if (re.isSuccessful() == true) { // 判断执行结果是否成功！
                System.out.println(re.code());
                System.out.println(re.toString());
            } else {
                System.out.println(re.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改文件存储状态(已完成！)
     */
    @Test
    public void setFileStatus() {
        String bucketName = "jstart";
        String fileName = "1674128424063多用户文档管理系统.md";
        int status = 1; // 0表示启用，1表示禁用
        String entry = bucketName + ":" + fileName;
        Auth auth = Auth.create(accessKey, secretKey);// 将AK和SK传入进行认证
        String path = "/chstatus/" + BASE64Encoder.encode(entry.getBytes()) + "/status/" + status + "\n";
        log.debug("请求的路径为：" + path);
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://rs.qiniuapi.com/chstatus/" + BASE64Encoder.encode(entry.getBytes()) + "/status/" + status;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "QBox " + access_token).build();
        Response re = null;
        try {
            re = client.newCall(request).execute();
            if (re.isSuccessful() == true) { // 判断执行结果是否成功！
                System.out.println(re.code());
                System.out.println(re.toString());
            } else {
                System.out.println(re.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制文件(已完成！)
     * 需要选择覆盖或者不覆盖
     */
    @Test
    public void copyToFile() {
        String buckName = "jstart";
        String nowFileName = "1674131709841kj.jpg";
        String lastFileName = "a.jpg";
        String EncodedEntryURISrc = buckName + ":" + nowFileName;
        String EncodedEntryURIDest = buckName + ":" + lastFileName;
        boolean isCover = true;// 禁止直接覆盖
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
    }

    /**
     * 修改文件的存储类型
     * 实现标准存储、低频访问存储、归档存储和深度归档存储之间的互相转换。
     */
    @Test
    public void setBucketType() {
        String buckName = "jstart";
        String nowFileName = "1674131709841kj.jpg";
        String EncodedEntryURI = buckName + ":" + nowFileName;
        String type = "1"; // 存储类型编号(0 表示标准存储，1 表示低频访问存储，2 表示归档存储，3 表示深度归档存储)
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
                    String message = "归档存储文件未解冻完成";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                } else if (re.code() == 400) {
                    String message = "修改失败，当前已经处于";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
