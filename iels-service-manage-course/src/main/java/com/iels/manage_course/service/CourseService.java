package com.iels.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.iels.framework.domain.cms.CmsPage;
import com.iels.framework.domain.cms.response.CmsPageResult;
import com.iels.framework.domain.cms.response.CmsPostPageResult;
import com.iels.framework.domain.course.*;
import com.iels.framework.domain.course.ext.CategoryNode;
import com.iels.framework.domain.course.ext.CourseInfo;
import com.iels.framework.domain.course.ext.CourseView;
import com.iels.framework.domain.course.ext.TeachplanNode;
import com.iels.framework.domain.course.request.CourseListRequest;
import com.iels.framework.domain.course.request.CourseMarketEx;
import com.iels.framework.domain.course.response.CourseCode;
import com.iels.framework.domain.course.response.CoursePublishResult;
import com.iels.framework.exception.ExceptionCast;
import com.iels.framework.model.response.CommonCode;
import com.iels.framework.model.response.QueryResponseResult;
import com.iels.framework.model.response.QueryResult;
import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.utils.DateUtils;
import com.iels.manage_course.client.CmsPageClient;
import com.iels.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName: CourseService
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/11/28 22
 * @Other:
 **/
@Service
public class CourseService {

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TeachplanRepository teachplanRepository;

    @Autowired
    private CourseBaseRepository courseBaseRepository;

    @Autowired
    private CoursePicRepository coursePicRepository;

    @Autowired
    private CourseMarketRepository courseMarketRepository;

    @Autowired
    private CmsPageClient cmsPageClient;

    @Autowired
    private CoursePubRepository coursePubRepository;

    @Autowired
    private TeachplanMediaRepository teachplanMediaRepository;

    @Autowired
    private TeachplanMediaPubRepository teachplanMediaPubRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    /*
     * @description: 根据 课程id 查询课程计划
     * @author: snypxk
     * @param courseId
     * @return: com.iels.framework.domain.course.ext.TeachplanNode
     **/
    public TeachplanNode selectList(String courseId) {
        return teachplanMapper.selectList(courseId);
    }

