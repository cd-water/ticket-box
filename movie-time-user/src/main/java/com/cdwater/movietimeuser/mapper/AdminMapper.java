package com.cdwater.movietimeuser.mapper;

import com.cdwater.movietimeuser.model.dto.AdminPageDTO;
import com.cdwater.movietimeuser.model.entity.Admin;
import com.cdwater.movietimeuser.model.vo.AdminPageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {

    Admin selectByUsername(String username);

    void insert(Admin admin);

    String selectPwdById(Long id);

    void updatePwdById(Long id, String password);

    void deleteById(Long id);

    List<AdminPageVO> selectPage(AdminPageDTO adminPageDTO);
}
