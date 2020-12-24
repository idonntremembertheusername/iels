<template>
  <el-container style="background:#fff">
    <el-header height="80px">
      <div class="header">
        <div class="nav-left">
          <h3 @click="loginHandler">大学生在线学习平台</h3>
        </div>
        <div class="nav-center">
          <ul>
            <li v-for="item in navlinks" :key="item.id">
              <router-link :to="{name:item.name}">{{item.title}}</router-link>
            </li>
            <li>
              <el-menu class="el-menu-demo" mode="horizontal" router>
                <el-submenu index="1">
                  <template slot="title">
                    <p class="el-menu-title">课程分类</p>
                  </template>
                  <div class="box">
                    <div class="menu-box" v-for="(category,index) in categoryList" :key="index">
                      <div class="el-menu-box">
                        <el-submenu :index="`${index+1}-1`">
                          <template slot="title">
                            <div
                              class="el-submenu-title"
                              @mouseenter="getCourseList(category.id)">
                              <span>{{category.name}}</span>
                            </div>
                          </template>
                          <div class="el-submenu-box">
                            <ul>
                              <li v-for="(course,i) in category.children" :key="i">
                                <el-menu-item>{{course.name}}</el-menu-item>
                              </li>
                            </ul>
                          </div>
                        </el-submenu>
                      </div>
                      <div class="el-submenu-slash"></div>
                    </div>
                  </div>
                </el-submenu>

              </el-menu>
            </li>
          </ul>
          <div style="display:inline-block">
            <el-col>
              <el-autocomplete
                class="inline-input"
                v-model="state1"
                :fetch-suggestions="querySearch"
                placeholder="请输入内容"
                @focus="handleFocus()"
                @select="handleSelect"
              ></el-autocomplete>
              <el-button type="primary" @click="searchHandler()">搜索</el-button>
            </el-col>
          </div>
        </div>

        <!-- el-dropdown -->
        <!-- 如果用户已登录 -->
        <div class="nav-right" v-if="user.username" @mouseenter="enterHandler()" @mouseleave="leaveHandler()">
          <span class="el-dropdown-link" @click="goPersonal">个人中心</span>
          <span class="user">{{user.username}}</span>
          <img src alt/>
          <ul class="my_account" v-show="isShow">
            <li>
              我的用户
              <i>></i>
            </li>
            <li>
              我的课程
              <i>></i>
            </li>
            <li @click="goshopCart()">
              我的订单
              <span class="count">(1)</span>
              <i>></i>
            </li>
            <li @click="logoutHandler()">
              退出
              <i>></i>
            </li>
          </ul>
        </div>

        <!-- el-dropdown -->
        <div class="nav-right" v-else>
          <span @click="gologin">登录</span>
        </div>
      </div>
    </el-header>
  </el-container>
</template>

<script>
    import utilApi from '../api/utils'

    export default {
        name: "Header",
        data() {
            return {
                user: {
                    id:'',
                    username: ''

                },
                isShow: false,
                navlinks: [
                    {id: 1, title: "首页", name: "Home"},
                    {id: 2, title: "精选课程", name: "Personal"}
                ],
                state1: "",
                course_classification: [],
                categoryList: [],
                courseList: [], //获取课程列表
                categorySecondList: []
                // allcourseList: [] //全部课程的列表
            };
        },
        methods: {
            querySearch(queryString, cb) {
                var course_classification = this.course_classification;
                //搜索框输入了信息之后进行过滤
                var results = queryString
                    ? course_classification.filter(this.createFilter(queryString))
                    : course_classification;
                //调用callback返回建议列表的数据
                cb(results);
            },
            createFilter(queryString) {
                return course_classification => {
                    return (
                        course_classification.value
                            .toLowerCase()
                            .indexOf(queryString.toLowerCase()) === 0
                    );
                };
            },
            // loadSearchResult() {
            //     return [
            //         {value: "腾讯课堂设计大师班"},
            //         {value: "腾讯自研前端课"},
            //         {value: "PS"},
            //         {value: "Java"},
            //         {value: "CAD"},
            //         {value: "Python"},
            //         {value: "平面设计"},
            //         {value: "实用英语"},
            //         {value: "公务员"},
            //         {value: "考研"},
            //         {value: "电商平台"}
            //     ];
            // },
            handleSelect(item) {
                console.log(item);
            },
            handleFocus() {
                this.course_classification = this.loadSearchResult();
            },
            searchHandler() {
                console.log(this.state1);
                // this.$router.push({
                //   name:'Search',
                //   query:{
                //     keywords:this.state1
                //   }
                // })
            },
            enterHandler() {
                this.isShow = true;
            },
            leaveHandler() {
                this.isShow = false;
            },
            goshopCart() {
                this.$router.push({
                    name: 'ShopCart'
                })
            },
            logoutHandler() {
                //清空store中存储的用户数据、删除cookie
                this.$store.dispatch('cleanUser');
                if (!this.$cookies.isKey('token')) {
                    this.$message({
                        type: 'primary',
                        message: '您已退出登录'
                    })
                }
            },
            //访问CategoryList.json返回数据
            //获取‘课程分类’这一标签下所有的课程分类
            getCategoryList() {
                this.$https.categoryList('/course/category/list').then(res => {
                    this.categoryList = res.children;
                });
            },


            //测试登录
            loginHandler() {
                this.$router.push({
                    name: "Login"
                });
            },
            goPersonal() {
                this.$router.push({
                    name: "Personal"
                });
            },
            gologin() {
                if (this.$store.state.userInfo.token) {
                    this.$message({
                        type: 'warn',
                        message: '您已登录!'
                    })
                } else {
                    console.log(this.$store.state.userInfo.token);
                    this.$router.push({
                        name: 'Login'
                    })
                }
            },
            clickhandler(routerPath) {
                this.$router.push({
                    name: routerPath
                });
            },
            // categoryHandler(index, categoryId) {
            //   this.$router.push({
            //     name: "Course",
            //     params: {
            //       categoryId: categoryId,
            //       categoryIndex: index
            //     }
            //   });
            // },
            // courseHandler(courseId) {
            //   this.$router.push({
            //     name: "CourseDetail",
            //     params: {
            //       courseId: courseId
            //     }
            //   });
            // }
            // changeDialogVisible(){
            //   this.$store.dispatch('handle_action_dialogvisible');
            // }
        },
        mounted() {
            this.getCategoryList();
            this.user = utilApi.getActiveUser()
            console.log('this.user:' + this.user)
            console.log(this.user.username)
        },
        computed: {
          // userInfo() {
          //    return this.$store.state.userInfo;
          //  }
            user(){
                return this.$store.state.user;
            }
        },
    };
