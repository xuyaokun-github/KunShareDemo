package cn.com.kun.common.vo;

import cn.com.kun.common.utils.JacksonUtils;
import com.github.pagehelper.IPage;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

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

    /**
     * 这个让前端进行传递可能会有sql注入
     */
    // description = "排序", example = "id desc"
    private String orderBy;

    //  description = "参数"
    private T param;

    public static <T> PageParam<T> build(Map<String, String> reqVO, Class<T> clazz) {

        PageParam<T> pageParam = new PageParam<>();
        //先处理和分页有关的参数,假如存在则处理进行赋值，否则用默认的值
        String pageNum = reqVO.get("pageNum");
        String pageSize = reqVO.get("pageSize");
        String orderBy = reqVO.get("orderBy");

        if (StringUtils.isNotEmpty(pageNum)){
            pageParam.setPageNum(Integer.valueOf(pageNum));
            reqVO.remove("pageNum");
        }
        if (StringUtils.isNotEmpty(pageSize)){
            pageParam.setPageSize(Integer.valueOf(pageSize));
            reqVO.remove("pageSize");
        }
        if (StringUtils.isNotEmpty(orderBy)){
            pageParam.setOrderBy(orderBy);
            reqVO.remove("orderBy");
        }
        //业务参数里，不能使用和分页参数同名的参数，这是强制规定
        T res = (T) JacksonUtils.toJavaObject(reqVO, clazz);
        pageParam.setParam(res);
        return pageParam;
    }

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

    /**
     * 该vo也可以用在es的dao层做分页
     * 调用该方法获取起始行，es中的from表示的不是页码，而是起始行
     * this.pageNum表示的是页码，供前端使用
     * @return
     */
    private Integer getStartRow() {
        return this.pageNum > 0 ? (this.pageNum - 1) * this.pageSize : 0;
//        this.endRow = this.startRow + this.pageSize * (this.pageNum > 0 ? 1 : 0);
    }
}
