/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.108.179资产测试
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : 192.168.108.179:3306
 Source Schema         : hx_asset

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 11/10/2022 15:01:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for asset
-- ----------------------------
DROP TABLE IF EXISTS `asset`;
CREATE TABLE `asset`  (
  `asset_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '资产id',
  `asset_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '固定资产名称',
  `asset_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台资产编号',
  `financial_asset_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '财务资产编号',
  `responsible_person_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '保管人工号（老工号）',
  `responsible_person_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '保管人姓名',
  `category` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产分类描述',
  `asset_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产状态',
  `factory_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出厂编号',
  `standard` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格型号',
  `measure` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位',
  `buyer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '采购人（工号）',
  `buy_date` date NULL DEFAULT NULL COMMENT '采购日期',
  `total_value` double NULL DEFAULT NULL COMMENT '资产总价值',
  `net_worth` double NULL DEFAULT NULL COMMENT '净值',
  `warranty` int(5) NULL DEFAULT NULL COMMENT '保修期（月）',
  `can_use_months` int(5) NULL DEFAULT NULL COMMENT '预计使用寿命（月）',
  `can_use_years` int(5) NULL DEFAULT NULL COMMENT '预计使用寿命（年）',
  `capitalization_date` date NULL DEFAULT NULL COMMENT '资本化日期/资产价值录入日期',
  `monetary_unit` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产原值币制',
  `company_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司代码',
  `company_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司名',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存放地点',
  `provider` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '供应商',
  `amount` int(5) NULL DEFAULT NULL COMMENT '数量',
  `brand` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品牌',
  `cost_center` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '成本中心（编号）',
  `cost_center_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '成本中心描述',
  `manage_dept` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理部门描述',
  `contract_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合同单号',
  `proposer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申请人（工号）',
  `usage_scenario` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产使用场景',
  `comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`asset_id`) USING BTREE,
  UNIQUE INDEX `code_unique`(`asset_code`) USING BTREE COMMENT '资产编号唯一性'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资产表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of asset
-- ----------------------------

-- ----------------------------
-- Table structure for asset_inventory_task
-- ----------------------------
DROP TABLE IF EXISTS `asset_inventory_task`;
CREATE TABLE `asset_inventory_task`  (
  `task_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '盘点任务编码',
  `task_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `inventory_users` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盘点人',
  `inventory_dept` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盘点组织',
  `start_date` date NULL DEFAULT NULL COMMENT '盘点开始时间',
  `end_date` date NULL DEFAULT NULL COMMENT '盘点结束时间',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盘点状态',
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人',
  `creator_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人名称',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`task_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '盘点任务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of asset_inventory_task
-- ----------------------------

-- ----------------------------
-- Table structure for asset_process
-- ----------------------------
DROP TABLE IF EXISTS `asset_process`;
CREATE TABLE `asset_process`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `process_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程类型',
  `user_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人工号',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人名称',
  `asset_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产编码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '流程总表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of asset_process
-- ----------------------------

-- ----------------------------
-- Table structure for asset_process_back
-- ----------------------------
DROP TABLE IF EXISTS `asset_process_back`;
CREATE TABLE `asset_process_back`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `process_id` int(255) NULL DEFAULT NULL COMMENT '流程总表id',
  `instance_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实例ID',
  `user_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人工号',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人名称',
  `asset_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台资产编码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资产归还流程' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of asset_process_back
-- ----------------------------

-- ----------------------------
-- Table structure for asset_process_counting
-- ----------------------------
DROP TABLE IF EXISTS `asset_process_counting`;
CREATE TABLE `asset_process_counting`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `process_id` int(11) NULL DEFAULT NULL COMMENT '流程总表id',
  `instance_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实例ID',
  `task_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盘点任务编码',
  `user_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人工号（盘点人工号）',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盘点人名称',
  `asset_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台资产编码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `counting_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盘点状态（0:待盘点,1:已盘点,2:盘点异常）',
  `counting_time` datetime NULL DEFAULT NULL COMMENT '盘点时间',
  `comment` varchar(2550) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资产盘点流程' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of asset_process_counting
-- ----------------------------

-- ----------------------------
-- Table structure for asset_process_disposal
-- ----------------------------
DROP TABLE IF EXISTS `asset_process_disposal`;
CREATE TABLE `asset_process_disposal`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `process_id` int(255) NULL DEFAULT NULL COMMENT '流程总表id',
  `instance_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实例ID',
  `user_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人工号',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人名称',
  `asset_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台资产编码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `file_Info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件信息',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型（510外卖；500报废）',
  `file_Info_add` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '补充附件信息',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批中；已完成',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资产处置流程' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of asset_process_disposal
-- ----------------------------

-- ----------------------------
-- Table structure for asset_process_exchange
-- ----------------------------
DROP TABLE IF EXISTS `asset_process_exchange`;
CREATE TABLE `asset_process_exchange`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `process_id` int(255) NULL DEFAULT NULL COMMENT '流程总表id',
  `instance_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实例ID',
  `user_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人工号',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人名称',
  `asset_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台资产编码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资产更换流程' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of asset_process_exchange
-- ----------------------------

-- ----------------------------
-- Table structure for asset_process_maintain
-- ----------------------------
DROP TABLE IF EXISTS `asset_process_maintain`;
CREATE TABLE `asset_process_maintain`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `process_id` int(255) NULL DEFAULT NULL COMMENT '流程总表id',
  `instance_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实例ID',
  `user_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人工号',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人名称',
  `asset_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台资产编码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `file_Info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资产维修流程' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of asset_process_maintain
-- ----------------------------

-- ----------------------------
-- Table structure for asset_process_receive
-- ----------------------------
DROP TABLE IF EXISTS `asset_process_receive`;
CREATE TABLE `asset_process_receive`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `process_id` int(255) NULL DEFAULT NULL COMMENT '流程总表id',
  `instance_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实例ID',
  `user_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人工号',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人名称',
  `asset_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台资产编码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资产领用流程' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of asset_process_receive
-- ----------------------------

-- ----------------------------
-- Table structure for asset_process_transfer
-- ----------------------------
DROP TABLE IF EXISTS `asset_process_transfer`;
CREATE TABLE `asset_process_transfer`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `process_id` int(255) NULL DEFAULT NULL COMMENT '流程总表id',
  `instance_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实例ID',
  `user_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人工号',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人名称',
  `asset_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台资产编码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资产转移流程' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of asset_process_transfer
-- ----------------------------

-- ----------------------------
-- Table structure for asset_process_transform
-- ----------------------------
DROP TABLE IF EXISTS `asset_process_transform`;
CREATE TABLE `asset_process_transform`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `process_id` int(255) NULL DEFAULT NULL COMMENT '流程总表id',
  `instance_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实例ID',
  `user_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人工号',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发起人名称',
  `asset_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台资产编码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `file_Info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件信息',
  `file_Info_add` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '补充附件信息',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批中；已完成',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资产改造流程' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of asset_process_transform
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代码生成业务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES (1, 'sys_user', '用户信息表', NULL, NULL, 'SysUser', 'crud', 'com.hexing.mature', 'mature', 'user', '用户信息', 'zxy', '0', '/', '{\"parentMenuId\":3}', 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27', NULL);
INSERT INTO `gen_table` VALUES (2, 'asset', '资产表', NULL, NULL, 'Asset', 'crud', 'com.hexing.assetNew', 'asset', 'asset', '资产表', 'zxy', '0', '/', '{}', 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07', NULL);
INSERT INTO `gen_table` VALUES (3, 'asset_process', '流程总表', NULL, NULL, 'AssetProcess', 'crud', 'com.hexing.assetNew', 'asset', 'process', '流程总', 'zxy', '0', '/', '{}', 'admin', '2022-09-08 16:26:06', '', '2022-09-26 09:44:33', NULL);
INSERT INTO `gen_table` VALUES (4, 'asset_process_back', '资产归还流程', NULL, NULL, 'AssetProcessBack', 'crud', 'com.hexing.assetNew', 'asset', 'back', '资产归还流程', 'zxy', '0', '/', '{}', 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:26:52', NULL);
INSERT INTO `gen_table` VALUES (5, 'asset_process_counting', '资产盘点流程', NULL, NULL, 'AssetProcessCounting', 'crud', 'com.hexing.assetNew', 'asset', 'counting', '资产盘点流程', 'zxy', '0', '/', '{}', 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:03', NULL);
INSERT INTO `gen_table` VALUES (6, 'asset_process_counting_task', '资产盘点任务流程', NULL, NULL, 'AssetProcessCountingTask', 'crud', 'com.hexing.assetNew', 'asset', 'task', '资产盘点任务流程', 'zxy', '0', '/', '{}', 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:17', NULL);
INSERT INTO `gen_table` VALUES (7, 'asset_process_disposal', '资产处置流程', NULL, NULL, 'AssetProcessDisposal', 'crud', 'com.hexing.assetNew', 'asset', 'disposal', '资产处置流程', 'zxy', '0', '/', '{}', 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:44', NULL);
INSERT INTO `gen_table` VALUES (8, 'asset_process_exchange', '资产更换流程', NULL, NULL, 'AssetProcessExchange', 'crud', 'com.hexing.assetNew', 'asset', 'exchange', '资产更换流程', 'zxy', '0', '/', '{}', 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:52', NULL);
INSERT INTO `gen_table` VALUES (9, 'asset_process_maintain', '资产维修流程', NULL, NULL, 'AssetProcessMaintain', 'crud', 'com.hexing.assetNew', 'asset', 'maintain', '资产维修流程', 'zxy', '0', '/', '{}', 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:02', NULL);
INSERT INTO `gen_table` VALUES (10, 'asset_process_receive', '资产领用流程', NULL, NULL, 'AssetProcessReceive', 'crud', 'com.hexing.assetNew', 'asset', 'receive', '资产领用流程', 'zxy', '0', '/', '{}', 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:09', NULL);
INSERT INTO `gen_table` VALUES (11, 'asset_process_transfer', '资产转移流程', NULL, NULL, 'AssetProcessTransfer', 'crud', 'com.hexing.assetNew', 'asset', 'transfer', '资产转移流程', 'zxy', '0', '/', '{}', 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:22', NULL);
INSERT INTO `gen_table` VALUES (12, 'asset_process_transform', '资产改造流程', NULL, NULL, 'AssetProcessTransform', 'crud', 'com.hexing.assetNew', 'asset', 'transform', '资产改造流程', 'zxy', '0', '/', '{}', 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:29', NULL);
INSERT INTO `gen_table` VALUES (13, 'asset_counting_task', '盘点任务表', NULL, NULL, 'AssetCountingTask', 'crud', 'com.hexing.mature', 'mature', 'task', '盘点任务', 'zxy', '0', '/', NULL, 'admin', '2022-09-13 14:45:15', '', NULL, NULL);

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 142 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
INSERT INTO `gen_table_column` VALUES (1, '1', 'user_id', '用户ID', 'bigint(20)', 'Long', 'userId', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (2, '1', 'dept_id', '部门ID', 'bigint(20)', 'Long', 'deptId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (3, '1', 'user_name', '用户账号', 'varchar(30)', 'String', 'userName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (4, '1', 'nick_name', '用户昵称', 'varchar(30)', 'String', 'nickName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 4, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (5, '1', 'user_type', '用户类型（00系统用户）', 'varchar(2)', 'String', 'userType', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'select', '', 5, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (6, '1', 'email', '用户邮箱', 'varchar(50)', 'String', 'email', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (7, '1', 'phonenumber', '手机号码', 'varchar(11)', 'String', 'phonenumber', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (8, '1', 'sex', '用户性别（0男 1女 2未知）', 'char(1)', 'String', 'sex', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'select', '', 8, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (9, '1', 'avatar', '头像地址', 'varchar(100)', 'String', 'avatar', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (10, '1', 'password', '密码', 'varchar(100)', 'String', 'password', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (11, '1', 'status', '帐号状态（0正常 1停用）', 'char(1)', 'String', 'status', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'radio', '', 11, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (12, '1', 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 12, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (13, '1', 'login_ip', '最后登录IP', 'varchar(128)', 'String', 'loginIp', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 13, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (14, '1', 'login_date', '最后登录时间', 'datetime', 'Date', 'loginDate', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 14, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (15, '1', 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 15, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (16, '1', 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 16, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (17, '1', 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 17, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (18, '1', 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 18, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (19, '1', 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'textarea', '', 19, 'admin', '2022-09-08 10:05:19', '', '2022-09-08 10:06:27');
INSERT INTO `gen_table_column` VALUES (20, '2', 'asset_id', '资产id', 'bigint(20)', 'Long', 'assetId', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (21, '2', 'asset_name', '固定资产名称', 'varchar(255)', 'String', 'assetName', '0', '0', NULL, '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (22, '2', 'asset_code', '平台资产编号', 'varchar(255)', 'String', 'assetCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (23, '2', 'financial_asset_code', '财务资产编号', 'varchar(255)', 'String', 'financialAssetCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (25, '2', 'category', '资产分类描述', 'varchar(255)', 'String', 'category', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (26, '2', 'asset_status', '资产状态描述', 'char(1)', 'String', 'assetStatus', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'radio', '', 7, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (27, '2', 'factory_no', '出厂编号', 'varchar(255)', 'String', 'factoryNo', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (28, '2', 'standard', '规格型号', 'varchar(255)', 'String', 'standard', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (29, '2', 'measure', '单位', 'varchar(255)', 'String', 'measure', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (30, '2', 'buyer', '采购人（工号）', 'varchar(255)', 'String', 'buyer', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 11, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (31, '2', 'buy_date', '采购日期', 'date', 'Date', 'buyDate', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 12, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (32, '2', 'net_worth', '净值', 'double', 'Long', 'netWorth', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 13, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (33, '2', 'warranty', '保修期（月）', 'int(5)', 'Integer', 'warranty', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 14, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (34, '2', 'can_use_months', '预计使用寿命（月）', 'int(5)', 'Integer', 'canUseMonths', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 15, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (35, '2', 'can_use_years', '预计使用寿命（年）', 'int(5)', 'Integer', 'canUseYears', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 16, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (36, '2', 'capitalization_date', '资本化日期/资产价值录入日期', 'date', 'Date', 'capitalizationDate', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 17, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (37, '2', 'monetary_unit', '资产原值币制', 'varchar(10)', 'String', 'monetaryUnit', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 18, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (38, '2', 'company_code', '公司代码', 'varchar(255)', 'String', 'companyCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 19, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (39, '2', 'company_name', '公司代码描述', 'varchar(255)', 'String', 'companyName', '0', '0', NULL, '1', '1', '1', '1', 'LIKE', 'input', '', 20, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (40, '2', 'location', '存放地点', 'varchar(255)', 'String', 'location', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 21, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (41, '2', 'provider', '供应商', 'varchar(255)', 'String', 'provider', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 22, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (42, '2', 'amount', '数量', 'int(5)', 'Integer', 'amount', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 23, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (43, '2', 'brand', '品牌', 'varchar(255)', 'String', 'brand', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 24, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (44, '2', 'cost_center', '成本中心（编号）', 'varchar(255)', 'String', 'costCenter', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 25, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (45, '2', 'cost_center_name', '成本中心描述', 'varchar(255)', 'String', 'costCenterName', '0', '0', NULL, '1', '1', '1', '1', 'LIKE', 'input', '', 26, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (46, '2', 'manage_dept', '管理部门描述', 'varchar(255)', 'String', 'manageDept', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 27, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (47, '2', 'contract_no', '合同单号', 'varchar(255)', 'String', 'contractNo', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 28, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (48, '2', 'proposer', '申请人（工号）', 'varchar(255)', 'String', 'proposer', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 29, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (49, '2', 'usage_scenario', '资产使用场景', 'varchar(255)', 'String', 'usageScenario', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 30, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (50, '2', 'comment', '备注', 'varchar(255)', 'String', 'comment', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 31, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (51, '2', 'create_by', '创建者', 'varchar(255)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 32, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (52, '2', 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 33, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (53, '2', 'update_by', '更新者', 'varchar(255)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 34, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (54, '2', 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 35, 'admin', '2022-09-08 15:52:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (55, '3', 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-08 16:26:06', '', '2022-09-26 09:44:33');
INSERT INTO `gen_table_column` VALUES (56, '3', 'process_type', '流程类型', 'varchar(255)', 'String', 'processType', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'select', 'dingtalk_asset_process_type', 2, 'admin', '2022-09-08 16:26:06', '', '2022-09-26 09:44:33');
INSERT INTO `gen_table_column` VALUES (57, '3', 'user_code', '发起人工号', 'varchar(255)', 'String', 'userCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2022-09-08 16:26:06', '', '2022-09-26 09:44:33');
INSERT INTO `gen_table_column` VALUES (58, '3', 'asset_code', '资产编码', 'varchar(255)', 'String', 'assetCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-09-08 16:26:06', '', '2022-09-26 09:44:33');
INSERT INTO `gen_table_column` VALUES (59, '3', 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 5, 'admin', '2022-09-08 16:26:06', '', '2022-09-26 09:44:33');
INSERT INTO `gen_table_column` VALUES (60, '3', 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2022-09-08 16:26:06', '', '2022-09-26 09:44:33');
INSERT INTO `gen_table_column` VALUES (61, '4', 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:26:52');
INSERT INTO `gen_table_column` VALUES (62, '4', 'process_id', '流程总表id', 'int(255)', 'Long', 'processId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:26:52');
INSERT INTO `gen_table_column` VALUES (63, '4', 'instance_id', '实例ID', 'varchar(255)', 'String', 'instanceId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:26:52');
INSERT INTO `gen_table_column` VALUES (64, '4', 'user_code', '发起人工号', 'varchar(255)', 'String', 'userCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:26:52');
INSERT INTO `gen_table_column` VALUES (65, '4', 'asset_code', '平台资产编码', 'varchar(255)', 'String', 'assetCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:26:52');
INSERT INTO `gen_table_column` VALUES (66, '4', 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:26:52');
INSERT INTO `gen_table_column` VALUES (67, '4', 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:26:52');
INSERT INTO `gen_table_column` VALUES (68, '5', 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:03');
INSERT INTO `gen_table_column` VALUES (69, '5', 'process_id', '流程总表id', 'int(11)', 'Long', 'processId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:03');
INSERT INTO `gen_table_column` VALUES (70, '5', 'instance_id', '实例ID', 'varchar(255)', 'String', 'instanceId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:03');
INSERT INTO `gen_table_column` VALUES (71, '5', 'counting_task_code', '盘点任务编码', 'varchar(255)', 'String', 'countingTaskCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:03');
INSERT INTO `gen_table_column` VALUES (72, '5', 'user_code', '发起人工号', 'varchar(255)', 'String', 'userCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:03');
INSERT INTO `gen_table_column` VALUES (73, '5', 'asset_code', '平台资产编码', 'varchar(255)', 'String', 'assetCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:03');
INSERT INTO `gen_table_column` VALUES (74, '5', 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:03');
INSERT INTO `gen_table_column` VALUES (75, '5', 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:03');
INSERT INTO `gen_table_column` VALUES (76, '6', 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:17');
INSERT INTO `gen_table_column` VALUES (77, '6', 'process_id', '流程总表id', 'int(255)', 'Long', 'processId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:17');
INSERT INTO `gen_table_column` VALUES (78, '6', 'instance_id', '实例ID', 'varchar(255)', 'String', 'instanceId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:17');
INSERT INTO `gen_table_column` VALUES (79, '6', 'user_code', '发起人工号', 'varchar(255)', 'String', 'userCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:17');
INSERT INTO `gen_table_column` VALUES (80, '6', 'counting_task_code', '盘点任务编码', 'varchar(255)', 'String', 'countingTaskCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:17');
INSERT INTO `gen_table_column` VALUES (81, '6', 'counting_range', '盘点范围', 'varchar(255)', 'String', 'countingRange', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:17');
INSERT INTO `gen_table_column` VALUES (82, '6', 'start_date', '开始时间', 'date', 'Date', 'startDate', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 7, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:17');
INSERT INTO `gen_table_column` VALUES (83, '6', 'end_date', '结束时间', 'date', 'Date', 'endDate', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 8, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:17');
INSERT INTO `gen_table_column` VALUES (84, '6', 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 9, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:17');
INSERT INTO `gen_table_column` VALUES (85, '6', 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 10, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:17');
INSERT INTO `gen_table_column` VALUES (86, '7', 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:44');
INSERT INTO `gen_table_column` VALUES (87, '7', 'process_id', '流程总表id', 'int(255)', 'Long', 'processId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:44');
INSERT INTO `gen_table_column` VALUES (88, '7', 'instance_id', '实例ID', 'varchar(255)', 'String', 'instanceId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:44');
INSERT INTO `gen_table_column` VALUES (89, '7', 'user_code', '发起人工号', 'varchar(255)', 'String', 'userCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:44');
INSERT INTO `gen_table_column` VALUES (90, '7', 'asset_code', '平台资产编码', 'varchar(255)', 'String', 'assetCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:44');
INSERT INTO `gen_table_column` VALUES (91, '7', 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:44');
INSERT INTO `gen_table_column` VALUES (92, '7', 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:44');
INSERT INTO `gen_table_column` VALUES (93, '8', 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:52');
INSERT INTO `gen_table_column` VALUES (94, '8', 'process_id', '流程总表id', 'int(255)', 'Long', 'processId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:52');
INSERT INTO `gen_table_column` VALUES (95, '8', 'instance_id', '实例ID', 'varchar(255)', 'String', 'instanceId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:52');
INSERT INTO `gen_table_column` VALUES (96, '8', 'user_code', '发起人工号', 'varchar(255)', 'String', 'userCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:52');
INSERT INTO `gen_table_column` VALUES (97, '8', 'asset_code', '平台资产编码', 'varchar(255)', 'String', 'assetCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:52');
INSERT INTO `gen_table_column` VALUES (98, '8', 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:52');
INSERT INTO `gen_table_column` VALUES (99, '8', 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:27:52');
INSERT INTO `gen_table_column` VALUES (100, '9', 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:02');
INSERT INTO `gen_table_column` VALUES (101, '9', 'process_id', '流程总表id', 'int(255)', 'Long', 'processId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:02');
INSERT INTO `gen_table_column` VALUES (102, '9', 'instance_id', '实例ID', 'varchar(255)', 'String', 'instanceId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:02');
INSERT INTO `gen_table_column` VALUES (103, '9', 'user_code', '发起人工号', 'varchar(255)', 'String', 'userCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:02');
INSERT INTO `gen_table_column` VALUES (104, '9', 'asset_code', '平台资产编码', 'varchar(255)', 'String', 'assetCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:02');
INSERT INTO `gen_table_column` VALUES (105, '9', 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:02');
INSERT INTO `gen_table_column` VALUES (106, '9', 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:02');
INSERT INTO `gen_table_column` VALUES (107, '10', 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:09');
INSERT INTO `gen_table_column` VALUES (108, '10', 'process_id', '流程总表id', 'int(255)', 'Long', 'processId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:09');
INSERT INTO `gen_table_column` VALUES (109, '10', 'instance_id', '实例ID', 'varchar(255)', 'String', 'instanceId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:09');
INSERT INTO `gen_table_column` VALUES (110, '10', 'user_code', '发起人工号', 'varchar(255)', 'String', 'userCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:09');
INSERT INTO `gen_table_column` VALUES (111, '10', 'asset_code', '平台资产编码', 'varchar(255)', 'String', 'assetCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:09');
INSERT INTO `gen_table_column` VALUES (112, '10', 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:09');
INSERT INTO `gen_table_column` VALUES (113, '10', 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:09');
INSERT INTO `gen_table_column` VALUES (114, '11', 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:22');
INSERT INTO `gen_table_column` VALUES (115, '11', 'process_id', '流程总表id', 'int(255)', 'Long', 'processId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:22');
INSERT INTO `gen_table_column` VALUES (116, '11', 'instance_id', '实例ID', 'varchar(255)', 'String', 'instanceId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:22');
INSERT INTO `gen_table_column` VALUES (117, '11', 'user_code', '发起人工号', 'varchar(255)', 'String', 'userCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:22');
INSERT INTO `gen_table_column` VALUES (118, '11', 'asset_code', '平台资产编码', 'varchar(255)', 'String', 'assetCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:22');
INSERT INTO `gen_table_column` VALUES (119, '11', 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:22');
INSERT INTO `gen_table_column` VALUES (120, '11', 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:22');
INSERT INTO `gen_table_column` VALUES (121, '12', 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:29');
INSERT INTO `gen_table_column` VALUES (122, '12', 'process_id', '流程总表id', 'int(255)', 'Long', 'processId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:29');
INSERT INTO `gen_table_column` VALUES (123, '12', 'instance_id', '实例ID', 'varchar(255)', 'String', 'instanceId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:29');
INSERT INTO `gen_table_column` VALUES (124, '12', 'user_code', '发起人工号', 'varchar(255)', 'String', 'userCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-09-08 16:26:06', '', '2022-09-08 16:28:29');
INSERT INTO `gen_table_column` VALUES (125, '12', 'asset_code', '平台资产编码', 'varchar(255)', 'String', 'assetCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2022-09-08 16:26:07', '', '2022-09-08 16:28:29');
INSERT INTO `gen_table_column` VALUES (126, '12', 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2022-09-08 16:26:07', '', '2022-09-08 16:28:29');
INSERT INTO `gen_table_column` VALUES (127, '12', 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2022-09-08 16:26:07', '', '2022-09-08 16:28:29');
INSERT INTO `gen_table_column` VALUES (128, '13', 'task_id', '盘点任务id', 'varchar(255)', 'String', 'taskId', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-13 14:45:15', '', NULL);
INSERT INTO `gen_table_column` VALUES (129, '13', 'task_code', '盘点任务编码', 'varchar(255)', 'String', 'taskCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-09-13 14:45:15', '', NULL);
INSERT INTO `gen_table_column` VALUES (130, '13', 'user_code', '发起人', 'varchar(255)', 'String', 'userCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2022-09-13 14:45:15', '', NULL);
INSERT INTO `gen_table_column` VALUES (131, '13', 'inventory_range', '盘点范围', 'varchar(255)', 'String', 'inventoryRange', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-09-13 14:45:15', '', NULL);
INSERT INTO `gen_table_column` VALUES (132, '13', 'asset_counted', '已盘点资产数', 'int(20)', 'Long', 'assetCounted', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2022-09-13 14:45:15', '', NULL);
INSERT INTO `gen_table_column` VALUES (133, '13', 'asset_not_counted', '待盘点资产数', 'int(20)', 'Long', 'assetNotCounted', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2022-09-13 14:45:15', '', NULL);
INSERT INTO `gen_table_column` VALUES (134, '13', 'asset_abnormal', '异常资产数目', 'int(20)', 'Long', 'assetAbnormal', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2022-09-13 14:45:15', '', NULL);
INSERT INTO `gen_table_column` VALUES (135, '13', 'start_date', '盘点开始时间', 'date', 'Date', 'startDate', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 8, 'admin', '2022-09-13 14:45:15', '', NULL);
INSERT INTO `gen_table_column` VALUES (136, '13', 'end_date', '盘点结束时间', 'date', 'Date', 'endDate', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 9, 'admin', '2022-09-13 14:45:15', '', NULL);
INSERT INTO `gen_table_column` VALUES (137, '13', 'status', '盘点状态', 'varchar(255)', 'String', 'status', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'radio', '', 10, 'admin', '2022-09-13 14:45:15', '', NULL);
INSERT INTO `gen_table_column` VALUES (138, '2', 'responsible_person_code', '保管人工号（老工号）', 'varchar(255)', 'String', 'responsiblePersonCode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, '', '2022-09-15 08:56:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (139, '2', 'total_value', '资产总价值', 'double', 'Long', 'totalValue', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 13, '', '2022-09-15 08:56:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (140, '2', 'inventory_status', '盘点状态', 'varchar(255)', 'String', 'inventoryStatus', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'select', 'asset_counting_status', 32, '', '2022-09-15 08:56:44', '', '2022-09-15 13:54:07');
INSERT INTO `gen_table_column` VALUES (141, '2', 'remark', '备注', 'varchar(255)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 38, '', '2022-09-15 08:56:44', '', '2022-09-15 13:54:07');

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob NULL COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'Blob类型的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`, `calendar_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日历信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'Cron类型的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------
INSERT INTO `qrtz_cron_triggers` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME4', 'DEFAULT', '0 0 8,12,17,21 * * ? ', 'Asia/Shanghai');
INSERT INTO `qrtz_cron_triggers` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME5', 'DEFAULT', '0 59 23 * * ? ', 'Asia/Shanghai');

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint(13) NOT NULL COMMENT '触发的时间',
  `sched_time` bigint(13) NOT NULL COMMENT '定时器制定的时间',
  `priority` int(11) NOT NULL COMMENT '优先级',
  `state` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态',
  `job_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`, `entry_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '已触发的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务组名',
  `description` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务详细信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------
INSERT INTO `qrtz_job_details` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME4', 'DEFAULT', NULL, 'com.hexing.quartz.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000F5441534B5F50524F504552544945537372001F636F6D2E686578696E672E71756172747A2E646F6D61696E2E5379734A6F6200000000000000010200084C000A636F6E63757272656E747400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C000C696E766F6B6554617267657471007E00094C00086A6F6247726F757071007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C00076A6F624E616D6571007E00094C000D6D697366697265506F6C69637971007E00094C000673746174757371007E000978720028636F6D2E686578696E672E636F6D6D6F6E2E636F72652E646F6D61696E2E42617365456E7469747900000000000000010200074C0008637265617465427971007E00094C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C0006706172616D7371007E00034C000672656D61726B71007E00094C000B73656172636856616C756571007E00094C0008757064617465427971007E00094C000A75706461746554696D6571007E000C787074000561646D696E7372000E6A6176612E7574696C2E44617465686A81014B59741903000078707708000001836468B80078707400007070707400013174001530203020382C31322C31372C3231202A202A203F2074002853796E63456D706C6F796565446570744A6F622E6175746F456D706C6F7965655472616E7366657274000744454641554C547372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000000004740018E5908CE6ADA5E4BABAE59198E7BB84E7BB87E69EB6E69E8474000131740001307800);
INSERT INTO `qrtz_job_details` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME5', 'DEFAULT', NULL, 'com.hexing.quartz.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000F5441534B5F50524F504552544945537372001F636F6D2E686578696E672E71756172747A2E646F6D61696E2E5379734A6F6200000000000000010200084C000A636F6E63757272656E747400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C000C696E766F6B6554617267657471007E00094C00086A6F6247726F757071007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C00076A6F624E616D6571007E00094C000D6D697366697265506F6C69637971007E00094C000673746174757371007E000978720028636F6D2E686578696E672E636F6D6D6F6E2E636F72652E646F6D61696E2E42617365456E7469747900000000000000010200074C0008637265617465427971007E00094C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C0006706172616D7371007E00034C000672656D61726B71007E00094C000B73656172636856616C756571007E00094C0008757064617465427971007E00094C000A75706461746554696D6571007E000C787074000838303031353330367372000E6A6176612E7574696C2E44617465686A81014B59741903000078707708000001836481126078707400007070707400013174000E30203539203233202A202A203F2074002A496E76656E746F72795461736B4A6F622E757064617465496E76656E746F72795461736B53746174757374000744454641554C547372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000000005740018E69BB4E696B0E79B98E782B9E4BBBBE58AA1E78AB6E6808174000131740001307800);

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`, `lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '存储的悲观锁信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('RuoyiScheduler', 'STATE_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('RuoyiScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '暂停的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint(13) NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint(13) NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`, `instance_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '调度器状态表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------
INSERT INTO `qrtz_scheduler_state` VALUES ('RuoyiScheduler', 'localhost.localdomain1665465215911', 1665471685853, 15000);

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint(7) NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint(12) NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint(10) NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '简单触发器的信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int(11) NULL DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int(11) NULL DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint(20) NULL DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint(20) NULL DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '同步机制的行锁表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint(13) NULL DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint(13) NULL DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int(11) NULL DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '触发器的类型',
  `start_time` bigint(13) NOT NULL COMMENT '开始时间',
  `end_time` bigint(13) NULL DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint(2) NULL DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `sched_name`(`sched_name`, `job_name`, `job_group`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '触发器详细信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------
INSERT INTO `qrtz_triggers` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME4', 'DEFAULT', 'TASK_CLASS_NAME4', 'DEFAULT', NULL, 1665478800000, -1, 5, 'WAITING', 'CRON', 1665461820000, 0, NULL, -1, '');
INSERT INTO `qrtz_triggers` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME5', 'DEFAULT', 'TASK_CLASS_NAME5', 'DEFAULT', NULL, 1665503940000, -1, 5, 'WAITING', 'CRON', 1665461820000, 0, NULL, -1, '');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int(5) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2022-08-05 14:10:41', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2022-08-05 14:10:41', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2022-08-05 14:10:41', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'false', 'Y', 'admin', '2022-08-05 14:10:41', 'admin', '2022-09-15 15:06:10', '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2022-08-05 14:10:41', '', NULL, '是否开启注册用户功能（true开启，false关闭）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(4) NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_label_en` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典标签_英文',
  `dict_value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 211 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', NULL, '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', NULL, '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', NULL, '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', NULL, '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', NULL, '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', NULL, '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', NULL, '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', NULL, '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', NULL, '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', NULL, 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', NULL, 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', NULL, 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', NULL, 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', NULL, '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', NULL, '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', NULL, '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', NULL, '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 1, '新增', NULL, '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (19, 2, '修改', NULL, '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (20, 3, '删除', NULL, '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (21, 4, '授权', NULL, '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (22, 5, '导出', NULL, '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (23, 6, '导入', NULL, '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (24, 7, '强退', NULL, '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (25, 8, '生成代码', NULL, '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (26, 9, '清空数据', NULL, '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (27, 1, '成功', NULL, '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (28, 2, '失败', NULL, '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (100, 1, '个人', 'person', '0', 'asset_usage_scenario', NULL, 'default', 'N', '0', 'admin', '2022-09-08 16:52:53', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (101, 2, '公共', 'common', '1', 'asset_usage_scenario', NULL, 'default', 'N', '0', 'admin', '2022-09-08 16:53:09', '80015306', '2022-09-20 09:50:54', NULL);
INSERT INTO `sys_dict_data` VALUES (102, 1, 'IT', 'IT', '1', 'manage_dept', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:13:14', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (103, 2, '研发测试部', 'dev', '2', 'manage_dept', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:13:44', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (104, 3, '生产部', 'product', '3', 'manage_dept', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:14:06', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (105, 4, '总务部', 'adm', '4', 'manage_dept', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:14:25', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (107, 1, '固定资产-租赁资产', '固定资产-租赁资产', '101', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:20:57', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (108, 2, '固定资产-房屋建筑物', '固定资产-房屋建筑物', '102', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:21:47', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (109, 3, '固定资产-机器设备', '固定资产-机器设备', '103', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:22:02', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (110, 4, '固定资产-动力及电气', '固定资产-动力及电气', '104', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:22:22', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (111, 5, '固定资产-办公设备', '固定资产-办公设备', '105', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:22:34', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (112, 6, '固定资产-运输设备', '固定资产-运输设备', '106', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:22:57', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (113, 7, '固定资产-电子设备', '固定资产-电子设备', '107', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:23:12', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (114, 8, '固定资产-电脑及软件', '固定资产-电脑及软件', '108', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:23:23', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (115, 9, '固定资产-其他设备', '固定资产-其他设备', '109', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:23:44', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (116, 10, '固定资产-土地使用权', '固定资产-土地使用权', '110', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:23:57', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (117, 11, '固定资产-专利及非专利技术', '固定资产-专利及非专利技术', '111', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:24:17', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (118, 12, '固定资产-商标及著作权', '固定资产-商标及著作权', '112', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:24:31', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (119, 13, '固定资产-特许权', '固定资产-特许权', '113', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:24:44', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (120, 14, '固定资产-租赁', '固定资产-租赁', '114', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:25:01', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (121, 15, '无形资产-租赁资产', '无形资产-租赁资产', '201', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:26:52', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (122, 16, '无形资产-房屋建筑物', '无形资产-房屋建筑物', '202', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:27:15', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (123, 17, '无形资产-机器设备', '无形资产-机器设备', '203', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:27:27', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (124, 18, '无形资产-动力及电气', '无形资产-动力及电气', '204', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:27:46', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (125, 19, '无形资产-办公设备', '无形资产-办公设备', '205', 'asset_category', '', 'default', 'N', '0', 'admin', '2022-09-08 17:28:09', 'admin', '2022-09-08 17:28:27', NULL);
INSERT INTO `sys_dict_data` VALUES (126, 20, '无形资产-运输设备', '无形资产-运输设备', '206', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:28:42', 'admin', '2022-09-08 17:28:51', NULL);
INSERT INTO `sys_dict_data` VALUES (127, 21, '无形资产-电子设备', '无形资产-电子设备', '207', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:29:10', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (128, 22, '无形资产-电脑及软件', '无形资产-电脑及软件', '208', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:29:32', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (129, 23, '无形资产-其他设备', '无形资产-其他设备', '209', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:29:46', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (130, 24, '无形资产-土地使用权', '无形资产-土地使用权', '210', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:29:58', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (131, 25, '无形资产-专利及非专利技术', '无形资产-专利及非专利技术', '211', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:30:17', 'admin', '2022-09-08 17:30:25', NULL);
INSERT INTO `sys_dict_data` VALUES (132, 26, '无形资产-商标及著作权', '无形资产-商标及著作权', '212', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:30:47', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (133, 27, '无形资产-特许权', '无形资产-特许权', '213', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:31:02', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (134, 28, '无形资产-租赁', '无形资产-租赁', '214', 'asset_category', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:31:23', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (135, 0, '闲置', '闲置', '0', 'asset_status', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:34:01', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (136, 1, '在用', '在用', '1', 'asset_status', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:34:16', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (137, 2, '维修', '维修', '2', 'asset_status', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:34:27', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (138, 3, '报废', '报废', '3', 'asset_status', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:34:41', 'admin', '2022-09-08 17:34:48', NULL);
INSERT INTO `sys_dict_data` VALUES (139, 4, '出租', '出租', '4', 'asset_status', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:34:57', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (140, 5, '未使用', '未使用', '5', 'asset_status', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:35:06', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (141, 6, '外卖', '外卖', '6', 'asset_status', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:35:19', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (142, 0, '杭州海兴电力科技股份有限公司', '杭州海兴电力科技股份有限公司', '10', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:39:24', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (143, 1, '湖北省智网瑞达电力设计有限公司', '湖北省智网瑞达电力设计有限公司', '11', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:39:38', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (144, 2, '海南海兴国际科技发展有限公司', '海南海兴国际科技发展有限公司', '12', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:39:52', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (145, 3, '南京海兴电网技术有限公司', '南京海兴电网技术有限公司', '13', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:49:36', 'admin', '2022-09-08 17:49:40', NULL);
INSERT INTO `sys_dict_data` VALUES (146, 4, '宁波泽联科技有限公司', '宁波泽联科技有限公司', '14', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:49:57', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (147, 5, '杭州海兴泽科信息技术有限公司', '杭州海兴泽科信息技术有限公司', '15', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:50:09', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (148, 6, '杭州粒合信息科技有限公司', '杭州粒合信息科技有限公司', '16', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:50:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (149, 7, '宁波恒力达科技有限公司', '宁波恒力达科技有限公司', '17', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:50:25', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (150, 8, '宁波海兴新能源有限公司', '宁波海兴新能源有限公司', '18', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:50:32', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (151, 9, '杭州利沃得电源有限公司', '杭州利沃得电源有限公司', '19', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:50:45', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (152, 10, '宁波甬利仪表科技有限公司', '宁波甬利仪表科技有限公司', '20', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:50:55', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (153, 11, '深圳利沃德电源有限公司', '深圳利沃德电源有限公司', '21', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:51:03', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (154, 12, '湖南海兴电器有限责任公司', '湖南海兴电器有限责任公司', '22', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:51:13', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (155, 13, '宁波涌聚能源科技有限责任公司', '宁波涌聚能源科技有限责任公司', '23', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:51:21', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (156, 14, '深圳和兴电力科技有限公司', '深圳和兴电力科技有限公司', '24', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:51:29', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (157, 15, '宁波甬奥科技有限公司', '宁波甬奥科技有限公司', '25', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:51:37', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (158, 16, '广东和兴电力科技有限公司', '广东和兴电力科技有限公司', '26', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:51:47', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (159, 17, '浙江海兴控股集团有限公司', '浙江海兴控股集团有限公司', '27', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:52:03', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (160, 18, '丽水海聚股权投资有限公司', '丽水海聚股权投资有限公司', '28', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:52:16', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (161, 19, '杭州埃度软件有限公司', '杭州埃度软件有限公司', '29', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:52:24', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (162, 20, '宁波积睿企业管理合伙企业（有限合伙）', '宁波积睿企业管理合伙企业（有限合伙）', '30', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:52:33', 'admin', '2022-09-08 17:52:42', NULL);
INSERT INTO `sys_dict_data` VALUES (163, 21, '丽水聚泽企业管理合伙企业 （有限合伙)', '丽水聚泽企业管理合伙企业 （有限合伙)', '31', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:53:01', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (164, 22, '丽水聚得企业管理合伙企业（有限合伙）', '丽水聚得企业管理合伙企业（有限合伙）', '32', 'company_list', NULL, 'default', 'N', '0', 'admin', '2022-09-08 17:53:11', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (165, 0, '公司代码', 'companyCode', 'companyCode', 'asset_import_required_field', NULL, 'default', 'N', '0', 'admin', '2022-09-09 14:28:26', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (166, 1, '公司代码描述', 'companyName', 'companyName', 'asset_import_required_field', NULL, 'default', 'N', '0', 'admin', '2022-09-09 14:28:39', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (167, 2, '固定资产名称', 'assetName', 'assetName', 'asset_import_required_field', NULL, 'default', 'N', '0', 'admin', '2022-09-09 14:28:57', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (168, 3, '财务资产编号', 'financialAssetCode', 'financialAssetCode', 'asset_import_required_field', NULL, 'default', 'N', '0', 'admin', '2022-09-09 14:29:30', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (169, 4, '资产分类描述', 'category', 'category', 'asset_import_required_field', NULL, 'default', 'N', '0', 'admin', '2022-09-09 14:29:43', 'admin', '2022-09-09 14:29:48', NULL);
INSERT INTO `sys_dict_data` VALUES (170, 5, '资产总价值', 'totalValue', 'totalValue', 'asset_import_required_field', NULL, 'default', 'N', '0', 'admin', '2022-09-09 14:30:02', 'admin', '2022-09-09 14:30:06', NULL);
INSERT INTO `sys_dict_data` VALUES (171, 6, '净值', 'netWorth', 'netWorth', 'asset_import_required_field', NULL, 'default', 'N', '0', 'admin', '2022-09-09 14:30:23', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (172, 7, '资本化日期/资产价值录入日期', 'capitalizationDate', 'capitalizationDate', 'asset_import_required_field', NULL, 'default', 'N', '0', 'admin', '2022-09-09 14:30:38', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (173, 8, '管理部门描述', 'manageDept', 'manageDept', 'asset_import_required_field', NULL, 'default', 'N', '0', 'admin', '2022-09-09 14:30:51', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (174, 0, '盘点中', 'counting', '0', 'inventory_task_status', NULL, 'success', 'N', '0', 'admin', '2022-09-15 11:22:16', 'admin', '2022-09-16 14:16:04', NULL);
INSERT INTO `sys_dict_data` VALUES (175, 1, '已完成', 'finished', '1', 'inventory_task_status', NULL, 'info', 'N', '0', 'admin', '2022-09-15 11:22:31', 'admin', '2022-09-16 14:16:09', NULL);
INSERT INTO `sys_dict_data` VALUES (176, 0, '待盘点', 'not counted', '0', 'asset_counting_status', NULL, 'primary', 'N', '0', 'admin', '2022-09-15 11:24:35', 'admin', '2022-09-21 09:11:58', NULL);
INSERT INTO `sys_dict_data` VALUES (177, 1, '已盘点', 'counted', '1', 'asset_counting_status', NULL, 'success', 'N', '0', 'admin', '2022-09-15 11:25:01', 'admin', '2022-09-21 09:12:04', NULL);
INSERT INTO `sys_dict_data` VALUES (178, 2, '盘点异常', 'abnormal', '2', 'asset_counting_status', NULL, 'warning', 'N', '0', 'admin', '2022-09-15 11:25:15', 'admin', '2022-09-21 09:12:12', NULL);
INSERT INTO `sys_dict_data` VALUES (179, 0, '杭州2号楼', '杭州2号楼', '杭州2号楼', 'asset_location', NULL, 'default', 'N', '0', '80015306', '2022-09-19 15:20:49', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (180, 1, '杭州1号楼1楼', '杭州1号楼1楼', '杭州1号楼1楼', 'asset_location', NULL, 'default', 'N', '0', '80015306', '2022-09-19 15:21:21', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (181, 2, '杭州1号楼2楼', '杭州1号楼2楼', '杭州1号楼2楼', 'asset_location', NULL, 'default', 'N', '0', '80015306', '2022-09-19 15:22:49', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (182, 0, '领用流程', 'asset_receive', '100', 'dingtalk_asset_process_type', NULL, 'default', 'N', '0', '80015306', '2022-09-19 16:16:03', '80015306', '2022-09-20 13:44:42', NULL);
INSERT INTO `sys_dict_data` VALUES (183, 1, '归还流程', 'asset_back', '200', 'dingtalk_asset_process_type', NULL, 'default', 'N', '0', '80015306', '2022-09-19 16:16:16', '80015306', '2022-09-20 13:44:48', NULL);
INSERT INTO `sys_dict_data` VALUES (184, 2, '更换流程', 'asset_exchange', '300', 'dingtalk_asset_process_type', NULL, 'default', 'N', '0', '80015306', '2022-09-19 16:16:39', '80015306', '2022-09-20 13:45:17', NULL);
INSERT INTO `sys_dict_data` VALUES (185, 3, '转移流程', 'asset_transfer', '400', 'dingtalk_asset_process_type', NULL, 'default', 'N', '0', '80015306', '2022-09-19 16:16:56', '80015306', '2022-09-20 14:15:22', NULL);
INSERT INTO `sys_dict_data` VALUES (190, 9, '保管人工号（老工号）', 'responsiblePersonCode', 'responsiblePersonCode', 'asset_import_required_field', NULL, 'default', 'N', '0', '80015306', '2022-09-19 18:05:20', '80015306', '2022-09-19 18:05:30', NULL);
INSERT INTO `sys_dict_data` VALUES (191, 10, '资产使用场景', 'usageScenario', 'usageScenario', 'asset_import_required_field', NULL, 'default', 'N', '0', '80015306', '2022-09-21 11:07:13', '80015306', '2022-09-21 11:07:28', NULL);
INSERT INTO `sys_dict_data` VALUES (192, 11, '资产状态描述', 'assetStatus', 'assetStatus', 'asset_import_required_field', NULL, 'default', 'N', '0', '80015306', '2022-09-21 11:09:21', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (193, 12, '存放地点', 'location', 'location', 'asset_import_required_field', NULL, 'default', 'N', '0', '80015306', '2022-09-21 11:09:34', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (194, 0, '代领用流程', '代领用流程', '110', 'dingtalk_asset_process_type', NULL, 'default', 'N', '0', '80015306', '2022-09-22 14:32:48', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (195, 1, '代归还流程', '代归还流程', '210', 'dingtalk_asset_process_type', NULL, 'default', 'N', '0', '80015306', '2022-09-22 14:32:57', '80015306', '2022-09-22 14:33:01', NULL);
INSERT INTO `sys_dict_data` VALUES (197, 8, '报废流程', '报废流程', '500', 'ding_asset_process_type', NULL, 'default', 'N', '0', 'admin', '2022-09-28 16:04:42', '80015306', '2022-10-11 10:58:03', NULL);
INSERT INTO `sys_dict_data` VALUES (198, 9, '外卖流程', '外卖流程', '510', 'ding_asset_process_type', NULL, 'default', 'N', '0', 'admin', '2022-09-28 16:05:03', '80015306', '2022-10-11 10:58:16', NULL);
INSERT INTO `sys_dict_data` VALUES (199, 7, '改造流程', '改造流程', '600', 'ding_asset_process_type', NULL, 'default', 'N', '0', 'admin', '2022-09-28 16:05:21', '80015306', '2022-10-11 10:47:56', NULL);
INSERT INTO `sys_dict_data` VALUES (200, 6, '维修流程', '维修流程', '700', 'ding_asset_process_type', NULL, 'default', 'N', '0', 'admin', '2022-09-28 16:05:36', '80015306', '2022-10-11 10:47:49', NULL);
INSERT INTO `sys_dict_data` VALUES (201, 10, '盘点流程', '盘点流程', '1000', 'ding_asset_process_type', NULL, 'default', 'N', '0', 'admin', '2022-09-28 16:06:19', '80015306', '2022-10-11 10:48:24', NULL);
INSERT INTO `sys_dict_data` VALUES (202, 0, '未生效', '未生效', '0', 'asset_process_status', NULL, 'info', 'N', '0', 'admin', '2022-10-09 08:43:10', 'admin', '2022-10-09 08:44:46', NULL);
INSERT INTO `sys_dict_data` VALUES (203, 0, '已生效', '已生效', '1', 'asset_process_status', NULL, 'success', 'N', '0', 'admin', '2022-10-09 08:43:27', 'admin', '2022-10-09 08:44:52', NULL);
INSERT INTO `sys_dict_data` VALUES (204, 0, '领用流程', 'asset_receive', '100', 'ding_asset_process_type', NULL, 'default', 'N', '0', '80015306', '2022-10-11 10:40:45', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (205, 1, '代领用流程', '代领用流程', '110', 'ding_asset_process_type', NULL, 'default', 'N', '0', '80015306', '2022-10-11 10:45:41', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (206, 2, '归还流程', 'asset_back', '200', 'ding_asset_process_type', NULL, 'default', 'N', '0', '80015306', '2022-10-11 10:45:55', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (207, 3, '代归还流程', '代归还流程', '210', 'ding_asset_process_type', NULL, 'default', 'N', '0', '80015306', '2022-10-11 10:46:35', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (208, 4, '更换流程', 'asset_exchange', '300', 'ding_asset_process_type', NULL, 'default', 'N', '0', '80015306', '2022-10-11 10:47:02', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (209, 5, '转移流程', 'asset_transfer', '400', 'ding_asset_process_type', NULL, 'default', 'N', '0', '80015306', '2022-10-11 10:47:19', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (210, 0, '导入', '导入', '000', 'ding_asset_process_type', NULL, 'default', 'N', '0', '80015306', '2022-10-11 11:45:08', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 112 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2022-08-05 14:10:41', '', NULL, '登录状态列表');
INSERT INTO `sys_dict_type` VALUES (100, '资产分类', 'asset_category', '0', 'admin', '2022-09-08 16:47:03', 'admin', '2022-09-08 16:48:18', NULL);
INSERT INTO `sys_dict_type` VALUES (101, '资产状态', 'asset_status', '0', 'admin', '2022-09-08 16:48:05', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (102, '资产存放地点', 'asset_location', '0', 'admin', '2022-09-08 16:48:38', '80015306', '2022-09-19 15:14:06', NULL);
INSERT INTO `sys_dict_type` VALUES (103, '资产管理部门', 'manage_dept', '0', 'admin', '2022-09-08 16:49:02', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (104, '资产使用场景', 'asset_usage_scenario', '0', 'admin', '2022-09-08 16:49:50', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (105, '集团公司', 'company_list', '0', 'admin', '2022-09-08 17:37:46', 'admin', '2022-09-08 17:48:26', '公司列表（用于平台资产编号生成）');
INSERT INTO `sys_dict_type` VALUES (106, '资产导入必填字段', 'asset_import_required_field', '0', 'admin', '2022-09-09 14:28:00', '', NULL, '资产导入必填字段');
INSERT INTO `sys_dict_type` VALUES (107, '资产盘点任务状态', 'inventory_task_status', '0', 'admin', '2022-09-15 11:20:10', 'admin', '2022-09-15 11:21:24', '');
INSERT INTO `sys_dict_type` VALUES (108, '资产盘点状态', 'asset_counting_status', '0', 'admin', '2022-09-15 11:21:13', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (109, '钉钉资产调拨流程类型', 'dingtalk_asset_process_type', '0', '80015306', '2022-09-19 16:15:14', 'admin', '2022-09-28 16:03:06', NULL);
INSERT INTO `sys_dict_type` VALUES (110, '钉钉资产流程类型', 'ding_asset_process_type', '0', 'admin', '2022-09-28 16:03:46', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (111, '资产流程状态', 'asset_process_status', '0', 'admin', '2022-10-09 08:42:21', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (4, '同步人员组织架构', 'DEFAULT', 'SyncEmployeeDeptJob.autoEmployeeTransfer', '0 0 8,12,17,21 * * ? ', '1', '1', '0', 'admin', '2022-09-22 16:55:28', 'admin', '2022-09-22 16:57:19', '');
INSERT INTO `sys_job` VALUES (5, '更新盘点任务状态', 'DEFAULT', 'InventoryTaskJob.updateInventoryTaskStatus', '0 59 23 * * ? ', '1', '1', '0', '80015306', '2022-09-22 17:22:04', '', '2022-09-22 17:23:34', '');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统访问记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
  `menu_en_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单英文名',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` int(1) NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int(1) NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2025 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', NULL, 0, 1, 'system', NULL, '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2022-08-05 14:10:40', '', NULL, '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', NULL, 0, 2, 'monitor', NULL, '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2022-08-05 14:10:40', '', NULL, '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', NULL, 0, 3, 'tool', NULL, '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2022-08-05 14:10:40', '', NULL, '系统工具目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', NULL, 1, 1, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2022-08-05 14:10:40', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', NULL, 1, 2, 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2022-08-05 14:10:40', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', NULL, 1, 3, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2022-08-05 14:10:40', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', NULL, 1, 4, 'dept', 'system/dept/index', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2022-08-05 14:10:40', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', NULL, 1, 5, 'post', 'system/post/index', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2022-08-05 14:10:40', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', NULL, 1, 6, 'dict', 'system/dict/index', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2022-08-05 14:10:40', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', NULL, 1, 7, 'config', 'system/config/index', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2022-08-05 14:10:40', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', NULL, 1, 8, 'notice', 'system/notice/index', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2022-08-05 14:10:40', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', NULL, 1, 9, 'log', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2022-08-05 14:10:40', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', NULL, 2, 1, 'online', 'monitor/online/index', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2022-08-05 14:10:40', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (110, '定时任务', NULL, 2, 2, 'job', 'monitor/job/index', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2022-08-05 14:10:40', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu` VALUES (111, '数据监控', NULL, 2, 3, 'druid', 'monitor/druid/index', '', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid', 'admin', '2022-08-05 14:10:40', '', NULL, '数据监控菜单');
INSERT INTO `sys_menu` VALUES (112, '服务监控', NULL, 2, 4, 'server', 'monitor/server/index', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2022-08-05 14:10:40', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (113, '缓存监控', NULL, 2, 5, 'cache', 'monitor/cache/index', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', '2022-08-05 14:10:40', '', NULL, '缓存监控菜单');
INSERT INTO `sys_menu` VALUES (114, '缓存列表', NULL, 2, 6, 'cacheList', 'monitor/cache/list', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis-list', 'admin', '2022-08-05 14:10:40', '', NULL, '缓存列表菜单');
INSERT INTO `sys_menu` VALUES (115, '表单构建', NULL, 3, 1, 'build', 'tool/build/index', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2022-08-05 14:10:40', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu` VALUES (116, '代码生成', NULL, 3, 2, 'gen', 'tool/gen/index', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2022-08-05 14:10:40', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu` VALUES (117, '系统接口', '系统接口', 3, 3, 'http://localhost/dev-api/doc.html#/home', 'tool/swagger/index', '', 0, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2022-08-05 14:10:40', 'admin', '2022-10-10 11:20:03', '系统接口菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', NULL, 108, 1, 'operlog', 'monitor/operlog/index', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2022-08-05 14:10:40', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', NULL, 108, 2, 'logininfor', 'monitor/logininfor/index', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', '2022-08-05 14:10:40', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', NULL, 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', NULL, 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', NULL, 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', NULL, 100, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', NULL, 100, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', NULL, 100, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', NULL, 100, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', NULL, 101, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', NULL, 101, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', NULL, 101, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', NULL, 101, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', NULL, 101, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', NULL, 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', NULL, 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', NULL, 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', NULL, 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', NULL, 103, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', NULL, 103, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', NULL, 103, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', NULL, 103, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', NULL, 104, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', NULL, 104, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', NULL, 104, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', NULL, 104, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', NULL, 104, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', NULL, 105, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', NULL, 105, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', NULL, 105, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', NULL, 105, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', NULL, 105, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', NULL, 106, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', NULL, 106, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', NULL, 106, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', NULL, 106, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', NULL, 106, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '公告查询', NULL, 107, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告新增', NULL, 107, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告修改', NULL, 107, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告删除', NULL, 107, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', NULL, 500, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', NULL, 500, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '日志导出', NULL, 500, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '登录查询', NULL, 501, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录删除', NULL, 501, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '日志导出', NULL, 501, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '在线查询', NULL, 109, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '批量强退', NULL, 109, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '单条强退', NULL, 109, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '任务查询', NULL, 110, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1049, '任务新增', NULL, 110, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1050, '任务修改', NULL, 110, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1051, '任务删除', NULL, 110, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1052, '状态修改', NULL, 110, 5, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1053, '任务导出', NULL, 110, 7, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1054, '生成查询', NULL, 115, 1, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成修改', NULL, 115, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成删除', NULL, 115, 3, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '导入代码', NULL, 115, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '预览代码', NULL, 115, 4, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '生成代码', NULL, 115, 5, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2022-08-05 14:10:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2000, '资产入库', '资产入库', 0, 4, 'asset_acceptance', NULL, NULL, 1, 0, 'M', '0', '0', '', 'build', 'admin', '2022-09-08 15:51:05', 'admin', '2022-09-08 16:18:20', '');
INSERT INTO `sys_menu` VALUES (2001, '入库验收', '入库验收', 2000, 1, 'checkList', '/', NULL, 1, 0, 'C', '0', '0', 'asset:acceptance:check', 'build', 'admin', '2022-09-08 15:52:07', 'admin', '2022-09-08 16:09:21', '');
INSERT INTO `sys_menu` VALUES (2002, '资产明细', '资产明细', 0, 5, 'asset', NULL, NULL, 1, 0, 'M', '0', '0', '', 'build', 'admin', '2022-09-08 16:17:27', 'admin', '2022-09-08 17:23:17', '');
INSERT INTO `sys_menu` VALUES (2003, '资产列表', '资产列表', 2002, 1, 'asset', 'asset/asset/index', NULL, 1, 0, 'C', '0', '0', 'asset:collection:list', 'build', 'admin', '2022-09-08 16:21:19', 'admin', '2022-09-08 17:23:25', '');
INSERT INTO `sys_menu` VALUES (2004, '资产处置', '资产处置', 2002, 2, 'disposal', 'asset/disposal/index', NULL, 1, 0, 'C', '0', '0', 'asset:disposal:list', 'build', 'admin', '2022-09-08 16:22:54', 'admin', '2022-09-20 16:49:22', '');
INSERT INTO `sys_menu` VALUES (2005, '资产维修', '资产维修', 2002, 3, 'maintain', 'asset/maintain/index', NULL, 1, 0, 'C', '0', '0', 'asset:maintain:list', 'build', 'admin', '2022-09-08 16:24:30', 'admin', '2022-09-20 16:49:33', '');
INSERT INTO `sys_menu` VALUES (2006, '资产改造', '资产改造', 2002, 4, 'transform', 'asset/transform/index', NULL, 1, 0, 'C', '0', '0', 'asset:transform:list', 'build', 'admin', '2022-09-08 16:25:40', 'admin', '2022-09-20 16:52:17', '');
INSERT INTO `sys_menu` VALUES (2007, '资产盘点', '资产盘点', 0, 6, 'inventory', NULL, NULL, 1, 0, 'M', '0', '0', '', 'build', 'admin', '2022-09-08 16:26:26', 'admin', '2022-09-15 10:12:10', '');
INSERT INTO `sys_menu` VALUES (2008, '盘点任务', '盘点任务', 2007, 1, 'task', 'asset/task/index', NULL, 1, 0, 'C', '0', '0', 'asset:task:list', 'build', 'admin', '2022-09-08 16:27:23', 'admin', '2022-09-15 10:12:27', '');
INSERT INTO `sys_menu` VALUES (2010, '报废箱', '报废箱', 0, 8, 'scrap', '/', NULL, 1, 0, 'C', '0', '0', 'asset:scrap:list', 'build', 'admin', '2022-09-08 16:29:22', 'admin', '2022-09-23 13:20:03', '');
INSERT INTO `sys_menu` VALUES (2011, '添加', '添加', 2008, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'asset:task:add', '#', 'admin', '2022-09-16 09:31:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2012, '导出', '导出', 2008, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'asset:task:export', '#', 'admin', '2022-09-16 09:31:38', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2013, '导入', '导入', 2003, 0, '', NULL, NULL, 1, 0, 'F', '0', '0', 'asset:asset:import', '#', 'admin', '2022-09-16 10:42:22', 'admin', '2022-09-16 10:54:10', '');
INSERT INTO `sys_menu` VALUES (2014, '导出', '导出', 2003, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'asset:asset:export', '#', 'admin', '2022-09-16 10:42:50', 'admin', '2022-09-16 10:54:20', '');
INSERT INTO `sys_menu` VALUES (2015, '删除', 'remove', 2003, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'asset:asset:remove', '#', 'admin', '2022-09-16 10:55:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2016, '下载导入模板', '下载导入模板', 2003, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'asset:asset:template', '#', '80015306', '2022-09-16 13:07:09', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2017, '查看资产详情', '查看资产详情', 2003, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'asset:asset:query', '#', 'admin', '2022-09-16 13:10:59', 'admin', '2022-09-16 13:11:08', '');
INSERT INTO `sys_menu` VALUES (2019, '删除', '删除', 2008, 3, '', NULL, NULL, 1, 0, 'F', '0', '0', 'asset:task:remove', '#', 'admin', '2022-09-16 15:25:04', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2020, '资产调拨', '资产调拨', 2002, 5, 'transfer', 'asset/transfer/index', NULL, 1, 0, 'C', '0', '0', 'asset:process:list', 'build', 'admin', '2022-09-21 11:41:28', 'admin', '2022-09-26 09:40:40', '');
INSERT INTO `sys_menu` VALUES (2021, '盘点记录导出', '盘点记录导出', 2008, 4, '', NULL, NULL, 1, 0, 'F', '0', '0', 'asset:counting:export', '#', 'admin', '2022-09-21 16:15:01', 'admin', '2022-09-21 16:15:11', '');
INSERT INTO `sys_menu` VALUES (2022, '资产统计', '资产统计', 0, 7, 'statistics', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'chart', 'admin', '2022-09-23 13:19:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2023, '数据统计', '数据统计', 2022, 1, 'data', 'statistics/data/index', NULL, 1, 0, 'C', '0', '0', 'statistics:data:list', 'tree', 'admin', '2022-09-23 13:21:05', 'admin', '2022-09-27 18:13:24', '');
INSERT INTO `sys_menu` VALUES (2024, '盘点统计', '盘点统计', 2022, 2, 'inventory', 'statistics/inventory/index', NULL, 1, 0, 'C', '0', '0', 'statistics:inventory:list', 'slider', 'admin', '2022-09-23 13:22:26', 'admin', '2022-09-27 18:13:43', '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int(4) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', 0xE696B0E78988E69CACE58685E5AEB9, '0', 'admin', '2022-08-05 14:10:41', '', NULL, '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', 0xE7BBB4E68AA4E58685E5AEB9, '0', 'admin', '2022-08-05 14:10:41', 'admin', '2022-09-08 16:10:07', '管理员');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int(1) NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作日志记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2022-08-05 14:10:40', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2022-08-05 14:10:40', '', NULL, '普通角色');
INSERT INTO `sys_role` VALUES (100, '资产管理员', 'manager', 3, '1', 1, 1, '0', '0', 'admin', '2022-09-13 11:01:14', 'admin', '2022-09-26 16:33:48', '资产管理员');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 4);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 111);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 116);
INSERT INTO `sys_role_menu` VALUES (2, 117);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1054);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (100, 1);
INSERT INTO `sys_role_menu` VALUES (100, 2);
INSERT INTO `sys_role_menu` VALUES (100, 3);
INSERT INTO `sys_role_menu` VALUES (100, 100);
INSERT INTO `sys_role_menu` VALUES (100, 101);
INSERT INTO `sys_role_menu` VALUES (100, 102);
INSERT INTO `sys_role_menu` VALUES (100, 103);
INSERT INTO `sys_role_menu` VALUES (100, 104);
INSERT INTO `sys_role_menu` VALUES (100, 105);
INSERT INTO `sys_role_menu` VALUES (100, 106);
INSERT INTO `sys_role_menu` VALUES (100, 107);
INSERT INTO `sys_role_menu` VALUES (100, 108);
INSERT INTO `sys_role_menu` VALUES (100, 109);
INSERT INTO `sys_role_menu` VALUES (100, 110);
INSERT INTO `sys_role_menu` VALUES (100, 111);
INSERT INTO `sys_role_menu` VALUES (100, 112);
INSERT INTO `sys_role_menu` VALUES (100, 113);
INSERT INTO `sys_role_menu` VALUES (100, 114);
INSERT INTO `sys_role_menu` VALUES (100, 115);
INSERT INTO `sys_role_menu` VALUES (100, 116);
INSERT INTO `sys_role_menu` VALUES (100, 117);
INSERT INTO `sys_role_menu` VALUES (100, 500);
INSERT INTO `sys_role_menu` VALUES (100, 501);
INSERT INTO `sys_role_menu` VALUES (100, 1000);
INSERT INTO `sys_role_menu` VALUES (100, 1001);
INSERT INTO `sys_role_menu` VALUES (100, 1002);
INSERT INTO `sys_role_menu` VALUES (100, 1003);
INSERT INTO `sys_role_menu` VALUES (100, 1004);
INSERT INTO `sys_role_menu` VALUES (100, 1005);
INSERT INTO `sys_role_menu` VALUES (100, 1006);
INSERT INTO `sys_role_menu` VALUES (100, 1007);
INSERT INTO `sys_role_menu` VALUES (100, 1008);
INSERT INTO `sys_role_menu` VALUES (100, 1009);
INSERT INTO `sys_role_menu` VALUES (100, 1010);
INSERT INTO `sys_role_menu` VALUES (100, 1011);
INSERT INTO `sys_role_menu` VALUES (100, 1012);
INSERT INTO `sys_role_menu` VALUES (100, 1013);
INSERT INTO `sys_role_menu` VALUES (100, 1014);
INSERT INTO `sys_role_menu` VALUES (100, 1015);
INSERT INTO `sys_role_menu` VALUES (100, 1016);
INSERT INTO `sys_role_menu` VALUES (100, 1017);
INSERT INTO `sys_role_menu` VALUES (100, 1018);
INSERT INTO `sys_role_menu` VALUES (100, 1019);
INSERT INTO `sys_role_menu` VALUES (100, 1020);
INSERT INTO `sys_role_menu` VALUES (100, 1021);
INSERT INTO `sys_role_menu` VALUES (100, 1022);
INSERT INTO `sys_role_menu` VALUES (100, 1023);
INSERT INTO `sys_role_menu` VALUES (100, 1024);
INSERT INTO `sys_role_menu` VALUES (100, 1025);
INSERT INTO `sys_role_menu` VALUES (100, 1026);
INSERT INTO `sys_role_menu` VALUES (100, 1027);
INSERT INTO `sys_role_menu` VALUES (100, 1028);
INSERT INTO `sys_role_menu` VALUES (100, 1029);
INSERT INTO `sys_role_menu` VALUES (100, 1030);
INSERT INTO `sys_role_menu` VALUES (100, 1031);
INSERT INTO `sys_role_menu` VALUES (100, 1032);
INSERT INTO `sys_role_menu` VALUES (100, 1033);
INSERT INTO `sys_role_menu` VALUES (100, 1034);
INSERT INTO `sys_role_menu` VALUES (100, 1035);
INSERT INTO `sys_role_menu` VALUES (100, 1036);
INSERT INTO `sys_role_menu` VALUES (100, 1037);
INSERT INTO `sys_role_menu` VALUES (100, 1038);
INSERT INTO `sys_role_menu` VALUES (100, 1039);
INSERT INTO `sys_role_menu` VALUES (100, 1040);
INSERT INTO `sys_role_menu` VALUES (100, 1041);
INSERT INTO `sys_role_menu` VALUES (100, 1042);
INSERT INTO `sys_role_menu` VALUES (100, 1043);
INSERT INTO `sys_role_menu` VALUES (100, 1044);
INSERT INTO `sys_role_menu` VALUES (100, 1045);
INSERT INTO `sys_role_menu` VALUES (100, 1046);
INSERT INTO `sys_role_menu` VALUES (100, 1047);
INSERT INTO `sys_role_menu` VALUES (100, 1048);
INSERT INTO `sys_role_menu` VALUES (100, 1049);
INSERT INTO `sys_role_menu` VALUES (100, 1050);
INSERT INTO `sys_role_menu` VALUES (100, 1051);
INSERT INTO `sys_role_menu` VALUES (100, 1052);
INSERT INTO `sys_role_menu` VALUES (100, 1053);
INSERT INTO `sys_role_menu` VALUES (100, 1054);
INSERT INTO `sys_role_menu` VALUES (100, 1055);
INSERT INTO `sys_role_menu` VALUES (100, 1056);
INSERT INTO `sys_role_menu` VALUES (100, 1057);
INSERT INTO `sys_role_menu` VALUES (100, 1058);
INSERT INTO `sys_role_menu` VALUES (100, 1059);
INSERT INTO `sys_role_menu` VALUES (100, 2000);
INSERT INTO `sys_role_menu` VALUES (100, 2001);
INSERT INTO `sys_role_menu` VALUES (100, 2002);
INSERT INTO `sys_role_menu` VALUES (100, 2003);
INSERT INTO `sys_role_menu` VALUES (100, 2004);
INSERT INTO `sys_role_menu` VALUES (100, 2005);
INSERT INTO `sys_role_menu` VALUES (100, 2006);
INSERT INTO `sys_role_menu` VALUES (100, 2007);
INSERT INTO `sys_role_menu` VALUES (100, 2008);
INSERT INTO `sys_role_menu` VALUES (100, 2010);
INSERT INTO `sys_role_menu` VALUES (100, 2011);
INSERT INTO `sys_role_menu` VALUES (100, 2012);
INSERT INTO `sys_role_menu` VALUES (100, 2013);
INSERT INTO `sys_role_menu` VALUES (100, 2014);
INSERT INTO `sys_role_menu` VALUES (100, 2015);
INSERT INTO `sys_role_menu` VALUES (100, 2016);
INSERT INTO `sys_role_menu` VALUES (100, 2017);
INSERT INTO `sys_role_menu` VALUES (100, 2019);
INSERT INTO `sys_role_menu` VALUES (100, 2020);
INSERT INTO `sys_role_menu` VALUES (100, 2021);
INSERT INTO `sys_role_menu` VALUES (100, 2022);
INSERT INTO `sys_role_menu` VALUES (100, 2023);
INSERT INTO `sys_role_menu` VALUES (100, 2024);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户，1为钉钉用户）',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2004 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 1, 'admin', '超级管理员', '00', '', '', '1', '', '$2a$10$xza.syD3Pwu4Dc4k4jwThekl2Vh4vO2cSTDa6WZ21qcC2YWHGJKOO', '0', '0', '10.0.11.125', '2022-10-11 14:58:43', 'admin', '2022-08-05 14:10:40', '', '2022-10-11 14:58:43', '管理员');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);

SET FOREIGN_KEY_CHECKS = 1;
