package cn.com.kun.common.vo;

import com.github.pagehelper.IPage;

/**
 * 前端可以默认不传分页参数，不过一般都需要传
 * 假如不传，则使用limit 1 20
 * 本类是IPage对象的实现类，其实这不是必须的，用自定义的对象也是可以的
 * 只不过PageHelper提供一个方法重载入参就是IPage
 *
 * Created by xuyaokun On 2021/5/18 22:12
 * @desc:
 */
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
