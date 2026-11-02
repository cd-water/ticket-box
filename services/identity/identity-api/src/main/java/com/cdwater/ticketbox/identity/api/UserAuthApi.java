package com.cdwater.ticketbox.identity.api;

import com.cdwater.ticketbox.identity.api.dto.UserProfileDTO;

public interface UserAuthApi {

    void sendCode(String phone);

    UserProfileDTO verifyCode(String phone, String code);

    UserProfileDTO verifyPassword(String phone, String password);

    UserProfileDTO getProfile(long userId);

    UserProfileDTO updateNickname(long userId, String nickname);

    UserProfileDTO updatePassword(long userId, String password);

    UserProfileDTO updateAvatar(long userId, byte[] content, String contentType, String extension);
}
