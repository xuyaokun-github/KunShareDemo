package cn.com.kun.service.mybatis;

import org.apache.ibatis.jdbc.SQL;

public class SqlBuilderService {

    public static void main(String[] args) {

        new SQL().SELECT("aaa", "bbb")
                .FROM("tableName")
                .WHERE("");

    }
}
