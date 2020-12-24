<template>
  <div class="personal-body">
    <section class="inner-center clearfix">
      <aside class="aside-left">
        <div class="l-nav-area">
          <div class="title">个人中心</div>
          <div class="my-ico">
            <img :src="user.userpic" alt/>
            <p>{{user.username}}</p>

          </div>
          <ul class="l-nav">
            <li class="l-nav-item" v-for="item in navLinks" :key="item.id" @click="changeComponents(item.id)"
                :class="{active:currentIndex===item.id}">
              <p>
                {{item.title}}
                <i class="el-icon-arrow-right pull-right"></i>
              </p>
            </li>
          </ul>
        </div>
      </aside>
      <main class="main">
        <ClassSchedule v-show="isSchedule"/>
        <PersonSetting v-show="isSetting"/>
      </main>
      <div class="clearfix"></div>
    </section>
  </div>
</template>
<script>
  import ClassSchedule from "../Personnal/ClassSchedule";
  import PersonSetting from "../Personnal/PersonSetting";
  import utilApi from '../api/utils'

  export default {
    name: "Personal",
    components: {
      ClassSchedule,
      PersonSetting
    },
    data() {
      return {
          user:{
              id:'',
              username:'',
              userpic:''
          },
        isSchedule: true,
        isSetting: false,
        isCollection: false,
        currentIndex: 1,
        navLinks: [
          {id: 1, title: "我的学习"},
          {id: 2, title: "个人中心"},
          {id: 3, title: "找回密码"}
        ]
      }
    },
    methods: {
      changeComponents(itemId) {
        this.currentIndex = itemId;
        if (itemId === 1) {
          //我的学习组件
          this.isSchedule = true;
          this.isSetting = false;
          this.isCollection = false;
        } else if (itemId === 2) {
          //个人中心组件
          this.isSchedule = false;
          this.isSetting = true;
          this.isCollection = false;
        } else {
          //我的收藏组件
          this.isSchedule = false;
          this.isSetting = false;
          this.isCollection = true;
        }
      }
    },

      mounted() {
          this.user = utilApi.getActiveUser();

      },
    computed: {

     }
  };
</script>
<style scoped>
  .personal-body {
    margin-top: 48px;
  }

  .clearfix {
    clear: both;
  }

  .inner-center {
    display: block;
    width: 990px;
    position: relative;
    margin-left: auto;
    margin-right: auto;
    margin-bottom: 50px;
    box-sizing: border-box;
  }

  .aside-left {
    float: left;
    width: 200px;
    box-sizing: border-box;
    position: relative;
  }

  .l-nav-area {
    background: #fff;
    margin-bottom: 20px;
    border: 1px solid #dddddd;
  }

  .l-nav-area .title {
    font-size: 18px;
    text-align: center;
    line-height: 60px;
    font-weight: bold;
    color: #00a4ff;
  }

  .l-nav-area .my-ico {
    width: 100%;
    text-align: center;
  }

  .l-nav-area .my-ico img {
    box-shadow: 2px 2px 4px #000;
    width: 70%;
    margin: 0px auto;
    border-radius: 50%;
  }

  .l-nav-area .my-ico p {
    text-align: center;
    line-height: 50px;
  }

  .l-nav-item {
    height: 45px;
    line-height: 45px;
  }

  .l-nav-item .pull-right {
    position: absolute;
    right: 10px;
    padding-top: 15px;
  }
  .l-nav li.active{
    color: #00a4ff;
  }
  .l-nav-item p {
    display: block;
    color: #333;
    text-indent: 20px;
    border-left: 3px solid transparent;
    cursor: pointer;
  }

  .l-nav-item p:hover {
    color: #23b8ff;
  }

  .main {
    margin-left: -200px;
    padding-left: 230px;
    float: left;
    width: 100%;
    box-sizing: border-box;
  }
</style>