    /*
     * @description: 添加课程计划
     * @author: snypxk
     * @date: 2019/11/29
     * @param teachplan
     * @return: com.xuecheng.framework.model.response.ResponseResult
     * @note:
     **/
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        if (teachplan == null
                || StringUtils.isEmpty(teachplan.getCourseid())
                || StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //课程ID
        String courseid = teachplan.getCourseid();
        //页面传入的父ID
        String parentid = teachplan.getParentid();
        //如果父ID为空,说明没有则说明新添加的课程计划属于二级章节
        if (StringUtils.isEmpty(parentid)) {
            //根据课程ID得到父结点ID
            parentid = this.getTeachplanRoot(courseid);
        }
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        Teachplan parentNode = optional.get();
        String grade = parentNode.getGrade();
        Teachplan teachplanNew = new Teachplan();
        BeanUtils.copyProperties(teachplan, teachplanNew);
        teachplanNew.setParentid(parentid);
        teachplanNew.setCourseid(courseid);
        if (grade.equals("1")) {
            teachplanNew.setGrade("2");
        } else {
            teachplanNew.setGrade("3");
        }
        teachplanRepository.save(teachplanNew);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //查询课程的根结点,如果查不到就要自动添加要结点
    private String getTeachplanRoot(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        //如果根据课程ID查找不到课程,说明这是第一次添加一级章节[即没有二级章节,也没有三级章节],一级章节就是课程的名称
        if (!optional.isPresent()) {
            return null;
        }
        //课程信息
        CourseBase courseBase = optional.get();
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        if (teachplanList == null || teachplanList.size() <= 0) {
            //如果查不到就要自动添加要根结点
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setPname(courseBase.getName());
            teachplan.setCourseid(courseId);
            teachplan.setStatus("0");
            teachplanRepository.save(teachplan);
            return teachplanList.get(0).getId();
        }
        return teachplanList.get(0).getId();
    }

    /*
     * @description: 添加课程图片
     * @author: snypxk
     * @param courseId
     * @param pic - 课程图片路径信息
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    @Transactional
    public ResponseResult addCoursePic(String courseId, String pic) {
        CoursePic coursePic = null;
        //先查询课程图片:
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if (optional.isPresent()) {
            //已经存在课程图片就更新
            coursePic = optional.get();
        }
        if (coursePic == null) {
            //没有就新添加
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /*
     * @description: 查找课程图片
     * @author: snypxk
     * @param courseId
     * @return: com.iels.framework.domain.course.CoursePic
     **/
    public CoursePic findCoursepic(String courseId) {
        Optional<CoursePic> optionalCoursePic = coursePicRepository.findById(courseId);
        if (optionalCoursePic.isPresent()) {
            CoursePic coursePic = optionalCoursePic.get();
            return coursePic;
        }
        return null;
    }

    /*
     * @description: 根据课程Id删除课程图片
     * @author: snypxk
     * @param courseId
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        Long result = coursePicRepository.deleteByCourseid(courseId);
        if (result > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /*
     * @description: 根据课程id获取四张表的信息,用于生成课程的视图
     * @author: snypxk
     * @param id - 课程id
     * @return: com.iels.framework.domain.course.ext.CourseView
     **/
    public CourseView getCourseView(String id) {
        CourseView courseView = new CourseView();
        //查询课程基本信息
        Optional<CourseBase> optional = courseBaseRepository.findById(id);
        if (optional.isPresent()) {
            CourseBase courseBase = optional.get();
            courseView.setCourseBase(courseBase);
        }
        //查询课程营销信息
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(id);
        if (courseMarketOptional.isPresent()) {
            CourseMarket courseMarket = courseMarketOptional.get();
            courseView.setCourseMarket(courseMarket);
        }
        //查询课程图片信息
        Optional<CoursePic> picOptional = coursePicRepository.findById(id);
        if (picOptional.isPresent()) {
            CoursePic coursePic = picOptional.get();
            courseView.setCoursePic(picOptional.get());
        }
        //查询课程计划信息
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    /*
     * @description: 根据id查询课程基本信息
     * @author: snypxk
     * @param courseId - 课程id
     * @return: com.iels.framework.domain.course.CourseBase
     **/
    public CourseBase findCourseBaseById(String courseId) {
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
        if (baseOptional.isPresent()) {
            CourseBase courseBase = baseOptional.get();
            return courseBase;
        }
        ExceptionCast.cast(CourseCode.COURSE_GET_NOT_EXISTS);
        return null;
    }

    /*
     * @description: 课程预览
     * @author: snypxk
     * @param id - 课程id
     * @return: com.iels.framework.domain.course.response.CoursePublishResult
     **/
    public CoursePublishResult preview(String id) {
        //请求CMS服务来添加页面
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setDataUrl(publish_dataUrlPre + id);
        cmsPage.setPageName(id + ".html");
        CourseBase courseBase = this.findCourseBaseById(id);
        cmsPage.setPageAliase(courseBase.getName());
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageWebPath(publish_page_webpath);
        //远程调用CMS服务
        CmsPageResult cmsPageResult = cmsPageClient.saveCmsPage(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            //保存不成功
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        //拼装页面预览的URL
        String previewPageURL = previewUrl + pageId;
        return new CoursePublishResult(CommonCode.SUCCESS, previewPageURL);
    }

    /*
     * @description: 一键发布课程-- 2019/12/5
     * @author: snypxk
     * @param courseId
     * @return: com.iels.framework.domain.course.response.CoursePublishResult
     **/
    @Transactional
    public CoursePublishResult publish(String courseId) {
        //课程信息
        CourseBase one = this.findCourseBaseById(courseId);
        //发布课程详情页面
        CmsPostPageResult cmsPostPageResult = this.publish_page(courseId);
        if (!cmsPostPageResult.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //更新课程状态
        CourseBase courseBase = this.saveCoursePubState(courseId);
        //保存课程索引信息
        //先创建一个CoursePub对象
        CoursePub coursePub = this.createCoursePub(courseId);
        //把CoursePub对象保存到数据库
        this.saveCoursePub(courseId, coursePub);

        //课程缓存...
        //页面url
        String pageUrl = cmsPostPageResult.getPageUrl();
        //向表[teachplan_media_pub]保存课程的媒资信息
        this.saveTeachplanMediaPub(courseId);
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }

    /*
     * @description: 向表[teachplan_media_pub]保存课程的媒资信息
     * @author: snypxk
     * @param courseId - 课程id
     * @return: void
     * 课程媒资信息是在课程发布的时候存入ElasticSearch索引库,因为课程发布后课程信息将基本不再修改.
     * 具体的业务流程如下:
     * 1、课程发布,向课程媒资信息表写入数据
     *    1）根据课程id删除表[teachplan_media_pub]中的数据
     *    2）根据课程id查询[teachplan_media]数据
     *    3）将查询到的[teachplan_media]数据插入到表[teachplan_media_pub]中
     * 2、Logstash定时扫描课程媒资信息表,并将课程媒资信息写入索引库
     **/
    private void saveTeachplanMediaPub(String courseId) {
        //查询课程媒资信息
        List<TeachplanMedia> teachplanMediaList = teachplanMediaRepository.findByCourseId(courseId);
        //将课程计划的媒资信息存储待索引的表[teachplan_media_pub]
        teachplanMediaPubRepository.deleteByCourseId(courseId);
        List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();
        for (TeachplanMedia teachplanMedia : teachplanMediaList) {
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia, teachplanMediaPub);
            //添加时间戳
            teachplanMediaPub.setTimestamp(new Date());
            teachplanMediaPubList.add(teachplanMediaPub);
        }
        teachplanMediaPubRepository.saveAll(teachplanMediaPubList);
    }

    /*
     * @description: 创建CoursePub对象
     * @author: snypxk
     * @param id - 课程id
     * @return: com.xuecheng.framework.domain.course.CoursePub
     **/
    private CoursePub createCoursePub(String id) {
        CoursePub coursePub = new CoursePub();
        //根据id查询course_base
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(id);
        if (courseBaseOptional.isPresent()) {
            CourseBase courseBase = courseBaseOptional.get();
            //将CourseBase的属性拷贝到CoursePub对应的属性
            BeanUtils.copyProperties(courseBase, coursePub);
        }
        //根据id查询course_pic
        Optional<CoursePic> coursePicOptional = coursePicRepository.findById(id);
        if (coursePicOptional.isPresent()) {
            CoursePic coursePic = coursePicOptional.get();
            //将CoursePic的属性拷贝到CoursePub对应的属性
            BeanUtils.copyProperties(coursePic, coursePub);
        }
        //根据id查询course_market
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(id);
        if (courseMarketOptional.isPresent()) {
            CourseMarket courseMarket = courseMarketOptional.get();
            //将CourseMarket的属性拷贝到CoursePub对应的属性
            BeanUtils.copyProperties(courseMarket, coursePub);
        }
        //根据id查询teachplan
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        String jsonString = JSON.toJSONString(teachplanNode);
        //将课程计划信息JSON串保存到 course_pub 中
        coursePub.setTeachplan(jsonString);
        return coursePub;
    }

    /*
     * @description: 把CoursePub对象保存到数据库
     * @author: snypxk
     * @param courseId
     * @param coursePub
     * @return: com.xuecheng.framework.domain.course.CoursePub
     **/
    private CoursePub saveCoursePub(String courseId, CoursePub coursePub) {
        CoursePub coursePubNew = null;
        //先根据courseId查询
        Optional<CoursePub> coursePubOptional = coursePubRepository.findById(courseId);
        if (coursePubOptional.isPresent()) {
            coursePubNew = coursePubOptional.get();
        } else {
            coursePubNew = new CoursePub();
        }
        BeanUtils.copyProperties(coursePub, coursePubNew);
        //设置课程ID
        coursePubNew.setId(courseId);
        //时间戳: 给logstash使用
        coursePubNew.setTimestamp(new Date());
        //发布时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        coursePubNew.setPubTime(date);
        coursePubRepository.save(coursePubNew);
        return coursePubNew;
    }


    //更新课程发布状态
    private CourseBase saveCoursePubState(String courseId) {
        CourseBase courseBase = this.findCourseBaseById(courseId);
        //更新发布状态: 已经发布202002
        courseBase.setStatus("202002");
        return courseBaseRepository.save(courseBase);
    }

    //发布课程的正式页面
    private CmsPostPageResult publish_page(String courseId) {
        CourseBase one = this.findCourseBaseById(courseId);
        //发布课程预览页面
        CmsPage cmsPage = new CmsPage();
        //站点
        cmsPage.setSiteId(publish_siteId);//课程预览站点
        //模板
        cmsPage.setTemplateId(publish_templateId);
        //页面名称
        cmsPage.setPageName(courseId + ".html");
        //页面别名
        cmsPage.setPageAliase(one.getName());
        //页面访问路径
        cmsPage.setPageWebPath(publish_page_webpath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //数据url
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);
        //远程调用服务cmsPageClient,进行发布页面
        return cmsPageClient.postPageQuick(cmsPage);
    }

    /*
     * @description: 保存媒资信息
     * @author: snypxk
     * @param teachplanMedia
     * @return: com.xuecheng.framework.model.response.ResponseResult
     **/
    public ResponseResult savemedia(TeachplanMedia teachplanMedia) {
        if (teachplanMedia == null || StringUtils.isEmpty(teachplanMedia.getTeachplanId())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //课程计划
        String teachplanId = teachplanMedia.getTeachplanId();
        //查询课程计划
        Optional<Teachplan> optional = teachplanRepository.findById(teachplanId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_ISNULL);
        }
        Teachplan teachplan = optional.get();
        //只允许为叶子结点课程计划[三级课程计划]选择视频
        String grade = teachplan.getGrade();
        if (StringUtils.isEmpty(grade) || !grade.equals("3")) {
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_GRADEERROR);
        }
        TeachplanMedia one = null;
        Optional<TeachplanMedia> teachplanMediaOptional = teachplanMediaRepository.findById(teachplanId);
        //      if (!teachplanMediaOptional.isPresent()) {
        //            one = new TeachplanMedia();
        //        } else {
        //            one = teachplanMediaOptional.get();
        //        }
        //                  <=====>
        // one = teachplanMediaOptional.orElseGet(TeachplanMedia::new);
        one = teachplanMediaOptional.orElseGet(TeachplanMedia::new);
        //保存媒资信息与课程计划信息
        one.setTeachplanId(teachplanId);
        one.setCourseId(teachplanMedia.getCourseId());
        one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());
        one.setMediaId(teachplanMedia.getMediaId());
        one.setMediaUrl(teachplanMedia.getMediaUrl());
        teachplanMediaRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /*
     * @description: 据 companyId 和 userId 和 关键字 查询课程列表
     * @author: snypxk
     * @param companyId - 公司或机构id
     * @param userId    - 用户id(在controller中用工具类IelsOauth2Util从请求头中获取)
     * @param page
     * @param size
     * @param courseListRequest - 关键字
     * @return: com.iels.framework.model.response.QueryResponseResult<com.iels.framework.domain.course.ext.CourseInfo>
     **/
    public QueryResponseResult<CourseInfo> findCourseList(String companyId,
                                                          String userId,
                                                          int page, int size,
                                                          CourseListRequest courseListRequest) {
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        //将公司id参数传入
        courseListRequest.setCompanyId(companyId);
        courseListRequest.setUserId(userId);
        if (StringUtils.isNotEmpty(courseListRequest.getName())) {
            courseListRequest.setName("%" + courseListRequest.getName() + "%");
        } else {
            courseListRequest.setName(null);
        }
        //调用DAO
        if (page <= 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 4;
        }
        PageHelper.startPage(page, size);
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);
        List<CourseInfo> list = courseListPage.getResult();
        long total = courseListPage.getTotal();
        QueryResult<CourseInfo> courseIncfoQueryResult = new QueryResult<CourseInfo>();
        courseIncfoQueryResult.setList(list);
        courseIncfoQueryResult.setTotal(total);
        return new QueryResponseResult<CourseInfo>(CommonCode.SUCCESS, courseIncfoQueryResult);
    }

    /*
     * @description: 获取所有的课程分类列表数据
     * @author: snypxk
     * @param
     * @return: com.iels.framework.domain.course.ext.CategoryNode
     **/
    public CategoryNode getAllCategory() {
        return categoryMapper.getAllCategory();
    }

    /*
     * @description: 根据课程id查询课程基本信息
     * @author: snypxk
     * @param courseId
     * @return: com.iels.framework.domain.course.CourseBase
     **/
    public CourseBase getCourseBaseInfo(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (optional.isPresent()) {
            CourseBase courseBase = optional.get();
            String mt = courseBase.getMt();
            String st = courseBase.getSt();
            Optional<Category> categoryMt = categoryRepository.findById(mt);
            Optional<Category> categorySt = categoryRepository.findById(st);
            categoryMt.ifPresent(category -> courseBase.setMtName(category.getName()));
            categorySt.ifPresent(category -> courseBase.setStName(category.getName()));
            return courseBase;
        }
        return null;
    }

    /*
     * @description: 根据课程id更新课程基础信息
     * @author: snypxk
     * @param courseBase
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    public ResponseResult updateCourseBaseInfo(CourseBase courseBase) {
        if (courseBase != null && StringUtils.isNotEmpty(courseBase.getId())) {
            courseBaseRepository.save(courseBase);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    public CourseMarket getCourseMarketInfo(String courseId) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        return optional.orElse(null);
    }

    public ResponseResult updateCourseMarketInfo(CourseMarketEx courseMarketex) {
        CourseMarket courseMarket = new CourseMarket();
        courseMarket.setId(courseMarketex.getId());
        courseMarket.setCharge(courseMarketex.getCharge());
        courseMarket.setPrice(courseMarketex.getPrice());
        courseMarket.setPrice_old(courseMarketex.getPrice_old());
        courseMarket.setQq(courseMarketex.getQq());
        courseMarket.setValid(courseMarketex.getValid());
        courseMarket.setStartTime(DateUtils.parse(courseMarketex.getStartTime()));
        courseMarket.setEndTime(DateUtils.parse(courseMarketex.getEndTime()));
        courseMarketRepository.save(courseMarket);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public TeachplanNode getParentTeachplanList(String courseId) {
        TeachplanNode teachplanNode = teachplanMapper.selectParentList(courseId);
        if (teachplanNode != null) {
            return teachplanNode;
        }
        return null;
    }

    /*
     * @description: 后端不作任何处理,直接保存或更新前台传过来的Teachplan
     * @author: snypxk
     * @param teachplan
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    public ResponseResult addTeachPlan(Teachplan teachplan) {
        Teachplan save = teachplanRepository.save(teachplan);
        if (save != null) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /*
     * @description: 根据课程计划id以及课程等级来删除课程计划
     * @author: snypxk
     * @param teachplanId
     * @param grade
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    @Transactional
    public ResponseResult deleteTeachPlan(String teachplanId, String grade) {
        try {
            teachplanRepository.deleteById(teachplanId);
            if (grade.equals("1") || grade.equals("2")) {
                teachplanRepository.deleteAllByParentid(teachplanId);
            }
            return new ResponseResult(CommonCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(CommonCode.FAIL);
        }
    }

    /*
     * @description: 添加新课程-为课程添加基本信息
     * @author: snypxk
     * @param courseBase
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    @Transactional
    public ResponseResult addCourseBase(CourseBase courseBase) {

        try {
            courseBaseRepository.save(courseBase);
            return new ResponseResult(CommonCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(CommonCode.FAIL);
        }
    }

    /*
     * @description: 首页课程信息
     * @author: thesky
     * @param courseListRequest
     * @return: QueryResponseResult<com.xuecheng.framework.domain.course.ext.CourseInfo>
     **/


    public QueryResponseResult findAllCourse(CourseListRequest courseListRequest){
        if(courseListRequest == null){
            courseListRequest = new CourseListRequest();
        }
        List<CourseInfo> list=courseMapper.findAllCourse();

        QueryResult<CourseInfo> courseInfoQueryResult = new QueryResult<CourseInfo>();
        courseInfoQueryResult.setList(list);
        return new QueryResponseResult(CommonCode.SUCCESS,courseInfoQueryResult);

    }
}
