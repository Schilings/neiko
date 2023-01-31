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
    `refresh_token_value` text NULL DEFAULT NULL COMMENT 'RefreshToken刷新令牌',
    `refresh_token_issued_at` varchar(255) NULL DEFAULT NULL COMMENT 'RefreshToken刷新令牌生效时间',
    `refresh_token_expires_at` varchar(255) NULL DEFAULT NULL COMMENT 'RefreshToken刷新令牌失效时间',
    `refresh_token_metadata` text NULL DEFAULT NULL COMMENT 'RefreshToken刷新令牌元数据',
    `oidc_id_token_value` text NULL DEFAULT NULL COMMENT 'OidcIdToken令牌',
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



