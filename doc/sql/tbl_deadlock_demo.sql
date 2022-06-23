DROP TABLE IF EXISTS `tbl_deadlock_demo`;
CREATE TABLE `tbl_deadlock_demo`
(
    `ID`        bigint(20) NOT NULL,
    `VERSION`   bigint(20) NULL DEFAULT NULL,
    `DEMO_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `DEMO_KEY`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL,
    PRIMARY KEY (`ID`) USING BTREE,
    UNIQUE INDEX `DEMO_UN`(`DEMO_NAME`, `DEMO_KEY`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


