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

    @PostMapping()
    public AjaxResult add(@RequestBody Article article) {
        System.out.println("===========");
        System.out.println(article);
        return AjaxResult.success(service.save(article));
    }

    @DeleteMapping()
    public AjaxResult delete(@RequestBody Article article) {
        Article temp = new Article();
        temp.setId(article.getId());
        temp.setIsDeleted(1L);
        return AjaxResult.success(service.updateById(temp));
    }

    @PutMapping()
    public AjaxResult update(@RequestBody Article article) {
        article.setVersion(article.getVersion() + 1);
        return AjaxResult.success(service.updateById(article));
    }

    @GetMapping
    public AjaxResult get(String id) {
        return AjaxResult.success(service.getById(id));
    }

//    @GetMapping("/list")
//    public AjaxResult list(Integer pageNum, Integer pageSize) {
//        System.out.println("pageNum:" + pageNum + "pageSize:" + pageSize);
//            PageHelper.startPage(pageNum, pageSize);
//        List<Article> list = service.getBaseMapper().selectList(null);
//        return AjaxResult.success(new PageInfo<>(list));
//    }

    @GetMapping("/list")
    public AjaxResult list(Long menuId, Integer pageNum, Integer pageSize) {
        System.out.println("pageNum:" + pageNum + ",   pageSize:" + pageSize);
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getMenuId, menuId).orderByDesc(Article::getUpdateTime).orderByDesc(Article::getCreateTime);
        return AjaxResult.success(service.page(page, wrapper));
    }
}
