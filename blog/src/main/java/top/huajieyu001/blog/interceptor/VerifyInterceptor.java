package top.huajieyu001.blog.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.huajieyu001.blog.constant.AccountConstant;
import top.huajieyu001.blog.constant.RedisConstant;
import top.huajieyu001.blog.domain.Account;
import top.huajieyu001.blog.holder.AccountHolder;
import top.huajieyu001.blog.util.EncryptUtils;
import top.huajieyu001.blog.util.VerifyUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author huajieyu
 * @Date 5/2/2025 7:49 PM
 * @Version 1.0
 * @Description TODO
 */
public class VerifyInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate stringRedisTemplate;

    public VerifyInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authorization = request.getHeader("Authorization");

        JWTVerifier build = JWT.require(Algorithm.HMAC256("wenzhenhao-2000")).build();
        try{
            build.verify(authorization);

            String username = build.verify(authorization).getClaim("username").asString();
            String email = build.verify(authorization).getClaim("email").asString();

            // 判断是否解析成功，如果username或者email是null则代表获取参数失败，信息不完整
            if(username == null || email == null){
                return false;
            }
            // 判断当前用户是否是已注册用户
            Boolean member = stringRedisTemplate.opsForSet().isMember(RedisConstant.REDIS_KEY_ACCOUNT_USED_USERNAME, username);
            if(Boolean.FALSE.equals(member)){
                return false;
            }

            // 全部校验通过，此时可以把用户信息存起来
            Account account = new Account();
            account.setUsername(username);
            account.setEmail(email);
            AccountHolder.setAccount(account);

            stringRedisTemplate.opsForValue().set(RedisConstant.REDIS_KEY_ACCOUNT_USED_USERNAME, username);
            // 下面的逻辑会生成无数个30分钟内可访问的token，弃用
//            Date expiresAt = build.verify(authorization).getExpiresAt();
//            if(expiresAt != null){
//                long time = expiresAt.getTime();
//                // 如果过期时间小于30分钟，此时有接口被访问，则刷新一下token
//                if(time - System.currentTimeMillis() < 1000 * 60 * 30){
//                    String token = EncryptUtils.createToken(account, "");
//                    response.setHeader("RefreshToken", token);
//                }
//            }
            return true;
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            response.getWriter().write("无效签名");
            return false;
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            response.getWriter().write("令牌已过期");
            return false;
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            response.getWriter().write("算法匹配失败");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("校验失败");
            return false;
        }
    }

    private void refreshToken(){
        stringRedisTemplate.opsForValue().get(RedisConstant.REDIS_KEY_ACCOUNT_ACTIVE_STATE);
    }
}
