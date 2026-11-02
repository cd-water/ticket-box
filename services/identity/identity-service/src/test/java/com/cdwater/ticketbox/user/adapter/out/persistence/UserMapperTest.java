package com.cdwater.ticketbox.user.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdwater.ticketbox.user.adapter.out.persistence.mapper.UserMapper;
import com.cdwater.ticketbox.user.adapter.out.persistence.po.UserPO;
import com.cdwater.ticketbox.identity.IdentityApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = IdentityApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void select_seed_users() {
        UserPO u1 = userMapper.selectOne(
                new LambdaQueryWrapper<UserPO>().eq(UserPO::getPhone, "13800000001"));
        assertNotNull(u1);
        assertEquals("user_mjafo8", u1.getNickname());
        assertTrue(u1.getPassword().startsWith("$2a$"));

        UserPO u2 = userMapper.selectOne(
                new LambdaQueryWrapper<UserPO>().eq(UserPO::getPhone, "13800000002"));
        assertNotNull(u2);
        assertNull(u2.getPassword());
    }
}
