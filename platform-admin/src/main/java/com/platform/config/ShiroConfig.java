/*
 * 项目名称:platform-plus
 * 类名称:ShiroConfig.java
 * 包名称:com.platform.config
 *
 * 修改履历:
 *      日期                修正者      主要内容
 *      2018/11/21 16:04    李鹏军      初版完成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.config;

import com.platform.datascope.DataScopeInterceptor;
import com.platform.modules.sys.oauth2.Oauth2Filter;
import com.platform.modules.sys.oauth2.Oauth2Realm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置
 *
 * @author 李鹏军
 */
@Configuration
public class ShiroConfig {

    @Bean("sessionManager")
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(Oauth2Realm oAuth2Realm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        //oauth过滤
        Map<String, Filter> filters = new HashMap<>(16);
        filters.put("oauth2", new Oauth2Filter());
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/druid/**", "anon");
        filterMap.put("/sys/login", "anon");
        filterMap.put("/sys/checkuser", "anon");
        filterMap.put("/sys/loginbind", "anon");  //liuqianru add 2020/12/10
        filterMap.put("/qkjvip/memberactivity/infohtml", "anon");
        filterMap.put("/qkjvip/memberactivity/queryAllhtml", "anon");
        filterMap.put("/qkjvip/memberactivity/queryAllcount", "anon");
        filterMap.put("/qkjvip/memberactivity/actityisexist", "anon");
        filterMap.put("/qkjvip/sendmsgbatch/sendmsg", "anon");
        filterMap.put("/qkjvip/membersignupmember/savesign", "anon");
        filterMap.put("/qkjvip/membercpon/sendCponDetail", "anon");
        filterMap.put("/qkjvip/member/selectMemByOpenid", "anon");
        filterMap.put("/qkjvip/member/queryAll", "anon");
        filterMap.put("/qkjvip/member/queryAllByMobile", "anon");
        filterMap.put("/qkjvip/member/selectMemByJueruMemberid", "anon");
        filterMap.put("/qkjvip/member/checkFromScan", "anon"); // liuqianru add 2021/05/17
        filterMap.put("/qkjvip/member/getMemberLevelFromScan", "anon"); // liuqianru add 2021/05/14
        filterMap.put("/qkjvip/membersignup/save", "anon");
        filterMap.put("/qkjvip/membersignup/queryAll", "anon");
        filterMap.put("/remote/news/**", "anon"); //liuqianru add 2021/03/11
        filterMap.put("/cmnt/mgmtcomment/viewList", "anon"); //liuqianru add 2021/05/26
        filterMap.put("/cmnt/mgmtcomment/save", "anon"); //liuqianru add 2021/05/26
        filterMap.put("/cmnt/mgmtthumbsup/**", "anon"); //liuqianru add 2021/05/26
        filterMap.put("/qkjvip/newscarousel/carouselList", "anon"); // liuqianru add 2021/05/10
        filterMap.put("/qkjvip/news/newsList", "anon"); // liuqianru add 2021/05/10
        filterMap.put("/qkjvip/news/newsInfo", "anon"); // liuqianru add 2021/05/10
        filterMap.put("/qkjvip/newsthumbsup/**", "anon"); // liuqianru add 2021/06/04
        filterMap.put("/qkjvip/content/getInfo", "anon");
        filterMap.put("/qkjvip/contentsharerecords/addIntegral", "anon");
        filterMap.put("/sys/login2", "anon");
        filterMap.put("/sys/userreg", "anon");
        filterMap.put("/sys/oss/upload", "anon");
        filterMap.put(" /addjson/address", "anon");
        filterMap.put("/captcha.jpg", "anon");

        filterMap.put("/diagram-viewer/**", "anon");
        filterMap.put("/editor-app/**", "anon");
        filterMap.put("/service/**", "anon");
        filterMap.put("/ajax/**", "anon");  //liuqianru add 前端调用ajax允许匿名访问
        filterMap.put("/**", "oauth2");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * 数据权限插件
     *
     * @return DataScopeInterceptor
     */
    @Bean
    public DataScopeInterceptor dataScopeInterceptor() {
        return new DataScopeInterceptor();
    }
}
