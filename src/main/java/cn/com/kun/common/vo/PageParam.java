package cn.com.kun.common.vo;

import com.github.pagehelper.IPage;

public class PageParam<T>  implements IPage {

    //  description = "页码", defaultValue =  1
    private Integer pageNum = 1;

    // description = "页数", defaultValue = 20
    private Integer pageSize = 20;

    // description = "排序", example = "id desc"
    private String orderBy;

    //  description = "参数"
    private T param;

    public PageParam<T> setOrderBy(String orderBy) {
        // 此处可优化 优化详情且看解析
        this.orderBy = orderBy;
        return this;
    }

    @Override
    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String getOrderBy() {
        return orderBy;
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }
}
