package top.huajieyu001.blog.util;

import top.huajieyu001.blog.constant.MatchConstant;
import top.huajieyu001.blog.result.AjaxResult;

/**
 * @Author huajieyu
 * @Date 5/2/2025 9:22 PM
 * @Version 1.0
 * @Description TODO
 */
public class VerifyUtils {

    public static AjaxResult verifyUsername(String username) {
        if (username.length() < 6 || username.length() > 20) {
            return AjaxResult.error("用户名需要在6-20位之间");
        }
        if (!username.matches(MatchConstant.USERNAME_REGEX)) {
            return AjaxResult.error("用户名格式有误，请输入6-20位的密码，包含任意数字和大小写字母即可");
        }
        return AjaxResult.success();
    }

    public static AjaxResult verifyPassword(String password) {
        if (password.length() < 6 || password.length() > 20) {
            return AjaxResult.error("密码需要在6-20位之间");
        }
        if (!password.matches(MatchConstant.PASSWORD_REGEX)) {
            return AjaxResult.error("密码格式有误，请输入6-20位的密码，包含任意数字和大小写字母即可");
        }
        return AjaxResult.success();
    }

    public static AjaxResult verifyEmail(String email) {
        if (email == null || email.length() == 0 || email.matches("")) {
            return AjaxResult.error("邮箱格式有误");
        }
        return AjaxResult.success();
    }

//    public static AjaxResult verifyPhone(String phone) {}
}
