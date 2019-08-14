package xyz.fz.configuration.shiro.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import xyz.fz.configuration.shiro.realm.JwtToken;
import xyz.fz.util.AccountLocal;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class AccountJwtFilter extends BasicHttpAuthenticationFilter {

    public static final String AUTH_FLAG = "jwtAuth";

    private static final String TOKEN_FLAG = "jwtToken";

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            executeLogin(request, response);
        }
        return true;
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {

        String headerJwt = ((HttpServletRequest) request).getHeader(AUTH_FLAG);

        if (StringUtils.isNotBlank(headerJwt)) {
            request.setAttribute(TOKEN_FLAG, new JwtToken(headerJwt));
            return true;
        }

        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTH_FLAG)) {
                    request.setAttribute(TOKEN_FLAG, new JwtToken(cookie.getValue()));
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        AccountLocal.remove();
        getSubject(request, response).login((JwtToken) request.getAttribute(TOKEN_FLAG));
        return true;
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) {
        AccountLocal.remove();
    }
}
