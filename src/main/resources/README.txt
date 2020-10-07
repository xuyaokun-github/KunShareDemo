1.doc/batch-db.sql
springbatch自带的依赖脚本

2.单测测试用例目录
src\main\resources\testData
src\test\resources\testDataBak

3.打包
package spring-boot:repackage -Dmaven.test.skip=true

4.服务器上简单启动
java -Xms512M -Xmx512M -Xmn256M -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -Xss256k -jar /home/kunghsu/springboot/kunsharedemo-0.0.1.war

5.发布到私服
clean deploy -Dmaven.test.skip=true