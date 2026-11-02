package com.cdwater.ticketbox.identity.application;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.user.application.port.AvatarStorage;
import com.cdwater.ticketbox.user.application.port.CaptchaStore;
import com.cdwater.ticketbox.user.domain.model.User;
import com.cdwater.ticketbox.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserIdentityService {

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
    private final PasswordEncoder passwordEncoder;
    private final AvatarStorage avatarStorage;
    private final SecureRandom random = new SecureRandom();

    public void sendCode(String phone) {
        String code = String.format("%06d", random.nextInt(1_000_000));
        captchaStore.save(phone, code, CAPTCHA_TTL);
        log.info("[MOCK SMS] phone={}, code={}", phone, code);
    }

    public User verifyCode(String phone, String code) {
        String saved = captchaStore.consume(phone);
        if (saved == null || !saved.equals(code)) {
            throw new BizException(ResultCode.BAD_REQUEST, "验证码错误或已过期");
        }
        User user = userRepository.findOrCreateByPhone(phone,
                () -> User.register(phone, NICKNAME_PREFIX + randomNickname()));
        requireEnabled(user);
        return user;
    }

    public User verifyPassword(String phone, String password) {
        User user = userRepository.findByPhone(phone).orElse(null);
        if (user == null
                || !user.hasPassword()
                || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BizException(ResultCode.BAD_REQUEST, "手机号或密码错误");
        }
        requireEnabled(user);
        return user;
    }

    public User getProfile(long userId) {
        return requireUser(userId);
    }

    public User updateNickname(long userId, String nickname) {
        User user = requireUser(userId);
        user.changeNickname(nickname);
        if (!userRepository.updateNickname(userId, user.getNickname())) {
            throw new BizException(ResultCode.INTERNAL_SERVER_ERROR, "昵称更新失败");
        }
        return user;
    }

    public User updatePassword(long userId, String password) {
        User user = requireUser(userId);
        String passwordHash = passwordEncoder.encode(password);
        if (!userRepository.updatePassword(userId, passwordHash)) {
            throw new BizException(ResultCode.INTERNAL_SERVER_ERROR, "密码更新失败");
        }
        return user;
    }

    public User updateAvatar(long userId, byte[] content, String contentType, String extension) {
        if (content == null || content.length == 0) {
            throw new BizException(ResultCode.BAD_REQUEST, "请选择头像文件");
        }
        if (content.length > MAX_AVATAR_BYTES) {
            throw new BizException(ResultCode.BAD_REQUEST, "头像不能超过 2MB");
        }
        if (contentType == null || !AVATAR_TYPES.containsKey(contentType)
                || extension == null || extension.isBlank()
                || !isImage(content, contentType)) {
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
        return user;
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public List<User> findByIds(Collection<Long> ids) {
        return userRepository.findByIds(ids);
    }

    private boolean isImage(byte[] content, String contentType) {
        return switch (contentType) {
            case "image/png" -> content.length >= 8
                    && (content[0] & 0xFF) == 0x89 && content[1] == 0x50
                    && content[2] == 0x4E && content[3] == 0x47
                    && content[4] == 0x0D && content[5] == 0x0A
                    && content[6] == 0x1A && content[7] == 0x0A;
            case "image/jpeg" -> content.length >= 3
                    && (content[0] & 0xFF) == 0xFF && (content[1] & 0xFF) == 0xD8
                    && (content[2] & 0xFF) == 0xFF;
            case "image/webp" -> content.length >= 12
                    && content[0] == 'R' && content[1] == 'I'
                    && content[2] == 'F' && content[3] == 'F'
                    && content[8] == 'W' && content[9] == 'E'
                    && content[10] == 'B' && content[11] == 'P';
            default -> false;
        };
    }

    private User requireUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "用户不存在"));
    }

    private void requireEnabled(User user) {
        if (!user.isEnabled()) {
            throw new BizException(ResultCode.FORBIDDEN, "账号已禁用");
        }
    }

    private String randomNickname() {
        StringBuilder sb = new StringBuilder(NICKNAME_LENGTH);
        for (int i = 0; i < NICKNAME_LENGTH; i++) {
            sb.append(NICKNAME_CHARS.charAt(random.nextInt(NICKNAME_CHARS.length())));
        }
        return sb.toString();
    }
}
