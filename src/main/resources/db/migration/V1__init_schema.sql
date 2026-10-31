CREATE TABLE `t_user`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID（主键）',
    `phone`       VARCHAR(20)  NOT NULL COMMENT '手机号',
    `password`    VARCHAR(100) NULL COMMENT '密码（BCrypt）',
    `nickname`    VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '昵称（静默注册自动生成）',
    `avatar`      VARCHAR(255) NOT NULL DEFAULT '' COMMENT '头像URL',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表';

CREATE TABLE `t_admin`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '管理员ID（主键）',
    `username`    VARCHAR(20)  NOT NULL COMMENT '账号',
    `password`    VARCHAR(100) NOT NULL COMMENT '密码（BCrypt）',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表';

CREATE TABLE `t_movie`
(
    `id`           BIGINT        NOT NULL AUTO_INCREMENT COMMENT '电影ID（主键）',
    `title`        VARCHAR(64)   NOT NULL COMMENT '片名',
    `poster`       VARCHAR(255)  NOT NULL DEFAULT '' COMMENT '海报URL',
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

CREATE TABLE `t_genre`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '电影类型ID（主键）',
    `name`        VARCHAR(32) NOT NULL COMMENT '电影类型名称',
    `sort_order`  INT         NOT NULL DEFAULT 0 COMMENT '排序（越小越靠前）',
    `status`      TINYINT     NOT NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '电影类型表';

CREATE TABLE `t_movie_genre`
(
    `movie_id` BIGINT NOT NULL COMMENT '关联电影ID(t_movie.id)',
    `genre_id` BIGINT NOT NULL COMMENT '关联电影类型ID(t_genre.id)',
    PRIMARY KEY (`movie_id`, `genre_id`),
    KEY `idx_genre` (`genre_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '电影类型关联表';

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

CREATE TABLE `t_movie_crew`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT COMMENT '电影演职人员ID（主键）',
    `movie_id`   BIGINT      NOT NULL COMMENT '关联电影ID(t_movie.id)',
    `person_id`  BIGINT      NOT NULL COMMENT '关联人员ID(t_person.id)',
    `duty`       TINYINT     NOT NULL COMMENT '职责 1-导演 2-演员',
    `role_name`  VARCHAR(64) NOT NULL DEFAULT '' COMMENT '饰演角色（导演为空）',
    `sort_order` INT         NOT NULL DEFAULT 0 COMMENT '排序（越小越靠前）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_movie_person_duty` (`movie_id`, `person_id`, `duty`),
    KEY `idx_person` (`person_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '电影演职人员关联表';

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
    KEY `idx_movie_root_hot` (`movie_id`, `root_id`, `status`, `like_count`, `id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '影评表';

CREATE TABLE `t_movie_comment_like`
(
    `comment_id` BIGINT NOT NULL COMMENT '关联影评ID(t_movie_comment.id)',
    `user_id`    BIGINT NOT NULL COMMENT '关联用户ID(t_user.id)',
    PRIMARY KEY (`comment_id`, `user_id`),
    KEY `idx_user` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '影评点赞关联表';