-- 初始化测试数据
-- 有密码账号的密码明文均为 Aa123456（BCrypt 加密存储）
-- 图片统一使用 https://github.com/cd-water.png

-- 用户：1 有密码；2-10 静默注册形态（无密码）；9 已禁用
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

-- 电影：1-2、6-10、16-17、21-26 热映；3-4、11-14、18-19、27-33 待映；5、15、20 已下架
INSERT INTO `t_movie` (`id`, `title`, `poster`, `synopsis`, `duration`, `release_date`, `status`)
VALUES (1, '星际征途', 'https://github.com/cd-water.png',
        '一支深空探险队穿越虫洞寻找人类新家园，途中遭遇未知文明。',
        142, DATE_SUB(NOW(), INTERVAL 20 DAY), 1),
       (2, '山海奇缘', 'https://github.com/cd-water.png',
        '少年意外唤醒上古神兽，踏上寻找山海图卷的冒险旅程。',
        118, DATE_SUB(NOW(), INTERVAL 3 DAY), 1),
       (3, '午夜列车', 'https://github.com/cd-water.png',
        '一列只在午夜出现的列车，载着乘客驶向各自未完成的心愿。',
        126, DATE_ADD(NOW(), INTERVAL 7 DAY), 1),
       (4, '夏日回声', 'https://github.com/cd-water.png',
        '海边小镇的夏天，两个少年用一台旧收音机记录彼此的秘密。',
        105, DATE_ADD(NOW(), INTERVAL 21 DAY), 1),
       (5, '旧日时光', 'https://github.com/cd-water.png',
        '老照相馆即将关闭，店主用最后一批底片拼凑出一座城市的记忆。',
        98, DATE_SUB(NOW(), INTERVAL 60 DAY), 0),
       (6, '极速追逐', 'https://github.com/cd-water.png',
        '一名卧底警探在最后七十二小时里追查跨境飙车集团。',
        121, DATE_SUB(NOW(), INTERVAL 12 DAY), 1),
       (7, '欢乐一家亲', 'https://github.com/cd-water.png',
        '三代同堂的一家人为了老宅拆迁闹出一连串啼笑皆非的故事。',
        108, DATE_SUB(NOW(), INTERVAL 7 DAY), 1),
       (8, '时光信笺', 'https://github.com/cd-water.png',
        '一封迟到十年的信，让两位旧友重新走进彼此的生活。',
        112, DATE_SUB(NOW(), INTERVAL 45 DAY), 1),
       (9, '雪线之上', 'https://github.com/cd-water.png',
        '高原哨所里，一群年轻士兵在风雪中守护着一条生命线。',
        134, DATE_SUB(NOW(), INTERVAL 8 DAY), 1),
       (10, '城市脉搏', 'https://github.com/cd-water.png',
        '镜头跟随十二位夜班工作者，记录一座城市入睡后的呼吸。',
        96, DATE_SUB(NOW(), INTERVAL 1 DAY), 1),
       (11, '末日风暴', 'https://github.com/cd-water.png',
        '超级风暴席卷沿海城市，一支救援队逆风挺进重灾区。',
        130, DATE_ADD(NOW(), INTERVAL 14 DAY), 1),
       (12, '星海彼岸', 'https://github.com/cd-water.png',
        '两名宇航员在深空失联，却通过延迟信号谈了一场跨光年的恋爱。',
        145, DATE_ADD(NOW(), INTERVAL 30 DAY), 1),
       (13, '笑闹江湖', 'https://github.com/cd-water.png',
        '一个不会武功的店小二被误认成武林盟主，从此笑料不断。',
        102, DATE_ADD(NOW(), INTERVAL 5 DAY), 1),
       (14, '无声告白', 'https://github.com/cd-water.png',
        '聋哑女孩与街头钢琴师用音乐完成了一次没有台词的告白。',
        115, DATE_ADD(NOW(), INTERVAL 45 DAY), 1),
       (15, '长夜将尽', 'https://github.com/cd-water.png',
        '一桩尘封二十年的悬案，在退休警察的最后一次走访中松动。',
        127, DATE_SUB(NOW(), INTERVAL 90 DAY), 0),
       (16, '云端之城', 'https://github.com/cd-water.png',
        '漂浮在云层之上的城市失去动力，少年们踏上修复核心的旅途。',
        119, DATE_SUB(NOW(), INTERVAL 25 DAY), 1),
       (17, '逆风飞翔', 'https://github.com/cd-water.png',
        '小镇少年组建滑翔伞队，向从未有人飞越的山口发起挑战。',
        110, DATE_SUB(NOW(), INTERVAL 15 DAY), 1),
       (18, '钢铁防线', 'https://github.com/cd-water.png',
        '一支工兵连在敌后坚守三天，为大部队转移赢得时间。',
        138, DATE_ADD(NOW(), INTERVAL 60 DAY), 1),
       (19, '时间旅人', 'https://github.com/cd-water.png',
        '每次回到过去都会失去一段记忆，他仍决定再见她一面。',
        124, DATE_ADD(NOW(), INTERVAL 18 DAY), 1),
       (20, '温暖的院子', 'https://github.com/cd-water.png',
        '一座老院子里，几户人家在一年四季里互相扶持。',
        100, DATE_SUB(NOW(), INTERVAL 120 DAY), 0),
       (21, '深海灯塔', 'https://github.com/cd-water.png',
        '一名灯塔守望者在风暴夜救起陌生潜水员，却发现对方带着海底秘密。',
        114, DATE_SUB(NOW(), INTERVAL 2 DAY), 1),
       (22, '荒原骑士', 'https://github.com/cd-water.png',
        '末世荒原上，一名独行骑士护送最后的种子库穿越沙暴。',
        128, DATE_SUB(NOW(), INTERVAL 4 DAY), 1),
       (23, '街角书店', 'https://github.com/cd-water.png',
        '即将关门的旧书店里，店主与常客们用一本书交换一段人生。',
        101, DATE_SUB(NOW(), INTERVAL 5 DAY), 1),
       (24, '星火燎原', 'https://github.com/cd-water.png',
        '战火中的一支通信班，用断断续续的电波守住最后一座城。',
        136, DATE_SUB(NOW(), INTERVAL 6 DAY), 1),
       (25, '雾港谜案', 'https://github.com/cd-water.png',
        '港口连续失踪案背后，警探发现所有线索都指向二十年前的沉船。',
        119, DATE_SUB(NOW(), INTERVAL 9 DAY), 1),
       (26, '春风十里', 'https://github.com/cd-water.png',
        '两个重逢的旧友在南方小城度过一个春天，重新学会告别。',
        107, DATE_SUB(NOW(), INTERVAL 10 DAY), 1),
       (27, '深空边境', 'https://github.com/cd-water.png',
        '人类在深空边境建立前哨站，一名舰长必须在救援与撤离间做选择。',
        143, DATE_ADD(NOW(), INTERVAL 3 DAY), 1),
       (28, '山海之外', 'https://github.com/cd-water.png',
        '少女跟随灵兽穿过山海图卷，寻找被时间抹去的故乡。',
        121, DATE_ADD(NOW(), INTERVAL 10 DAY), 1),
       (29, '无声证词', 'https://github.com/cd-water.png',
        '一名律师为失语证人辩护，在沉默中拼出案件真相。',
        116, DATE_ADD(NOW(), INTERVAL 12 DAY), 1),
       (30, '夏日终章', 'https://github.com/cd-water.png',
        '毕业前的最后一个夏天，两个少年决定把未说出口的话写进歌里。',
        104, DATE_ADD(NOW(), INTERVAL 25 DAY), 1),
       (31, '雪国来信', 'https://github.com/cd-water.png',
        '一封寄错地址的信，让两个陌生人在雪国小镇开始通信。',
        111, DATE_ADD(NOW(), INTERVAL 35 DAY), 1),
       (32, '铁血黎明', 'https://github.com/cd-water.png',
        '黎明前的阵地上，一支突击队必须在增援到达前守住高地。',
        139, DATE_ADD(NOW(), INTERVAL 40 DAY), 1),
       (33, '风筝与云', 'https://github.com/cd-water.png',
        '一个放风筝的孩子和一朵迷路的云，在天空展开一段冒险。',
        99, DATE_ADD(NOW(), INTERVAL 50 DAY), 1);

