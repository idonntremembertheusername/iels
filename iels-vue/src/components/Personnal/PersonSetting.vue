<template>
  <div class="wrap-content">
    <h1 class="wrapper-tt">个人信息</h1>
    <div class="wrapper-bd">
      <div class="contact-box contact-box-mobile">
<!--        <div class="contact-box-hd">-->
<!--          <h2>用户呢称</h2>-->
<!--          <span>修改</span>-->
<!--        </div>-->
<!--        <div class="contact-box-bd">-->
<!--          <span class="num" >{{userInfo.username}}</span>-->
<!--        </div>-->
<!--      </div>-->
<!--      <div class="contact-box contact-box-mobile">-->
<!--        <div class="contact-box-hd">-->
<!--          <h2>手机号码</h2>-->
<!--          <span>修改</span>-->
<!--        </div>-->
<!--        <div class="contact-box-bd">-->
<!--          <span class="num" >{{userInfo.phone}}</span>-->
<!--        </div>-->
<!--      </div>-->
<!--      <div class="contact-box contact-box-mobile">-->
<!--        <div class="contact-box-hd">-->
<!--          <h2>电子邮箱</h2>-->
<!--          <span>修改</span>-->
<!--        </div>-->
<!--        <div class="contact-box-bd">-->
<!--          <span class="num" >{{userInfo.email}}</span>-->
<!--        </div>-->
        <el-form
          ref="userInfoForm"
          :rules="rules"
          :model="userInfoForm"
          label-width="100px">

          <el-form-item label="用户名">
            <el-input v-model="userInfoForm.username"></el-input>
          </el-form-item>

          <el-form-item label="电话">
            <el-input v-model="userInfoForm.phone"></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="userInfoForm.email"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click=" updateUser" >修改</el-button>
<!--            <el-button>取消</el-button>-->
          </el-form-item>
        </el-form>

      </div>
    </div>

  </div>
</template>
<script>
    import utilApi from '../api/utils'
    import {userInfo} from "../../restful/api";

    export default {
        name: "PersonSetting",
        data() {
            return {
                user: {
                    id: '',
                    username: ''
                },
                userInfoForm: {
                    username: '',
                    phone: '',
                    email: ''
                },
                rules: {
                    //验证用户名是否合法
                    username: [
                        {required: true, message: "请输入您的昵称", trigger: "blur"},
                        {min: 2, max: 5, message: "长度在2到5个字符", trigger: "blur"}
                    ],
                    //验证密码是否合法
                    phone: [{required: true, message: '请输入电话', trigger: 'blur'},
                        {min: 11, max: 11, message: '长度在 11 到 11个字符', trigger: 'blur'}
                        ],
                    email: [{required: true, message: '请输入邮箱', trigger: 'blur'},
                        {min: 11, max: 11, message: '长度在 3 到 10 个字符', trigger: 'blur'}
                    ]
                }
            };
        },

        methods: {
            getUserInfo() {
                //this.user = utilApi.getActiveUser()
                this.$https.userInfo('/ucenter/userInfo').then(res => {
                    this.userInfoForm = res;

                });
            },
            test(){
                //var qs = require('qs')
                this.$http.post('/ucenter/updateUserInfo',this.userInfoForm)
                    .then((res)=>console.log(res))
            },
            updateUser(){
                this.$refs.userInfoForm.validate(async isuserInfoFormRules => {
                    if (!isuserInfoFormRules) return
                   // var qs = require('qs')
                    const {data: res} = await this.$http.post('/ucenter/updateUserInfo', this.userInfoForm)
                    if (!res.success) {
                        if (res.message) {
                             return this.$message.error(res.message)
                         } else {
                             this.$message.error('修改失败')
                         }
                    }
                     this.$message.success('修改成功')
                    console.log(res)
                    this.$router.push('/PersonSetting')

                })

            }
        },
        mounted() {
            this.getUserInfo();
            this.user = utilApi.getActiveUser()
        },
        created() {
             this.getUserInfo();
        },
        computed: {

        },


    }
</script>
<style scoped>
  .wrapper-tt {
    font-size: 22px;
    line-height: 1.3;
    margin-bottom: 20px;
  }

  h1 {
    font-weight: 400;
  }

  .wrapper-bd {
    padding: 40px;
    min-height: 560px;
    background-color: #F2F6FC;
    border: none;
  }

  .contact-box {
    padding-bottom: 36px;
    margin-bottom: 36px;
    border-bottom: 1px solid #aaa;
  }

  .contact-box-hd {
    margin-bottom: 16px;
  }

  .contact-box-hd h2 {
    font-size: 20px;
    display: inline-block;
    line-height: 1.4;
    font-weight: 400;
  }

  .contact-box-hd span {
    margin-left: 12px;
    font-size: 14px;
    color: #23b8ff;
    text-decoration: none;
    cursor: pointer;
  }

  .contact-box-bd .num {
    font-size: 20px;
    display: inline-block;
    line-height: 1.4;
  }
</style>
