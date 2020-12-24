import Axios from 'axios';
import store from '../restful/store';
import VueCookies from 'vue-cookies';
// 这是Axios，用于连接接口
//添加请求拦截器
Axios.interceptors.request.use((config) => {
  //在发送请求之前做些什么
  //判断是否存在token，如果存在的话，每个http header都加上token
  if (VueCookies.isKey('token')) {
    config.headers.Authorization = VueCookies.get('token');
  }
  return config;
});
// Axios.interceptors.response.use((response)=>{
//   return response;
// },(error)=>{
// })
//  Axios.defaults.baseURL = 'https://www.luffycity.com/api/v1'
//Axios.defaults.baseURL='http://127.0.0.1:8888/api/private/v1/'

//登录
export function login(url,loginForm) {
    var qs = require('qs')
    let loginform = qs.stringify(this.loginForm)
    return Axios.post(url,loginform).then((res) => {
      return res;
    })
}

//获取课程分类列表
export function categoryList(url) {
  return Axios.get(url).then((res) => {
    console.log(res.data);
    return res.data;
  })
}
//获取用户信息
export function userInfo(url) {
  return Axios.get(url).then((res) => {
    console.log(res.data);
    return res.data;
  })
}
//获取课程列表
export function courseList(url) {
  return Axios.get(url).then((res) => {
    return res.data;
  })
}

export function updateUser(url,userInfoForm) {
  //var qs = require('qs')
  //let userInfo= qs.stringify(this.userInfoForm)
  return Axios.post(url,userInfoForm).then((res) => {
    return res;
  })
}

// export function course_section(url, courseId) {
//   return Axios.get(`${url}${courseId}/sections/`).then((res) => {
//     return res.data;
//   })
// }
//
// export function paymentInfo(url, courseId) {
//   return Axios.get(`${url}${courseId}/payment_info/`).then((res) => {
//     return res.data;
//   })
// }
//
// export function course_top(url, courseId) {
//   return Axios.get(`${url}${courseId}/top/`).then((res) => {
//     return res.data;
//   })
// }
//
// export function teacher_info(url, courseId) {
//   return Axios.get(`${url}${courseId}/right/`).then((res) => {
//     return res.data;
//   })
// }
//
// export function course_comment(url, courseId) {
//   return Axios.get(`${url}${courseId}/comment/`).then((res) => {
//     return res.data;
//   })
// }
//
// export function shopCart(url, params) {
//   return Axios.post(`${url}`, params).then(res => {
//     return res.data;
//   })
// }
//
// export function shopCartList(url) {
//   return Axios.get(url).then((res) => {
//     return res.data;
//   })
// }
