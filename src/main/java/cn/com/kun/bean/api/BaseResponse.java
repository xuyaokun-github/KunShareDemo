package cn.com.kun.bean.api;

/**
 * 统一返回响应
 * 这里叫ResultVO或者BaseResponse都可以，名字能看懂就行
 * author:xuyaokun_kzx
 * date:2022/8/17
 * desc:
*/
public class BaseResponse<T> {

    /**
     * 状态码，比如1000代表响应成功
     */
    private String code;
    /**
     * 响应信息，用来说明响应情况
     */
    private String msg;
    /**
     * 响应的具体数据
     */
    private T data;

    public BaseResponse(T data) {
        this(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    public BaseResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResponse(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    public BaseResponse(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
        this.data = data;
    }

    //method
    public static <T> BaseResponse<T> success() {
        BaseResponse vo = new BaseResponse(ResultCode.SUCCESS);
        return vo;
    }

    public static <T> BaseResponse<T> success(T value) {
        BaseResponse vo = new BaseResponse(ResultCode.SUCCESS);
        vo.setData(value);
        return vo;
    }

    public static <T> BaseResponse<T> systemError(T value) {
        BaseResponse vo = new BaseResponse(ResultCode.ERROR);
        vo.setData(value);
        return vo;
    }

    public static <T> BaseResponse<T> error() {
        BaseResponse vo = new BaseResponse(ResultCode.ERROR);
        return vo;
    }

    public static <T> BaseResponse<T> error(ResultCode resultCode, Object... args) {
        BaseResponse vo = new BaseResponse(resultCode);
        vo.setMsg(String.format(resultCode.getMsg(), args));
        return vo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
