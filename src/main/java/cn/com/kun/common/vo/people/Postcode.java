package cn.com.kun.common.vo.people;

import cn.com.kun.common.annotation.DesensitizationField;

import static cn.com.kun.common.constants.DesensitizationConstants.*;

/**
 * 每个地址都有对应的邮编信息
 *
 * author:xuyaokun_kzx
 * date:2021/5/25
 * desc:
*/
public class Postcode {

    /**
     * 邮政编码
     */
    @DesensitizationField(expression = POSTCODE_EXPRESSION, replace = POSTCODE_REPLACE)
    private String postcodeNum;

    /**
     * 邮政编码描述
     */
    private String postcodeNumDesc;

    public String getPostcodeNum() {
        return postcodeNum;
    }

    public void setPostcodeNum(String postcodeNum) {
        this.postcodeNum = postcodeNum;
    }

    public String getPostcodeNumDesc() {
        return postcodeNumDesc;
    }

    public void setPostcodeNumDesc(String postcodeNumDesc) {
        this.postcodeNumDesc = postcodeNumDesc;
    }
}
