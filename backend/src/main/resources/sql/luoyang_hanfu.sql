/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : luoyang_hanfu

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 09/04/2026 21:22:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '管理员账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（BCrypt）',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ADMIN' COMMENT '角色（SUPER_ADMIN/ADMIN）',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1启用，0禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'superadmin', '$2a$12$H2FiOcLqu/yLdRrDikNcx.YVaJXPx7qRMXHvUtN/1nOeaAaDQyBXu', 'SUPER_ADMIN', 1, '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `admin` VALUES (2, 'admin', '$2a$12$H2FiOcLqu/yLdRrDikNcx.YVaJXPx7qRMXHvUtN/1nOeaAaDQyBXu', 'ADMIN', 1, '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);

-- ----------------------------
-- Table structure for attraction
-- ----------------------------
DROP TABLE IF EXISTS `attraction`;
CREATE TABLE `attraction`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '景点名称',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '地址',
  `recommend_hanfu_id` bigint NULL DEFAULT NULL COMMENT '推荐汉服ID',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_attraction_hanfu`(`recommend_hanfu_id` ASC) USING BTREE,
  CONSTRAINT `fk_attraction_hanfu` FOREIGN KEY (`recommend_hanfu_id`) REFERENCES `hanfu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '景点推荐表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of attraction
-- ----------------------------
INSERT INTO `attraction` VALUES (1, '丽景门', '洛阳市老城区中州东路', 1, '/uploads/attraction/ljm.jpg', '适合唐制夜景拍摄', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `attraction` VALUES (2, '龙门石窟', '洛阳市洛龙区龙门中街', 4, '/uploads/attraction/lmg.jpg', '适合宋制、明制简约款', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `image` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片地址',
  `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '/booking' COMMENT '跳转链接',
  `sort` int NOT NULL DEFAULT 1 COMMENT '排序',
  `enabled` tinyint NOT NULL DEFAULT 1 COMMENT '启用状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '首页轮播图' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES (1, '唐制汉服 · 门店实拍', '/uploads/hanfu/8399371e-988d-4c28-aba3-da4623b5dc29.jpg', '/booking', 1, 1, '2026-04-08 21:45:52', '2026-04-09 14:57:29', 0);
INSERT INTO `banner` VALUES (2, '宋制汉服 · 日常出片', '/uploads/hanfu/8f3bbc82-c97c-4a3b-82da-11f3d30ea668.jpg', '/booking', 2, 1, '2026-04-08 21:45:52', '2026-04-09 14:57:35', 0);
INSERT INTO `banner` VALUES (3, '魏晋风 · 宽松飘逸', '/uploads/hanfu/16f1bbfb-3348-44bf-838b-761d7c77ef6c.jpg', '/booking', 3, 1, '2026-04-08 21:45:52', '2026-04-09 14:57:43', 0);

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'MESSAGE' COMMENT '类型',
  `rating` int NULL DEFAULT 5 COMMENT '评分 (1-5)',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `images` json NULL COMMENT '图片列表',
  `reply_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '回复',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_feedback_user`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_feedback_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '反馈留言表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of feedback
-- ----------------------------

-- ----------------------------
-- Table structure for guide
-- ----------------------------
DROP TABLE IF EXISTS `guide`;
CREATE TABLE `guide`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '涓婚敭ID',
  `style` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '绌挎惌椋庢牸',
  `scenic` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '閫傚悎鏅?偣',
  `reason` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '鎼?厤鐞嗙敱',
  `photography_tips` json NULL COMMENT '鎷嶆憚璐村＋',
  `image_before` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '椋庢牸绀轰緥鍥',
  `image_after` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '瀹炴櫙鏁堟灉鍥',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '閫昏緫鍒犻櫎',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '娲涢槼鏃呮父鏀荤暐琛' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of guide
-- ----------------------------
INSERT INTO `guide` VALUES (1, '初唐 · 齐胸襦裙', '应天门', '应天门的宏大建筑语汇极具气势，初唐齐胸襦裙色彩明快、裁大气，二者结合能够完美还原神都武周时期的盛世繁华。', '[\"利用中心轴线对称构构图\", \"夕阳余晖增加质感\", \"仰角拍摄体现气势\"]', '/uploads/hanfu/cfa78966-d9e5-41d1-9202-ddc2d5777d1e.jpg', '/uploads/hanfu/3b57737b-4117-4d4f-b3ab-6cea391fae4e.jpg', '2026-04-09 10:41:51', '2026-04-09 11:35:14', 0);
INSERT INTO `guide` VALUES (2, '魏晋 · 大袖衫', '龙门石窟', '魏晋名士风度与龙门千年石刻的苍劲之美不谋而合。大袖衫的轻盈飘逸能中和石壁的厚重感。', '[\"利用自然光影雕琢褶皱\", \"动态捕捉袖摆飞扬\", \"在大佛下留白拍摄\"]', '/uploads/hanfu/7f5e604a-a2a0-4468-8091-3ca678653df3.jpg', '/uploads/hanfu/4ed4c471-10c6-499f-9aeb-6b0c69bd2dac.jpg', '2026-04-09 10:41:51', '2026-04-09 11:35:11', 0);
INSERT INTO `guide` VALUES (3, '明制 · 马面裙', '洛邑古城', '洛邑古城的红墙黛瓦与明制马面裙的华丽端庄相得益彰，最能体现东方古典韵味。', '[\"红墙作为纯色背景\", \"侧身回眸增加动感\", \"局部特写刺绣细节\"]', '/uploads/hanfu/86455fad-f646-4367-bba4-654454ab1fa7.jpg', '/uploads/hanfu/86455fad-f646-4367-bba4-654454ab1fa7.jpg', '2026-04-09 10:48:56', '2026-04-09 11:35:16', 0);
INSERT INTO `guide` VALUES (4, '宋制 · 褙子', '天街', '天街的繁华景象与宋制汉服的清新自然相得益彰，展现出一幅活生生的宋代市井画卷。', '[\"抓拍行走的动态\", \"利用摊位作为背景\", \"特写手持道具\"]', '/uploads/hanfu/c5098828-045d-4ea7-ab38-9c010abdea18.jpg', '/uploads/hanfu/c5098828-045d-4ea7-ab38-9c010abdea18.jpg', '2026-04-09 10:48:56', '2026-04-09 11:29:45', 0);
INSERT INTO `guide` VALUES (5, '大唐 · 披帛', '明堂天堂', '明堂天堂的正气庄严与披帛的华瑞之美结合，展现出大唐盛世的雍容华贵。', '[\"俯视角度增加冲击力\", \"特写金边纹理\", \"长镜头记录走动姿态\"]', '/uploads/hanfu/dc216cf7-7f9b-4915-a590-cdece0e3fc61.jpg', '/uploads/hanfu/ba0876e6-f2c5-4edb-a75f-8ebcb82198e2.jpg', '2026-04-09 11:35:23', '2026-04-09 11:41:27', 0);
INSERT INTO `guide` VALUES (6, '绝美 · 洛神赋', '洛河边', '洛河之畔的水波粼粼与洛神赋主题汉服的灵动飘逸相呼应，再现曹植笔下的凄美浪漫。', '[\"利用水面反光\", \"动态捕捉长裙摆\", \"特写眼神神韵\"]', '/uploads/hanfu/fb6546ae-aed1-44c7-af06-3bde50e1fe47.jpg', '/uploads/hanfu/e1ebd9f1-9ab7-4f7a-b991-2ea78a63ae18.jpg', '2026-04-09 16:20:52', '2026-04-09 16:20:52', 0);

-- ----------------------------
-- Table structure for hanfu
-- ----------------------------
DROP TABLE IF EXISTS `hanfu`;
CREATE TABLE `hanfu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '汉服名称',
  `dynasty` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '朝代（唐制/宋制/明制/魏晋/洛阳特色）',
  `color` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主色系',
  `image_list` json NULL COMMENT '图片列表',
  `size` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '尺码',
  `rent_price` decimal(10, 2) NOT NULL COMMENT '租赁价',
  `stock` int NOT NULL DEFAULT 0 COMMENT '库存',
  `on_shelf` tinyint NOT NULL DEFAULT 1 COMMENT '上架：1是，0否',
  `hot_score` int NOT NULL DEFAULT 0 COMMENT '热度分',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dynasty`(`dynasty` ASC) USING BTREE,
  INDEX `idx_on_shelf`(`on_shelf` ASC) USING BTREE,
  INDEX `idx_hot`(`hot_score` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '汉服信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hanfu
-- ----------------------------
INSERT INTO `hanfu` VALUES (1, '牡丹霓裳·唐制齐胸襦裙', '唐制', '绯红', '[\"/uploads/hanfu/afc3e767-aa46-426f-b4f0-75c6e6994720.jpg\"]', 'S/M/L', 168.00, 18, 1, 95, '适合古城夜景与灯会拍摄。', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `hanfu` VALUES (2, '上阳花月·唐制大袖衫', '唐制', '鎏金白', '[\"/uploads/hanfu/2c48d4cf-ee40-43cc-84cb-91a32262839e.jpg\"]', 'M/L/XL', 198.00, 12, 1, 90, '适合应天门建筑群拍摄。', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `hanfu` VALUES (3, '洛水流光·唐制半臂套', '唐制', '雾青', '[\"/uploads/hanfu/39087886-2630-4822-8598-389173881304.jpg\"]', 'S/M/L', 148.00, 19, 1, 83, '轻便外出款。', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `hanfu` VALUES (4, '汴京清影·宋制褙子裙', '宋制', '月白', '[\"/uploads/hanfu/c5098828-045d-4ea7-ab38-9c010abdea18.jpg\"]', 'S/M/L', 188.00, 14, 1, 88, '素雅风格。', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `hanfu` VALUES (5, '烟雨临川·宋制对襟衫', '宋制', '水绿', '[\"/uploads/hanfu/4feaa750-2349-456f-82dc-0206e3e75b0d.jpg\"]', 'M/L', 158.00, 16, 1, 80, '春夏轻旅拍。', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `hanfu` VALUES (6, '金阙华章·明制马面裙', '明制', '朱红', '[\"/uploads/hanfu/86455fad-f646-4367-bba4-654454ab1fa7.jpg\"]', 'M/L/XL', 228.00, 9, 1, 93, '节庆活动热门款。', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `hanfu` VALUES (7, '长安夜宴·明制圆领袍', '明制', '墨黑', '[\"/uploads/hanfu/198d2762-4c4d-41ee-b568-f1acab1b334a.jpg\"]', 'L/XL', 218.00, 7, 1, 86, '夜景对比表现好。', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `hanfu` VALUES (8, '竹间逸客·魏晋交领襦裙', '魏晋', '青灰', '[\"/uploads/hanfu/fb11a5b0-22fb-4fe0-a267-ec0c82ed2501.jpg\"]', 'S/M/L', 182.00, 11, 1, 79, '飘逸自然风。', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `hanfu` VALUES (9, '风过兰亭·魏晋广袖套', '魏晋', '素白', '[\"/uploads/hanfu/f7226087-b3e8-442f-91d8-46ea36f815bf.jpg\"]', 'M/L', 192.00, 9, 1, 83, '古风园林拍摄推荐。', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `hanfu` VALUES (10, '神都华彩·洛阳特色联名款', '洛阳特色', '宫墙红', '[\"/uploads/hanfu/9d04a19e-e70e-4a95-a49c-91e3ee8202b0.jpg\"]', 'M/L', 248.00, 5, 1, 94, '洛阳主题联名款。', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);

-- ----------------------------
-- Table structure for notification
-- ----------------------------
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单ID',
  `channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '渠道',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SENT' COMMENT '状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '通知记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notification
-- ----------------------------
INSERT INTO `notification` VALUES (1, 1, 1, 'SMS', '预约成功', '您的订单 LYHF202604080001 已预约成功。', 'SENT', '2026-04-08 21:45:52');
INSERT INTO `notification` VALUES (2, 2, 2, 'SMS', '取衣提醒', '您的订单 LYHF202604080002 今日可到店取衣。', 'SENT', '2026-04-08 21:45:52');

-- ----------------------------
-- Table structure for recommend_config
-- ----------------------------
DROP TABLE IF EXISTS `recommend_config`;
CREATE TABLE `recommend_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `hanfu_id` bigint NOT NULL COMMENT '汉服ID',
  `is_new` tinyint NOT NULL DEFAULT 0 COMMENT '是否新品',
  `recommended` tinyint NOT NULL DEFAULT 0 COMMENT '是否推荐',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_recommend_hanfu`(`hanfu_id` ASC) USING BTREE,
  CONSTRAINT `fk_recommend_hanfu` FOREIGN KEY (`hanfu_id`) REFERENCES `hanfu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '新品推荐配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of recommend_config
-- ----------------------------
INSERT INTO `recommend_config` VALUES (1, 1, 1, 0, '2026-04-08 21:45:52', '2026-04-09 17:01:42');
INSERT INTO `recommend_config` VALUES (2, 4, 1, 1, '2026-04-08 21:45:52', '2026-04-09 17:01:42');
INSERT INTO `recommend_config` VALUES (3, 6, 1, 1, '2026-04-08 21:45:52', '2026-04-09 17:01:42');
INSERT INTO `recommend_config` VALUES (4, 10, 1, 1, '2026-04-08 21:45:52', '2026-04-09 17:01:42');
INSERT INTO `recommend_config` VALUES (5, 2, 1, 1, '2026-04-09 15:04:52', '2026-04-09 17:01:42');
INSERT INTO `recommend_config` VALUES (6, 7, 1, 1, '2026-04-09 15:04:53', '2026-04-09 17:01:42');
INSERT INTO `recommend_config` VALUES (7, 9, 1, 1, '2026-04-09 15:04:53', '2026-04-09 17:01:42');
INSERT INTO `recommend_config` VALUES (8, 3, 1, 1, '2026-04-09 15:04:53', '2026-04-09 17:01:42');
INSERT INTO `recommend_config` VALUES (9, 5, 0, 0, '2026-04-09 15:04:53', '2026-04-09 17:01:42');
INSERT INTO `recommend_config` VALUES (10, 8, 0, 0, '2026-04-09 15:04:53', '2026-04-09 17:01:42');

-- ----------------------------
-- Table structure for reservation_order
-- ----------------------------
DROP TABLE IF EXISTS `reservation_order`;
CREATE TABLE `reservation_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `hanfu_id` bigint NOT NULL COMMENT '汉服ID',
  `reservation_date` date NOT NULL COMMENT '预约日期',
  `time_slot` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '时段',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '数量',
  `service_ids` json NULL COMMENT '服务ID列表',
  `customer_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '联系人',
  `customer_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '联系人电话',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '总金额',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'RESERVED' COMMENT '状态',
  `pay_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'BALANCE' COMMENT '支付方式',
  `pay_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'UNPAID' COMMENT '支付状态',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_hanfu`(`hanfu_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  CONSTRAINT `fk_order_hanfu` FOREIGN KEY (`hanfu_id`) REFERENCES `hanfu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '预约订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of reservation_order
-- ----------------------------
INSERT INTO `reservation_order` VALUES (1, 'LYHF202604080001', 1, 1, '2026-04-10', '09:00-12:00', 1, '[1, 3]', '洛洛', '13800000001', 566.00, 'COMPLETED', 'BALANCE', 'PAID', '到店前联系', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `reservation_order` VALUES (2, 'LYHF202604080002', 2, 6, '2026-04-11', '14:00-18:00', 1, '[2]', '阳阳', '13800000002', 356.00, 'COMPLETED', 'OFFLINE', 'PAID', '已取衣', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `reservation_order` VALUES (3, 'LYHF202604080003', 3, 8, '2026-04-12', '10:00-12:00', 1, '[]', '汉小服', '13800000003', 182.00, 'CANCELLED', 'BALANCE', 'REFUNDED', '行程变更', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `reservation_order` VALUES (4, '2042165526628814848', 4, 3, '2026-03-31', '13:00-17:00', 1, '[202, 303]', 'user', '17737061239', 148.00, 'COMPLETED', 'BALANCE', 'UNPAID', 'xl', '2026-04-09 16:59:38', '2026-04-09 16:59:38', 0);

-- ----------------------------
-- Table structure for service_package
-- ----------------------------
DROP TABLE IF EXISTS `service_package`;
CREATE TABLE `service_package`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类型（妆造/发型/摄影/道具）',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '套餐名',
  `subtitle` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '副标题',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1上架，0下架',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '附加服务套餐表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of service_package
-- ----------------------------
INSERT INTO `service_package` VALUES (1, '妆造', '基础妆造套餐', '约 50 分钟', 99.00, '基础底妆+发饰', '/uploads/hanfu/d3cbaaba-4517-4072-8acb-0ce14e1e2bce.png', 1, '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `service_package` VALUES (2, '发型', '古风盘发造型', '赠送发饰租赁', 128.00, '古风盘发+发饰', '/uploads/hanfu/22579108-8e9e-48fc-95a3-12789e705c80.png', 1, '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `service_package` VALUES (3, '摄影', '外景跟拍 1 小时', '含 10 张精修', 299.00, '10 张精修', '/uploads/hanfu/00bb58f0-9acd-4c38-b83b-f6210b00a98c.png', 1, '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `service_package` VALUES (4, '道具', '扇伞道具组合', '拍摄拍摄点缀', 39.00, '折扇+油纸伞', '/uploads/hanfu/3ba79fab-6e86-475e-bdd3-3a53c869de5e.png', 1, '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);

-- ----------------------------
-- Table structure for store
-- ----------------------------
DROP TABLE IF EXISTS `store`;
CREATE TABLE `store`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '门店名称',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '地址',
  `business_time` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '营业时间',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '电话',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '门店信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of store
-- ----------------------------
INSERT INTO `store` VALUES (1, '洛阳老城汉服馆', '洛阳市老城区中州中路古城街88号', '09:00-21:00', '0379-88888888', '主打唐宋制汉服', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `store` VALUES (2, '龙门石窟汉服体验店', '洛阳市洛龙区龙门大道66号', '09:00-20:00', '0379-66666666', '适合景区跟拍套餐', '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录密码（BCrypt）',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `balance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '余额',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1启用，0禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'test_user_1', '洛洛', '$2a$12$H2FiOcLqu/yLdRrDikNcx.YVaJXPx7qRMXHvUtN/1nOeaAaDQyBXu', '13800000001', 'u1@example.com', '/images/3.png', 520.00, 1, '2026-04-08 21:45:52', '2026-04-09 15:48:44', 0);
INSERT INTO `user` VALUES (2, 'test_user_2', '阳阳', '$2a$12$H2FiOcLqu/yLdRrDikNcx.YVaJXPx7qRMXHvUtN/1nOeaAaDQyBXu', '13800000002', 'u2@example.com', '/images/4.png', 300.00, 1, '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `user` VALUES (3, 'test_user_3', '汉小服', '$2a$12$H2FiOcLqu/yLdRrDikNcx.YVaJXPx7qRMXHvUtN/1nOeaAaDQyBXu', '13800000003', 'u3@example.com', '/images/5.png', 88.00, 1, '2026-04-08 21:45:52', '2026-04-08 21:45:52', 0);
INSERT INTO `user` VALUES (4, '17737061239', 'user', '$2a$10$Tu4F.tE.kDwpMFcUCOmvZe8dH9LTVtK2d1vVwerjsnQ4YFKFer3Ne', '17737061239', '', NULL, 0.00, 1, '2026-04-09 16:58:41', '2026-04-09 16:58:41', 0);

SET FOREIGN_KEY_CHECKS = 1;
