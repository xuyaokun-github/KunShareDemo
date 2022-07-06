package cn.com.kun.foo.javacommon.completableFuture.serialInvoke;

public class AppHeadInfoResponse {

    Object data;

    public AppHeadInfoResponse(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
