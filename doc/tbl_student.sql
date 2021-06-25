CREATE TABLE `tbl_student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_card` varchar(50) DEFAULT NULL COMMENT '身份证号',
  `student_name` varchar(50) DEFAULT NULL COMMENT '学生姓名',
  `address` varchar(255) DEFAULT NULL COMMENT '住址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `test`.`tbl_student`(`id`, `id_card`, `student_name`, `address`, `create_time`) VALUES (4, '440923', 'tmac', 'shenzhenshi', '2021-06-25 14:55:31');

