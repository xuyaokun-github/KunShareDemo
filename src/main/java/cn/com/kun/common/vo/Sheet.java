package cn.com.kun.common.vo;

import java.util.Collection;

/**
 * 页集合。 结构由一个total总数和一个List列表组合而成。
 * 与easyUI配合使用（但是这样不好用）
 *
 * @param <T> 集合元素的数据类型
 */
public class Sheet<T> implements java.io.Serializable {

    private long total = -1;

    private Collection<T> rows;

    public Sheet() {
        super();
    }

    public Sheet(int total, Collection<? extends T> data) {
        this((long) total, data);
    }

    public Sheet(long total, Collection<? extends T> data) {
        this.total = total;
        this.rows = (Collection<T>) data;
    }

    public static <E> Sheet<E> asSheet(Collection<E> data) {
        return data == null ? new Sheet() : new Sheet(data.size(), data);
    }

    /**
     * 判断数据列表是否为空
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        return this.rows == null || this.rows.isEmpty();
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Collection<T> getRows() {
        return rows;
    }

    public void setRows(Collection<T> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "Sheet[total=" + this.total + ", rows=" + this.rows + "]";
    }
}
