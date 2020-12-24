// pages/personInfo/personInfo.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    user:""

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var userInfo = wx.getStorageSync('user');
    console.log(userInfo.id)
    wx.request({
      //后端接口提供的url
      url: 'http://127.0.0.1:40300/ucenter/findUserInfo?id='+userInfo.id,
      method:'POST',
      //需要传入的参数
      data:{},
      //如果访问成功
      success:function(res){
        var user= res.data;
        if(user == null){
          //如果获取数据失败，提示使用者
          var toastText = '获取数据失败';
          wx.showToast({
            title: toastText,
            icon:'',
            //显示时长为2s
            duration:2000
          })
        }else{
          that.setData({
            user:user,            
          })          
          console.log(user);
        }
      }
    })

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    wx.setNavigationBarTitle({
      title: "个人中心"
    })  

  },
  updateUserSubmit(e){
    var that = this;
    var userInfo = wx.getStorageSync('user');
    var name=e.detail.value.name;
    var sex=e.detail.value.sex;
    var phone=e.detail.value.phone;
    var email=e.detail.value.email;
    var utype=e.detail.value.utype;
    wx.request({
      //后端接口提供的url
      url: 'http://127.0.0.1:40300/ucenter/updateUser?id='+userInfo.id+"&password="+name+"&sex="+sex+"&phone="+phone+"&email="+email+"&utype="+utype,
      method:'POST',
      //需要传入的参数
      data:{},
      //如果访问成功
      success:function(res){
        var user= res.data;
        // wx.navigateTo({
        //   url: "../personInfo/personInfo"
        //   })
        wx.showToast({
          icon:"none",
          title:"修改成功"
        })
          console.log(res.data)
      }
    })
    console.log('form发生了submit事件，携带数据为：', e.detail.value)

    

  }


  

  

 

 
})