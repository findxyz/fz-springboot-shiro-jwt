package xyz.fz.configuration.shiro.filter;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.servlet.AdviceFilter;
import xyz.fz.service.impl.AccountJwtServiceImpl;
import xyz.fz.util.AccountLocal;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class AccountJwtRefreshFilter extends AdviceFilter {

    private Cache<Long, Long> recentlyRefreshedAccountIds = CacheBuilder.newBuilder()
            .maximumSize(5000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    private AccountJwtServiceImpl accountJwtService;

    public AccountJwtRefreshFilter(AccountJwtServiceImpl accountJwtService) {
        this.accountJwtService = accountJwtService;
    }

    @Override
    public boolean preHandle(ServletRequest request, ServletResponse response) {
        AccountLocal.AccountInfo accountInfo = AccountLocal.getAccountInfo();
        if (accountInfo != null
                && recentlyRefreshedAccountIds.getIfPresent(accountInfo.getAccount().getId()) == null) {
            String jwt = accountJwtService.refreshJwt();
            if (StringUtils.isNotBlank(jwt)) {
                recentlyRefreshedAccountIds.put(accountInfo.getAccount().getId(), accountInfo.getAccount().getId());
                accountJwtService.setJwtCookie((HttpServletResponse) response, jwt);
            }
        }
        return true;
    }
}
