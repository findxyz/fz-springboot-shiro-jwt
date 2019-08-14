package xyz.fz.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.fz.configuration.shiro.filter.AccountJwtFilter;
import xyz.fz.model.Account;
import xyz.fz.configuration.shiro.realm.JwtData;
import xyz.fz.service.AccountService;
import xyz.fz.util.AccountLocal;
import xyz.fz.util.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class AccountJwtServiceImpl extends JwtUtil {

    @Value("${jwt.default-exp-seconds}")
    private int defaultExpSeconds;

    @Value("${jwt.default-refresh-cycle-seconds}")
    private long defaultRefreshCycleSeconds;

    public AccountJwtServiceImpl(@Value("${jwt.key}") String jwtKey) {
        super(jwtKey);
    }

    @Resource(name = "mockAccountServiceImpl")
    private AccountService accountService;

    public String buildJwt(Account account, int expSeconds) {
        Date expDate = new Date(System.currentTimeMillis() + expSeconds * 1000);
        JwtData jwtData = new JwtData(account.getId() + "", account.getVersion(), expDate);
        return this.buildJwt(jwtData);
    }

    public String buildJwt(Account account) {
        return buildJwt(account, defaultExpSeconds);
    }

    public Account verifyJwt(String jwt) {
        Account account = null;
        JwtData jwtData = super.restoreJwt(jwt);
        if (jwtData != null) {
            account = accountService.getAccount(Long.parseLong(jwtData.getId()));
            if (account == null
                    || account.getVersion() != jwtData.getVersion()
                    || (new Date()).after(jwtData.getExpTime())) {
                account = null;
            } else {
                AccountLocal.AccountInfo accountInfo = new AccountLocal.AccountInfo(jwtData, account);
                AccountLocal.setAccountInfo(accountInfo);
            }
        }
        return account;
    }

    public String refreshJwt(long refreshCycleSeconds) {
        String newJwt = null;
        AccountLocal.AccountInfo accountInfo = AccountLocal.getAccountInfo();
        if ((accountInfo.getJwtData().getExpTime().getTime() - System.currentTimeMillis()) < refreshCycleSeconds * 1000) {
            newJwt = this.buildJwt(accountInfo.getAccount());
        }
        return newJwt;
    }

    public String refreshJwt() {
        return refreshJwt(defaultRefreshCycleSeconds);
    }

    public void setJwtCookie(HttpServletResponse response, String jwt) {
        Cookie cookie = new Cookie(AccountJwtFilter.AUTH_FLAG, jwt);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(defaultExpSeconds);
        response.addCookie(cookie);
    }
}
