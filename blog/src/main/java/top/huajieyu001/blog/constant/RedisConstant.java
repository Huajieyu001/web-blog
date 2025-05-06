package top.huajieyu001.blog.constant;

/**
 * @Author huajieyu
 * @Date 5/2/2025 8:03 PM
 * @Version 1.0
 * @Description TODO
 */
public class RedisConstant {

    public static final String REDIS_KEY_ACCOUNT_EMAIL = "account:email:";

    public static final String REDIS_KEY_ACCOUNT_CODE = "account:code:";

    public static final String REDIS_KEY_ACCOUNT_CODE_LIMIT = "account:code:limit:";

    public static final String REDIS_KEY_ACCOUNT_INFO = "account:info:";

    public static final String REDIS_KEY_ACCOUNT_INFO_USERNAME = "username";

    public static final String REDIS_KEY_ACCOUNT_INFO_PASSWORD = "password";

    public static final String REDIS_KEY_ACCOUNT_INFO_EMAIL = "email";

    public static final String REDIS_KEY_ACCOUNT_VERIFY_LIMIT = "account:verify:limit:";

    public static final String REDIS_KEY_ACCOUNT_DENY_LOGIN = "account:deny:login:";

    /**
     * 作为set的key，记录所有用户名
     */
    public static final String REDIS_KEY_ACCOUNT_USED_USERNAME = "account:used:username";

    /**
     * 作为value的key前缀，后缀需要email，用于记录某个邮件key对应的账户
     */
    public static final String REDIS_KEY_ACCOUNT_EMAIL_USERNAME = "account:email:username:";

    public static final String REDIS_KEY_ACCOUNT_ACTIVE_STATE = "account:active:state:";

    public static final String REDIS_KEY_ACCOUNT_USING_TOKEN = "account:using:token:";

}
