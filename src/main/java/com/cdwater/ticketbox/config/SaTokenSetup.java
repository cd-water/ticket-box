package com.cdwater.ticketbox.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/** 双端 Sa-Token 会话与路由鉴权配置。 */
@Configuration
public class SaTokenSetup implements WebMvcConfigurer {

    public static final StpLogic USER = new StpLogic("user");
    public static final StpLogic ADMIN = new StpLogic("admin");

    private static final long DAY = 24 * 60 * 60L;

    public SaTokenSetup() {
        USER.setConfig(new SaTokenConfig()
                .setTokenName("Authorization")
                .setTokenPrefix("Bearer")
                .setIsReadHeader(true)
                .setIsReadCookie(false)
                .setIsReadBody(false)
                .setIsConcurrent(true)
                .setIsShare(false)
                .setAutoRenew(true)
                .setIsLog(true)
                .setTimeout(60 * DAY)
                .setActiveTimeout(30 * DAY)
                .setTokenStyle("uuid"));
        ADMIN.setConfig(new SaTokenConfig()
                .setTokenName("Authorization")
                .setTokenPrefix("Bearer")
                .setIsReadHeader(true)
                .setIsReadCookie(false)
                .setIsReadBody(false)
                .setIsConcurrent(true)
                .setIsShare(false)
                .setAutoRenew(true)
                .setIsLog(true)
                .setTimeout(15 * DAY)
                .setActiveTimeout(7 * DAY)
                .setTokenStyle("random-64"));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> {
            if (handler instanceof ResourceHttpRequestHandler) {
                return;
            }
            SaRouter.match("/api/admin/**")
                    .notMatch("/api/admin/login")
                    .check(r -> ADMIN.checkLogin());
            SaRouter.match("/api/**")
                    .notMatch("/api/admin/**", "/api/user/send-code", "/api/user/login/**")
                    .check(r -> USER.checkLogin());
        })).addPathPatterns("/api/**");
    }
}
