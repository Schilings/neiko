
-- ----------------------------
-- Records of nk_authorization_consent
-- ----------------------------
INSERT INTO `nk_authorization_consent`
VALUES (1, '111111111111111111', 'principalName:client1', 'authorities1,authorities2,authorities3', '2022-09-17 19:41:16', NULL);
INSERT INTO `nk_authorization_consent`
VALUES (2, '222222222222222222', 'principalName:client2', 'authorities1,authorities2,authorities3', '2022-09-17 19:41:16', NULL);
INSERT INTO `nk_authorization_consent`
VALUES (3, '3333333333333333', 'principalName:test', 'authorities1,authorities2,authorities3',  '2022-09-17 19:41:16', NULL);
INSERT INTO `nk_authorization_consent`
VALUES (4, '44444444444444444', 'principalName:messaging-client', 'authorities1,authorities2,authorities3', '2022-09-17 19:41:16', NULL);


-- ----------------------------
-- Records of nk_oauth2_registered_client
-- ----------------------------
INSERT INTO `nk_oauth2_registered_client` 
VALUES (111111111111111111, 'client1', '{noop}secret1',NULL,NULL,'CLIENT_1',
        'client_secret_post,client_secret_basic',
        'authorization_code,implicit,refresh_token,client_credentials,password',
        'http://127.0.0.1:8000/login/oauth2/code/sas-oidc-login,http://127.0.0.1:8000/login/oauth2/code/sas-authorization-code-login,http://127.0.0.1:8000/authorized',
        'openid,email,phone,address,message.read,message.write',
        0, NULL, NULL, '2022-09-17 19:41:16', NULL);
INSERT INTO `nk_oauth2_registered_client`

VALUES (222222222222222222, 'client2', '{noop}secret2',NULL,NULL,'CLIENT_2',
        'client_secret_post,client_secret_basic',
        'authorization_code,implicit,refresh_token,client_credentials,password',
        'http://127.0.0.1:8000/login/oauth2/code/sas-oidc-login,http://127.0.0.1:8000/login/oauth2/code/sas-authorization-code-login,http://127.0.0.1:8000/authorized',
        'openid,email,phone,address,message.read,message.write',
         0, NULL, NULL, '2022-09-17 19:41:16', NULL);

INSERT INTO `nk_oauth2_registered_client`
VALUES (3333333333333333, 'test', '{noop}test',NULL,NULL,'TEST',
        'client_secret_post,client_secret_basic',
        'federated_identity,authorization_code,implicit,refresh_token,client_credentials,password',
        'http://127.0.0.1:9000/oauth2Login,http://127.0.0.1:9000/authorizeLogin,http://127.0.0.1:9000/webjars/oauth/oauth2.html,http://127.0.0.1:9000/swagger-ui/oauth2-redirect.html,http://127.0.0.1:8000/login/oauth2/code/sas-oidc-login,http://127.0.0.1:8000/login/oauth2/code/sas-authorization-code-login,http://127.0.0.1:8000/authorized',
        'openid,email,phone,address,message.read,message.write,skip_password_decode,skip_captcha,authority_info_claim,user_info_claim,system_user_info,customer_user_info',
         0, NULL, NULL, '2022-09-17 19:41:16', NULL);

INSERT INTO `nk_oauth2_registered_client`
VALUES (333333334444444, 'demo', '{noop}demo',NULL,NULL,'DEMO',
        'client_secret_post,client_secret_basic',
        'federated_identity,authorization_code,implicit,refresh_token,client_credentials,password',
        'http://127.0.0.1:9000/oauth2Login,http://127.0.0.1:9000/authorizeLogin,http://127.0.0.1:9000/webjars/oauth/oauth2.html,http://127.0.0.1:9000/swagger-ui/oauth2-redirect.html,http://127.0.0.1:8000/login/oauth2/code/sas-oidc-login,http://127.0.0.1:8000/login/oauth2/code/sas-authorization-code-login,http://127.0.0.1:8000/authorized',
        'openid,email,phone,address,message.read,message.write,skip_password_decode,skip_captcha,authority_info_claim,user_info_claim,system_user_info,customer_user_info',
        0, NULL, NULL, '2022-09-17 19:41:16', NULL);

