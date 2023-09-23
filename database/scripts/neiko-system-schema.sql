
-- ----------------------------
-- Table structure for nk_sys_config
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_sys_config`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `name` varchar(255) NULL DEFAULT NULL COMMENT '配置名称',
  `conf_key` varchar(255) NULL DEFAULT NULL COMMENT '配置在缓存中的key名',
  `conf_value` varchar(255) NULL DEFAULT NULL COMMENT '配置值',
  `category` varchar(255) NULL DEFAULT NULL COMMENT '分类',
  `remarks` varchar(255) NULL DEFAULT NULL COMMENT '备注',
  `deleted` bigint NULL DEFAULT NULL,
  `create_by` bigint NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for nk_sys_dict
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_sys_dict`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `code` varchar(255) NULL DEFAULT NULL COMMENT '标识',
  `title` varchar(255) NULL DEFAULT NULL COMMENT '名称',
  `value_type` int NULL DEFAULT NULL COMMENT '数据类型（1:Number 2:String 3:Boolean）',
  `hash_code` varchar(255) NULL DEFAULT NULL COMMENT 'Hash值',
  `remarks` varchar(255) NULL DEFAULT NULL COMMENT '备注',
  `deleted` bigint NULL DEFAULT NULL,
  `create_by` bigint NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);


-- ----------------------------
-- Table structure for nk_sys_dict_item
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_sys_dict_item`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `dict_code` varchar(255) NULL DEFAULT NULL COMMENT '字典标识',
  `value` varchar(255) NULL DEFAULT NULL COMMENT '数据值',
  `name` varchar(255) NULL DEFAULT NULL COMMENT '文本值',
  `status` int NULL DEFAULT NULL COMMENT '状态(1：启用 0：禁用)',
  `attributes` longtext NULL COMMENT '附加属性值',
  `sort` int NULL DEFAULT NULL COMMENT '排序（升序',
  `remarks` varchar(255) NULL DEFAULT NULL COMMENT '备注',
  `deleted` bigint NULL DEFAULT NULL,
  `create_by` bigint NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);


-- ----------------------------
-- Table structure for nk_sys_menu
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_sys_menu`  (
  `id` bigint NOT NULL COMMENT '菜单ID',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级ID',
  `title` varchar(255) NULL DEFAULT NULL COMMENT '菜单名称',
  `icon` varchar(255) NULL DEFAULT NULL COMMENT '菜单图标',
  `permission` varchar(255) NULL DEFAULT NULL COMMENT '授权标识',
  `path` varchar(255) NULL DEFAULT NULL COMMENT '路由地址',
  `target_type` int NULL DEFAULT NULL COMMENT '打开方式 (1组件 2内链 3外链)',
  `uri` varchar(255) NULL DEFAULT NULL COMMENT '定位标识 (打开方式为组件时其值为组件相对路径，其他为URL地址)',
  `sort` int NULL DEFAULT NULL COMMENT '显示排序',
  `keep_alive` int NULL DEFAULT NULL COMMENT '组件缓存：0-开启，1-关闭',
  `hidden` int NULL DEFAULT NULL COMMENT '隐藏菜单:  0-否，1-是',
  `type` int NULL DEFAULT NULL COMMENT '菜单类型 （0目录，1菜单，2按钮）',
  `remarks` varchar(255) NULL DEFAULT NULL COMMENT '备注信息',
  `deleted` bigint NULL DEFAULT NULL,
  `create_by` bigint NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);


-- ----------------------------
-- Table structure for nk_sys_organization
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_sys_organization`  (
  `id` bigint NOT NULL COMMENT 'ID',
  `name` varchar(255) NULL DEFAULT NULL COMMENT '组织名称',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级ID',
  `hierarchy` varchar(255) NULL DEFAULT NULL COMMENT '层级信息，从根节点到当前节点的最短路径，使用-分割节点ID',
  `depth` int NULL DEFAULT NULL COMMENT '当前节点深度',
  `sort` int NULL DEFAULT NULL COMMENT '排序字段，由小到大',
  `remarks` varchar(255) NULL DEFAULT NULL COMMENT '备注',
  `deleted` bigint NULL DEFAULT NULL,
  `create_by` bigint NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for nk_sys_role
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_sys_role`  (
  `id` bigint NOT NULL COMMENT '角色ID编号',
  `name` varchar(255) NULL DEFAULT NULL COMMENT '角色名称',
  `code` varchar(255) NULL DEFAULT NULL COMMENT '角色标识',
  `type` int NULL DEFAULT NULL COMMENT '角色类型，1：系统角色 2：业务角色',
  `scope_type` int NULL DEFAULT NULL COMMENT '数据权限',
  `scope_resources` varchar(255) NULL DEFAULT NULL COMMENT '数据范围资源，当数据范围类型为自定义时使用',
  `remarks` varchar(255) NULL DEFAULT NULL COMMENT '角色备注',
  `deleted` bigint NULL DEFAULT NULL,
  `create_by` bigint NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);


-- ----------------------------
-- Table structure for nk_sys_role_menu
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_sys_role_menu`  (
  `id` bigint NOT NULL COMMENT 'ID',
  `role_code` varchar(255) NULL DEFAULT NULL COMMENT '角色 Code',
  `menu_id` bigint NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
);


-- ----------------------------
-- Table structure for nk_sys_user
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_sys_user`  (
  `user_id` bigint NOT NULL COMMENT '主键id',
  `username` varchar(255) NULL DEFAULT NULL COMMENT '登录账号',
  `nickname` varchar(255) NULL DEFAULT NULL COMMENT '昵称',
  `password` varchar(255) NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(255) NULL DEFAULT NULL COMMENT 'md5密码盐',
  `avatar` varchar(255) NULL DEFAULT NULL COMMENT '头像',
  `sex` int NULL DEFAULT NULL COMMENT '性别(0-默认未知,1-男,2-女)',
  `email` varchar(255) NULL DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(255) NULL DEFAULT NULL COMMENT '电话',
  `status` int NULL DEFAULT NULL COMMENT '状态(1-正常, 0-冻结)',
  `type` int NULL DEFAULT NULL COMMENT '1:系统用户， 2：客户用户',
  `organization_id` bigint NULL DEFAULT NULL COMMENT '组织机构ID',
  `deleted` bigint NULL DEFAULT NULL,
  `create_by` bigint NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`)
);


-- ----------------------------
-- Table structure for nk_sys_user_role
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_sys_user_role`  (
  `id` bigint NOT NULL,
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `role_code` varchar(255) NULL DEFAULT NULL COMMENT '角色Code',
  PRIMARY KEY (`id`)
);

