package com.springbootes;

import com.alibaba.fastjson.JSON;
import com.springbootes.pojo.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DemoApplication.class)
@Slf4j
public class DemoApplicationTests {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient client;

    @Test
    public void contextLoads() throws IOException {
        //1创建索引请求
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("kuang_index");
        //2执行请求
        CreateIndexResponse indexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println("indexResponse = " + indexResponse);
    }

    @Test
    void indexApi() throws IOException {
        //获取索引请求
        GetIndexRequest request = new GetIndexRequest("kuang_index");
        //根据该请求判断索引是否存在
        System.out.println(client.indices().exists(request, RequestOptions.DEFAULT));

        //删除一个索引请求
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("kuang_index");
        //发送请求
        AcknowledgedResponse delete = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        //查看是否删除成功
        System.out.println("delete = " + delete.isAcknowledged());
    }

    @Test
    void docApi() throws IOException {

        User user = new User("jlz", 2);
        //创建请求 指定索引库
        IndexRequest request = new IndexRequest("t1");
        //文档id
        request.id("2");
        //指定参数类型
        request.source(JSON.toJSONString(user), XContentType.JSON);
        IndexResponse index = client.index(request, RequestOptions.DEFAULT);
        log.info(index.toString());
        log.info(String.valueOf(index.status()));
    }

    @Test
    void getDoc() throws IOException {
        //get/doc/1
        GetRequest request = new GetRequest("t1", "2");
        //获取source上下文
//        request.fetchSourceContext(new FetchSourceContext(false));
//        配置排序
//        request.storedFields("age");
        boolean exists = client.exists(request, RequestOptions.DEFAULT);
        if (exists) {
            log.info("2号文档存在");
        }
//        //获取文档信息
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSource());
        //文档呢欧容
        System.out.println(response.getSourceAsString());
    }

    @Test
    void update() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("t1", "2");
        updateRequest.timeout("1s");
        User user = new User("jlz", 18);

        updateRequest.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse response = client.update(updateRequest, RequestOptions.DEFAULT);
        log.info("response = " + response.getResult());
        log.info("status = " + response.status());
    }

    @Test
    void delete() throws IOException {
        DeleteRequest request = new DeleteRequest("t1", "2");
        request.timeout("1s");
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        log.info("response = " + response.getResult());
        log.info("status = " + response.status());
    }

    @Test
    void getBatch() throws IOException {
        //批量操作 新增 删除 更新等都是一样 只是请求对象不一样
        BulkRequest bulkRequest = new BulkRequest();

        List<User> list = new ArrayList<>(10);
        list.add(new User("j", 1));
        list.add(new User("l", 2));
        list.add(new User("z", 3));
        list.add(new User("h", 4));
        list.add(new User("o", 5));
        list.forEach(user -> {
            bulkRequest.add(new IndexRequest("t1").id(user.getAge() + "").source(JSON.toJSONString(user), XContentType.JSON));
        });

        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        log.info(String.valueOf(bulk.hasFailures()));
    }

}
