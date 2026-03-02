package com.cdwater.movietimeframework.interceptor;

import com.cdwater.movietimecommon.annotations.SkipAppInterceptor;
import com.cdwater.movietimecommon.enums.RetEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import com.cdwater.movietimecommon.utils.JwtUtil;
import com.cdwater.movietimecommon.utils.UserContext;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用户Token拦截器
 */
@Component
public class AppTokenInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        //检查controller方法是否有Skip注解
        if (handlerMethod.getMethodAnnotation(SkipAppInterceptor.class) != null) {
            return true;
        }
        //获取请求头
        String authHeader = request.getHeader("Authorization");
        //请求头为空或格式错误
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException(RetEnum.UNAUTHORIZED);
        }
        //获取accessToken
        String accessToken = authHeader.substring(7);
        try {
            //验证accessToken
            Claims claims = jwtUtil.parseToken(accessToken);
            //检查是否在Redis黑名单
            String jti = claims.getId();
            if (jwtUtil.existTokenBlackList(jti)) {
                throw new BusinessException(RetEnum.UNAUTHORIZED);
            }
            //ThreadLocal保存用户上下文
            Long userId = Long.valueOf(claims.getSubject());
            String phone = claims.get("phone", String.class);
            UserContext.setCurrentUser(userId, phone);
        } catch (Exception e) {
            throw new BusinessException(RetEnum.UNAUTHORIZED);
        }
        //放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeCurrentUser();
    }
}
