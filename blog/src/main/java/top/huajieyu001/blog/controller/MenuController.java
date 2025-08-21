package top.huajieyu001.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.huajieyu001.blog.domain.Menu;
import top.huajieyu001.blog.result.AjaxResult;
import top.huajieyu001.blog.service.MenuService;

/**
 * @Author huajieyu
 * @Date 5/2/2025 7:49 PM
 * @Version 1.0
 * @Description TODO
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public AjaxResult list() {
        return AjaxResult.success(menuService.lambdaQuery().eq(Menu::getIsDeleted, 0).list());
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody Menu menu) {
        return AjaxResult.success(menuService.save(menu));
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody Menu menu) {
        return AjaxResult.success(menuService.updateById(menu));
    }

    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody Menu menu) {
        menu.setIsDeleted(1);
        return AjaxResult.success(menuService.updateById(menu));
    }
}