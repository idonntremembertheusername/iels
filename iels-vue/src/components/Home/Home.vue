<template>
  <div>
    <div>
      <el-carousel :interval="3000" arrow="always" height="400px">
        <el-carousel-item v-for="item in lunboImgs" :key="item.id">
          <img :src="item.imgSrc" alt="图片不存在" />
        </el-carousel-item>
<!--        <div class="my-class-box" v-if="user.username">-->
<!--          <div class="title">课程表</div>-->
<!--          <div>-->
<!--            <p>继续学习 程序语言设计</p>-->
<!--            <p>正在学习-使用对象</p>-->
<!--          </div>-->
<!--          <div>-->
<!--            <p>继续学习 程序语言设计</p>-->
<!--            <p>正在学习-使用对象</p>-->
<!--          </div>-->
<!--          <div>-->
<!--            <p>继续学习 程序语言设计</p>-->
<!--            <p>正在学习-使用对象</p>-->
<!--          </div>-->
<!--          <div>-->
<!--            <router-link :to="{name:'Personal'}">我的课程</router-link>-->
<!--          </div>-->
<!--        </div>-->
<!--        <div class="my-class-box my-class-box-unlogin" v-else>-->
<!--          <div class="unlogin-title">-->
<!--            <p class="unlogin-tips">跟进你的学习进度</p>-->
<!--            <div class="unlogin-img"></div>-->
<!--          </div>-->
<!--          <span class="login-button" @click="goLogin()">登录</span>-->
<!--        </div>-->
      </el-carousel>
    </div>
    <nav class="cate-tab">
      <div class="cate-tab-sub">
        <ul class="cate-tab-list">
          <li
            class="cate-tab-item"
            v-for="(category,index) in categoryList" :key="category.id"
            :class="{active:currentIndex===index}"
            @click="categoryHandler(index,category.id)"
          >{{category.name}}</li>
        </ul>
        <div class="cate-tab-btn-wrap">
          <span class="btn-cate-tab-edit btn-outline" @click="showInterestBox()">修改兴趣</span>
        </div>
      </div>
    </nav>
    <section class="cat-content">
      <!-- 自定义模块 -->
        <SelectedCourse v-show="isSelected" />
        <div class="container-1200">
          <div class="mod-sub mod-all-courses">
            <router-link :to="{name:'Home'}">
              <span class="mod-all-courses-link">查看全部课程</span>
              <i class="el-icon-arrow-right"></i>
            </router-link>
          </div>
        </div>
    </section>
    <InterestBox />
  </div>
