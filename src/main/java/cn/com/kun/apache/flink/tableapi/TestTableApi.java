package cn.com.kun.apache.flink.tableapi;

public class TestTableApi {

    public static void main(String[] args) {

//        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
//        BatchTableEnvironment tableEnv = BatchTableEnvironment.create(env);
//
//        String catalogName     = "myhive";
//        String defaultDatabase = "default";
//        String hiveConfDir     = "/etc/hive/conf.cloudera.hive/";
//        String version         = "1.2.1";
//
//        HiveCatalog hive = new HiveCatalog(catalogName, defaultDatabase, hiveConfDir, version);
//        tableEnv.registerCatalog(catalogName, hive);
//
//        try {
//            tableEnv.useCatalog(catalogName);
//            tableEnv.useDatabase("test_myq");
//
//            tableEnv.sqlUpdate("insert into mytable values ('Mao', 6)");
//            tableEnv.execute("insert into mytable");
//
//            Table mytable = tableEnv.sqlQuery("select * from mytable");
//            //将table转换成DataSet
//            // convert the Table into a DataSet of Tuple2<String, Integer> via a TypeInformation
//            TupleTypeInfo<Tuple2<String, Integer>> tupleType = new TupleTypeInfo<>(
//                    Types.STRING,
//                    Types.INT);
//            DataSet<Tuple2<String, Integer>> dsTuple = tableEnv.toDataSet(mytable, tupleType);
//
//            dsTuple.print();
//            env.execute("TestFlinkHive");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}

