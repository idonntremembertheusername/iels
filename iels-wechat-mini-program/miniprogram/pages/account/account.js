

Page({

  data: {
    userInfo: "",
    hasUserInfo: false,
    // canIUse: wx.canIUse('button.open-type.getUserInfo')
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var userInfo = wx.getStorageSync('user');
    var hasUserInfo=wx.getStorageInfoSync('hasUser');
    //var hasUserInfo=wx.getStorageInfoSync('hasUser');
    this.setData({
      userInfo:userInfo,
      hasUserInfo:hasUserInfo
      
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
  getUserInfo: function() {
    // this.setData({
    //   hasUserInfo:true
    // })
    
    wx.navigateTo({
      url: "../login/login"
      })
    
  },



  // toCart(){
  //   wx.navigateTo({
  //     url: '../cart/cart'
  //   })
  // }

})