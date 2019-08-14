package xyz.fz.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.fz.model.Account;
import xyz.fz.model.Result;
import xyz.fz.service.impl.AccountJwtServiceImpl;
import xyz.fz.service.AccountService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

    @Resource
    private AccountService accountService;

    @Resource
    private AccountJwtServiceImpl accountJwtService;

    @RequestMapping("/doLogin")
    public Result doLogin(@RequestParam("userName") String userName,
                          @RequestParam("passWord") String passWord,
                          HttpServletResponse response) {
        Account account = accountService.getAccount(userName, passWord);
        if (account != null) {
            String jwt = accountJwtService.buildJwt(account);
            accountJwtService.setJwtCookie(response, jwt);
            return Result.ofJwt(jwt);
        } else {
            return Result.ofMessage("用户名或密码错误");
        }
    }
}
