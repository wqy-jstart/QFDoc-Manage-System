package cn.tedu.authuploadsystem.controller;

import cn.tedu.authuploadsystem.service.IBucketService;
import cn.tedu.authuploadsystem.util.BASE64Encoder;
import cn.tedu.authuploadsystem.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IBucketService bucketService;

    public BucketController(){
        log.debug("创建控制器类：BucketController");
    }

    /**
     * @param bucketName  空间名称
     * @return string code
     */
    @ApiOperation("创建存储空间")
    @ApiOperationSupport(order = 100)
    @ApiImplicitParam(name = "bucketName",value = "空间名",required = true,dataType = "string")
    @PostMapping("/{bucketName}/create")
    public JsonResult<String> createBucket(@PathVariable String bucketName) {
        log.debug("接受传递的空间名为："+bucketName);
        String bucket = bucketService.createBucket(bucketName);
        return JsonResult.ok(bucket);
    }
}
