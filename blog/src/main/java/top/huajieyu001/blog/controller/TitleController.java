package top.huajieyu001.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.huajieyu001.blog.domain.Title;
import top.huajieyu001.blog.result.AjaxResult;
import top.huajieyu001.blog.service.TitleService;

/**
 * @Author huajieyu
 * @Date 5/2/2025 7:49 PM
 * @Version 1.0
 * @Description TODO
 */
@RestController
@RequestMapping("/title")
public class TitleController {

    @Autowired
    private TitleService titleService;

    @GetMapping("/list")
    public AjaxResult list() {
        return AjaxResult.success(titleService.lambdaQuery().eq(Title::getIsDeleted, 0).list());
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody Title title) {
        return AjaxResult.success(titleService.save(title));
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody Title title) {
        return AjaxResult.success(titleService.updateById(title));
    }

    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody Title title) {
        title.setIsDeleted(1);
        return AjaxResult.success(titleService.updateById(title));
    }
}
