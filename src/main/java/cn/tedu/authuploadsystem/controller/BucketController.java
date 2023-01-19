package cn.tedu.authuploadsystem.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 存储空间的控制器层
 */
@Api(tags = "存储空间管理")
@Slf4j
@Validated
@RestController
@RequestMapping("/bucket")
public class BucketController {
    /**
     * @param bucketName  空间名称
     * @return string code
     */
    @ApiOperation("创建存储空间")
    @ApiOperationSupport(order = 100)
    @ApiImplicitParam(name = "bucketName",value = "空间名",required = true,dataType = "string")
    @PostMapping("/{bucketName}/create")
    public String createBucket(@PathVariable String bucketName) {
        log.debug("接受传递的空间名为："+bucketName);
        String accessKey = "ezgtMB0XLRrugRqaeg1NiyLFI3O0eoVyj8y0fUQT"; // AccessKey的值
        String secretKey = "Bz_jGB3IGhGTTGnVIAF2vyniKshV2Wx9ttX0bc9_"; // SecretKey的值
        String storageArea = "z2"; // 区域值
        Auth auth = Auth.create(accessKey,secretKey);// 将AK和SK传入进行认证
        String path = "/mkbucketv2/" + encode(bucketName.getBytes()) + "/region/"+ storageArea +"\n";
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://rs.qiniu.com/mkbucketv2/" + encode(bucketName.getBytes()) + "/region/" + storageArea;
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

    /**
     * 编码
     *
     * @param str 参数
     * @return String
     */
    public static String encode(byte[] str) {
        return new sun.misc.BASE64Encoder().encode(str);
    }
}
