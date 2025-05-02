package top.huajieyu001.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.huajieyu001.blog.domain.Account;
import top.huajieyu001.blog.service.AccountService;
import top.huajieyu001.blog.mapper.AccountMapper;
import org.springframework.stereotype.Service;

/**
* @author xanadu
* @description 针对表【account】的数据库操作Service实现
* @createDate 2025-05-02 19:45:54
*/
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
    implements AccountService{

}




