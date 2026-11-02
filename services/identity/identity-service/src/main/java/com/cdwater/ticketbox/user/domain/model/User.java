package com.cdwater.ticketbox.user.domain.model;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {

    public static final int STATUS_ENABLED = 1;

    private Long id;

    private String phone;

    /** BCrypt 密文，静默注册用户为 null */
    private String password;

    private String nickname;

    private String avatar;

    /** 0-禁用 1-启用 */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public static User register(String phone, String nickname) {
        User user = new User();
        user.setPhone(phone);
        user.setNickname(nickname);
        user.setAvatar("");
        user.setStatus(STATUS_ENABLED);
        return user;
    }

    public boolean isEnabled() {
        return Integer.valueOf(STATUS_ENABLED).equals(status);
    }

    public boolean hasPassword() {
        return password != null && !password.isBlank();
    }

    /** 修改昵称；长度 2-20 字符。 */
    public void changeNickname(String nickname) {
        String stripped = nickname == null ? "" : nickname.strip();
        if (stripped.length() < 2 || stripped.length() > 20) {
            throw new BizException(ResultCode.BAD_REQUEST, "昵称长度需为 2-20 个字符");
        }
        this.nickname = stripped;
    }
}
