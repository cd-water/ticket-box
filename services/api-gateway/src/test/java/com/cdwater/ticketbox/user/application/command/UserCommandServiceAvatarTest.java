package com.cdwater.ticketbox.user.application.command;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.user.application.port.AvatarStorage;
import com.cdwater.ticketbox.user.application.port.CaptchaStore;
import com.cdwater.ticketbox.user.application.port.UserAuthSession;
import com.cdwater.ticketbox.user.domain.model.User;
import com.cdwater.ticketbox.user.application.query.converter.UserViewConverter;
import com.cdwater.ticketbox.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
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
    private final UserCommandService service = new UserCommandService(
            userRepository,
            mock(CaptchaStore.class),
            mock(UserAuthSession.class),
            new BCryptPasswordEncoder(),
            avatarStorage,
            mock(UserViewConverter.class));

    private User user() {
        User user = new User();
        user.setId(1L);
        user.setPhone("13800000001");
        user.setNickname("测试用户");
        user.setAvatar("https://github.com/cd-water.png");
        return user;
    }

    private MockMultipartFile pngFile() {
        byte[] png = new byte[]{(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A};
        return new MockMultipartFile("file", "a.png", "image/png", png);
    }

    @Test
    void storage_failure_does_not_update_profile() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user()));
        when(avatarStorage.upload(anyLong(), any(byte[].class), anyString(), anyString()))
                .thenThrow(new BizException(ResultCode.INTERNAL_SERVER_ERROR, "头像上传失败"));

        assertThrows(BizException.class, () -> service.updateAvatar(1L, pngFile()));
        verify(userRepository, never()).updateAvatar(anyLong(), anyString());
    }

    @Test
    void db_failure_deletes_uploaded_object() {
        String url = "http://localhost:9000/ticket-box/avatars/1/x.png";
        when(userRepository.findById(1L)).thenReturn(Optional.of(user()));
        when(avatarStorage.upload(anyLong(), any(byte[].class), anyString(), anyString()))
                .thenReturn(url);
        when(userRepository.updateAvatar(1L, url)).thenReturn(false);

        assertThrows(BizException.class, () -> service.updateAvatar(1L, pngFile()));
        verify(avatarStorage).deleteIfOwned(url);
    }
}
