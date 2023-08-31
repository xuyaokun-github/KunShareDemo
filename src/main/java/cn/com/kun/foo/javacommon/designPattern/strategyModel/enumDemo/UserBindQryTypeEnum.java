package cn.com.kun.foo.javacommon.designPattern.strategyModel.enumDemo;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举+抽象-实现策略模式
 *
 * author:xuyaokun_kzx
 * date:2023/8/2
 * desc:
*/
public enum UserBindQryTypeEnum {

    DKKJ("DKKJ") {
        @Override
        public UserCenterQueryVO buildQryParams() {
            UserCenterQueryVO queryVO = new UserCenterQueryVO();
            queryVO.setChannel(this.getChannel());
            return queryVO;
        }

        @Override
        public boolean existBindRelation(UserCenterQueryResVO queryResVO) {
            return StringUtils.isNotEmpty(queryResVO.getDkkjOpenId());
        }
    },
    EMAIL("EMAIL") {
        @Override
        public UserCenterQueryVO buildQryParams() {
            UserCenterQueryVO queryVO = new UserCenterQueryVO();
            queryVO.setChannel(this.getChannel());
            return queryVO;
        }
        @Override
        public boolean existBindRelation(UserCenterQueryResVO queryResVO) {
            return StringUtils.isNotEmpty(queryResVO.getEmail());
        }
    },
    IDWX("IDWX") {
        @Override
        public UserCenterQueryVO buildQryParams() {
            UserCenterQueryVO queryVO = new UserCenterQueryVO();
            queryVO.setChannel(this.getChannel());
            return queryVO;
        }
        @Override
        public boolean existBindRelation(UserCenterQueryResVO queryResVO) {
            return StringUtils.isNotEmpty(queryResVO.getIdwxOpenId());
        }
    };

    private String channel;

    private static Map<String, UserBindQryTypeEnum> queryTypeEnumMap = new HashMap<>();
    static {
        queryTypeEnumMap.put("PM10", DKKJ);
        queryTypeEnumMap.put("PM01", IDWX);
        queryTypeEnumMap.put("EM01", EMAIL);
        queryTypeEnumMap.put("EM02", EMAIL);
    }

    UserBindQryTypeEnum(String channel) {
        this.channel = channel;
    }

    public static UserBindQryTypeEnum get(String channel) {

        return queryTypeEnumMap.get(channel);
    }

    public abstract UserCenterQueryVO buildQryParams();

    /**
     * 可以实现多个方法
     * @return
     */
    public abstract boolean existBindRelation(UserCenterQueryResVO queryResVO);


    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
