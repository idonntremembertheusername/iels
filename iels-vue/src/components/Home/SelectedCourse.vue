<template>
  <div>
    <div class="container-1200">
      <div class="mod-sub">
        <div class="container-1200">
          <h2>IT互联网热门课程</h2>
        </div>
        <div class="hover-expand">
          <div class="container-1200">
            <div class="custom-course-box">
              <div class="custom-course-list">
                <ul class="course-card-list">
                  <li
                    class="course-card-item"
                    v-for="(item,index) in courseList.slice(0,maxSize)"
                    :key="index" @click="goPurchase()">
                    <a href="#" class="item-img-link">
                      <img :src="item.pic" alt="图片不存在" class="item-img"/>
                    </a>
                    <div class="changecolor">
                      <h4 class="item-tt">
                        <a href="#" class="item-tt-link">{{item.name}}</a>
                      </h4>
                       <div class="item-line item-line--middle">
                        <span class="line-cell item-task">共{{item.description}}课时</span>
                        <a
                          href="#"
                          class="line-cell item-source-link"
                        >{{item.description|fenge}}</a>
                      </div>
                      <div class="item-line item-line--bottom">
                        <!-- 将来改成课程等级 -->
                        <span class="line-cell item-price">火热</span>
                        <span class="line-cell item-user">{{item.description}}人最近报名</span>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- <el-container>

    </el-container>-->
    <div class="container-1200">
      <div class="mod-sub">
        <div class="container-1200">
          <h2>编程入门</h2>
        </div>
        <div class="hover-expand">
          <div class="container-1200">
            <div class="custom-course-box">
              <div class="custom-course-list">
                <ul class="course-card-list">
                  <li
                    class="course-card-item"
                    v-for="(item,index) in courseList.slice(1*maxSize,2*maxSize)"
                    :key="index"
                  >
                    <a href="#" class="item-img-link">
                      <img :src="item.pic" alt="图片不存在" class="item-img"/>
                    </a>
                    <div class="changecolor">
                      <h4 class="item-tt">
                        <a href="#" class="item-tt-link">{{item.name}}</a>
                      </h4>
                      <div class="item-line item-line--middle">
                        <span class="line-cell item-task">共{{item.description}}课时</span>
                        <a
                          href="#"
                          class="line-cell item-source-link"
                        >{{item.description|fenge}}</a>
                      </div>
                      <div class="item-line item-line--bottom">
                        <span class="line-cell item-price">火热</span>
                        <span class="line-cell item-user">{{item.description}}人最近报名</span>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="container-1200">
      <div class="mod-sub">
        <div class="container-1200">
          <h2>近期热门</h2>
        </div>
        <div class="hover-expand">
          <div class="container-1200">
            <div class="custom-course-box">
              <div class="custom-course-list">
                <ul class="course-card-list">

                  <li
                    class="course-card-item"
                    v-for="(item,index) in courseList.slice(2*maxSize,3*maxSize)"
                    :key="index"
                  >
                    <a href="#" class="item-img-link">
                      <img :src="item.pic" alt="图片不存在" class="item-img"/>
                    </a>
                    <div class="changecolor">
                      <h4 class="item-tt">
                        <a href="#" class="item-tt-link">{{item.name}}</a>
                      </h4>
                      <div class="item-line item-line--middle">
                        <span class="line-cell item-task">共{{item.description}}课时</span>
                        <a
                          href="#"
                          class="line-cell item-source-link"
                        >{{item.description|fenge}}</a>
                      </div>
                      <div class="item-line item-line--bottom">
                        <span class="line-cell item-price">火热</span>
                        <span class="line-cell item-user">{{item.description}}人最近报名</span>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>
<script>
  export default {
    name: "SelectedCourse",
    data() {
      return {
        courseList: [],
        maxSize: 10
      };
    },
    methods: {
      //获取首页下的热门课程（即全部课程）
      //访问AllCourse.json返回数据
      getCourseList() {
        this.$https.courseList('/course/allcourse').then(res => {
            this.courseList = res.queryResult.list;


        });
      },
      goPurchase(){
        this.$router.push({
          name:'Purchase'
        })
      }
      // courseHandler(courseId){
      //   this.$router.push({
      //     name:'CourseDetail',
      //     params:{
      //       courseId:courseId
      //     }
      //   })
      // }
    },
    created() {
      this.getCourseList();
    },
    filters: {
      fenge(val) {
        var list = val.split(" ");
        return list[0];
      }
    }
  };
