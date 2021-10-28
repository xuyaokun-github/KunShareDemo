CREATE TABLE `tbl_pessimistic_lock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `resource` varchar(500) NOT NULL COMMENT '锁定的资源，可以是方法名或者业务唯一标志',
  `description` varchar(1000) NOT NULL DEFAULT '' COMMENT '业务场景描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uiq_idx_resource` (`resource`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='数据库分布式悲观锁表';

INSERT INTO `test`.`tbl_memory_cache_notice`(`id`, `config_name`, `biz_key`, `update_timemillis`, `cluster_name`) VALUES (2, 'memorycache-student-service', '10', '1625732916235', 'c2');
INSERT INTO `test`.`tbl_memory_cache_notice`(`id`, `config_name`, `biz_key`, `update_timemillis`, `cluster_name`) VALUES (4, 'memorycache-student-service', '10', '1625733744394', 'c2');
INSERT INTO `test`.`tbl_memory_cache_notice`(`id`, `config_name`, `biz_key`, `update_timemillis`, `cluster_name`) VALUES (6, 'memorycache-student-service', '10', '1625733852259', 'c2');
INSERT INTO `test`.`tbl_memory_cache_notice`(`id`, `config_name`, `biz_key`, `update_timemillis`, `cluster_name`) VALUES (8, 'memorycache-student-service', '10', '1625733974522', 'c2');
INSERT INTO `test`.`tbl_memory_cache_notice`(`id`, `config_name`, `biz_key`, `update_timemillis`, `cluster_name`) VALUES (10, 'memorycache-student-service', '10', '1625734176993', 'c2');
