package com.cdwater.movietimeuser.service;

import com.cdwater.movietimeuser.model.dto.UserEditDTO;
import com.cdwater.movietimeuser.model.dto.UserPageDTO;
import com.cdwater.movietimeuser.model.vo.UserDetailVO;
import com.cdwater.movietimeuser.model.vo.UserListVO;
import com.cdwater.movietimeuser.model.vo.UserPageVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {

    PageInfo<UserPageVO> page(UserPageDTO userPageDTO);

    Long count();

    UserDetailVO detail(Long userId, String authHeader);

    void follow(Long targetId);

    List<UserListVO> listFollower(Long userId);

    List<UserListVO> listFan(Long userId);

    void edit(UserEditDTO userEditDTO);
}
