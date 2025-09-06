//package top.huajieyu001.blog.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//
///**
// * @Author huajieyu
// * @Date 5/3/2025 7:14 PM
// * @Version 1.0
// * @Description TODO
// */
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/account/login").permitAll() // 必须配置
//                .anyRequest().authenticated();
//    }
//}
