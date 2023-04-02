-- ----------------------------
-- Records of nk_sys_config
-- ----------------------------
INSERT INTO `nk_sys_config` VALUES (1, '网站弹窗开关', 'site_popup', '1233', 'group', '宣传网站是否弹出框的控制开关。 1：开启 0：关闭', 0, NULL, NULL, '2022-09-17 19:41:21', NULL);

-- ----------------------------
-- Records of nk_sys_dict
-- ----------------------------
INSERT INTO `nk_sys_dict` VALUES (2, 'log_status', '日志状态', 1, 'b3b0d5919e4a46ffa6dd17d9d7799c58', '正常、异常', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict` VALUES (3, 'gender', '性别', 1, 'ae18a6a3e6744f10bd35dc71867edf8d', '用户性别', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict` VALUES (4, 'grant_types', '授权类型', 1, 'e5316daadb490e9ca7e1ac5c5607a4', 'OAuth授权类型', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict` VALUES (5, 'operation_type', '操作类型', 1, '6b375bba88f649caa38e95d8e5c5c5c9', '操作日志的操作类型', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict` VALUES (6, 'role_type', '角色类型', 1, '90875dc38a154b9d810e8556f8972d9b', '角色类型，系统保留角色不允许删除', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict` VALUES (7, 'dict_value_type', '字典数据类型', 1, '886c8965bdaa4c1e91ffcd5fb20ea84f', 'Number、String、Boolean', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict` VALUES (8, 'login_event_type', '登陆事件类型', 1, '56f191fa64ad42b5948e9dcb331cded9', '1：登陆  2：登出', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict` VALUES (9, 'yes_or_no', '是否', 1, 'aa22893ca4d243cb8eb198e0f7d66824', NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict` VALUES (13, 'user_type', '用户类型', 1, 'a2730f33f24045578ebe7786282e1955', '用户类型', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict` VALUES (14, 'recipient_filter_type', '消息接收人筛选方式', 1, '54f95affca9e4c53aa679bca2855351f', '接收人筛选方式', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict` VALUES (15, 'notify_channel', '通知渠道', 1, 'b1d33b2d410b4214920f67c01af80f2f', '通知渠道', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict` VALUES (16, 'menu_type', '菜单类型', 1, '88b77280a299482a8e58fbc5fcc065a3', '系统菜单的类型', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict` VALUES (17, 'user_status', '用户状态', 1, '9527', NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);

-- ----------------------------
-- Records of nk_sys_dict_item
-- ----------------------------
INSERT INTO `nk_sys_dict_item` VALUES (3, 'log_status', '1', '正常', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"Normal \",\"zh-CN\":\"正常\"},\"textColor\":\"#34890A\"}', 0, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (4, 'log_status', '0', '异常', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"Error\",\"zh-CN\":\"异常\"},\"textColor\":\"#FF0000\"}', 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (5, 'gender', '1', '男', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"Male\",\"zh-CN\":\"男\"},\"textColor\":\"\"}', 0, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (6, 'gender', '2', '女', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"Female\",\"zh-CN\":\"女\"},\"textColor\":\"\"}', 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (7, 'gender', '3', '未知', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"Unknown\",\"zh-CN\":\"未知\"},\"textColor\":\"\"}', 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (8, 'grant_types', 'password', '密码模式', 1, '{}', 0, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (9, 'grant_types', 'authorization_code', '授权码模式', 1, '{}', 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (10, 'grant_types', 'client_credentials', '客户端模式', 1, '{}', 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (11, 'grant_types', 'refresh_token', '刷新模式', 1, '{}', 3, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (12, 'grant_types', 'implicit', '简化模式', 1, '{}', 4, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (13, 'login_event_type', '1', '登陆', 1, '{\"tagColor\":\"cyan\",\"languages\":{\"en-US\":\"Login\",\"zh-CN\":\"登陆\"},\"textColor\":\"\"}', 0, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (14, 'login_event_type', '2', '登出', 1, '{\"tagColor\":\"pink\",\"languages\":{\"en-US\":\"Logout\",\"zh-CN\":\"登出\"},\"textColor\":\"\"}', 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (15, 'operation_type', '3', '查看', 1, '{\"tagColor\":\"purple\",\"languages\":{\"en-US\":\"Read\",\"zh-CN\":\"查看\"},\"textColor\":\"\"}', 3, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (16, 'operation_type', '4', '新建', 1, '{\"tagColor\":\"cyan\",\"languages\":{\"en-US\":\"Create\",\"zh-CN\":\"新建\"},\"textColor\":\"\"}', 4, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (17, 'operation_type', '5', '修改', 1, '{\"tagColor\":\"orange\",\"languages\":{\"en-US\":\"Update\",\"zh-CN\":\"修改\"},\"textColor\":\"\"}', 5, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (18, 'operation_type', '6', '删除', 1, '{\"tagColor\":\"pink\",\"languages\":{\"en-US\":\"Delete\",\"zh-CN\":\"删除\"},\"textColor\":\"\"}', 6, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (19, 'role_type', '1', '系统', 1, '{\"tagColor\":\"orange\",\"languages\":{\"en-US\":\"System\",\"zh-CN\":\"系统\"},\"textColor\":\"\"}', 1, '系统角色不能删除', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (20, 'role_type', '2', '自定义', 1, '{\"tagColor\":\"green\",\"languages\":{\"en-US\":\"Custom\",\"zh-CN\":\"自定义\"},\"textColor\":\"\"}', 2, '自定义角色可以删除', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (21, 'dict_type', '1', 'Number', 1, '{}', 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (22, 'dict_type', '2', 'String', 1, '{}', 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (23, 'dict_type', '3', 'Boolean', 1, '{}', 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (24, 'dict_value_type', '1', 'Number', 1, '{}', 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (25, 'dict_value_type', '2', 'String', 1, '{}', 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (26, 'dict_value_type', '3', 'Boolean', 1, '{}', 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (27, 'yes_or_no', '1', '是', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"Yes\",\"zh-CN\":\"是\"},\"textColor\":\"\"}', 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (28, 'yes_or_no', '0', '否', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"No\",\"zh-CN\":\"否\"},\"textColor\":\"\"}', 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (49, 'user_type', '1', '系统用户', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"System User\",\"zh-CN\":\"系统用户\"},\"textColor\":\"\"}', 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (50, 'recipient_filter_type', '1', '全部', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"All\",\"zh-CN\":\"全部\"},\"textColor\":\"\"}', 1, '不筛选，对全部用户发送', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (51, 'recipient_filter_type', '2', '指定角色', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"Specify the role\",\"zh-CN\":\"指定角色\"},\"textColor\":\"\"}', 2, '筛选拥有指定角色的用户', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (52, 'recipient_filter_type', '3', '指定组织', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"Specify the organization\",\"zh-CN\":\"指定组织\"},\"textColor\":\"\"}', 3, '筛选指定组织的用户', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (53, 'recipient_filter_type', '4', '指定类型', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"Specify the type\",\"zh-CN\":\"指定类型\"},\"textColor\":\"\"}', 4, '筛选指定用户类型的用户', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (54, 'recipient_filter_type', '5', '指定用户', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"Specify the user\",\"zh-CN\":\"指定用户\"},\"textColor\":\"\"}', 5, '指定用户发送', 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (55, 'notify_channel', '1', '站内', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"Station\",\"zh-CN\":\"站内\"},\"textColor\":\"\"}', 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (56, 'notify_channel', '2', '短信', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"SMS\",\"zh-CN\":\"短信\"},\"textColor\":\"\"}', 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (57, 'notify_channel', '3', '邮箱', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"Email\",\"zh-CN\":\"邮箱\"},\"textColor\":\"\"}', 3, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (59, 'menu_type', '0', '目录', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"Catalog\",\"zh-CN\":\"目录\"},\"textColor\":\"\"}', 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (60, 'menu_type', '1', '菜单', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"Menu\",\"zh-CN\":\"菜单\"},\"textColor\":\"\"}', 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (61, 'menu_type', '2', '按钮', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"Button\",\"zh-CN\":\"按钮\"},\"textColor\":\"\"}', 3, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (62, 'operation_type', '0', '其他', 1, '{\"tagColor\":\"\",\"languages\":{\"en-US\":\"Other\",\"zh-CN\":\"其他\"},\"textColor\":\"\"}', 0, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (63, 'operation_type', '1', '导入', 1, '{\"tagColor\":\"green\",\"languages\":{\"en-US\":\"Import\",\"zh-CN\":\"导入\"},\"textColor\":\"\"}', 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (64, 'operation_type', '2', '导出', 1, '{\"tagColor\":\"blue\",\"languages\":{\"en-US\":\"Export\",\"zh-CN\":\"导出\"},\"textColor\":\"\"}', 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (65, 'user_status', '0', '锁定', 1, '{\"tagColor\":\"#d9d9d9\",\"languages\":{\"en-US\":\"Locked\",\"zh-CN\":\"锁定\"},\"textColor\":\"#d9d9d9\",\"badgeStatus\":\"default\"}', 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);
INSERT INTO `nk_sys_dict_item` VALUES (66, 'user_status', '1', '正常', 1, '{\"tagColor\":\"blue\",\"languages\":{\"en-US\":\"Normal\",\"zh-CN\":\"正常\"},\"textColor\":\"#5b8ff9\",\"badgeStatus\":\"processing\"}', 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:22', NULL);

-- ----------------------------
-- Records of nk_sys_menu
-- ----------------------------
INSERT INTO `nk_sys_menu` VALUES (10028, 0, '个人页', 'user', NULL, 'account', 1, 'account/settings/Index', 0, 0, 1, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (10030, 10028, '个人设置', NULL, NULL, 'settings', 1, NULL, 1, 0, 1, 0, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (10031, 10030, '基本设置', NULL, NULL, 'base', 1, 'account/settings/BaseSetting', 1, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (10032, 10030, '安全设置', NULL, NULL, 'security', 1, 'account/settings/Security', 2, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (10034, 10030, '账户绑定', NULL, NULL, 'binding', 1, 'account/settings/Binding', 4, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (10035, 10030, '新消息通知', NULL, NULL, 'notification', 1, 'account/settings/Notification', 5, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);

INSERT INTO `nk_sys_menu` VALUES (100000, 0, '系统管理', 'setting', NULL, 'system', 1, NULL, 1, 0, 0, 0, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100100, 100000, '系统用户', NULL, NULL, 'user', 1, 'system/user/SysUserPage', 1, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100101, 100100, '系统用户查询', NULL, 'system:user:read', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100102, 100100, '系统用户新增', NULL, 'system:user:add', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100103, 100100, '系统用户修改', NULL, 'system:user:edit', NULL, 1, NULL, 2, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100104, 100100, '系统用户删除', NULL, 'system:user:del', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100105, 100100, '系统用户授权', NULL, 'system:user:grant', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100106, 100100, '系统用户改密', NULL, 'system:user:pass', NULL, 1, NULL, 4, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100200, 100000, '角色管理', NULL, NULL, 'role', 1, 'system/role/SysRolePage', 2, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100201, 100200, '系统角色查询', NULL, 'system:role:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100202, 100200, '系统角色新增', NULL, 'system:role:add', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100203, 100200, '系统角色修改', NULL, 'system:role:edit', NULL, 1, NULL, 2, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100204, 100200, '系统角色删除', NULL, 'system:role:del', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100205, 100200, '系统角色授权', NULL, 'system:role:grant', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100400, 100000, '配置信息', NULL, NULL, 'config', 1, 'system/config/SysConfigPage', 6, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100401, 100400, '配置查询', NULL, 'system:config:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100402, 100400, '配置新增', NULL, 'system:config:add', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100403, 100400, '配置修改', NULL, 'system:config:edit', NULL, 1, NULL, 2, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100404, 100400, '配置删除', NULL, 'system:config:del', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100500, 100000, '字典管理', NULL, NULL, 'dict', 1, 'system/dict/SysDictPage', 5, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100501, 100500, '字典查询', NULL, 'system:dict:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100502, 100500, '字典新增', NULL, 'system:dict:add', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100503, 100500, '字典修改', NULL, 'system:dict:edit', NULL, 1, NULL, 2, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100504, 100500, '字典删除', NULL, 'system:dict:del', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100700, 100000, '组织架构', NULL, NULL, 'organization', 1, 'system/organization/SysOrganizationPage', 4, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100701, 100700, '组织架构查询', NULL, 'system:organization:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100702, 100700, '组织架构新增', NULL, 'system:organization:add', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100703, 100700, '组织架构修改', NULL, 'system:organization:edit', NULL, 1, NULL, 2, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100704, 100700, '组织架构删除', NULL, 'system:organization:del', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100705, 100700, '组织机构校正', NULL, 'system:organization:revised', NULL, 1, NULL, 5, 0, 0, 2, '校正组织机构层级和深度', 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100800, 100000, '菜单权限', NULL, NULL, 'menu', 1, 'system/menu/SysMenuPage', 3, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100801, 100800, '菜单权限查询', NULL, 'system:menu:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100802, 100800, '菜单权限新增', NULL, 'system:menu:add', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100803, 100800, '菜单权限修改', NULL, 'system:menu:edit', NULL, 1, NULL, 2, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (100804, 100800, '菜单权限删除', NULL, 'system:menu:del', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);


INSERT INTO `nk_sys_menu` VALUES (200000, 0, '认证管理', 'setting', NULL, 'authorization', 1, NULL, 1, 0, 0, 0, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (200100, 200000, '客户端', NULL, NULL, 'client', 1, 'authorization/client/RegisteredClientPage', 1, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (200101, 200100, '客户端查询', NULL, 'authorization:registeredClient:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (200102, 200100, '客户端新增', NULL, 'authorization:registeredClient:add', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (200103, 200100, '客户端修改', NULL, 'authorization:registeredClient:edit', NULL, 1, NULL, 2, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (200104, 200100, '客户端删除', NULL, 'authorization:registeredClient:del', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (200200, 200000, '认证同意信息', NULL, NULL, 'consent', 1, 'authorization/consent/AuthorizationConsentPage', 1, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (200201, 200200, '认证同意信息查询', NULL, 'authorization:authorizationConsent:read', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (200300, 200000, '认证信息', NULL, NULL, 'authorization', 1, 'authorization/authorization/AuthorizationPage', 1, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (200301, 200300, '认证信息查询', NULL, 'authorization:authorization:read', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);

INSERT INTO `nk_sys_menu` VALUES (110000, 0, '日志管理', 'file-search', NULL, 'log', 1, NULL, 2, 0, 0, 0, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (110100, 110000, '操作日志', NULL, NULL, 'operation-log', 1, 'log/operation-log/OperationLogPage', 2, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (110101, 110100, '操作日志查询', NULL, 'log:operation-log:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (110200, 110000, '登陆日志', NULL, NULL, 'login-log', 1, 'log/login-log/LoginLogPage', 1, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (110201, 110200, '登陆日志查询', NULL, 'log:login-log:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (110300, 110000, '访问日志', NULL, NULL, 'access-log', 1, 'log/access-log/AccessLogPage', 3, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (110301, 110300, '访问日志查询', NULL, 'log:access-log:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);

INSERT INTO `nk_sys_menu` VALUES (120000, 0, '消息通知', 'message', NULL, 'notify', 1, NULL, 3, 0, 0, 0, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (120100, 120000, '公告信息', NULL, NULL, 'announcement', 1, 'notify/announcement/AnnouncementPage', 1, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (120101, 120100, '公告信息查询', NULL, 'notify:announcement:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (120102, 120100, '公告信息新增', NULL, 'notify:announcement:add', NULL, 1, NULL, 1, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (120103, 120100, '公告信息修改', NULL, 'notify:announcement:edit', NULL, 1, NULL, 2, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (120104, 120100, '公告信息删除', NULL, 'notify:announcement:del', NULL, 1, NULL, 3, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (120200, 120000, '用户公告', NULL, NULL, 'userannouncement', 1, 'notify/userannouncement/UserAnnouncementPage', 1, 0, 1, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (120201, 120200, '用户公告表查询', NULL, 'notify:userannouncement:read', NULL, 1, NULL, 0, 0, 0, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);

INSERT INTO `nk_sys_menu` VALUES (660000, 0, 'Neiko 官网', 'crown', NULL, 'neiko', 2, '', 0, 1, 0, 1, NULL, 0, 1, 1, '2022-01-19 21:35:22', '2022-01-19 21:36:56');
INSERT INTO `nk_sys_menu` VALUES (990000, 0, '开发平台', 'desktop', '', 'develop', 1, NULL, 99, 0, 0, 0, NULL, 0, NULL, NULL, NULL, '2021-12-03 11:57:31');
INSERT INTO `nk_sys_menu` VALUES (990100, 990000, '接口文档', 'file', NULL, 'swagger', 3, 'http://neiko-admin:8080/swagger-ui/index.html', 1, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (990200, 990000, '文档增强', 'file-text', NULL, 'doc', 3, 'http://neiko-admin:8080/doc.html', 2, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (990300, 990000, '调度中心', 'rocket', NULL, 'xxl-job', 3, 'http://neiko-job:8888/xxl-job-admin', 3, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (990400, 990000, '服务监控', 'alert', NULL, 'monitor', 3, 'http://neiko-monitor:9999', 4, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);
INSERT INTO `nk_sys_menu` VALUES (990500, 990000, '代码生成', 'printer', NULL, 'codegen', 3, 'http://localhost:7777', 5, 0, 0, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:17', NULL);


-- ----------------------------
-- Records of nk_sys_organization
-- ----------------------------
INSERT INTO `nk_sys_organization` VALUES (6, '高大上公司', 0, '0', 1, 1, '一个神秘的组织', 0, NULL, NULL, '2022-09-17 19:41:21', NULL);
INSERT INTO `nk_sys_organization` VALUES (7, '产品研发部', 0, '0', 1, 1, '一个🐂皮的部门', 0, NULL, NULL, '2022-09-17 19:41:21', NULL);
INSERT INTO `nk_sys_organization` VALUES (8, 'java开发一组', 7, '0-7', 2, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:21', NULL);
INSERT INTO `nk_sys_organization` VALUES (9, 'Java开发二组', 7, '0-7', 2, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:21', NULL);
INSERT INTO `nk_sys_organization` VALUES (10, '谷歌', 6, '0-6', 2, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:21', NULL);
INSERT INTO `nk_sys_organization` VALUES (11, '不会Ollie', 10, '0-6-10', 3, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:21', NULL);
INSERT INTO `nk_sys_organization` VALUES (12, 'treflip高手', 10, '0-6-10', 3, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:21', NULL);
INSERT INTO `nk_sys_organization` VALUES (13, 'impossible', 10, '0-6-10', 3, 2, NULL, 0, NULL, NULL, '2022-09-17 19:41:21', NULL);
INSERT INTO `nk_sys_organization` VALUES (14, '测试', 12, '0-6-10-12', 4, 1, NULL, 0, NULL, NULL, '2022-09-17 19:41:21', NULL);

-- ----------------------------
-- Records of nk_sys_role
-- ----------------------------
INSERT INTO `nk_sys_role` VALUES (1, '管理员', 'ROLE_ADMIN', 1, 0, NULL, '管理员', 0, NULL, NULL, '2022-09-17 19:41:16', NULL);
INSERT INTO `nk_sys_role` VALUES (2, '测试工程师', 'ROLE_TEST', 2, 1, NULL, '测试工程师', 0, NULL, NULL, '2022-09-17 19:41:16', NULL);
INSERT INTO `nk_sys_role` VALUES (14, '销售主管', 'ROLE_SALES_EXECUTIVE', 2, 1, NULL, '销售主管', 0, NULL, NULL, '2022-09-17 19:41:16', NULL);
INSERT INTO `nk_sys_role` VALUES (15, '销售专员', 'ROLE_SALESMAN', 2, 1, NULL, '销售专员', 0, NULL, NULL, '2022-09-17 19:41:16', NULL);

-- ----------------------------
-- Records of nk_sys_role_menu
-- ----------------------------
INSERT INTO `nk_sys_role_menu` VALUES (279, 'ROLE_SALES_EXECUTIVE', 10);
INSERT INTO `nk_sys_role_menu` VALUES (280, 'ROLE_SALES_EXECUTIVE', 10);
INSERT INTO `nk_sys_role_menu` VALUES (281, 'ROLE_SALES_EXECUTIVE', 10);
INSERT INTO `nk_sys_role_menu` VALUES (282, 'ROLE_SALES_EXECUTIVE', 10);
INSERT INTO `nk_sys_role_menu` VALUES (283, 'ROLE_SALES_EXECUTIVE', 10);
INSERT INTO `nk_sys_role_menu` VALUES (284, 'ROLE_SALES_EXECUTIVE', 10);
INSERT INTO `nk_sys_role_menu` VALUES (285, 'ROLE_SALES_EXECUTIVE', 78);
INSERT INTO `nk_sys_role_menu` VALUES (286, 'ROLE_SALES_EXECUTIVE', 79);
INSERT INTO `nk_sys_role_menu` VALUES (570, 'ROLE_ADMIN', 10031);
INSERT INTO `nk_sys_role_menu` VALUES (571, 'ROLE_ADMIN', 10030);
INSERT INTO `nk_sys_role_menu` VALUES (572, 'ROLE_ADMIN', 10028);
INSERT INTO `nk_sys_role_menu` VALUES (573, 'ROLE_ADMIN', 10032);
INSERT INTO `nk_sys_role_menu` VALUES (574, 'ROLE_ADMIN', 10034);
INSERT INTO `nk_sys_role_menu` VALUES (575, 'ROLE_ADMIN', 10035);


INSERT INTO `nk_sys_role_menu` VALUES (974, 'ROLE_ADMIN', 200000);
INSERT INTO `nk_sys_role_menu` VALUES (975, 'ROLE_ADMIN', 200100);
INSERT INTO `nk_sys_role_menu` VALUES (976, 'ROLE_ADMIN', 200101);
INSERT INTO `nk_sys_role_menu` VALUES (977, 'ROLE_ADMIN', 200102);
INSERT INTO `nk_sys_role_menu` VALUES (978, 'ROLE_ADMIN', 200103);
INSERT INTO `nk_sys_role_menu` VALUES (979, 'ROLE_ADMIN', 200104);
INSERT INTO `nk_sys_role_menu` VALUES (980, 'ROLE_ADMIN', 200200);
INSERT INTO `nk_sys_role_menu` VALUES (981, 'ROLE_ADMIN', 200201);
INSERT INTO `nk_sys_role_menu` VALUES (982, 'ROLE_ADMIN', 200300);
INSERT INTO `nk_sys_role_menu` VALUES (983, 'ROLE_ADMIN', 200301);



INSERT INTO `nk_sys_role_menu` VALUES (576, 'ROLE_ADMIN', 100101);
INSERT INTO `nk_sys_role_menu` VALUES (577, 'ROLE_ADMIN', 100100);
INSERT INTO `nk_sys_role_menu` VALUES (578, 'ROLE_ADMIN', 100000);
INSERT INTO `nk_sys_role_menu` VALUES (579, 'ROLE_ADMIN', 100102);
INSERT INTO `nk_sys_role_menu` VALUES (580, 'ROLE_ADMIN', 100103);
INSERT INTO `nk_sys_role_menu` VALUES (581, 'ROLE_ADMIN', 100104);
INSERT INTO `nk_sys_role_menu` VALUES (582, 'ROLE_ADMIN', 100105);
INSERT INTO `nk_sys_role_menu` VALUES (583, 'ROLE_ADMIN', 100106);
INSERT INTO `nk_sys_role_menu` VALUES (584, 'ROLE_ADMIN', 100201);
INSERT INTO `nk_sys_role_menu` VALUES (585, 'ROLE_ADMIN', 100200);
INSERT INTO `nk_sys_role_menu` VALUES (586, 'ROLE_ADMIN', 100202);
INSERT INTO `nk_sys_role_menu` VALUES (587, 'ROLE_ADMIN', 100203);
INSERT INTO `nk_sys_role_menu` VALUES (588, 'ROLE_ADMIN', 100204);
INSERT INTO `nk_sys_role_menu` VALUES (589, 'ROLE_ADMIN', 100205);
INSERT INTO `nk_sys_role_menu` VALUES (590, 'ROLE_ADMIN', 100401);
INSERT INTO `nk_sys_role_menu` VALUES (591, 'ROLE_ADMIN', 100400);
INSERT INTO `nk_sys_role_menu` VALUES (592, 'ROLE_ADMIN', 100402);
INSERT INTO `nk_sys_role_menu` VALUES (593, 'ROLE_ADMIN', 100403);
INSERT INTO `nk_sys_role_menu` VALUES (594, 'ROLE_ADMIN', 100404);
INSERT INTO `nk_sys_role_menu` VALUES (595, 'ROLE_ADMIN', 100501);
INSERT INTO `nk_sys_role_menu` VALUES (596, 'ROLE_ADMIN', 100500);
INSERT INTO `nk_sys_role_menu` VALUES (597, 'ROLE_ADMIN', 100502);
INSERT INTO `nk_sys_role_menu` VALUES (598, 'ROLE_ADMIN', 100503);
INSERT INTO `nk_sys_role_menu` VALUES (599, 'ROLE_ADMIN', 100504);
INSERT INTO `nk_sys_role_menu` VALUES (600, 'ROLE_ADMIN', 100701);
INSERT INTO `nk_sys_role_menu` VALUES (601, 'ROLE_ADMIN', 100700);
INSERT INTO `nk_sys_role_menu` VALUES (602, 'ROLE_ADMIN', 100702);
INSERT INTO `nk_sys_role_menu` VALUES (603, 'ROLE_ADMIN', 100703);
INSERT INTO `nk_sys_role_menu` VALUES (604, 'ROLE_ADMIN', 100704);
INSERT INTO `nk_sys_role_menu` VALUES (605, 'ROLE_ADMIN', 100705);
INSERT INTO `nk_sys_role_menu` VALUES (606, 'ROLE_ADMIN', 100801);
INSERT INTO `nk_sys_role_menu` VALUES (607, 'ROLE_ADMIN', 100800);
INSERT INTO `nk_sys_role_menu` VALUES (608, 'ROLE_ADMIN', 100802);
INSERT INTO `nk_sys_role_menu` VALUES (609, 'ROLE_ADMIN', 100803);
INSERT INTO `nk_sys_role_menu` VALUES (610, 'ROLE_ADMIN', 100804);
INSERT INTO `nk_sys_role_menu` VALUES (611, 'ROLE_ADMIN', 110101);
INSERT INTO `nk_sys_role_menu` VALUES (612, 'ROLE_ADMIN', 110100);
INSERT INTO `nk_sys_role_menu` VALUES (613, 'ROLE_ADMIN', 110000);
INSERT INTO `nk_sys_role_menu` VALUES (614, 'ROLE_ADMIN', 110201);
INSERT INTO `nk_sys_role_menu` VALUES (615, 'ROLE_ADMIN', 110200);
INSERT INTO `nk_sys_role_menu` VALUES (616, 'ROLE_ADMIN', 110301);
INSERT INTO `nk_sys_role_menu` VALUES (617, 'ROLE_ADMIN', 110300);
INSERT INTO `nk_sys_role_menu` VALUES (618, 'ROLE_ADMIN', 120101);
INSERT INTO `nk_sys_role_menu` VALUES (619, 'ROLE_ADMIN', 120100);
INSERT INTO `nk_sys_role_menu` VALUES (620, 'ROLE_ADMIN', 120000);
INSERT INTO `nk_sys_role_menu` VALUES (621, 'ROLE_ADMIN', 120102);
INSERT INTO `nk_sys_role_menu` VALUES (622, 'ROLE_ADMIN', 120103);
INSERT INTO `nk_sys_role_menu` VALUES (623, 'ROLE_ADMIN', 120104);
INSERT INTO `nk_sys_role_menu` VALUES (624, 'ROLE_ADMIN', 120201);
INSERT INTO `nk_sys_role_menu` VALUES (625, 'ROLE_ADMIN', 120200);
INSERT INTO `nk_sys_role_menu` VALUES (626, 'ROLE_ADMIN', 990100);
INSERT INTO `nk_sys_role_menu` VALUES (627, 'ROLE_ADMIN', 990000);
INSERT INTO `nk_sys_role_menu` VALUES (628, 'ROLE_ADMIN', 990200);
INSERT INTO `nk_sys_role_menu` VALUES (629, 'ROLE_ADMIN', 990300);
INSERT INTO `nk_sys_role_menu` VALUES (630, 'ROLE_ADMIN', 990400);
INSERT INTO `nk_sys_role_menu` VALUES (631, 'ROLE_ADMIN', 990500);
INSERT INTO `nk_sys_role_menu` VALUES (632, 'ROLE_ADMIN', 660000);
INSERT INTO `nk_sys_role_menu` VALUES (633, 'ROLE_TEST', 10031);
INSERT INTO `nk_sys_role_menu` VALUES (634, 'ROLE_TEST', 10030);
INSERT INTO `nk_sys_role_menu` VALUES (635, 'ROLE_TEST', 10028);
INSERT INTO `nk_sys_role_menu` VALUES (636, 'ROLE_TEST', 10032);
INSERT INTO `nk_sys_role_menu` VALUES (637, 'ROLE_TEST', 660000);
INSERT INTO `nk_sys_role_menu` VALUES (638, 'ROLE_TEST', 100000);
INSERT INTO `nk_sys_role_menu` VALUES (639, 'ROLE_TEST', 100100);
INSERT INTO `nk_sys_role_menu` VALUES (640, 'ROLE_TEST', 100101);
INSERT INTO `nk_sys_role_menu` VALUES (641, 'ROLE_TEST', 100102);
INSERT INTO `nk_sys_role_menu` VALUES (642, 'ROLE_TEST', 100103);
INSERT INTO `nk_sys_role_menu` VALUES (643, 'ROLE_TEST', 100104);
INSERT INTO `nk_sys_role_menu` VALUES (644, 'ROLE_TEST', 100105);
INSERT INTO `nk_sys_role_menu` VALUES (645, 'ROLE_TEST', 100106);
INSERT INTO `nk_sys_role_menu` VALUES (646, 'ROLE_TEST', 100200);
INSERT INTO `nk_sys_role_menu` VALUES (647, 'ROLE_TEST', 100201);
INSERT INTO `nk_sys_role_menu` VALUES (648, 'ROLE_TEST', 100202);
INSERT INTO `nk_sys_role_menu` VALUES (649, 'ROLE_TEST', 100203);
INSERT INTO `nk_sys_role_menu` VALUES (650, 'ROLE_TEST', 100204);
INSERT INTO `nk_sys_role_menu` VALUES (651, 'ROLE_TEST', 100205);
INSERT INTO `nk_sys_role_menu` VALUES (652, 'ROLE_TEST', 100400);
INSERT INTO `nk_sys_role_menu` VALUES (653, 'ROLE_TEST', 100401);
INSERT INTO `nk_sys_role_menu` VALUES (654, 'ROLE_TEST', 100402);
INSERT INTO `nk_sys_role_menu` VALUES (655, 'ROLE_TEST', 100403);
INSERT INTO `nk_sys_role_menu` VALUES (656, 'ROLE_TEST', 100404);
INSERT INTO `nk_sys_role_menu` VALUES (657, 'ROLE_TEST', 100500);
INSERT INTO `nk_sys_role_menu` VALUES (658, 'ROLE_TEST', 100501);
INSERT INTO `nk_sys_role_menu` VALUES (659, 'ROLE_TEST', 100502);
INSERT INTO `nk_sys_role_menu` VALUES (660, 'ROLE_TEST', 100503);
INSERT INTO `nk_sys_role_menu` VALUES (661, 'ROLE_TEST', 100504);
INSERT INTO `nk_sys_role_menu` VALUES (662, 'ROLE_TEST', 100700);
INSERT INTO `nk_sys_role_menu` VALUES (663, 'ROLE_TEST', 100701);
INSERT INTO `nk_sys_role_menu` VALUES (664, 'ROLE_TEST', 100702);
INSERT INTO `nk_sys_role_menu` VALUES (665, 'ROLE_TEST', 100703);
INSERT INTO `nk_sys_role_menu` VALUES (666, 'ROLE_TEST', 100704);
INSERT INTO `nk_sys_role_menu` VALUES (667, 'ROLE_TEST', 100705);
INSERT INTO `nk_sys_role_menu` VALUES (668, 'ROLE_TEST', 100800);
INSERT INTO `nk_sys_role_menu` VALUES (669, 'ROLE_TEST', 100801);
INSERT INTO `nk_sys_role_menu` VALUES (670, 'ROLE_TEST', 100802);
INSERT INTO `nk_sys_role_menu` VALUES (671, 'ROLE_TEST', 100803);
INSERT INTO `nk_sys_role_menu` VALUES (672, 'ROLE_TEST', 100804);
INSERT INTO `nk_sys_role_menu` VALUES (673, 'ROLE_TEST', 10034);
INSERT INTO `nk_sys_role_menu` VALUES (674, 'ROLE_TEST', 10035);
INSERT INTO `nk_sys_role_menu` VALUES (675, 'ROLE_TEST', 990000);
INSERT INTO `nk_sys_role_menu` VALUES (676, 'ROLE_TEST', 990100);
INSERT INTO `nk_sys_role_menu` VALUES (677, 'ROLE_TEST', 990200);
INSERT INTO `nk_sys_role_menu` VALUES (678, 'ROLE_TEST', 990300);
INSERT INTO `nk_sys_role_menu` VALUES (679, 'ROLE_TEST', 990400);
INSERT INTO `nk_sys_role_menu` VALUES (680, 'ROLE_TEST', 990500);

-- ----------------------------
-- Records of nk_sys_user
-- ----------------------------
INSERT INTO `nk_sys_user` VALUES (1, 'admin', '超管牛逼', '{bcrypt}$2a$10$r5OS6t8x7XxWK7vRWKM7qec.LpnpLI.lXKragxjhIseTnRVvFIsNa', NULL, 'sysuser/1/avatar/20200226/ab6bd5221afe4238ae4987f278758113.jpg', 1, 'chengbohua@foxmail.com', '15800000000', 1, 1, 6, 0, NULL, NULL, '2022-09-17 19:41:16', NULL);
INSERT INTO `nk_sys_user` VALUES (2, 'test', '测试用户213', '{bcrypt}$2a$10$r5OS6t8x7XxWK7vRWKM7qec.LpnpLI.lXKragxjhIseTnRVvFIsNa', NULL, 'sysuser/10/avatar/20201204/002875d468db41239ee02ad99ab14490.jpg', 2, 'magic.xiaohua@gmail.com', '12345678520', 1, 1, 6, 0, NULL, NULL, '2022-09-17 19:41:16', NULL);

-- ----------------------------
-- Records of nk_sys_user_role
-- ----------------------------
INSERT INTO `nk_sys_user_role` VALUES (1, 1, 'ROLE_ADMIN');
INSERT INTO `nk_sys_user_role` VALUES (4, 1, 'ROLE_TEST');
INSERT INTO `nk_sys_user_role` VALUES (6, 14, 'ROLE_SALES_EXECUTIVE');

