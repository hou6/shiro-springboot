package com.houliu.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author houliu
 * @create 2020-02-14 17:12
 */

@Configuration
public class ShiroConfig {

    private static final String hashAlgorithmName = "md5";  //加密算法的名称
    private static final Integer iterCount = 10000;  //迭代次数
    private static final String SHIRO_FILTER = "shiroFilter";
    private static final String REMEMBERME = "rememberMe";
    private static final Integer REMEMBERME_MAX_AGE = 604800;  //毫秒：7天

    //ShiroFilterFactoryBean     @Qualifier("securityManager"):获取的是@Bean(name = "securityManager")，可以跟userRealm达到相同的效果
    @Bean(SHIRO_FILTER)
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        //添加filter的内助过滤器
        /**
         *  anon: 无需认证即可访问
         *  authc: 必须认证才能访问
         *  user: 必须拥有记住我功能才能访问
         *  perms: 必须拥有对某个资源的权限才能访问
         *  role: 拥有某个角色的权限才能访问
         */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();  //由于下面用的是链式编程，所以这里用LinkedHashMap
        filterChainDefinitionMap.put("/user/add","perms[user:add]");
        filterChainDefinitionMap.put("/user/update","perms[user:update]");
//        filterChainDefinitionMap.put("/user/*","authc");  //* 代表通配符
        //设置登出的路径
        filterChainDefinitionMap.put("/logout","logout");
        //设置没有权限时跳转到的登录页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noAuthc");
        //设置需要登录页面
        shiroFilterFactoryBean.setLoginUrl("/toLogin");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    //DefaultWebSecurityManager     @Qualifier("userRealm"): userRealm默认使用的就是userRealm()方法名
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm
            ,@Qualifier("cookieRememberMeManager") CookieRememberMeManager cookieRememberMeManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联realm
        securityManager.setRealm(userRealm);
        //关联记住我
        securityManager.setRememberMeManager(cookieRememberMeManager);
        return securityManager;
    }

    //创建realm对象
    @Bean(name = "userRealm")
    public UserRealm userRealm(CredentialsMatcher credentialsMatcher){
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(credentialsMatcher);  //在Realm中设置密码比对器
        return userRealm;
    }

    /**
     * 密码比对器
     */
    @Bean(name = "credentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置密码加密方式
        credentialsMatcher.setHashAlgorithmName(hashAlgorithmName);
        //设置迭代次数
        credentialsMatcher.setHashIterations(iterCount);
        credentialsMatcher.setStoredCredentialsHexEncoded(false);
        return credentialsMatcher;
    }

    /**
     * 设置记住我的一些参数
     * @return
     */
    @Bean("simpleCookie")
    public SimpleCookie simpleCookie(){
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName(REMEMBERME);  //设置记住我cookie的名字
        simpleCookie.setMaxAge(REMEMBERME_MAX_AGE);  //设置记住我的记住时间，7天
        simpleCookie.setHttpOnly(true);  //只允许http请求访问cookie
        return simpleCookie;
    }

    /**
     * 记住我管理器
     * @param simpleCookie
     * @return
     */
    @Bean(name = "cookieRememberMeManager")
    public CookieRememberMeManager cookieRememberMeManager(@Qualifier("simpleCookie") SimpleCookie simpleCookie){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(simpleCookie);
        return cookieRememberMeManager;
    }

    /**
     * 调用工厂中 Initializable 类型的组件的 init 方法，(注解开发时用,会自动切面)
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    /**
     *  加入注解的使用，不加入这个注解不生效--开始   (注解开发时用，识别对应的注解，进行对应的切入)
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * shiro和thymeleaf整合
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }

    /**
     * 注册shiro的委托过滤器，相当于之前在web.xml里面配置的
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy() {
        FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<>();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName(SHIRO_FILTER);
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }


    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

}
