package top.huajieyu001.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.huajieyu001.blog.domain.Article;
import top.huajieyu001.blog.service.ArticleService;
import top.huajieyu001.blog.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

/**
* @author xanadu
* @description 针对表【article】的数据库操作Service实现
* @createDate 2025-05-02 19:45:54
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{

}




