package xyz.fz.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fz.model.Result;

@RestController
@RequestMapping("/order")
public class OrderController {

    @RequiresPermissions("order:view")
    @RequestMapping("/view")
    public Result view() {
        return Result.ofData("order:view");
    }

    @RequiresPermissions("order:edit")
    @RequestMapping("/edit")
    public Result edit() {
        return Result.ofData("order:edit");
    }
}
