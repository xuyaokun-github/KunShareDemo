CREATE TABLE `tbl_database_lock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `resource` varchar(500) NOT NULL COMMENT '锁定的资源，可以是方法名或者业务唯一标志',
  `description` varchar(1000) NOT NULL DEFAULT '' COMMENT '业务场景描述',

  `request_id` varchar(64) NULL COMMENT '唯一标识',
  `request_time` datetime NULL COMMENT '抢锁时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uiq_idx_resource` (`resource`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='数据库分布式悲观锁表';

INSERT INTO `test`.`tbl_database_lock` (`id`, `resource`, `description`, `request_id`, `request_time`) VALUES ('1', 'cn.com.kun.service.distributedlock.dblock.DBLockDemoService.test', '', '', NULL);
