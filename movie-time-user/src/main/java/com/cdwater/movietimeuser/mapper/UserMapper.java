package com.cdwater.movietimeuser.mapper;

import com.cdwater.movietimeuser.model.dto.UserPageDTO;
import com.cdwater.movietimeuser.model.entity.User;
import com.cdwater.movietimeuser.model.entity.UserFollow;
import com.cdwater.movietimeuser.model.vo.UserListVO;
import com.cdwater.movietimeuser.model.vo.UserPageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<UserPageVO> selectPage(UserPageDTO userPageDTO);

    Long count();

    User selectByPhone(String phone);

    void insert(User user);

    void updatePwdByPhone(String phone, String newPassword);

    User selectById(Long id);

    void updateById(User user);

    void insertFollow(Long followerId, Long followeeId);

    void deleteFollow(Long followerId, Long followeeId);

    List<UserListVO> selectByIds(List<Long> ids);

    List<UserFollow> selectFollow();
}
