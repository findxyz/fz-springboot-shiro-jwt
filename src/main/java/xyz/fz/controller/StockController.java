package xyz.fz.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fz.model.Result;

@RestController
@RequestMapping("/stock")
public class StockController {

    @RequiresPermissions("stock:view")
    @RequestMapping("/view")
    public Result view() {
        return Result.ofData("stock:view");
    }

    @RequiresPermissions("stock:edit")
    @RequestMapping("/edit")
    public Result edit() {
        return Result.ofData("stock:edit");
    }
}
