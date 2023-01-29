package cn.tedu.authuploadsystem.schdule;

import cn.tedu.authuploadsystem.mapper.RoleMapper;
import cn.tedu.authuploadsystem.service.IBucketService;
import cn.tedu.authuploadsystem.service.IRoleService;
import cn.tedu.authuploadsystem.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 策略二：利用执行计划来完成缓存的加载，计划重建缓存时间
 *
 * @author java@Wqy
 * @version 0.0.1
 * @since 2023.1.28
 */
@Slf4j
@Component // 声明一个组件类
public class CacheSchedule {

    // 注入用户持久层接口
    @Autowired
    private IUserService userService;

    // 注入角色持久层接口
    @Autowired
    private IRoleService roleService;

    public CacheSchedule() {
        log.debug("创建计划任务对象:CacheSchedule");
    }

    // 关于@Schedule注解的参数配置
    // fixedRate：执行频率，将按照上一次开始执行的时间来计算下一次的执行时间，以毫秒值为单位
    // fixedDelay：执行间隔时间，即上次执行结束后再过多久执行下一次，以毫秒值为单位
    // cron：使用1个字符串，其中包括6~7个值，各值之间使用1个空格进行分隔
    // >> 在cron的字符串中各值依次表示：秒 分 时 日 月 周（星期） [年]
    // >> 以上各值都可以使用通配符
    // >> 使用星号（*）表示任意值
    // >> 使用问号（?）表示不关心具体值，问号只能用于“日”和“周（星期）”
    // >> 例如："56 34 12 15 11 ? 2022"表示“2022年11月15日12:34:56，无视当天星期几”
    // >> 以上各值，可以使用“x/x”格式的值，例如：在分钟对应的位置设置“1/5”，则表示当分钟值为1时执行，且每间隔5分钟执行1次
    @Scheduled(fixedRate = 2 * 24 * 60 * 60 * 1000) // 2days
    public void rebuildCache() {

        log.debug("开始执行【重建用户缓存】计划任务…………");
        userService.rebuildCache();// 调用BrandService中重新加载Redis缓存的方法
        log.debug("本次【重建用户缓存】计划任务执行完成！");

        log.debug("开始执行【重建角色缓存】计划任务…………");
        roleService.rebuildCache();// 调用BrandService中重新加载Redis缓存的方法
        log.debug("本次【重建角色缓存】计划任务执行完成！");
    }


}
