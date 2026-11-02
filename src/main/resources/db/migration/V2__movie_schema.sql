# 1. 电影表
CREATE TABLE `t_movie`
(
    `id`           BIGINT        NOT NULL AUTO_INCREMENT COMMENT '电影ID（主键）',
    `title`        VARCHAR(64)   NOT NULL COMMENT '片名',
    `poster`       VARCHAR(255)  NOT NULL COMMENT '海报URL',
    `synopsis`     VARCHAR(2000) NOT NULL COMMENT '简介',
    `duration`     INT           NOT NULL DEFAULT 0 COMMENT '片长（分钟）',
    `release_date` DATETIME      NOT NULL COMMENT '上映时间',
    `status`       TINYINT       NOT NULL DEFAULT 1 COMMENT '状态 0-下架 1-上架',
    `create_time`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_status_release` (`status`, `release_date`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '电影表';

# 2. 类型表
CREATE TABLE `t_genre`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '类型ID（主键）',
    `name`        VARCHAR(32) NOT NULL COMMENT '类型名称',
    `sort_order`  INT         NOT NULL DEFAULT 0 COMMENT '排序（越小越靠前）',
    `status`      TINYINT     NOT NULL DEFAULT 1 COMMENT '状态 0-停用 1-启用',
    `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '类型表';

# 3. 电影类型关联表
CREATE TABLE `t_movie_genre`
(
    `movie_id` BIGINT NOT NULL COMMENT '关联电影ID(t_movie.id)',
    `genre_id` BIGINT NOT NULL COMMENT '关联类型ID(t_genre.id)',
    PRIMARY KEY (`movie_id`, `genre_id`),
    KEY `idx_genre` (`genre_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '电影类型关联表';

# 4. 人员表
CREATE TABLE `t_person`
(
    `id`          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '人员ID（主键）',
    `name`        VARCHAR(64)   NOT NULL COMMENT '姓名',
    `avatar`      VARCHAR(255)  NOT NULL DEFAULT '' COMMENT '头像URL',
    `intro`       VARCHAR(1000) NOT NULL DEFAULT '' COMMENT '简介',
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '人员表';

# 5. 电影人员关联表
CREATE TABLE `t_movie_person`
(
    `movie_id`   BIGINT      NOT NULL COMMENT '关联电影ID(t_movie.id)',
    `person_id`  BIGINT      NOT NULL COMMENT '关联人员ID(t_person.id)',
    `duty`       TINYINT     NOT NULL COMMENT '职责 1-导演 2-演员',
    `role_name`  VARCHAR(64) NOT NULL DEFAULT '' COMMENT '饰演角色（导演为空）',
    `sort_order` INT         NOT NULL DEFAULT 0 COMMENT '排序（越小越靠前）',
    PRIMARY KEY (`movie_id`, `person_id`, `duty`, `role_name`),
    KEY `idx_person` (`person_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '电影人员关联表';

# 6. 影评表
CREATE TABLE `t_movie_comment`
(
    `id`          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '影评ID（主键）',
    `movie_id`    BIGINT        NOT NULL COMMENT '关联电影ID(t_movie.id)',
    `user_id`     BIGINT        NOT NULL COMMENT '关联用户ID(t_user.id)',
    `root_id`     BIGINT        NOT NULL DEFAULT 0 COMMENT '关联根影评ID(t_movie_comment.id)，0-一级影评',
    `parent_id`   BIGINT        NOT NULL DEFAULT 0 COMMENT '关联父影评ID(t_movie_comment.id)，0-一级影评',
    `content`     VARCHAR(1000) NOT NULL COMMENT '影评内容',
    `like_count`  INT           NOT NULL DEFAULT 0 COMMENT '点赞数',
    `reply_count` INT           NOT NULL DEFAULT 0 COMMENT '回复数（仅根影评维护）',
    `status`      TINYINT       NOT NULL DEFAULT 1 COMMENT '状态 0-隐藏 1-正常 2-已删除',
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_movie_root_time` (`movie_id`, `root_id`, `create_time`),
    KEY `idx_root_time` (`root_id`, `create_time`),
    KEY `idx_movie_root_hot` (`movie_id`, `root_id`, `like_count`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '影评表';

# 7. 影评点赞关联表
CREATE TABLE `t_movie_comment_like`
(
    `comment_id` BIGINT NOT NULL COMMENT '关联影评ID(t_movie_comment.id)',
    `user_id`    BIGINT NOT NULL COMMENT '关联用户ID(t_user.id)',
    PRIMARY KEY (`comment_id`, `user_id`),
    KEY `idx_user` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '影评点赞关联表';

# 8. 影院表
CREATE TABLE `t_cinema`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '影院ID（主键）',
    `name`        VARCHAR(64)  NOT NULL COMMENT '影院名称',
    `address`     VARCHAR(255) NOT NULL COMMENT '影院地址',
    `phone`       VARCHAR(20)  NOT NULL COMMENT '联系电话',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态 0-停用 1-启用',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '影院表';

# 9. 影厅表
CREATE TABLE `t_hall`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '影厅ID（主键）',
    `cinema_id`   BIGINT      NOT NULL COMMENT '关联影院ID(t_cinema.id)',
    `name`        VARCHAR(32) NOT NULL COMMENT '影厅名称',
    `status`      TINYINT     NOT NULL DEFAULT 1 COMMENT '状态 0-停用 1-启用',
    `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_cinema_name` (`cinema_id`, `name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '影厅表';

# 10. 座位表
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

# 11. 排场表
CREATE TABLE `t_screening`
(
    `id`          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '排场ID（主键）',
    `cinema_id`   BIGINT         NOT NULL COMMENT '关联影院ID(t_cinema.id)',
    `hall_id`     BIGINT         NOT NULL COMMENT '关联影厅ID(t_hall.id)',
    `movie_id`    BIGINT         NOT NULL COMMENT '关联电影ID(t_movie.id)',
    `start_time`  DATETIME       NOT NULL COMMENT '开场时间',
    `end_time`    DATETIME       NOT NULL COMMENT '散场时间',
    `price`       DECIMAL(10, 2) NOT NULL COMMENT '票价（元）',
    `status`      TINYINT        NOT NULL DEFAULT 1 COMMENT '状态 0-取消 1-正常',
    `create_time` DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_cinema_movie_start` (`cinema_id`, `movie_id`, `start_time`),
    KEY `idx_cinema_start` (`cinema_id`, `start_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '排场表';

# 12. 排场座位表
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

# 13. 电影订单表
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
