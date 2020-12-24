<template>
  <div class="wrapper-plan">
    <h1 class="wrapper-tt">课程表</h1>
    <section class="award-wrap">
      <div class="award">
        <h2 class="award-title">
          <span>今日上课：</span>
          <span class="award-title-num">10</span>
          <span>分钟</span>
        </h2>
      </div>
    </section>
    <section class="my-course">
      <div class="title">
        <div class="tit-item">
          <span>
            <em v-for="item in sortButton" :key="item.id" :class="{active:sortflag==item.id}" @click="sortflag=item.id">{{item.name}}</em>
          </span>
        </div>
      </div>
      <div class="cont">
        <div class="courses-list">
          <div class="myclass-course-list g-flow">
            <ul class="myclass-course-list-ul f-cb">
              <li
                class="myclass-course-list-item"
                v-for="(item,index) in SortedCourseList_OnNumbers"
                :key="index"
              >
                <div class="course-card">
                  <div class="ykt-course-card">
                    <a href="javascript:void(0);" class="headwrap">
                      <div class="course-card-head">
                        <img
                          :src="item.course_img"
                          alt
                        />
                      </div>
                      <div class="course-card-title">{{item.name}}</div>
                      <div class="course-card-progress">
                        <div class="progress-all">
                          <div class="progress-current" style="width:90%;"></div>
                        </div>
                        <div class="progress-txt">已学习10/{{item.numbers}}课时</div>
                      </div>
                    </a>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
<script>
  export default {
    name: "ClassSchedule",
    data() {
      return {
        courseList: [],
        maxSize: 10,
        sortflag: 0,
        sortButton: [
          {id: 0, name: "按学习人数排序"},
          {id: 1, name: "按课程章节排序"}
        ]
      };
    },
    methods: {
      //获取个人课程，这里是获取所有课程
      //访问AllCourse.json返回数据
      getPersonCourseList() {
        this.$https.courseList('../../static/data/CourseData/AllCourse.json').then(res => {
          if (res.error_no === 0) {
            this.courseList = res.data.slice(0*this.maxSize,1*this.maxSize);
          }
        });
      },
      sortCourse(sortflag) {
        if (sortflag == 0) {
          //按学习人数排序
          let arr = this.courseList;
          let i = arr.length;
          while (i > 0) {
            for (let j = 0; j < i - 1; j++) {
              if (arr[j].learn_number < arr[j + 1].learn_number) {
                let temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
              }
            }
            i--;
          }
          return arr;
        } else if (sortflag == 1) {
          //按课程章节排序
          let arr = this.courseList;
          let i = arr.length;
          while (i > 0) {
            for (let j = 0; j < i - 1; j++) {
              if (arr[j].numbers < arr[j + 1].numbers) {
                let temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
              }
            }
            i--;
          }
          return arr;
        }
      }
    },
    created() {
      this.getPersonCourseList();
    },
    computed: {
      SortedCourseList_OnNumbers() {
        return this.sortCourse(this.sortflag);
      }
    },
  };
