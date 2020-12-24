
Page({

  /**
   * 页面的初始数据
   */
  data: {
    my_courses:[],
    my_microSpecialty:[],
    status:1 ,//任务状态
    hasUserInfo:false,
    userInfo:[]
    
  },
  onLoad:function(){
    wx.setNavigationBarTitle({
      title: "我的学习"
    })
  },
  showStatus: function(e){
    let status = e.currentTarget.dataset.status;
    this.setData({
      status:status
    })
  },
  onShow:function(){
    var that = this;
    var userInfo = wx.getStorageSync('user');
    
    console.log(userInfo.id)
    
    wx.request({
      //后端接口提供的url
      url: 'http://127.0.0.1:40600/learning/course/getcourse/'+userInfo.id,
      method:'GET',
      //需要传入的参数
      data:{},
      //如果访问成功
      success:function(res){
        var my_courses = res.data.queryResult.list;
        if(my_courses == null){
          //如果获取数据失败，提示使用者
          var toastText = '获取数据失败' + res.data.message;
          wx.showToast({
            title: toastText,
            icon:'',
            //显示时长为2s
            duration:2000
          })
        }else{
          that.setData({
            my_courses:my_courses,
            hasUserInfo:true
          })
          console.log(res.data.queryResult.list);
        }
      }
    })
    // wx.cloud.callFunction({
    //   name: 'getMyCourse',
    // }).then(res=>{
    //   this.setData({
    //     my_courses: res.result.data
    //   })
    // })
  },


  gologin: function(){
    wx.navigateTo({
      url: '../login/login'
    });
  }
})