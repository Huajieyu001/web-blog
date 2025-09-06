package top.huajieyu001.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import top.huajieyu001.blog.constant.AccountConstant;
import top.huajieyu001.blog.constant.RedisConstant;
import top.huajieyu001.blog.domain.Account;
import top.huajieyu001.blog.domain.TokenPojo;
import top.huajieyu001.blog.domain.form.AccountForm;
import top.huajieyu001.blog.domain.form.UpdatePwdForm;
import top.huajieyu001.blog.holder.AccountHolder;
import top.huajieyu001.blog.mapper.AccountMapper;
import top.huajieyu001.blog.result.AjaxResult;
import top.huajieyu001.blog.result.AjaxResult;
import top.huajieyu001.blog.service.AccountService;
import top.huajieyu001.blog.util.EncryptUtils;
import top.huajieyu001.blog.util.VerifyUtils;
import top.huajieyu001.blog.util.service.EmailService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author xanadu
 * @description 针对表【account】的数据库操作Service实现
 * @createDate 2025-05-02 19:45:54
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
        implements AccountService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private EmailService emailService;

    @Override
    public AjaxResult getVerifyCode(String email) {
        String redisEmail = stringRedisTemplate.opsForValue().get(RedisConstant.REDIS_KEY_ACCOUNT_EMAIL + email);
        if (redisEmail != null) {
            return AjaxResult.error("您输入的邮箱已经注册！");
        }

        String exists = stringRedisTemplate.opsForValue().get(RedisConstant.REDIS_KEY_ACCOUNT_CODE_LIMIT + email);
        if(exists != null) {
            return AjaxResult.error("您获取验证码的操作过于频繁，请稍后重试（两次获取的时间间隔不小于一分钟）");
        }
        String code = EncryptUtils.createCode();

        stringRedisTemplate.opsForValue().set(RedisConstant.REDIS_KEY_ACCOUNT_CODE_LIMIT + email, "1", 1, TimeUnit.MINUTES);
        stringRedisTemplate.opsForValue().set(RedisConstant.REDIS_KEY_ACCOUNT_CODE + email, code);
        stringRedisTemplate.expire(RedisConstant.REDIS_KEY_ACCOUNT_CODE + email, 5, TimeUnit.MINUTES);

        emailService.sendVerificationCode(email, code);

        return AjaxResult.successAndSetMsg("验证码已发送到您的邮箱，请注意查收，有效时间为5分钟");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult signup(AccountForm accountForm) {
        // 验证邮箱格式和是否注册过
        AjaxResult emailVerificationResult = VerifyUtils.verifyEmail(accountForm.getEmail());
        if (emailVerificationResult.getCode() != 200) {
            return emailVerificationResult;
        }

        String accountKey = RedisConstant.REDIS_KEY_ACCOUNT_INFO + accountForm.getEmail();

        // 获取用户，如果获取到说明当前邮箱已经注册过，中断后续处理
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(accountKey);
        if(!entries.isEmpty()) {
            return AjaxResult.error("您的邮箱已有绑定账号");
        }
        // 根据邮箱获取验证码
        String code = stringRedisTemplate.opsForValue().get(RedisConstant.REDIS_KEY_ACCOUNT_CODE + accountForm.getEmail());
        // 验证验证码是否正确
        if (code == null || code.isEmpty()) {
            return AjaxResult.error("您未获取验证码或者验证码已过期，请重新获取");
        }
        if (!code.equals(accountForm.getCaptcha())) {
            return AjaxResult.error("验证码有误，请重新输入");
        }

        // 验证用户名格式
        AjaxResult result = VerifyUtils.verifyUsername(accountForm.getUsername());
        if (result.getCode() != 200) {
            return result;
        }
        // 验证用户名格式
        AjaxResult passwordVerificationResult = VerifyUtils.verifyPassword(accountForm.getPassword());
        if (passwordVerificationResult.getCode() != 200) {
            return passwordVerificationResult;
        }

        // 查用户名是否在redis中存在，存在说明被别人注册过了
        Boolean usernameUsed = stringRedisTemplate.opsForSet().isMember(RedisConstant.REDIS_KEY_ACCOUNT_USED_USERNAME, accountForm.getUsername());
        if (usernameUsed != null && usernameUsed) {
            return AjaxResult.error("用户名已存在，请重试");
        }
        // 如果redis中不存在，那么查数据库看看是否存在
        // 查用户名是否在mysql中存在，存在说明被别人注册过了
        boolean exists = lambdaQuery().eq(Account::getUsername, accountForm.getUsername()).exists();

        if (exists) {
            return AjaxResult.error("用户名已存在，请重试");
        }

        // 对密码进行加密，存储时使用密文
        String encrypt = EncryptUtils.encrypt(accountForm.getPassword());

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 创建用户
        Account account = new Account(null, UUID.randomUUID().toString().substring(0, 10), accountForm.getUsername(), encrypt, accountForm.getEmail(),
                AccountConstant.ACCOUNT_DEFAULT_PERMISSIONS, AccountConstant.ACCOUNT_DEFAULT_CREATE_BY,
                now, null, now, AccountConstant.ACCOUNT_DEFAULT_IS_DELETED);

        // 把新用户的信息保存到数据库中
        boolean save = save(account);
        if (!save) {
            return AjaxResult.error("创建用户失败，请重试");
        }

        // 把新用户的信息保存到redis
        Map<String, Object> accountMap = BeanUtil.beanToMap(account, new HashMap<>(),
                CopyOptions.create().setIgnoreNullValue(true).setFieldValueEditor((field, value) -> value == null ? null : value.toString()));
        stringRedisTemplate.opsForHash().putAll(accountKey, accountMap);
        // 记录username，下一个人注册的时候先查里面是否有重复username
        stringRedisTemplate.opsForSet().add(RedisConstant.REDIS_KEY_ACCOUNT_USED_USERNAME, accountForm.getUsername());
        return AjaxResult.success();
    }

    @Override
    public AjaxResult login(String username, String password) {
        Account account = lambdaQuery().eq(Account::getUsername, username).one();
        if (account == null) {
            return AjaxResult.error("用户不存在");
        }
        String email = account.getEmail();
        String denyLogin = stringRedisTemplate.opsForValue().get(RedisConstant.REDIS_KEY_ACCOUNT_DENY_LOGIN + email);
        if(denyLogin != null) {
            return AjaxResult.error("密码错误，重试太多，请通过邮件重置密码！");
        }

        if (!EncryptUtils.verifyPassword(password, account.getPassword())) {
            String verifiedCount = stringRedisTemplate.opsForValue().get(RedisConstant.REDIS_KEY_ACCOUNT_VERIFY_LIMIT + email);
            if (verifiedCount == null || verifiedCount.isEmpty()) {
                stringRedisTemplate.opsForValue().set(RedisConstant.REDIS_KEY_ACCOUNT_VERIFY_LIMIT + email, String.valueOf(AccountConstant.ACCOUNT_DEFAULT_PASSWORD_VERIFY_COUNT - 1));
                return AjaxResult.error("密码错误，请重试，您还有" + AccountConstant.ACCOUNT_DEFAULT_PASSWORD_VERIFY_COUNT + "次机会，如全部输错则只能通过邮件重置！");
            }
            int nowCount = Integer.parseInt(verifiedCount);
            if (nowCount > 0) {
                stringRedisTemplate.opsForValue().decrement(RedisConstant.REDIS_KEY_ACCOUNT_VERIFY_LIMIT + email);
                return AjaxResult.error("密码错误，请重试，您还有" + nowCount + "次机会，如全部输错则只能通过邮件重置！");
            } else {
                stringRedisTemplate.opsForValue().set(RedisConstant.REDIS_KEY_ACCOUNT_DENY_LOGIN + email, "1");
                return AjaxResult.error("密码错误，重试太多，请通过邮件重置密码！");
            }
        }
        // 通过验证，如果有重试次数限制，则把限制删除
        stringRedisTemplate.delete(RedisConstant.REDIS_KEY_ACCOUNT_VERIFY_LIMIT + email);

        // 生成JWT令牌
        String token = EncryptUtils.createToken(account, "");
        return AjaxResult.success(token);
    }

    @Override
    @Transactional
    public AjaxResult changePassword(UpdatePwdForm form) {
        TokenPojo account = AccountHolder.getAccount();
        if(account == null) {
            return AjaxResult.error("用户信息不存在");
        }

        LambdaQueryChainWrapper<Account> wrapper = this.lambdaQuery();
        wrapper.eq(Account::getUsername, account.getUsername());

        Account getAccount = wrapper.one();
        if(getAccount == null) {
            return AjaxResult.error("未查询到该用户");
        }

        if(!EncryptUtils.verifyPassword(form.getOldPassword(), getAccount.getPassword())) {
            return AjaxResult.error("密码不正确");
        }

        LambdaUpdateChainWrapper<Account> updateWrapper = this.lambdaUpdate();
        updateWrapper.set(Account::getPassword, EncryptUtils.encrypt(form.getNewPassword()));
        updateWrapper.eq(Account::getUsername, account.getUsername());

        if(updateWrapper.update()){
            stringRedisTemplate.opsForSet().add(RedisConstant.REDIS_KEY_TOKEN_BLACK_LIST, account.getToken());
        }
        return AjaxResult.success();
    }
}




