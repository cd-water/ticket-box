CREATE TABLE `t_booking_order`
(
    `order_no`     BIGINT         NOT NULL COMMENT '订单号（雪花算法，主键）',
    `user_id`      BIGINT         NOT NULL COMMENT '关联用户ID',
    `product_type`         VARCHAR(16)    NOT NULL COMMENT '商品类型 MOVIE/SHOW/PLAY',
    `status`       TINYINT        NOT NULL DEFAULT 0 COMMENT '状态 0-待支付 1-已支付 2-已取消（含超时）',
    `total_amount` DECIMAL(10, 2) NOT NULL COMMENT '订单总金额（元）',
    `expire_time`  DATETIME       NOT NULL COMMENT '支付截止时间',
    `pay_time`     DATETIME       NULL COMMENT '支付时间',
    `create_time`  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`order_no`),
    KEY `idx_user_product_type_status_time` (`user_id`, `product_type`, `status`, `create_time`),
    KEY `idx_status_expire` (`status`, `expire_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '统一订单表';

CREATE TABLE `t_booking_line`
(
    `order_no`          BIGINT         NOT NULL COMMENT '关联订单号',
    `product_id`        BIGINT         NOT NULL COMMENT '排场ID/演出ID/玩乐项目ID',
    `ticket_type_id`            BIGINT         NULL COMMENT '演出票档ID',
    `title`             VARCHAR(128)   NOT NULL COMMENT '商品名称快照',
    `venue_name`        VARCHAR(128)   NULL COMMENT '影院/场馆名称快照',
    `venue_address`     VARCHAR(255)   NULL COMMENT '场馆/项目地址快照',
    `hall_name`         VARCHAR(64)    NULL COMMENT '影厅名称快照',
    `ticket_type_name`  VARCHAR(64)    NULL COMMENT '票档名称快照',
    `zone`              VARCHAR(32)    NULL COMMENT '区域快照',
    `start_time`        DATETIME       NULL COMMENT '开场时间快照',
    `end_time`          DATETIME       NULL COMMENT '散场时间快照',
    `unit_price`        DECIMAL(10, 2) NOT NULL COMMENT '单价快照',
    `quantity`          INT            NOT NULL COMMENT '数量',
    PRIMARY KEY (`order_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单行项目（下单快照）';

CREATE TABLE `t_booking_seat`
(
    `order_no` BIGINT         NOT NULL COMMENT '关联订单号',
    `seat_id`  BIGINT         NOT NULL COMMENT '座位ID',
    `zone`     VARCHAR(32)    NULL COMMENT '区域（演出）',
    `seat_no`  INT            NULL COMMENT '座位号（演出）',
    `row_num`  INT            NULL COMMENT '排号（电影）',
    `col_num`  INT            NULL COMMENT '列号（电影）',
    `price`    DECIMAL(10, 2) NOT NULL COMMENT '票价（元）',
    PRIMARY KEY (`order_no`, `seat_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单座位快照';

