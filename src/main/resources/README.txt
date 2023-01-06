1.doc/batch-db.sql
springbatch自带的依赖脚本

2.单测测试用例目录
src\main\resources\testData
src\test\resources\testDataBak

3.打包
clean package spring-boot:repackage -Dmaven.test.skip=true

4.服务器上简单启动
java -Xms512M -Xmx512M -Xmn256M -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -Xss256k -jar /home/kunghsu/springboot/kunsharedemo-0.0.1.war

5.发布到私服
clean deploy -Dmaven.test.skip=true

6.springcloud体系规划
注册中心：KunShare-EurekaServer
网关：KunShare-Zuul

7.本地启动windows redis
 .\redis-server.exe .\redis.windows.conf

8.集成xxl-job
admin调度中心用8061端口
http://localhost:8061/xxl-job-admin

9.整合配置中心
http://localhost:8097/kunshare-config-server/kunsharedemo/dev/master

10.引入flink
mvn clean compile package -Dmaven.test.skip=true -Dspring-boot.repackage.skip=true
java -jar .\kunsharedemo-1.0.2-jar-with-dependencies.jar flinkClassName=cn.com.kun.apache.flink.demo1.LocalRunningDataSourceTestJob
flinkClassName=cn.com.kun.apache.flink.demo1.RemoteServerRunTestJob
java -jar .\kunsharedemo-1.0.2-jar-with-dependencies.jar flinkClassName=cn.com.kun.apache.flink.demo1.MyFlinkDemoJob

用springboot打包：
mvn clean compile package -Dmaven.test.skip=true

11.引入sentinel
java -Dserver.port=8070 -Dcsp.sentinel.dashboard.server=localhost:8070 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar

12.引入Hadoop + Hive
进入hadoop的sbin目录
.\start-all.cmd
进入hive的bin目录
.\hive --service metastore
.\hive --service hiveserver2
(为了简化项目结构，单独起一个工程放flink相关demo)

13.
2.本地备忘
windows操作(2.7.0版)
先启动zk
.\zookeeper-server-start.bat ../../config/zookeeper.properties
启动kafka
./kafka-server-start.bat ..\..\config\server.properties
启动kakfa manager
D:\km2.0.0.2\bin

kafka查看消费组未消费数据情况0.9之后版本
bin/kafka-topics.sh --zookeeper 127.0.0.1:2181 --list //查看topic
bin/kafka-consumer-groups.sh --new-consumer --bootstrap-server localhost:9092 --list //查看消费组
bin/kafka-consumer-groups.sh --new-consumer --bootstrap-server localhost:9092 --describe --group daemon //查看消费情况

查看所有消费者组
./kafka-consumer-groups.bat --bootstrap-server localhost:9092 --list

查看某个消费者组的消费情况
./kafka-consumer-groups.bat --bootstrap-server localhost:9092 --describe --group kunsharedemo
./kafka-consumer-groups.bat --bootstrap-server localhost:9092 --describe --group kunwebdemo


Group : 消费者组
Topic : topic的名字
Pid : partition的ID
Offset : kafka消费者在对应分区上已经消费的消息数【位置】
logSize : 已经写到该分区的消息数【位置】
Lag : 还有多少消息未读取（Lag = logSize - Offset）
Owner : 分区创建在哪个broker

#创建topic
./kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test0804-1
./kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 3 --partitions 4 --topic test0804-2

#查看分区数、副本数
./kafka-topics.bat --zookeeper localhost:2181 --describe --topic test0804-1