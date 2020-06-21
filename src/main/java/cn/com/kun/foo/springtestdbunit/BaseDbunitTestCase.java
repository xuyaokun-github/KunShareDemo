package cn.com.kun.foo.springtestdbunit;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.util.ResourceUtils;
import org.xml.sax.InputSource;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by xuyaokun On 2020/6/18 22:36
 * @desc: 
 */
public class BaseDbunitTestCase {

    //org.dbunit.database.DatabaseConnection
    //这个不是真正的数据库的连接的  封装
    private DatabaseConnection conn;

    //这个就是临时文件
    private File tempFile;
    FileOutputStream fileOutputStream;

    private IDataSet testDataDataSet;

    public BaseDbunitTestCase(String testDataFileName) throws DatabaseUnitException {

        //使用基于xml格式的IDataSet
//        dataSetTestData = new FlatXmlDataSet(new FlatXmlProducer(
//                new InputSource(AbstractDbunitTestCase.class.getClassLoader()
//                        .getResourceAsStream(testDataName))));

        File file = null;
        try {
            file = ResourceUtils.getFile(testDataFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(file.getAbsolutePath());
        //使用基于XSL文件的IDataSet
        try {
            testDataDataSet = new XlsDataSet(new File(file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initDbunitTestCase(Connection conn1, String tableName) throws SQLException, DatabaseUnitException, IOException{

        //1.设置一个数据库连接
        setConn(conn1);
        //2.指定表名，进行备份
        backOneTable(tableName);
        //3.插入测试数据
        insertTestData();
    }


    /**
     * 这个方法的作用就是初始化上面的DatabaseConnection
     * @param conn1
     */
    public void setConn(Connection conn1) throws DatabaseUnitException {
        conn = new DatabaseConnection(conn1);
    }

    /**
     * 备份多个表
     * @param tabNames
     */
    public void backManyTable(String... tabNames) throws DataSetException, IOException {

        QueryDataSet dataSet = new QueryDataSet(conn);
        //第二步：设置要备份的这个表
        for (String tabName : tabNames) {
            dataSet.addTable(tabName);
        }
        //创建备份文件
        //默认会创建在系统的temp目录下
        tempFile = File.createTempFile("DbunitTestCase-back-",".xml");
        System.out.println("备份表文件所在路径：" + tempFile.getAbsolutePath());
        fileOutputStream = new FileOutputStream(tempFile);
        //备份，用QueryDataSet进行读取然后用FlatXmlDataSet进行写入
        FlatXmlDataSet.write(dataSet, fileOutputStream);
    }

    /**
     * 备份一张表
     * @param tabName
     */
    public void backOneTable(String tabName) throws IOException, DataSetException {
        backManyTable(tabName);
    }


    /**
     * 插入测试数据,会真正插到库中
     */
    public void insertTestData() throws DatabaseUnitException, SQLException {
        //testDataDataSet是上面创建的基于excel文件的数据集
        /*
            原理就是将数据集然后通过conn这个数据库连接借助JDBC写入到数据库
            CLEAN_INSERT其实包含两个数据库操作，一是清理，二是插入
            插入操作用的是：org.dbunit.operation.AbstractBatchOperation.execute
            更多请参考我的源码分析文章
         */
        DatabaseOperation.CLEAN_INSERT.execute(conn, testDataDataSet);
    }


    /**
     * 还原表的数据
     */
    public void resumeTable() throws DatabaseUnitException, SQLException, FileNotFoundException {
        //在备份的时候，数据库表数据全量被存进了临时文件中
        IDataSet dataSet = new FlatXmlDataSet(new FlatXmlProducer(
                new InputSource(new FileInputStream(tempFile))));
        //执行清理并插入
        DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
        //删除临时文件
        try {
            fileOutputStream.close();
            tempFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
