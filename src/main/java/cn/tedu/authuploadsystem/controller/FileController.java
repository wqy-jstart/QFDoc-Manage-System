package cn.tedu.authuploadsystem.controller;

import cn.tedu.authuploadsystem.pojo.entity.CopyToFile;
import cn.tedu.authuploadsystem.service.IFileService;
import cn.tedu.authuploadsystem.web.JsonResult;
import com.alibaba.fastjson.JSONException;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;

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
@RequestMapping("/file")
public class FileController {

    @Value("${image.dirPath}")
    private String dirPath;

    // 注入业务层接口
    @Autowired
    private IFileService fileService;

    public FileController() {
        log.debug("创建控制器类：UploadController");
    }

    /**
     * 处理文件上传
     *
     * @param file 文件
     * @return 返回文件上传的路径
     * @throws JSONException 序列化异常
     */
    @ApiOperation("文件上传")
    @ApiOperationSupport(order = 100)
    @PostMapping("/image")
    public JsonResult<String> uploadImg(MultipartFile file) throws JSONException {
        System.out.println("图片名称:" + file);
        String fileName = System.currentTimeMillis() + file.getOriginalFilename();
        String buckName = "jstart";
        String result = fileService.uploadImage(fileName, buckName);
        return JsonResult.ok(result);
    }

    /**
     * 删除本地文件
     *
     * @param url 本地文件路径
     */
    @ApiOperation("删除上传后的本地图片")
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

