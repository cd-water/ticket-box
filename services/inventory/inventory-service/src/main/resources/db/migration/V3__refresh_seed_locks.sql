UPDATE t_screening_seat
SET lock_expire_time = DATE_ADD(NOW(), INTERVAL 15 MINUTE)
WHERE status = 1;

UPDATE t_show_seat
SET lock_expire_time = DATE_ADD(NOW(), INTERVAL 15 MINUTE)
WHERE status = 1;
