-- 订单测试数据：覆盖待支付、已支付、已取消、已超时四种状态

-- ===== 电影订单 =====
INSERT INTO `t_movie_order` (`order_no`, `user_id`, `screening_id`, `movie_title`, `cinema_name`, `hall_name`, `start_time`, `end_time`, `total_amount`, `status`, `expire_time`, `pay_time`)
VALUES (1900000000000000001, 1, 1, '星际征途', '星光国际影城（人民广场店）', '1号厅', DATE_ADD(NOW(), INTERVAL 120 MINUTE), DATE_ADD(NOW(), INTERVAL 262 MINUTE), 99.80, 0, DATE_ADD(NOW(), INTERVAL 15 MINUTE), NULL),
       (1900000000000000002, 1, 1, '星际征途', '星光国际影城（人民广场店）', '1号厅', DATE_ADD(NOW(), INTERVAL 120 MINUTE), DATE_ADD(NOW(), INTERVAL 262 MINUTE), 99.80, 1, DATE_ADD(NOW(), INTERVAL 15 MINUTE), NOW());

INSERT INTO `t_movie_order` (`order_no`, `user_id`, `screening_id`, `movie_title`, `cinema_name`, `hall_name`, `start_time`, `end_time`, `total_amount`, `status`, `expire_time`, `pay_time`)
SELECT 1900000000000000006, 2, s.id, m.title, c.name, h.name, s.start_time, s.end_time, s.price * 2, 1,
       DATE_ADD(NOW(), INTERVAL 15 MINUTE), NOW()
FROM `t_screening` s
JOIN `t_movie` m ON m.id = s.movie_id
JOIN `t_cinema` c ON c.id = s.cinema_id
JOIN `t_hall` h ON h.id = s.hall_id
WHERE s.id = 3;

INSERT INTO `t_movie_order` (`order_no`, `user_id`, `screening_id`, `movie_title`, `cinema_name`, `hall_name`, `start_time`, `end_time`, `total_amount`, `status`, `expire_time`, `pay_time`)
SELECT 1900000000000000007, 3, s.id, m.title, c.name, h.name, s.start_time, s.end_time, s.price * 2, 2,
       DATE_SUB(NOW(), INTERVAL 10 MINUTE), NULL
FROM `t_screening` s
JOIN `t_movie` m ON m.id = s.movie_id
JOIN `t_cinema` c ON c.id = s.cinema_id
JOIN `t_hall` h ON h.id = s.hall_id
WHERE s.id = 5;

INSERT INTO `t_movie_order` (`order_no`, `user_id`, `screening_id`, `movie_title`, `cinema_name`, `hall_name`, `start_time`, `end_time`, `total_amount`, `status`, `expire_time`, `pay_time`)
SELECT 1900000000000000008, 4, s.id, m.title, c.name, h.name, s.start_time, s.end_time, s.price * 2, 3,
       DATE_SUB(NOW(), INTERVAL 30 MINUTE), NULL
FROM `t_screening` s
JOIN `t_movie` m ON m.id = s.movie_id
JOIN `t_cinema` c ON c.id = s.cinema_id
JOIN `t_hall` h ON h.id = s.hall_id
WHERE s.id = 7;

INSERT INTO `t_movie_order` (`order_no`, `user_id`, `screening_id`, `movie_title`, `cinema_name`, `hall_name`, `start_time`, `end_time`, `total_amount`, `status`, `expire_time`, `pay_time`)
SELECT 1900000000000000009, 5, s.id, m.title, c.name, h.name, s.start_time, s.end_time, s.price, 0,
       DATE_ADD(NOW(), INTERVAL 15 MINUTE), NULL
FROM `t_screening` s
JOIN `t_movie` m ON m.id = s.movie_id
JOIN `t_cinema` c ON c.id = s.cinema_id
JOIN `t_hall` h ON h.id = s.hall_id
WHERE s.id = 9;

INSERT INTO `t_movie_order` (`order_no`, `user_id`, `screening_id`, `movie_title`, `cinema_name`, `hall_name`, `start_time`, `end_time`, `total_amount`, `status`, `expire_time`, `pay_time`)
SELECT 1900000000000000010, 6, s.id, m.title, c.name, h.name, s.start_time, s.end_time, s.price * 3, 1,
       DATE_ADD(NOW(), INTERVAL 15 MINUTE), NOW()
FROM `t_screening` s
JOIN `t_movie` m ON m.id = s.movie_id
JOIN `t_cinema` c ON c.id = s.cinema_id
JOIN `t_hall` h ON h.id = s.hall_id
WHERE s.id = 11;

