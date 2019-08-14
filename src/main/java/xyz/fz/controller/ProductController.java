package xyz.fz.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fz.model.Result;

@RestController
@RequestMapping("/product")
public class ProductController {

    @RequiresPermissions("product:view")
    @RequestMapping("/view")
    public Result view() {
        return Result.ofData("product:view");
    }

    @RequiresPermissions("product:edit")
    @RequestMapping("/edit")
    public Result edit() {
        return Result.ofData("product:edit");
    }
}
