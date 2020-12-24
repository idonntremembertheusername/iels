// pages/login/login.js
var app=getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    username:"",
    password:"",
    userInfo:"",
    hasUserInfo:true

    

  },
  onLoad: function (options) {
    wx.setNavigationBarTitle({
      title: "登录"
    })
  },
  usernameInput: function (e) {
    this.setData({
    username: e.detail.value
    })
    },
    passwordInput: function (e) {
    this.setData({
    password: e.detail.value
    })
    },
//     //点击登陆的时候触发的事件
signin: function () {
  var that = this;
  //登陆的时候要传过来的参数
  var name = that.data.username
  var pwd = that.data.password
  if (that.data.username == "") {
  wx.showModal({
  title: "信息提示",
  content: "用户名不能为空!"
  })
  } else if (that.data.password == "") {
  wx.showModal({
  title: "信息提示",
  content: "请输入密码!"
  })
  }
  console.log("用户名：" + name + "密码：" + pwd)
  //发送ajax请求到服务器-登录
  wx.request({
  url: 'http://127.0.0.1:40300/ucenter/userlogin?username='+name+"&password="+pwd,
  data: {},
  method: 'POST',
  
  success: function (res) {
   console.log(res)
  console.log(res.data)
  var userInfo=res.data;
  // this.setData({
  //   hasUserInfo:true
  // })
  wx.setStorage({
    data: userInfo,
    key: 'user',
  })
  // wx.setStorage({
  //   data: hasUserInfo,
  //   key: 'hasUser',
  // })
  // console.log("返回的结果"+JSON.stringify(res.data.msg))
  // console.log("返回的结果" + JSON.stringify(res.data.status))
  //弹出提示
  // wx.showModal({
  // title: "信息提示",
  // content: msg
  //  })
  // if (){
  // // console.log(status)
  // //跳转到index页面
  
  wx.switchTab({
  url: '../account/account',
  })
  },
  fail: function (res) {
  wx.showToast({
  title: '服务器网络错误,请稍后重试',  
  icon: 'loading', 
  duration: 1500 
  })
  },
  complete: function (res) {
  
  },
  })
  },
  //点击注册的时候触发的事件
  register: function () {
  wx.navigateTo({
  url: "../register/register"
  })
  }
})