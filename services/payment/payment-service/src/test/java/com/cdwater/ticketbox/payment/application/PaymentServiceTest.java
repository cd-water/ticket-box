package com.cdwater.ticketbox.payment.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PaymentServiceTest {

    private static final long ORDER_NO = 2900000000000000001L;

    @Autowired
    PaymentService service;

    @Autowired
    JdbcTemplate jdbc;

    @AfterEach
    void cleanUp() {
        jdbc.update("DELETE FROM t_payment_transaction WHERE payment_no = ?", "P" + ORDER_NO);
        jdbc.update("DELETE FROM t_payment_order WHERE order_no = ?", ORDER_NO);
    }

    @Test
    void create_is_idempotent_by_order_no() {
        var first = service.create(ORDER_NO, new BigDecimal("49.90"), "MOCK");
        var second = service.create(ORDER_NO, new BigDecimal("49.90"), "MOCK");

        assertEquals(first.getPaymentNo(), second.getPaymentNo());
        assertEquals("SUCCESS", first.getStatus());
        assertEquals("MOCK", first.getChannel());
    }

    @Test
    void close_marks_payment_closed() {
        var created = service.create(ORDER_NO, new BigDecimal("49.90"), "MOCK");
        service.close(created.getPaymentNo());

        var closed = service.find(created.getPaymentNo()).orElseThrow();
        assertEquals("CLOSED", closed.getStatus());
    }
}
