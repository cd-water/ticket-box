package com.cdwater.movietimeuser.service;

import com.cdwater.movietimeuser.model.dto.*;
import com.cdwater.movietimeuser.model.vo.AdminLoginRes;
import com.cdwater.movietimeuser.model.vo.AppLoginRes;

public interface AuthService {

    /**
     * Admin端
     */
    AdminLoginRes adminLogin(AdminLoginDTO adminLoginDTO);

    void adminLogout(String header);

    void adminChangePwd(AdminCpwdDTO adminCpwdDTO, String header);

    /**
     * App端
     */
    void appSendCode(SendCodeDTO sendCodeDTO);

    AppLoginRes appPhoneLogin(AppLoginDTO appLoginDTO);

    AppLoginRes appPwdLogin(AppLoginDTO appLoginDTO);

    void appChangePwd(AppCpwdDTO appCpwdDTO);

    void appForgetPwd(AppFpwdDTO appFpwdDTO);

    void appLogout(String authHeader);

    AppLoginRes appRefreshToken(String authHeader, String refreshToken);
}
