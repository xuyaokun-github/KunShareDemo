package cn.com.kun.bean.entity;

import java.util.Date;

public class SceneMsgRecordDO {

    private Long id;
    private String record_id;
    private String task_id;
    private String scene_code;
    private String send_request_content;
    private String status;
    private Integer priority;
    private Integer send_qps_threshold;
    private Date create_time;
    private Date update_time;
    private String update_by;
    private String create_oper;

    public SceneMsgRecordDO(String sceneCode, String recordId) {

        this.scene_code = sceneCode;
        this.record_id = recordId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getScene_code() {
        return scene_code;
    }

    public void setScene_code(String scene_code) {
        this.scene_code = scene_code;
    }

    public String getSend_request_content() {
        return send_request_content;
    }

    public void setSend_request_content(String send_request_content) {
        this.send_request_content = send_request_content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getSend_qps_threshold() {
        return send_qps_threshold;
    }

    public void setSend_qps_threshold(Integer send_qps_threshold) {
        this.send_qps_threshold = send_qps_threshold;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public String getCreate_oper() {
        return create_oper;
    }

    public void setCreate_oper(String create_oper) {
        this.create_oper = create_oper;
    }
}
