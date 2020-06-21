package cn.com.kun.base;

import cn.com.kun.KunShareDemoApplicationTest;
import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.*;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;

/**
 * 基于XML的用法
 *
 * Created by xuyaokun On 2020/6/21 16:58
 * @desc:
 */
@RunWith(SpringRunner.class)
//@SpringBootTest必须指定classes = KunShareDemoApplicationTest.class，否则会报错java.lang.IllegalStateException: Found multiple @SpringBootConfiguration
@SpringBootTest(classes = KunShareDemoApplicationTest.class)
//@Rollback可以不用加，默认就是会回滚，假如不希望回滚，则用@Rollback(value = false)
//@Rollback
//@Transactional注解也不用加，集成AbstractTransactionalJUnit4SpringContextTests就有了
public class BaseXmlDBUnit extends AbstractTransactionalJUnit4SpringContextTests {

    protected static final Logger logger = LoggerFactory.getLogger(BaseXmlDBUnit.class);

    @Autowired
    private DataSource dataSource;

    private IDatabaseConnection conn;

    //读取文件的根目录
    public static final String ROOT_URL = System.getProperty("user.dir") + "/src/test/resources/";

    /**
     * 这里是基类的@Before
     * 执行顺序是先执行父类的@Before，再执行子类的@Before
     * @throws Exception
     */
    @Before
    public void setup() throws Exception {

        //创建一个数据库连接
        conn = new DatabaseConnection(DataSourceUtils.getConnection(dataSource));
        //指定数据库配置是Mysql
        DatabaseConfig dbConfig = conn.getConfig();
        dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
    }

    protected void setUp(String fileName){
        try {
            IDataSet dataSet = getXmlDataSet(fileName);
            //插入模拟数据
            //这里手动调用DatabaseOperation，其实用@DatabaseSetup注解就是指定要执行哪些DatabaseOperation
            DatabaseOperation.CLEAN_INSERT.execute(getConnection(), dataSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return
     * @Title: getConnection
     */
    protected IDatabaseConnection getConnection() {
        return conn;
    }

    /**
     * 创建基于xml文件的数据集
     *
     * @param name
     * @return
     * @throws DataSetException
     * @throws IOException
     * @Title: getXmlDataSet
     */
    protected IDataSet getXmlDataSet(String name) throws DataSetException, IOException {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        return builder.build(new FileInputStream(new File(ROOT_URL + name)));
    }


}
