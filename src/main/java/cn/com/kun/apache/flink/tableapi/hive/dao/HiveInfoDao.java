package cn.com.kun.apache.flink.tableapi.hive.dao;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.catalog.hive.HiveCatalog;

/**
 * hive操作层
 * author:xuyaokun_kzx
 * date:2021/12/21
 * desc:
*/
public class HiveInfoDao {

    private static TableEnvironment tableEnv;
    private static final String DEFAULT_DATABASE = "default";

    public static void main(String[] args) {

        String catalogName     = "myhive";
        String hiveConfDir     = "/etc/hive/conf.cloudera.hive/";
        String version         = "1.2.1";
        String database = "test_myq";
        HiveInfoDao.init(catalogName, hiveConfDir, version, database);
    }

    /**
     * 这里有个坑，注意JM和TM执行代码并不是同一个JVM,因此这里需要注意空指针问题
     * 在使用tableEnv前要判断下是否为空
     *
     * @param catalogName
     * @param hiveConfDir
     * @param version
     * @param database
     */
    public static void init(String catalogName, String hiveConfDir, String version, String database){

        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        tableEnv = TableEnvironment.create(environmentSettings);

        HiveCatalog hive = new HiveCatalog(catalogName, DEFAULT_DATABASE, hiveConfDir, version);
        //java.io.IOException: No FileSystem for scheme: hdfs
//        hive.getHiveConf().set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

        tableEnv.registerCatalog(catalogName, hive);
        tableEnv.useCatalog(catalogName);
        tableEnv.useDatabase(database);

    }

    /**
     * 查询sql
     * 返回table实例，由上层进行解析或进一步使用
     * @param sql
     * @return
     */
    public static Table query(String sql){

        Table mytable = tableEnv.sqlQuery(sql);
        return mytable;
    }


}
