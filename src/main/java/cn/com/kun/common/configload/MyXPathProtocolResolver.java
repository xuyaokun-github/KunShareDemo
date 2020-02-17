package cn.com.kun.common.configload;

import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * 用于加载jar外部的properties文件，扩展classpath
 * -- app.jar
 * -- config/a.property   INSIDE order=3
 * -- a.property          INSIDE order=4
 * -- config/a.property       OUTSIDE order=1
 * -- a.property              OUTSIDE order=2
 * <p>
 * 例如：* 1、@PropertySource("::a.property")
 * 查找路径为：./config/a.property,./a.property,如果找不到则返回null,路径相对于app.jar
 * 2、@PropertySource("::x/a.property")
 * 查找路径为：./config/x/a.property,./x/a.property,路径相对于app.jar
 * 3、@PropertySource("*:a.property")
 * 查找路径为：./config/a.property,./a.property,CLASSPATH:/config/a.property,CLASSPATH:/a.property
 * 4、@PropertySource("*:x/a.property")
 * 查找路径为：./config/x/a.property,./x/a.property,CLASSPATH:/config/x/a.property,CLASSPATH:/x/a.property
 * <p>
 * 如果指定了customConfigPath，上述路径中的/config则会被替换
 *
 **/
public class MyXPathProtocolResolver implements ProtocolResolver {

    /**
     * 查找OUTSIDE的配置路径，如果找不到，则返回null
     * （返回null会直接导致报异常，所以可以优化成找不到就到全局找）
     */
    private static final String X_PATH_OUTSIDE_PREFIX = "::";

    /**
     * 查找OUTSIDE 和 INSIDE，其中inside将会转换为CLASS_PATH
     * 假如在OUTSIDE找不到，就到INSIDE中找，即是到全局CLASS_PATH中找
     */
    private static final String X_PATH_GLOBAL_PREFIX = "*:";


    private static final String CONFIG_DIR = "customConfig";


    private String customConfigPath;//配置文件的放置目录，可以不指定

    public MyXPathProtocolResolver(String configPath) {
        this.customConfigPath = configPath;
    }

    @Override
    public Resource resolve(String location, ResourceLoader resourceLoader) {

        //假如不是 :: 或者 *: ，不做处理
        if (!location.startsWith(X_PATH_OUTSIDE_PREFIX) && !location.startsWith(X_PATH_GLOBAL_PREFIX)) {
            return null;
        }

        //拿到真实路径
        String realPath = path(location);

        Collection<String> fileLocations = searchLocationsForFile(realPath);
        //根据fileLocations集合中的所有路径，尝试到系统中加载
        Resource resource = this.getResourceByResourceLoader(fileLocations, resourceLoader);
        if (resource != null){
            return resource;
        }

        //运行到这里，说明上面的方式没加载到，OUTSIDE的方式没有加载到，所以继续往下，尝试用全局的方式加载
        //假如不是 *: 直接返回。所以只支持两种 ： :: 和 *:
        boolean global = location.startsWith(X_PATH_GLOBAL_PREFIX);
        if (!global) {
            return null;
        }

        Collection<String> classpathLocations = searchLocationsForClasspath(realPath);
        resource = this.getResourceByResourceLoader(classpathLocations, resourceLoader);
        if (resource != null){
            return resource;
        }

        return resourceLoader.getResource(realPath);
    }


    private Resource getResourceByResourceLoader(Collection<String> locations,ResourceLoader resourceLoader){

        for (String path : locations) {
            Resource resource = resourceLoader.getResource(path);
            if (resource != null && resource.exists()) {
                //假如加载到一个，就返回Resource对象，无需继续
                //所以配置文件之间存在优先级关系
                return resource;
            }
        }
        return null;
    }

    /**
     * 从文件系统中找
     *
     * @param location
     * @return
     */
    private Collection<String> searchLocationsForFile(String location) {
        //locations是一个路径集合，每个路径都以file:开头
        Collection<String> locations = new LinkedHashSet<>();
        String _location = shaping(location);
        if (customConfigPath != null) {
            //假如customConfigPath不为空，说明放自定义配置的目录是自定义的，需要动态变
            String prefix = ResourceUtils.FILE_URL_PREFIX + customConfigPath;
            if (!customConfigPath.endsWith("/")) {
                locations.add(prefix + "/" + _location);
            } else {
                locations.add(prefix + _location);
            }
        } else {
            //假如没指定，就默认用customConfig
            locations.add(ResourceUtils.FILE_URL_PREFIX + CONFIG_DIR + "/" + _location);
        }
        //添加一个不带目录的，只有文件名
        locations.add(ResourceUtils.FILE_URL_PREFIX + "./" + _location);
        return locations;
    }

    /**
     * 从全局Classpath中找
     *
     * @param location
     * @return
     */
    private Collection<String> searchLocationsForClasspath(String location) {
        Collection<String> locations = new LinkedHashSet<>();
        String _location = shaping(location);
        if (customConfigPath != null) {
            String prefix = ResourceUtils.CLASSPATH_URL_PREFIX + customConfigPath;
            if (!customConfigPath.endsWith("/")) {
                locations.add(prefix + "/" + _location);
            } else {
                locations.add(prefix + _location);
            }
        } else {
            locations.add(ResourceUtils.CLASSPATH_URL_PREFIX + "/" + CONFIG_DIR + "/" + _location);
        }

        locations.add(ResourceUtils.CLASSPATH_URL_PREFIX + "/" + _location);
        return locations;
    }

    private String shaping(String location) {
        if (location.startsWith("./")) {
            return location.substring(2);
        }
        if (location.startsWith("/")) {
            return location.substring(1);
        }
        return location;
    }

    /**
     * remove protocol
     * 去除路径前的::或者*:,得到完整的路径
     *
     * @param location
     * @return
     */
    private String path(String location) {
        return location.substring(2);
    }
}
