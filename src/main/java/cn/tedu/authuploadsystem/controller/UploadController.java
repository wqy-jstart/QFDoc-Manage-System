package cn.tedu.authuploadsystem.controller;

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
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    //处理文件上传
    @ApiOperation("文件上传")
    @ApiOperationSupport(order = 100)
    @PostMapping("/image")
    public JsonResult<String> uploadImg(MultipartFile file) throws JSONException {
        System.out.println("图片名称:" + file);
        String fileName = System.currentTimeMillis()+file.getOriginalFilename();
        try {
            uploadFile(fileName.getBytes(), dirPath , fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回json
        return JsonResult.ok(fileName);
    }

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
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

        String accessKey = "ezgtMB0XLRrugRqaeg1NiyLFI3O0eoVyj8y0fUQT"; // AccessKey的值
        String secretKey = "Bz_jGB3IGhGTTGnVIAF2vyniKshV2Wx9ttX0bc9_"; // SecretKey的值
        String bucket = "jstart"; // 存储空间名
        String localFilePath = filePath + "/" + fileName; // 上传图片路径

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket); // 上传文件时生成的凭证Token
        try {
            Response response = uploadManager.put(localFilePath, fileName, upToken);
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key); // 上传成功后的文件名
            System.out.println(putRet.hash); // 上传成功后的hash值
        } catch (QiniuException ex) { // 手动捕获一个异常
            System.out.println("出现QuniuException异常！");
        }
    }

    // 删除文件
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
