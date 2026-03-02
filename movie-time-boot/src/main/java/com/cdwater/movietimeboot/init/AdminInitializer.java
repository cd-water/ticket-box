package com.cdwater.movietimeboot.init;

import com.cdwater.movietimecommon.constants.TextConstant;
import com.cdwater.movietimeuser.mapper.AdminMapper;
import com.cdwater.movietimeuser.model.entity.Admin;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AdminInitializer implements ApplicationRunner {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Admin admin = adminMapper.selectByUsername(TextConstant.DEFAULT_ADMIN_USERNAME);
        if (admin != null) {
            //超级管理员已存在
            return;
        }
        //创建默认超级管理员
        Admin superAdmin = Admin.builder()
                .username(TextConstant.DEFAULT_ADMIN_USERNAME)
                .password(passwordEncoder.encode(TextConstant.DEFAULT_ADMIN_PASSWORD))
                .top(TextConstant.TOP)
                .build();
        adminMapper.insert(superAdmin);
        log.info("创建默认超级管理员：账号-{}，密码-{}", TextConstant.DEFAULT_ADMIN_USERNAME, TextConstant.DEFAULT_ADMIN_PASSWORD);
    }
}
