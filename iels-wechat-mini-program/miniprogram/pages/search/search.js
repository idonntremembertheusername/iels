// pages/search/search.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    courseSearchlist:[]                             

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    var courseSearchlist = JSON.parse(options.data);
    that.setData({
      courseSearchlist: courseSearchlist
    })
    console.log(courseSearchlist)
  }
})