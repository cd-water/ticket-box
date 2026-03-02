package com.cdwater.movietimeuser.service.impl;

import com.cdwater.movietimecommon.enums.RetEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import com.cdwater.movietimecommon.utils.AdminContext;
import com.cdwater.movietimeuser.mapper.AdminMapper;
import com.cdwater.movietimeuser.model.dto.AdminCreateDTO;
import com.cdwater.movietimeuser.model.dto.AdminPageDTO;
import com.cdwater.movietimeuser.model.dto.AdminEditDTO;
import com.cdwater.movietimeuser.model.entity.Admin;
import com.cdwater.movietimeuser.model.vo.AdminPageVO;
import com.cdwater.movietimeuser.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public void create(AdminCreateDTO adminCreateDTO) {
        Admin adminDB = adminMapper.selectByUsername(adminCreateDTO.getUsername());
        if (adminDB != null) {
            //管理员已存在
            throw new BusinessException(RetEnum.ACCOUNT_EXIST);
        }
        //创建管理员
        Admin admin = Admin.builder()
                .username(adminCreateDTO.getUsername())
                .password(passwordEncoder.encode(adminCreateDTO.getPassword()))
                .build();
        adminMapper.insert(admin);
    }

    @Override
    public void delete(Long id) {
        Long adminId = AdminContext.getId();
        if (Objects.equals(adminId, id)) {
            //不允许删除当前（超级）管理员
            throw new BusinessException(RetEnum.FORBIDDEN);
        }
        adminMapper.deleteById(id);
    }

    @Override
    public void edit(AdminEditDTO adminEditDTO) {
        Long id = adminEditDTO.getId();
        String password = adminEditDTO.getPassword();
        adminMapper.updatePwdById(id, passwordEncoder.encode(password));
    }

    @Override
    public PageInfo<AdminPageVO> page(AdminPageDTO adminPageDTO) {
        PageHelper.startPage(adminPageDTO.getPageNum(), adminPageDTO.getPageSize());
        List<AdminPageVO> list = adminMapper.selectPage(adminPageDTO);
        return PageInfo.of(list);
    }
}
