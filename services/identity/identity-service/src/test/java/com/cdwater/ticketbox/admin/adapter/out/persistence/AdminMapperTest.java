package com.cdwater.ticketbox.admin.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdwater.ticketbox.admin.adapter.out.persistence.mapper.AdminMapper;
import com.cdwater.ticketbox.admin.adapter.out.persistence.po.AdminPO;
import com.cdwater.ticketbox.identity.IdentityApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = IdentityApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminMapperTest {

    @Autowired
    private AdminMapper adminMapper;

    @Test
    void select_seed_admin_by_username() {
        AdminPO admin = adminMapper.selectOne(
                new LambdaQueryWrapper<AdminPO>().eq(AdminPO::getUsername, "admin"));
        assertNotNull(admin);
        assertTrue(admin.getPassword().startsWith("$2a$"));
        assertEquals(1, admin.getStatus());
    }
}