INSERT INTO `t_movie_order` (`order_no`, `user_id`, `screening_id`, `movie_title`, `cinema_name`, `hall_name`, `start_time`, `end_time`, `total_amount`, `status`, `expire_time`, `pay_time`)
SELECT 1900000000000000011, 1, s.id, m.title, c.name, h.name, s.start_time, s.end_time, s.price * 2, 1,
       DATE_ADD(NOW(), INTERVAL 15 MINUTE), NOW()
FROM `t_screening` s
JOIN `t_movie` m ON m.id = s.movie_id
JOIN `t_cinema` c ON c.id = s.cinema_id
JOIN `t_hall` h ON h.id = s.hall_id
WHERE s.id = 16;

INSERT INTO `t_movie_order_seat` (`order_no`, `seat_id`, `row_num`, `col_num`, `price`)
SELECT 1900000000000000001, s.id, s.row_num, s.col_num, 49.90
FROM `t_seat` s
WHERE s.hall_id = 1 AND s.row_num = 1 AND s.col_num IN (1, 2);

INSERT INTO `t_movie_order_seat` (`order_no`, `seat_id`, `row_num`, `col_num`, `price`)
SELECT 1900000000000000002, s.id, s.row_num, s.col_num, 49.90
FROM `t_seat` s
WHERE s.hall_id = 1 AND s.row_num = 1 AND s.col_num IN (3, 4);

INSERT INTO `t_movie_order_seat` (`order_no`, `seat_id`, `row_num`, `col_num`, `price`)
SELECT 1900000000000000006, s.id, s.row_num, s.col_num, 52.90
FROM `t_seat` s
WHERE s.hall_id = 3 AND s.row_num = 1 AND s.col_num IN (1, 2);

INSERT INTO `t_movie_order_seat` (`order_no`, `seat_id`, `row_num`, `col_num`, `price`)
SELECT 1900000000000000007, s.id, s.row_num, s.col_num, 42.90
FROM `t_seat` s
WHERE s.hall_id = 5 AND s.row_num = 1 AND s.col_num IN (1, 2);

INSERT INTO `t_movie_order_seat` (`order_no`, `seat_id`, `row_num`, `col_num`, `price`)
SELECT 1900000000000000008, s.id, s.row_num, s.col_num, 38.90
FROM `t_seat` s
WHERE s.hall_id = 7 AND s.row_num = 2 AND s.col_num IN (1, 2);

INSERT INTO `t_movie_order_seat` (`order_no`, `seat_id`, `row_num`, `col_num`, `price`)
SELECT 1900000000000000009, s.id, s.row_num, s.col_num, 46.90
FROM `t_seat` s
WHERE s.hall_id = 9 AND s.row_num = 1 AND s.col_num = 1;

INSERT INTO `t_movie_order_seat` (`order_no`, `seat_id`, `row_num`, `col_num`, `price`)
SELECT 1900000000000000010, s.id, s.row_num, s.col_num, 44.90
FROM `t_seat` s
WHERE s.hall_id = 11 AND s.row_num = 1 AND s.col_num IN (1, 2, 3);

INSERT INTO `t_movie_order_seat` (`order_no`, `seat_id`, `row_num`, `col_num`, `price`)
SELECT 1900000000000000011, s.id, s.row_num, s.col_num, 54.90
FROM `t_seat` s
WHERE s.hall_id = 16 AND s.row_num = 1 AND s.col_num IN (1, 2);

-- 待支付订单锁定座位
UPDATE `t_screening_seat` ss
JOIN `t_seat` s ON s.id = ss.seat_id
SET ss.status = 1, ss.lock_order_no = 1900000000000000001, ss.lock_expire_time = DATE_ADD(NOW(), INTERVAL 15 MINUTE)
WHERE ss.screening_id = 1 AND s.row_num = 1 AND s.col_num IN (1, 2);

UPDATE `t_screening_seat` ss
JOIN `t_seat` s ON s.id = ss.seat_id
SET ss.status = 1, ss.lock_order_no = 1900000000000000009, ss.lock_expire_time = DATE_ADD(NOW(), INTERVAL 15 MINUTE)
WHERE ss.screening_id = 9 AND s.row_num = 1 AND s.col_num = 1;

-- 已支付订单占用座位
UPDATE `t_screening_seat` ss
JOIN `t_seat` s ON s.id = ss.seat_id
SET ss.status = 2, ss.lock_order_no = 1900000000000000002, ss.lock_expire_time = NULL
WHERE ss.screening_id = 1 AND s.row_num = 1 AND s.col_num IN (3, 4);

