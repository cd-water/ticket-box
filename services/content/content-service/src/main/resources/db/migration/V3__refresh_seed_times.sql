UPDATE t_screening
SET start_time = DATE_ADD(NOW(), INTERVAL id * 30 MINUTE),
    end_time   = DATE_ADD(DATE_ADD(NOW(), INTERVAL id * 30 MINUTE), INTERVAL 150 MINUTE);

UPDATE t_show
SET start_time = DATE_ADD(NOW(), INTERVAL id DAY);
