package cn.com.kun.component.scc.configcache;

import cn.com.kun.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.bootstrap.config.PropertySourceBootstrapConfiguration;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * SpringCloudConfig客户端扩展
 * 实现配置的保存和基于本地缓存的应急启动
 * 支持2.1.x版本springboot,1.5.x版不兼容，稍微调整也可以支持
 *
 * author:xuyaokun_kzx
 * date:2021/8/9
 * desc:
*/
@Configuration
@EnableConfigurationProperties(CustomSccCacheProperties.class)
public class SpringCloudConfigCacheInitializer implements ApplicationContextInitializer {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringCloudConfigCacheInitializer.class);

    @Autowired(required = false)
    List<PropertySourceLocator> propertySourceLocatorList = Collections.EMPTY_LIST;

    @Autowired
    CustomSccCacheProperties customSccCacheProperties;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        //先判断是否启用了scc
        if (!hasSpringCloudConfigSourceLocator()){
            LOGGER.info("未开启Spring Cloud Config功能");
            return;
        }
        LOGGER.info("已开启Spring Cloud Config功能");

        //判断缓存功能是否开启
        if (!customSccCacheProperties.isEnabled()){
            LOGGER.info("Spring Cloud Config Cache功能未开启");
            return;
        }

        MutablePropertySources mutablePropertySources = applicationContext.getEnvironment().getPropertySources();
        //判断加载远程配置是否成功
        PropertySource remotePropertySource = getRemotePropertySource(mutablePropertySources);
        if (remotePropertySource != null){

            //远程配置已经加载成功
            Map<String, Object> sourceMap = getSourceMap(remotePropertySource);
            String newVersion = getVersion(mutablePropertySources);
            if (newVersion != null){
                sourceMap.put("config.client.version", newVersion);
            }
            //保存到本地
            File file = new File(customSccCacheProperties.getPath());
            if (file.exists()){
                //判断版本是否为最新再决定是否更新
                //从本地中加载文件
                Properties properties = loadLocalProperties(file.getPath());
                if (properties != null){
                    //假如远程返回的有新版本才需要写文件，否则不需要
                    if (hasNewVersion(properties, sourceMap)){
                        LOGGER.info("远程配置版本与本地缓存配置版本不相同，更新本地缓存");
                        saveToLocalFile(sourceMap, file.getPath());
                    }
                }
            }else {
                //文件不存在直接存
                saveToLocalFile(sourceMap, file.getPath());
            }

        }else {
            LOGGER.info("Spring Cloud Config读取远程配置失败");
            //未加载成功，使用本地缓存启动
            /*
                为什么要这样做？
                防止远程config server因为不可抗因素停机，无法正常对外提供服务时
                本服务不依赖config server也能正常启动，避免了强依赖性
             */
            //加载本地文件，然后添加到MutablePropertySources的首位
            if (customSccCacheProperties.isUseLocal()){
                //假如指定了使用本地
                File file = new File(customSccCacheProperties.getPath());
                if (file.exists()){
                    Properties properties = loadLocalProperties(file.getPath());
                    if (properties != null){
                        HashMap hashMap = new HashMap(properties);
                        mutablePropertySources.addFirst(new MapPropertySource("loadLocalPropertySource", hashMap));
                        LOGGER.info("添加本地缓存文件配置至配置源集合");
                    }
                }else {
                    LOGGER.warn("本地缓存文件不存在，无法基于本地缓存进行启动");
                }
            }
        }

    }

    private boolean hasNewVersion(Properties properties, Map<String, Object> sourceMap) {

        //集成git:会有一个属性 config.client.version，因为文件每发生一次变动就会有一次git version
        String newVersion = (String) sourceMap.get("config.client.version");
        String oldVersion = (String) properties.get("config.client.version");
        if (StringUtils.isNotEmpty(newVersion)){
            return !newVersion.equals(oldVersion);
        }
        return true;
    }

    private void saveToLocalFile(Map<String, Object> sourceMap, String path) {

        FileSystemResource fileSystemResource = new FileSystemResource(path);
        File backupFile = fileSystemResource.getFile();
        File oldBackupFile = new File(path + ".old");

        try {
            if (backupFile.exists()){
                //移动
                if(oldBackupFile.exists()){
                    if (!oldBackupFile.delete()){
                        //删不掉，给个日志
                        LOGGER.info("删除旧文件失败");
                    }
                }
                //其实可以强制覆盖，不用判断
                Path path1 = Files.move(backupFile.toPath(), oldBackupFile.toPath());
                if (path1 == null && !backupFile.delete()){
                    LOGGER.info("删除旧文件失败");
                    return;
                }
            }
            backupFile.createNewFile();
            if (!backupFile.canWrite()){
                LOGGER.info("file[{}]不可写", backupFile.getAbsolutePath());
                return;
            }
            OrderedProperties properties = new OrderedProperties();
            //开始保存
            Iterator<String> iterator = sourceMap.keySet().iterator();
            List<String> keyList = new ArrayList<>(sourceMap.keySet());
            Collections.sort(keyList);
            //无序
//            while (iterator.hasNext()){
//                String key = iterator.next();
//                properties.setProperty(key, String.valueOf(sourceMap.get(key)));
//            }
            //按字典序输出key(虽然list排过序了，但是丢给Properties之后又会变成无序)
            for (String key : keyList) {
                properties.setProperty(key, String.valueOf(sourceMap.get(key)));
            }

            //输出一点提示语到文件中
            FileOutputStream fileOutputStream = new FileOutputStream(fileSystemResource.getFile());
            StringBuilder builder = new StringBuilder();
            builder.append("可通过该文件进行临时启动");
            builder.append("\n");
            builder.append("生成时间:" + DateUtils.now());
            builder.append("\n");
            //改成通过字符串形式保存，这样能保证顺序是正确的（但是这个方法是打注释用的，前面会带上#号）
//            for (String key : keyList) {
//                builder.append(key + "=" + String.valueOf(sourceMap.get(key)));
//                builder.append("\n");
//            }
            //store方法会自动加上一个时间（这个方法是上注释的意思）
            properties.store(fileOutputStream, builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 加载本地配置文件，读取为Properties
     * @param path
     * @return
     */
    private Properties loadLocalProperties(String path) {

        Properties properties = new Properties();
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        try {
            FileSystemResource fileSystemResource = new FileSystemResource(path);
            propertiesFactoryBean.setLocation(fileSystemResource);
            propertiesFactoryBean.setProperties(properties);
            propertiesFactoryBean.afterPropertiesSet();
            return propertiesFactoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, Object> getSourceMap(PropertySource remotePropertySource) {

        Map<String, Object> sourceMap = new HashMap<>();
        if (remotePropertySource instanceof CompositePropertySource){
            CompositePropertySource compositePropertySource = (CompositePropertySource) remotePropertySource;
            compositePropertySource.getPropertySources().forEach(propertySource -> {
                MapPropertySource mapPropertySource = (MapPropertySource) propertySource;
                for (String propertyName : mapPropertySource.getPropertyNames()) {
                    //后出现的默认覆盖前面的（跟spring的设计保持一致）
                    sourceMap.put(propertyName, mapPropertySource.getProperty(propertyName));
                }
            });
        }else {
            if (remotePropertySource instanceof MapPropertySource){
                MapPropertySource mapPropertySource = (MapPropertySource) remotePropertySource;
                for (String propertyName : mapPropertySource.getPropertyNames()) {
                    //后出现的默认覆盖前面的（跟spring的设计保持一致）
                    sourceMap.put(propertyName, mapPropertySource.getProperty(propertyName));
                }
            }else {
                LinkedHashMap linkedHashMap = (LinkedHashMap) remotePropertySource.getSource();
                Set<String> keySet = linkedHashMap.keySet();
                for (String key : keySet) {
                    sourceMap.put(key, remotePropertySource.getProperty(key));
                }
            }
        }

        return sourceMap;
    }

    private String getVersion(MutablePropertySources mutablePropertySources){
        String version = null;//config.client.version
        Iterator<PropertySource<?>> iterator = mutablePropertySources.iterator();
        while (iterator.hasNext()) {
            PropertySource source = iterator.next();
            if (source.getName().equals("bootstrapProperties-configClient")){
                version = (String) source.getProperty("config.client.version");
                break;
            }
        }
        return version;
    }

    /**
     * 获取含有具体数据配置的配置源
     * @param mutablePropertySources
     * @return
     */
    private PropertySource getRemotePropertySource(MutablePropertySources mutablePropertySources) {

        //为空说明根本没集成scc
        String sourceName = "";
        Iterator<PropertySource<?>> iterator = mutablePropertySources.iterator();
        while (iterator.hasNext()){
            PropertySource source = iterator.next();
            //以bootstrapProperties开头的就是远程配置中心的配置源
            /*
                当config-server是基于git提供配置时，返回的配置源集合中有两个以bootstrapProperties开头的配置源
                其中一个是bootstrapProperties-configClient，里面只有一个属性即config.client.version
                另一个是具体的 或者 bootstrapProperties-httpxxxx
                当config-server是基于本地文件提供配置时
                只有一个配置源，即类似bootstrapProperties-filexxxx
                所以这里必须要做下兼容，假如判断到存在bootstrapProperties-configClient，跳过
             */
            if (source.getName().equals("bootstrapProperties-configClient")){
                continue;
            }
            if (source.getName().startsWith(PropertySourceBootstrapConfiguration.BOOTSTRAP_PROPERTY_SOURCE_NAME)){
                sourceName = source.getName();
                break;
            }
        }
        if (StringUtils.isNotEmpty(sourceName)){
            PropertySource propertySource = mutablePropertySources.get(sourceName);
            //springboot1.x版用下面的方式获取PropertySource
//            if (propertySource instanceof CompositePropertySource){
//                for (PropertySource propertySource1 : ((CompositePropertySource)propertySource).getPropertySources()) {
//                    if (propertySource1.getName().equals("configService")){
//                        return propertySource1;
//                    }
//                }
//            }
            return propertySource;
        }

        return null;
    }

    /**
     * 判断是否已经注入了ConfigServicePropertySourceLocator
     * @return
     */
    private boolean hasSpringCloudConfigSourceLocator() {

        for (PropertySourceLocator propertySourceLocator : propertySourceLocatorList){
            if (propertySourceLocator instanceof ConfigServicePropertySourceLocator){
                return true;
            }
        }
        return false;
    }

}