    /**
     * 删除指定空间内的文件
     *
     * @param bucketName 存储空间名称
     * @param key        文件名
     * @return 返回结果状态码
     */
    @ApiOperation("删除指定空间的文件")
    @ApiOperationSupport(order = 300)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "存储空间名", required = true, dataType = "string"),
            @ApiImplicitParam(name = "key", value = "文件名", required = true, dataType = "string"),
    })
    @PostMapping("/{bucketName}/{key}/deleteToFile")
    public JsonResult<String> deleteToFile(@PathVariable String bucketName,
                                           @PathVariable String key) {
        log.debug("开始处理删除存储空间：{}的文件：{}", bucketName, key);
        String statusId = fileService.deleteToFile(bucketName, key);
        return JsonResult.ok(statusId);
    }

    /**
     * 修改文件启用状态
     *
     * @param bucketName 存储空间名
     * @param key        文件名
     * @return 返回结果状态码
     */
    @ApiOperation("修改文件启用状态")
    @ApiOperationSupport(order = 400)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "存储空间名", required = true, dataType = "string"),
            @ApiImplicitParam(name = "key", value = "文件名", required = true, dataType = "string"),
    })
    @PostMapping("/{bucketName}/{key}/setFileStatusToEnable")
    public JsonResult<String> setFileStatusToEnable(@PathVariable String bucketName,
                                                    @PathVariable String key) {
        log.debug("开始处理存储空间：{}的文件：{}的存储状态为启用", bucketName, key);
        String statusId = fileService.setFileStatusToEnable(bucketName, key);
        return JsonResult.ok(statusId);
    }


    /**
     * 修改文件禁用状态
     *
     * @param bucketName 存储空间名
     * @param key        文件名
     * @return 返回结果状态码
     */
    @ApiOperation("修改文件禁用状态")
    @ApiOperationSupport(order = 401)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "存储空间名", required = true, dataType = "string"),
            @ApiImplicitParam(name = "key", value = "文件名", required = true, dataType = "string"),
    })
    @PostMapping("/{bucketName}/{key}/setFileStatusToDisable")
    public JsonResult<String> setFileStatusToDisable(@PathVariable String bucketName,
                                                     @PathVariable String key) {
        log.debug("开始处理存储空间：{}的文件：{}的存储状态为禁用", bucketName, key);
        String statusId = fileService.setFileStatusToDisable(bucketName, key);
        return JsonResult.ok(statusId);
    }

    /**
     * 复制文件
     *
     * @param copyToFile 复制的所有文件信息
     * @return 返回结果状态码
     */
    @ApiOperation("复制文件")
    @ApiOperationSupport(order = 403)
    @PostMapping("/copyToFile")
    public JsonResult<String> copyToFile(@Valid CopyToFile copyToFile) {
        log.debug("开始处理复制文件的请求---存储空间名：{}；源文件：{}；目标文件：{}；是否覆盖：{}", copyToFile.getBucketName(),
                copyToFile.getNowFileKey(), copyToFile.getLastFileKey(), copyToFile.getIsCover());
        String statusId = fileService.copyToFile(copyToFile);
        return JsonResult.ok(statusId);
    }

    /**
     * 设置存储类型为标准访问类型
     *
     * @param bucketName 存储空间名
     * @param fileName   文件名
     * @return 返回结果状态码
     */
    @ApiOperation("设置存储类型为标准访问类型")
    @ApiOperationSupport(order = 404)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "存储空间名", required = true, dataType = "string"),
            @ApiImplicitParam(name = "fileName", value = "文件名", required = true, dataType = "string"),
    })
    @PostMapping("/{bucketName}/{fileName}/setBucketType0")
    public JsonResult<String> setBucketType0(@PathVariable String bucketName, @PathVariable String fileName) {
        log.debug("开始处理修改文件访问类型，存储空间：{}，文件名：{} ---标准访问类型", bucketName, fileName);
        String result = fileService.setBucketType0(bucketName, fileName);
        return JsonResult.ok(result);
    }

    /**
     * 设置存储类型为低频访问类型
     *
     * @param bucketName 存储空间名
     * @param fileName   文件名
     * @return 返回结果状态码
     */
    @ApiOperation("设置存储类型为低频访问类型")
    @ApiOperationSupport(order = 405)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "存储空间名", required = true, dataType = "string"),
            @ApiImplicitParam(name = "fileName", value = "文件名", required = true, dataType = "string"),
    })
    @PostMapping("/{bucketName}/{fileName}/setBucketType1")
    public JsonResult<String> setBucketType1(@PathVariable String bucketName, @PathVariable String fileName) {
        log.debug("开始处理修改文件访问类型，存储空间：{}，文件名：{} ---低频访问类型", bucketName, fileName);
        String result = fileService.setBucketType1(bucketName, fileName);
        return JsonResult.ok(result);
    }

    /**
     * 设置存储类型为归档访问类型
     *
     * @param bucketName 存储空间名
     * @param fileName   文件名
     * @return 返回结果状态码
     */
    @ApiOperation("设置存储类型为归档访问类型")
    @ApiOperationSupport(order = 406)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "存储空间名", required = true, dataType = "string"),
            @ApiImplicitParam(name = "fileName", value = "文件名", required = true, dataType = "string"),
    })
    @PostMapping("/{bucketName}/{fileName}/setBucketType2")
    public JsonResult<String> setBucketType2(@PathVariable String bucketName, @PathVariable String fileName) {
        log.debug("开始处理修改文件访问类型，存储空间：{}，文件名：{} ---归档访问类型", bucketName, fileName);
        String result = fileService.setBucketType2(bucketName, fileName);
        return JsonResult.ok(result);
    }

    /**
     * 设置存储类型为深度归档访问类型
     *
     * @param bucketName 存储空间名
     * @param fileName   文件名
     * @return 返回结果状态码
     */
    @ApiOperation("设置存储类型为深度归档访问类型")
    @ApiOperationSupport(order = 407)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "存储空间名", required = true, dataType = "string"),
            @ApiImplicitParam(name = "fileName", value = "文件名", required = true, dataType = "string"),
    })
    @PostMapping("/{bucketName}/{fileName}/setBucketType3")
    public JsonResult<String> setBucketType3(@PathVariable String bucketName, @PathVariable String fileName) {
        log.debug("开始处理修改文件访问类型，存储空间：{}，文件名：{} ---深度归档访问类型", bucketName, fileName);
        String result = fileService.setBucketType3(bucketName, fileName);
        return JsonResult.ok(result);
    }
}
