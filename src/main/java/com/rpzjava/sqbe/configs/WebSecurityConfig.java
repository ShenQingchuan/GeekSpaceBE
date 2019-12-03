package com.rpzjava.sqbe.configs;

import com.rpzjava.sqbe.utils.RedisUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录拦截配置
 */
@Configuration
public class WebSecurityConfig extends WebMvcConfigurationSupport {

    private final RedisUtils redisUtils;

    public WebSecurityConfig(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    /**
     * 注入Bean 让 Spring 扫描 SecurityInterceptor
     * 不然过滤器不起作用
     */
    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    /**
     * 配置自定义拦截拦截器
     */
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());
        List<String> list = new ArrayList<>();
        list.add("/user/"); // 放行新增用户接口地址
        list.add("/login"); // 放行登陆接口地址
        addInterceptor.excludePathPatterns(list);
        addInterceptor.addPathPatterns("/**");//拦截所有请求
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {
        /**
         * 在业务处理器处理请求之前被调用
         */
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            ServletOutputStream out = response.getOutputStream();//创建一个输出流
            OutputStreamWriter ow = new OutputStreamWriter(out, StandardCharsets.UTF_8);//设置编码格式,防止汉字乱码
            String token = request.getHeader("token");//获取 Token
            if (token != null) {//判断 Token 是否为空
                if (redisUtils.hasKey(token)) {//判断 Token 是否存在
                    redisUtils.expire(token, 60); //如果 Token 存在 重新赋予过期时间 并放行
                    return true;
                }
                ow.write("token错误，请重新登录");//要返回的信息
                ow.flush();//冲刷出流，将所有缓冲的数据发送到目的地
                ow.close();//关闭流
                return false;//拦截
            }
            ow.write("token为空，请重新登录");//要返回的信息
            ow.flush();//冲刷出流，将所有缓冲的数据发送到目的地
            ow.close();//关闭流
            return false;//拦截
        }
    }

}