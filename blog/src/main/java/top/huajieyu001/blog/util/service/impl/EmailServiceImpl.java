package top.huajieyu001.blog.util.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import top.huajieyu001.blog.result.AjaxResult;
import top.huajieyu001.blog.util.VerifyUtils;
import top.huajieyu001.blog.util.service.EmailService;

/**
 * @Author huajieyu
 * @Date 5/5/2025 7:43 PM
 * @Version 1.0
 * @Description TODO
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String myEmail;

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationCode(String target, String code) {
        AjaxResult ajaxResult = VerifyUtils.verifyEmail(target);
        if(ajaxResult.getCode() != 200){
            throw new RuntimeException("目标邮箱格式错误");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(myEmail);
        message.setTo(target);
        message.setSubject("【个人网站】邮箱验证码");
        message.setText("您的验证码是：" + code + "，有效期为5分钟。");

        mailSender.send(message);
    }
}
