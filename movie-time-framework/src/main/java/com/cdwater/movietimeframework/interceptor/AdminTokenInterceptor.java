package com.cdwater.movietimeframework.interceptor;

import com.cdwater.movietimecommon.enums.RetEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import com.cdwater.movietimecommon.utils.AdminContext;
import com.cdwater.movietimecommon.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 管理员Token拦截器
 */
@Component
public class AdminTokenInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头
        String header = request.getHeader("Authorization");
        //请求头为空或格式错误
        if (header == null || !header.startsWith("Bearer ")) {
            throw new BusinessException(RetEnum.UNAUTHORIZED);
        }
        //获取token
        String token = header.substring(7);
        try {
            //验证token
            Claims claims = jwtUtil.parseToken(token);
            //检查是否在Redis黑名单
            String jti = claims.getId();
            if (jwtUtil.existTokenBlackList(jti)) {
                throw new BusinessException(RetEnum.UNAUTHORIZED);
            }
            //ThreadLocal保存管理员上下文
            Long adminId = Long.valueOf(claims.getSubject());
            Integer top = claims.get("top", Integer.class);
            AdminContext.setCurrentAdmin(adminId, top);
        } catch (Exception e) {
            throw new BusinessException(RetEnum.UNAUTHORIZED);
        }
        //放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AdminContext.removeCurrentAdmin();
    }
}
