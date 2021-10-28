CREATE TABLE `tbl_memory_cache_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `config_name` varchar(255) DEFAULT NULL COMMENT '缓存管理器名称',
  `biz_key` varchar(255) DEFAULT NULL COMMENT '缓存项的key',
  `update_timemillis` varchar(50) DEFAULT NULL COMMENT '更新时间戳',
  `cluster_name` varchar(255) DEFAULT NULL COMMENT '集群名（机房名）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;