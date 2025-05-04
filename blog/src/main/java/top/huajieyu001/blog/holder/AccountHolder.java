package top.huajieyu001.blog.holder;

import top.huajieyu001.blog.domain.Account;

/**
 * @Author huajieyu
 * @Date 5/2/2025 9:15 PM
 * @Version 1.0
 * @Description TODO
 */
public class AccountHolder {

    public static final ThreadLocal<Account> tl = new ThreadLocal<Account>();

    public static Account getAccount() {
        return tl.get();
    }

    public static void setAccount(Account account) {
        tl.set(account);
    }
}
