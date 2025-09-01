package top.huajieyu001.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.huajieyu001.blog.domain.Article;
import top.huajieyu001.blog.holder.AccountHolder;
import top.huajieyu001.blog.result.AjaxResult;
import top.huajieyu001.blog.service.ArticleService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author huajieyu
 * @Date 5/2/2025 7:48 PM
 * @Version 1.0
 * @Description TODO
 */
@RestController
@RequestMapping("/article")
@AllArgsConstructor
public class ArticleController {

    private final ArticleService service;

    @GetMapping("/index")
    public AjaxResult index() {
        return AjaxResult.success(AccountHolder.getAccount());
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody Article article) {
        article.setIsDeleted(0);
        return AjaxResult.success(service.save(article));
    }

    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody Article article) {
        article.setIsDeleted(1);
        article.setUpdateTime(LocalDateTime.now());
        return AjaxResult.success(service.updateById(article));
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody Article article) {
        article.setVersion(article.getVersion() == null ? 1 : article.getVersion() + 1);
        article.setUpdateTime(LocalDateTime.now());
        return AjaxResult.success(service.updateById(article));
    }

    @GetMapping("/get")
    public AjaxResult get(String id) {
        Article article = service.getById(id);
        if(article == null || article.getIsDeleted() == null || article.getIsDeleted() != 0){
            return AjaxResult.error("该文章不存在");
        }
        return AjaxResult.success(article);
    }

    @GetMapping("/list")
    public AjaxResult list(Long menuId, Integer pageNum, Integer pageSize) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        if(menuId != null) {
            wrapper.eq(Article::getMenuId, menuId);
        }
        wrapper.eq(Article::getIsDeleted, 0);
        wrapper.orderByDesc(Article::getUpdateTime).orderByDesc(Article::getCreateTime);
        return AjaxResult.success(service.page(page, wrapper));
    }
}
