package top.huajieyu001.blog.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.huajieyu001.blog.domain.Account;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * @Author huajieyu
 * @Date 5/2/2025 8:13 PM
 * @Version 1.0
 * @Description TODO
 */
public class EncryptUtils {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    public static final String SIGNATURE = "wenzhenhao-2000";

    /**
     * 对文本进行不可逆加密
     * @param plainText 需要加密的文本
     * @return 加密后的文本
     */
    public static String encrypt(String plainText) {
        return ENCODER.encode(plainText);
    }

    /**
     * 验证密码是否正确
     * @param password 密码
     * @param encryptText 加密密文
     * @return 验证结果
     */
    public static boolean verifyPassword(String password, String encryptText) {
        return ENCODER.matches(password, encryptText);
    }

    /**
     * 生成6位数验证码
     * @return 验证码
     */
    public static String createCode(){
        return String.format("%06d", RANDOM.nextInt(1000000));
    }

    /**
     * 生成JWT令牌token
     * @param account 生产令牌的用户
     * @param signature 签名
     * @return 令牌
     */
    public static String createToken(Account account, String signature) {
        HashMap<String, Object> map = new HashMap<>();
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.HOUR, 12);

        try {
            return JWT.create()
                    .withHeader(map)
                    .withClaim("account", new ObjectMapper().writeValueAsString(account))
                    .withExpiresAt(instance.getTime())
                    .sign(Algorithm.HMAC256(SIGNATURE));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据签名解析JWT令牌token
     * @param token
     * @param signature 签名
     * @return 解析后的用户
     */
    public static Account resolveToken(String token, String signature) {
        JWTVerifier build = JWT.require(Algorithm.HMAC256(SIGNATURE)).build();
        Date expiresAt = build.verify(token).getExpiresAt();
        if (expiresAt != null) {
            // 校验是否过期，如果令牌过期，则直接返回null
            if (expiresAt.before(Calendar.getInstance().getTime())) {
                return null;
            }
        }
        String username = build.verify(token).getClaim("username").asString();
        String email = build.verify(token).getClaim("email").asString();
        Account account = new Account();
        account.setUsername(username);
        account.setEmail(email);
        return account;
    }
}
