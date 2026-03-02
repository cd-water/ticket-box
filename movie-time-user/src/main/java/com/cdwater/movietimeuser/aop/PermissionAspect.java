package com.cdwater.movietimeuser.aop;

import com.cdwater.movietimecommon.constants.TextConstant;
import com.cdwater.movietimecommon.enums.RetEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import com.cdwater.movietimecommon.utils.AdminContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 管理员权限控制切面
 */
@Aspect
@Component
public class PermissionAspect {

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.cdwater.movietimeuser.aop.SuperAdmin)")
    public void permissionPointcut() {
    }

    /**
     * 通知
     */
    @Before("permissionPointcut()")
    public void permissionAdvice() {
        //仅限超级管理员操作
        Integer permission = AdminContext.getPermission();
        if (!permission.equals(TextConstant.TOP)) {
            throw new BusinessException(RetEnum.FORBIDDEN);
        }
    }
}
