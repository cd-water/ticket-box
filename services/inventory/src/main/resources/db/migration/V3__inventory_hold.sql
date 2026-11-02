CREATE TABLE `t_hold`
(
    `order_no`     BIGINT      NOT NULL COMMENT '订单号（业务主键，预留幂等键）',
    `product_type` VARCHAR(16) NOT NULL COMMENT '商品类型 MOVIE/SHOW/PLAY',
    `product_id`   BIGINT      NOT NULL COMMENT '排场ID/演出ID/玩乐项目ID',
    `zone`         VARCHAR(32) NULL COMMENT '演出区域（仅演出）',
    `quantity`     INT         NOT NULL COMMENT '预留数量',
    `status`       TINYINT     NOT NULL DEFAULT 0 COMMENT '状态 0-已预留 1-已确认 2-已释放',
    `expire_time`  DATETIME    NOT NULL COMMENT '预留过期时间',
    `create_time`  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`order_no`),
    KEY `idx_status_expire` (`status`, `expire_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '统一库存预留表';
