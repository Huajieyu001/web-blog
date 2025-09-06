package top.huajieyu001.blog.domain.form;

import lombok.Data;

/**
 * @Author huajieyu
 * @Date 2025/9/6 14:12
 * @Version 1.0
 * @Description TODO
 */
@Data
public class UpdatePwdForm {

    private String oldPassword;

    private String newPassword;
}
