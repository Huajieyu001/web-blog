package top.huajieyu001.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.huajieyu001.blog.constant.AccountConstant;
import top.huajieyu001.blog.constant.RedisConstant;
import top.huajieyu001.blog.domain.Account;
import top.huajieyu001.blog.domain.form.AccountForm;
import top.huajieyu001.blog.mapper.AccountMapper;
import top.huajieyu001.blog.result.AjaxResult;
import top.huajieyu001.blog.result.AjaxResult;
import top.huajieyu001.blog.service.AccountService;
import top.huajieyu001.blog.util.EncryptUtils;
import top.huajieyu001.blog.util.VerifyUtils;

import java.time.LocalDateTime;
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

    @Override
    public AjaxResult getVerifyCode(String email) {
        String redisEmail = stringRedisTemplate.opsForValue().get(RedisConstant.REDIS_KEY_ACCOUNT_EMAIL + email);
        if (redisEmail != null) {
            return AjaxResult.error("您输入的邮箱已经注册！");
        }

        stringRedisTemplate.opsForValue().set(RedisConstant.REDIS_KEY_ACCOUNT_CODE + email, email);
        stringRedisTemplate.opsForValue().set(RedisConstant.REDIS_KEY_ACCOUNT_CODE + email, EncryptUtils.createCode());
        stringRedisTemplate.expire(RedisConstant.REDIS_KEY_ACCOUNT_CODE + email, 5, TimeUnit.MINUTES);

        // TODO 发送验证码的邮件给用户邮箱

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

        // 根据邮箱获取验证码
        String code = stringRedisTemplate.opsForValue().get(RedisConstant.REDIS_KEY_ACCOUNT_CODE + accountForm.getEmail());
        // 验证验证码是否正确
        if (code == null || code.isEmpty()) {
            return AjaxResult.error("验证码已过期，请重新获取");
        }
        if (!code.equals(accountForm.getVerificationCode())) {
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

        String accountKey = RedisConstant.REDIS_KEY_ACCOUNT_INFO + accountForm.getEmail();

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
        Map<String, Object> accountMap = BeanUtil.beanToMap(account);
        stringRedisTemplate.opsForHash().putAll(accountKey + accountForm.getUsername(), accountMap);
        // 记录username，下一个人注册的时候先查里面是否有重复username
        stringRedisTemplate.opsForSet().add(RedisConstant.REDIS_KEY_ACCOUNT_USED_USERNAME, accountForm.getUsername());
        return AjaxResult.success();
    }

    @Override
    public AjaxResult login(String username, String password) {

        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(RedisConstant.REDIS_KEY_ACCOUNT_INFO + username);
//        BeanU
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
                stringRedisTemplate.opsForValue().set(RedisConstant.REDIS_KEY_ACCOUNT_VERIFY_LIMIT + email, "4");
                return AjaxResult.error("密码错误，请重试，您还有" + 4 + "次机会，如全部输错则只能通过邮件重置！");
            }
            int nowCount = Integer.parseInt(verifiedCount);
            if (nowCount > 0) {
                stringRedisTemplate.opsForValue().decrement(RedisConstant.REDIS_KEY_ACCOUNT_VERIFY_LIMIT + email);
                return AjaxResult.error("密码错误，请重试，您还有" + (nowCount - 1) + "次机会，如全部输错则只能通过邮件重置！");
            } else {
                stringRedisTemplate.opsForValue().set(RedisConstant.REDIS_KEY_ACCOUNT_DENY_LOGIN + email, "1");
                return AjaxResult.error("密码错误，重试太多，请通过邮件重置密码！");
            }
        }
        // 通过验证，如果有重试次数限制，则把限制删除
        stringRedisTemplate.delete(RedisConstant.REDIS_KEY_ACCOUNT_VERIFY_LIMIT + email);

        // TODO 需要再返回jwt令牌
        return AjaxResult.successAndSetMsg("登录成功");
    }
}