-- 电影类型字典
INSERT INTO `t_genre` (`id`, `name`, `sort_order`)
VALUES (1, '科幻', 1),
       (2, '冒险', 2),
       (3, '动画', 3),
       (4, '奇幻', 4),
       (5, '悬疑', 5),
       (6, '剧情', 6),
       (7, '青春', 7),
       (8, '家庭', 8),
       (9, '动作', 9),
       (10, '喜剧', 10),
       (11, '爱情', 11),
       (12, '战争', 12),
       (13, '纪录片', 13),
       (14, '灾难', 14);

-- 电影与类型关联
INSERT INTO `t_movie_genre` (`movie_id`, `genre_id`)
VALUES (1, 1), (1, 2),
       (2, 3), (2, 4),
       (3, 5), (3, 6),
       (4, 6), (4, 7),
       (5, 6), (5, 8),
       (6, 9), (6, 5),
       (7, 10), (7, 8),
       (8, 11), (8, 6),
       (9, 12), (9, 6),
       (10, 13),
       (11, 14), (11, 2),
       (12, 1), (12, 11),
       (13, 10), (13, 9),
       (14, 6), (14, 11),
       (15, 5), (15, 6),
       (16, 3), (16, 4),
       (17, 6), (17, 7),
       (18, 12), (18, 9),
       (19, 1), (19, 5),
       (20, 8), (20, 6),
       (21, 5), (21, 6),
       (22, 9), (22, 2),
       (23, 6), (23, 8),
       (24, 12), (24, 6),
       (25, 5), (25, 9),
       (26, 11), (26, 6),
       (27, 1), (27, 2),
       (28, 4), (28, 2),
       (29, 5), (29, 6),
       (30, 7), (30, 11),
       (31, 11), (31, 6),
       (32, 12), (32, 9),
       (33, 3), (33, 8);

