-- ----------------------------
-- Table structure for nk_authorization
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_authorization`  (
    `id` varchar(255) NOT NULL COMMENT 'ID',
    `registered_client_id` varchar(255) NULL DEFAULT NULL COMMENT '客户端ClentId',
    `principal_name` varchar(255) NULL DEFAULT NULL COMMENT '授权主体名称',
    `authorization_grant_type` varchar(255) NULL DEFAULT NULL COMMENT '授权方式',
    `attributes` text NULL DEFAULT NULL COMMENT '额外属性',
    `state` varchar(255) NULL DEFAULT NULL COMMENT 'state值',
    `authorization_code_value` varchar(500) NULL DEFAULT NULL COMMENT 'AuthorizationCode授权码',
    `authorization_code_issued_at` varchar(255) NULL DEFAULT NULL COMMENT 'AuthorizationCode授权码生效时间',
    `authorization_code_expires_at` varchar(255) NULL DEFAULT NULL COMMENT 'AuthorizationCode授权码失效时间',
    `authorization_code_metadata` text NULL DEFAULT NULL COMMENT 'AuthorizationCode授权码元数据',
    `access_token_value` text NULL DEFAULT NULL COMMENT 'AccessToken访问令牌',
    `access_token_issued_at` varchar(255) NULL DEFAULT NULL COMMENT 'AccessToken访问令牌生效时间',
    `access_token_expires_at` varchar(255) NULL DEFAULT NULL COMMENT 'AccessToken访问令牌失效时间',
    `access_token_metadata` text NULL DEFAULT NULL COMMENT 'AccessToken访问令牌元数据',
    `access_token_type` varchar(1000) NULL DEFAULT NULL COMMENT 'AccessToken访问令牌类型',
    `access_token_scopes` varchar(255) NULL DEFAULT NULL COMMENT 'AccessToken访问令牌作用域',
    `refresh_token_value` varchar(255) NULL DEFAULT NULL COMMENT 'RefreshToken刷新令牌',
    `refresh_token_issued_at` varchar(255) NULL DEFAULT NULL COMMENT 'RefreshToken刷新令牌生效时间',
    `refresh_token_expires_at` varchar(255) NULL DEFAULT NULL COMMENT 'RefreshToken刷新令牌失效时间',
    `refresh_token_metadata` text NULL DEFAULT NULL COMMENT 'RefreshToken刷新令牌元数据',
    `oidc_id_token_value` varchar(1000) NULL DEFAULT NULL COMMENT 'OidcIdToken令牌',
    `oidc_id_token_issued_at` varchar(255) NULL DEFAULT NULL COMMENT 'OidcIdToken令牌生效时间',
    `oidc_id_token_expires_at` varchar(255) NULL DEFAULT NULL COMMENT 'OidcIdToken令牌失效时间',
    `oidc_id_token_metadata` text NULL DEFAULT NULL COMMENT 'OidcIdToken令牌元数据',
    `oidc_id_token_claims` text NULL DEFAULT NULL COMMENT 'OidcIdToken令牌声明',
    `create_time` datetime(0) NULL DEFAULT NULL,
    `update_time` datetime(0) NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
    );

-- ----------------------------
-- Table structure for nk_authorization_consent
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_authorization_consent`  (
    `id` bigint NOT NULL COMMENT 'ID',
    `registered_client_id` varchar(255) NULL DEFAULT NULL COMMENT '对应的客户端ID',
    `principal_name` varchar(255) NULL DEFAULT NULL COMMENT '主体',
    `authorities` varchar(255) NULL DEFAULT NULL COMMENT '权限',
    `create_time` datetime(0) NULL DEFAULT NULL,
    `update_time` datetime(0) NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
    );


-- ----------------------------
-- Table structure for nk_oauth2_registered_client
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_oauth2_registered_client`  (
    `id` bigint NOT NULL COMMENT 'ID',
    `client_id` varchar(255) NULL DEFAULT NULL COMMENT '对应的客户端ID',
    `client_secret` varchar(255) NULL DEFAULT NULL COMMENT '客户端secret',
    `client_id_issued_at` varchar(255) NULL DEFAULT NULL COMMENT 'clientId生效时间',
    `client_secret_expires_at` varchar(255) NULL DEFAULT NULL COMMENT 'clientSecret失效时间',
    `client_name` varchar(255) NULL DEFAULT NULL COMMENT '客户端名称',
    `client_authentication_methods` varchar(255) NULL DEFAULT NULL COMMENT '客户端支持的认证方式',
    `authorization_grant_types` varchar(255) NULL DEFAULT NULL COMMENT '客户端支持的授权方式',
    `redirect_uris` varchar(1000) NULL DEFAULT NULL COMMENT '客户端配置的回调地址',
    `scopes` varchar(255) NULL DEFAULT NULL COMMENT '客户端支持的作用域',
    `deleted` bigint NULL DEFAULT NULL,
    `create_by` bigint NULL DEFAULT NULL,
    `update_by` bigint NULL DEFAULT NULL,
    `create_time` datetime(0) NULL DEFAULT NULL,
    `update_time` datetime(0) NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
    );

-- ----------------------------
-- Table structure for nk_oauth2_token_settings
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_oauth2_token_settings`  (
    `client_id` varchar(255) NOT NULL COMMENT '对应的客户端ID',
    `access_token_time_to_live` bigint NULL DEFAULT NULL COMMENT '访问令牌有效时长,单位秒',
    `refresh_token_time_to_live` bigint NULL DEFAULT NULL COMMENT '刷新令牌有效时长,单位秒',
    `authorization_code_time_to_live` bigint NULL DEFAULT NULL COMMENT '授权码有效时长,单位秒',
    `token_format` varchar(255) NULL DEFAULT NULL COMMENT '令牌格式',
    `reuse_refresh_tokens` int NULL DEFAULT NULL COMMENT '刷新令牌是否可以重复使用',
    `id_token_signature_algorithm` varchar(255) NULL DEFAULT NULL COMMENT 'ID Token签名算法',
    PRIMARY KEY (`client_id`)
    );

-- ----------------------------
-- Table structure for nk_oauth2_client_settings
-- ----------------------------
CREATE TABLE IF NOT EXISTS `nk_oauth2_client_settings`  (
    `client_id` varchar(255) NOT NULL COMMENT '对应的客户端ID',
    `require_proof_key` int NULL DEFAULT NULL COMMENT '是否需要ProofKey',
    `require_authorization_consent` int NULL DEFAULT NULL COMMENT '是否需要用户授权同意',
    `jwk_set_url` varchar(255) NULL DEFAULT NULL COMMENT 'jwkSetUrl',
    `signing_algorithm` varchar(255) NULL DEFAULT NULL COMMENT '签名算法',
    PRIMARY KEY (`client_id`)
    );

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

