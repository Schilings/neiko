
-- ----------------------------
-- Table structure for nk_notify_announcement
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_notify_announcement`  (
  `id` bigint NOT NULL COMMENT 'ID',
  `title` varchar(255) NULL DEFAULT NULL COMMENT '标题',
  `content` text NULL COMMENT '内容',
  `recipient_filter_type` int NULL DEFAULT NULL COMMENT '接收人筛选方式',
  `recipient_filter_condition` longtext NULL COMMENT '对应接收人筛选方式的条件信息。如角色标识，组织ID，用户类型，用户ID等',
  `receive_mode` longtext NULL COMMENT '接收方式',
  `status` int NULL DEFAULT NULL COMMENT '状态',
  `immortal` int NULL DEFAULT NULL COMMENT '永久有效的',
  `deadline` datetime(0) NULL DEFAULT NULL COMMENT '截止日期',
  `deleted` bigint NULL DEFAULT NULL,
  `create_by` bigint NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for nk_notify_user_announcement
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_notify_user_announcement`  (
  `id` bigint NOT NULL COMMENT 'ID',
  `announcement_id` bigint NULL DEFAULT NULL COMMENT '公告id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `state` int NULL DEFAULT NULL COMMENT '状态，已读(1)|未读(0)',
  `read_time` datetime(0) NULL DEFAULT NULL COMMENT '阅读时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '拉取时间',
  PRIMARY KEY (`id`)
);

