package top.huajieyu001.blog.holder;

import top.huajieyu001.blog.domain.Account;
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

    public static boolean verifyPermissions(String username){
        if(tl.get() == null || username == null || username.isEmpty()){
            return false;
        }

        if(tl.get().getUsername().equals(username)){
            return true;
        }

        return false;
    }
}
