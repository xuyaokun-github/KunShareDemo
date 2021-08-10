package cn.com.kun.config.redisson;

import cn.com.kun.component.runner.MyCommandLineRunner;
import cn.com.kun.common.utils.RedissonUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.spring.starter.RedissonProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * 自定义RedissonConfig（支持密码加密）
 * RedissonAutoConfiguration类的Redisson将不会生效
 *
 * Created by xuyaokun On 2020/9/27 23:00
 * @desc:
 */
@ConditionalOnProperty(prefix = "kunsharedemo.redisson", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Configuration
public class RedissonConfig {

    public final static Logger LOGGER = LoggerFactory.getLogger(MyCommandLineRunner.class);

    //自定义的配置项--start
    @Value("${redisson.url}")
    private String url;

    @Value("${redisson.password}")
    private String password;

    @Value("${redisson.cluster.urls}")
    private String clusterUrls;
    //自定义的配置项--end

    /**
     * RedissonProperties是通过RedissonAutoConfiguration类加载进来的
     * 假如没有以spring.redis.redisson开头的配置项，容器将不会存在RedissonProperties
     */
    @Autowired(required = false)
    private RedissonProperties redissonProperties;

    @Autowired
    private ApplicationContext ctx;

    /**
     * 单实例模式
     *
     * @return
     * @throws IOException
     */
    @Bean(name = "redissonClient")
    public RedissonClient redissonClientSingle() throws IOException {
        RedissonClient redisson = null;
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + url);
//        //去掉默认的序列化Jackson,改用JDK序列化
//        config.setCodec(new SerializationCodec());
        redisson = Redisson.create(config);
        //赋值到工具类
        RedissonUtil.init(redisson);
        return redisson;
    }

    /**
     * 集群模式（使用自定义的配置进行初始化RedissonClient）
     *
     * @return
     * @throws IOException
     */
    @Bean(name = "redissonClientCluster")
    @ConditionalOnProperty(prefix = "kunsharedemo.redisson.cluster", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
    public RedissonClient redissonClientCluster() throws IOException {

        String[] nodes = clusterUrls.split(",");
        //redisson版本是3.5，集群的ip前面要加上“redis://”，不然会报错，3.2版本可不加
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = "redis://" + nodes[i];
        }
        RedissonClient redisson = null;
        Config config = new Config();
        //调用set方法可以自定义所有配置项，包括最小最大空闲连接数等
        ClusterServersConfig clusterServersConfig = config.useClusterServers() //集群模式
                .setScanInterval(5000) //设置集群状态扫描时间
                .setTimeout(300000) //超时时间
                .addNodeAddress(nodes);

        if (!StringUtils.isEmpty(password)){
            //密码解密
            String decrptPassword = password.replace("#", "");
            clusterServersConfig.setPassword(decrptPassword);
        }
        redisson = Redisson.create(config);
        //检测是否配置成功
        LOGGER.info("Redisson初始化结果：" + redisson.getConfig().toJSON().toString());
        //赋值到工具类
        RedissonUtil.init(redisson);
        return redisson;
    }

    /**
     * 集群模式（使用yml文件进行初始化RedissonClient）
     *
     * @return
     * @throws IOException
     */
//    @Bean(name = "redissonClient")
    public RedissonClient redissonClientCluster2() throws IOException {

        RedissonClient redisson = null;
        Config config = new Config();
        if (this.redissonProperties.getConfig() != null) {
            try {
                InputStream is = this.getConfigStream();
                config = Config.fromYAML(is);
            } catch (IOException var10) {
                throw new IllegalArgumentException("Can't parse config", var10);
            }
        }

        if (!StringUtils.isEmpty(config.useClusterServers().getPassword())){
            //密码解密
            String decrptPassword = config.useClusterServers().getPassword().replace("#", "").replace("\\", "");
            //设置解密后的密码进行覆盖
            config.useClusterServers().setPassword(decrptPassword);
        }
        redisson = Redisson.create(config);
        //检测是否配置成功
        LOGGER.info("Redisson初始化结果：" + redisson.getConfig().toJSON().toString());
        //赋值到工具类
        RedissonUtil.init(redisson);
        return redisson;
    }

    private InputStream getConfigStream() throws IOException {
        Resource resource = this.ctx.getResource(this.redissonProperties.getConfig());
        InputStream is = resource.getInputStream();
        return is;
    }

}
