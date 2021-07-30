package cn.com.kun.common.enums;

/**
 * 数据库表过期数据清理枚举类
 * 这种信息不常变，不需要放到数据库保存
 *
 * author:xuyaokun_kzx
 * date:2021/7/30
 * desc:
*/
public enum TblExpiredDataRemoveEnum {

    /**
     * 一个枚举项，表示需要清理的数据库表
     */
    STUDENT("tbl_student", 30),
    USER("tbl_user", 40);

    /**
     * 表名
     */
    public String tblName;

    /**
     * 天数（清理多少天之前的数据）
     */
    public int days;

    TblExpiredDataRemoveEnum(String tblName, int days) {
        this.tblName = tblName;
        this.days = days;
    }
}
