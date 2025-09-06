package top.huajieyu001.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import top.huajieyu001.blog.constant.RedisConstant;
import top.huajieyu001.blog.domain.Article;
import top.huajieyu001.blog.domain.vo.ArticleDetailVo;
import top.huajieyu001.blog.result.AjaxResult;
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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addArticle(ArticleDetailVo articleDetailVo) {
        Article article = new Article();
        BeanUtils.copyProperties(articleDetailVo, article);
        Long id = stringRedisTemplate.opsForValue().increment(RedisConstant.REDIS_KEY_ARTICLE_INCR_ID);
        Long contentId = stringRedisTemplate.opsForValue().increment(RedisConstant.REDIS_KEY_CONTENT_INCR_ID);
        article.setId(id);
        save(article);
        return AjaxResult.success();
    }
}




