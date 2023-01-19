package cn.tedu.authuploadsystem.service.impl;

import cn.tedu.authuploadsystem.service.IBucketService;
import cn.tedu.authuploadsystem.util.BASE64Encoder;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 存储空间的业务层接口实现类
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.19
 */
@Slf4j
@Service
public class BucketServiceImpl implements IBucketService {

    // AK
    @Value("${auth.key.accessKey}")
    private String accessKey;
    // SK
    @Value("${auth.key.secretKey}")
    private String secretKey;
    // 地域值
    @Value("${auth.key.storageArea}")
    private String storageArea;

    public BucketServiceImpl(){
        log.debug("创建业务层接口实现类：BucketServiceImpl");
    }

    /**
     * 创建存储空间的实现方法
     * @param bucketName 空间名
     * @return 返回创建的状态码
     */
    @Override
    public String createBucket(String bucketName) {
        log.debug("开始处理创建存储空间的功能，参数：{}",bucketName);
        Auth auth = Auth.create(accessKey,secretKey);// 将AK和SK传入进行认证
        String path = "/mkbucketv2/" + BASE64Encoder.encode(bucketName.getBytes()) + "/region/"+ storageArea +"\n";
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://rs.qiniu.com/mkbucketv2/" + BASE64Encoder.encode(bucketName.getBytes()) + "/region/" + storageArea;
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
        return re.code()+""; // 将状态码返回给用户
    }
}
