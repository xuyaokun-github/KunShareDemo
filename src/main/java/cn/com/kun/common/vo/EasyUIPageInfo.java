package cn.com.kun.common.vo;

import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;

/**
 * 与EasyUI的分页查询场景配合使用
 *
 * Created by xuyaokun On 2020/5/17 23:03
 * @desc:
 */
public class EasyUIPageInfo<T> extends PageInfo {

    private Collection<T> rows;//必须要有这个属性，EasyUI前端才能拿到数据

    public EasyUIPageInfo(List list) {
        super(list);
        this.rows = list;
    }

    public Collection<T> getRows() {
        return rows;
    }

    public void setRows(Collection<T> rows) {
        this.rows = rows;
    }
}
