package cn.com.kun.elasticsearch.service.impl;

import cn.com.kun.elasticsearch.dao.NbaPlayerElasticRepository;
import cn.com.kun.elasticsearch.entity.NbaPlayerDocBean;
import cn.com.kun.elasticsearch.service.INbaPlayerElasticService;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;


/**
 *
 *
 * @author xuyaokun
 * @date 2019/12/5 16:25
 */
@Service
public class NbaPlayerElasticServiceImpl implements INbaPlayerElasticService {

    public final static Logger logger = LoggerFactory.getLogger(NbaPlayerElasticServiceImpl.class);

    //官方推荐用这个
    @Autowired(required = false)
    private ElasticsearchOperations elasticsearchTemplate;

    @Autowired(required = false)
    private NbaPlayerElasticRepository elasticRepository;

    private Pageable pageable = PageRequest.of(0, 10);//页码、每页条数


    @Override
    public void createIndex() {

        //根据一个class对象，直接创建索引
        /*
            可以重复调用，重复调用也不会返回false,创建成功的话每次都是返回true
         */
        boolean createSuccess = elasticsearchTemplate.createIndex(NbaPlayerDocBean.class);
        logger.info("创建索引返回：" + createSuccess);
    }

    /**
     * 删除索引
     * @param index
     */
    @Override
    public void deleteIndex(String index) {
        elasticsearchTemplate.deleteIndex(index);
    }

    @Override
    public void save(NbaPlayerDocBean docBean) {

        NbaPlayerDocBean result = elasticRepository.save(docBean);
        /*
            插入后拿到的对象是插入前的实体，是相同对象（自动生成的id不会回写）
            同一个实体，即使id相同也能调用保存多次
            其实对于http接口也是一样的，同一个id的文档，你PUT多次都可以的
            只不过后续的会返回"result": "updated"，表示这是一次更新，而不是生成
         */
        logger.info("插入前后，是否同一个对象：" + (result == docBean));//true
    }

    /**
     * 批量保存
     *
     * @param list
     */
    @Override
    public void saveAll(List<NbaPlayerDocBean> list) {

        //这里返回的是一个集合，但是集合里的元素可能不带有id
        //因为自动生成的id,通过这种方式无法回写到实体类
        Iterable<NbaPlayerDocBean> result = elasticRepository.saveAll(list);
        result.forEach(vo -> {
            logger.info(JSONObject.toJSONString(vo));
        });
    }

    /**
     * 查询全部记录
     * @return
     */
    @Override
    public Iterator<NbaPlayerDocBean> findAll() {

        return elasticRepository.findAll().iterator();
    }

    /**
     * 分页查询（查所有）
     *
     */
    @Override
    public Page<NbaPlayerDocBean> queryAll(Pageable pageable) {
        return elasticRepository.findAll(pageable);
    }

    @Override
    public Page<NbaPlayerDocBean> findByContent(String content) {
        return elasticRepository.findByContent(content, pageable);
    }

    @Override
    public Page<NbaPlayerDocBean> findByFirstCode(String firstCode) {
        return elasticRepository.findByFirstCode(firstCode,pageable);
    }

    @Override
    public List<NbaPlayerDocBean> findByFirstCodeNoPage(String firstCode) {
        return elasticRepository.findByFirstCode(firstCode);
    }

    @Override
    public Page<NbaPlayerDocBean> findBySecordCode(String secordCode) {
        return elasticRepository.findBySecordCode(secordCode,pageable);
    }


    /**
     * 自定义查询条件查询
     *
     * @return
     */
    @Override
    public Page<NbaPlayerDocBean> queryByQueryBuilder(){

        //创建builder
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        //builder下有must、should以及mustNot 相当于sql中的and、or以及not

        //设置模糊搜索,firstCode字段中包含11的记录
        //好像并没有体现出模糊的效果，好像是匹配查询
        //fuzzy它是一种搜索技术，它并不是sql中的like用法
        /*
        简单说下吧
        假如值是11FirstCode，你用11FirstCod、11FirstCo去查，能查到
        用11FirstC或者11First，就查不到了，因为差别有距离，它有一个默认距离，差得太远，通过fuzzy则查不出来
         */
        builder.must(QueryBuilders.fuzzyQuery("firstCode", "11FirstCod"));

        //通配符查询（验证是ok的）
        //用通配符，能起到like的效果
//        builder.must(QueryBuilders.wildcardQuery("firstCode", "11Fir*Code"));

        //设置secordCode为XX7475,表示=操作(验证是ok的)
//        builder.must(new QueryStringQueryBuilder("XX7475").field("secordCode"));

        //排序(选择排序字段)
        FieldSortBuilder sort = SortBuilders.fieldSort("secordCode").order(SortOrder.DESC);

        //设置分页
        //====注意!es的分页和Hibernate一样api是从第0页开始的=========
        PageRequest page = PageRequest.of(0, 2);

        //构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //将搜索条件设置到构建中
        nativeSearchQueryBuilder.withQuery(builder);
        //将分页设置到构建中（表示需要分页）
        nativeSearchQueryBuilder.withPageable(page);
        //将排序设置到构建中
        nativeSearchQueryBuilder.withSort(sort);
        //创建NativeSearchQuery
        NativeSearchQuery query = nativeSearchQueryBuilder.build();

        //执行,返回包装结果的分页
        Page<NbaPlayerDocBean> resutlList = elasticRepository.search(query);

        return resutlList;
    }

}