</script>
<style scoped>
  .el-submenu-box {
    width: 350px;
    height: auto;
    /*height: 400px;*/
    /*overflow: scroll;*/
  }

  .el-submenu-box ul li {
    color: #333;
    font-size: 14px;
    margin-top: 10px;
    margin-right: 20px;
    margin-left: 30px;
    padding-bottom: 10px;
  }

  .el-submenu-box span {
    margin-top: 10px;
    margin-right: 20px;
    margin-left: 10px;
    padding-bottom: 10px;
  }

  .el-menu-title {
    font-size: 16px;
    color: #333;
  }

  .el-submenu-title a {
    padding-left: 10px;
  }

  .el-submenu-subtitle a {
    padding-left: 10px;
    padding-right: 10px;
    font-size: 14px;
    margin-right: 10px;
  }

  .el-submenu-subtitle a:hover {
    transition: 0.3s all;
    background-color: #dcdfe6;
  }

  .el-menu-box {
    width: 150px;
    height: 40px;
  }

  .el-submenu-title {
    color: #333;
  }

  .side-li {
    padding: 15px 0 12px;
    margin-top: 23px;
  }

  .side-li .side-li-title {
    font-size: 13px;
    padding-left: 30px;
    display: inline-block;
    vertical-align: middle;
    vertical-align: top;
    text-align: left;
    margin-bottom: 60px;
  }

  .side-li .side-li-content {
    display: inline-block;
    vertical-align: middle;
    width: 65%;
    vertical-align: top;
    font-size: 14px;
    margin-bottom: 10px;
  }

  .side-li .side-li-content a {
    padding-right: 10px;
    padding-left: 10px;
    margin-bottom: 10px;
    margin-top: 10px;
    padding-top: 10px;
    padding-bottom: 10px;
  }

  .el-submenu-slash {
    width: 98%;
    align-items: center;
    height: 1px;
    /* border: solid 1px #606266; */
  }

  .el-menu-demo {
    border-bottom: #c9c9c9;
    padding-top: 8.5px;
    height: 70px;
  }

  .el-header {
    border-bottom: #c9c9c9;
    box-shadow: 0 0.5px 0.5px 0 #c9c9c9;
  }

  .header {
    width: 1200px;
    height: 80px;
    line-height: 80px;
    margin: 0 auto;
  }

  .nav-left {
    float: left;
  }

  .nav-left .learn-header {
    margin-bottom: 15px;
    margin-left: -50px;
  }

  .nav-center {
    float: left;
    margin-left: 100px;
  }

  .nav-center ul {
    display: inline-block;
    overflow: hidden;
  }

  .nav-center ul li {
    float: left;
    margin: 0 5px;
    padding: 0 20px;
    height: 80px;
    line-height: 80px;
    text-align: center;
    position: relative;
  }

  .nav-center ul li a {
    color: #4a4a4a;
    width: 100%;
    height: 60px;
    display: inline-block;
  }

  .nav-center ul li a:hover {
    color: #b3b3b3;
  }

  .nav-center ul li a.is-active {
    color: #4a4a4a;
    border-bottom: 4px solid #ffc210;
  }

  .nav-right {
    float: right;
    position: relative;
    z-index: 100;
  }

  .nav-right span {
    cursor: pointer;
  }

  .nav-right .user {
    margin-left: 15px;
  }

  .nav-right img {
    width: 26px;
    height: 26px;
    border-radius: 50%;
    display: inline-block;
    vertical-align: middle;
    margin-left: 15px;
  }

  .nav-right ul {
    position: absolute;
    width: 221px;
    z-index: 100;
    font-size: 12px;
    top: 80px;
    background: #ffffff;
    border-top: 2px solid #d0d0d0;
    box-shadow: 0 2px 4px 0 #e8e8e8;
  }

  .nav-right ul li {
    height: 40px;
    color: #4a4a4a;
    padding-left: 30px;
    padding-right: 20px;
    font-size: 12px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    cursor: pointer;
    transition: all 0.2s linear;
  }

  .nav-right ul li span.msg {
    margin-left: -80px;
    color: red;
  }

  .nav-right ul li span.count {
    margin-left: -100px;
    color: red;
  }
</style>
