CREATE TABLE `t_payment_order`
(
    `id`              BIGINT         NOT NULL AUTO_INCREMENT,
    `payment_no`      VARCHAR(64)    NOT NULL,
    `order_no`        BIGINT         NOT NULL,
    `amount`          DECIMAL(10, 2) NOT NULL,
    `channel`         VARCHAR(32)    NOT NULL,
    `status`          VARCHAR(16)    NOT NULL,
    `idempotency_key` VARCHAR(64)    NOT NULL,
    `create_time`     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `pay_time`        DATETIME       NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_payment_no` (`payment_no`),
    UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付单表';

CREATE TABLE `t_payment_transaction`
(
    `id`             BIGINT         NOT NULL AUTO_INCREMENT,
    `payment_no`     VARCHAR(64)    NOT NULL,
    `type`           VARCHAR(16)    NOT NULL,
    `amount`         DECIMAL(10, 2) NOT NULL,
    `channel_txn_no` VARCHAR(64)    NOT NULL,
    `create_time`    DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_payment_no` (`payment_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付流水表';
