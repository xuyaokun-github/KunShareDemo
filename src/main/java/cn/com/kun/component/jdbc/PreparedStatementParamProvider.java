package cn.com.kun.component.jdbc;

import java.sql.PreparedStatement;

public interface PreparedStatementParamProvider {

    void initPreparedStatementParam(PreparedStatement ps);
}