UPDATE `t_screening_seat` ss
JOIN `t_seat` s ON s.id = ss.seat_id
SET ss.status = 2, ss.lock_order_no = 1900000000000000006, ss.lock_expire_time = NULL
WHERE ss.screening_id = 3 AND s.row_num = 1 AND s.col_num IN (1, 2);

UPDATE `t_screening_seat` ss
JOIN `t_seat` s ON s.id = ss.seat_id
SET ss.status = 2, ss.lock_order_no = 1900000000000000010, ss.lock_expire_time = NULL
WHERE ss.screening_id = 11 AND s.row_num = 1 AND s.col_num IN (1, 2, 3);

UPDATE `t_screening_seat` ss
JOIN `t_seat` s ON s.id = ss.seat_id
SET ss.status = 2, ss.lock_order_no = 1900000000000000011, ss.lock_expire_time = NULL
WHERE ss.screening_id = 16 AND s.row_num = 1 AND s.col_num IN (1, 2);

-- ===== 演出订单 =====
INSERT INTO `t_show_order` (`order_no`, `user_id`, `show_id`, `show_title`, `venue_name`, `venue_address`, `start_time`, `total_amount`, `status`, `expire_time`, `pay_time`)
SELECT 1900000000000000003, 1, s.id, s.title, s.venue_name, s.venue_address, s.start_time, 1360.00, 1,
       DATE_ADD(NOW(), INTERVAL 15 MINUTE), NOW()
FROM `t_show` s WHERE s.id = 1;

INSERT INTO `t_show_order` (`order_no`, `user_id`, `show_id`, `show_title`, `venue_name`, `venue_address`, `start_time`, `total_amount`, `status`, `expire_time`, `pay_time`)
SELECT 1900000000000000004, 1, s.id, s.title, s.venue_name, s.venue_address, s.start_time, 798.00, 1,
       DATE_ADD(NOW(), INTERVAL 15 MINUTE), NOW()
FROM `t_show` s WHERE s.id = 4;

INSERT INTO `t_show_order` (`order_no`, `user_id`, `show_id`, `show_title`, `venue_name`, `venue_address`, `start_time`, `total_amount`, `status`, `expire_time`, `pay_time`)
SELECT 1900000000000000012, 2, s.id, s.title, s.venue_name, s.venue_address, s.start_time, 1160.00, 1,
       DATE_ADD(NOW(), INTERVAL 15 MINUTE), NOW()
FROM `t_show` s WHERE s.id = 2;

INSERT INTO `t_show_order` (`order_no`, `user_id`, `show_id`, `show_title`, `venue_name`, `venue_address`, `start_time`, `total_amount`, `status`, `expire_time`, `pay_time`)
SELECT 1900000000000000013, 3, s.id, s.title, s.venue_name, s.venue_address, s.start_time, 180.00, 0,
       DATE_ADD(NOW(), INTERVAL 15 MINUTE), NULL
FROM `t_show` s WHERE s.id = 3;

INSERT INTO `t_show_order` (`order_no`, `user_id`, `show_id`, `show_title`, `venue_name`, `venue_address`, `start_time`, `total_amount`, `status`, `expire_time`, `pay_time`)
SELECT 1900000000000000014, 4, s.id, s.title, s.venue_name, s.venue_address, s.start_time, 360.00, 2,
       DATE_SUB(NOW(), INTERVAL 10 MINUTE), NULL
FROM `t_show` s WHERE s.id = 5;

INSERT INTO `t_show_order` (`order_no`, `user_id`, `show_id`, `show_title`, `venue_name`, `venue_address`, `start_time`, `total_amount`, `status`, `expire_time`, `pay_time`)
SELECT 1900000000000000015, 1, s.id, s.title, s.venue_name, s.venue_address, s.start_time, 199.00, 3,
       DATE_SUB(NOW(), INTERVAL 30 MINUTE), NULL
FROM `t_show` s WHERE s.id = 4;

INSERT INTO `t_show_order_seat` (`order_no`, `seat_id`, `zone`, `seat_no`, `price`)
SELECT 1900000000000000003, seat.id, seat.zone, seat.seat_no, 680.00
FROM `t_show_seat` seat
WHERE seat.show_id = 1 AND seat.zone = 'A区' AND seat.seat_no IN (1, 2);

