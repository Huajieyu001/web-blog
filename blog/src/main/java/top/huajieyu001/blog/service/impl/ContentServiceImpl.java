package top.huajieyu001.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.huajieyu001.blog.domain.Content;
import top.huajieyu001.blog.service.ContentService;
import top.huajieyu001.blog.mapper.ContentMapper;
import org.springframework.stereotype.Service;

/**
* @author xanadu
* @description 针对表【content】的数据库操作Service实现
* @createDate 2025-05-02 19:45:54
*/
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, Content>
    implements ContentService{

}




