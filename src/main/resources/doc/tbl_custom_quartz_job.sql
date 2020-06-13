/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50611
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50611
File Encoding         : 65001

Date: 2020-06-13 17:44:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_custom_quartz_job
-- ----------------------------
DROP TABLE IF EXISTS `tbl_custom_quartz_job`;
CREATE TABLE `tbl_custom_quartz_job` (
  `job_id` bigint(13) NOT NULL AUTO_INCREMENT,
  `job_class` varchar(200) DEFAULT NULL,
  `job_name` varchar(50) DEFAULT NULL,
  `group_name` varchar(50) DEFAULT NULL,
  `job_param` varchar(500) DEFAULT NULL,
  `trigger_name` varchar(50) DEFAULT NULL,
  `trigger_group_name` varchar(50) DEFAULT NULL,
  `trigger_param` varchar(500) DEFAULT NULL,
  `cron` varchar(50) DEFAULT NULL,
  `enabled` varchar(2) DEFAULT 'Y',
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tbl_custom_quartz_job
-- ----------------------------
INSERT INTO `tbl_custom_quartz_job` VALUES ('1', 'cn.com.kun.quartz.job.MyFirstCustomQuartzJob', 'MyFirstCustomQuartzJob', 'MyFirstCustomQuartzGroup', '{\'name\':\'kunghsu\'}', 'MyFirstCustomQuartzTrigger', 'MyFirstCustomQuartzTriggerGroup', '{\'name\':\'kunghsu\'}', '0/2 * * * * ? 2020', 'Y');
