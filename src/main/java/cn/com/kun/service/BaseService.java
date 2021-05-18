package cn.com.kun.service;

import cn.com.kun.common.vo.PageParam;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;


/**
 *
 * author:xuyaokun_kzx
 * date:2021/5/18
 * desc:
 * @param <Param> 泛型request (下面Param和Result只是一个泛型参数)
 * @param <Result> 泛型response
*/
public interface BaseService<Param, Result> {

    /**
     * 分页查询
     * （这个方法子类不需要实现，需要做分页查询时就直接调用即可）
     * @param param 请求参数DTO
     * @return 分页集合
     */
    default PageInfo<Result> page(PageParam<Param> param) {
        /**
         * 返回的PageInfo对象里信息有很多，例如total
         * 是不是应该把这个PageInfo对象直接返回给前端呢？
         * 我建议是不，因为前端不需要用到这么多信息，信息应该尽量精简化
         * 应该在服务层做一下转换，再返回给控制层
         */
        return PageHelper.startPage(param).doSelectPageInfo(() -> list(param.getParam()));
    }

    /**
     * 分页查询（不需要执行count）
     * （这个方法子类不需要实现，需要做分页查询时就直接调用即可）
     * @param param 请求参数DTO
     * @return 分页集合
     */
    default PageInfo<Result> pageNoCount(PageParam<Param> param) {

        /**
         * false表示不需要count
         */
        Page page = PageHelper.startPage(param.getPageNum(), param.getPageSize(), false);
        page.setOrderBy(param.getOrderBy());
        return page.doSelectPageInfo(() -> list(param.getParam()));
    }

    /**
     * 集合查询
     * (要想用上面的分页查询方法，必须要有一个list方法作为基础，这个是必须要实现的
     * 所以最佳实践中，假如业务层有用到dao层的分页查询，再实现这个接口，否则不需要)
     * @param param 查询参数
     * @return 查询响应
     */
    List<Result> list(Param param);

}