package cn.tedu.authuploadsystem.config;

import cn.tedu.authuploadsystem.filter.JwtAuthorizationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 创建Spring Security的配置类
 * 配置放行路径,登录页面,认证授权,禁用"防止伪造跨域攻击的机制",通过预检机制
 *
 * @author java.@Wqy
 * @version 0.0.1
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)// 启用方法级别的权限检查!
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // 将自己写的JWT过滤器装配进来
    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    public SecurityConfiguration(){
        log.debug("创建配置类对象:SecurityConfiguration");
    }

    /**
     * 配置密码编译器,否则无法验证密码是否正确,服务端会报错
     * @return 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();// 如果使用BCrypt编码器,则UserDetails中的Password密码必须是BCrypt加密的密码
    }

    /**
     * 重写认证信息接口
     * @return AuthenticationManager
     * @throws Exception Exception
     */
    @Bean// 该注解便于Spring框架进行管理,自动调用,放入容器,利于自动装配
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 重写认证的配置方法,重写前只有super.configure(http)---[所有请求都要被认证],对此应根据自身需求去改变
     * @param http http参数
     * @throws Exception 抛出异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 如果不调用父类方法,默认所有请求都不需要通过认证,可以直接访问
       // super.configure(http);

        // 白名单路径 这些路径不需要登录即可访问(但可能会有未放行的文件不能显示)---白名单外403
        String[] urls = {
                "/favicon.ico",
                "/doc.html",
                "/**/*.js",
                "/**/*.css",
                "/swagger-resources",
                "/v2/api-docs",
                "/users/insert",
                "/users/login",
        };

        // ★客户端请求时携带了请求头,称为复杂请求OPTIONS,该请求会经过预检机制(PreFlight),解决方案目前有两种
        // 1.该方法会自动启用一个CorsFilter,这是Spring Security内置专门用于处理跨域问题的过滤器,也会对OPTIONS请求放行,效果完全相同!!
        http.cors();

        // 将"防止伪造跨域攻击的机制"禁用(如果不添加该配置,Post请求会403---浏览器的安全措施)
        http.csrf().disable();

        // 添加这些方法,可以手动匹配进行随机认证-----链式写法
        http.authorizeRequests() // 管理请求授权
                .mvcMatchers(urls) // 可匹配的路径
                .permitAll() // 直接许可,即可不需要通过认证即可访问
                .anyRequest() // 除了以上配置过的以外的其他所有请求
                .authenticated(); // 要求是"已经通过认证的"

        // 将JWT过滤器添加到Spring Security框架的过滤器链中
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        // 启用登录 :
//        http.formLogin();
    }
}