</script>
<style scoped>
  .clearfix {
    clear: both;
  }

  .wrapper-plan {
    position: relative;
  }

  .wrapper-tt {
    font-size: 22px;
    line-height: 1.3;
    margin-bottom: 20px;
    font-weight: 400;
  }

  h1 {
    margin: 0;
  }

  h2 {
    line-height: 2;
    font-weight: 400;
  }

  h3 {
    line-height: 2;
    font-weight: 400;
  }

  .award-wrap {
    position: relative;
    margin-top: -12px;
  }

  .award-wrap .toggle-btn {
    position: absolute;
    right: 100px;
    color: #009eef;
    cursor: pointer;
    line-height: 36px;
    top: -36px;
    width: 130px;
    text-align: center;
  }

  icon-score {
    display: inline-block;
    vertical-align: -6px;
    width: 24px;
    height: 24px;
    background: url("https://7.url.cn/edu/user/7b1c6672d01f2ae5090842d7eb872971.png") no-repeat;
  }

  .i-v-top {
    margin-left: 5px;
    font-size: 16px;
  }

  .icon-font {
    font-family: webfont;
    font-style: normal;
    font-weight: 400;
    font-feature-settings: normal;
    font-variant: normal;
    text-transform: none;
    line-height: 1;
    position: relative;
    vertical-align: 0;
  }

  .award {
    position: relative;
    overflow: hidden;
    padding: 15px;
  }

  .award .award-title {
    position: relative;
    color: #777;
    margin-bottom: 12px;
  }

  .award .award-title-num {
    color: #000;
    font-size: 35px;
  }

  .award .award-body {
    border-top: 1px solid #f4f4f4;
    padding-top: 22px;
    margin: 0 -16px;
  }

  .award .award-item {
    float: left;
    text-align: center;
    width: 250px;
  }

  .award .award-item-title {
    font-size: 16px;
    margin-bottom: 10px;
    color: #333;
  }

  .award .award-item-data {
    color: #333;
    font-size: 12px;
    display: inline-block;
    vertical-align: middle;
    line-height: 12px;
    margin-bottom: 9px;
  }

  .award .award-item-data span:first-child {
    line-height: 30px;
    font-size: 30px;
  }

  .award .award-item-sep {
    width: 1px;
    height: 67px;
    float: left;
    background: #efeff4;
  }

  .award .award-item-addition {
    color: #bbb;
    text-align: center;
  }

  .my-course {
    padding: 0;
  }

  .my-course .title {
    /* padding: 10px 0; */
    font-size: 18px;
    line-height: 45px;
    font-weight: bold;
    margin-bottom: 15px;
    border: 1px solid #dddddd;
  }

  .my-course .title .tit-item {
    background: #fff;
    padding: 0 10px;
    line-height: 15px;
    color: #333;
  }

  .my-course .title .tit-item.active {
    color: #00a4ff;
  }

  .my-course .title .tit-item span {
    padding: 10px 0;
    display: inline-block;
    font-size: 14px;
  }

  .my-course .title .tit-item span em {
    display: inline-block;
    padding: 0 20px;
    border-right: solid 1px #999;
    font-style: normal;
    cursor: pointer;
  }
  .my-course .title .tit-item span em.active{
    color: #00a4ff;
  }
  .my-course .title .tit-item span em:last-child {
    border: none;
  }

  .g-flow {
    margin: 0 auto;
    text-align: left;
  }

  .myclass-course-list-ul {
    margin-right: -22px;
  }

  .f-cb {
    zoom: 1;
  }

  .myclass-course-list-item {
    height: 259px;
    width: 223px;
    display: inline-block;
    margin-left: 14px;
    margin-right: 12px;
    overflow: hidden;
    margin-bottom: 20px;
  }

  .ykt-course-card {
    height: 259px;
    width: 225px;
    box-sizing: border-box;
    background: #fff;
    border: 1px solid #fff;
  }

  .ykt-course-card .headwrap {
    overflow: hidden;
    display: inline-block;
    width: 100%;
    vertical-align: top;
  }

  .ykt-course-card .course-card-head {
    height: 94px;
    width: 100%;
    position: relative;
    overflow: hidden;
  }

  .ykt-course-card .course-card-head img {
    width: 100%;
    vertical-align: top;
    transition: transform, 1s, ease, -webkit-transform;
    position: absolute;
    z-index: 2;
  }

  .myclass-course-list-item:hover {
    border-bottom: 1px solid #999;
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.15);
    transition: 0.3s all ease;
  }

  .course-card-title {
    font-size: 16px;
    padding-top: 10px;
    padding-left: 3px;
    margin: 10px 10px 15px;
    color: #3a4348;
    height: 80px;
    line-height: 24px;
    box-sizing: border-box;
    overflow: hidden;
  }

  .course-card-progress {
    width: 205px;
    margin: 0 auto;
  }

  .course-card-progress .progress-all {
    height: 6px;
    width: 100%;
    background: #d8d8d8;
    margin-bottom: 8px;
  }

  .course-card-progress .progress-all .progress-current {
    height: 100%;
    background: #49af4f;
  }

  .course-card-progress .progress-txt {
    width: 100%;
    color: #49af4f;
    font-size: 14px;
  }
</style>
