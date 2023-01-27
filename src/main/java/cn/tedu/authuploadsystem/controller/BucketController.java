package cn.tedu.authuploadsystem.controller;

import cn.tedu.authuploadsystem.pojo.entity.Bucket;
import cn.tedu.authuploadsystem.pojo.entity.Tag;
import cn.tedu.authuploadsystem.service.IBucketService;
import cn.tedu.authuploadsystem.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    public BucketController() {
        log.debug("创建控制器类：BucketController");
    }

    /**
     * @param bucketName 空间名称
     * @return string code
     */
    @ApiOperation("创建存储空间")
    @ApiOperationSupport(order = 100)
    @ApiImplicitParam(name = "bucketName", value = "空间名", required = true, dataType = "string")
    @PreAuthorize("hasAuthority('/space/create')")
    @PostMapping("/{bucketName}/create")
    public JsonResult<String> createBucket(@PathVariable String bucketName) {
        log.debug("开始处理创建存储空间的请求，参数：{}" + bucketName);
        String bucket = bucketService.createBucket(bucketName);
        return JsonResult.ok(bucket);
    }

    /**
     * 删除存储空间
     *
     * @param bucketName 存储空间的名称
     * @return 返回操作的结果状态码
     */
    @ApiOperation("删除存储空间")
    @ApiOperationSupport(order = 200)
    @ApiImplicitParam(name = "bucketName", value = "存储空间名称", required = true, dataType = "string")
    @PreAuthorize("hasAuthority('/space/delete')")
    @PostMapping("/{bucketName}/drop")
    public JsonResult<String> dropBucket(@PathVariable String bucketName) {
        log.debug("开始处理删除存储空间的请求，参数：{}", bucketName);
        String s = bucketService.dropBucket(bucketName);
        return JsonResult.ok(s);
    }

    /**
     * 查询指定存储空间的列表
     *
     * @param bucketName 空间名称
     * @return 返回查询的该空间的文件列表
     */
    @ApiOperation("查询指定存储空间的列表")
    @ApiOperationSupport(order = 300)
    @ApiImplicitParam(name = "bucketName", value = "空间名称", required = true, dataType = "string")
    @PreAuthorize("hasAuthority('/space/list')")
    @GetMapping("/{bucketName}/bucketList")
    public JsonResult<List<Bucket>> bucketList(@PathVariable String bucketName) {
        log.debug("开始处理查询存储空间：{}的文件列表", bucketName);
        List<Bucket> buckets = bucketService.bucketList(bucketName);
        return JsonResult.ok(buckets);
    }

    @ApiOperation("设置空间公开权限")
    @ApiOperationSupport(order = 400)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "存储空间名", required = true, dataType = "string")
    })
    @PreAuthorize("hasAuthority('/space/updateToAuth')")
    @PostMapping("/{bucketName}/setBucketPublic")
    public JsonResult<String> setBucketPublic(@PathVariable String bucketName) {
        log.debug("开始处理设置存储空间：{}的访问权限为公开！", bucketName);
        String result = bucketService.setBucketPublic(bucketName);
        return JsonResult.ok(result);
    }

    @ApiOperation("设置空间私有权限")
    @ApiOperationSupport(order = 401)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "存储空间名", required = true, dataType = "string")
    })
    @PreAuthorize("hasAuthority('/space/updateToAuth')")
    @PostMapping("/{bucketName}/setBucketPrivate")
    public JsonResult<String> setBucketPrivate(@PathVariable String bucketName) {
        log.debug("开始处理设置存储空间：{}的访问权限为私有！", bucketName);
        String result = bucketService.setBucketPrivate(bucketName);
        return JsonResult.ok(result);
    }

    /**
     * 设置存储空间的标签
     *
     * @param bucketName 空间名
     * @param key        标签名
     * @param value      标签值
     * @return 返回结果状态码
     */
    @ApiOperation("设置标签")
    @ApiOperationSupport(order = 402)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "空间名", required = true, dataType = "string"),
            @ApiImplicitParam(name = "key", value = "标签名", required = true, dataType = "string"),
            @ApiImplicitParam(name = "value", value = "标签值", required = true, dataType = "string"),

    })
    @PreAuthorize("hasAuthority('/space/AddTags')")
    @PostMapping("/{bucketName}/{key}/{value}/setBucketTags")
    public JsonResult<String> setBucketTags(@PathVariable String bucketName,
                                            @PathVariable String key,
                                            @PathVariable String value) {
        log.debug("开始处理添加存储空间:{}的标签的请求,key:{};value:{}", bucketName, key, value);
        String s = bucketService.setBucketTags(bucketName, key, value);
        return JsonResult.ok(s);
    }

    /**
     * 查询指定存储空间的标签数据
     *
     * @param bucketName 空间名
     * @return 返回List集合
     */
    @ApiOperation("查询指定存储空间的标签数据")
    @ApiOperationSupport(order = 403)
    @ApiImplicitParam(name = "bucketName", value = "空间名称", required = true, dataType = "string")
    @PreAuthorize("hasAuthority('/space/list')")
    @GetMapping("/{bucketName}/selectToTags")
    public JsonResult<List<Tag>> selectToTags(@PathVariable String bucketName) {
        log.debug("开始处理查询存储空间:{}的标签数据", bucketName);
        List<Tag> list = bucketService.selectToTags(bucketName);
        return JsonResult.ok(list);
    }

    /**
     * 删除空间标签
     *
     * @param bucketName 空间名
     * @return 返回结果状态码
     */
    @ApiOperation("删除空间标签")
    @ApiOperationSupport(order = 404)
    @ApiImplicitParam(name = "bucketName", value = "空间名称", required = true, dataType = "string")
    @PreAuthorize("hasAuthority('/space/delTags')")
    @DeleteMapping("/{bucketName}/deleteToTags")
    public JsonResult<String> deleteToTags(@PathVariable String bucketName) {
        log.debug("开始处理删除存储空间:{}的标签信息的请求", bucketName);
        String result = bucketService.deleteToTags(bucketName);
        return JsonResult.ok(result);
    }
}
