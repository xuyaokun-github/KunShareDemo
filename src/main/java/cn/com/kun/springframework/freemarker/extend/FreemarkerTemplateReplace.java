package cn.com.kun.springframework.freemarker.extend;

import java.util.Map;

public class FreemarkerTemplateReplace {

    private String templateName;
    private String timeStamp;
    private String htmlSource;
    private Map<String, Object> paramSource;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getHtmlSource() {
        return htmlSource;
    }

    public void setHtmlSource(String htmlSource) {
        this.htmlSource = htmlSource;
    }

    public Map<String, Object> getParamSource() {
        return paramSource;
    }

    public void setParamSource(Map<String, Object> paramSource) {
        this.paramSource = paramSource;
    }
}
