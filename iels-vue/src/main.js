// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import Vuex from 'vuex';
Vue.config.productionTip = false
Vue.use(Vuex)

//引入store
import store from '../src/restful/store';


import axios from 'axios'
Vue.prototype.$http = axios


import utilApi from './components/api/utils'
// 给 axios 设置请求拦截器:
axios.interceptors.request.use(config => {

  // 在发送请求向header添加jwt
  let jwt = utilApi.getJwt()
  if(jwt) {
    // 为请求头对象，添加 Token 验证的 Authorization 字段
    config.headers.Authorization = 'Bearer ' + jwt
  }
  return config
})

//导入ElementUI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css'
Vue.use(ElementUI)

//导入路由
import router from './router/index'
//全局守卫，当导航栏的地址发生改变时，这个全局的路由钩子函数就会调用
// router.beforeEach((to, from, next) => {
//   //如果用户访问登录页，直接放行
//   if (to.path === '/login') return next();
//   //从Cookie中判断token是否存在
//   if(VueCookies.isKey('token')){
//     //存储用户数据
//     let userInfo={
//       username:VueCookies.get('username'),
//       token:VueCookies.get('token'),
//     }
//     store.dispatch('getUser',userInfo);
//   }
//   next();
//
// })
// 导入axios
import * as api from './restful/api';
Vue.prototype.$https = api

//导入视频播放插件vue-video-player
import VueVideoPlayer from 'vue-video-player';
import 'video.js/dist/video-js.css'
Vue.use(VueVideoPlayer)

//导入vue-cookies
import VueCookies from 'vue-cookies';
Vue.use(VueCookies)

//声明公共模块组件Header和Footer
import Header from './components/Common/Header'
import Footer from '../src/components/Common/Footer.vue';
Vue.component(Header.name, Header)
Vue.component(Footer.name, Footer)

//导入全局样式
import '../static/global/index.css'


/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  //在其他组件中通过this.$store获取当前store实例
  store,
  components: {
    App
  },
  template: '<App/>'
})
