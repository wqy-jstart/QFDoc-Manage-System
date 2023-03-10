package cn.tedu.authuploadsystem.service.impl;

import cn.tedu.authuploadsystem.ex.ServiceException;
import cn.tedu.authuploadsystem.pojo.entity.Body;
import cn.tedu.authuploadsystem.pojo.entity.Bucket;
import cn.tedu.authuploadsystem.pojo.entity.Tag;
import cn.tedu.authuploadsystem.repo.IBucketRedisRepository;
import cn.tedu.authuploadsystem.service.IBucketService;
import cn.tedu.authuploadsystem.util.BASE64Encoder;
import cn.tedu.authuploadsystem.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    // 注入Bucket的缓存接口类
    @Autowired
    private IBucketRedisRepository bucketRedisRepository;

    public BucketServiceImpl() {
        log.debug("创建业务层接口实现类：BucketServiceImpl");
    }

    /**
     * 创建存储空间的实现方法
     *
     * @param bucketName 空间名
     * @return 返回创建的状态码
     */
    @Override
    public String createBucket(String bucketName) {
        log.debug("开始处理创建存储空间的功能，参数：{}", bucketName);
        Auth auth = Auth.create(accessKey, secretKey);// 将AK和SK传入进行认证
        String path = "/mkbucketv2/" + BASE64Encoder.encode(bucketName.getBytes()) + "/region/" + storageArea + "\n";
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://rs.qiniu.com/mkbucketv2/" + BASE64Encoder.encode(bucketName.getBytes()) + "/region/" + storageArea;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "QBox " + access_token).build();
        Response re = null;
        try {
            re = client.newCall(request).execute();
            if (re.isSuccessful()) { // 判断执行结果是否成功！
                System.out.println(re.code());
                System.out.println(re.toString());
            } else {
                if (re.code() == 614) {
                    String message = "创建失败，该空间名已存在！";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                } else if (re.code() == 631) {
                    String message = "创建失败，该空间名不存在！";
                    throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
                } else if (re.code() == 400) {
                    String message = "创建失败，不符合命名规范！";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                } else if (re.code() == 401) {
                    String message = "认证信息有误！";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                } else if (re.code() == 630) {
                    String message = "已创建的空间数量达到上限，无法创建新空间";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return re.code() + ""; // 将状态码返回给用户
    }

    /**
     * 处理删除存储空间的功能
     *
     * @param bucketName 存储空间的名称
     * @return 返回操作的结果状态码
     */
    @Override
    public String dropBucket(String bucketName) {
        log.debug("开始处理删除存储空间：{}的功能", bucketName);
        Auth auth = Auth.create(accessKey, secretKey);// 将AK和SK传入进行认证
        String path = "/drop/" + bucketName + "\n";
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://rs.qiniu.com/drop/" + bucketName;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "QBox " + access_token).build();
        Response re = null;
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
        return re.code() + ""; // 将状态码返回给用户
    }

    /**
     * 处理查询指定存储空间的文件列表信息
     *
     * @param bucketName 存储空间名
     * @return 返回文件列表
     */
    @Override
    public List<Bucket> bucketList(String bucketName) {
        log.debug("开始处理查询存储空间：{}的文件列表", bucketName);
        List<Bucket> list = bucketRedisRepository.list();
        log.debug("结果：{}",list);
        if (! list.isEmpty()){ // 判断List集合是否为空，也可以用list.size()方法
            log.debug("命中缓存，即将返回！");
            return list;
        }
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
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucketName, prefix, limit, delimiter);
        List<Bucket> keyList = new ArrayList<>();
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            FileInfo[] items = fileListIterator.next();
            if (items == null) { // 捕获该异常，放到Spring MVC框架的全局异常处理器"ExceptionHandler"进行响应处理
                String message = "该存储空间不存在！";
                throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
            }
            for (FileInfo item : items) {
                Bucket bucket = new Bucket();
                bucket.setKey(item.key);
                bucket.setHash(item.hash);
                bucket.setSize(item.fsize);
                bucket.setMimeType(item.mimeType);
                bucket.setPutTime(item.putTime);
                bucket.setType(item.type);
                keyList.add(bucket);
            }
        }
        return keyList; // 返回List列表
    }

    /**
     * 设置Bucket公开访问权限
     *
     * @param bucketName 存储空间名
     * @return 返回结果状态码
     */
    @Override
    public String setBucketPublic(String bucketName) {
        return setBucketAuth(bucketName, 0);
    }

    /**
     * 设置Bucket私有访问权限
     *
     * @param bucketName 存储空间名
     * @return 返回结果状态码
     */
    @Override
    public String setBucketPrivate(String bucketName) {
        return setBucketAuth(bucketName, 1);
    }

    /**
     * 给指定空间设置标签
     *
     * @param bucketName 空间名
     * @param key        标签名
     * @param value      标签值
     * @return 返回结果状态码
     */
    @Override
    public String setBucketTags(String bucketName, String key, String value) {
        log.debug("开始处理添加存储空间:{}的标签,key:{};value:{}", bucketName, key, value);
        Auth auth = Auth.create(accessKey, secretKey);// 将AK和SK传入进行认证
        String path = "/bucketTagging?bucket=" + bucketName + "\n";
        log.debug("认证的路径为：" + path);
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://uc.qiniuapi.com/bucketTagging?bucket=" + bucketName;
        OkHttpClient client = new OkHttpClient();
        Tag tag = new Tag(key, value);
        System.out.println("标签对象:" + tag);
        List<Tag> list = new ArrayList<>();
        list.add(tag);
        Body body = new Body();
        body.setTags(list);
        System.out.println(body);
        String s = JSON.toJSONString(body);
        System.out.println("JSON对象:" + s);
        RequestBody requestBody = RequestBody.create(MediaType.parse("json"), s);
        Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "QBox " + access_token).put(requestBody).build();
        okhttp3.Response re = null;
        try {
            re = client.newCall(request).execute();
            if (re.isSuccessful()) { // 判断执行结果是否成功！
                System.out.println(re.code());
                System.out.println(re.toString());
            } else {
                System.out.println("错误代码：" + re.code());
                System.out.println(re.toString());
                if (re.code() == 631) {
                    String message = "修改失败，该空间不存在！";
                    throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return re.code() + "";
    }

    /**
     * 查询指定存储空间的标签数据
     *
     * @param buckName 存储空间名
     * @return 返回List集合
     */
    @Override
    public List<Tag> selectToTags(String buckName) {
        log.debug("开始处理查询存储空间:{}的标签数据", buckName);
        Auth auth = Auth.create(accessKey, secretKey);// 将AK和SK传入进行认证
        String path = "/bucketTagging?bucket=" + buckName + "\n";
        log.debug("认证的路径为：" + path);
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://uc.qiniuapi.com/bucketTagging?bucket=" + buckName;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "QBox " + access_token).build();
        okhttp3.Response re = null;
        List<Tag> resultList = null;
        try {
            re = client.newCall(request).execute();
            if (re.isSuccessful()) { // 判断执行结果是否成功！
                System.out.println(re.code());
                System.out.println(re.toString());
                ResponseBody body = re.body(); // 获取body中的json数据
                if (body == null) {
                    String message = "响应数据为空,请检查操作是否有误!";
                    throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
                }
                String str = body.string();
                if ("{}".equals(str)){
                    String message = "查询失败，该存储空间暂无标签！";
                    log.debug(message);
                    throw new ServiceException(ServiceCode.ERR_NOT_FOUND,message);
                }
                int start = str.indexOf(":") + 1;
                int end = str.lastIndexOf("}");
                String result = str.substring(start, end);
                resultList = JSONObject.parseArray(result, Tag.class);// 将json转为List<Tag>集合
            } else {
                System.out.println("错误代码：" + re.code());
                System.out.println(re.toString());
                if (re.code() == 631) {
                    String message = "修改失败，该空间不存在！";
                    throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * 开始处理删除空间标签的功能
     * @param bucketName 空间名
     * @return 返回结果状态码
     */
    @Override
    public String deleteToTags(String bucketName) {
        log.debug("开始处理删除空间:{}的标签", bucketName);
        Auth auth = Auth.create(accessKey, secretKey);// 将AK和SK传入进行认证
        String path = "/bucketTagging?bucket=" + bucketName + "\n";
        log.debug("认证的路径为：" + path);
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://uc.qiniuapi.com/bucketTagging?bucket=" + bucketName;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "QBox " + access_token).delete().build();
        okhttp3.Response re = null;
        try {
            re = client.newCall(request).execute();
            if (re.isSuccessful()) { // 判断执行结果是否成功！
                System.out.println(re.code());
                System.out.println(re.toString());
            } else {
                System.out.println("错误代码：" + re.code());
                System.out.println(re.toString());
                if (re.code() == 631) {
                    String message = "修改失败，该空间不存在！";
                    throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return re.code() + "";
    }

    /**
     * 处理设置存储空间访问权限的逻辑
     *
     * @param bucketName 存储空间的名称
     * @param authId     权限Id
     * @return 返回结果状态码
     */
    private String setBucketAuth(String bucketName, Integer authId) {
        String[] tips = {"公开", "私有"};
        log.debug("您要设置Bucket空间名：{}的存储权限为：{}", bucketName, tips[authId]);
        Auth auth = Auth.create(accessKey, secretKey);// 将AK和SK传入进行认证
        String path = "/private?bucket=" + bucketName + "&private=" + authId + "\n";
        log.debug("请求的路径为：" + path);
        String access_token = auth.sign(path);
        System.out.println(access_token);
        String url = "http://uc.qiniuapi.com/private?bucket=" + bucketName + "&private=" + authId;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "QBox " + access_token).build();
        Response re = null;
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
}