INSERT INTO `t_show_order_seat` (`order_no`, `seat_id`, `zone`, `seat_no`, `price`)
SELECT 1900000000000000004, seat.id, seat.zone, seat.seat_no, 399.00
FROM `t_show_seat` seat
WHERE seat.show_id = 4 AND seat.zone = 'VIP' AND seat.seat_no IN (1, 2);

INSERT INTO `t_show_order_seat` (`order_no`, `seat_id`, `zone`, `seat_no`, `price`)
SELECT 1900000000000000012, seat.id, seat.zone, seat.seat_no, 580.00
FROM `t_show_seat` seat
WHERE seat.show_id = 2 AND seat.zone = 'A区' AND seat.seat_no IN (3, 4);

INSERT INTO `t_show_order_seat` (`order_no`, `seat_id`, `zone`, `seat_no`, `price`)
SELECT 1900000000000000013, seat.id, seat.zone, seat.seat_no, 180.00
FROM `t_show_seat` seat
WHERE seat.show_id = 3 AND seat.zone = 'C区' AND seat.seat_no = 5;

INSERT INTO `t_show_order_seat` (`order_no`, `seat_id`, `zone`, `seat_no`, `price`)
SELECT 1900000000000000014, seat.id, seat.zone, seat.seat_no, 180.00
FROM `t_show_seat` seat
WHERE seat.show_id = 5 AND seat.zone = 'B区' AND seat.seat_no IN (1, 2);

INSERT INTO `t_show_order_seat` (`order_no`, `seat_id`, `zone`, `seat_no`, `price`)
SELECT 1900000000000000015, seat.id, seat.zone, seat.seat_no, 199.00
FROM `t_show_seat` seat
WHERE seat.show_id = 4 AND seat.zone = '普通' AND seat.seat_no = 10;

UPDATE `t_show_seat`
SET status = 2, lock_order_no = 1900000000000000003, lock_expire_time = NULL
WHERE show_id = 1 AND zone = 'A区' AND seat_no IN (1, 2);

UPDATE `t_show_seat`
SET status = 2, lock_order_no = 1900000000000000004, lock_expire_time = NULL
WHERE show_id = 4 AND zone = 'VIP' AND seat_no IN (1, 2);

UPDATE `t_show_seat`
SET status = 2, lock_order_no = 1900000000000000012, lock_expire_time = NULL
WHERE show_id = 2 AND zone = 'A区' AND seat_no IN (3, 4);

UPDATE `t_show_seat`
SET status = 1, lock_order_no = 1900000000000000013, lock_expire_time = DATE_ADD(NOW(), INTERVAL 15 MINUTE)
WHERE show_id = 3 AND zone = 'C区' AND seat_no = 5;

-- ===== 玩乐订单 =====
INSERT INTO `t_play_order` (`order_no`, `user_id`, `item_id`, `item_title`, `item_address`, `unit_price`, `quantity`, `total_amount`, `status`, `expire_time`, `pay_time`)
VALUES (1900000000000000005, 1, 1, '城市密室逃脱', '上海市静安区南京西路100号', 189.00, 2, 378.00, 1, DATE_ADD(NOW(), INTERVAL 15 MINUTE), NOW()),
       (1900000000000000016, 2, 2, '暖冬温泉', '上海市浦东新区温泉路8号', 289.00, 1, 289.00, 1, DATE_ADD(NOW(), INTERVAL 15 MINUTE), NOW()),
       (1900000000000000017, 3, 3, '亲子陶艺', '上海市徐汇区艺术街22号', 158.00, 2, 316.00, 0, DATE_ADD(NOW(), INTERVAL 15 MINUTE), NULL),
       (1900000000000000018, 4, 1, '城市密室逃脱', '上海市静安区南京西路100号', 189.00, 1, 189.00, 2, DATE_SUB(NOW(), INTERVAL 10 MINUTE), NULL),
       (1900000000000000019, 5, 2, '暖冬温泉', '上海市浦东新区温泉路8号', 289.00, 3, 867.00, 1, DATE_ADD(NOW(), INTERVAL 15 MINUTE), NOW()),
       (1900000000000000020, 1, 3, '亲子陶艺', '上海市徐汇区艺术街22号', 158.00, 1, 158.00, 3, DATE_SUB(NOW(), INTERVAL 30 MINUTE), NULL);

-- 已支付/待支付订单占用库存（取消、超时不占用）
UPDATE `t_play_item` SET stock = stock - 2 WHERE id = 1;
UPDATE `t_play_item` SET stock = stock - 4 WHERE id = 2;
UPDATE `t_play_item` SET stock = stock - 2 WHERE id = 3;
