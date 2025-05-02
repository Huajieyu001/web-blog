package top.huajieyu001.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.huajieyu001.blog.domain.Title;
import top.huajieyu001.blog.service.TitleService;
import top.huajieyu001.blog.mapper.TitleMapper;
import org.springframework.stereotype.Service;

/**
* @author xanadu
* @description 针对表【title】的数据库操作Service实现
* @createDate 2025-05-02 19:45:54
*/
@Service
public class TitleServiceImpl extends ServiceImpl<TitleMapper, Title>
    implements TitleService{

}




