package xyz.fz.configuration.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;
import xyz.fz.model.Account;
import xyz.fz.service.impl.AccountJwtServiceImpl;
import xyz.fz.service.AccountService;

import javax.annotation.Resource;
import java.util.Arrays;

@Component
public class AccountJwtRealm extends AuthorizingRealm {

    @Resource
    private AccountJwtServiceImpl accountJwtService;

    @Resource(name = "mockAccountServiceImpl")
    private AccountService accountService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken jwtToken) throws AuthenticationException {
        String jwt = jwtToken.getPrincipal().toString();
        Account account = accountJwtService.verifyJwt(jwt);
        if (account == null) {
            throw new UnauthenticatedException();
        }
        return new SimpleAuthenticationInfo(account.getId(), jwtToken.getCredentials(), "AccountJwtRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        long accountId = (long) principals.getPrimaryPrincipal();
        Account account = accountService.getAccount(accountId);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(Arrays.asList(account.getRoles().split(",")));
        simpleAuthorizationInfo.addStringPermissions(Arrays.asList(account.getPermissions().split(",")));
        return simpleAuthorizationInfo;
    }
}
