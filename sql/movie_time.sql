/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80045 (8.0.45)
 Source Host           : localhost:3306
 Source Schema         : movie_time

 Target Server Type    : MySQL
 Target Server Version : 80045 (8.0.45)
 File Encoding         : 65001

 Date: 25/02/2026 16:48:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`
(
    `id`          bigint UNSIGNED                                               NOT NULL AUTO_INCREMENT COMMENT '管理员id，主键',
    `username`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '用户名',
    `password`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码，BCrypt加密',
    `top`         tinyint                                                       NOT NULL DEFAULT 0 COMMENT '权限，0-普通管理员，1-超级管理员',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_username` (`username` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin`
VALUES (1, 'admin', '$2a$10$IKO68k5YLN2t2qof69NTs.NcjuBXszxYAQ6KPK9wvoImBNRGga7k2', 1, '2026-02-25 10:29:57',
        '2026-02-25 16:12:25');
INSERT INTO `admin`
VALUES (2, 'admin01', '$2a$10$HsBeL1Tpyk9zGBq97XWpze.4SUxZMG7mfFttWnyJadB3QT3Qxhfcu', 0, '2026-02-25 10:30:37',
        '2026-02-25 11:29:05');
INSERT INTO `admin`
VALUES (3, 'admin02', '$2a$10$Avxtu9v9K8HS/sSStaMtvOVDIZOvXPWV/n7oYY1oo/ic19rgsF.Tq', 0, '2026-02-25 10:30:47',
        '2026-02-25 11:28:53');
INSERT INTO `admin`
VALUES (4, 'admin03', '$2a$10$TNcq0VR.TBtONdMceTOkzegOgY9K97CtpwExC2KgYyF.dhgvrIosy', 0, '2026-02-25 10:30:57',
        '2026-02-25 10:30:57');

-- ----------------------------
-- Table structure for cinema
-- ----------------------------
DROP TABLE IF EXISTS `cinema`;
CREATE TABLE `cinema`
(
    `id`          bigint UNSIGNED                                               NOT NULL AUTO_INCREMENT COMMENT '影院id，主键',
    `name`        varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '名称',
    `address`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地址',
    `longitude`   decimal(10, 7)                                                NOT NULL COMMENT '经度',
    `latitude`    decimal(9, 7)                                                 NOT NULL COMMENT '纬度',
    `phone`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '联系电话',
    `tags`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签，用英文逗号分隔',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '影院表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cinema
-- ----------------------------
INSERT INTO `cinema`
VALUES (1, '武汉万达影城（汉街店）', '武汉市武昌区楚河汉街第三街区', 114.3416000, 30.5517000, '027-87318888',
        'IMAX,杜比全景声,4DX,RealD', '2026-02-25 11:51:25', '2026-02-25 11:51:25');
INSERT INTO `cinema`
VALUES (2, '武汉武商摩尔影城', '武汉市江汉区解放大道688号武商MALL·7楼', 114.2732000, 30.5901000, '027-85857777',
        'IMAX,杜比影院,4K激光', '2026-02-25 11:51:25', '2026-02-25 11:51:25');
INSERT INTO `cinema`
VALUES (3, '武汉百丽宫影城（壹方店）', '武汉市江岸区中山大道1515号壹方购物中心4楼', 114.3025000, 30.5987000,
        '027-82286666', 'IMAX,激光放映,Dolby Cinema', '2026-02-25 11:51:25', '2026-02-25 11:51:25');
INSERT INTO `cinema`
VALUES (4, '武汉金逸影城（光谷店）', '武汉市洪山区光谷广场步行街4楼', 114.4156000, 30.5087000, '027-87695555',
        'IMAX,RealD,4DX', '2026-02-25 11:51:25', '2026-02-25 11:51:25');
INSERT INTO `cinema`
VALUES (5, '武汉CGV影城（永旺梦乐城店）', '武汉市东西湖区金银潭大道1号永旺梦乐城3楼', 114.1374000, 30.6475000,
        '027-83238888', 'IMAX,4DX,ScreenX,STARIUM', '2026-02-25 11:51:25', '2026-02-25 11:51:25');
INSERT INTO `cinema`
VALUES (6, '武汉卢米埃影城（凯德1818店）', '武汉市武昌区中北路109号凯德1818中心7楼', 114.3389000, 30.5562000,
        '027-87119999', 'LD,杜比全景声,HEPA', '2026-02-25 11:51:25', '2026-02-25 11:51:25');
INSERT INTO `cinema`
VALUES (7, '武汉博纳国际影城（银泰店）', '武汉市江汉区中山大道818号银泰创意城8楼', 114.2798000, 30.5865000,
        '027-85553333', 'IMAX,激光放映,RealD', '2026-02-25 11:51:25', '2026-02-25 11:51:25');
INSERT INTO `cinema`
VALUES (8, '武汉万象影城（万象城店）', '武汉市武昌区中山大道258号万象城5楼', 114.3465000, 30.5598000, '027-87326666',
        'IMAX,杜比全景声,CINITY', '2026-02-25 11:51:25', '2026-02-25 11:51:25');
INSERT INTO `cinema`
VALUES (9, '武汉中影国际影城（光谷天地店）', '武汉市洪山区光谷大道光谷天地3楼', 114.4256000, 30.4789000, '027-87402222',
        'IMAX,中国巨幕,RealD', '2026-02-25 11:51:25', '2026-02-25 11:51:25');
INSERT INTO `cinema`
VALUES (10, '武汉寰映影城（恒隆广场店）', '武汉市江京区京汉大道668号恒隆广场6楼', 114.2876000, 30.5945000, '027-85518888',
        'IMAX激光,杜比全景声,PRIME', '2026-02-25 11:51:25', '2026-02-25 11:51:25');
INSERT INTO `cinema`
VALUES (11, '武汉百老汇影城（中心书城店）', '武汉市武昌区丁字桥路中南国际汇7楼', 114.3325000, 30.5438000, '027-88079999',
        'IMAX,4K激光,Dolby Atmos', '2026-02-25 11:51:25', '2026-02-25 11:51:25');
INSERT INTO `cinema`
VALUES (12, '武汉沃美影城（凯德西城店）', '武汉市硚口区古田二路凯德西城5楼', 114.2134000, 30.5886000, '027-83867777',
        'IMAX,RealD,儿童厅', '2026-02-25 11:51:25', '2026-02-25 11:51:25');

-- ----------------------------
-- Table structure for movie
-- ----------------------------
DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie`
(
    `id`           bigint UNSIGNED                                               NOT NULL AUTO_INCREMENT COMMENT '电影id，主键',
    `name`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '名称',
    `cover`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '封面URL',
    `type`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '类型，用/分隔',
    `release_date` date                                                          NULL     DEFAULT NULL COMMENT '上映日期',
    `duration`     int UNSIGNED                                                  NULL     DEFAULT NULL COMMENT '时长（分钟）',
    `description`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '剧情简介',
    `director`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '导演',
    `actors`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '主演列表，用/分隔',
    `rating`       decimal(3, 1)                                                 NOT NULL DEFAULT 0.0 COMMENT '评分（0-10）',
    `status`       tinyint                                                       NOT NULL DEFAULT 1 COMMENT '状态，0-下架，1-热映，2-待映',
    `create_time`  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 27
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '电影表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of movie
-- ----------------------------
INSERT INTO `movie`
VALUES (1, '流浪地球3', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '科幻/冒险', '2026-01-22', 120,
        '太阳即将毁灭，人类开启流浪地球计划，延续文明火种', '郭帆', '吴京/刘德华/李雪健', 9.2, 1, '2026-02-25 10:44:51',
        '2026-02-25 11:07:22');
INSERT INTO `movie`
VALUES (2, '满江红', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '剧情/悬疑', '2026-01-25', 140,
        '南宋绍兴年间，岳飞死后四年，秦桧率兵与金国会谈前夕发生的一桩谜案', '张艺谋', '沈腾/易烊千玺/张译', 8.5, 1,
        '2026-02-25 10:44:51', '2026-02-25 11:06:44');
INSERT INTO `movie`
VALUES (3, '无名', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动作/剧情', '2026-01-20', 125,
        '影片通过对上世纪二十年代开始奋斗在上海的中国共产党领导下的中共特科', '程耳', '梁朝伟/王一博/周迅', 8.1, 1,
        '2026-02-25 10:44:51', '2026-02-25 11:06:47');
INSERT INTO `movie`
VALUES (4, '熊出没·伴我\"熊芯\"', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动画/喜剧', '2026-01-18', 95,
        '当神秘的外星机器人突然降临森林，熊大熊二和光头强意外发现了一个巨大阴谋', '林永长/邵和麟', '谭笑/张伟/张秉君',
        8.8, 1, '2026-02-25 10:44:51', '2026-02-25 11:06:42');
INSERT INTO `movie`
VALUES (5, '深海', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动画/奇幻', '2026-01-15', 110,
        '一位少女在神秘的深海世界中，探索一段关于救赎与成长的奇妙旅程', '田晓鹏', '苏鑫/王栋/滕家兴', 8.3, 1,
        '2026-02-25 10:44:51', '2026-02-25 11:06:40');
INSERT INTO `movie`
VALUES (6, '球2：闪电五连鞭', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '喜剧', '2026-01-28', 100,
        '马小军和朋友们在火星上的一场爆笑冒险', '周星驰', '周星驰/吴孟达/林允', 7.9, 1, '2026-02-25 10:44:51',
        '2026-02-25 11:06:37');
INSERT INTO `movie`
VALUES (7, '阿凡达：水之道', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '科幻/动作', '2026-01-10', 192,
        '杰克和奈蒂莉组建了家庭，他们的孩子也逐渐长大，为这个家庭带来了许多欢乐', '詹姆斯·卡梅隆',
        '萨  ·沃辛顿/佐伊·索尔达娜/西格妮·韦弗', 9.0, 1, '2026-02-25 10:44:51', '2026-02-25 11:06:34');
INSERT INTO `movie`
VALUES (8, '黑豹2', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动作/科幻', '2026-01-12', 161,
        '瓦坎达国王特查拉去世后，苏睿试图继续保护她的国家', '瑞恩·库格勒', '莱蒂希娅·赖特/露皮塔·尼永奥/安吉拉·贝塞特',
        8.2, 1, '2026-02-25 10:44:51', '2026-02-25 11:06:31');
INSERT INTO `movie`
VALUES (9, '壮志凌云2：独行侠', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动作/剧情', '2026-01-08', 131,
        '彼得·米切尔上校作为一名顶尖的海军飞行员，他在职业生涯中面临着新的挑战', '约瑟夫·科辛斯基',
        '汤姆·克鲁斯/詹妮弗·康纳利/迈尔斯·泰勒', 8.7, 1, '2026-02-25 10:44:51', '2026-02-25 11:06:29');
INSERT INTO `movie`
VALUES (10, '小黄人大眼萌2', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动画/喜剧', '2026-01-05', 87,
        '格鲁的成长故事，以及他与 minions 的初次相遇', '凯尔·巴尔达', '史蒂夫·卡瑞尔/皮埃尔·柯芬/艾伦· 巴金', 8.4, 1,
        '2026-02-25 10:44:51', '2026-02-25 11:06:50');
INSERT INTO `movie`
VALUES (11, '沙丘2', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '科幻/冒险', '2026-03-01', 166,
        '保罗·厄崔迪与契尼和弗雷曼人联手，开启了对致幻剂和宇宙控制权的征途之路', '丹尼斯·维伦纽瓦',
        '提莫西·查  梅/赞达亚/丽贝卡·弗格森', 0.0, 2, '2026-02-25 10:47:02', '2026-02-25 10:53:35');
INSERT INTO `movie`
VALUES (12, '死侍3', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动作/喜剧', '2026-04-15', 120,
        '死侍与金刚狼的首次联手，漫威多元宇宙的全新冒险', '肖恩·利维', '瑞安·雷诺兹/休·杰克曼/艾玛·科林', 0.0, 2,
        '2026-02-25 10:47:02', '2026-02-25 10:53:35');
INSERT INTO `movie`
VALUES (13, '哥斯拉大战金刚2', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动作/科幻', '2026-03-29', 115,
        '哥斯拉和金刚两大泰坦巨兽再次联手，对抗隐藏在世界中的巨大威胁', '亚当·温加德',
        '丽贝卡·豪尔/布莱恩·泰里·亨利/丹·史蒂文斯', 0.0, 2, '2026-02-25 10:47:02', '2026-02-25 10:53:35');
INSERT INTO `movie`
VALUES (14, '疯狂麦克斯：弗瑞奥萨', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动作/冒险', '2026-05-24',
        148, '弗瑞奥萨的起源故事，讲述她如何在末日废土中成长为一位强大的战士', '乔治·米勒',
        '安雅·泰  -乔伊/克里斯·海姆斯沃斯/汤姆·伯克', 0.0, 2, '2026-02-25 10:47:02', '2026-02-25 10:53:35');
INSERT INTO `movie`
VALUES (15, '加菲猫家族', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动画/喜剧', '2026-05-16', 101,
        '加菲猫与失散多年的父亲重逢，开启了一段温馨搞笑的家庭冒险', '马克·因戴利',
        '克里斯·帕拉特/塞缪尔·杰克逊/汉娜·沃丁厄姆', 0.0, 2, '2026-02-25 10:47:02', '2026-02-25 10:53:35');
INSERT INTO `movie`
VALUES (16, '头脑特工队2', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动画/冒险', '2026-06-14', 96,
        '莱莉进入青春期后，新的情绪开始出现，给大脑总部带来了翻天覆地的变化', '凯尔西·曼', '艾米·波勒/玛 雅·霍克/乔伊·金',
        0.0, 2, '2026-02-25 10:47:02', '2026-02-25 10:53:35');
INSERT INTO `movie`
VALUES (17, '碟中谍8：最终清算', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动作/惊悚', '2026-05-23', 160,
        '伊森·亨特和他的团队将面对前所未有的挑战，完成最终的使命', '克里斯托弗·麦奎里',
        '汤姆·克鲁  /海莉·阿特维尔/庞·克莱门捷夫', 0.0, 2, '2026-02-25 10:47:02', '2026-02-25 10:53:35');
INSERT INTO `movie`
VALUES (18, '角斗士2', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动作/剧情', '2026-07-04', 150,
        '卢修斯继承了马克西姆斯的遗志，在罗马竞技场中为自由而战', '雷德利·斯科特',
        '保罗·梅斯卡尔/佩德罗·帕 斯卡/丹泽尔·华盛顿', 0.0, 2, '2026-02-25 10:47:02', '2026-02-25 10:53:35');
INSERT INTO `movie`
VALUES (19, '海洋奇缘2', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动画/奇幻', '2026-06-27', 100,
        '莫阿娜开启新的航海冒险，探索大洋洲的古老传说', '小托马斯·卡卢姆', '奥丽依·卡拉瓦霍/道恩·强森', 0.0, 2,
        '2026-02-25 10:47:02', '2026-02-25 10:53:35');
INSERT INTO `movie`
VALUES (20, '魔法奇缘2：解除魔法', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '奇幻/喜剧', '2026-05-30', 119,
        '吉赛尔公主为了拯救家庭，必须在理想化的童话世界和现实世界之间做出选择', '亚当·山克曼',
        '艾米·亚当斯/帕特里克·德姆西/玛雅·鲁道夫', 0.0, 2, '2026-02-25 10:47:02', '2026-02-25 10:53:35');
INSERT INTO `movie`
VALUES (21, '战狼2', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动作/战争', '2025-07-27', 123,
        '冷锋被卷入了一场非洲国家的叛乱，他本可以安全撤离，但却无法忘记对军人的誓言', '吴京', '吴京/弗兰克·格 里罗/吴刚',
        7.2, 0, '2026-02-25 10:48:22', '2026-02-25 10:53:35');
INSERT INTO `movie`
VALUES (22, '复仇者联盟4：终局之战', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动作/科幻', '2025-04-24',
        181, '灭霸消灭宇宙一半生命后，幸存的复仇者们必须团结一致，逆转乾坤', '安东尼·罗素/乔·罗素',
        '小罗伯特·唐尼/克里斯·埃文斯/马克·鲁法洛', 8.5, 0, '2026-02-25 10:48:22', '2026-02-25 10:53:35');
INSERT INTO `movie`
VALUES (23, '你好，李焕英', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '喜剧/剧情', '2025-02-12', 128,
        '2001年的某一天，刚刚考上大学的贾晓玲意外穿越回到了1981年，遇到了年轻时的母亲', '贾玲', '贾玲/ 张小斐/沈腾', 8.1,
        0, '2026-02-25 10:48:22', '2026-02-25 10:53:35');
INSERT INTO `movie`
VALUES (24, '哪吒之魔童降世', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '动画/奇幻', '2025-07-26', 110,
        '天地灵气孕育的混元珠被元始天尊炼化为灵珠和魔丸，魔丸托生为哪吒', '饺子', '吕艳婷/囧森瑟夫/瀚  ', 8.7, 0,
        '2026-02-25 10:48:22', '2026-02-25 10:53:35');
INSERT INTO `movie`
VALUES (25, '流浪地球', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '科幻/冒险', '2025-02-05', 125,
        '太阳即将毁灭，人类在地球表面建造出巨大的推进器，寻找新的家园', '郭帆', '屈楚萧/吴京/李光洁', 7.9, 0,
        '2026-02-25 10:48:22', '2026-02-25 10:53:35');
INSERT INTO `movie`
VALUES (26, '唐人街探案3', 'http://127.0.0.1:9000/movie-time/default-avatar.png', '喜剧/悬疑', '2025-02-12', 136,
        '继曼谷、纽约之后，唐仁和秦福来到东京，本想参加婚礼的他们却被卷入了一场谋杀案', '陈思诚', '王宝强/刘昊然/张子枫',
        6.8, 0, '2026-02-25 10:49:26', '2026-02-25 10:53:35');

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`
(
    `order_no`     bigint UNSIGNED                                               NOT NULL COMMENT '订单号，主键',
    `user_id`      bigint UNSIGNED                                               NOT NULL COMMENT '关联用户id',
    `screening_id` bigint UNSIGNED                                               NOT NULL COMMENT '关联场次id',
    `seat_info`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '座位编码：5_8,5_9',
    `ticket_count` int UNSIGNED                                                  NOT NULL COMMENT '购票数量',
    `amount`       decimal(10, 2)                                                NOT NULL COMMENT '支付金额',
    `status`       tinyint                                                       NOT NULL DEFAULT 0 COMMENT '状态，0-待支付，1-已支付，2-已取消',
    `pay_time`     datetime                                                      NULL     DEFAULT NULL COMMENT '支付时间',
    `cancel_time`  datetime                                                      NULL     DEFAULT NULL COMMENT '取消时间',
    `create_time`  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`order_no`) USING BTREE,
    INDEX `idx_user_id_create_time` (`user_id` ASC, `create_time` ASC) USING BTREE,
    INDEX `idx_status_create_time` (`status` ASC, `create_time` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order`
VALUES (20240574791684096, 1, 5, '5_8,5_9', 2, 136.00, 1, '2026-02-25 14:30:00', NULL, '2026-02-25 15:53:52',
        '2026-02-25 15:53:52');
INSERT INTO `order`
VALUES (20240574791684097, 2, 15, '3_5,3_6,3_7', 3, 174.00, 1, '2026-02-25 15:20:00', NULL, '2026-02-25 15:53:52',
        '2026-02-25 15:53:52');
INSERT INTO `order`
VALUES (20240574791684098, 3, 28, '6_10,6_11', 2, 150.00, 2, NULL, '2026-02-25 16:00:00', '2026-02-25 15:53:52',
        '2026-02-25 15:53:52');
INSERT INTO `order`
VALUES (20240574791684099, 4, 42, '4_5', 1, 58.00, 2, NULL, '2026-02-25 16:00:00', '2026-02-25 15:53:52',
        '2026-02-25 15:53:52');
INSERT INTO `order`
VALUES (20240574791684100, 5, 67, '7_12,7_13,7_14,7_15', 4, 292.00, 1, '2026-02-25 17:10:00', NULL,
        '2026-02-25 15:53:52', '2026-02-25 15:53:52');

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`
(
    `id`          bigint UNSIGNED                                                NOT NULL AUTO_INCREMENT COMMENT '影评id，主键',
    `user_id`     bigint UNSIGNED                                                NOT NULL COMMENT '关联用户id',
    `movie_id`    bigint UNSIGNED                                                NOT NULL COMMENT '关联电影id',
    `content`     varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
    `parent_id`   bigint UNSIGNED                                                NOT NULL DEFAULT 0 COMMENT '父影评id（0表示顶级影评）',
    `status`      tinyint                                                        NOT NULL DEFAULT 0 COMMENT '状态，0-正常，1-违规',
    `create_time` datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id_create_time` (`user_id` ASC, `create_time` ASC) USING BTREE,
    INDEX `idx_movie_id_parent_id_create_time` (`movie_id` ASC, `parent_id` ASC, `create_time` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '影评表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review
-- ----------------------------
INSERT INTO `review`
VALUES (1, 1, 1, '特效太震撼了！国产科幻的巅峰之作，每一帧都是壁纸级别的画质', 0, 0, '2026-02-25 11:00:33',
        '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (2, 2, 1, '剧情紧凑，情感真挚，刘德华的表演太有层次了，看到最后泪目', 0, 0, '2026-02-25 11:00:33',
        '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (3, 3, 1, '比第一部更宏大，世界观设定更完整，期待后续作品', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (4, 4, 1, '特效不错，但感觉节奏有点快，人物太多有点记不住', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (5, 5, 1, '中国科幻电影终于站起来了！票房大卖！', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (6, 6, 2, '张艺谋这次真的没让人失望，反转不断，全程无尿点', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (7, 7, 2, '沈腾居然能把正剧演得这么好，喜剧和悬疑的平衡恰到好处', 0, 0, '2026-02-25 11:00:33',
        '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (8, 8, 2, '剧情设计精妙，层层递进，最后朗诵满江红那段太燃了', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (9, 9, 2, '画面很美，但有些地方感觉还是有点刻意了', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (10, 10, 2, '易烊千玺的演技有进步，和沈腾的配合很默契', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (11, 1, 3, '程耳的镜头语言太有质感了，每一帧都是艺术', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (12, 2, 3, '梁朝伟依然是影帝级别的表演，眼神全是戏', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (13, 3, 3, '非线性叙事很有意思，但需要仔细看才能看懂', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (14, 4, 3, '王一博的表现超出预期，和梁朝伟的对手戏很有张力', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (15, 5, 3, '电影的氛围营造得太好了，压抑又华丽', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (16, 6, 4, '带孩子看的，我自己也看哭了，母爱真的太伟大了', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (17, 7, 4, '熊出没系列越做越好，这次的故事内核很温暖', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (18, 8, 4, '动画制作精良，剧情紧凑，大人小孩都能看', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (19, 9, 4, '每年春节必看，已经成传统了，这次没有失望', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (20, 10, 4, '比前几部都要好，情感真挚，值得二刷', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (21, 1, 5, '画面美得窒息，粒子水墨技术太牛了', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (22, 2, 5, '故事很治愈，但看完有点伤感，关于成长的代价', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (23, 3, 5, '技术层面是天花板级别的，画面每一帧都是艺术品', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (24, 4, 5, '剧情有点深奥，小朋友可能看不懂，更适合成年人', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (25, 5, 5, '国产动画的里程碑，希望能拿奖', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (26, 6, 6, '周星驰回归！依然是那个无厘头风格，笑点密集', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (27, 7, 6, '火星上的马家军太搞笑了，经典梗一个接一个', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (28, 8, 6, '情怀满分，但感觉创意有点重复之前的套路', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (29, 9, 6, '吴孟达的遗作，看得又笑又哭，怀念那个时代', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (30, 10, 6, '轻松解压的喜剧，适合周末放松', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (31, 1, 7, '视觉奇观！卡梅隆十三年的等待是值得的', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (32, 2, 7, '水下世界的呈现简直太震撼了，IMAX必看', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (33, 3, 7, '剧情相对简单，但世界观的构建太强大了', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (34, 4, 7, '技术层面依然是行业标杆，每一帧都在烧钱', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (35, 5, 7, '三个小时的时长完全感觉不到，沉浸感太强了', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (36, 6, 8, '致敬查德维克·博斯曼，整部电影的情感基调很哀伤', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (37, 7, 8, '苏睿的成长线写得很扎实，继承者担得起重任', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (38, 8, 8, '缺少了黑豹的气场，但作为续作已经尽力了', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (39, 9, 8, '海底王国纳摩的设定很酷，打斗场面精彩', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (40, 10, 8, 'MCU第四阶段比较优秀的一部了', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (41, 1, 9, '阿汤哥真的是拼命三郎，实景拍摄太震撼了', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (42, 2, 9, '战斗机空战的镜头太燃了，IMAX视听效果炸裂', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (43, 3, 9, '比第一部更好，情感和动作完美结合', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (44, 4, 9, '36年后的续集，情怀拉满，依然热血沸腾', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (45, 5, 9, '年度最佳动作片之一，实至名归', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (46, 6, 10, '萌化了！小黄人无论什么时候都这么可爱', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (47, 7, 10, '格鲁的起源故事很有趣，了解了他的成长经历', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (48, 8, 10, '全年龄段的喜剧，笑点自然不尴尬', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (49, 9, 10, '动画色彩鲜艳，配乐也很棒，很适合带孩子看', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (50, 10, 10, '比第一部更精彩，剧情和笑点都有升级', 0, 0, '2026-02-25 11:00:33', '2026-02-25 11:00:33');
INSERT INTO `review`
VALUES (51, 3, 1, '完全同意！特别是空间站爆炸那段，看得我手心冒汗', 1, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (52, 7, 1, '第二遍看发现了很多细节，郭导确实用心了', 2, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (53, 2, 1, '我也觉得人物有点多，但看完就能理清了', 4, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (54, 8, 1, '期待第四部！希望不用再等四年', 3, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (55, 1, 2, '最后的全军朗诵满江红真的震撼，电影院里好多人都在背', 6, 0, '2026-02-25 11:10:24',
        '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (56, 5, 2, '我也发现了，沈腾这次真的收敛了很多，效果反而更好', 7, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (57, 3, 2, '后面反转确实有点多，但都在合理范围内', 9, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (58, 4, 2, '易烊千玺演技确实进步了，和沈腾的对手戏很有火花', 10, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (59, 6, 3, '程耳的审美真的很在线，灯光和构图都是教科书级别的', 11, 0, '2026-02-25 11:10:24',
        '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (60, 8, 3, '我看了三遍才完全看懂，细节太多了', 13, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (61, 2, 3, '王一博的眼神戏真的不错，和梁朝伟对视那场戏绝了', 14, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (62, 9, 3, '这种压抑的氛围贯穿全片，看完很久都出不来', 15, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (63, 2, 4, '我也是带孩子看的，结果我自己哭得比孩子还惨', 16, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (64, 5, 4, '每年春节都带孩子看，已经成习惯了', 19, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (65, 1, 4, '这次真的做得很好，国产动画加油', 18, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (66, 8, 4, '熊妈妈的形象塑造得很好，既坚强又温柔', 20, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (67, 4, 5, '技术确实强，但剧情感觉有点压抑', 22, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (68, 9, 5, '我带孩子看的孩子有点坐不住，确实更偏向成人向', 24, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (69, 3, 5, '粒子水墨真的太美了，希望能拿奥斯卡', 23, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (70, 6, 5, '最后那段参宿的梦境解析太深刻了', 21, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (71, 2, 6, '确实是情怀满分，看到吴孟达真的有点想哭', 27, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (72, 5, 6, '笑点密集，但感觉和之前的套路有点像', 28, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (73, 8, 6, '轻松解压不错，但还是期待周星驰能有点新突破', 30, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (74, 1, 6, '马家军的火星版还是很新奇的', 26, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (75, 6, 7, '巨幕厅看的，效果真的炸裂，值回票价', 31, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (76, 9, 7, '三个小时确实有点长，中间差点睡着', 34, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (77, 3, 7, '水下世界的构建太强大了，每一帧都是壁纸', 32, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (78, 7, 7, '剧情虽然简单但完整，重在视觉体验', 33, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (79, 1, 8, '博斯曼走了之后，整部电影确实少了点什么', 36, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (80, 4, 8, '纳摩的设定确实很酷，和海王不太一样', 38, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (81, 5, 8, '作为纪念作品已经很用心了', 35, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (82, 10, 8, '苏睿能担得起这个角色，期待后续', 37, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (83, 3, 9, '听说阿汤哥真的是自己开飞机拍戏，太拼了', 41, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (84, 7, 9, 'IMAX必须看，音效和画面都是顶级的', 42, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (85, 2, 9, '36年了，阿汤哥还是那么帅', 44, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (86, 8, 9, '比预期好很多，没有消费情怀', 43, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (87, 1, 10, '小黄人说话真的太好玩了，孩子全程笑个不停', 46, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (88, 5, 10, '知道了格鲁为什么会变成那样的人', 47, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (89, 3, 10, '大人看也完全不觉得幼稚，很用心', 49, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (90, 9, 10, '配乐真的很好听，片尾曲还在循环', 48, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (91, 4, 1, '郭导说会有第四部，已经在筹备了', 17, 0, '2026-02-25 11:10:24', '2026-02-25 11:21:49');
INSERT INTO `review`
VALUES (92, 6, 2, '背词那段真的是全场高潮', 25, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (93, 2, 3, '程耳的镜头语言真的值得反复研究', 12, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (94, 8, 4, '熊出没系列真是越来越稳了', 29, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (95, 1, 5, '看网上说粒子水墨技术申请了专利', 39, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (96, 10, 7, '等了13年，卡梅隆从来没让人失望', 40, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (97, 3, 9, '这才是真正的硬核动作片', 45, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (98, 7, 10, '小黄人真是永远的宝藏', 50, 0, '2026-02-25 11:10:24', '2026-02-25 11:10:24');
INSERT INTO `review`
VALUES (99, 10, 1, '情意满满，赞！', 0, 0, '2026-02-25 11:23:14', '2026-02-25 11:39:38');

-- ----------------------------
-- Table structure for review_like
-- ----------------------------
DROP TABLE IF EXISTS `review_like`;
CREATE TABLE `review_like`
(
    `id`          bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '影评点赞id，主键',
    `user_id`     bigint UNSIGNED NOT NULL COMMENT '关联用户id',
    `review_id`   bigint UNSIGNED NOT NULL COMMENT '关联影评id',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_user_id_review_id` (`user_id` ASC, `review_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 440
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '影评点赞表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review_like
-- ----------------------------
INSERT INTO `review_like`
VALUES (1, 1, 1, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (2, 2, 1, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (3, 3, 1, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (4, 4, 1, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (5, 5, 1, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (6, 6, 1, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (7, 7, 1, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (8, 8, 1, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (9, 9, 1, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (10, 1, 2, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (11, 2, 2, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (12, 3, 2, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (13, 4, 2, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (14, 5, 2, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (15, 6, 2, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (16, 7, 2, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (17, 8, 2, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (18, 1, 3, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (19, 2, 3, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (20, 3, 3, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (21, 4, 3, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (22, 5, 3, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (23, 6, 3, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (24, 7, 3, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (25, 2, 4, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (26, 3, 4, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (27, 4, 4, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (28, 5, 4, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (29, 1, 5, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (30, 2, 5, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (31, 3, 5, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (32, 4, 5, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (33, 5, 5, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (34, 6, 5, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (35, 7, 5, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (36, 8, 5, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (37, 9, 5, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (38, 10, 5, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (39, 1, 6, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (40, 2, 6, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (41, 3, 6, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (42, 4, 6, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (43, 5, 6, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (44, 6, 6, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (45, 7, 6, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (46, 8, 6, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (47, 2, 7, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (48, 3, 7, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (49, 4, 7, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (50, 5, 7, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (51, 6, 7, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (52, 1, 8, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (53, 2, 8, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (54, 3, 8, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (55, 4, 8, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (56, 5, 8, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (57, 6, 8, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (58, 7, 8, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (59, 8, 8, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (60, 9, 8, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (61, 1, 9, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (62, 2, 9, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (63, 3, 9, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (64, 2, 10, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (65, 3, 10, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (66, 4, 10, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (67, 5, 10, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (68, 6, 10, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (69, 1, 11, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (70, 2, 11, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (71, 3, 11, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (72, 4, 11, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (73, 5, 11, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (74, 6, 11, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (75, 7, 11, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (76, 1, 12, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (77, 2, 12, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (78, 3, 12, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (79, 4, 12, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (80, 5, 12, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (81, 6, 12, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (82, 7, 12, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (83, 8, 12, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (84, 9, 12, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (85, 2, 13, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (86, 3, 13, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (87, 4, 13, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (88, 1, 14, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (89, 2, 14, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (90, 3, 14, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (91, 4, 14, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (92, 5, 14, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (93, 1, 15, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (94, 2, 15, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (95, 3, 15, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (96, 4, 15, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (97, 5, 15, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (98, 6, 15, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (99, 1, 16, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (100, 2, 16, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (101, 3, 16, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (102, 4, 16, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (103, 5, 16, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (104, 6, 16, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (105, 7, 16, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (106, 8, 16, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (107, 9, 16, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (108, 10, 16, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (109, 2, 17, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (110, 3, 17, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (111, 4, 17, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (112, 5, 17, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (113, 6, 17, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (114, 1, 18, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (115, 2, 18, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (116, 3, 18, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (117, 4, 18, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (118, 2, 19, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (119, 3, 19, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (120, 4, 19, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (121, 1, 20, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (122, 2, 20, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (123, 3, 20, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (124, 4, 20, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (125, 5, 20, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (126, 6, 20, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (127, 7, 20, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (128, 1, 21, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (129, 2, 21, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (130, 3, 21, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (131, 4, 21, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (132, 5, 21, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (133, 6, 21, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (134, 1, 22, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (135, 2, 22, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (136, 3, 22, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (137, 2, 23, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (138, 3, 23, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (139, 4, 23, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (140, 5, 23, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (141, 6, 23, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (142, 7, 23, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (143, 1, 24, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (144, 2, 24, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (145, 2, 25, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (146, 3, 25, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (147, 4, 25, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (148, 5, 25, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (149, 1, 26, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (150, 2, 26, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (151, 3, 26, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (152, 4, 26, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (153, 5, 26, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (154, 6, 26, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (155, 7, 26, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (156, 8, 26, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (157, 1, 27, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (158, 2, 27, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (159, 3, 27, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (160, 4, 27, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (161, 5, 27, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (162, 6, 27, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (163, 2, 28, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (164, 3, 28, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (165, 1, 29, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (166, 2, 29, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (167, 3, 29, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (168, 4, 29, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (169, 5, 29, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (170, 6, 29, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (171, 7, 29, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (172, 8, 29, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (173, 9, 29, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (174, 10, 29, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (175, 2, 30, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (176, 3, 30, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (177, 1, 31, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (178, 2, 31, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (179, 3, 31, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (180, 4, 31, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (181, 5, 31, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (182, 6, 31, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (183, 7, 31, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (184, 8, 31, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (185, 1, 32, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (186, 2, 32, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (187, 3, 32, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (188, 4, 32, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (189, 5, 32, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (190, 6, 32, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (191, 7, 32, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (192, 8, 32, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (193, 9, 32, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (194, 2, 33, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (195, 3, 33, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (196, 4, 33, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (197, 1, 34, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (198, 2, 34, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (199, 2, 35, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (200, 3, 35, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (201, 4, 35, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (202, 5, 35, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (203, 6, 35, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (204, 7, 35, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (205, 8, 35, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (206, 9, 35, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (207, 10, 35, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (208, 1, 36, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (209, 2, 36, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (210, 3, 36, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (211, 4, 36, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (212, 5, 36, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (213, 2, 37, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (214, 3, 37, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (215, 4, 37, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (216, 5, 37, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (217, 6, 37, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (218, 1, 38, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (219, 2, 38, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (220, 2, 39, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (221, 3, 39, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (222, 4, 39, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (223, 5, 39, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (224, 6, 39, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (225, 7, 39, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (226, 1, 40, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (227, 2, 40, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (228, 3, 40, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (229, 1, 41, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (230, 2, 41, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (231, 3, 41, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (232, 4, 41, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (233, 5, 41, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (234, 6, 41, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (235, 7, 41, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (236, 8, 41, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (237, 9, 41, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (238, 10, 41, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (239, 2, 42, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (240, 3, 42, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (241, 4, 42, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (242, 5, 42, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (243, 6, 42, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (244, 7, 42, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (245, 8, 42, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (246, 1, 43, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (247, 2, 43, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (248, 3, 43, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (249, 4, 43, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (250, 5, 43, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (251, 2, 44, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (252, 3, 44, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (253, 4, 44, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (254, 1, 45, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (255, 2, 45, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (256, 3, 45, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (257, 4, 45, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (258, 5, 45, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (259, 6, 45, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (260, 7, 45, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (261, 8, 45, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (262, 9, 45, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (263, 2, 46, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (264, 3, 46, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (265, 4, 46, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (266, 5, 46, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (267, 6, 46, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (268, 7, 46, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (269, 8, 46, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (270, 9, 46, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (271, 10, 46, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (272, 1, 47, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (273, 2, 47, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (274, 3, 47, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (275, 2, 48, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (276, 3, 48, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (277, 4, 48, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (278, 1, 49, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (279, 2, 49, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (280, 3, 49, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (281, 4, 49, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (282, 5, 49, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (283, 1, 50, '2026-02-25 12:00:00');
INSERT INTO `review_like`
VALUES (284, 2, 50, '2026-02-25 12:05:00');
INSERT INTO `review_like`
VALUES (285, 3, 50, '2026-02-25 12:10:00');
INSERT INTO `review_like`
VALUES (286, 4, 50, '2026-02-25 12:15:00');
INSERT INTO `review_like`
VALUES (287, 5, 50, '2026-02-25 12:20:00');
INSERT INTO `review_like`
VALUES (288, 6, 50, '2026-02-25 12:25:00');
INSERT INTO `review_like`
VALUES (289, 7, 50, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (290, 8, 50, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (291, 9, 50, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (292, 1, 51, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (293, 2, 51, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (294, 3, 51, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (295, 1, 52, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (296, 2, 52, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (297, 1, 53, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (298, 2, 53, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (299, 3, 53, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (300, 4, 53, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (301, 1, 54, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (302, 2, 54, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (303, 3, 54, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (304, 1, 55, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (305, 2, 55, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (306, 3, 55, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (307, 4, 55, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (308, 5, 55, '2026-02-25 12:50:00');
INSERT INTO `review_like`
VALUES (309, 1, 56, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (310, 2, 56, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (311, 1, 57, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (312, 2, 57, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (313, 3, 57, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (314, 2, 58, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (315, 3, 58, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (316, 1, 59, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (317, 2, 59, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (318, 3, 59, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (319, 4, 59, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (320, 1, 60, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (321, 2, 60, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (322, 2, 61, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (323, 3, 61, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (324, 4, 61, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (325, 1, 62, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (326, 2, 62, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (327, 3, 62, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (328, 1, 63, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (329, 2, 63, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (330, 3, 63, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (331, 4, 63, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (332, 5, 63, '2026-02-25 12:50:00');
INSERT INTO `review_like`
VALUES (333, 2, 64, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (334, 3, 64, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (335, 1, 65, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (336, 2, 65, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (337, 1, 66, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (338, 2, 66, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (339, 3, 66, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (340, 2, 67, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (341, 1, 68, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (342, 2, 68, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (343, 1, 69, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (344, 2, 69, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (345, 3, 69, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (346, 4, 69, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (347, 1, 70, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (348, 2, 70, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (349, 1, 71, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (350, 2, 71, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (351, 3, 71, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (352, 4, 71, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (353, 5, 71, '2026-02-25 12:50:00');
INSERT INTO `review_like`
VALUES (354, 2, 72, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (355, 3, 72, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (356, 1, 73, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (357, 2, 73, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (358, 2, 74, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (359, 3, 74, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (360, 1, 75, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (361, 2, 75, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (362, 3, 75, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (363, 4, 75, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (364, 5, 75, '2026-02-25 12:50:00');
INSERT INTO `review_like`
VALUES (365, 6, 75, '2026-02-25 12:55:00');
INSERT INTO `review_like`
VALUES (366, 2, 76, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (367, 1, 77, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (368, 2, 77, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (369, 3, 77, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (370, 4, 77, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (371, 1, 78, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (372, 2, 78, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (373, 3, 78, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (374, 1, 79, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (375, 2, 79, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (376, 2, 80, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (377, 3, 80, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (378, 1, 81, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (379, 2, 81, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (380, 3, 81, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (381, 2, 82, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (382, 3, 82, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (383, 4, 82, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (384, 1, 83, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (385, 2, 83, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (386, 3, 83, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (387, 4, 83, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (388, 5, 83, '2026-02-25 12:50:00');
INSERT INTO `review_like`
VALUES (389, 1, 84, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (390, 2, 84, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (391, 3, 84, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (392, 4, 84, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (393, 5, 84, '2026-02-25 12:50:00');
INSERT INTO `review_like`
VALUES (394, 6, 84, '2026-02-25 12:55:00');
INSERT INTO `review_like`
VALUES (395, 2, 85, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (396, 3, 85, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (397, 1, 86, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (398, 2, 86, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (399, 3, 86, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (400, 1, 87, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (401, 2, 87, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (402, 3, 87, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (403, 4, 87, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (404, 5, 87, '2026-02-25 12:50:00');
INSERT INTO `review_like`
VALUES (405, 2, 88, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (406, 1, 89, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (407, 2, 89, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (408, 3, 89, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (409, 4, 89, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (410, 1, 90, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (411, 2, 90, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (412, 3, 90, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (413, 1, 91, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (414, 2, 91, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (415, 3, 91, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (416, 4, 91, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (417, 1, 92, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (418, 2, 92, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (419, 3, 92, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (420, 4, 92, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (421, 5, 92, '2026-02-25 12:50:00');
INSERT INTO `review_like`
VALUES (422, 6, 92, '2026-02-25 12:55:00');
INSERT INTO `review_like`
VALUES (423, 2, 93, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (424, 3, 93, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (425, 1, 94, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (426, 2, 94, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (427, 3, 94, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (428, 4, 94, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (429, 1, 95, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (430, 2, 95, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (431, 3, 95, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (432, 1, 96, '2026-02-25 12:30:00');
INSERT INTO `review_like`
VALUES (433, 2, 96, '2026-02-25 12:35:00');
INSERT INTO `review_like`
VALUES (434, 3, 96, '2026-02-25 12:40:00');
INSERT INTO `review_like`
VALUES (435, 4, 96, '2026-02-25 12:45:00');
INSERT INTO `review_like`
VALUES (436, 5, 96, '2026-02-25 12:50:00');
INSERT INTO `review_like`
VALUES (437, 6, 96, '2026-02-25 12:55:00');
INSERT INTO `review_like`
VALUES (438, 7, 96, '2026-02-25 13:00:00');
INSERT INTO `review_like`
VALUES (439, 8, 96, '2026-02-25 13:05:00');

-- ----------------------------
-- Table structure for screening
-- ----------------------------
DROP TABLE IF EXISTS `screening`;
CREATE TABLE `screening`
(
    `id`          bigint UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '场次id，主键',
    `movie_id`    bigint UNSIGNED                                              NOT NULL COMMENT '关联电影id',
    `cinema_id`   bigint UNSIGNED                                              NOT NULL COMMENT '关联影院id',
    `hall_name`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '影厅名称',
    `row_count`   int UNSIGNED                                                 NOT NULL COMMENT '排数',
    `col_count`   int UNSIGNED                                                 NOT NULL COMMENT '座数',
    `start_time`  datetime                                                     NOT NULL COMMENT '开场时间',
    `end_time`    datetime                                                     NOT NULL COMMENT '散场时间',
    `price`       decimal(10, 2)                                               NOT NULL COMMENT '票价',
    `create_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_cinema_id_movie_id_start_time` (`cinema_id` ASC, `movie_id` ASC, `start_time` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 107
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '场次表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of screening
-- ----------------------------
INSERT INTO `screening`
VALUES (1, 1, 1, 'IMAX激光厅', 10, 18, '2077-02-26 10:30:00', '2077-02-26 12:40:00', 68.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (2, 1, 1, 'IMAX激光厅', 10, 18, '2077-02-26 14:00:00', '2077-02-26 16:10:00', 68.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (3, 1, 1, 'IMAX激光厅', 10, 18, '2077-02-26 19:30:00', '2077-02-26 21:40:00', 75.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (4, 2, 1, '杜比全景声厅', 9, 16, '2077-02-26 11:00:00', '2077-02-26 13:40:00', 58.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (5, 2, 1, '杜比全景声厅', 9, 16, '2077-02-26 15:30:00', '2077-02-26 18:10:00', 58.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (6, 3, 1, '4DX厅', 8, 14, '2077-02-26 13:30:00', '2077-02-26 15:45:00', 72.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (7, 3, 1, '4DX厅', 8, 14, '2077-02-26 20:00:00', '2077-02-26 22:15:00', 78.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (8, 5, 1, '3号厅', 9, 15, '2077-02-26 09:30:00', '2077-02-26 11:20:00', 45.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (9, 5, 1, '3号厅', 9, 15, '2077-02-26 14:30:00', '2077-02-26 16:20:00', 45.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (10, 7, 1, 'IMAX激光厅', 10, 18, '2077-02-27 10:00:00', '2077-02-26 13:12:00', 88.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (11, 1, 2, '杜比影院', 9, 17, '2077-02-26 10:00:00', '2077-02-26 12:10:00', 72.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (12, 1, 2, '杜比影院', 9, 17, '2077-02-26 15:00:00', '2077-02-26 17:10:00', 72.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (13, 2, 2, '1号厅', 10, 16, '2077-02-26 11:30:00', '2077-02-26 14:10:00', 55.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (14, 2, 2, '1号厅', 10, 16, '2077-02-26 16:30:00', '2077-02-26 19:10:00', 55.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (15, 4, 2, '2号厅', 8, 14, '2077-02-26 09:00:00', '2077-02-26 10:35:00', 42.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (16, 4, 2, '2号厅', 8, 14, '2077-02-26 14:00:00', '2077-02-26 15:35:00', 42.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (17, 9, 2, 'IMAX厅', 10, 18, '2077-02-26 13:00:00', '2077-02-26 15:31:00', 65.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (18, 9, 2, 'IMAX厅', 10, 18, '2077-02-26 19:00:00', '2077-02-26 21:31:00', 72.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (19, 10, 2, '3号厅', 8, 13, '2077-02-26 10:30:00', '2077-02-26 12:07:00', 48.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (20, 10, 2, '3号厅', 8, 13, '2077-02-26 15:30:00', '2077-02-26 17:07:00', 48.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (21, 3, 3, '1号激光厅', 9, 16, '2077-02-26 09:30:00', '2077-02-26 11:45:00', 55.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (22, 3, 3, '1号激光厅', 9, 16, '2077-02-26 14:30:00', '2077-02-26 16:45:00', 55.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (23, 5, 3, '2号厅', 8, 14, '2077-02-26 10:30:00', '2077-02-26 12:20:00', 48.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (24, 5, 3, '2号厅', 8, 14, '2077-02-26 15:30:00', '2077-02-26 17:20:00', 48.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (25, 6, 3, '3号厅', 8, 14, '2077-02-26 11:00:00', '2077-02-26 12:40:00', 52.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (26, 6, 3, '3号厅', 8, 14, '2077-02-26 16:00:00', '2077-02-26 17:40:00', 52.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (27, 8, 3, 'Dolby Cinema', 9, 16, '2077-02-26 13:30:00', '2077-02-26 16:31:00', 75.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (28, 8, 3, 'Dolby Cinema', 9, 16, '2077-02-26 19:30:00', '2077-02-26 22:31:00', 82.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (29, 1, 3, 'IMAX厅', 10, 18, '2077-02-27 10:30:00', '2077-02-27 12:40:00', 65.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (30, 1, 3, 'IMAX厅', 10, 18, '2077-02-27 15:30:00', '2077-02-27 17:40:00', 65.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (31, 2, 4, '1号厅', 10, 16, '2077-02-26 10:00:00', '2077-02-26 12:40:00', 52.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (32, 2, 4, '1号厅', 10, 16, '2077-02-26 15:00:00', '2077-02-26 17:40:00', 52.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (33, 4, 4, '2号厅', 8, 14, '2077-02-26 09:30:00', '2077-02-26 11:05:00', 45.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (34, 4, 4, '2号厅', 8, 14, '2077-02-26 14:30:00', '2077-02-26 16:05:00', 45.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (35, 7, 4, 'IMAX厅', 10, 18, '2077-02-26 11:30:00', '2077-02-26 14:41:00', 68.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (36, 7, 4, 'IMAX厅', 10, 18, '2077-02-26 18:00:00', '2077-02-26 21:11:00', 75.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (37, 9, 4, 'RealD厅', 9, 16, '2077-02-26 13:00:00', '2077-02-26 15:31:00', 58.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (38, 9, 4, 'RealD厅', 9, 16, '2077-02-26 19:30:00', '2077-02-26 22:01:00', 65.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (39, 10, 4, '3号厅', 8, 13, '2077-02-26 10:00:00', '2077-02-26 11:27:00', 42.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (40, 10, 4, '3号厅', 8, 13, '2077-02-26 15:00:00', '2077-02-26 16:27:00', 42.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (41, 1, 5, 'IMAX厅', 10, 18, '2077-02-26 09:30:00', '2077-02-26 11:40:00', 62.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (42, 1, 5, 'IMAX厅', 10, 18, '2077-02-26 14:00:00', '2077-02-26 16:10:00', 62.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (43, 3, 5, '4DX厅', 8, 14, '2077-02-26 11:00:00', '2077-02-26 13:15:00', 68.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (44, 3, 5, '4DX厅', 8, 14, '2077-02-26 16:30:00', '2077-02-26 18:45:00', 68.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (45, 5, 5, 'ScreenX厅', 9, 16, '2077-02-26 10:30:00', '2077-02-26 12:15:00', 58.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (46, 5, 5, 'ScreenX厅', 9, 16, '2077-02-26 15:30:00', '2077-02-26 17:15:00', 58.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (47, 7, 5, 'STARIUM厅', 9, 17, '2077-02-26 13:00:00', '2077-02-26 16:11:00', 72.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (48, 7, 5, 'STARIUM厅', 9, 17, '2077-02-26 19:00:00', '2077-02-26 22:11:00', 78.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (49, 10, 5, '2号厅', 8, 13, '2077-02-26 09:00:00', '2077-02-26 10:27:00', 45.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (50, 10, 5, '2号厅', 8, 13, '2077-02-26 14:00:00', '2077-02-26 15:27:00', 45.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (51, 2, 6, 'LD厅', 9, 16, '2077-02-26 10:30:00', '2077-02-26 13:10:00', 55.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (52, 2, 6, 'LD厅', 9, 16, '2077-02-26 15:30:00', '2077-02-26 18:10:00', 55.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (53, 4, 6, '1号厅', 8, 14, '2077-02-26 09:00:00', '2077-02-26 10:35:00', 42.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (54, 4, 6, '1号厅', 8, 14, '2077-02-26 14:30:00', '2077-02-26 16:05:00', 42.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (55, 6, 6, '杜比全景声厅', 8, 14, '2077-02-26 11:30:00', '2077-02-26 13:10:00', 48.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (56, 6, 6, '杜比全景声厅', 8, 14, '2077-02-26 16:30:00', '2077-02-26 18:10:00', 48.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (57, 9, 6, '2号厅', 9, 15, '2077-02-26 13:00:00', '2077-02-26 15:31:00', 52.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (58, 9, 6, '2号厅', 9, 15, '2077-02-26 19:00:00', '2077-02-26 21:31:00', 58.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (59, 1, 7, 'IMAX激光厅', 10, 18, '2077-02-26 10:00:00', '2077-02-26 12:10:00', 65.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (60, 1, 7, 'IMAX激光厅', 10, 18, '2077-02-26 15:30:00', '2077-02-26 17:40:00', 65.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (61, 3, 7, 'RealD厅', 9, 16, '2077-02-26 11:00:00', '2077-02-26 13:15:00', 52.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (62, 3, 7, 'RealD厅', 9, 16, '2077-02-26 16:30:00', '2077-02-26 18:45:00', 52.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (63, 5, 7, '1号厅', 8, 14, '2077-02-26 09:30:00', '2077-02-26 11:15:00', 45.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (64, 5, 7, '1号厅', 8, 14, '2077-02-26 14:30:00', '2077-02-26 16:15:00', 45.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (65, 8, 7, '2号厅', 9, 16, '2077-02-26 13:30:00', '2077-02-26 16:31:00', 58.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (66, 8, 7, '2号厅', 9, 16, '2077-02-26 19:30:00', '2077-02-26 22:31:00', 65.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (67, 2, 8, 'IMAX厅', 10, 18, '2077-02-26 09:30:00', '2077-02-26 12:10:00', 58.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (68, 2, 8, 'IMAX厅', 10, 18, '2077-02-26 14:30:00', '2077-02-26 17:10:00', 58.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (69, 4, 8, '杜比全景声厅', 8, 14, '2077-02-26 10:30:00', '2077-02-26 12:05:00', 48.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (70, 4, 8, '杜比全景声厅', 8, 14, '2077-02-26 15:00:00', '2077-02-26 16:35:00', 48.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (71, 7, 8, 'CINITY厅', 10, 18, '2077-02-26 11:30:00', '2077-02-26 14:41:00', 75.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (72, 7, 8, 'CINITY厅', 10, 18, '2077-02-26 18:00:00', '2077-02-26 21:11:00', 82.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (73, 10, 8, '1号厅', 8, 13, '2077-02-26 09:00:00', '2077-02-26 10:27:00', 42.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (74, 10, 8, '1号厅', 8, 13, '2077-02-26 14:00:00', '2077-02-26 15:27:00', 42.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (75, 1, 9, '中国巨幕厅', 10, 18, '2077-02-26 10:00:00', '2077-02-26 12:10:00', 62.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (76, 1, 9, '中国巨幕厅', 10, 18, '2077-02-26 15:00:00', '2077-02-26 17:10:00', 62.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (77, 3, 9, 'IMAX厅', 10, 18, '2077-02-26 11:00:00', '2077-02-26 13:15:00', 58.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (78, 3, 9, 'IMAX厅', 10, 18, '2077-02-26 16:00:00', '2077-02-26 18:15:00', 58.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (79, 6, 9, 'RealD厅', 8, 14, '2077-02-26 09:30:00', '2077-02-26 11:10:00', 48.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (80, 6, 9, 'RealD厅', 8, 14, '2077-02-26 14:30:00', '2077-02-26 16:10:00', 48.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (81, 9, 9, '1号厅', 9, 16, '2077-02-26 13:30:00', '2077-02-26 16:01:00', 55.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (82, 9, 9, '1号厅', 9, 16, '2077-02-26 19:00:00', '2077-02-26 21:31:00', 62.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (83, 2, 10, 'IMAX激光厅', 10, 18, '2077-02-26 09:30:00', '2077-02-26 12:10:00', 65.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (84, 2, 10, 'IMAX激光厅', 10, 18, '2077-02-26 14:30:00', '2077-02-26 17:10:00', 65.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (85, 5, 10, 'PRIME厅', 9, 16, '2077-02-26 10:30:00', '2077-02-26 12:15:00', 55.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (86, 5, 10, 'PRIME厅', 9, 16, '2077-02-26 15:30:00', '2077-02-26 17:15:00', 55.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (87, 7, 10, '杜比全景声厅', 9, 16, '2077-02-26 11:30:00', '2077-02-26 14:41:00', 68.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (88, 7, 10, '杜比全景声厅', 9, 16, '2077-02-26 17:30:00', '2077-02-26 20:41:00', 75.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (89, 10, 10, '1号厅', 8, 13, '2077-02-26 09:00:00', '2077-02-26 10:27:00', 45.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (90, 10, 10, '1号厅', 8, 13, '2077-02-26 14:00:00', '2077-02-26 15:27:00', 45.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (91, 1, 11, 'IMAX厅', 10, 18, '2077-02-26 10:30:00', '2077-02-26 12:40:00', 62.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (92, 1, 11, 'IMAX厅', 10, 18, '2077-02-26 15:30:00', '2077-02-26 17:40:00', 62.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (93, 3, 11, '激光厅', 9, 16, '2077-02-26 11:00:00', '2077-02-26 13:15:00', 52.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (94, 3, 11, '激光厅', 9, 16, '2077-02-26 16:00:00', '2077-02-26 18:15:00', 52.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (95, 6, 11, '1号厅', 8, 14, '2077-02-26 09:30:00', '2077-02-26 11:10:00', 45.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (96, 6, 11, '1号厅', 8, 14, '2077-02-26 14:30:00', '2077-02-26 16:10:00', 45.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (97, 8, 11, 'Dolby Atmos厅', 9, 16, '2077-02-26 13:00:00', '2077-02-26 16:01:00', 58.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (98, 8, 11, 'Dolby Atmos厅', 9, 16, '2077-02-26 19:00:00', '2077-02-26 22:01:00', 65.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (99, 2, 12, 'IMAX厅', 10, 18, '2077-02-26 09:00:00', '2077-02-26 11:40:00', 55.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (100, 2, 12, 'IMAX厅', 10, 18, '2077-02-26 14:00:00', '2077-02-26 16:40:00', 55.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (101, 4, 12, 'RealD厅', 8, 14, '2077-02-26 10:00:00', '2077-02-26 11:35:00', 42.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (102, 4, 12, 'RealD厅', 8, 14, '2077-02-26 15:00:00', '2077-02-26 16:35:00', 42.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (103, 5, 12, '儿童厅', 8, 12, '2077-02-26 09:30:00', '2077-02-26 11:15:00', 48.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (104, 5, 12, '儿童厅', 8, 12, '2077-02-26 14:30:00', '2077-02-26 16:15:00', 48.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (105, 9, 12, '1号厅', 9, 16, '2077-02-26 11:30:00', '2077-02-26 14:01:00', 52.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');
INSERT INTO `screening`
VALUES (106, 9, 12, '1号厅', 9, 16, '2077-02-26 16:30:00', '2077-02-26 19:01:00', 58.00, '2026-02-25 14:13:17',
        '2026-02-25 16:42:46');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          bigint UNSIGNED                                               NOT NULL AUTO_INCREMENT COMMENT '用户id，主键',
    `phone`       varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '手机号',
    `password`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '密码，BCrypt加密',
    `nickname`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '昵称',
    `avatar`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '头像URL',
    `gender`      tinyint                                                       NOT NULL DEFAULT 0 COMMENT '性别，0-未知，1-男，2-女',
    `birthday`    date                                                          NULL     DEFAULT NULL COMMENT '生日',
    `introduce`   varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '简介',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_phone` (`phone` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user`
VALUES (1, '13600000001', '$2a$10$vOwE4BjEFnBgxtVViIadOeRsJiMW8fs0pkYaEF3Wh6T1Z6E0w1cZG', '电影迷小王',
        'http://127.0.0.1:9000/movie-time/default-avatar.png', 1, '1999-09-09', '热爱电影，每周必看一部新片',
        '2026-02-25 10:35:42', '2026-02-25 11:45:18');
INSERT INTO `user`
VALUES (2, '13600000002', NULL, 'user_KscIVe', 'http://127.0.0.1:9000/movie-time/default-avatar.png', 0, NULL, NULL,
        '2026-02-25 10:35:58', '2026-02-25 10:35:58');
INSERT INTO `user`
VALUES (3, '13600000003', NULL, 'user_KvoDea', 'http://127.0.0.1:9000/movie-time/default-avatar.png', 0, NULL, NULL,
        '2026-02-25 10:36:14', '2026-02-25 10:36:14');
INSERT INTO `user`
VALUES (4, '13600000004', NULL, 'user_ngBBSg', 'http://127.0.0.1:9000/movie-time/default-avatar.png', 0, NULL, NULL,
        '2026-02-25 10:36:28', '2026-02-25 10:36:28');
INSERT INTO `user`
VALUES (5, '13600000005', NULL, 'user_nfmlSO', 'http://127.0.0.1:9000/movie-time/default-avatar.png', 0, NULL, NULL,
        '2026-02-25 10:36:42', '2026-02-25 10:36:42');
INSERT INTO `user`
VALUES (6, '13600000006', NULL, 'user_uSGKjG', 'http://127.0.0.1:9000/movie-time/default-avatar.png', 0, NULL, NULL,
        '2026-02-25 10:36:55', '2026-02-25 10:36:55');
INSERT INTO `user`
VALUES (7, '13600000007', NULL, 'user_KZWuVf', 'http://127.0.0.1:9000/movie-time/default-avatar.png', 0, NULL, NULL,
        '2026-02-25 10:37:09', '2026-02-25 10:37:09');
INSERT INTO `user`
VALUES (8, '13600000008', NULL, 'user_QgvPeO', 'http://127.0.0.1:9000/movie-time/default-avatar.png', 0, NULL, NULL,
        '2026-02-25 10:37:25', '2026-02-25 10:37:25');
INSERT INTO `user`
VALUES (9, '13600000009', NULL, 'user_rTKARD', 'http://127.0.0.1:9000/movie-time/default-avatar.png', 0, NULL, NULL,
        '2026-02-25 10:37:39', '2026-02-25 10:37:39');
INSERT INTO `user`
VALUES (10, '13600000010', NULL, 'user_KMSJqn', 'http://127.0.0.1:9000/movie-time/default-avatar.png', 0, NULL, NULL,
        '2026-02-25 10:37:54', '2026-02-25 10:37:54');

-- ----------------------------
-- Table structure for user_follow
-- ----------------------------
DROP TABLE IF EXISTS `user_follow`;
CREATE TABLE `user_follow`
(
    `id`          bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户关注关系id，主键',
    `follower_id` bigint UNSIGNED NOT NULL COMMENT '关联关注者id',
    `followee_id` bigint UNSIGNED NOT NULL COMMENT '关联被关注者id',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_follower_id_followee_id` (`follower_id` ASC, `followee_id` ASC) USING BTREE,
    INDEX `idx_followee_id` (`followee_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 37
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户关注关系表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_follow
-- ----------------------------
INSERT INTO `user_follow`
VALUES (1, 1, 2, '2026-02-20 10:00:00');
INSERT INTO `user_follow`
VALUES (2, 1, 3, '2026-02-20 10:05:00');
INSERT INTO `user_follow`
VALUES (3, 1, 5, '2026-02-20 10:10:00');
INSERT INTO `user_follow`
VALUES (4, 1, 7, '2026-02-21 09:00:00');
INSERT INTO `user_follow`
VALUES (5, 2, 1, '2026-02-20 10:01:00');
INSERT INTO `user_follow`
VALUES (6, 2, 3, '2026-02-20 11:00:00');
INSERT INTO `user_follow`
VALUES (7, 2, 4, '2026-02-20 11:05:00');
INSERT INTO `user_follow`
VALUES (8, 2, 6, '2026-02-21 09:30:00');
INSERT INTO `user_follow`
VALUES (9, 3, 1, '2026-02-20 10:06:00');
INSERT INTO `user_follow`
VALUES (10, 3, 2, '2026-02-20 11:01:00');
INSERT INTO `user_follow`
VALUES (11, 3, 5, '2026-02-20 12:00:00');
INSERT INTO `user_follow`
VALUES (12, 3, 8, '2026-02-21 10:00:00');
INSERT INTO `user_follow`
VALUES (13, 4, 2, '2026-02-20 11:06:00');
INSERT INTO `user_follow`
VALUES (14, 4, 6, '2026-02-20 13:00:00');
INSERT INTO `user_follow`
VALUES (15, 4, 7, '2026-02-20 13:05:00');
INSERT INTO `user_follow`
VALUES (16, 5, 1, '2026-02-20 10:11:00');
INSERT INTO `user_follow`
VALUES (17, 5, 3, '2026-02-20 12:01:00');
INSERT INTO `user_follow`
VALUES (18, 5, 8, '2026-02-20 14:00:00');
INSERT INTO `user_follow`
VALUES (19, 5, 9, '2026-02-21 11:00:00');
INSERT INTO `user_follow`
VALUES (20, 6, 2, '2026-02-21 09:31:00');
INSERT INTO `user_follow`
VALUES (21, 6, 4, '2026-02-20 13:01:00');
INSERT INTO `user_follow`
VALUES (22, 6, 9, '2026-02-20 15:00:00');
INSERT INTO `user_follow`
VALUES (23, 6, 10, '2026-02-21 11:30:00');
INSERT INTO `user_follow`
VALUES (24, 7, 1, '2026-02-21 09:01:00');
INSERT INTO `user_follow`
VALUES (25, 7, 4, '2026-02-20 13:06:00');
INSERT INTO `user_follow`
VALUES (26, 7, 8, '2026-02-20 15:30:00');
INSERT INTO `user_follow`
VALUES (27, 8, 3, '2026-02-21 10:01:00');
INSERT INTO `user_follow`
VALUES (28, 8, 5, '2026-02-20 14:01:00');
INSERT INTO `user_follow`
VALUES (29, 8, 7, '2026-02-20 15:31:00');
INSERT INTO `user_follow`
VALUES (30, 8, 10, '2026-02-21 12:00:00');
INSERT INTO `user_follow`
VALUES (31, 9, 5, '2026-02-21 11:01:00');
INSERT INTO `user_follow`
VALUES (32, 9, 6, '2026-02-20 15:01:00');
INSERT INTO `user_follow`
VALUES (33, 9, 10, '2026-02-21 12:30:00');
INSERT INTO `user_follow`
VALUES (34, 10, 6, '2026-02-21 11:31:00');
INSERT INTO `user_follow`
VALUES (35, 10, 8, '2026-02-21 12:01:00');
INSERT INTO `user_follow`
VALUES (36, 10, 9, '2026-02-21 12:31:00');

SET FOREIGN_KEY_CHECKS = 1;
