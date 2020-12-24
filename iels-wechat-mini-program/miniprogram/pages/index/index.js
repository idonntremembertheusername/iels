import { indexMock } from '../../utils/indexMock.js';

const app = getApp();

Page({
  data: {
    mainPage:{
      "icon": [
        {
          "image": "http://img.iels.com/cloud.png",
          "title": "云计算"
        },
        {
          "image": "http://img.iels.com/python.png",
          "title": "python"
        },
        {
          "image": "http://img.iels.com/111.png",
          "title": "大数据"
        },
        {
          "image": "http://img.iels.com/linux.png",
          "title": "Linux"
        },
        {
          "image": "http://img.iels.com/java.png",
          "title": "Java"
        },
        {
          "image": "http://img.iels.com/suanfa.png",
          "title": "算法"
        },
        {
          "image": "http://img.iels.com/vuejs.png",
          "title": "vue.js"
        },
        {
          "image": "http://img.iels.com/mores.png",
          "title": "更多课程"
        }
      ],
  //     "sellWell": {
  //       "title": "推荐课程",
  //       "content": [
  //         {
  //           "image": "http://img.iels.com/Machine_Learning.jpeg",
  //           "title": "Python数据分析与机器学习实战",
  //           "price": 99,
  //           "isBig": false,
  //           "id": "c1"
  //         },
  //         {
  //           "image": "http://img.iels.com/django.png",
  //           "title": "Django开发实战",
  //           "price": 368,
  //           "isBig": false,
  //           "id": "c2"
  //         },
  //         {
  //           "image": "http://img.iels.com/blockChain.png",
  //           "title": " 区块链开发从入门到进阶",
  //           "price": 39,
  //           "isBig": false,
  //           "id": "c3"
  //         },
  //         {
  //           "image": "http://img.iels.com/LinuxIntroduction.jpeg",
  //           "title": "Linux系统基础5周入门精讲",
  //           "price": 159,
  //           "isBig": false,
  //           "id": "c4"
  //         },
  //         {
  //           "image": "http://img.iels.com/microService.png",
  //           "title": "从0开始学习微服务架构",
  //           "price": 169,
  //           "isBig": false,
  //           "id": "c5"
  //         },
  //         {
  //           "image": "http://img.iels.com/CMDB.jpeg",
  //           "title": "运维自动的基石项目－CMDB开发",
  //           "price": "",
  //           "isBig": false,
  //           "id": "c6"
  //         }
  //       ]
  //     },
  //     "special": [
  //       {
  //         "image": "https://edu-image.nosdn.127.net/307e3bbd-0b8f-4166-a8ad-a694476020e8.png?imageView&amp;quality=100&amp;thumbnail=690y272"
  //       },
  //       {
  //         "image": "https://edu-image.nosdn.127.net/d078fac6-cb60-4e6b-a57e-cdc0c4c4b594.png?imageView&amp;quality=100&amp;thumbnail=690y272"
  //       },
  //       {
  //         "image": "https://edu-image.nosdn.127.net/9edd7b21-48fe-4ce6-9c3b-de1e98253d8e.jpg?imageView&amp;quality=100&amp;thumbnail=690y272"
  //       }
  //     ],
  //     "office": [
  //       {
  //         "image": "https://edu-image.nosdn.127.net/245b8ecc-7cd0-4895-a75f-aa3d173b0d0e.jpg?imageView&amp;quality=100&amp;thumbnail=690y296",
  //         "title": "我懂个P",
  //         "price": 299,
  //         "isBig": true
  //       },
  //       {
  //         "image": "https://edu-image.nosdn.127.net/07c12ca8-88a0-46e4-8662-4365a4c19b25.jpg?imageView&amp;quality=100&amp;thumbnail=336y190",
  //         "title": "交互式PPT",
  //         "price": 199,
  //         "isBig": false
  //       },
  //       {
  //         "image": "https://edu-image.nosdn.127.net/a28917c5-f112-491e-a0a8-1e99c2bbceb9.jpg?imageView&amp;quality=100&amp;thumbnail=336y190",
  //         "title": "表格之道(专业版)",
  //         "price": 99,
  //         "isBig": false
  //       },
  //       {
  //         "image": "https://edu-image.nosdn.127.net/07e3b8f9-5721-47bb-a710-ab8cce9cab9e.jpg?imageView&amp;quality=100&amp;thumbnail=336y190",
  //         "title": "玩转Power BI可视化分析",
  //         "price": 298,
  //         "isBig": false
  //       },
  //       {
  //         "image": "https://edu-image.nosdn.127.net/9f826b80-24bf-4a14-abca-02af2b177516.jpg?imageView&amp;quality=100&amp;thumbnail=336y190",
  //         "title": "零基础PPT快速入门",
  //         "price": 129,
  //         "isBig": false
  //       }
  //     ],
  //     "workplace": [
  //       {
  //         "image": "https://edu-image.nosdn.127.net/53590B6203A16FC64CD115771DFD8E15.png?imageView&amp;quality=100&amp;thumbnail=336y190",
  //         "title": "提升个人魅力，从个人品牌开始！",
  //         "price": "",
  //         "isBig": false
  //       },
  //       {
  //         "image": "https://edu-image.nosdn.127.net/c5a212d8-ae46-43b7-a4e1-c827a1a29eb6.JPG?imageView&amp;quality=100&amp;crop=119_5_1028_695&amp;thumbnail=336y190",
  //         "title": "零基础PPT快速入门",
  //         "price": "",
  //         "isBig": false
  //       },
  //       {
  //         "image": "https://img-ph-mirror.nosdn.127.net/B-4L3l8M9ONM0mNKkL2ywg==/2436447398424478006.jpg?imageView&amp;quality=100&amp;thumbnail=336y190",
  //         "title": "强势领导力",
  //         "price": "",
  //         "isBig": false
  //       },
  //       {
  //         "image": "https://edu-image.nosdn.127.net/169f62c0-ac23-4c7b-9d04-a857ea2e3bb1.png?imageView&amp;quality=100&amp;crop=0_0_1470_675&amp;thumbnail=336y190",
  //         "title": "零基础PPT快速入门",
  //         "price": "",
  //         "isBig": false
  //       },
  //       {
  //         "image": "https://edu-image.nosdn.127.net/CF9CB83E27098C1DD7BAD3CD9A2B2814.png?imageView&amp;quality=100&amp;thumbnail=336y190",
  //         "title": "动力爆棚：工作生活强力赋能",
  //         "price": "",
  //         "isBig": false
  //       },
  //       {
  //         "image": "https://edu-image.nosdn.127.net/3D71660B850C51BA14783ED8C5CD7150.png?imageView&amp;quality=100&amp;thumbnail=336y190",
  //         "title": "小米商战课",
  //         "price": "",
  //         "isBig": false
  //       }
  //     ],
  //     "language": [],
  //     "finance": [],
  //     "out_workplace": []
    },
  
    ads: [{
      "image": "https://hcdn1.luffycity.com/static/frontend/index/banner2@2x_1567507260.133423.png"
    },
    {
      "image": "https://hcdn1.luffycity.com/static/frontend/index/banner3_1567492242.652474.png"
    },
    {
      "image": "https://hcdn1.luffycity.com/static/frontend/index/banner1_1567492241.7829425.png"
    },
    {
      "image": "http://img.iels.com/sitdown.jpg"
    }
    
  ],

  courselist:[],
  
  
  },

  onLoad:function () {
    // const url = this.data.url;
    // indexMock(url)
    //   .then(res => {
    //     console.log(res)
    //     this.setData({
    //       mainPage:res
    //     })
    //   })
    //   .then(res=>{
    //     console.log("mainpage",this.data.mainPage)
    //   })
    // wx.request({
      
    //   url: 'http://127.0.0.1:31200/course/allcourse',
      
        
     
    //   success: (result) => {
    //     //console.log(result);
    //     console.log(result.data.queryResult)
    //   },
      
    // });
      
  },
  onShow: function () {
    //这里的this是指窗口而在request中this是指onShow方法(因为是页面调用onShow,onShow调用request)，所以要先定义
    var that = this;
    wx.request({
      //后端接口提供的url
      url: 'http://127.0.0.1:31200/course/allcourse',
      method:'GET',
      //需要传入的参数
      data:{},
      //如果访问成功
      success:function(res){
        var courselist = res.data.queryResult;
        if(courselist == null){
          //如果获取数据失败，提示使用者
          var toastText = '获取数据失败' + res.data.msg;
          wx.showToast({
            title: toastText,
            icon:'',
            //显示时长为2s
            duration:2000
          })
        }else{
          that.setData({
            courselist:courselist
          })
          console.log(res.data.queryResult);
        }
      }
    })

  },
  handleInput(e){
    //console.log(e);

  },
  //搜索功能
  goSearch : function(e){  
    
    var that = this;    
    var formData = e.detail.value; 
    console.log(formData)   
    if (formData) {       
    wx.request({         
    url: 'http://127.0.0.1:40100/search/course/wechatList?keyword='+formData,        
    data: {},
    header: {
      'content-type': 'application/json'
     },                   
    success: function(res) {  
      var courseSearchlist = res.data.queryResult;        
      that.setData({            
        courseSearchlist: courseSearchlist,          
      })         
     if (res.data.message=='操作失败！'){            
       wx.showToast({              
         title: '无相关视频',              
         icon: 'none',              
         duration: 1500            
        })          
      }
    else{ 
      console.log(courseSearchlist) 
      var str = JSON.stringify(courseSearchlist); 
      console.log(str);                   
      wx.navigateTo({                   
        url: '../search/search?data='+str,           
      })          
    }                    
            
  }      })    }
     else
     {       wx.showToast({        
       title: '输入不能为空',        
       icon: 'none',        
       duration: 1500      
      }) 
    }
  
  }    
  
});
