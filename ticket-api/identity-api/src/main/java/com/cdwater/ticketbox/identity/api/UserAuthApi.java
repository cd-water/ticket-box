package com.cdwater.ticketbox.identity.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.identity.api.dto.UserProfileDTO;

public interface UserAuthApi {

    void sendCode(String phone) throws BizException;

    UserProfileDTO verifyCode(String phone, String code) throws BizException;

    UserProfileDTO verifyPassword(String phone, String password) throws BizException;

    UserProfileDTO getProfile(long userId) throws BizException;

    UserProfileDTO updateNickname(long userId, String nickname) throws BizException;

    UserProfileDTO updatePassword(long userId, String password) throws BizException;

    UserProfileDTO updateAvatar(long userId, byte[] content, String contentType, String extension)
            throws BizException;
}
