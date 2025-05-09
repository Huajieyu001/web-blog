package top.huajieyu001.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.huajieyu001.blog.domain.form.AccountForm;
import top.huajieyu001.blog.result.AjaxResult;
import top.huajieyu001.blog.result.BlogResult;
import top.huajieyu001.blog.service.AccountService;

/**
 * @Author huajieyu
 * @Date 5/2/2025 7:48 PM
 * @Version 1.0
 * @Description TODO
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/code")
    public AjaxResult getVerifyCode(String email) {
        return accountService.getVerifyCode(email);
    }

    @PostMapping("/signup")
    public AjaxResult signup(@RequestBody AccountForm accountForm) {
        return accountService.signup(accountForm);
    }

    @PostMapping("/login")
    public AjaxResult login(String username, String password) {
        return accountService.login(username, password);
    }

//    @GetMapping
//    public String logout() {
//        return accountService.logout();
//    }
}
