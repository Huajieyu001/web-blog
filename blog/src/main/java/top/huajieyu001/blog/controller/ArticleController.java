package top.huajieyu001.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.huajieyu001.blog.domain.Article;
import top.huajieyu001.blog.domain.vo.ArticleDetailVo;
import top.huajieyu001.blog.holder.AccountHolder;
import top.huajieyu001.blog.result.AjaxResult;
import top.huajieyu001.blog.service.ArticleService;

/**
 * @Author huajieyu
 * @Date 5/2/2025 7:48 PM
 * @Version 1.0
 * @Description TODO
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/index")
    public AjaxResult index() {
        return AjaxResult.success(AccountHolder.getAccount());
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody ArticleDetailVo articleDetailVo) {
        return articleService.addArticle(articleDetailVo);
    }
}
