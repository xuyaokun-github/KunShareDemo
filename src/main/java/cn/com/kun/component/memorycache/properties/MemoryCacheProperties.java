package cn.com.kun.component.memorycache.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "memorycache")
public class MemoryCacheProperties {

    /**
     * 全局开关,默认为关闭
     * 设置为false,禁用MemoryCache功能
     */
    private boolean enabled;

    /**
     * 角色：
     * 1.All 表示同时作为维护方和应用方
     * 2.Maintain 表示维护方
     * 3.Apply 表示应用方
     *
     */
    private String role;

    /**
     * 应用方配置
     */
    private Apply apply = new Apply();

    /**
     * 维护方配置
     */
    private Maintain maintain = new Maintain();

    /**
     * 缓存刷新通知的实现类型：Redis\Eureka\Custom
     * 默认是Redis
     */
    private String noticeImplType = "Redis";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getNoticeImplType() {
        return noticeImplType;
    }

    public void setNoticeImplType(String noticeImplType) {
        this.noticeImplType = noticeImplType;
    }

    public Maintain getMaintain() {
        return maintain;
    }

    public void setMaintain(Maintain maintain) {
        this.maintain = maintain;
    }

    public Apply getApply() {
        return apply;
    }

    public void setApply(Apply apply) {
        this.apply = apply;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public boolean isMaintainApp() {

        return "All".equals(this.role) || "Maintain".equals(this.role);
    }


    public boolean isApplyApp() {

        return "All".equals(this.role) || "Apply".equals(this.role);
    }
}
