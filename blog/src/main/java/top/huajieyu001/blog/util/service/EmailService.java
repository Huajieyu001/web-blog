package top.huajieyu001.blog.util.service;

/**
 * @Author huajieyu
 * @Date 5/5/2025 7:43 PM
 * @Version 1.0
 * @Description TODO
 */
public interface EmailService {

    void sendVerificationCode(String target, String code);
}
