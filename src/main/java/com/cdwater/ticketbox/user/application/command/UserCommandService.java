package com.cdwater.ticketbox.user.application.command;

import com.cdwater.ticketbox.shared.error.BizException;
import com.cdwater.ticketbox.shared.error.ResultCode;
import com.cdwater.ticketbox.user.application.port.CaptchaStore;
import com.cdwater.ticketbox.user.application.port.UserAuthSession.LoginToken;
import com.cdwater.ticketbox.user.application.port.UserAuthSession;
import com.cdwater.ticketbox.user.application.query.view.LoginView;
import com.cdwater.ticketbox.user.domain.model.User;
import com.cdwater.ticketbox.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

/** 用户认证命令：验证码、密码登录与注销。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommandService {

    private static final Duration CAPTCHA_TTL = Duration.ofMinutes(5);
    private static final String NICKNAME_PREFIX = "user_";
    private static final int NICKNAME_LENGTH = 6;
    private static final String NICKNAME_CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";

    private final UserRepository userRepository;
    private final CaptchaStore captchaStore;
    private final UserAuthSession userAuthSession;
    private final PasswordEncoder passwordEncoder;
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
        return new LoginView(token.getTokenName(), token.getTokenValue(), user.getId(),
                user.getPhone(), user.getNickname(), user.getAvatar());
    }

    private String randomNickname() {
        StringBuilder sb = new StringBuilder(NICKNAME_LENGTH);
        for (int i = 0; i < NICKNAME_LENGTH; i++) {
            sb.append(NICKNAME_CHARS.charAt(random.nextInt(NICKNAME_CHARS.length())));
        }
        return sb.toString();
    }
}
