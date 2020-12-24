// miniprogram/pages/courseInfo/courseInfo.js
// wx.cloud.init() 
// const db = wx.cloud.database();
// const course_info = db.collection('course_info');



import { viewContent } from '../../utils/viewContent.js';

Page({

  /**
   * 页面的初始数据
   */
  data: {
    status: 1,
    id:"",
    course:[]
  },
  showStatus: function(e){
    let status = e.currentTarget.dataset.status;
    if(status === '3'){
      wx.cloud.callFunction({
        // 云函数名称
        name: 'getComment',
        // 传给云函数的参数
        data: {
          id: this.data.id
        }
      })
      .then(res => {
        if(res.result.data.length > 0){
          console.log("[getComment]",res.result.data) 
          this.setData({
            comment: res.result.data[0].comment
          })
        }
        
  
      })
      .catch(console.error)
    }
    this.setData({
      status:status
    })
  },
  
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var str = options.id;    
    wx.request({
      //后端接口提供的url
      url: 'http://127.0.0.1:31200/course/coursebase/'+str,
      method:'GET',
      //需要传入的参数
      data:{       
      },
      //如果访问成功
      success:function(res){
        console.log(res.data)
        var course = res.data;
        if(course == null){
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
            course:course
          })
          console.log(res.data);
        }
      }
    })
    // wx.showLoading({
    //   title: '数据加载中...'
    // });
  
  },
  onClickButton: function(){
      // if(id === cart_courseId){
      //   wx.showToast({
      //     icon:"none",
      //     title:"已添加"
      //   })
      //   return ;
      // }
      var that = this;
      var course=that.data.course;
      var userInfo = wx.getStorageSync('user');
      console.log(course)
      wx.request({
        //后端接口提供的url
        url: 'http://127.0.0.1:40600/learning/course/addCourse?courseId='+userInfo.id+'&userId='+course.id,
        
        method:'POST',
        //需要传入的参数
        data:{},
        
        //如果访问成功
        success:function(res){
          
          console.log(res.data)
          wx.showToast({
            icon:"none",
            title:"成功添加到我的学习"
          })
         
        }
      })
      
    
 
   
  },

  
    
    
  
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
   
    
    

  
   
  }
  
})