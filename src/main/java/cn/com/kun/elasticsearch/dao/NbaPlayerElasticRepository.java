package cn.com.kun.elasticsearch.dao;

import cn.com.kun.elasticsearch.entity.NbaPlayerDocBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface NbaPlayerElasticRepository extends ElasticsearchRepository<NbaPlayerDocBean, Long> {

    /*
      下面定义三个自定义方法
     */

    //可以不用Query注解，因为JPA会自动根据方法名的规则解析，并生成查询条件

    //默认的注释
    //@Query("{\"bool\" : {\"must\" : {\"field\" : {\"content\" : \"?\"}}}}")
    Page<NbaPlayerDocBean> findByContent(String content, Pageable pageable);

    //都是自动的，假如我们的需求是简单查询，完全可以不用@Query注解
//    @Query("{\"bool\" : {\"must\" : {\"field\" : {\"firstCode\" : \"?\"}}}}")
    Page<NbaPlayerDocBean> findByFirstCode(String firstCode, Pageable pageable);

    //当然也可以不用分页的方式
    List<NbaPlayerDocBean> findByFirstCode(String firstCode);

    Page<NbaPlayerDocBean> findBySecordCode(String secordCode, Pageable pageable);


}
