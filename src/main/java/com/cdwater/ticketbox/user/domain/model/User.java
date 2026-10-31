package com.cdwater.ticketbox.user.domain.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_user")
public class User {

    public static final int STATUS_ENABLED = 1;

    @TableId(type = IdType.AUTO)
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
}
