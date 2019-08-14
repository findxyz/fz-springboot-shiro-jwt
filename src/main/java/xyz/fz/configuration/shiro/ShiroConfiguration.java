package xyz.fz.configuration.shiro;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.fz.configuration.shiro.filter.AccountJwtFilter;
import xyz.fz.configuration.shiro.filter.AccountJwtRefreshFilter;
import xyz.fz.configuration.shiro.realm.AccountJwtRealm;
import xyz.fz.service.impl.AccountJwtServiceImpl;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    @Resource
    private AccountJwtServiceImpl accountJwtService;

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(AccountJwtRealm accountJwtRealm) {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(accountJwtRealm);

        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", new AccountJwtFilter());
        filters.put("jwtRefresh", new AccountJwtRefreshFilter(accountJwtService));
        shiroFilterFactoryBean.setFilters(filters);

        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitions = new HashMap<>();
        filterChainDefinitions.put("/favicon.ico", "anon");
        filterChainDefinitions.put("/login.html", "anon");
        filterChainDefinitions.put("/doLogin", "anon");
        filterChainDefinitions.put("/**", "jwt, jwtRefresh");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitions);

        return shiroFilterFactoryBean;
    }
}
