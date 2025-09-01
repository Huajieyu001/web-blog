package top.huajieyu001.blog.controller;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.huajieyu001.blog.domain.Menu;
import top.huajieyu001.blog.result.AjaxResult;
import top.huajieyu001.blog.service.MenuService;

import java.time.LocalDateTime;

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
        Menu condition = menuService.lambdaQuery().eq(Menu::getName, menu.getName()).eq(Menu::getIsDeleted, 0).one();
        if (condition != null) {
            return AjaxResult.error("该菜单已存在");
        }
        menu.setIsDeleted(0);
        return AjaxResult.success(menuService.save(menu));
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody Menu menu) {
        Menu condition = menuService.lambdaQuery().eq(Menu::getName, menu.getName()).eq(Menu::getIsDeleted, 0).one();
        if (condition != null) {
            return AjaxResult.error("该菜单已存在");
        }
        menu.setUpdateTime(LocalDateTime.now());
        if(menu.getComment() != null && menu.getComment().isEmpty()) {
            menu.setComment(null);
        }
        return AjaxResult.success(menuService.updateById(menu));
    }

    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody Menu menu) {
        menu.setIsDeleted(1);
        menu.setUpdateTime(LocalDateTime.now());
        return AjaxResult.success(menuService.updateById(menu));
    }
}