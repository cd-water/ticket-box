package com.cdwater.movietimeuser.service;

import com.cdwater.movietimeuser.model.dto.AdminCreateDTO;
import com.cdwater.movietimeuser.model.dto.AdminPageDTO;
import com.cdwater.movietimeuser.model.dto.AdminEditDTO;
import com.cdwater.movietimeuser.model.vo.AdminPageVO;
import com.github.pagehelper.PageInfo;

public interface AdminService {

    void create(AdminCreateDTO adminCreateDTO);

    void delete(Long id);

    void edit(AdminEditDTO adminEditDTO);

    PageInfo<AdminPageVO> page(AdminPageDTO adminPageDTO);
}
