-- 基础测试数据
-- 有密码账号的密码明文均为 Aa123456（BCrypt 加密存储）
-- 图片统一使用 https://github.com/cd-water.png

-- 用户：1 有密码；2-10 静默注册形态（无密码）；9 已停用
INSERT INTO `t_user` (`id`, `phone`, `password`, `nickname`, `avatar`, `status`)
VALUES (1, '13800000001', '$2a$10$GIo5IeU8h/hoUoXFt6sFOu91qMy.0QrjdS6zO0jcC9znCQ.5cIvpC', 'user_mjafo8', 'https://github.com/cd-water.png', 1),
       (2, '13800000002', NULL, 'user_214neo', 'https://github.com/cd-water.png', 1),
       (3, '13800000003', NULL, 'user_3a7k2m', 'https://github.com/cd-water.png', 1),
       (4, '13800000004', NULL, 'user_9x2p4q', 'https://github.com/cd-water.png', 1),
       (5, '13800000005', NULL, 'user_5m8v1c', 'https://github.com/cd-water.png', 1),
       (6, '13800000006', NULL, 'user_7q3n6z', 'https://github.com/cd-water.png', 1),
       (7, '13800000007', NULL, 'user_2w9r5t', 'https://github.com/cd-water.png', 1),
       (8, '13800000008', NULL, 'user_8e4y7u', 'https://github.com/cd-water.png', 1),
       (9, '13800000009', NULL, 'user_6i1o3p', 'https://github.com/cd-water.png', 0),
       (10, '13800000010', NULL, 'user_0k5j8h', 'https://github.com/cd-water.png', 1);

-- 管理员：账号 admin，密码 Aa123456
INSERT INTO `t_admin` (`id`, `username`, `password`)
VALUES (1, 'admin', '$2a$10$GIo5IeU8h/hoUoXFt6sFOu91qMy.0QrjdS6zO0jcC9znCQ.5cIvpC');

-- 轮播图：链接统一指向 GitHub 占位
INSERT INTO `t_banner` (`id`, `image_url`, `link_url`, `sort_order`, `status`)
VALUES (1, 'https://github.com/cd-water.png', 'https://github.com/cd-water', 1, 1),
       (2, 'https://github.com/cd-water.png', 'https://github.com/cd-water', 2, 1),
       (3, 'https://github.com/cd-water.png', 'https://github.com/cd-water', 3, 1),
       (4, 'https://github.com/cd-water.png', 'https://github.com/cd-water', 4, 1),
       (5, 'https://github.com/cd-water.png', 'https://github.com/cd-water', 5, 1),
       (6, 'https://github.com/cd-water.png', 'https://github.com/cd-water', 6, 0);