-- 人员：导演与演员
INSERT INTO `t_person` (`id`, `name`, `avatar`, `intro`)
VALUES (1, '张远航', 'https://github.com/cd-water.png', '导演，擅长科幻与冒险题材。'),
       (2, '林晓', 'https://github.com/cd-water.png', '演员，戏路宽广，喜剧与正剧皆宜。'),
       (3, '陈默', 'https://github.com/cd-water.png', '演员，以细腻的情感表达见长。'),
       (4, '王海', 'https://github.com/cd-water.png', '导演，专注奇幻与家庭题材。'),
       (5, '李晴', 'https://github.com/cd-water.png', '演员，动画配音与真人表演均有涉猎。'),
       (6, '周野', 'https://github.com/cd-water.png', '演员，擅长悬疑与犯罪类型。'),
       (7, '赵岚', 'https://github.com/cd-water.png', '导演，作品风格冷峻。'),
       (8, '吴桐', 'https://github.com/cd-water.png', '导演，关注青春与成长题材。'),
       (9, '郑川', 'https://github.com/cd-water.png', '演员，舞台剧出身。'),
       (10, '何静', 'https://github.com/cd-water.png', '导演，纪录片与家庭题材创作者。'),
       (11, '高远', 'https://github.com/cd-water.png', '导演，动作片起家。'),
       (12, '许宁', 'https://github.com/cd-water.png', '演员，动作戏亲身上阵。'),
       (13, '白露', 'https://github.com/cd-water.png', '演员，新锐面孔。'),
       (14, '沈舟', 'https://github.com/cd-water.png', '导演，喜剧节奏把控出色。'),
       (15, '唐果', 'https://github.com/cd-water.png', '演员，喜剧天赋出众。'),
       (16, '顾言', 'https://github.com/cd-water.png', '导演，擅长爱情与都市题材。'),
       (17, '苏禾', 'https://github.com/cd-water.png', '演员，文艺片常客。'),
       (18, '陆鸣', 'https://github.com/cd-water.png', '导演，战争与历史题材研究者。'),
       (19, '韩川', 'https://github.com/cd-water.png', '演员，硬汉形象深入人心。'),
       (20, '罗晴', 'https://github.com/cd-water.png', '导演，纪录片导演。');

