package cn.com.kun.component.cache.localcache;

import freemarker.template.Template;

import java.io.IOException;
import java.util.function.Supplier;

public class FmkTemplateLocalCache extends LocalCache<Template>{

    public static void main(String[] args) {

        FmkTemplateLocalCache fmkTemplateLocalCache = new FmkTemplateLocalCache();
//        fmkTemplateLocalCache.get("", "", null);
        Template template = fmkTemplateLocalCache.getTemplate("", "", "0000");
        System.out.println(template);
    }

    /**
     * 对外界暴露的可以是各种参数的方法
     * @param templateName
     * @param timeStamp
     * @param htmlSource
     * @return
     */
    public Template getTemplate(String templateName, String timeStamp, String htmlSource){

        Supplier<Template> supplier = new Supplier<Template>() {
            @Override
            public Template get() {
                Template template = null;
                try {
                    template = new Template(templateName, htmlSource, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return template;
            }
        };

        return get(templateName, timeStamp, supplier);
    }


}
