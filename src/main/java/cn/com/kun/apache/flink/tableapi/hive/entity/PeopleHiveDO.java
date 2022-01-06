package cn.com.kun.apache.flink.tableapi.hive.entity;

/**

 CREATE TABLE `people`(
 `id` int,
 `name` string,
 `destination` string)
 COMMENT 'This is a test table'
 ROW FORMAT SERDE
 'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'
 WITH SERDEPROPERTIES (
 'field.delim'='\t',
 'line.delim'='\n',
 'serialization.format'='\t')
 STORED AS INPUTFORMAT
 'org.apache.hadoop.mapred.TextInputFormat'
 OUTPUTFORMAT
 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
 LOCATION
 'hdfs://localhost:9000/user/hive/warehouse/test.db/people'
 TBLPROPERTIES (
 'transient_lastDdlTime'='1641439332')


 * author:xuyaokun_kzx
 * date:2022/1/6
 * desc:
*/
public class PeopleHiveDO {

    /**
     */
    private Integer id;

    /**
     */
    private String name;

    /**
     *
     */
    private String destination;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "PeopleHiveDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
