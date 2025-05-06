package top.huajieyu001.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.huajieyu001.blog.interceptor.VerifyInterceptor;

/**
 * @Author huajieyu
 * @Date 5/5/2025 10:37 PM
 * @Version 1.0
 * @Description TODO
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置拦截器
        registry.addInterceptor(new VerifyInterceptor(stringRedisTemplate))
                .addPathPatterns("/**")
                .excludePathPatterns("/account/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
