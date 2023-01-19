package cn.tedu.authuploadsystem.controller;

import cn.tedu.authuploadsystem.service.IUploadService;
import cn.tedu.authuploadsystem.service.impl.UploadServiceImpl;
import cn.tedu.authuploadsystem.web.JsonResult;
import com.alibaba.fastjson.JSONException;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 上传图片到七牛云
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.16
 */
@Api(tags = "用户上传文件模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${image.dirPath}")
    private String dirPath;

    // 注入业务层接口
    @Autowired
    private IUploadService uploadService;

    public UploadController(){
        log.debug("创建控制器类：UploadController");
    }

    /**
     * 处理文件上传
     * @param file 文件
     * @return 返回文件上传的路径
     * @throws JSONException 序列化异常
     */
    @ApiOperation("文件上传")
    @ApiOperationSupport(order = 100)
    @PostMapping("/image")
    public JsonResult<String> uploadImg(MultipartFile file) throws JSONException {
        System.out.println("图片名称:" + file);
        String fileName = System.currentTimeMillis()+file.getOriginalFilename();
        String buckName = "jstart";
        String result = uploadService.uploadImage(fileName,buckName);
        return JsonResult.ok(result);
    }

    /**
     * 删除本地文件
     * @param url 本地文件路径
     */
    @ApiOperation("删除上传的图片")
    @ApiOperationSupport(order = 200)
    @GetMapping("/remove")
    public void remove(String url) {
        log.debug("开始处理删除图片的请求...路径:{}", url);
        if (new File(dirPath + "/" + url).delete()) {//File对象的delete()方法,返回值boolean
            System.out.println("删除成功!");
        } else {
            System.out.println("删除失败!");
        }
    }
}
