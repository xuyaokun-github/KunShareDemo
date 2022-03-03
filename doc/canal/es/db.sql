drop table if EXISTS tbl_canal_test;
CREATE TABLE `tbl_canal_test`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `name`        varchar(50)  DEFAULT NULL COMMENT '姓名',
    `sex`         varchar(50)  DEFAULT NULL COMMENT '性别',
    `age`         varchar(255) DEFAULT NULL COMMENT '年龄',
    `email`       varchar(255) DEFAULT NULL COMMENT '邮箱',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

