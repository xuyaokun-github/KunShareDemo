package cn.com.kun.common.constants;

public class MemoryCacheConfigConstants {

    /**
     * 这里必须要定义成常量字符串，不然@Cache会提示编译错误
     */
    public static final String MEMORY_CACHE_CONFIG_NAME_STUDENT = "memorycache-student-service";

    /**
     * 有些时候，查询条件不止一个，就需要定义多个 缓存管理器
     * 例如本例，针对同一个学生模块，可能会有多个缓存管理器
     */
    public static final String MEMORY_CACHE_CONFIG_NAME_STUDENT_2 = "memorycache-student-service-2";

    public static final String MEMORY_CACHE_CONFIG_NAME_STUDENT_3 = "memorycache-student-service-3";


}
