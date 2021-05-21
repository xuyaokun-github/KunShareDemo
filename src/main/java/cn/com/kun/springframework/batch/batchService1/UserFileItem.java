package cn.com.kun.springframework.batch.batchService1;

/**
 * 文件行的映射实体类
 * 一个对象就表示文件中的一行
 *
 * author:xuyaokun_kzx
 * date:2021/5/21
 * desc:
*/
public class UserFileItem {

    private Long uid;
    private String tag;
    private Integer type;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
