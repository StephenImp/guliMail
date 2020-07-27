package com.atguigu.gulimall.search;

import cn.hutool.json.JSONUtil;
import com.atguigu.gulimall.search.pojo.Account;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

/**
 * 描述： es测试类 <br>
 * CreateDate: 2020/5/29 <br>
 * @author huaguoguo
 */
@Slf4j
public class ElasticSearchTests {

    public static final RequestOptions COMMON_OPTIONS;
    private static RestHighLevelClient client;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        COMMON_OPTIONS = builder.build();
        client = new RestHighLevelClient(RestClient.builder(new HttpHost("192.168.199.206", 9200, "http")));
    }

    /**
     * 基本测试
     * @throws IOException
     */
    @Test
    public void testIndex() throws IOException {
        IndexRequest request = new IndexRequest("users");
        User user = new User();
        user.name = "荣桑";
        user.age = 18;
        String jsonStr = JSONUtil.toJsonStr(user);
        request.id("1");
        request.source(jsonStr, XContentType.JSON);
        IndexResponse response = client.index(request, COMMON_OPTIONS);
        log.info(response.toString());
    }

    @Data
    class User {
        private String name;
        private Integer age;
    }

    /**
     * search测试
     * @throws IOException
     */
    @Test
    public void testSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("bank");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("address","mill"));
        searchSourceBuilder.aggregation(AggregationBuilders.terms("ageAgg").field("age").size(10));
        searchSourceBuilder.aggregation(AggregationBuilders.avg("ageAvg").field("age"));
        searchRequest.source(searchSourceBuilder);
        log.info(searchSourceBuilder.toString());
        SearchResponse response = client.search(searchRequest, COMMON_OPTIONS);
        log.info(response.toString());
        // 从结果中拿到 hit中的 account数据
        SearchHit[] searchHits = response.getHits().getHits();
        for (SearchHit searchHit : searchHits) {
            String source = searchHit.getSourceAsString();
            Account account = JSONUtil.toBean(source, Account.class);
            log.info(account.toString());
        }
        // 拿到聚合的结果
        Terms ageAgg = response.getAggregations().get("ageAgg");
        ageAgg.getBuckets().forEach(bucker -> {
            log.info(bucker.getKeyAsString());
        });
        Avg avg = response.getAggregations().get("ageAvg");
        log.info("平均值： {}",avg.getValue());
    }

}
