package cn.com.kun.springframework.core.jackson.lanyang;

/**
 * respect lanyang
 *
 * author:xuyaokun_kzx
 * date:2021/12/16
 * desc:
*/
public class LanYangVO {

    /**
     * 值是 0、1、2、3 希望在返回返回前端时，自动转成 完成、成功、停止、失败
     */
    @StatusWrapped
    private String status;

    private String name;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