INSERT INTO `nk_oauth2_registered_client`
VALUES (44444444444444444, 'messaging-client', '{noop}messaging-secret',NULL,NULL,'MESSAGE_CLIENT',
        'client_secret_post,client_secret_basic',
        'authorization_code,implicit,refresh_token,client_credentials,password',
        'http://127.0.0.1:8000/login/oauth2/code/sas-oidc-login,http://127.0.0.1:8000/login/oauth2/code/sas-authorization-code-login,http://127.0.0.1:8000/authorized',
        'openid,profile,email,phone,address,message.read,message.write',
        0, NULL, NULL, '2022-09-17 19:41:16', NULL);

INSERT INTO `nk_oauth2_registered_client`
VALUES (555555555555555, 'messaging-client1', '{noop}messaging-secret1',NULL,NULL,'MESSAGE_CLIENT1',
        'client_secret_post,client_secret_basic',
        'authorization_code,implicit,refresh_token,client_credentials,password',
        'http://127.0.0.1:8000/login/oauth2/code/sas-oidc-login,http://127.0.0.1:8000/login/oauth2/code/sas-authorization-code-login,http://127.0.0.1:8000/authorized',
        'openid,profile,email,phone,address,message.read,message.write',
        0, NULL, NULL, '2022-09-17 19:41:16', NULL);
INSERT INTO `nk_oauth2_registered_client`
VALUES (666666666666666, 'messaging-client2', '{noop}messaging-secret2',NULL,NULL,'MESSAGE_CLIENT2',
        'client_secret_post,client_secret_basic',
        'authorization_code,implicit,refresh_token,client_credentials,password',
        'http://127.0.0.1:8000/login/oauth2/code/sas-oidc-login,http://127.0.0.1:8000/login/oauth2/code/sas-authorization-code-login,http://127.0.0.1:8000/authorized',
        'openid,profile,email,phone,address,message.read,message.write',
        0, NULL, NULL, '2022-09-17 19:41:16', NULL);


-- ----------------------------
-- Records of nk_oauth2_token_settings
-- ----------------------------
INSERT INTO `nk_oauth2_token_settings` VALUES ('client1',300,3600,300,'self-contained',0,'RS256');
INSERT INTO `nk_oauth2_token_settings` VALUES ('client2',300,3600,300,'self-contained',0,'RS256');
INSERT INTO `nk_oauth2_token_settings` VALUES ('test',1800,3600,300,'self-contained',0,'RS256');
INSERT INTO `nk_oauth2_token_settings` VALUES ('demo',1800,3600,300,'reference',0,'RS256');
INSERT INTO `nk_oauth2_token_settings` VALUES ('messaging-client',300,3600,300,'self-contained',0,'RS256');
INSERT INTO `nk_oauth2_token_settings` VALUES ('messaging-client1',300,3600,300,'self-contained',0,'RS256');
INSERT INTO `nk_oauth2_token_settings` VALUES ('messaging-client2',300,3600,300,'self-contained',0,'RS256');

-- ----------------------------
-- Records of nk_oauth2_client_settings
-- ----------------------------
INSERT INTO `nk_oauth2_client_settings` VALUES ('client1',0,1,NULL,NULL);
INSERT INTO `nk_oauth2_client_settings` VALUES ('client2',0,1,NULL,NULL);
INSERT INTO `nk_oauth2_client_settings` VALUES ('test',0,1,NULL,NULL);
INSERT INTO `nk_oauth2_client_settings` VALUES ('demo',0,1,NULL,NULL);
INSERT INTO `nk_oauth2_client_settings` VALUES ('messaging-client',0,1,NULL,NULL);
INSERT INTO `nk_oauth2_client_settings` VALUES ('messaging-client1',0,1,NULL,NULL);
INSERT INTO `nk_oauth2_client_settings` VALUES ('messaging-client2',0,1,NULL,NULL);
