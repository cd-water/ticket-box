package com.cdwater.ticketbox.identity.adapter.in.rpc;

import com.cdwater.ticketbox.identity.api.UserAuthApi;
import com.cdwater.ticketbox.identity.api.dto.UserProfileDTO;
import com.cdwater.ticketbox.identity.application.UserIdentityService;
import com.cdwater.ticketbox.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@RequiredArgsConstructor
public class UserAuthApiImpl implements UserAuthApi {

    private final UserIdentityService userIdentityService;

    @Override
    public void sendCode(String phone) {
        userIdentityService.sendCode(phone);
    }

    @Override
    public UserProfileDTO verifyCode(String phone, String code) {
        return toProfile(userIdentityService.verifyCode(phone, code));
    }

    @Override
    public UserProfileDTO verifyPassword(String phone, String password) {
        return toProfile(userIdentityService.verifyPassword(phone, password));
    }

    @Override
    public UserProfileDTO getProfile(long userId) {
        return toProfile(userIdentityService.getProfile(userId));
    }

    @Override
    public UserProfileDTO updateNickname(long userId, String nickname) {
        return toProfile(userIdentityService.updateNickname(userId, nickname));
    }

    @Override
    public UserProfileDTO updatePassword(long userId, String password) {
        return toProfile(userIdentityService.updatePassword(userId, password));
    }

    @Override
    public UserProfileDTO updateAvatar(long userId, byte[] content, String contentType, String extension) {
        return toProfile(userIdentityService.updateAvatar(userId, content, contentType, extension));
    }

    private UserProfileDTO toProfile(User user) {
        return new UserProfileDTO(user.getId(), user.getPhone(), user.getNickname(), user.getAvatar());
    }
}
