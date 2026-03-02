CREATE TABLE admin
(
    id          BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '管理员id，主键',
    username    VARCHAR(32)  NOT NULL COMMENT '用户名',
    password    VARCHAR(255) NOT NULL COMMENT '密码，BCrypt加密',
    top         TINYINT      NOT NULL DEFAULT 0 COMMENT '权限，0-普通管理员，1-超级管理员',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_username (username)
) COMMENT '管理员表';

CREATE TABLE user
(
    id          BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '用户id，主键',
    phone       VARCHAR(11) NOT NULL COMMENT '手机号',
    password    VARCHAR(255) COMMENT '密码，BCrypt加密',
    nickname    VARCHAR(32) NOT NULL COMMENT '昵称',
    avatar      VARCHAR(255) COMMENT '头像URL',
    gender      TINYINT     NOT NULL DEFAULT 0 COMMENT '性别，0-未知，1-男，2-女',
    birthday    DATE COMMENT '生日',
    introduce   VARCHAR(128) COMMENT '简介',
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_phone (phone)
) COMMENT '用户表';

CREATE TABLE user_follow
(
    id          BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '用户关注关系id，主键',
    follower_id BIGINT UNSIGNED NOT NULL COMMENT '关联关注者id',
    followee_id BIGINT UNSIGNED NOT NULL COMMENT '关联被关注者id',
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_follower_id_followee_id (follower_id, followee_id),
    INDEX idx_followee_id (followee_id)
) COMMENT '用户关注关系表';

CREATE TABLE movie
(
    id           BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '电影id，主键',
    name         VARCHAR(32)   NOT NULL COMMENT '名称',
    cover        VARCHAR(255)  NOT NULL COMMENT '封面URL',
    type         VARCHAR(32) COMMENT '类型，用/分隔',
    release_date DATE COMMENT '上映日期',
    duration     INT UNSIGNED COMMENT '时长（分钟）',
    description  VARCHAR(255) COMMENT '剧情简介',
    director     VARCHAR(32) COMMENT '导演',
    actors       VARCHAR(255) COMMENT '主演列表，用/分隔',
    rating       DECIMAL(3, 1) NOT NULL DEFAULT 0.0 COMMENT '评分（0-10）',
    status       TINYINT       NOT NULL DEFAULT 1 COMMENT '状态，0-下架，1-热映，2-待映',
    create_time  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '电影表';

CREATE TABLE review
(
    id          BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '影评id，主键',
    user_id     BIGINT UNSIGNED NOT NULL COMMENT '关联用户id',
    movie_id    BIGINT UNSIGNED NOT NULL COMMENT '关联电影id',
    content     VARCHAR(1000)   NOT NULL COMMENT '内容',
    parent_id   BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '父影评id（0表示顶级影评）',
    status      TINYINT         NOT NULL DEFAULT 0 COMMENT '状态，0-正常，1-违规',
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id_create_time (user_id, create_time),
    INDEX idx_movie_id_parent_id_create_time (movie_id, parent_id, create_time)
) COMMENT '影评表';

CREATE TABLE review_like
(
    id          BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '影评点赞id，主键',
    user_id     BIGINT UNSIGNED NOT NULL COMMENT '关联用户id',
    review_id   BIGINT UNSIGNED NOT NULL COMMENT '关联影评id',
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_id_review_id (user_id, review_id)
) COMMENT '影评点赞表';

CREATE TABLE cinema
(
    id          BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '影院id，主键',
    name        VARCHAR(63)    NOT NULL COMMENT '名称',
    address     VARCHAR(255)   NOT NULL COMMENT '地址',
    longitude   DECIMAL(10, 7) NOT NULL COMMENT '经度',
    latitude    DECIMAL(9, 7)  NOT NULL COMMENT '纬度',
    phone       VARCHAR(20)    NOT NULL COMMENT '联系电话',
    tags        VARCHAR(255)   NOT NULL COMMENT '标签，用英文逗号分隔',
    create_time DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '影院表';

CREATE TABLE screening
(
    id          BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '场次id，主键',
    movie_id    BIGINT UNSIGNED NOT NULL COMMENT '关联电影id',
    cinema_id   BIGINT UNSIGNED NOT NULL COMMENT '关联影院id',
    hall_name   VARCHAR(32)     NOT NULL COMMENT '影厅名称',
    row_count   INT UNSIGNED    NOT NULL COMMENT '排数',
    col_count   INT UNSIGNED    NOT NULL COMMENT '座数',
    start_time  DATETIME        NOT NULL COMMENT '开场时间',
    end_time    DATETIME        NOT NULL COMMENT '散场时间',
    price       DECIMAL(10, 2)  NOT NULL COMMENT '票价',
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_cinema_id_movie_id_start_time (cinema_id, movie_id, start_time)
) COMMENT '场次表';

CREATE TABLE `order`
(
    order_no     BIGINT UNSIGNED PRIMARY KEY NOT NULL COMMENT '订单号，主键',
    user_id      BIGINT UNSIGNED             NOT NULL COMMENT '关联用户id',
    screening_id BIGINT UNSIGNED             NOT NULL COMMENT '关联场次id',
    seat_info    VARCHAR(255)                NOT NULL COMMENT '座位编码：5_8,5_9',
    ticket_count INT UNSIGNED                NOT NULL COMMENT '购票数量',
    amount       DECIMAL(10, 2)              NOT NULL COMMENT '支付金额',
    status       TINYINT                     NOT NULL DEFAULT 0 COMMENT '状态，0-待支付，1-已支付，2-已取消',
    pay_time     DATETIME COMMENT '支付时间',
    cancel_time  DATETIME COMMENT '取消时间',
    create_time  DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id_create_time (user_id, create_time),
    INDEX idx_status_create_time (status, create_time)
) COMMENT '订单表';