-- 电影演职人员：1-导演，2-演员
INSERT INTO `t_movie_crew` (`movie_id`, `person_id`, `duty`, `role_name`, `sort_order`)
VALUES (1, 1, 1, '', 0),
       (1, 2, 2, '陆衡', 1),
       (1, 3, 2, '阿岚', 2),
       (2, 4, 1, '', 0),
       (2, 5, 2, '阿木', 1),
       (3, 7, 1, '', 0),
       (3, 6, 2, '苏晚', 1),
       (4, 8, 1, '', 0),
       (4, 9, 2, '小满', 1),
       (5, 10, 1, '', 0),
       (5, 2, 2, '林叔', 1),
       (6, 11, 1, '', 0),
       (6, 12, 2, '程野', 1),
       (6, 13, 2, '阿杰', 2),
       (7, 14, 1, '', 0),
       (7, 15, 2, '老爸', 1),
       (8, 16, 1, '', 0),
       (8, 17, 2, '沈知', 1),
       (9, 18, 1, '', 0),
       (9, 19, 2, '连长', 1),
       (10, 20, 1, '', 0),
       (10, 12, 2, '讲述者', 1),
       (11, 1, 1, '', 0),
       (11, 13, 2, '幸存者', 1),
       (12, 4, 1, '', 0),
       (12, 11, 2, '宇航员', 1),
       (13, 14, 1, '', 0),
       (13, 15, 2, '小师妹', 1),
       (14, 8, 1, '', 0),
       (14, 17, 2, '林夏', 1),
       (15, 7, 1, '', 0),
       (15, 18, 2, '侦探', 1),
       (16, 16, 1, '', 0),
       (16, 5, 2, '云朵', 1),
       (17, 11, 1, '', 0),
       (17, 19, 2, '队长', 1),
       (18, 18, 1, '', 0),
       (18, 20, 2, '指挥官', 1),
       (19, 20, 1, '', 0),
       (19, 6, 2, '旅人', 1),
       (20, 10, 1, '', 0),
       (20, 3, 2, '老周', 1),
       (21, 7, 1, '', 0),
       (21, 6, 2, '守塔人', 1),
       (21, 13, 2, '记者', 2),
       (22, 11, 1, '', 0),
       (22, 12, 2, '骑士', 1),
       (22, 19, 2, '首领', 2),
       (23, 8, 1, '', 0),
       (23, 9, 2, '店主', 1),
       (23, 17, 2, '常客', 2),
       (24, 18, 1, '', 0),
       (24, 19, 2, '营长', 1),
       (24, 2, 2, '通讯员', 2),
       (25, 7, 1, '', 0),
       (25, 6, 2, '警探', 1),
       (25, 3, 2, '目击者', 2),
       (26, 16, 1, '', 0),
       (26, 17, 2, '林夏', 1),
       (26, 2, 2, '周明', 2),
       (27, 1, 1, '', 0),
       (27, 5, 2, '舰长', 1),
       (27, 12, 2, '驾驶员', 2),
       (28, 4, 1, '', 0),
       (28, 5, 2, '灵兽', 1),
       (28, 13, 2, '少女', 2),
       (29, 7, 1, '', 0),
       (29, 6, 2, '律师', 1),
       (29, 9, 2, '证人', 2),
       (30, 8, 1, '', 0),
       (30, 17, 2, '夏禾', 1),
       (30, 13, 2, '陈夏', 2),
       (31, 16, 1, '', 0),
       (31, 17, 2, '沈冬', 1),
       (31, 3, 2, '邮差', 2),
       (32, 18, 1, '', 0),
       (32, 19, 2, '连长', 1),
       (32, 12, 2, '突击手', 2),
       (33, 10, 1, '', 0),
       (33, 5, 2, '风筝', 1),
       (33, 15, 2, '小云', 2);

