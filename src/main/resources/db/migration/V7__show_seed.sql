-- 演出测试数据

-- 演出：一场一条记录，只记录开场时间（演出时长是约数）
INSERT INTO `t_show` (`id`, `title`, `poster`, `description`, `duration`, `venue_name`, `venue_address`, `start_time`, `status`)
VALUES (1, '星光之夜演唱会', 'https://github.com/cd-water.png', '一场关于星光与城市的现场演唱会。', 120, '星光大剧院', '星光大道88号', DATE_ADD(NOW(), INTERVAL 1 DAY), 1),
       (2, '星光之夜演唱会', 'https://github.com/cd-water.png', '一场关于星光与城市的现场演唱会。', 120, '星光大剧院', '星光大道88号', DATE_ADD(NOW(), INTERVAL 2 DAY), 1),
       (3, '暗恋桃花源', 'https://github.com/cd-water.png', '经典话剧，悲喜交错的舞台故事。', 150, '星光大剧院', '星光大道88号', DATE_ADD(NOW(), INTERVAL 3 DAY), 1),
       (4, '城市音乐节', 'https://github.com/cd-water.png', '多组乐队轮番登场的城市音乐节。', 180, '城市音乐中心', '音乐路66号', DATE_ADD(NOW(), INTERVAL 4 DAY), 1),
       (5, '亲子奇幻剧场', 'https://github.com/cd-water.png', '适合亲子观看的互动奇幻演出。', 90, '星光大剧院', '星光大道88号', DATE_ADD(NOW(), INTERVAL 2 DAY), 1),
       (6, '午夜爵士音乐会', 'https://github.com/cd-water.png', '午夜时分的爵士乐现场。', 120, '城市音乐中心', '音乐路66号', DATE_ADD(NOW(), INTERVAL 5 DAY), 1),
       (7, '亲子科学秀', 'https://github.com/cd-water.png', '用实验和互动带孩子认识科学。', 90, '星光大剧院', '星光大道88号', DATE_ADD(NOW(), INTERVAL 6 DAY), 1),
       (8, '脱口秀之夜', 'https://github.com/cd-water.png', '几位脱口秀演员轮番上场的爆笑夜晚。', 100, '城市音乐中心', '音乐路66号', DATE_ADD(NOW(), INTERVAL 7 DAY), 1);

-- 演出票档：按区域定价
INSERT INTO `t_show_ticket_type` (`id`, `show_id`, `name`, `zone`, `price`, `sort_order`)
VALUES (1, 1, 'A区', 'A区', 680.00, 1),
       (2, 1, 'B区', 'B区', 480.00, 2),
       (3, 1, 'C区', 'C区', 280.00, 3),
       (4, 2, 'A区', 'A区', 580.00, 1),
       (5, 2, 'B区', 'B区', 380.00, 2),
       (6, 2, 'C区', 'C区', 180.00, 3),
       (7, 3, 'A区', 'A区', 380.00, 1),
       (8, 3, 'B区', 'B区', 280.00, 2),
       (9, 3, 'C区', 'C区', 180.00, 3),
       (10, 4, 'VIP区', 'VIP', 399.00, 1),
       (11, 4, '普通区', '普通', 199.00, 2),
       (12, 5, 'A区', 'A区', 280.00, 1),
       (13, 5, 'B区', 'B区', 180.00, 2),
       (14, 5, 'C区', 'C区', 120.00, 3),
       (15, 6, 'A区', 'A区', 480.00, 1),
       (16, 6, 'B区', 'B区', 380.00, 2),
       (17, 6, 'C区', 'C区', 280.00, 3),
       (18, 7, 'A区', 'A区', 280.00, 1),
       (19, 7, 'B区', 'B区', 180.00, 2),
       (20, 7, 'C区', 'C区', 120.00, 3),
       (21, 8, 'A区', 'A区', 380.00, 1),
       (22, 8, 'B区', 'B区', 280.00, 2),
       (23, 8, 'C区', 'C区', 180.00, 3);

-- 演出座位：按区域连续编号；除城市音乐节外分 A/B/C 区
INSERT INTO `t_show_seat` (`show_id`, `zone`, `seat_no`)
SELECT s.id, z.zone, (z.r - z.first_row) * 10 + c.n
FROM `t_show` s
CROSS JOIN (SELECT 1 AS r, 'A区' AS zone, 1 AS first_row UNION ALL
            SELECT 2, 'A区', 1 UNION ALL
            SELECT 3, 'B区', 3 UNION ALL
            SELECT 4, 'B区', 3 UNION ALL
            SELECT 5, 'B区', 3 UNION ALL
            SELECT 6, 'C区', 6 UNION ALL
            SELECT 7, 'C区', 6 UNION ALL
            SELECT 8, 'C区', 6) z
CROSS JOIN (SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
            UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
            UNION ALL SELECT 9 UNION ALL SELECT 10) c
WHERE s.id IN (1, 2, 3, 5, 6, 7, 8);

-- 城市音乐节座位：VIP 与普通两个区域，各自连续编号
INSERT INTO `t_show_seat` (`show_id`, `zone`, `seat_no`)
SELECT s.id, z.zone, (z.r - z.first_row) * 10 + c.n
FROM `t_show` s
CROSS JOIN (SELECT 1 AS r, 'VIP' AS zone, 1 AS first_row UNION ALL
            SELECT 2, 'VIP', 1 UNION ALL
            SELECT 3, '普通', 3 UNION ALL
            SELECT 4, '普通', 3 UNION ALL
            SELECT 5, '普通', 3 UNION ALL
            SELECT 6, '普通', 3 UNION ALL
            SELECT 7, '普通', 3 UNION ALL
            SELECT 8, '普通', 3) z
CROSS JOIN (SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
            UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
            UNION ALL SELECT 9 UNION ALL SELECT 10) c
WHERE s.id = 4;
