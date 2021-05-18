package cn.com.kun.service;

import cn.com.kun.common.vo.PageParam;
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
        return PageHelper.startPage(param).doSelectPageInfo(() -> list(param.getParam()));
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