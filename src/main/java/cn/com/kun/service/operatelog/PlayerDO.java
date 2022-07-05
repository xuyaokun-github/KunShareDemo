package cn.com.kun.service.operatelog;

/**
 * 模拟这个是一个Dao层的bean
 * author:xuyaokun_kzx
 * date:2022/7/5
 * desc:
*/
public class PlayerDO {

    private Long playerId;

    private String playName;

    private String playAddress;

    public String getPlayName() {
        return playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public String getPlayAddress() {
        return playAddress;
    }

    public void setPlayAddress(String playAddress) {
        this.playAddress = playAddress;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "PlayerDO{" +
                "playerId=" + playerId +
                ", playName='" + playName + '\'' +
                ", playAddress='" + playAddress + '\'' +
                '}';
    }
}
