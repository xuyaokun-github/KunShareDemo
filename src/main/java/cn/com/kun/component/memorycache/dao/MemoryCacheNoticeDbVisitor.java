package cn.com.kun.component.memorycache.dao;

import cn.com.kun.component.memorycache.entity.MemoryCacheNoticeDO;

import java.util.List;

/**
 * 数据库访问层接口（扩展点）
 *
 * author:xuyaokun_kzx
 * date:2023/3/2
 * desc:
*/
public interface MemoryCacheNoticeDbVisitor {

    /**
     * 保存数据库信息
     * 假如组件使用方，需要扩展DAO层实现，可以复写save方法，甚至可以多保存一些内容进数据库
     *
     * @param noticeMsgDO
     */
    boolean save(MemoryCacheNoticeDO noticeMsgDO);

    List<MemoryCacheNoticeDO> query(String clusterName);


    boolean delete(Long id);

}
