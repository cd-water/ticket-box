# 1. 玩乐项目表
CREATE TABLE `t_play_item`
(
    `id`          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '玩乐项目ID（主键）',
    `title`       VARCHAR(64)    NOT NULL COMMENT '项目名称',
    `cover`       VARCHAR(255)   NOT NULL COMMENT '封面URL',
    `address`     VARCHAR(255)   NOT NULL COMMENT '地址',
    `description` VARCHAR(2000)  NOT NULL COMMENT '项目简介',
    `price`       DECIMAL(10, 2) NOT NULL COMMENT '票价（元）',
    `stock`       INT            NOT NULL DEFAULT 0 COMMENT '剩余票数',
    `status`      TINYINT        NOT NULL DEFAULT 1 COMMENT '状态 0-下架 1-上架',
    `create_time` DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '玩乐项目表';

# 2. 玩乐订单表
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
    `status`       TINYINT        NOT NULL DEFAULT 0 COMMENT '状态 0-待支付 1-已支付 2-已取消 3-已超时',
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
