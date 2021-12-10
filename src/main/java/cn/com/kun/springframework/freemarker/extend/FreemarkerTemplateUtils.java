package cn.com.kun.springframework.freemarker.extend;

import cn.com.kun.component.cache.localcache.FmkTemplateLocalCache;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ConcurrentModel;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Freemarker Template替换工具类
 * author:xuyaokun_kzx
 * date:2021/11/23
 * desc:
*/
public class FreemarkerTemplateUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(FreemarkerTemplateUtils.class);

    /**
     * 假如发现性能较差，再使用这个缓存机制，否则不要用缓存机制
     * 假如拼模板这个动作并发很高，则有必要用上缓存
     * 为什么可能性能会较差？因为创建模板的过程涉及到IO
     */
    private static Map<String, Map<String, Template>> templateCache = new ConcurrentHashMap<>();

    private static FmkTemplateLocalCache fmkTemplateLocalCache = new FmkTemplateLocalCache();

    public static String replace(FreemarkerTemplateReplace freemarkerTemplateReplace){

        String result = null;
        try (Writer out = new StringWriter()) {
            //htmlSource通常不会随便改变，name与sourceCode应该是一一对应
            Template template = fmkTemplateLocalCache.getTemplate(freemarkerTemplateReplace.getTemplateName(), freemarkerTemplateReplace.getTimeStamp(), freemarkerTemplateReplace.getHtmlSource());
//            Template template = getTemplate(freemarkerTemplateReplace.getTemplateName(), freemarkerTemplateReplace.getTimeStamp(), freemarkerTemplateReplace.getHtmlSource());
            //注意，这里假如html语法有误，模板对象拿到是空对象
            if (template == null){
                LOGGER.error(String.format("模板对象创建异常, html:%s param:%s", freemarkerTemplateReplace.getHtmlSource(), freemarkerTemplateReplace.getParamSource()));
                return null;
            }
            template.createProcessingEnvironment(freemarkerTemplateReplace.getParamSource(), out).process();
            result = out.toString();
        } catch (Exception e) {
            LOGGER.error(String.format("html模板替换异常, html:%s param:%s", freemarkerTemplateReplace.getHtmlSource(), freemarkerTemplateReplace.getParamSource()), e);
        }
        return result;
    }

    /**
     *
     * 获取Template的过程支持缓存
     * 这样做，就必须每次尝试获取模板时就必须携带时间戳
     *
     * @param templateName
     * @param timeStamp
     * @param htmlSource
     * @param paramSource
     * @return
     */
    public static String replace(String templateName, String timeStamp, String htmlSource, Map<String, Object> paramSource){

        String result = null;
        try (Writer out = new StringWriter()) {
            //htmlSource通常不会随便改变，name与sourceCode应该是一一对应
//            Template template = getTemplate(templateName, timeStamp, htmlSource);
            Template template = fmkTemplateLocalCache.getTemplate(templateName, timeStamp, htmlSource);
            template.createProcessingEnvironment(paramSource, out).process();
            result = out.toString();
        } catch (Exception e) {
            LOGGER.error(String.format("html模板替换异常, html:%s param:%s", htmlSource, paramSource), e);
        }
        return result;
    }

    private static Template getTemplate(String templateName, String timeStamp, String htmlSource) throws IOException {

        Template template = null;

        Map<String, Template> timeStampMap = templateCache.get(templateName);
        if (timeStampMap == null){
            timeStampMap = new ConcurrentHashMap();
            templateCache.put(templateName, timeStampMap);
        }

        template = timeStampMap.get(timeStamp);
        if (template == null){
            //假如使用最新的时间戳获取不到，说明模板发生了变化，必须将旧缓存清空
            timeStampMap.clear();
            template = new Template(templateName, htmlSource, null);
            timeStampMap.put(timeStamp, template);
        }

        return template;
    }



    /**
     * org.springframework.ui.ConcurrentModel在低版本1.5.12是没有的
     * @param htmlSource 源html
     * @param paramSource 参数
     * @return
     */
    public static String replace(String htmlSource, ConcurrentModel paramSource){

        //参数也可以传Map<String, Object>
        return replace(htmlSource, paramSource.asMap());
    }

    public static String replace(String htmlSource, Map<String, Object> paramSource){

        String result = null;
        try (Writer out = new StringWriter()) {
            //htmlSource通常不会随便改变，name与sourceCode应该是一一对应
            Template template = new Template("commonTemplate", htmlSource, null);
            template.createProcessingEnvironment(paramSource, out).process();
            result = out.toString();
        } catch (Exception e) {
            LOGGER.error(String.format("html模板替换异常, html:%s param:%s", htmlSource, paramSource), e);
        }
        return result;
    }

}
