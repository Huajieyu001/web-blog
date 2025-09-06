package top.huajieyu001.blog.holder;

import top.huajieyu001.blog.domain.TokenPojo;

/**
 * @Author huajieyu
 * @Date 5/2/2025 9:15 PM
 * @Version 1.0
 * @Description TODO
 */
public class AccountHolder {

    public static final ThreadLocal<TokenPojo> tl = new ThreadLocal<TokenPojo>();

    public static TokenPojo getAccount() {
        return tl.get();
    }

    public static void setAccount(TokenPojo account) {
        tl.set(account);
    }

    public static boolean verifyPermissions(Long id) {
        if (tl.get() == null || id == null) {
            return false;
        }

        if (tl.get().getId().equals(id)) {
            return true;
        }

        return false;
    }

    public static boolean isAdmin() {
        if (tl.get() == null) {
            return false;
        }

        return tl.get().getPermissions() == 1;
    }
}
