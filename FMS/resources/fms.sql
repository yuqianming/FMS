/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : fms_1

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2019-01-15 10:39:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for build_mode_order
-- ----------------------------
DROP TABLE IF EXISTS `build_mode_order`;
CREATE TABLE `build_mode_order` (
  `build_mode` varchar(50) NOT NULL,
  `order_num` int(2) NOT NULL,
  PRIMARY KEY (`build_mode`),
  KEY `index_1` (`build_mode`,`order_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of build_mode_order
-- ----------------------------
INSERT INTO `build_mode_order` VALUES ('合计', '99');
INSERT INTO `build_mode_order` VALUES ('存量直接满足', '4');
INSERT INTO `build_mode_order` VALUES ('改造', '2');
INSERT INTO `build_mode_order` VALUES ('新建', '1');
INSERT INTO `build_mode_order` VALUES ('维护改造', '3');

-- ----------------------------
-- Table structure for department_info
-- ----------------------------
DROP TABLE IF EXISTS `department_info`;
CREATE TABLE `department_info` (
  `org_id` varchar(20) NOT NULL COMMENT '部门ID',
  `org_name` varchar(200) DEFAULT NULL COMMENT '部门名称',
  `pid` varchar(20) DEFAULT NULL COMMENT '部门父ID',
  PRIMARY KEY (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of department_info
-- ----------------------------

-- ----------------------------
-- Table structure for menu_info
-- ----------------------------
DROP TABLE IF EXISTS `menu_info`;
CREATE TABLE `menu_info` (
  `menu_id` varchar(50) NOT NULL COMMENT '菜单ID',
  `menu_name` varchar(100) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单路径',
  `pid` varchar(50) DEFAULT NULL COMMENT '菜单父ID',
  `type` varchar(50) DEFAULT NULL COMMENT '类别，menu：菜单、button：按钮',
  `level` varchar(2) DEFAULT NULL,
  `sort` int(10) DEFAULT NULL COMMENT '排序值',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu_info
-- ----------------------------
INSERT INTO `menu_info` VALUES ('BT01010', '查询', null, 'FN0101', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT01011', '导出Excel', null, 'FN0101', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02010', '批量导入', null, 'FN0201', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02011', '导出Excel', null, 'FN0201', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02020', '批量导入', null, 'FN0202', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02021', '导出Excel', null, 'FN0202', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02030', '批量导入', null, 'FN0203', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02031', '导出Excel', null, 'FN0203', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02040', '删除', null, 'FN0204', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02041', '修改', null, 'FN0204', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02042', '批量导入', null, 'FN0204', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02043', '导出Excel', null, 'FN0204', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02050', '删除', null, 'FN0205', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02051', '清空已付款', null, 'FN0205', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02052', '修改', null, 'FN0205', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02053', '批量导入', null, 'FN0205', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02054', '导出Excel', null, 'FN0205', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02060', '批量导入', null, 'FN0206', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT02061', '导出Excel', null, 'FN0206', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT03010', '新增', null, 'FN0301', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT03011', '删除', null, 'FN0301', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT03012', '导入数据', null, 'FN0301', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT03013', '开始测试', null, 'FN0301', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT03014', '确认签字', null, 'FN0301', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT03015', '导出Excel', null, 'FN0301', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT03016', '修改', null, 'FN0301', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT03020', '新增', null, 'FN0302', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT03021', '删除', null, 'FN0302', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT03022', '导入数据', null, 'FN0302', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT03023', '开始测试', null, 'FN0302', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT03024', '确认签字', null, 'FN0302', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT03025', '导出Excel', null, 'FN0302', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT03026', '修改', null, 'FN0302', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT04010', '付款完成', null, 'FN0401', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT04011', '删除', null, 'FN0401', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT04020', '批量导入', null, 'FN0402', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT04021', '导入确认', null, 'FN0402', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501010', '新增', null, 'FN050101', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501011', '删除', null, 'FN050101', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501012', '批量导入', null, 'FN050101', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501013', '修改', null, 'FN050101', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501020', '新增', null, 'FN050102', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501021', '删除', null, 'FN050102', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501022', '批量导入', null, 'FN050102', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501023', '修改', null, 'FN050102', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501030', '新增', null, 'FN050103', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501031', '删除', null, 'FN050103', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501032', '批量导入', null, 'FN050103', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501033', '修改', null, 'FN050103', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501040', '新增', null, 'FN050104', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501041', '删除', null, 'FN050104', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501042', '批量导入', null, 'FN050104', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501043', '修改', null, 'FN050104', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501050', '新增', null, 'FN050105', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501051', '删除', null, 'FN050105', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501052', '批量导入', null, 'FN050105', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501053', '修改', null, 'FN050105', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501060', '新增', null, 'FN050106', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501061', '删除', null, 'FN050106', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501062', '批量导入', null, 'FN050106', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501063', '修改', null, 'FN050106', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501070', '新增', null, 'FN050107', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501071', '删除', null, 'FN050107', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501072', '批量导入', null, 'FN050107', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501073', '修改', null, 'FN050107', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501080', '新增', null, 'FN050108', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501081', '删除', null, 'FN050108', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501082', '批量导入', null, 'FN050108', 'button', null, null);
INSERT INTO `menu_info` VALUES ('BT0501083', '修改', null, 'FN050108', 'button', null, null);
INSERT INTO `menu_info` VALUES ('FN0100', '项目总览', '#', null, 'menu', '1', '1');
INSERT INTO `menu_info` VALUES ('FN0101', '整体浏览', '/overview/index', 'FN0100', 'menu', '1', '1');
INSERT INTO `menu_info` VALUES ('FN0200', '数据导入', '#', null, 'menu', '1', '5');
INSERT INTO `menu_info` VALUES ('FN0201', '导入成本单', '/costSheet/index', 'FN0200', 'menu', '1', '1');
INSERT INTO `menu_info` VALUES ('FN0202', '核算入库表', '/check/index', 'FN0200', 'menu', '1', '2');
INSERT INTO `menu_info` VALUES ('FN0203', '仓储交易日志', '/deal/index', 'FN0200', 'menu', '1', '3');
INSERT INTO `menu_info` VALUES ('FN0204', '签字未付款', '/signature/index', 'FN0200', 'menu', '1', '4');
INSERT INTO `menu_info` VALUES ('FN0205', '已付款', '/payment/index', 'FN0200', 'menu', '1', '5');
INSERT INTO `menu_info` VALUES ('FN0206', '财务物资调整', '/material/index', 'FN0200', 'menu', '1', '6');
INSERT INTO `menu_info` VALUES ('FN0300', '付款测试', '#', null, 'menu', '1', '2');
INSERT INTO `menu_info` VALUES ('FN0301', '订单测试', '/orderTest/index', 'FN0300', 'menu', '1', '1');
INSERT INTO `menu_info` VALUES ('FN0302', '项目测试', '/projectTest/index', 'FN0300', 'menu', '1', '2');
INSERT INTO `menu_info` VALUES ('FN0400', '正式付款', '#', null, 'menu', '1', '3');
INSERT INTO `menu_info` VALUES ('FN0401', '签字付款', '/signPayment/index', 'FN0400', 'menu', '1', '1');
INSERT INTO `menu_info` VALUES ('FN0402', '线上付款导入', '/onlinePayment/index', 'FN0400', 'menu', '1', '2');
INSERT INTO `menu_info` VALUES ('FN0500', '数据字典', '#', null, 'menu', '1', '6');
INSERT INTO `menu_info` VALUES ('FN0501', '数据字典', '/dictionary/index', 'FN0500', 'menu', '1', '1');
INSERT INTO `menu_info` VALUES ('FN050101', '科目', '/account/index', 'FN0501', 'menu', '2', '1');
INSERT INTO `menu_info` VALUES ('FN050102', '供应商', '/supplier/index', 'FN0501', 'menu', '2', '2');
INSERT INTO `menu_info` VALUES ('FN050103', 'PMS', '/pms/index', 'FN0501', 'menu', '2', '3');
INSERT INTO `menu_info` VALUES ('FN050104', 'CRM', '/crm/index', 'FN0501', 'menu', '2', '4');
INSERT INTO `menu_info` VALUES ('FN050105', '销项', '/cancelProj/index', 'FN0501', 'menu', '2', '5');
INSERT INTO `menu_info` VALUES ('FN050106', '旧项目', '/oldProj/index', 'FN0501', 'menu', '2', '6');
INSERT INTO `menu_info` VALUES ('FN050107', '税率', '/taxRate/index', 'FN0501', 'menu', '2', '7');
INSERT INTO `menu_info` VALUES ('FN050108', '供应商调整', '/supplierAdj/index', 'FN0501', 'menu', '2', '8');
INSERT INTO `menu_info` VALUES ('FN0600', '系统管理', '#', null, 'menu', '1', '11');
INSERT INTO `menu_info` VALUES ('FN0601', '用户管理', '/userAdmin/index', 'FN0600', 'menu', '1', '1');
INSERT INTO `menu_info` VALUES ('FN0602', '角色管理', '/roleAdmin/index', 'FN0600', 'menu', '1', '2');
INSERT INTO `menu_info` VALUES ('FN0603', '机构管理', '/departAdmin/index', 'FN0600', 'menu', '1', '3');
INSERT INTO `menu_info` VALUES ('FN0700', '数据核对', '#', null, 'menu', '1', '4');
INSERT INTO `menu_info` VALUES ('FN0701', '项目数据核对', '/projectCheck/index', 'FN0700', 'menu', '1', '1');
INSERT INTO `menu_info` VALUES ('FN0702', '已销项检查', '/cancelProjPay/index', 'FN0700', 'menu', '1', '2');
INSERT INTO `menu_info` VALUES ('FN0703', '超额付款检查', '/overPay/index', 'FN0700', 'menu', '1', '3');
INSERT INTO `menu_info` VALUES ('FN0704', '仓储数据核对', '/storeCheck/index', 'FN0700', 'menu', '1', '4');
INSERT INTO `menu_info` VALUES ('FN0800', '统计报表', '#', null, 'menu', '1', '7');
INSERT INTO `menu_info` VALUES ('FN0801', '销项检查汇总', '/cancelReport/index', 'FN0800', 'menu', '1', '1');
INSERT INTO `menu_info` VALUES ('FN0802', '超额付款检查汇总', '/overpayReport/index', 'FN0800', 'menu', '1', '2');
INSERT INTO `menu_info` VALUES ('FN0803', '已付款情况统计', '/paymentReport/index', 'FN0800', 'menu', '1', '3');
INSERT INTO `menu_info` VALUES ('FN0804', '成本单账龄分析', '/costReport/index', 'FN0800', 'menu', '1', '4');
INSERT INTO `menu_info` VALUES ('FN0805', '仓储核对汇总', '/storeReport/index', 'FN0800', 'menu', '1', '5');
INSERT INTO `menu_info` VALUES ('FN0900', '内容管理', '#', null, 'menu', '1', '10');
INSERT INTO `menu_info` VALUES ('FN0901', '公告管理', '/notice/index', 'FN0900', 'menu', '1', '1');
INSERT INTO `menu_info` VALUES ('FN0902', '文档管理', '/files/index', 'FN0900', 'menu', '1', '2');

-- ----------------------------
-- Table structure for order_type
-- ----------------------------
DROP TABLE IF EXISTS `order_type`;
CREATE TABLE `order_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(10) NOT NULL COMMENT '类型名称',
  PRIMARY KEY (`id`),
  KEY `index_1` (`id`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_type
-- ----------------------------
INSERT INTO `order_type` VALUES ('1', 'PO');
INSERT INTO `order_type` VALUES ('2', 'OFP');
INSERT INTO `order_type` VALUES ('3', 'SK');
INSERT INTO `order_type` VALUES ('4', 'SJ');
INSERT INTO `order_type` VALUES ('5', '利息资本化');

-- ----------------------------
-- Table structure for role_info
-- ----------------------------
DROP TABLE IF EXISTS `role_info`;
CREATE TABLE `role_info` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `org_id` varchar(20) DEFAULT NULL COMMENT '所属机构',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_info
-- ----------------------------

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `menu_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6096 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for role_user
-- ----------------------------
DROP TABLE IF EXISTS `role_user`;
CREATE TABLE `role_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  `user_id` varchar(20) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_user
-- ----------------------------

-- ----------------------------
-- Table structure for table_excel
-- ----------------------------
DROP TABLE IF EXISTS `table_excel`;
CREATE TABLE `table_excel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(50) NOT NULL,
  `field` varchar(50) NOT NULL,
  `type` varchar(50) DEFAULT NULL,
  `field_name` varchar(100) DEFAULT NULL,
  `column_num` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of table_excel
-- ----------------------------
INSERT INTO `table_excel` VALUES ('1', 't_cost', ' list_id         ', 'String', '清单ID    ', '1');
INSERT INTO `table_excel` VALUES ('2', 't_cost', ' busi_date       ', 'String', '业务日期    ', '2');
INSERT INTO `table_excel` VALUES ('3', 't_cost', ' order_code      ', 'String', '采购订单号   ', '3');
INSERT INTO `table_excel` VALUES ('4', 't_cost', ' source_type     ', 'String', '订单来源类型  ', '4');
INSERT INTO `table_excel` VALUES ('5', 't_cost', ' supplier_name   ', 'String', '供应商名称   ', '5');
INSERT INTO `table_excel` VALUES ('6', 't_cost', ' supplier_code   ', 'String', '供应商编号   ', '6');
INSERT INTO `table_excel` VALUES ('7', 't_cost', ' material_code   ', 'String', '物料编码    ', '7');
INSERT INTO `table_excel` VALUES ('8', 't_cost', ' manufacturer    ', 'String', '生产厂家    ', '8');
INSERT INTO `table_excel` VALUES ('9', 't_cost', ' spec_code       ', 'String', '规格编号    ', '9');
INSERT INTO `table_excel` VALUES ('10', 't_cost', ' material_name   ', 'String', '物料名称    ', '10');
INSERT INTO `table_excel` VALUES ('11', 't_cost', ' service_code    ', 'String', '物资服务编码  ', '11');
INSERT INTO `table_excel` VALUES ('12', 't_cost', ' unit            ', 'String', '计量单位    ', '12');
INSERT INTO `table_excel` VALUES ('13', 't_cost', ' quantity        ', 'BigDecimal', '数量      ', '13');
INSERT INTO `table_excel` VALUES ('14', 't_cost', ' unit_price_e_tax', 'BigDecimal', '不含税单价   ', '14');
INSERT INTO `table_excel` VALUES ('15', 't_cost', ' total_amt_e_tax ', 'BigDecimal', '不含税总金额  ', '15');
INSERT INTO `table_excel` VALUES ('19', 't_cost', ' period_cost_amt ', 'BigDecimal', '当期成本确认金额', '16');
INSERT INTO `table_excel` VALUES ('20', 't_cost', ' account_amt     ', 'BigDecimal', '入账金额    ', '17');
INSERT INTO `table_excel` VALUES ('21', 't_cost', ' project_code    ', 'String', '项目编码    ', '18');
INSERT INTO `table_excel` VALUES ('22', 't_cost', ' project_name    ', 'String', '项目名称    ', '19');
INSERT INTO `table_excel` VALUES ('23', 't_cost', ' address_code    ', 'String', '站址编码    ', '20');
INSERT INTO `table_excel` VALUES ('24', 't_cost', ' address_name    ', 'String', '站址名称    ', '21');
INSERT INTO `table_excel` VALUES ('25', 't_cost', ' accounting_org  ', 'String', '核算组织    ', '22');
INSERT INTO `table_excel` VALUES ('26', 't_cost', ' doc_type        ', 'String', '单据类型    ', '23');
INSERT INTO `table_excel` VALUES ('27', 't_cost', ' documentary     ', 'String', '制单人     ', '24');
INSERT INTO `table_excel` VALUES ('28', 't_cost', ' assemble_sts    ', 'String', '是否装配    ', '25');
INSERT INTO `table_excel` VALUES ('29', 't_cost', ' allocation_sts  ', 'String', '是否分摊    ', '26');
INSERT INTO `table_excel` VALUES ('30', 't_cost', ' asset_trans_sts ', 'String', '是否转资    ', '27');
INSERT INTO `table_excel` VALUES ('31', 't_cost', ' timestamp       ', 'Date', '时间戳     ', '28');
INSERT INTO `table_excel` VALUES ('32', 't_cost', ' account_code    ', 'String', '科目编码    ', '29');
INSERT INTO `table_excel` VALUES ('33', 't_cost', ' account_name    ', 'String', '科目名称    ', '30');
INSERT INTO `table_excel` VALUES ('34', 't_cost', ' order_type      ', 'String', '订单类型    ', '31');
INSERT INTO `table_excel` VALUES ('35', 't_cost', ' voucher_no      ', 'String', '凭证编号    ', '32');
INSERT INTO `table_excel` VALUES ('36', 't_cost', ' detail_no       ', 'String', '交易明细行号  ', '33');
INSERT INTO `table_excel` VALUES ('37', 't_account', 'account_code', 'String', '科目编号', '1');
INSERT INTO `table_excel` VALUES ('38', 't_account', 'account_name', 'String', '科目名称', '2');
INSERT INTO `table_excel` VALUES ('39', 't_supplier', 'supplier_code', 'String', '供应商编码', '1');
INSERT INTO `table_excel` VALUES ('40', 't_supplier', 'supplier_name', 'String', '供应商名称', '2');
INSERT INTO `table_excel` VALUES ('41', 't_tax_rate', 'tax_rate', 'BigDecimal', '税率', '2');
INSERT INTO `table_excel` VALUES ('42', 't_tax_rate', 'scope', 'String', '应用范围', '1');
INSERT INTO `table_excel` VALUES ('43', 'order_test', 'order_code', 'String', '订单编号', '2');
INSERT INTO `table_excel` VALUES ('44', 'order_test', 't_invoice_amt', 'BigDecimal', '本次开票金额', '8');
INSERT INTO `table_excel` VALUES ('45', 'order_test', 't_pay_amt', 'BigDecimal', '本次交付金额', '9');
INSERT INTO `table_excel` VALUES ('46', 'project_test', 'project_code', 'String', '项目编号', '3');
INSERT INTO `table_excel` VALUES ('47', 'project_test', 't_invoice_amt', 'BigDecimal', '本次开票金额', '8');
INSERT INTO `table_excel` VALUES ('48', 'project_test', 't_pay_amt', 'BigDecimal', '本次交付金额', '9');
INSERT INTO `table_excel` VALUES ('49', 't_godown_entry', 'entry_date', 'String', '日期', '1');
INSERT INTO `table_excel` VALUES ('50', 't_godown_entry', 'voucher_no', 'String', '凭证编号', '2');
INSERT INTO `table_excel` VALUES ('51', 't_godown_entry', 'entry_code', 'String', '入库单号', '3');
INSERT INTO `table_excel` VALUES ('52', 't_godown_entry', 'order_code', 'String', '采购订单号', '4');
INSERT INTO `table_excel` VALUES ('53', 't_godown_entry', 'amount', 'BigDecimal', '金额', '5');
INSERT INTO `table_excel` VALUES ('54', 't_store_deal', 'project_code', 'String', '项目编码', '1');
INSERT INTO `table_excel` VALUES ('55', 't_store_deal', 'project_name', 'String', '项目名称', '2');
INSERT INTO `table_excel` VALUES ('56', 't_store_deal', 'order_code', 'String', '采购订单号', '3');
INSERT INTO `table_excel` VALUES ('57', 't_store_deal', 'batch_no', 'String', '批次号', '4');
INSERT INTO `table_excel` VALUES ('58', 't_store_deal', 'material_code', 'String', 'PMS物料编码', '5');
INSERT INTO `table_excel` VALUES ('59', 't_store_deal', 'material_name', 'String', '物料名称', '6');
INSERT INTO `table_excel` VALUES ('60', 't_store_deal', 'busi_type', 'String', '业务类型', '7');
INSERT INTO `table_excel` VALUES ('61', 't_store_deal', 'opration_type', 'String', '操作类型', '8');
INSERT INTO `table_excel` VALUES ('62', 't_store_deal', 'supplier_code', 'String', '供应商编码', '9');
INSERT INTO `table_excel` VALUES ('63', 't_store_deal', 'supplier_name', 'String', '供应商名称', '10');
INSERT INTO `table_excel` VALUES ('64', 't_store_deal', 'quantity_org', 'int', '交易前数量', '11');
INSERT INTO `table_excel` VALUES ('65', 't_store_deal', 'quantity_txn', 'int', '交易数量', '12');
INSERT INTO `table_excel` VALUES ('66', 't_store_deal', 'quantity_left', 'int', '交易后数量', '13');
INSERT INTO `table_excel` VALUES ('67', 't_store_deal', 'total_amt_e_tax', 'BigDecimal', '总金额（产品净额）', '14');
INSERT INTO `table_excel` VALUES ('68', 't_store_deal', 'total_amt_i_tax', 'BigDecimal', '含税总金额（产品净额+税额）', '15');
INSERT INTO `table_excel` VALUES ('69', 't_store_deal', 'create_time', 'Date', '创建时间', '16');
INSERT INTO `table_excel` VALUES ('70', 't_pms', 'project_code', 'String', '项目编码', '1');
INSERT INTO `table_excel` VALUES ('71', 't_pms', 'project_name', 'String', '项目名称', '2');
INSERT INTO `table_excel` VALUES ('72', 't_pms', 'build_mode', 'String', '建设方式', '3');
INSERT INTO `table_excel` VALUES ('73', 't_pms', 'accept_date', 'String', '内验时间', '4');
INSERT INTO `table_excel` VALUES ('75', 't_pms', 'deliver_date', 'String', '交付时间', '5');
INSERT INTO `table_excel` VALUES ('76', 't_pms', 'project_status', 'String', '项目状态', '6');
INSERT INTO `table_excel` VALUES ('77', 't_pms', 'order_code', 'String', '需求订单号', '7');
INSERT INTO `table_excel` VALUES ('78', 't_crm', 'rent_status', 'String', '状态起租', '1');
INSERT INTO `table_excel` VALUES ('79', 't_crm', 'order_code', 'String', '需求订单号', '2');
INSERT INTO `table_excel` VALUES ('80', 't_cancel_proj', 'project_code', 'String', '项目编码', '1');
INSERT INTO `table_excel` VALUES ('81', 't_cancel_proj', 'project_name', 'String', '项目名称', '2');
INSERT INTO `table_excel` VALUES ('82', 't_old_proj', 'project_code', 'String', '项目编码', '1');
INSERT INTO `table_excel` VALUES ('83', 't_old_proj', 'project_name', 'String', '项目名称', '2');
INSERT INTO `table_excel` VALUES ('84', 't_supplier_adj', 's_supplier_code', 'String', '待调整供应商编码', '1');
INSERT INTO `table_excel` VALUES ('85', 't_supplier_adj', 's_supplier_name', 'String', '待调整供应商名称', '2');
INSERT INTO `table_excel` VALUES ('86', 't_supplier_adj', 't_supplier_code', 'String', '目标供应商编码', '3');
INSERT INTO `table_excel` VALUES ('87', 't_supplier_adj', 't_supplier_name', 'String', '目标供应商名称', '4');
INSERT INTO `table_excel` VALUES ('88', 't_signature', 'batch_no', 'String', '批次号', '1');
INSERT INTO `table_excel` VALUES ('89', 't_signature', 'project_code', 'String', '项目编号', '2');
INSERT INTO `table_excel` VALUES ('90', 't_signature', 'supplier_code', 'String', '供应商编码', '3');
INSERT INTO `table_excel` VALUES ('91', 't_signature', 'supplier_name', 'String', '供应商名称', '4');
INSERT INTO `table_excel` VALUES ('92', 't_signature', 'order_code', 'String', '订单号', '5');
INSERT INTO `table_excel` VALUES ('93', 't_signature', 'account_name', 'String', '科目名称', '6');
INSERT INTO `table_excel` VALUES ('94', 't_signature', 'address_code', 'String', '站址编码', '7');
INSERT INTO `table_excel` VALUES ('95', 't_signature', 'address_name', 'String', '站址名称', '8');
INSERT INTO `table_excel` VALUES ('96', 't_signature', 'invoice_amt', 'BigDecimal', '本次发票', '9');
INSERT INTO `table_excel` VALUES ('97', 't_signature', 'invoice_amt_e_tax', 'BigDecimal', '本次发票净额', '10');
INSERT INTO `table_excel` VALUES ('98', 't_signature', 'pay_amt', 'BigDecimal', '本次付款', '11');
INSERT INTO `table_excel` VALUES ('99', 't_signature', 'pay_amt_e_tax', 'BigDecimal', '本次付款净额', '12');
INSERT INTO `table_excel` VALUES ('100', 't_signature', 'remark', 'String', '备注', '13');
INSERT INTO `table_excel` VALUES ('101', 't_signature', 'tax_rate', 'String', '税率', '14');
INSERT INTO `table_excel` VALUES ('102', 't_payment', 'project_code', 'String', '项目编号', '1');
INSERT INTO `table_excel` VALUES ('103', 't_payment', 'supplier_code', 'String', '供应商编码', '2');
INSERT INTO `table_excel` VALUES ('104', 't_payment', 'supplier_name', 'String', '供应商名称', '3');
INSERT INTO `table_excel` VALUES ('105', 't_payment', 'order_code', 'String', '订单号', '4');
INSERT INTO `table_excel` VALUES ('106', 't_payment', 'account_name', 'String', '科目名称', '5');
INSERT INTO `table_excel` VALUES ('107', 't_payment', 'address_code', 'String', '站址编码', '6');
INSERT INTO `table_excel` VALUES ('108', 't_payment', 'address_name', 'String', '站址名称', '7');
INSERT INTO `table_excel` VALUES ('109', 't_payment', 'invoice_amt', 'BigDecimal', '本次发票', '8');
INSERT INTO `table_excel` VALUES ('110', 't_payment', 'invoice_amt_e_tax', 'BigDecimal', '本次发票净额', '9');
INSERT INTO `table_excel` VALUES ('111', 't_payment', 'pay_amt', 'BigDecimal', '本次付款', '10');
INSERT INTO `table_excel` VALUES ('112', 't_payment', 'pay_amt_e_tax', 'BigDecimal', '本次付款净额', '11');
INSERT INTO `table_excel` VALUES ('113', 't_payment', 'month', 'String', '日期', '12');
INSERT INTO `table_excel` VALUES ('114', 't_payment', 'voucher_no', 'String', '凭证号', '13');
INSERT INTO `table_excel` VALUES ('115', 't_payment', 'online_offline', 'String', '线上线下', '14');
INSERT INTO `table_excel` VALUES ('116', 't_payment', 'settle_no', 'String', '结算单号', '15');
INSERT INTO `table_excel` VALUES ('117', 't_payment', 'remark', 'String', '备注', '16');
INSERT INTO `table_excel` VALUES ('118', 't_payment', 'tax_rate', 'String', '税率', '17');
INSERT INTO `table_excel` VALUES ('119', 't_material_adj', 'operate_type', 'String', '操作类型', '1');
INSERT INTO `table_excel` VALUES ('120', 't_material_adj', 'item_explain', 'String', '事项说明', '2');
INSERT INTO `table_excel` VALUES ('121', 't_material_adj', 'amt', 'BigDecimal', '金额', '3');
INSERT INTO `table_excel` VALUES ('122', 't_material_adj', 'voucher_no', 'String', '凭证号', '4');
INSERT INTO `table_excel` VALUES ('123', 't_material_adj', 'system_type', 'String', '系统类别', '5');
INSERT INTO `table_excel` VALUES ('124', 't_godown_entry', 'is_check', 'String', '是否进行明细核对', '6');
INSERT INTO `table_excel` VALUES ('125', 't_godown_entry', 'remark', 'String', '备注', '7');
INSERT INTO `table_excel` VALUES ('126', 't_store_deal', 'is_check', 'String', '是否进行明细核对', '17');
INSERT INTO `table_excel` VALUES ('127', 't_store_deal', 'remark', 'String', '备注', '18');
INSERT INTO `table_excel` VALUES ('128', 't_material_adj', 'month', 'String', '月份', '6');
INSERT INTO `table_excel` VALUES ('129', 't_material_adj', 'detail_adj', 'String', '明细调整', '7');
INSERT INTO `table_excel` VALUES ('130', 't_material_adj', 'doc_code', 'String', '单据编号', '8');

-- ----------------------------
-- Table structure for t_account
-- ----------------------------
DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_code` varchar(10) NOT NULL COMMENT '科目编号',
  `account_name` varchar(50) NOT NULL COMMENT '科目名称',
  `update_by` varchar(20) NOT NULL COMMENT '最后修改人',
  `org_id` varchar(20) NOT NULL COMMENT '所属机构',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18416 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_account
-- ----------------------------

-- ----------------------------
-- Table structure for t_cancel_proj
-- ----------------------------
DROP TABLE IF EXISTS `t_cancel_proj`;
CREATE TABLE `t_cancel_proj` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_code` varchar(30) NOT NULL COMMENT '项目编码',
  `project_name` varchar(50) DEFAULT NULL COMMENT '项目名称',
  `update_by` varchar(20) NOT NULL COMMENT '最后修改人',
  `org_id` varchar(20) NOT NULL COMMENT '所属机构',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `index_project_code` (`org_id`,`project_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=30153 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_cancel_proj
-- ----------------------------

-- ----------------------------
-- Table structure for t_cost
-- ----------------------------
DROP TABLE IF EXISTS `t_cost`;
CREATE TABLE `t_cost` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `list_id` varchar(50) NOT NULL COMMENT '清单ID',
  `busi_date` date NOT NULL COMMENT '业务日期',
  `order_code` varchar(50) DEFAULT NULL COMMENT '采购订单号',
  `source_type` varchar(30) DEFAULT NULL COMMENT '订单来源类型',
  `supplier_name` varchar(100) NOT NULL COMMENT '供应商名称',
  `supplier_code` varchar(30) NOT NULL COMMENT '供应商编号',
  `material_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `manufacturer` varchar(100) DEFAULT NULL COMMENT '生产厂家',
  `spec_code` varchar(100) DEFAULT NULL COMMENT '规格编号',
  `material_name` varchar(100) DEFAULT NULL COMMENT '物料名称',
  `service_code` varchar(30) DEFAULT NULL COMMENT '物资服务编码',
  `unit` varchar(20) DEFAULT NULL COMMENT '计量单位',
  `quantity` decimal(11,2) DEFAULT NULL COMMENT '数量',
  `unit_price_e_tax` decimal(11,2) DEFAULT NULL COMMENT '不含税单价',
  `total_amt_e_tax` decimal(11,2) DEFAULT NULL COMMENT '不含税总金额',
  `total_amt_i_tax` decimal(11,2) DEFAULT NULL COMMENT '含税总价',
  `progress` decimal(5,2) DEFAULT NULL COMMENT '成本确认进度',
  `tax_amt` decimal(11,2) DEFAULT NULL COMMENT '税金',
  `period_cost_amt` decimal(11,2) DEFAULT NULL COMMENT '当期成本确认金额',
  `account_amt` decimal(11,2) DEFAULT NULL COMMENT '入账金额',
  `project_code` varchar(30) NOT NULL COMMENT '项目编码',
  `project_name` varchar(100) DEFAULT NULL COMMENT '项目名称',
  `address_code` varchar(30) DEFAULT NULL COMMENT '站址编码',
  `address_name` varchar(100) DEFAULT NULL COMMENT '站址名称',
  `accounting_org` varchar(50) DEFAULT NULL COMMENT '核算组织',
  `doc_type` varchar(20) DEFAULT NULL COMMENT '单据类型',
  `documentary` varchar(20) DEFAULT NULL COMMENT '制单人',
  `assemble_sts` varchar(10) DEFAULT NULL COMMENT '是否装配',
  `allocation_sts` varchar(10) DEFAULT NULL COMMENT '是否分摊',
  `asset_trans_sts` varchar(10) DEFAULT NULL COMMENT '是否转资',
  `timestamp` date DEFAULT NULL COMMENT '时间戳',
  `account_code` varchar(10) NOT NULL COMMENT '科目编码',
  `account_name` varchar(50) NOT NULL COMMENT '科目名称',
  `order_type` varchar(50) DEFAULT NULL COMMENT '订单类型',
  `voucher_no` varchar(20) DEFAULT NULL COMMENT '凭证编号',
  `detail_no` varchar(250) DEFAULT NULL COMMENT '交易明细行号',
  `update_by` varchar(20) NOT NULL COMMENT '最后修改人',
  `org_id` varchar(20) NOT NULL COMMENT '所属机构',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `uuid` varchar(255) DEFAULT NULL COMMENT 'uuid',
  PRIMARY KEY (`id`),
  KEY `index_project_supplier` (`org_id`,`project_code`,`supplier_name`,`order_type`,`order_code`,`account_amt`) USING BTREE,
  KEY `index_project` (`org_id`,`project_code`,`busi_date`,`account_amt`) USING BTREE,
  KEY `index_type_code_date` (`org_id`,`order_type`,`order_code`,`busi_date`) USING BTREE,
  KEY `index_pms` (`org_id`,`project_code`,`assemble_sts`,`asset_trans_sts`,`doc_type`,`busi_date`,`account_amt`) USING BTREE,
  KEY `index` (`org_id`,`order_code`,`account_name`,`account_amt`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=5151085 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_cost
-- ----------------------------

-- ----------------------------
-- Table structure for t_crm
-- ----------------------------
DROP TABLE IF EXISTS `t_crm`;
CREATE TABLE `t_crm` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rent_status` varchar(20) NOT NULL COMMENT '起租状态',
  `order_code` varchar(50) NOT NULL COMMENT '需求订单号',
  `update_by` varchar(20) NOT NULL COMMENT '最后修改人',
  `org_id` varchar(20) NOT NULL COMMENT '所属机构',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74221 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_crm
-- ----------------------------

-- ----------------------------
-- Table structure for t_file
-- ----------------------------
DROP TABLE IF EXISTS `t_file`;
CREATE TABLE `t_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) DEFAULT NULL,
  `file_path` varchar(500) DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  `user_id` varchar(50) DEFAULT NULL,
  `org_id` varchar(50) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_file
-- ----------------------------

-- ----------------------------
-- Table structure for t_godown_entry
-- ----------------------------
DROP TABLE IF EXISTS `t_godown_entry`;
CREATE TABLE `t_godown_entry` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `entry_date` varchar(10) NOT NULL COMMENT '日期',
  `voucher_no` varchar(20) DEFAULT NULL COMMENT '凭证编号',
  `entry_code` varchar(50) DEFAULT NULL COMMENT '入库订单号',
  `order_code` varchar(50) DEFAULT NULL COMMENT '采购订单号',
  `amount` decimal(11,2) DEFAULT NULL COMMENT '金额',
  `is_check` varchar(20) DEFAULT NULL COMMENT '是否进行明细核对',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `update_by` varchar(20) NOT NULL COMMENT '最后修改人',
  `org_id` varchar(20) NOT NULL COMMENT '所属机构',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `index_1` (`entry_code`,`org_id`,`is_check`,`entry_date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28730 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_godown_entry
-- ----------------------------

-- ----------------------------
-- Table structure for t_log
-- ----------------------------
DROP TABLE IF EXISTS `t_log`;
CREATE TABLE `t_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `log_type` char(1) DEFAULT NULL COMMENT '1：导入，2：同步',
  `table_name` varchar(20) DEFAULT NULL COMMENT '导入表名',
  `file_name` varchar(50) DEFAULT NULL COMMENT '导入文件名',
  `user_id` varchar(20) DEFAULT NULL COMMENT '用户',
  `log_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `org_id` varchar(20) DEFAULT NULL COMMENT '所属机构',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=385 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_material_adj
-- ----------------------------
DROP TABLE IF EXISTS `t_material_adj`;
CREATE TABLE `t_material_adj` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operate_type` varchar(50) DEFAULT NULL COMMENT '操作类型',
  `item_explain` varchar(500) DEFAULT NULL COMMENT '事项说明',
  `amt` decimal(11,2) DEFAULT NULL COMMENT '金额',
  `voucher_no` varchar(20) DEFAULT NULL COMMENT '凭证号',
  `system_type` varchar(50) DEFAULT NULL COMMENT '入库系统类别',
  `month` varchar(8) DEFAULT NULL COMMENT '日期',
  `detail_adj` varchar(10) DEFAULT NULL COMMENT '明细调整，是或者否',
  `doc_code` varchar(200) DEFAULT NULL COMMENT '单据编号',
  `update_by` varchar(20) DEFAULT NULL COMMENT '最后修改人',
  `org_id` varchar(20) NOT NULL COMMENT '所属机构',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `index_1` (`org_id`,`system_type`,`operate_type`,`detail_adj`,`month`,`doc_code`,`amt`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=105589 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_material_adj
-- ----------------------------

-- ----------------------------
-- Table structure for t_notice
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `notice_title` varchar(255) DEFAULT NULL,
  `notice_content` varchar(1000) DEFAULT NULL,
  `notice_time` varchar(20) DEFAULT NULL,
  `org_id` varchar(20) DEFAULT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_notice
-- ----------------------------

-- ----------------------------
-- Table structure for t_old_proj
-- ----------------------------
DROP TABLE IF EXISTS `t_old_proj`;
CREATE TABLE `t_old_proj` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_code` varchar(30) NOT NULL COMMENT '项目编码',
  `project_name` varchar(50) DEFAULT NULL COMMENT '项目名称',
  `update_by` varchar(20) DEFAULT NULL COMMENT '最后修改人',
  `org_id` varchar(20) NOT NULL COMMENT '所属机构',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16300 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_old_proj
-- ----------------------------

-- ----------------------------
-- Table structure for t_over_view
-- ----------------------------
DROP TABLE IF EXISTS `t_over_view`;
CREATE TABLE `t_over_view` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_code` varchar(50) DEFAULT NULL COMMENT '项目编号',
  `supplier_name` varchar(100) DEFAULT NULL COMMENT '供应商名称',
  `account_name` varchar(50) DEFAULT NULL COMMENT '科目名称',
  `month` varchar(10) DEFAULT NULL COMMENT '月份',
  `account_amt` decimal(11,2) DEFAULT NULL COMMENT '入账金额',
  `t_pay_amt` decimal(11,2) DEFAULT NULL COMMENT '已付款不含税',
  `s_pay_amt` decimal(11,2) DEFAULT NULL COMMENT '签字未付款不含税',
  `t_invoice_amt` decimal(11,2) DEFAULT NULL COMMENT '已开发票',
  `t_invoice_amt_e_tax` decimal(11,2) DEFAULT NULL COMMENT '已开发票不含税',
  `table_name` varchar(20) DEFAULT NULL COMMENT '当前数据属于哪个表',
  `org_id` varchar(20) DEFAULT NULL COMMENT '机构编号',
  `uuid` varchar(225) DEFAULT NULL COMMENT 'uuid',
  `flag` int(2) DEFAULT '0' COMMENT '用于t_payment表，0：非退库红冲，1：退库红冲',
  `order_code` varchar(50) DEFAULT NULL,
  `order_type` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_1` (`org_id`,`project_code`,`supplier_name`,`account_name`,`account_amt`,`t_pay_amt`,`s_pay_amt`,`t_invoice_amt`,`t_invoice_amt_e_tax`) USING BTREE,
  KEY `index_2` (`org_id`,`project_code`,`supplier_name`,`account_name`,`flag`,`month`,`order_code`,`order_type`,`account_amt`,`t_pay_amt`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=5033036 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_over_view
-- ----------------------------

-- ----------------------------
-- Table structure for t_payment
-- ----------------------------
DROP TABLE IF EXISTS `t_payment`;
CREATE TABLE `t_payment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_code` varchar(30) DEFAULT NULL COMMENT '项目编码',
  `supplier_code` varchar(30) DEFAULT NULL COMMENT '供应商编号',
  `supplier_name` varchar(100) DEFAULT NULL COMMENT '供应商名称',
  `order_code` varchar(50) DEFAULT NULL COMMENT '订单编号',
  `account_name` varchar(50) NOT NULL COMMENT '科目名称',
  `address_code` varchar(30) DEFAULT NULL COMMENT '站址编码',
  `address_name` varchar(100) DEFAULT NULL COMMENT '站址名称',
  `invoice_amt` decimal(11,2) DEFAULT NULL COMMENT '本次发票',
  `invoice_amt_e_tax` decimal(11,2) DEFAULT NULL COMMENT '本次发票净额',
  `pay_amt` decimal(11,2) DEFAULT NULL COMMENT '本次付款',
  `pay_amt_e_tax` decimal(11,2) DEFAULT NULL COMMENT '本次付款净额',
  `month` varchar(6) DEFAULT NULL COMMENT '日期',
  `voucher_no` varchar(40) DEFAULT NULL COMMENT '凭证编号',
  `online_offline` varchar(10) DEFAULT NULL COMMENT '线上线下',
  `settle_no` varchar(50) DEFAULT NULL COMMENT '结算单号',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `tax_rate` decimal(7,4) DEFAULT NULL COMMENT '税率',
  `accounting_org` varchar(50) DEFAULT NULL COMMENT '核算组织',
  `update_by` varchar(20) DEFAULT NULL COMMENT '最后修改人',
  `org_id` varchar(20) NOT NULL COMMENT '所属机构',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `uuid` varchar(255) DEFAULT NULL,
  `flag` int(2) DEFAULT '0' COMMENT '0:非退库红冲，1：退库红冲',
  PRIMARY KEY (`id`),
  KEY `index_project_supplier_account` (`org_id`,`project_code`,`supplier_name`,`pay_amt_e_tax`) USING BTREE,
  KEY `index_supplier_project_pay` (`org_id`,`project_code`,`supplier_name`,`account_name`,`flag`,`pay_amt_e_tax`) USING BTREE,
  KEY `index_order` (`org_id`,`order_code`,`invoice_amt_e_tax`,`pay_amt_e_tax`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=2596255 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_payment
-- ----------------------------

-- ----------------------------
-- Table structure for t_pms
-- ----------------------------
DROP TABLE IF EXISTS `t_pms`;
CREATE TABLE `t_pms` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_code` varchar(30) NOT NULL COMMENT '项目编码',
  `project_name` varchar(100) NOT NULL COMMENT '项目名称',
  `build_mode` varchar(20) DEFAULT NULL COMMENT '建设方式',
  `accept_date` varchar(20) DEFAULT NULL COMMENT '内验时间',
  `deliver_date` varchar(20) DEFAULT NULL COMMENT '交付时间',
  `project_status` varchar(20) DEFAULT NULL COMMENT '项目状态',
  `order_code` varchar(50) DEFAULT NULL COMMENT '需求订单号',
  `update_by` varchar(20) DEFAULT NULL COMMENT '最后修改人',
  `org_id` varchar(20) NOT NULL COMMENT '所属机构',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `index_1` (`org_id`,`project_code`,`build_mode`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=121999 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_pms
-- ----------------------------

-- ----------------------------
-- Table structure for t_signature
-- ----------------------------
DROP TABLE IF EXISTS `t_signature`;
CREATE TABLE `t_signature` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `batch_no` varchar(100) DEFAULT NULL COMMENT '审签批次号',
  `project_code` varchar(30) DEFAULT NULL COMMENT '项目编码',
  `supplier_code` varchar(30) DEFAULT NULL COMMENT '供应商编号',
  `supplier_name` varchar(100) DEFAULT NULL COMMENT '供应商名称',
  `order_code` varchar(50) DEFAULT NULL COMMENT '订单编号',
  `account_name` varchar(50) NOT NULL COMMENT '科目名称',
  `address_code` varchar(30) DEFAULT NULL COMMENT '站址编码',
  `address_name` varchar(100) DEFAULT NULL COMMENT '站址名称',
  `invoice_amt` decimal(11,2) DEFAULT NULL COMMENT '本次发票',
  `invoice_amt_e_tax` decimal(11,2) DEFAULT NULL COMMENT '本次发票净额',
  `pay_amt` decimal(11,2) DEFAULT NULL COMMENT '本次付款',
  `pay_amt_e_tax` decimal(11,2) DEFAULT NULL COMMENT '本次付款净额',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `tax_rate` decimal(7,4) DEFAULT NULL COMMENT '税率',
  `accounting_org` varchar(50) DEFAULT NULL COMMENT '核算组织',
  `update_by` varchar(20) DEFAULT NULL COMMENT '最后修改人',
  `org_id` varchar(20) NOT NULL COMMENT '所属机构',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `uuid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_project_supplier_account` (`org_id`,`project_code`,`supplier_name`,`account_name`,`pay_amt_e_tax`,`invoice_amt`,`invoice_amt_e_tax`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14112 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_signature
-- ----------------------------

-- ----------------------------
-- Table structure for t_store_deal
-- ----------------------------
DROP TABLE IF EXISTS `t_store_deal`;
CREATE TABLE `t_store_deal` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_code` varchar(30) NOT NULL COMMENT '项目编号',
  `project_name` varchar(100) NOT NULL COMMENT '项目名称',
  `order_code` varchar(50) DEFAULT NULL COMMENT '采购订单号',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `material_code` varchar(50) DEFAULT NULL COMMENT '物料编码',
  `material_name` varchar(1000) DEFAULT NULL COMMENT '物料名称',
  `busi_type` varchar(50) DEFAULT NULL COMMENT '业务类型',
  `opration_type` varchar(20) DEFAULT NULL COMMENT '操作类型',
  `supplier_code` varchar(30) DEFAULT NULL COMMENT '供应商编号',
  `supplier_name` varchar(100) DEFAULT NULL COMMENT '供应商名称',
  `quantity_org` int(9) DEFAULT NULL COMMENT '交易前数量',
  `quantity_txn` int(9) DEFAULT NULL COMMENT '交易数量',
  `quantity_left` int(9) DEFAULT NULL COMMENT '交易后数量',
  `total_amt_e_tax` decimal(11,2) DEFAULT NULL COMMENT '总金额（产品净额）',
  `total_amt_i_tax` decimal(11,2) DEFAULT NULL COMMENT '含税总金额（产品净额+税额）',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_check` varchar(20) DEFAULT NULL COMMENT '是否进行明细核对',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `update_by` varchar(20) DEFAULT NULL COMMENT '最后修改人',
  `org_id` varchar(20) NOT NULL COMMENT '所属机构',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `index_order_operate` (`org_id`,`order_code`,`create_time`,`opration_type`,`is_check`) USING BTREE,
  KEY `index_batch_operate` (`org_id`,`batch_no`,`create_time`,`opration_type`,`is_check`)
) ENGINE=InnoDB AUTO_INCREMENT=282744 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_store_deal
-- ----------------------------

-- ----------------------------
-- Table structure for t_supplier
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier`;
CREATE TABLE `t_supplier` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `supplier_code` varchar(30) NOT NULL COMMENT '供应商编号',
  `supplier_name` varchar(100) NOT NULL COMMENT '供应商名称',
  `unique_name` varchar(100) DEFAULT NULL COMMENT '唯一名称',
  `update_by` varchar(20) NOT NULL COMMENT '最后修改人',
  `org_id` varchar(20) NOT NULL COMMENT '所属机构',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `index_1` (`org_id`,`supplier_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25417 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_supplier
-- ----------------------------

-- ----------------------------
-- Table structure for t_supplier_adj
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier_adj`;
CREATE TABLE `t_supplier_adj` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `s_supplier_code` varchar(30) NOT NULL COMMENT '待调整供应商代码',
  `s_supplier_name` varchar(100) NOT NULL COMMENT '待调整供应商名称',
  `t_supplier_code` varchar(30) NOT NULL COMMENT '目标供应商代码',
  `t_supplier_name` varchar(100) NOT NULL COMMENT '目标供应商名称',
  `update_by` varchar(20) DEFAULT NULL COMMENT '最后修改人',
  `org_id` varchar(20) NOT NULL COMMENT '所属机构',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_supplier_adj
-- ----------------------------

-- ----------------------------
-- Table structure for t_tax_rate
-- ----------------------------
DROP TABLE IF EXISTS `t_tax_rate`;
CREATE TABLE `t_tax_rate` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tax_rate` decimal(7,4) NOT NULL COMMENT '税率',
  `scope` varchar(100) DEFAULT NULL COMMENT '应用范围',
  `update_by` varchar(20) DEFAULT NULL COMMENT '最后修改人',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_tax_rate
-- ----------------------------

-- ----------------------------
-- Table structure for user_custom
-- ----------------------------
DROP TABLE IF EXISTS `user_custom`;
CREATE TABLE `user_custom` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(20) DEFAULT NULL COMMENT '用户ID',
  `custom_id` varchar(6) DEFAULT NULL COMMENT '自定义首页id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_custom
-- ----------------------------

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(40) NOT NULL COMMENT '用户ID',
  `password` varchar(50) DEFAULT NULL COMMENT '用户密码',
  `user_name` varchar(50) NOT NULL COMMENT '用户名称',
  `sex` varchar(1) DEFAULT NULL COMMENT '性别',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱地址',
  `org_id` varchar(20) DEFAULT NULL COMMENT '部门ID',
  `flag` varchar(2) DEFAULT '1' COMMENT '1：手动创建的用户，0：系统生成的管理员用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_info
-- ----------------------------
