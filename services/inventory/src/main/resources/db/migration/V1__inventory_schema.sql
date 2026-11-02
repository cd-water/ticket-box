CREATE TABLE `t_seat`
(
    `id`          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '座位ID（主键）',
    `hall_id`     BIGINT   NOT NULL COMMENT '关联影厅ID(t_hall.id)',
    `row_num`     INT      NOT NULL COMMENT '排号',
    `col_num`     INT      NOT NULL COMMENT '列号',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_hall_row_col` (`hall_id`, `row_num`, `col_num`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '座位表';


CREATE TABLE `t_screening_seat`
(
    `id`               BIGINT   NOT NULL AUTO_INCREMENT COMMENT '排场座位ID（主键）',
    `screening_id`     BIGINT   NOT NULL COMMENT '关联排场ID(t_screening.id)',
    `seat_id`          BIGINT   NOT NULL COMMENT '关联座位ID(t_seat.id)',
    `status`           TINYINT  NOT NULL DEFAULT 0 COMMENT '状态 0-可售 1-锁定 2-已售',
    `lock_order_no`    BIGINT   NULL COMMENT '锁定电影订单号(t_movie_order.order_no)',
    `lock_expire_time` DATETIME NULL COMMENT '锁定过期时间',
    `create_time`      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_screening_seat` (`screening_id`, `seat_id`),
    KEY `idx_screening_status` (`screening_id`, `status`),
    KEY `idx_lock_order` (`lock_order_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '排场座位表';


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

CREATE TABLE `t_play_stock`
(
    `item_id` BIGINT NOT NULL COMMENT '玩乐项目ID',
    `stock`   INT    NOT NULL DEFAULT 0 COMMENT '剩余票数',
    PRIMARY KEY (`item_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '玩乐库存表';
