package cn.com.kun.bean.model.people;

import cn.com.kun.common.annotation.DesensitizationField;

import static cn.com.kun.common.constants.DesensitizationConstants.STREETNAME_EXPRESSION;
import static cn.com.kun.common.constants.DesensitizationConstants.STREETNAME_REPLACE;

/**
 * 一个人可能拥有多个工作城市地址
 * author:xuyaokun_kzx
 * date:2021/5/25
 * desc:
*/
public class WorkAddress {

    /**
     * 城市名
     */
    private String cityName;

    /**
     * 街道名
     */
    @DesensitizationField(expression = STREETNAME_EXPRESSION, replace = STREETNAME_REPLACE)
    private String streetName;

    @DesensitizationField
    private Postcode postcode;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Postcode getPostcode() {
        return postcode;
    }

    public void setPostcode(Postcode postcode) {
        this.postcode = postcode;
    }
}
