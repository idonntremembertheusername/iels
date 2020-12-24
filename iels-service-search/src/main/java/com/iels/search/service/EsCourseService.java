package com.iels.search.service;

import com.iels.framework.domain.course.CoursePub;
import com.iels.framework.domain.course.TeachplanMediaPub;
import com.iels.framework.domain.search.CourseSearchParam;
import com.iels.framework.model.response.CommonCode;
import com.iels.framework.model.response.QueryResponseResult;
import com.iels.framework.model.response.QueryResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 搜索服务
 * @Author: snypxk
 * @Date: 2019/12/10 22
 * @Other:
 **/
@Service
@Slf4j
public class EsCourseService {

    @Value("${iels.course.index}")
    private String es_index;
    @Value("${iels.course.type}")
    private String es_type;
    @Value("${iels.course.source_field}")
    private String source_field;

    @Value("${iels.media.index}")
    private String media_index;
    @Value("${iels.media.type}")
    private String media_type;
    @Value("${iels.media.source_field}")
    private String media_field;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /*
     * @description: 课程搜索
     * @author: snypxk
     * @param page - 当前页码
     * @param size - 每页显示记录数
     * @param courseSearchParam - 搜索对象参数
     * @return: com.xuecheng.framework.model.response.QueryResponseResult<com.xuecheng.framework.domain.course.CoursePub>
     **/
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam) {
        //设置索引
        SearchRequest searchRequest = new SearchRequest(es_index);
        //设置类型
        searchRequest.types(es_type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //source源字段过虑
        String[] source_fields = source_field.split(",");
        //fetchSource(参数1,参数2): 参数1:结果集包含的字段数组  参数2:结果集不包含的字段数组
        searchSourceBuilder.fetchSource(source_fields, new String[]{});
        //按"关键字"搜索: 先把关键字分词,然后把分词结果分别在三个Fields: name, teachplan, description中进行搜索匹配
        if (StringUtils.isNotEmpty(courseSearchParam.getKeyword())) {
            //匹配关键字
            MultiMatchQueryBuilder multiMatchQueryBuilder =
                    QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(),
                            "name", "teachplan", "description");
            //设置匹配占比:关键字分词后匹配度要达到70%
            multiMatchQueryBuilder.minimumShouldMatch("70%");
            //提升一个字段的Boost值: 把在name中匹配的结果得分提高
            multiMatchQueryBuilder.field("name", 10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getMt())) {
            //根据一级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt", courseSearchParam.getMt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getSt())) {
            //根据二级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("st", courseSearchParam.getSt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getGrade())) {
            //根据难度等级
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade", courseSearchParam.getGrade()));
        }

        //布尔查询
        searchSourceBuilder.query(boolQueryBuilder);

        //设置分页参数
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 12;
        }
        int from = (page - 1) * size;
        searchSourceBuilder.from(from);//起始记录下标
        searchSourceBuilder.size(size);

        //设置高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        //设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);

        //请求搜索
        searchRequest.source(searchSourceBuilder);
        //响应结果对象
        QueryResult<CoursePub> queryResult = new QueryResult<>();
        //数据列表
        List<CoursePub> list = new ArrayList<>();
        SearchResponse searchResponse = null;
        try {
            //执行搜索
            searchResponse = restHighLevelClient.search(searchRequest);
            //结果集处理
            SearchHits hits = searchResponse.getHits();
            //当前页对应的搜索记录
            SearchHit[] searchHits = hits.getHits();
            //记录总数
            long totalHits = hits.getTotalHits();
            queryResult.setTotal(totalHits);
            for (SearchHit hit : searchHits) {
                CoursePub coursePub = new CoursePub();
                //取出source: 以map的格式取出
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //取出课程ID
                String id = (String) sourceAsMap.get("id");
                coursePub.setId(id);
                //记录的名称[课程名称]
                String name = (String) sourceAsMap.get("name");

                //取出高亮字段name
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                if (highlightFields != null) {
                    HighlightField highlightFieldName = highlightFields.get("name");
                    if (highlightFieldName != null) {
                        Text[] fragments = highlightFieldName.fragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        for (Text text :
                                fragments) {
                            stringBuffer.append(text);
                        }
                        name = stringBuffer.toString();
                    }
                }

                coursePub.setName(name);
                //图片
                String pic = (String) sourceAsMap.get("pic");
                coursePub.setPic(pic);
                //价格
                Double price = null;
                try {
                    if (sourceAsMap.get("price") != null) {
                        price = (Double) sourceAsMap.get("price");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                coursePub.setPrice(price);
                //旧价格
                Double price_old = null;
                try {
                    if (sourceAsMap.get("price_old") != null) {
                        price_old = (Double) sourceAsMap.get("price_old");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                coursePub.setPrice_old(price_old);
                list.add(coursePub);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("xuecheng search error..{}", e.getMessage());
        }
        queryResult.setList(list);
        return new QueryResponseResult<CoursePub>(CommonCode.SUCCESS, queryResult);
    }

    /*
     * @description: 查询课程所有信息返回给前端
     * @author: snypxk
     * @param id - 课程id
     * @return: java.util.Map<java.lang.String,com.xuecheng.framework.domain.course.CoursePub>
     **/
    public Map<String, CoursePub> getall(String id) {
        //设置索引库
        SearchRequest searchRequest = new SearchRequest(es_index);
        //设置类型
        searchRequest.types(es_type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //查询条件，根据课程id查询
        searchSourceBuilder.query(QueryBuilders.termsQuery("id", id));
        //取消source源字段过虑，查询所有字段
        // searchSourceBuilder.fetchSource(new String[]{"name", "grade", "charge","pic"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            //执行搜索
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取搜索结果
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        Map<String, CoursePub> map = new HashMap<>();
        for (SearchHit hit : searchHits) {
            String courseId = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String courseid = (String) sourceAsMap.get("id");
            String name = (String) sourceAsMap.get("name");
            String grade = (String) sourceAsMap.get("grade");
            String charge = (String) sourceAsMap.get("charge");
            String pic = (String) sourceAsMap.get("pic");
            String description = (String) sourceAsMap.get("description");
            String teachplan = (String) sourceAsMap.get("teachplan");
            CoursePub coursePub = new CoursePub();
            coursePub.setId(courseid);
            coursePub.setName(name);
            coursePub.setCharge(charge);
            coursePub.setPic(pic);
            coursePub.setGrade(grade);
            coursePub.setTeachplan(teachplan);
            coursePub.setDescription(description);
            map.put(courseid, coursePub);
        }
        return map;
    }

    /*
     * @description: 根据 多个课程计划ID数 [课程计划ID数组] 查询课程媒资信息
     * @author: snypxk
     * @param teachplanIds - 课程计划ID数组
     * @return: com.xuecheng.framework.model.response.QueryResponseResult<com.xuecheng.framework.domain.course.TeachplanMediaPub>
     **/
    public QueryResponseResult<TeachplanMediaPub> getmedia(String[] teachplanIds) {
        //设置索引库
        SearchRequest searchRequest = new SearchRequest(media_index);
        //设置类型
        searchRequest.types(media_type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //查询条件，根据 课程计划id数组 查询
        searchSourceBuilder.query(QueryBuilders.termsQuery("teachplan_id", teachplanIds));
        //source源字段过虑，查询所有字段
        String[] fields = media_field.split(",");
        searchSourceBuilder.fetchSource(fields, new String[]{});
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            //执行搜索
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取搜索结果
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        //数据列表
        List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //取出课程计划媒资信息
            String courseid = (String) sourceAsMap.get("courseid");
            String media_id = (String) sourceAsMap.get("media_id");
            String media_url = (String) sourceAsMap.get("media_url");
            String teachplan_id = (String) sourceAsMap.get("teachplan_id");
            String media_fileoriginalname = (String) sourceAsMap.get("media_fileoriginalname");

            teachplanMediaPub.setCourseId(courseid);
            teachplanMediaPub.setMediaUrl(media_url);
            teachplanMediaPub.setMediaFileOriginalName(media_fileoriginalname);
            teachplanMediaPub.setMediaId(media_id);
            teachplanMediaPub.setTeachplanId(teachplan_id);
            //将数据加入列表
            teachplanMediaPubList.add(teachplanMediaPub);
        }
        //构建返回课程媒资信息对象
        QueryResult<TeachplanMediaPub> queryResult = new QueryResult<>();
        queryResult.setList(teachplanMediaPubList);
        return new QueryResponseResult<TeachplanMediaPub>(CommonCode.SUCCESS, queryResult);
    }


    public QueryResponseResult<CoursePub> wechatList(CourseSearchParam courseSearchParam) {
        //设置索引
        SearchRequest searchRequest = new SearchRequest(es_index);
        //设置类型
        searchRequest.types(es_type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //source源字段过虑
        String[] source_fields = source_field.split(",");
        //fetchSource(参数1,参数2): 参数1:结果集包含的字段数组  参数2:结果集不包含的字段数组
        searchSourceBuilder.fetchSource(source_fields, new String[]{});
        //按"关键字"搜索: 先把关键字分词,然后把分词结果分别在三个Fields: name, teachplan, description中进行搜索匹配
        if (StringUtils.isNotEmpty(courseSearchParam.getKeyword())) {
            //匹配关键字
            MultiMatchQueryBuilder multiMatchQueryBuilder =
                    QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(),
                            "name", "teachplan", "description");
            //设置匹配占比:关键字分词后匹配度要达到70%
            multiMatchQueryBuilder.minimumShouldMatch("70%");
            //提升一个字段的Boost值: 把在name中匹配的结果得分提高
            multiMatchQueryBuilder.field("name", 10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getMt())) {
            //根据一级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt", courseSearchParam.getMt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getSt())) {
            //根据二级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("st", courseSearchParam.getSt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getGrade())) {
            //根据难度等级
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade", courseSearchParam.getGrade()));
        }

        //布尔查询
        searchSourceBuilder.query(boolQueryBuilder);



        //设置高亮显示
//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        highlightBuilder.preTags("<font class='eslight'>");
//        highlightBuilder.postTags("</font>");
//        //设置高亮字段
//        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
//        searchSourceBuilder.highlighter(highlightBuilder);

        //请求搜索
        searchRequest.source(searchSourceBuilder);
        //响应结果对象
        QueryResult<CoursePub> queryResult = new QueryResult<>();
        //数据列表
        List<CoursePub> list = new ArrayList<>();
        SearchResponse searchResponse = null;
        try {
            //执行搜索
            searchResponse = restHighLevelClient.search(searchRequest);
            //结果集处理
            SearchHits hits = searchResponse.getHits();
            //当前页对应的搜索记录
            SearchHit[] searchHits = hits.getHits();
            //记录总数
            long totalHits = hits.getTotalHits();
            queryResult.setTotal(totalHits);
            for (SearchHit hit : searchHits) {
                CoursePub coursePub = new CoursePub();
                //取出source: 以map的格式取出
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //取出课程ID
                String id = (String) sourceAsMap.get("id");
                coursePub.setId(id);
                //记录的名称[课程名称]
                String name = (String) sourceAsMap.get("name");

                //取出高亮字段name
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                if (highlightFields != null) {
                    HighlightField highlightFieldName = highlightFields.get("name");
                    if (highlightFieldName != null) {
                        Text[] fragments = highlightFieldName.fragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        for (Text text :
                                fragments) {
                            stringBuffer.append(text);
                        }
                        name = stringBuffer.toString();
                    }
                }

                coursePub.setName(name);
                //图片
                String pic = (String) sourceAsMap.get("pic");
                coursePub.setPic(pic);
                //价格
                Double price = null;
                try {
                    if (sourceAsMap.get("price") != null) {
                        price = (Double) sourceAsMap.get("price");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                coursePub.setPrice(price);
                //旧价格
                Double price_old = null;
                try {
                    if (sourceAsMap.get("price_old") != null) {
                        price_old = (Double) sourceAsMap.get("price_old");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                coursePub.setPrice_old(price_old);
                list.add(coursePub);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("xuecheng search error..{}", e.getMessage());
        }
        queryResult.setList(list);
        return new QueryResponseResult<CoursePub>(CommonCode.SUCCESS, queryResult);
    }
}
