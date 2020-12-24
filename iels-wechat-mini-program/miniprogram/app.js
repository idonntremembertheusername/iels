App({

  onLaunch: function () {
    
    this.globalData = {
      userInfo: userInfo,
      cart_coursesId:[],
    },

    wx.cloud.init({
      env:"thesky"

    }) 
  },
globalInfo:{
  userInfo:null

}
})
