package com.cdwater.ticketbox.user.application.command;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.identity.api.UserAuthApi;
import com.cdwater.ticketbox.identity.api.dto.UserProfileDTO;
import com.cdwater.ticketbox.user.application.port.UserAuthSession;
import com.cdwater.ticketbox.user.application.port.UserAuthSession.LoginToken;
import com.cdwater.ticketbox.user.application.query.view.LoginView;
import com.cdwater.ticketbox.user.application.query.view.ProfileView;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private static final long MAX_AVATAR_BYTES = 2L * 1024 * 1024;
    private static final Map<String, String> AVATAR_TYPES = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp");

    @DubboReference(check = false, timeout = 5000)
    private UserAuthApi userAuthApi;

    private final UserAuthSession userAuthSession;

    public void sendCode(SendCodeCommand command) {
        userAuthApi.sendCode(command.getPhone());
    }

    public LoginView loginByCode(CodeLoginCommand command) {
        UserProfileDTO profile = userAuthApi.verifyCode(command.getPhone(), command.getCode());
        return completeLogin(profile);
    }

    public LoginView loginByPwd(PwdLoginCommand command) {
        UserProfileDTO profile = userAuthApi.verifyPassword(
                command.getPhone(), command.getPassword());
        return completeLogin(profile);
    }

    public void logout() {
        userAuthSession.logout();
    }

    public ProfileView updateNickname(Long userId, UpdateNicknameCommand command) {
        return toProfile(userAuthApi.updateNickname(userId, command.getNickname()));
    }

    public ProfileView updatePassword(Long userId, UpdatePasswordCommand command) {
        ProfileView profile = toProfile(userAuthApi.updatePassword(userId, command.getPassword()));
        userAuthSession.logoutAll(userId);
        return profile;
    }

    public ProfileView updateAvatar(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST, "请选择头像文件");
        }
        if (file.getSize() > MAX_AVATAR_BYTES) {
            throw new BizException(ResultCode.BAD_REQUEST, "头像不能超过 2MB");
        }
        String contentType = file.getContentType();
        String extension = contentType == null ? null : AVATAR_TYPES.get(contentType);
        if (extension == null) {
            throw new BizException(ResultCode.BAD_REQUEST, "仅支持 jpg/png/webp 图片");
        }
        byte[] content;
        try {
            content = file.getBytes();
        } catch (IOException e) {
            throw new BizException(ResultCode.INTERNAL_SERVER_ERROR, "头像读取失败");
        }
        return toProfile(userAuthApi.updateAvatar(userId, content, contentType, extension));
    }

    private LoginView completeLogin(UserProfileDTO profile) {
        LoginToken token = userAuthSession.login(profile.getId());
        return new LoginView(token.getTokenName(), token.getTokenValue(),
                profile.getId(), profile.getPhone(), profile.getNickname(), profile.getAvatar());
    }

    private ProfileView toProfile(UserProfileDTO profile) {
        return new ProfileView(profile.getId(), profile.getPhone(),
                profile.getNickname(), profile.getAvatar());
    }
}
