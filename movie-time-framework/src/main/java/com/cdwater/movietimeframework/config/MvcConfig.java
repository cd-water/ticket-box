package com.cdwater.movietimeframework.config;

import com.cdwater.movietimeframework.interceptor.AdminTokenInterceptor;
import com.cdwater.movietimeframework.interceptor.AppTokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC配置
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private AdminTokenInterceptor adminTokenInterceptor;

    @Resource
    private AppTokenInterceptor appTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminTokenInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns(
                        "/admin/auth/login"
                );

        registry.addInterceptor(appTokenInterceptor)
                .addPathPatterns("/app/**")
                .excludePathPatterns(
                        "/app/movie/**",
                        "/app/cinema/**",
                        "/app/screening/**"
                );
    }
}
