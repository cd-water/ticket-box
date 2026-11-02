package com.cdwater.ticketbox.user.application.command;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.identity.application.UserIdentityService;
import com.cdwater.ticketbox.user.application.port.AvatarStorage;
import com.cdwater.ticketbox.user.application.port.CaptchaStore;
import com.cdwater.ticketbox.user.domain.model.User;
import com.cdwater.ticketbox.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserCommandServiceAvatarTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final AvatarStorage avatarStorage = mock(AvatarStorage.class);
    private final UserIdentityService service = new UserIdentityService(
            userRepository,
            mock(CaptchaStore.class),
            new BCryptPasswordEncoder(),
            avatarStorage);

    private User user() {
        User user = new User();
        user.setId(1L);
        user.setPhone("13800000001");
        user.setNickname("测试用户");
        user.setAvatar("https://github.com/cd-water.png");
        return user;
    }

    @Test
    void storage_failure_does_not_update_profile() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user()));
        when(avatarStorage.upload(anyLong(), any(byte[].class), anyString(), anyString()))
                .thenThrow(new BizException(ResultCode.INTERNAL_SERVER_ERROR, "头像上传失败"));

        assertThrows(BizException.class,
                () -> service.updateAvatar(1L, pngBytes(), "image/png", "png"));
        verify(userRepository, never()).updateAvatar(anyLong(), anyString());
    }

    @Test
    void db_failure_deletes_uploaded_object() {
        String url = "http://localhost:9000/ticket-box/avatars/1/x.png";
        when(userRepository.findById(1L)).thenReturn(Optional.of(user()));
        when(avatarStorage.upload(anyLong(), any(byte[].class), anyString(), anyString()))
                .thenReturn(url);
        when(userRepository.updateAvatar(1L, url)).thenReturn(false);

        assertThrows(BizException.class,
                () -> service.updateAvatar(1L, pngBytes(), "image/png", "png"));
        verify(avatarStorage).deleteIfOwned(url);
    }

    private byte[] pngBytes() {
        return new byte[]{(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A};
    }
}
