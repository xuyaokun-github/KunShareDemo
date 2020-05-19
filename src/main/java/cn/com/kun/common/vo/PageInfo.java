package cn.com.kun.common.vo;

import java.io.Serializable;
import java.util.Collection;

/**
 * 分页对象
 *
 */
public class PageInfo<E> implements Serializable {

    public static final String ORDER_ASC = "ASC";
    public static final String ORDER_DESC = "DESC";

    private int page = 1;  //页码
    private int rows = 20;  //页条数
    private int start = 0;  //开始行
    private int end = 0;  //结束行
    private long total = -1;  //总行数
    private int pages = -1;  //总页数
    private String sortName = "";
    private String sortOrder = ORDER_DESC;
    private Collection<E> result = null;

    public PageInfo() {
    }

    public PageInfo(int rows) {
        this.rows = rows;
    }

    public PageInfo(int page, int rows) {
        this.page = page;
        this.rows = rows;
        this.start = page > 0 ? (page - 1) * rows : 0;
        this.end = page * rows;
    }

    public PageInfo(int page, int rows, String sortName, String sortOrder) {
        this(page, rows);
        this.sortName = sortName;
        this.sortOrder = sortOrder;
    }

    public PageInfo next() {
        this.page++;
        return this;
    }

    public Collection<E> getResult() {
        return result;
    }

    public void setResult(Collection<E> result) {
        this.result = result;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getStart() {
        return page > 0 ? (page - 1) * rows : 0;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void copyTo(PageInfo copy) {
        if (copy == null) return;
        copy.page = this.page;
        copy.rows = this.rows;
    }

    @Override
    public String toString() {
        return "Pager{" +
                "page=" + page +
                ", rows=" + rows +
                ", start=" + start +
                ", end=" + end +
                ", total=" + total +
                ", pages=" + pages +
                '}';
    }
}