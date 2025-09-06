package top.huajieyu001.blog.service;

import top.huajieyu001.blog.domain.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import top.huajieyu001.blog.domain.form.AccountForm;
import top.huajieyu001.blog.domain.form.UpdatePwdForm;
import top.huajieyu001.blog.result.AjaxResult;
import top.huajieyu001.blog.result.BlogResult;

/**
* @author xanadu
* @description 针对表【account】的数据库操作Service
* @createDate 2025-05-02 19:45:54
*/
public interface AccountService extends IService<Account> {

    AjaxResult signup(AccountForm accountForm);

    AjaxResult getVerifyCode(String email);

    AjaxResult login(String username, String password);

    AjaxResult changePassword(UpdatePwdForm form);
}
