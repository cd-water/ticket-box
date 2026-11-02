# 1. 演出表
CREATE TABLE `t_show`
(
    `id`            BIGINT        NOT NULL AUTO_INCREMENT COMMENT '演出ID（主键）',
    `title`         VARCHAR(64)   NOT NULL COMMENT '演出名称',
    `poster`        VARCHAR(255)  NOT NULL COMMENT '海报URL',
    `description`   VARCHAR(2000) NOT NULL COMMENT '演出简介',
    `duration`      INT           NOT NULL DEFAULT 0 COMMENT '时长（分钟，约数）',
    `venue_name`    VARCHAR(64)   NOT NULL COMMENT '场馆名称',
    `venue_address` VARCHAR(255)  NOT NULL COMMENT '场馆地址',
    `start_time`    DATETIME      NOT NULL COMMENT '开场时间',
    `status`        TINYINT       NOT NULL DEFAULT 1 COMMENT '状态 0-下架 1-上架',
    `create_time`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '演出表';

# 2. 演出票档表
CREATE TABLE `t_show_ticket_type`
(
    `id`          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '演出票档ID（主键）',
    `show_id`     BIGINT         NOT NULL COMMENT '关联演出ID(t_show.id)',
    `name`        VARCHAR(64)    NOT NULL COMMENT '票档名称',
    `zone`        VARCHAR(32)    NOT NULL COMMENT '区域',
    `price`       DECIMAL(10, 2) NOT NULL COMMENT '票价（元）',
    `sort_order`  INT            NOT NULL DEFAULT 0 COMMENT '排序（越小越靠前）',
    `status`      TINYINT        NOT NULL DEFAULT 1 COMMENT '状态 0-停售 1-可售',
    `create_time` DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_show_zone` (`show_id`, `zone`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '演出票档表';

# 3. 演出座位表
CREATE TABLE `t_show_seat`
(
    `id`               BIGINT      NOT NULL AUTO_INCREMENT COMMENT '演出座位ID（主键）',
    `show_id`          BIGINT      NOT NULL COMMENT '关联演出ID(t_show.id)',
    `zone`             VARCHAR(32) NOT NULL COMMENT '区域',
    `seat_no`          INT         NOT NULL COMMENT '座位号（区域内连续编号）',
    `status`           TINYINT     NOT NULL DEFAULT 0 COMMENT '状态 0-可售 1-锁定 2-已售',
    `lock_order_no`    BIGINT      NULL COMMENT '锁定演出订单号(t_show_order.order_no)',
    `lock_expire_time` DATETIME    NULL COMMENT '锁定过期时间',
    `create_time`      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_show_seat` (`show_id`, `zone`, `seat_no`),
    KEY `idx_show_zone_status_seat` (`show_id`, `zone`, `status`, `seat_no`),
    KEY `idx_lock_order` (`lock_order_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '演出座位表';

# 4. 演出订单表
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
