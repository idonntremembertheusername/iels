package com.xuecheng.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

/**
 * @Description: 测试ES的搜索功能
 * @Author: snypxk
 * @Date: 2019/12/9 21
 * @Other:
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSearch {

    //高级客户端
    @Autowired
    private RestHighLevelClient client;

    //低级客户端
    @Autowired
    private RestClient restClient;

    /*
     * @description: 搜索type下的全部记录
     * @author: snypxk
     * @param
     * @return: void
     **/
    @Test
    public void testSearchAll() throws IOException {
        //1.创建搜索请求对象:SearchRequest 并设定要索引的索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //2.设定type类型
        searchRequest.types("doc");
        //3.创建 搜索源构建对象:SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //4.指定搜索方式: matchAllQuery - 搜索全部/所有  ***********************
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //5.设置搜索: source源字段过虑
        //fetchSource(参数1,参数2): 参数1:结果集包含的字段数组  参数2:结果集不包含的字段数组
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "description"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        //6.执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        //7.得到返回结果集
        SearchHits hits = searchResponse.getHits();
        //命中的总文档记录数: getTotalHits
        long totalHits = hits.getTotalHits();
        System.out.println("匹配到的总记录数: " + totalHits);
        //得到匹配度高的文档记录数组[默认取前10份文档(如果命中的总文档记录数有超过10份)]
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            //文档所在索引库的索引库名称
            String index = hit.getIndex();
            //文档所在索引库的类型名称
            String type = hit.getType();
            //文档ID
            String id = hit.getId();
            //文档的匹配度得分
            float score = hit.getScore();
            //获取源文档内容: 以String格式返回
            String sourceAsString = hit.getSourceAsString();
            //获取源文档内容: 以Map格式返回
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }


    /*
     * @description: 搜索type下的全部记录 - 分页查询
     * @author: snypxk
     * @param
     * @return: void
     **/
    @Test
    public void testSearchAllByPage() throws IOException {
        //1.创建搜索请求对象:SearchRequest 并设定要索引的索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //2.设定type类型
        searchRequest.types("doc");
        //3.创建 搜索源构建对象:SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //-----------------------3.1设置分页参数 S-----------------------
        //页码
        int page = 1;
        //每页记录数
        int size = 1;
        //计算出起始记录下标
        int from = (page - 1) * size;
        //起始记录下标,从0起
        searchSourceBuilder.from(from);
        //每页显示的记录数
        searchSourceBuilder.size(size);
        //-----------------------3.1设置分页参数 E-----------------------
        //4.指定搜索方式: matchAllQuery - 搜索全部/所有  ***********************
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //5.设置搜索: source源字段过虑
        //fetchSource(参数1,参数2): 参数1:结果集包含的字段数组  参数2:结果集不包含的字段数组
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "description"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        //6.执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        //7.得到返回结果集
        SearchHits hits = searchResponse.getHits();
        //命中的总文档记录数: getTotalHits
        long totalHits = hits.getTotalHits();
        System.out.println("匹配到的总记录数: " + totalHits);
        //得到匹配度高的文档记录数组[默认在没有分页的情况下,会取前10份文档(如果命中的总文档记录数有超过10份)]
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            //文档所在索引库的索引库名称
            String index = hit.getIndex();
            //文档所在索引库的类型名称
            String type = hit.getType();
            //文档ID
            String id = hit.getId();
            //文档的匹配度得分
            float score = hit.getScore();
            //获取源文档内容: 以String格式返回
            String sourceAsString = hit.getSourceAsString();
            //获取源文档内容: 以Map格式返回
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }


    /*
     * @description: termQuery - 精确查询，在搜索时会整体匹配关键字，不再将关键字分词
     * @author: snypxk
     * @param
     * @return: void
     **/
    @Test
    public void testSearchByTerm() throws IOException {
        //1.创建搜索请求对象:SearchRequest 并设定要索引的索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //2.设定type类型
        searchRequest.types("doc");
        //3.创建 搜索源构建对象:SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //-----------------------3.1设置分页参数 S-----------------------
        //页码
        int page = 1;
        //每页记录数
        int size = 1;
        //计算出起始记录下标
        int from = (page - 1) * size;
        //起始记录下标,从0起
        searchSourceBuilder.from(from);
        //每页显示的记录数
        searchSourceBuilder.size(size);
        //-----------------------3.1设置分页参数 E-----------------------
        //4.指定搜索方式: termQuery ***********************
        searchSourceBuilder.query(QueryBuilders.termQuery("name", "spring"));
        //5.设置搜索: source源字段过虑
        //fetchSource(参数1,参数2): 参数1:结果集包含的字段数组  参数2:结果集不包含的字段数组
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "description"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        //6.执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        //7.得到返回结果集
        SearchHits hits = searchResponse.getHits();
        //命中的总文档记录数: getTotalHits
        long totalHits = hits.getTotalHits();
        System.out.println("匹配到的总记录数: " + totalHits);
        //得到匹配度高的文档记录数组[默认在没有分页的情况下,会取前10份文档(如果命中的总文档记录数有超过10份)]
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            //文档所在索引库的索引库名称
            String index = hit.getIndex();
            //文档所在索引库的类型名称
            String type = hit.getType();
            //文档ID
            String id = hit.getId();
            //文档的匹配度得分
            float score = hit.getScore();
            //获取源文档内容: 以String格式返回
            String sourceAsString = hit.getSourceAsString();
            //获取源文档内容: 以Map格式返回
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }


    /*
     * @description: termsQuery - 根据ID集合匹配查询
     * @author: snypxk
     * @param
     * @return: void
     **/
    @Test
    public void testSearchByIds() throws IOException {
        //1.创建搜索请求对象:SearchRequest 并设定要索引的索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //2.设定type类型
        searchRequest.types("doc");
        //3.创建 搜索源构建对象:SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //-----------------------3.1设置分页参数 S-----------------------
        //页码
        int page = 1;
        //每页记录数
        int size = 1;
        //计算出起始记录下标
        int from = (page - 1) * size;
        //起始记录下标,从0起
        searchSourceBuilder.from(from);
        //每页显示的记录数
        searchSourceBuilder.size(size);
        //-----------------------3.1设置分页参数 E-----------------------
        //4.指定搜索方式: termQuery **********************
        String[] ids = new String[]{"1", "2"};
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id", ids));
        //5.设置搜索: source源字段过虑
        //fetchSource(参数1,参数2): 参数1:结果集包含的字段数组  参数2:结果集不包含的字段数组
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "description"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        //6.执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        //7.得到返回结果集
        SearchHits hits = searchResponse.getHits();
        //命中的总文档记录数: getTotalHits
        long totalHits = hits.getTotalHits();
        System.out.println("匹配到的总记录数: " + totalHits);
        //得到匹配度高的文档记录数组[默认在没有分页的情况下,会取前10份文档(如果命中的总文档记录数有超过10份)]
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            //文档所在索引库的索引库名称
            String index = hit.getIndex();
            //文档所在索引库的类型名称
            String type = hit.getType();
            //文档ID
            String id = hit.getId();
            //文档的匹配度得分
            float score = hit.getScore();
            //获取源文档内容: 以String格式返回
            String sourceAsString = hit.getSourceAsString();
            //获取源文档内容: 以Map格式返回
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }


    /*
     * @description: match Query - 即全文检索，它的搜索方式是先将搜索字符串分词，再使用各各词条从索引中搜索
     * @author: snypxk
     * @param
     * @return: void
     *      match query与Term query区别是match query在搜索前先将搜索关键字分词，再拿各各词语去索引中搜索
     *
     * query：搜索的关键字，对于英文关键字如果有多个单词则中间要用半角逗号分隔，
     * 而对于中文关键字中间可以用逗号分隔也可以不用。
     *
     * operator：or  表示只要有一个词在文档中出现则就符合条件，
     * 	         and 表示每个词都在文档中出现则才符合条件。
     *
     * 上边的搜索的执行过程是：
     * 	1.将“spring开发”分词，分为spring、开发两个词
     * 	2.再使用spring和开发两个词去匹配索引中搜索。
     * 	3.由于设置了operator为or，只要有一个词匹配成功则就返回该文档。
     **/
    @Test
    public void testSearchByMatchQuery() throws IOException {
        //1.创建搜索请求对象:SearchRequest 并设定要索引的索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //2.设定type类型
        searchRequest.types("doc");
        //3.创建 搜索源构建对象:SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //-----------------------3.1设置分页参数 S-----------------------
        //页码
        int page = 1;
        //每页记录数
        int size = 1;
        //计算出起始记录下标
        int from = (page - 1) * size;
        //起始记录下标,从0起
        searchSourceBuilder.from(from);
        //每页显示的记录数
        searchSourceBuilder.size(size);
        //-----------------------3.1设置分页参数 E-----------------------
        //4.指定搜索方式: matchQuery **********************
        searchSourceBuilder.query(
                QueryBuilders.matchQuery("description", "spring开发").operator(Operator.OR));
        //5.设置搜索: source源字段过虑
        //fetchSource(参数1,参数2): 参数1:结果集包含的字段数组  参数2:结果集不包含的字段数组
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "description"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        //6.执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        //7.得到返回结果集
        SearchHits hits = searchResponse.getHits();
        //命中的总文档记录数: getTotalHits
        long totalHits = hits.getTotalHits();
        System.out.println("匹配到的总记录数: " + totalHits);
        //得到匹配度高的文档记录数组[默认在没有分页的情况下,会取前10份文档(如果命中的总文档记录数有超过10份)]
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            //文档所在索引库的索引库名称
            String index = hit.getIndex();
            //文档所在索引库的类型名称
            String type = hit.getType();
            //文档ID
            String id = hit.getId();
            //文档的匹配度得分
            float score = hit.getScore();
            //获取源文档内容: 以String格式返回
            String sourceAsString = hit.getSourceAsString();
            //获取源文档内容: 以Map格式返回
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }


    /*
     * @description: match Query - 使用 minimum_should_match 可以指定文档匹配词的占比
     * @author: snypxk
     * @param null
     * @return:
     *      比如搜索语句:  “spring开发框架”
     *          会被分为三个词：spring、开发、框架
     *              设置"minimum_should_match": "80%"表示，三个词在文档的匹配占比为80%，
     *          即3*0.8=2.4，向上取整得2，表示至少有两个词在文档中要匹配成功。
     **/
    @Test
    public void testSearchByMatchQuery_minimum_should_match() throws IOException {
        //1.创建搜索请求对象:SearchRequest 并设定要索引的索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //2.设定type类型
        searchRequest.types("doc");
        //3.创建 搜索源构建对象:SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //-----------------------3.1设置分页参数 S-----------------------
        //页码
        int page = 1;
        //每页记录数
        int size = 1;
        //计算出起始记录下标
        int from = (page - 1) * size;
        //起始记录下标,从0起
        searchSourceBuilder.from(from);
        //每页显示的记录数
        searchSourceBuilder.size(size);
        //-----------------------3.1设置分页参数 E-----------------------
        //4.指定搜索方式: matchQuery **********************
        searchSourceBuilder.query(QueryBuilders
                .matchQuery("description", "spring开发框架")
                .minimumShouldMatch("80%"));
        //5.设置搜索: source源字段过虑
        //fetchSource(参数1,参数2): 参数1:结果集包含的字段数组  参数2:结果集不包含的字段数组
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "description"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        //6.执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        //7.得到返回结果集
        SearchHits hits = searchResponse.getHits();
        //命中的总文档记录数: getTotalHits
        long totalHits = hits.getTotalHits();
        System.out.println("匹配到的总记录数: " + totalHits);
        //得到匹配度高的文档记录数组[默认在没有分页的情况下,会取前10份文档(如果命中的总文档记录数有超过10份)]
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            //文档所在索引库的索引库名称
            String index = hit.getIndex();
            //文档所在索引库的类型名称
            String type = hit.getType();
            //文档ID
            String id = hit.getId();
            //文档的匹配度得分
            float score = hit.getScore();
            //获取源文档内容: 以String格式返回
            String sourceAsString = hit.getSourceAsString();
            //获取源文档内容: 以Map格式返回
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }


    /*
     * @description: multiMatchQuery 和 提升boost
     * @author: snypxk
     * @param
     * @return: void
     *   multiMatchQuery:  termQuery 和 matchQuery 一次只能匹配一个Field, 而 multiMatchQuery 一次可以匹配多个字段。
     *   提升boost :  匹配多个字段时可以提升字段的 boost（权重）来提高得分
     *
     **/
    @Test
    public void testSearchBymultiQuery() throws IOException {
        //1.创建搜索请求对象:SearchRequest 并设定要索引的索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //2.设定type类型
        searchRequest.types("doc");
        //3.创建 搜索源构建对象:SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //-----------------------3.1设置分页参数 S-----------------------
        //页码
        int page = 1;
        //每页记录数
        int size = 1;
        //计算出起始记录下标
        int from = (page - 1) * size;
        //起始记录下标,从0起
        searchSourceBuilder.from(from);
        //每页显示的记录数
        searchSourceBuilder.size(size);
        //-----------------------3.1设置分页参数 E-----------------------
        //4.指定搜索方式: matchQuery **********************
        searchSourceBuilder.query(QueryBuilders
                .multiMatchQuery("spring css", "name", "description")
                .minimumShouldMatch("50%")
                .field("name", 10));
        //field("name", 10): 表示权重提升10倍，执行上边的查询，发现name中包括spring关键字的文档排在前边.
        //5.设置搜索: source源字段过虑
        //fetchSource(参数1,参数2): 参数1:结果集包含的字段数组  参数2:结果集不包含的字段数组
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "description"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        //6.执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        //7.得到返回结果集
        SearchHits hits = searchResponse.getHits();
        //命中的总文档记录数: getTotalHits
        long totalHits = hits.getTotalHits();
        System.out.println("匹配到的总记录数: " + totalHits);
        //得到匹配度高的文档记录数组[默认在没有分页的情况下,会取前10份文档(如果命中的总文档记录数有超过10份)]
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            //文档所在索引库的索引库名称
            String index = hit.getIndex();
            //文档所在索引库的类型名称
            String type = hit.getType();
            //文档ID
            String id = hit.getId();
            //文档的匹配度得分
            float score = hit.getScore();
            //获取源文档内容: 以String格式返回
            String sourceAsString = hit.getSourceAsString();
            //获取源文档内容: 以Map格式返回
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }


    /*
     * @description: 布尔查询BoolQuery - 布尔查询对应于Lucene的BooleanQuery查询，实现将多个查询组合起来
     * @author: snypxk
     * @param
     * @return: void
     *   三个参数：
     *      must    ：文档必须匹配must所包括的查询条件，相当于 “AND”
     *      should  ：文档应该匹配should所包括的查询条件其中的一个或多个，相当于 "OR"
     *      must_not：文档不能匹配must_not所包括的该查询条件，相当于“NOT”
     *
     *  BoolQuery - 将搜索关键字分词，拿分词去索引库搜索
     **/
    @Test
    public void testSearchByBoolQuery() throws IOException {
        //1.创建搜索请求对象:SearchRequest 并设定要索引的索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //2.设定type类型
        searchRequest.types("doc");
        //3.创建 搜索源构建对象:SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //-----------------------3.1设置分页参数 S-----------------------
        //页码
        int page = 1;
        //每页记录数
        int size = 1;
        //计算出起始记录下标
        int from = (page - 1) * size;
        //起始记录下标,从0起
        searchSourceBuilder.from(from);
        //每页显示的记录数
        searchSourceBuilder.size(size);
        //-----------------------3.1设置分页参数 E-----------------------
        //4.指定搜索方式: BoolQuery **********************
        //4.1.先定义一个MultiMatchQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders
                .multiMatchQuery("spring css", "name", "description")
                .minimumShouldMatch("50%")
                .field("name", 10);
        //4.2.再定义一个TermQuery
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studymodel", "201001");
        //4.3.定义一个BoolQuery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(multiMatchQueryBuilder);
        boolQueryBuilder.must(termQueryBuilder);

        searchSourceBuilder.query(boolQueryBuilder);
        //field("name", 10): 表示权重提升10倍，执行上边的查询，发现name中包括spring关键字的文档排在前边.
        //5.设置搜索: source源字段过虑
        //fetchSource(参数1,参数2): 参数1:结果集包含的字段数组  参数2:结果集不包含的字段数组
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "description"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        //6.执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        //7.得到返回结果集
        SearchHits hits = searchResponse.getHits();
        //命中的总文档记录数: getTotalHits
        long totalHits = hits.getTotalHits();
        System.out.println("匹配到的总记录数: " + totalHits);
        //得到匹配度高的文档记录数组[默认在没有分页的情况下,会取前10份文档(如果命中的总文档记录数有超过10份)]
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            //文档所在索引库的索引库名称
            String index = hit.getIndex();
            //文档所在索引库的类型名称
            String type = hit.getType();
            //文档ID
            String id = hit.getId();
            //文档的匹配度得分
            float score = hit.getScore();
            //获取源文档内容: 以String格式返回
            String sourceAsString = hit.getSourceAsString();
            //获取源文档内容: 以Map格式返回
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }


    /*
     * @description: 过虑 是针对搜索的结果进行过虑，过虑器主要判断的是文档是否匹配，不去计算和判断文档的匹配度得分,
     *               所以过虑器性能比查询要高，且方便缓存，推荐尽量使用过虑器去实现查询或者过虑器和查询共同使用.
     * @author: snypxk
     * @param
     * @return: void
     *      过虑器在布尔查询中使用
     *          range：范围过虑
     *          term ：项匹配过虑
     *          注意：range和term一次只能对一个Field设置范围过虑
     **/
    @Test
    public void testSearchByFilter_BasedOn_BoolQuery() throws IOException {
        //1.创建搜索请求对象:SearchRequest 并设定要索引的索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //2.设定type类型
        searchRequest.types("doc");
        //3.创建 搜索源构建对象:SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //-----------------------3.1设置分页参数 S-----------------------
        //页码
        int page = 1;
        //每页记录数
        int size = 1;
        //计算出起始记录下标
        int from = (page - 1) * size;
        //起始记录下标,从0起
        searchSourceBuilder.from(from);
        //每页显示的记录数
        searchSourceBuilder.size(size);
        //-----------------------3.1设置分页参数 E-----------------------
        //4.指定搜索方式: BoolQuery **********************
        //4.1.先定义一个MultiMatchQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders
                .multiMatchQuery("spring css", "name", "description")
                .minimumShouldMatch("50%")
                .field("name", 10);
        //不再需要 TermQuery, 将其改造成一个过滤器以使效率更高.
        //TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studymodel", "201001");
        //4.2.定义一个BoolQuery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);

        //4.3.定义两个过滤器: 保留studymodel等于"201001"的记录
        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel", "201001"));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));

        searchSourceBuilder.query(boolQueryBuilder);
        //field("name", 10): 表示权重提升10倍，执行上边的查询，发现name中包括spring关键字的文档排在前边.
        //5.设置搜索: source源字段过虑
        //fetchSource(参数1,参数2): 参数1:结果集包含的字段数组  参数2:结果集不包含的字段数组
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "description"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        //6.执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        //7.得到返回结果集
        SearchHits hits = searchResponse.getHits();
        //命中的总文档记录数: getTotalHits
        long totalHits = hits.getTotalHits();
        System.out.println("匹配到的总记录数: " + totalHits);
        //得到匹配度高的文档记录数组[默认在没有分页的情况下,会取前10份文档(如果命中的总文档记录数有超过10份)]
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            //文档所在索引库的索引库名称
            String index = hit.getIndex();
            //文档所在索引库的类型名称
            String type = hit.getType();
            //文档ID
            String id = hit.getId();
            //文档的匹配度得分
            float score = hit.getScore();
            //获取源文档内容: 以String格式返回
            String sourceAsString = hit.getSourceAsString();
            //获取源文档内容: 以Map格式返回
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }


    /*
     * @description: 可以在字段上添加一个或多个排序，支持在keyword、date、float等类型上添加.text类型的字段上不允许添加排序.
     * @author: snypxk
     * @param
     * @return: void
     **/
    @Test
    public void testSearchAndSort() throws IOException {
        //1.创建搜索请求对象:SearchRequest 并设定要索引的索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //2.设定type类型
        searchRequest.types("doc");
        //3.创建 搜索源构建对象:SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //-----------------------3.1设置分页参数 S-----------------------
        //页码
        int page = 1;
        //每页记录数
        int size = 1;
        //计算出起始记录下标
        int from = (page - 1) * size;
        //起始记录下标,从0起
        searchSourceBuilder.from(from);
        //每页显示的记录数
        searchSourceBuilder.size(size);
        //-----------------------3.1设置分页参数 E-----------------------
        //4.指定搜索方式: BoolQuery **********************
        //4.1.定义一个BoolQuery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(0).lte(100));
        searchSourceBuilder.query(boolQueryBuilder);

        //4.2.添加排序
        searchSourceBuilder.sort("studymodel", SortOrder.DESC);
        searchSourceBuilder.sort("price", SortOrder.ASC);

        //5.设置搜索: source源字段过虑
        //fetchSource(参数1,参数2): 参数1:结果集包含的字段数组  参数2:结果集不包含的字段数组
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "description"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        //6.执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        //7.得到返回结果集
        SearchHits hits = searchResponse.getHits();
        //命中的总文档记录数: getTotalHits
        long totalHits = hits.getTotalHits();
        System.out.println("匹配到的总记录数: " + totalHits);
        //得到匹配度高的文档记录数组[默认在没有分页的情况下,会取前10份文档(如果命中的总文档记录数有超过10份)]
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            //文档所在索引库的索引库名称
            String index = hit.getIndex();
            //文档所在索引库的类型名称
            String type = hit.getType();
            //文档ID
            String id = hit.getId();
            //文档的匹配度得分
            float score = hit.getScore();
            //获取源文档内容: 以String格式返回
            String sourceAsString = hit.getSourceAsString();
            //获取源文档内容: 以Map格式返回
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }


    /*
     * @description: 高亮显示highlight - 高亮显示可以将搜索结果一个或多个字突出显示，以便向用户展示匹配关键字的位置。
     * @author: snypxk
     * @param
     * @return: void
     **/
    @Test
    public void testSearchAndSort_Highlight() throws IOException {
        //1.创建搜索请求对象:SearchRequest 并设定要索引的索引库名称
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //2.设定type类型
        searchRequest.types("doc");
        //3.创建 搜索源构建对象:SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //-----------------------3.1设置分页参数 S-----------------------
        //页码
        int page = 1;
        //每页记录数
        int size = 1;
        //计算出起始记录下标
        int from = (page - 1) * size;
        //起始记录下标,从0起
        searchSourceBuilder.from(from);
        //每页显示的记录数
        searchSourceBuilder.size(size);
        //-----------------------3.1设置分页参数 E-----------------------
        //4.指定搜索方式: BoolQuery **********************
        //4.1.先定义一个MultiMatchQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders
                .multiMatchQuery("开发框架", "name", "description")
                .minimumShouldMatch("50%")
                .field("name", 10);
        //4.2.定义一个BoolQuery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);
        //4.3.定义一个范围过滤器
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(0).lte(100));
        searchSourceBuilder.query(boolQueryBuilder);
        //4.4.添加排序
        searchSourceBuilder.sort("studymodel", SortOrder.DESC);
        searchSourceBuilder.sort("price", SortOrder.ASC);

        //4.5.设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<tag>");
        highlightBuilder.postTags("</tag>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        //highlightBuilder.fields().add(new HighlightBuilder.Field("description"));
        searchSourceBuilder.highlighter(highlightBuilder);

        //5.设置搜索: source源字段过虑
        //fetchSource(参数1,参数2): 参数1:结果集包含的字段数组  参数2:结果集不包含的字段数组
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "description"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        //6.执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        //7.得到返回结果集
        SearchHits hits = searchResponse.getHits();
        //命中的总文档记录数: getTotalHits
        long totalHits = hits.getTotalHits();
        System.out.println("匹配到的总记录数: " + totalHits);
        //得到匹配度高的文档记录数组[默认在没有分页的情况下,会取前10份文档(如果命中的总文档记录数有超过10份)]
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            //文档所在索引库的索引库名称
            String index = hit.getIndex();
            //文档所在索引库的类型名称
            String type = hit.getType();
            //文档ID
            String id = hit.getId();
            //文档的匹配度得分
            float score = hit.getScore();
            //获取源文档内容: 以String格式返回
            String sourceAsString = hit.getSourceAsString();
            //获取源文档内容: 以Map格式返回
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            //取出高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields != null) {
                //取出name高亮字段
                HighlightField highlightField_name = highlightFields.get("name");
                if (highlightField_name != null) {
                    Text[] fragments = highlightField_name.getFragments();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text text :
                            fragments) {
                        stringBuffer.append(text);
                    }
                    name = stringBuffer.toString();
                }
            }
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }
}
