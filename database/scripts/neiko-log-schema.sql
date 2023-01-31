
-- ----------------------------
-- Table structure for nk_log_access_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_log_access_log`  (
  `id` bigint NOT NULL COMMENT '主键ID 编号',
  `trace_id` varchar(255) NULL DEFAULT NULL COMMENT '追踪ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `username` varchar(255) NULL DEFAULT NULL COMMENT '用户名',
  `ip` varchar(255) NULL DEFAULT NULL COMMENT '访问IP地址',
  `user_agent` varchar(255) NULL DEFAULT NULL COMMENT '用户代理',
  `uri` varchar(255) NULL DEFAULT NULL COMMENT '请求URI',
  `matching_pattern` varchar(255) NULL DEFAULT NULL COMMENT '请求映射地址',
  `method` varchar(255) NULL DEFAULT NULL COMMENT '操作方式',
  `req_params` text NULL COMMENT '请求参数',
  `req_body` text NULL COMMENT '请求body',
  `http_status` int NULL DEFAULT NULL COMMENT '响应状态码',
  `result` text NULL COMMENT '响应信息',
  `error_msg` text NULL COMMENT '错误消息',
  `time` bigint NULL DEFAULT NULL COMMENT '执行时长',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
);


-- ----------------------------
-- Table structure for nk_log_login_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_log_login_log`  (
  `id` bigint NOT NULL COMMENT 'ID 编号',
  `trace_id` varchar(255) NULL DEFAULT NULL COMMENT '追踪ID',
  `client_id` varchar(255) NULL DEFAULT NULL COMMENT '客户端ID',
  `username` varchar(255) NULL DEFAULT NULL COMMENT '用户名',
  `ip` varchar(255) NULL DEFAULT NULL COMMENT '登陆IP',
  `os` varchar(255) NULL DEFAULT NULL COMMENT '操作系统',
  `status` int NULL DEFAULT NULL COMMENT '状态',
  `msg` varchar(255) NULL DEFAULT NULL COMMENT '日志消息',
  `location` varchar(255) NULL DEFAULT NULL COMMENT '登陆地点',
  `event_type` int NULL DEFAULT NULL COMMENT '事件类型',
  `browser` varchar(255) NULL DEFAULT NULL COMMENT '浏览器',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '登录/登出时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for nk_log_operation_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_log_operation_log`  (
  `id` bigint NOT NULL COMMENT '主键ID 编号',
  `trace_id` char(255) NULL DEFAULT NULL COMMENT '追踪ID',
  `msg` varchar(255) NULL DEFAULT NULL COMMENT '日志消息',
  `ip` varchar(255) NULL DEFAULT NULL COMMENT '访问IP地址',
  `user_agent` varchar(255) NULL DEFAULT NULL COMMENT '用户代理',
  `uri` varchar(255) NULL DEFAULT NULL COMMENT '请求URI',
  `method` varchar(255) NULL DEFAULT NULL COMMENT '请求方法',
  `params` text NULL COMMENT '操作提交的数据',
  `status` int NULL DEFAULT NULL COMMENT '操作状态',
  `type` int NULL DEFAULT NULL COMMENT '操作类型',
  `time` bigint NULL DEFAULT NULL COMMENT '执行时长',
  `result` varchar(500) NULL DEFAULT NULL COMMENT '操作结果',
  `operator` varchar(255) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
);


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

