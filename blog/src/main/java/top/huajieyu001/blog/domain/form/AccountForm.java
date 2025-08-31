package top.huajieyu001.blog.domain.form;

import lombok.Data;

/**
 * @Author huajieyu
 * @Date 5/3/2025 3:17 PM
 * @Version 1.0
 * @Description TODO
 */
@Data
public class AccountForm {
    private String username;
    private String password;
    private String email;
    private String captcha;
}
