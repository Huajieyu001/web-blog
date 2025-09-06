package top.huajieyu001.blog.constant;

/**
 * @Author huajieyu
 * @Date 5/3/2025 2:39 PM
 * @Version 1.0
 * @Description 用户相关常量
 */
public class AccountConstant {
    /**
     * 默认创建者
     */
    public static final Long ACCOUNT_DEFAULT_CREATE_BY = 1L;

    /**
     * 新用户默认权限
     */
    public static final int ACCOUNT_DEFAULT_PERMISSIONS = 2;

    /**
     * 新用户默认是否删除
     */
    public static final int ACCOUNT_DEFAULT_IS_DELETED = 0;

    /**
     * 重试密码次数
     */
    public static final int ACCOUNT_DEFAULT_PASSWORD_VERIFY_COUNT = 5;

    /**
     * 字符串"Bearer "
     */
    public static final String BEARER = "Bearer ";
}
