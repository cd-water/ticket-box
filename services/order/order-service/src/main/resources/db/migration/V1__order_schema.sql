CREATE TABLE `t_movie_order`
(
    `order_no`     BIGINT         NOT NULL COMMENT '订单号（雪花算法，主键）',
    `user_id`      BIGINT         NOT NULL COMMENT '关联用户ID(t_user.id)',
    `screening_id` BIGINT         NOT NULL COMMENT '关联排场ID(t_screening.id)',
    `movie_title`  VARCHAR(64)    NOT NULL COMMENT '片名快照',
    `cinema_name`  VARCHAR(64)    NOT NULL COMMENT '影院名称快照',
    `hall_name`    VARCHAR(32)    NOT NULL COMMENT '影厅名称快照',
    `start_time`   DATETIME       NOT NULL COMMENT '开场时间快照',
    `end_time`     DATETIME       NOT NULL COMMENT '散场时间快照',
    `total_amount` DECIMAL(10, 2) NOT NULL COMMENT '订单总金额（元）',
    `status`       TINYINT        NOT NULL DEFAULT 0 COMMENT '状态 0-待支付 1-已支付 2-已取消（含超时）',
    `expire_time`  DATETIME       NOT NULL COMMENT '支付截止时间',
    `pay_time`     DATETIME       NULL COMMENT '支付时间',
    `create_time`  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`order_no`),
    KEY `idx_user_status_time` (`user_id`, `status`, `create_time`),
    KEY `idx_status_expire` (`status`, `expire_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '电影订单表';

# 14. 电影订单座位关联表
CREATE TABLE `t_movie_order_seat`
(
    `order_no`    BIGINT         NOT NULL COMMENT '关联订单号(t_movie_order.order_no)',
    `seat_id`     BIGINT         NOT NULL COMMENT '关联座位ID(t_seat.id)',
    `row_num`     INT            NOT NULL COMMENT '下单时排号快照',
    `col_num`     INT            NOT NULL COMMENT '下单时列号快照',
    `price`       DECIMAL(10, 2) NOT NULL COMMENT '票价（元）',
    PRIMARY KEY (`order_no`, `seat_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '电影订单座位关联表';

CREATE TABLE `t_show_order`
(
    `order_no`      BIGINT         NOT NULL COMMENT '订单号（雪花算法，主键）',
    `user_id`       BIGINT         NOT NULL COMMENT '关联用户ID(t_user.id)',
    `show_id`       BIGINT         NOT NULL COMMENT '关联演出ID(t_show.id)',
    `show_title`    VARCHAR(64)    NOT NULL COMMENT '演出名称快照',
    `venue_name`    VARCHAR(64)    NOT NULL COMMENT '场馆名称快照',
    `venue_address` VARCHAR(255)   NOT NULL COMMENT '场馆地址快照',
    `start_time`    DATETIME       NOT NULL COMMENT '开场时间快照',
    `ticket_type_id`    BIGINT         NOT NULL COMMENT '关联演出票档ID(t_show_ticket_type.id)',
    `ticket_type_name`  VARCHAR(64)    NOT NULL COMMENT '票档名称快照',
    `zone`              VARCHAR(32)    NOT NULL COMMENT '区域快照',
    `unit_price`        DECIMAL(10, 2) NOT NULL COMMENT '单价快照',
    `quantity`          INT            NOT NULL COMMENT '数量',
    `total_amount`  DECIMAL(10, 2) NOT NULL COMMENT '订单总金额（元）',
    `status`        TINYINT        NOT NULL DEFAULT 0 COMMENT '状态 0-待支付 1-已支付 2-已取消（含超时）',
    `expire_time`   DATETIME       NOT NULL COMMENT '支付截止时间',
    `pay_time`      DATETIME       NULL COMMENT '支付时间',
    `create_time`   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`order_no`),
    KEY `idx_user_status_time` (`user_id`, `status`, `create_time`),
    KEY `idx_status_expire` (`status`, `expire_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '演出订单表';

# 5. 演出订单座位关联表
CREATE TABLE `t_show_order_seat`
(
    `order_no` BIGINT         NOT NULL COMMENT '关联订单号(t_show_order.order_no)',
    `seat_id`  BIGINT         NOT NULL COMMENT '关联演出座位ID(t_show_seat.id)',
    `zone`     VARCHAR(32)    NOT NULL COMMENT '区域快照',
    `seat_no`  INT            NOT NULL COMMENT '座位号快照（区域内连续编号）',
    `price`    DECIMAL(10, 2) NOT NULL COMMENT '票价（元）',
    PRIMARY KEY (`order_no`, `seat_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '演出订单座位关联表';

CREATE TABLE `t_play_order`
(
    `order_no`     BIGINT         NOT NULL COMMENT '订单号（雪花算法，主键）',
    `user_id`      BIGINT         NOT NULL COMMENT '关联用户ID(t_user.id)',
    `item_id`      BIGINT         NOT NULL COMMENT '关联玩乐项目ID(t_play_item.id)',
    `item_title`   VARCHAR(64)    NOT NULL COMMENT '项目名称快照',
    `item_address` VARCHAR(255)   NOT NULL COMMENT '项目地址快照',
    `unit_price`   DECIMAL(10, 2) NOT NULL COMMENT '单价快照',
    `quantity`     INT            NOT NULL COMMENT '数量',
    `total_amount` DECIMAL(10, 2) NOT NULL COMMENT '订单总金额（元）',
    `status`       TINYINT        NOT NULL DEFAULT 0 COMMENT '状态 0-待支付 1-已支付 2-已取消（含超时）',
    `expire_time`  DATETIME       NOT NULL COMMENT '支付截止时间',
    `pay_time`     DATETIME       NULL COMMENT '支付时间',
    `create_time`  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`order_no`),
    KEY `idx_user_status_time` (`user_id`, `status`, `create_time`),
    KEY `idx_status_expire` (`status`, `expire_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '玩乐订单表';
