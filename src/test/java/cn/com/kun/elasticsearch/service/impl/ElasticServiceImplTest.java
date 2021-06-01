package cn.com.kun.elasticsearch.service.impl;

import cn.com.kun.elasticsearch.entity.NbaPlayerDocBean;
import cn.com.kun.elasticsearch.service.INbaPlayerElasticService;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 *
 * 测试类
 *
 * @author xuyaokun
 * @date 2019/12/5 17:26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ElasticServiceImplTest {


    //自定义的业务层
    @Autowired
    private INbaPlayerElasticService elasticService;

    @Test
    public void createIndex(){
        elasticService.createIndex();
    }

    @Test
    public void deleteIndex(){
        elasticService.deleteIndex("indexName");
    }

    /**
     * 保存单个文档
     */
    @Test
    public void saveOne(){
        NbaPlayerDocBean docBean = new NbaPlayerDocBean();
        //假如不设置id,会默认生成
//        docBean.setId(11L);
        docBean.setType(111);
        docBean.setContent("11111111111string......");
        docBean.setFirstCode(docBean.getId() + "FirstCode");
        docBean.setSecordCode(docBean.getId() + "FirstCode");
        elasticService.save(docBean);
    }


    @Test
    public void saveAll(){
        List<NbaPlayerDocBean> list =new ArrayList<>();
        list.add(new NbaPlayerDocBean(1L,"XX0193","XX8064","xxxxxx",1));
        list.add(new NbaPlayerDocBean(2L,"XX0210","XX7475","xxxxxxxxxx",1));
        list.add(new NbaPlayerDocBean(3L,"XX0257","XX8097","xxxxxxxxxxxxxxxxxx",1));
        //可以不指定id,它会自己生成
        list.add(new NbaPlayerDocBean("XX0258","XX8097","xxxxxxxxxxxxxxxxxx",1));
        elasticService.saveAll(list);

    }

    @Test
    public void findAll(){
        Iterator<NbaPlayerDocBean> docBeanIterator = elasticService.findAll();
        //输出的就是一个json数组
        System.out.println(JSONObject.toJSONString(docBeanIterator));
    }

    @Test
    public void findByPage(){

        //分页默认从0页开始
        Pageable pageable = PageRequest.of(1, 3);//查询第1页，每页3条
        Page<NbaPlayerDocBean> docBeanPage = elasticService.queryAll(pageable);
        List<NbaPlayerDocBean> docBeanList = docBeanPage.getContent();
        long count = docBeanPage.getTotalElements();
        //输出的就是一个json数组
        System.out.println(JSONObject.toJSONString(docBeanPage));
    }

    @Test
    public void findByFirstCode(){

        //查询一个不存在的
//        Page<NbaPlayerDocBean> page = elasticService.findByFirstCode("xyk");

        //查询存在的
        Page<NbaPlayerDocBean> page = elasticService.findByFirstCode("XX0210");
        System.out.println();
    }

    @Test
    public void findByFirstCodeNoPage(){

        List<NbaPlayerDocBean> list = elasticService.findByFirstCodeNoPage("XX0210");
        System.out.println();
    }

    @Test
    public void queryByQueryBuilder(){

        Page<NbaPlayerDocBean> page = elasticService.queryByQueryBuilder();
        System.out.println(JSONObject.toJSONString(page));
    }


}