-- 影评：1-5 正常（含楼中楼），6 用户已删除，7 管理员已隐藏；其余覆盖多部电影
INSERT INTO `t_movie_comment` (`id`, `movie_id`, `user_id`, `root_id`, `parent_id`, `content`, `status`, `create_time`)
VALUES (1, 1, 1, 0, 0, '特效很震撼，IMAX 值得一看。', 1, DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
       (2, 1, 2, 0, 0, '剧情节奏有点拖，但整体不错。', 1, DATE_SUB(NOW(), INTERVAL 25 MINUTE)),
       (3, 1, 2, 1, 1, '同意，尤其是太空站那一段。', 1, DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
       (4, 1, 1, 1, 3, '那段配乐也很加分。', 1, DATE_SUB(NOW(), INTERVAL 15 MINUTE)),
       (5, 1, 2, 2, 2, '后半段确实有点慢。', 1, DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
       (6, 1, 1, 0, 0, '这条评论已被用户删除。', 2, DATE_SUB(NOW(), INTERVAL 8 MINUTE)),
       (7, 1, 2, 0, 0, '这条评论已被管理员隐藏。', 0, DATE_SUB(NOW(), INTERVAL 5 MINUTE)),
       (8, 2, 1, 0, 0, '适合带孩子一起看。', 1, DATE_SUB(NOW(), INTERVAL 3 MINUTE)),
       (9, 3, 3, 0, 0, '预告片氛围感拉满。', 1, DATE_SUB(NOW(), INTERVAL 40 MINUTE)),
       (10, 3, 4, 9, 9, '希望正片别剧透太多。', 1, DATE_SUB(NOW(), INTERVAL 38 MINUTE)),
       (11, 3, 5, 0, 0, '悬疑片爱好者狂喜。', 1, DATE_SUB(NOW(), INTERVAL 35 MINUTE)),
       (12, 4, 3, 0, 0, '青春片就该有夏天的味道。', 1, DATE_SUB(NOW(), INTERVAL 50 MINUTE)),
       (13, 4, 6, 12, 12, '海报很有感觉。', 1, DATE_SUB(NOW(), INTERVAL 48 MINUTE)),
       (14, 6, 4, 0, 0, '追车戏拍得很过瘾。', 1, DATE_SUB(NOW(), INTERVAL 60 MINUTE)),
       (15, 6, 2, 14, 14, 'IMAX 看更爽。', 1, DATE_SUB(NOW(), INTERVAL 58 MINUTE)),
       (16, 6, 7, 14, 15, '已经二刷了。', 1, DATE_SUB(NOW(), INTERVAL 55 MINUTE)),
       (17, 7, 5, 0, 0, '笑点密集，适合全家一起看。', 1, DATE_SUB(NOW(), INTERVAL 70 MINUTE)),
       (18, 7, 8, 0, 0, '有些桥段略老套，但好笑。', 1, DATE_SUB(NOW(), INTERVAL 68 MINUTE)),
       (19, 7, 5, 18, 18, '结尾彩蛋别错过。', 1, DATE_SUB(NOW(), INTERVAL 65 MINUTE)),
       (20, 8, 9, 0, 0, '很温柔的片子。', 1, DATE_SUB(NOW(), INTERVAL 80 MINUTE)),
       (21, 8, 10, 20, 20, '配乐加分。', 1, DATE_SUB(NOW(), INTERVAL 78 MINUTE)),
       (22, 9, 3, 0, 0, '战争场面很克制，反而更震撼。', 1, DATE_SUB(NOW(), INTERVAL 90 MINUTE)),
       (23, 9, 6, 0, 0, '历史细节考究。', 1, DATE_SUB(NOW(), INTERVAL 88 MINUTE)),
       (24, 10, 4, 0, 0, '纪录片视角很独特。', 1, DATE_SUB(NOW(), INTERVAL 100 MINUTE)),
       (25, 10, 8, 24, 24, '希望多排片。', 1, DATE_SUB(NOW(), INTERVAL 95 MINUTE)),
       (26, 16, 5, 0, 0, '画面太美了。', 1, DATE_SUB(NOW(), INTERVAL 110 MINUTE)),
       (27, 16, 7, 26, 26, '适合大银幕。', 1, DATE_SUB(NOW(), INTERVAL 105 MINUTE)),
       (28, 17, 2, 0, 0, '青春热血，推荐。', 1, DATE_SUB(NOW(), INTERVAL 120 MINUTE)),
       (29, 17, 9, 28, 28, '主角演技在线。', 1, DATE_SUB(NOW(), INTERVAL 118 MINUTE)),
       (30, 17, 10, 28, 29, '同感。', 1, DATE_SUB(NOW(), INTERVAL 115 MINUTE)),
       (31, 2, 3, 8, 8, '带娃看很合适。', 1, DATE_SUB(NOW(), INTERVAL 2 MINUTE)),
       (32, 2, 4, 0, 0, '神兽设计很可爱。', 1, DATE_SUB(NOW(), INTERVAL 1 MINUTE)),
       (33, 1, 5, 0, 0, '已经三刷，细节很多。', 1, DATE_SUB(NOW(), INTERVAL 130 MINUTE)),
       (34, 1, 6, 33, 33, '二刷发现好多伏笔。', 1, DATE_SUB(NOW(), INTERVAL 128 MINUTE)),
       (35, 1, 7, 33, 34, '比如开头那段广播。', 1, DATE_SUB(NOW(), INTERVAL 125 MINUTE)),
       (36, 1, 8, 0, 0, '配乐单曲循环中。', 1, DATE_SUB(NOW(), INTERVAL 122 MINUTE)),
       (37, 1, 9, 0, 0, '这条已被管理员隐藏。', 0, DATE_SUB(NOW(), INTERVAL 120 MINUTE)),
       (38, 1, 10, 0, 0, '已删除测试。', 2, DATE_SUB(NOW(), INTERVAL 119 MINUTE)),
       (39, 3, 9, 0, 0, '坐等上映。', 1, DATE_SUB(NOW(), INTERVAL 140 MINUTE)),
       (40, 3, 10, 39, 39, '一起？', 1, DATE_SUB(NOW(), INTERVAL 138 MINUTE));

-- 影评点赞：仅对正常状态的影评点赞，便于验证“最热”排序
INSERT INTO `t_movie_comment_like` (`comment_id`, `user_id`)
VALUES (1, 2), (1, 3), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10),
       (2, 1), (2, 3), (2, 4),
       (3, 1), (3, 3),
       (4, 2), (4, 3), (4, 4),
       (5, 1),
       (8, 2), (8, 3), (8, 4), (8, 5),
       (9, 1), (9, 4), (9, 5), (9, 6), (9, 7),
       (10, 3), (10, 5),
       (11, 3), (11, 4),
       (12, 4), (12, 5), (12, 6),
       (13, 3),
       (14, 2), (14, 5), (14, 6), (14, 7),
       (15, 4), (15, 6),
       (16, 2), (16, 4),
       (17, 6), (17, 7), (17, 8),
       (18, 5), (18, 6), (18, 7),
       (19, 8),
       (20, 1), (20, 10),
       (21, 9),
       (22, 4), (22, 6), (22, 9),
       (23, 3), (23, 4),
       (24, 3), (24, 5), (24, 8),
       (25, 4),
       (26, 6), (26, 7),
       (27, 5),
       (28, 1), (28, 3), (28, 9),
       (29, 2),
       (30, 2), (30, 9),
       (31, 1), (31, 2),
       (32, 1), (32, 5), (32, 6),
       (33, 1), (33, 2), (33, 4), (33, 6), (33, 7),
       (34, 1), (34, 5),
       (35, 5),
       (36, 1), (36, 2), (36, 3), (36, 4),
       (39, 1), (39, 2), (39, 3),
       (40, 9);

-- 根据点赞明细回填点赞数
UPDATE `t_movie_comment` c
SET c.like_count = (SELECT COUNT(*) FROM `t_movie_comment_like` l WHERE l.comment_id = c.id);

-- 根据回复明细回填根影评的回复数（仅统计正常状态）
UPDATE `t_movie_comment` c
    LEFT JOIN (SELECT root_id, COUNT(*) AS cnt
               FROM `t_movie_comment`
               WHERE root_id <> 0
                 AND status = 1
               GROUP BY root_id) r ON r.root_id = c.id
SET c.reply_count = COALESCE(r.cnt, 0)
WHERE c.root_id = 0;

-- 影院
INSERT INTO `t_cinema` (`id`, `name`, `address`, `phone`, `status`)
VALUES (1, '星光国际影城（人民广场店）', '人民路88号', '021-88880001', 1),
       (2, '万象影城（滨江店）', '滨江大道120号', '021-88880002', 1),
       (3, '幸福蓝海影城（大学城店）', '学府路66号', '021-88880003', 1),
       (4, '橙天嘉禾影城（中心店）', '中山路200号', '021-88880004', 1),
       (5, '博纳国际影城（科技园店）', '科创路18号', '021-88880005', 1),
       (6, '大地影院（老城店）', '解放路35号', '021-88880006', 1),
       (7, '横店电影城（新城店）', '湖滨路9号', '021-88880007', 1),
       (8, '金逸影城（购物中心店）', '购物中心5层', '021-88880008', 1),
       (9, '中影国际影城（文化宫店）', '文化宫路12号', '021-88880009', 1),
       (10, '耀莱成龙影城（东站店）', '东站广场3层', '021-88880010', 1),
       (11, '保利国际影城（北湖店）', '北湖路77号', '021-88880011', 1),
       (12, 'SFC上影影城（南岸店）', '南岸路101号', '021-88880012', 1);

-- 影厅：每个影院 2 个影厅
INSERT INTO `t_hall` (`id`, `cinema_id`, `name`, `status`)
VALUES (1, 1, '1号厅', 1), (2, 1, '2号厅', 1),
       (3, 2, '1号厅', 1), (4, 2, '2号厅', 1),
       (5, 3, '1号厅', 1), (6, 3, '2号厅', 1),
       (7, 4, '1号厅', 1), (8, 4, '2号厅', 1),
       (9, 5, '1号厅', 1), (10, 5, '2号厅', 1),
       (11, 6, '1号厅', 1), (12, 6, '2号厅', 1),
       (13, 7, '1号厅', 1), (14, 7, '2号厅', 1),
       (15, 8, '1号厅', 1), (16, 8, '2号厅', 1),
       (17, 9, '1号厅', 1), (18, 9, '2号厅', 1),
       (19, 10, '1号厅', 1), (20, 10, '2号厅', 1),
       (21, 11, '1号厅', 1), (22, 11, '2号厅', 1),
       (23, 12, '1号厅', 1), (24, 12, '2号厅', 1);

-- 座位：所有影厅统一 10 排 × 12 座
INSERT INTO `t_seat` (`hall_id`, `row_num`, `col_num`)
SELECT h.id, r.n, c.n
FROM `t_hall` h
CROSS JOIN (SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
            UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
            UNION ALL SELECT 9 UNION ALL SELECT 10) r
CROSS JOIN (SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
            UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
            UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12) c;

-- 排场：每个影院 2 个未来场次，仅安排热映电影
INSERT INTO `t_screening` (`id`, `cinema_id`, `hall_id`, `movie_id`, `start_time`, `end_time`, `price`, `status`)
SELECT sch.id, sch.cinema_id, sch.hall_id, sch.movie_id,
       DATE_ADD(NOW(), INTERVAL sch.start_min MINUTE),
       DATE_ADD(DATE_ADD(NOW(), INTERVAL sch.start_min MINUTE), INTERVAL m.duration MINUTE),
       sch.price, 1
FROM (SELECT 1 AS id, 1 AS cinema_id, 1 AS hall_id, 1 AS movie_id, 120 AS start_min, 49.90 AS price
      UNION ALL SELECT 2, 1, 2, 2, 180, 45.90
      UNION ALL SELECT 3, 2, 3, 6, 120, 52.90
      UNION ALL SELECT 4, 2, 4, 7, 200, 39.90
      UNION ALL SELECT 5, 3, 5, 8, 150, 42.90
      UNION ALL SELECT 6, 3, 6, 9, 220, 55.90
      UNION ALL SELECT 7, 4, 7, 10, 120, 38.90
      UNION ALL SELECT 8, 4, 8, 16, 240, 48.90
      UNION ALL SELECT 9, 5, 9, 17, 130, 46.90
      UNION ALL SELECT 10, 5, 10, 21, 190, 51.90
      UNION ALL SELECT 11, 6, 11, 22, 140, 44.90
      UNION ALL SELECT 12, 6, 12, 23, 210, 36.90
      UNION ALL SELECT 13, 7, 13, 24, 120, 58.90
      UNION ALL SELECT 14, 7, 14, 25, 180, 49.90
      UNION ALL SELECT 15, 8, 15, 26, 150, 43.90
      UNION ALL SELECT 16, 8, 16, 1, 230, 54.90
      UNION ALL SELECT 17, 9, 17, 2, 120, 41.90
      UNION ALL SELECT 18, 9, 18, 6, 200, 47.90
      UNION ALL SELECT 19, 10, 19, 7, 130, 39.90
      UNION ALL SELECT 20, 10, 20, 8, 190, 45.90
      UNION ALL SELECT 21, 11, 21, 9, 140, 53.90
      UNION ALL SELECT 22, 11, 22, 10, 220, 37.90
      UNION ALL SELECT 23, 12, 23, 16, 160, 50.90
      UNION ALL SELECT 24, 12, 24, 17, 250, 44.90
      UNION ALL SELECT 25, 1, 1, 1, 300, 49.90
      UNION ALL SELECT 26, 1, 2, 2, 330, 45.90) sch
JOIN `t_movie` m ON m.id = sch.movie_id;

-- 每个排场按影厅座位生成座位快照
INSERT INTO `t_screening_seat` (`screening_id`, `seat_id`)
SELECT sc.id, s.id
FROM `t_screening` sc
JOIN `t_seat` s ON s.hall_id = sc.hall_id;

-- 订单：1 个待支付、1 个已支付，均使用 1 号排场
INSERT INTO `t_order` (`id`, `order_no`, `user_id`, `screening_id`, `total_amount`, `status`, `expire_time`)
VALUES (1, 1900000000000000001, 1, 1, 99.80, 0, DATE_ADD(NOW(), INTERVAL 15 MINUTE)),
       (2, 1900000000000000002, 1, 1, 99.80, 1, DATE_ADD(NOW(), INTERVAL 15 MINUTE));

INSERT INTO `t_order_seat` (`order_id`, `seat_id`, `row_num`, `col_num`, `price`)
SELECT 1, s.id, s.row_num, s.col_num, 49.90
FROM `t_seat` s
WHERE s.hall_id = 1 AND s.row_num = 1 AND s.col_num IN (1, 2);

INSERT INTO `t_order_seat` (`order_id`, `seat_id`, `row_num`, `col_num`, `price`)
SELECT 2, s.id, s.row_num, s.col_num, 49.90
FROM `t_seat` s
WHERE s.hall_id = 1 AND s.row_num = 1 AND s.col_num IN (3, 4);

-- 待支付订单锁定 1-2 号座，已支付订单占用 3-4 号座
UPDATE `t_screening_seat` ss
JOIN `t_seat` s ON s.id = ss.seat_id
SET ss.status = 1,
    ss.lock_order_id = 1,
    ss.lock_expire_time = DATE_ADD(NOW(), INTERVAL 15 MINUTE)
WHERE ss.screening_id = 1 AND s.row_num = 1 AND s.col_num IN (1, 2);

UPDATE `t_screening_seat` ss
JOIN `t_seat` s ON s.id = ss.seat_id
SET ss.status = 2,
    ss.lock_order_id = 2,
    ss.lock_expire_time = NULL
WHERE ss.screening_id = 1 AND s.row_num = 1 AND s.col_num IN (3, 4);
