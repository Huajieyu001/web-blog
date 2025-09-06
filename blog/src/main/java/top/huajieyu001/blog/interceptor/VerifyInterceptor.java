package top.huajieyu001.blog.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.huajieyu001.blog.constant.AccountConstant;
import top.huajieyu001.blog.constant.RedisConstant;
import top.huajieyu001.blog.domain.Account;
import top.huajieyu001.blog.domain.TokenPojo;
import top.huajieyu001.blog.holder.AccountHolder;
import top.huajieyu001.blog.util.EncryptUtils;
import top.huajieyu001.blog.util.VerifyUtils;

import java.io.IOException;
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

        System.out.println("请求路径=========== " + request.getRequestURI());

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authorization = request.getHeader("Authorization");

        if(authorization == null){
            setUnauthorizedResponse(response, "认证失败");
            return false;
        }

        if(Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(RedisConstant.REDIS_KEY_TOKEN_BLACK_LIST, authorization))){
            setUnauthorizedResponse(response, "认证失败");
            return false;
        }

        JWTVerifier build = JWT.require(Algorithm.HMAC256("wenzhenhao-2000")).build();
        try{
            DecodedJWT verified = build.verify(authorization);

            String accountString = verified.getClaim("account").asString();
            Account readAccount = new ObjectMapper().readValue(accountString, Account.class);

            // 判断是否解析成功，如果username或者email是null则代表获取参数失败，信息不完整
            if(readAccount.getUsername() == null || readAccount.getEmail() == null){
                setUnauthorizedResponse(response, "认证失败");
                return false;
            }
            // 判断当前用户是否是已注册用户
            Boolean member = stringRedisTemplate.opsForSet().isMember(RedisConstant.REDIS_KEY_ACCOUNT_USED_USERNAME, readAccount.getEmail());
            if(Boolean.FALSE.equals(member)){
                setUnauthorizedResponse(response, "认证失败");
                return false;
            }

            // 全部校验通过，此时可以把用户信息存起来
            TokenPojo tokenPojo = new TokenPojo();
            BeanUtils.copyProperties(readAccount, tokenPojo);
            tokenPojo.setToken(authorization);
            AccountHolder.setAccount(tokenPojo);

            return true;
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            setUnauthorizedResponse(response, "无效签名");
            return false;
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            setUnauthorizedResponse(response, "令牌已过期");
            return false;
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            setUnauthorizedResponse(response, "认证失败");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            setUnauthorizedResponse(response, "认证失败");
            return false;
        }
    }

    private void refreshToken(){
        stringRedisTemplate.opsForValue().get(RedisConstant.REDIS_KEY_ACCOUNT_ACTIVE_STATE);
    }

    private void setUnauthorizedResponse(HttpServletResponse resp, String message){
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
        resp.setCharacterEncoding("UTF-8");

        Map<String,Object> map = new HashMap<>();
        map.put("message", message);

        try {
            resp.getWriter().write(new ObjectMapper().writeValueAsString(map));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