</template>
<script>
import InterestBox from "../Home/Interest"
import SelectedCourse from "./SelectedCourse";
import utilApi from '../api/utils'
export default {
  name: "Home",
  components: {
    SelectedCourse,
    InterestBox
  },
  data() {
    return {
        user:{
            username:''
        },
      categoryId: 0, //当前被选中课程分类的ID
      categoryList:[],//获取课程分类列表
      isSelected: true,
      currentIndex: 0, //当前正处于精选课程模块中
      lunboImgs: [
        {
          id: 1,
          imgSrc:
            "https://hcdn1.luffycity.com/static/frontend/index/banner1_1567492241.7829425.png"
        },
        {
          id: 2,
          imgSrc:
            "https://hcdn1.luffycity.com/static/frontend/index/banner2@2x_1567507260.133423.png"
        },
        {
          id: 3,
          imgSrc:
            "https://hcdn1.luffycity.com/static/frontend/index/banner3_1567492242.652474.png"
        }
      ]
    };
  },

  methods: {
    showInterestBox(){
      this.$store.dispatch('show_box');
    },
    goLogin(){
      this.$router.push({
        name:'Login'
      })
    },

    //首页下的课程分类索引处理
    categoryHandler(index,categoryId) {
      this.currentIndex = index;
      // this.$router.push({
      //   name:'Course',
      //   params:{
      //     categoryId:categoryId,
      //     categoryIndex:index
      //   }
      // })
    },

    //获取首页下的课程分类
    getCategoryList() {
      this.$https.categoryList('../../static/data/CategoryList.json').then(res => {
        this.categoryList = res.data;
        var firstObj={
          id:0,
          name:"精选推荐",
          category:0
        };
        this.categoryList.unshift(firstObj);
      });
    },
  },
    mounted(){
        this.user = utilApi.getActiveUser()
    },
  computed: {
      // user(){
      //     return this.$store.state.user;
      // }

  },
  created() {
    this.getCategoryList();
  },
};
</script>
<style scoped>
.mod-all-courses-link{
  font-size: 18px;
  color: #333;
}
.mod-all-courses-link:hover{
  color: #23b8ff;
  transition: 0.4s all ease;
}
.mod-all-courses{
  text-align: center;
  height: 70px;
  line-height: 60px;
  box-sizing: border-box;
  border: 5px solid #7878;
  border-radius: 3px;
}
.mod-sub{
  margin-bottom: 55px;
  width: 100%;
}
.container-1200 {
  width: 1200px;
  margin-left: auto;
  margin-right: auto;
}
.cat-content {
  display: block;
  width: 100%;
  min-width: 1240px;
}
.btn-cate-tab-edit {
  color: #666;
  border-color: #999;
  padding: 3px 10px;
}
.btn-outline {
  display: inline-block;
  vertical-align: middle;
  cursor: pointer;
  font-size: 14px;
  text-align: center;
  border-radius: 2px;
  user-select: none;
  border: 1px solid #23b8ff;
  background-color: #fff;
}
.cate-tab-btn-wrap {
  position: absolute;
  right: 0;
  top: 20px;
}
.cate-tab-item.active {
  transition: 0.3s all ease;
  color: #23b8ff;
  /* border-bottom: 4px solid #23b8ff; */
}
.cate-tab-item {
  margin-left: 2px;
  display: inline-block;
  padding: 16px 12px;
  position: relative;
  font-size: 16px;
  cursor: pointer;
}
.cate-tab-list {
  width: 1200px;
  display: inline-block;
  position: relative;
}
.cate-tab-sub {
  margin-left: -2px;
  display: inline-block;
  width: 1200px;
  position: relative;
}
.cate-tab {
  width: 100%;
  min-width: 1240px;
  text-align: center;
  position: relative;
  background-color: #fff;
  border-bottom: 1px solid #e5e5e5;
  display: block;
}
.el-carousel__arrow--left {
  left: 425px;
}
.el-carousel__arrow--right {
  right: 600px;
}
img {
  width: 1920px;
  height: auto;
}
.my-class-box {
  position: absolute;
  width: 230px;
  z-index: 50;
  top: 30px;
  right: 350px;
  background: #fff;
}
.my-class-box .title {
  margin: 0;
  padding: 15px 20px;
  border: none;
  background: #9bceea;
}
.my-class-box div {
  margin: 10px 20px 0 20px;
  padding-bottom: 10px;
  font-size: 14px;
  border-bottom: solid 1px #999;
}
.my-class-box p {
  padding: 0;
  margin: 0;
  line-height: 200%;
}
.my-class-box div p:nth-child(2) {
  font-size: 12px;
  color: #787d82;
}
.my-class-box div:last-child {
  border: none;
  text-align: center;
}
.my-class-box div:last-child a {
  display: block;
  border: solid 1px #00a4ff;
  color: #2da1e7;
  line-height: 40px;
}
.my-class-box-unlogin {
  line-height: 50px;
  background-color: #fff;
  height: auto;
  box-shadow: 0 1px 10px 3px rgba(0, 0, 0, 0.1);
}
.unlogin-title {
  /* padding:21px 0 15px; */
  margin-top: 10px;
  background-color: #fff;
  line-height: 1.5;
  display: block;
}
.unlogin-tips {
  text-align: center;
  font-size: 16px;
  color: #999;
  /* margin: 0; */
}
.unlogin-img {
  width: 170px;
  height: 130px;
  background: url("https://8.url.cn/edu/edu_modules/edu-ui/img/bg/index-sprite3.8339b953.png#unsprite")
    no-repeat -144px -370px;
  /* margin: 10px auto auto; */
}
.login-button {
  display: inline-block;
  line-height: 50px;
  width: 200px;
  margin-bottom: 15px;
  font-size: 16px;
  border-radius: 3px;
  background-color: #23b8ff;
  color: #fff;
  cursor: pointer;
  text-align: center;
  margin-left: 15px;
}
.login-button:hover {
  color: #787d82;
  transition: 0.5s all ease;
}
</style>