</script>
<style scoped>
  .changecolor {
    background: #fff;
    padding-top: 8px;
    padding-left: 8px;
    padding-bottom: 4px;
  }

  .cont-right {
    width: 78.5%;
    float: right;
  }

  .cont-right div:nth-child(2) {
    margin-left: 0;
  }

  .cont-right .recom-item {
    background: #fff;
    margin-top: 15px;
    width: 23.5%;
    margin-right: 5px;
    display: inline-block;
  }

  .cont-right .recom-item a {
    color: #050505;
    text-decoration: none;
  }

  .cont-right .recom-item p {
    position: relative;
    padding: 0;
    margin: 0;
    line-height: 200%;
  }

  .cont-right .recom-item ul {
    font-size: 14px;
    margin-top: 0;
    margin-bottom: 10px;
  }

  .cont-right .recom-item ul li {
    color: #999;
  }

  .cont-right .recom-item ul li:hover {
    border: 1px solid #99b;
    transition: 0.3s all ease;
  }

  .cont-right .recom-item ul li p {
    margin-bottom: 30px;
    margin-top: -15px;
  }

  .cont-right .recom-item ul li span {
    color: #ff7c2d;
  }

  .cont-right .recom-item ul li em {
    font-weight: bold;
  }

  .cont-left {
    width: 21%;
    display: inline-block;
  }

  .title {
    margin-bottom: 15px;
    position: relative;
    font-size: 20px;
    text-align: left;
  }

  .mod-sub {
    margin-top: 48px;
    margin-bottom: 55px;
    width: 100%;
  }

  .mod-sub h2 {
    font-size: 30px;
    line-height: 30px;
    text-align: center;
    position: relative;
    font-weight: 400;
  }

  .container-1200 {
    width: 1200px;
    margin-left: auto;
    margin-right: auto;
  }

  .hover-expand {
    position: relative;
  }

  .custom-course-box {
    height: 490px;
  }

  .custom-course-list {
    min-height: 231px;
    max-height: 490px;
    margin-top: 13px;
  }

  .course-card-list {
    margin-right: -16px;
    margin-left: -16px;
    font-size: 14px;
  }

  .course-card-item {
    display: inline-block;
    width: 220px;
    padding: 7px 7px 10px;
    border: 1px solid transparent;
    position: relative;
    margin-left: 9px;
    cursor: pointer;
  }

  .course-card-item:hover {
    border: 1px solid #999;
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.15);
    transition: 0.3s all ease;
  }

  .item-img-link {
    position: relative;
    display: block;
    -webkit-tap-highlight-color: transparent;
    text-decoration: none;
  }

  .item-img {
    display: block;
    width: 220px;
    height: 124px;
  }

  .item-tt {
    overflow: hidden;
    height: 40px;
    font-size: 14px;
    margin-bottom: 6px;
    position: relative;
    font-weight: 400;
  }

  .item-tt-link {
    color: #333;
    word-wrap: break-word;
    white-space: pre-wrap;
    word-break: break-all;
  }

  .item-line {
    overflow: hidden;
    margin-top: 0;
    height: 18px;
    font-size: 12px;
    color: #999;
  }

  .item-line--middle {
    margin-bottom: 3px;
  }

  .line-cell {
    float: left;
    margin-left: 8px;
  }

  .line-cell:first-child {
    margin-left: 0;
  }

  .item-task {
    color: #999;
  }

  .item-source-link {
    color: #999;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    word-wrap: normal;
  }

  .item-line--bottom {
    height: 24px;
    line-height: 24px;
  }

  .item-price {
    color: #f4621f;
    font-size: 14px;
  }

  .item-user {
    color: #999;
    margin-left: 10px;
  }
</style>
