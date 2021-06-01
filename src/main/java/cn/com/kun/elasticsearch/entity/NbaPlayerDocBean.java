package cn.com.kun.elasticsearch.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * shards：分片
 * replicas： 副本
 */
@Document(indexName = "nba-player", type = "_doc", shards = 1, replicas = 0, createIndex = false)
public class NbaPlayerDocBean {

    //加了@Id注解的，就会用这个属性的值作为 默认字段_id的值，同时它也会作为一个普通字段存到es中，名字就是id
    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String firstCode;

    @Field(type = FieldType.Keyword)
    private String secordCode;

    @Field(type = FieldType.Text, analyzer = "ik_max_word") //分词类型
    private String content;

    @Field(type = FieldType.Integer)
    private Integer type;

    public NbaPlayerDocBean() {
    }

    public NbaPlayerDocBean(String firstCode, String secordCode, String content, Integer type){
        this.firstCode=firstCode;
        this.secordCode=secordCode;
        this.content=content;
        this.type=type;
    }

    public NbaPlayerDocBean(Long id, String firstCode, String secordCode, String content, Integer type){
        this.id=id;
        this.firstCode=firstCode;
        this.secordCode=secordCode;
        this.content=content;
        this.type=type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstCode() {
        return firstCode;
    }

    public void setFirstCode(String firstCode) {
        this.firstCode = firstCode;
    }

    public String getSecordCode() {
        return secordCode;
    }

    public void setSecordCode(String secordCode) {
        this.secordCode = secordCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}