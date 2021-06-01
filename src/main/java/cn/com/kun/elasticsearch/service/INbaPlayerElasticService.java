package cn.com.kun.elasticsearch.service;

import cn.com.kun.elasticsearch.entity.NbaPlayerDocBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Iterator;
import java.util.List;

public interface INbaPlayerElasticService {

    void createIndex();

    void deleteIndex(String index);

    void save(NbaPlayerDocBean docBean);

    void saveAll(List<NbaPlayerDocBean> list);

    Iterator<NbaPlayerDocBean> findAll();

    Page<NbaPlayerDocBean> queryAll(Pageable pageable);

    Page<NbaPlayerDocBean> findByContent(String content);

    Page<NbaPlayerDocBean> findByFirstCode(String firstCode);

    List<NbaPlayerDocBean> findByFirstCodeNoPage(String firstCode);

    Page<NbaPlayerDocBean> findBySecordCode(String secordCode);

    Page<NbaPlayerDocBean> queryByQueryBuilder();
}
