import Vue from 'vue'
import Vuex from 'vuex'
import VueCookies from 'vue-cookies'
import utilApi from '../components/api/utils'
Vue.use(Vuex);
Vue.use(VueCookies)
//这是Vuex，用于组件间的传值
const state = {
  showInterestBox:true,//展示首页兴趣盒子
  userInfo:'',
  user:''
};
const mutations = {
  // LOGIN: (state, data) => {
  //   //更改token的值
  //   state.token = data;
  //   window.sessionStorage.setItem('token', data);
  // },

  //登录用到的用于将用户数据存储到state.userInfo中
   get_user(state,user){
    state.user = utilApi.getActiveUser()

  },
  get_userInfo(state,userInfo){
    state.userInfo =userInfo

  },
  //登出时用到的用于清空Cookie和state.userInfo
  // clean_user(state){
  //   state.userInfo={};
  //   VueCookies.remove('email');
  //   VueCookies.remove('phone');
  //   VueCookies.remove('username');
  //   VueCookies.remove('userId');
  //   VueCookies.remove('token');
  //   VueCookies.remove('roleId');
  //   console.log(state.userInfo);
  // },
  // LOGOUT: (state) => {
  //   //登出的时候要清除token
  //   state.token = null;
  //   state.username=null;
  //   window.sessionStorage.removeItem('token');
  //   window.sessionStorage.removeItem('username');
  // },
  // USERNAME: (state, data) => {
  //   //把用户名存起来
  //   state.username = data;
  //   window.sessionStorage.setItem('username', data);
  // },

  //这两个方法用于显示和隐藏首页中设置兴趣的封面
  showBox:(state)=>{
    state.showInterestBox=true;
  },
  concealBox:(state)=>{
    state.showInterestBox=false;
  }
};

const actions = {
  // UserLogin({
  //   commit
  // }, data) {
  //   commit('LOGIN', data);
  // },

  getUser({commit},user){
    commit('get_user',data);
  },
  cleanUser({commit}){
    commit('clean_user');
  },
  get_userInfo({commit},userInfo){
    commit('get_userInfo',userInfo);
  },

  // UserLogout({
  //   commit
  // }) {
  //   commit('LOGOUT');
  // },
  // UserName({
  //   commit
  // }, data) {
  //   commit('USERNAME', data);
  // },

  show_box({commit}){
    commit('showBox');
  },
  conceal_box({commit}){
    commit('concealBox')
  }
};

export default new Vuex.Store({
  state,
  mutations,
  actions
});
