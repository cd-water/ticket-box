package com.cdwater.ticketbox.user.application.command;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.user.application.command.SendCodeCommand;
import com.cdwater.ticketbox.user.application.port.AvatarStorage;
import com.cdwater.ticketbox.user.application.port.CaptchaStore;
import com.cdwater.ticketbox.user.application.port.UserAuthSession;
import com.cdwater.ticketbox.user.application.port.UserAuthSession.LoginToken;
import com.cdwater.ticketbox.user.application.query.converter.UserViewConverter;
import com.cdwater.ticketbox.user.application.query.view.LoginView;
import com.cdwater.ticketbox.user.application.query.view.ProfileView;
import com.cdwater.ticketbox.user.domain.model.User;
import com.cdwater.ticketbox.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Map;

/** 用户认证命令：验证码、密码登录与注销。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommandService {

    private static final Duration CAPTCHA_TTL = Duration.ofMinutes(5);
    private static final String NICKNAME_PREFIX = "user_";
    private static final int NICKNAME_LENGTH = 6;
    private static final String NICKNAME_CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final long MAX_AVATAR_BYTES = 2L * 1024 * 1024;
    private static final Map<String, String> AVATAR_TYPES = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp");

    private final UserRepository userRepository;
    private final CaptchaStore captchaStore;
    private final UserAuthSession userAuthSession;
    private final PasswordEncoder passwordEncoder;
    private final AvatarStorage avatarStorage;
    private final UserViewConverter userViewConverter;
    private final SecureRandom random = new SecureRandom();

    /** 发送验证码；当前为 MOCK，验证码仅写入日志。 */
    public void sendCode(SendCodeCommand command) {
        String code = String.format("%06d", random.nextInt(1_000_000));
        captchaStore.save(command.getPhone(), code, CAPTCHA_TTL);
        log.info("[MOCK SMS] phone={}, code={}", command.getPhone(), code);
    }

    /** 验证码登录；首次登录静默注册。 */
    public LoginView loginByCode(CodeLoginCommand command) {
        String saved = captchaStore.consume(command.getPhone());
        if (saved == null || !saved.equals(command.getCode())) {
            throw new BizException(ResultCode.BAD_REQUEST, "验证码错误或已过期");
        }
        User user = userRepository.findOrCreateByPhone(command.getPhone(),
                () -> User.register(command.getPhone(), NICKNAME_PREFIX + randomNickname()));
        return completeLogin(user);
    }

    /** 密码登录；统一错误提示，避免泄露账号信息。 */
    public LoginView loginByPwd(PwdLoginCommand command) {
        User user = userRepository.findByPhone(command.getPhone()).orElse(null);
        if (user == null
                || !user.hasPassword()
                || !passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new BizException(ResultCode.BAD_REQUEST, "手机号或密码错误");
        }
        return completeLogin(user);
    }

    public void logout() {
        userAuthSession.logout();
    }

    private LoginView completeLogin(User user) {
        if (!user.isEnabled()) {
            throw new BizException(ResultCode.FORBIDDEN, "账号已禁用");
        }
        LoginToken token = userAuthSession.login(user.getId());
        return userViewConverter.toLoginView(user, token.getTokenName(), token.getTokenValue());
    }

    public ProfileView updateNickname(Long userId, UpdateNicknameCommand command) {
        User user = requireUser(userId);
        user.changeNickname(command.getNickname());
        if (!userRepository.updateNickname(userId, user.getNickname())) {
            throw new BizException(ResultCode.INTERNAL_SERVER_ERROR, "昵称更新失败");
        }
        return profile(user);
    }

    public ProfileView updatePassword(Long userId, UpdatePasswordCommand command) {
        User user = requireUser(userId);
        String passwordHash = passwordEncoder.encode(command.getPassword());
        if (!userRepository.updatePassword(userId, passwordHash)) {
            throw new BizException(ResultCode.INTERNAL_SERVER_ERROR, "密码更新失败");
        }
        userAuthSession.logoutAll(userId);
        return profile(user);
    }

    public ProfileView updateAvatar(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST, "请选择头像文件");
        }
        if (file.getSize() > MAX_AVATAR_BYTES) {
            throw new BizException(ResultCode.BAD_REQUEST, "头像不能超过 2MB");
        }

        byte[] content;
        try {
            content = file.getBytes();
        } catch (IOException e) {
            throw new BizException(ResultCode.INTERNAL_SERVER_ERROR, "头像读取失败");
        }
        String contentType = detectImageType(content);
        String extension = contentType == null ? null : AVATAR_TYPES.get(contentType);
        if (extension == null) {
            throw new BizException(ResultCode.BAD_REQUEST, "仅支持 jpg/png/webp 图片");
        }

        User user = requireUser(userId);
        String oldAvatar = user.getAvatar();
        String url = avatarStorage.upload(userId, content, contentType, extension);
        try {
            if (!userRepository.updateAvatar(userId, url)) {
                throw new BizException(ResultCode.INTERNAL_SERVER_ERROR, "头像更新失败");
            }
        } catch (RuntimeException e) {
            avatarStorage.deleteIfOwned(url);
            throw e;
        }
        avatarStorage.deleteIfOwned(oldAvatar);
        user.setAvatar(url);
        return profile(user);
    }

    private static String detectImageType(byte[] content) {
        if (content.length >= 8
                && (content[0] & 0xFF) == 0x89 && content[1] == 'P' && content[2] == 'N'
                && content[3] == 'G' && content[4] == 0x0D && content[5] == 0x0A
                && content[6] == 0x1A && content[7] == 0x0A) {
            return "image/png";
        }
        if (content.length >= 3
                && (content[0] & 0xFF) == 0xFF && (content[1] & 0xFF) == 0xD8
                && (content[2] & 0xFF) == 0xFF) {
            return "image/jpeg";
        }
        if (content.length >= 12
                && content[0] == 'R' && content[1] == 'I' && content[2] == 'F' && content[3] == 'F'
                && content[8] == 'W' && content[9] == 'E' && content[10] == 'B' && content[11] == 'P') {
            return "image/webp";
        }
        return null;
    }

    private User requireUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "用户不存在"));
    }

    private ProfileView profile(User user) {
        return userViewConverter.toProfileView(user);
    }

    private String randomNickname() {
        StringBuilder sb = new StringBuilder(NICKNAME_LENGTH);
        for (int i = 0; i < NICKNAME_LENGTH; i++) {
            sb.append(NICKNAME_CHARS.charAt(random.nextInt(NICKNAME_CHARS.length())));
        }
        return sb.toString();
    }
}
