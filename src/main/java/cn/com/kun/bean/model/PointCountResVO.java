package cn.com.kun.bean.model;

import java.util.List;

/**
 * 折线图接口实体类-球星的得分趋势图
 *
 * author:xuyaokun_kzx
 * date:2021/6/18
 * desc:
*/
public class PointCountResVO {

    //'麦迪'
    private String name;

    //得分集合，[50, 30, 24, 18, 35, 47, 60]
    private List<Integer> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
