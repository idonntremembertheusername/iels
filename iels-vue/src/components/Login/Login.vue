<template>
  <div class="box">
    <div class="login">
      <div class="login-title">
        <p>身份认证</p>
      </div>
      <div class="login_box">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="登录" name="first">
            <el-form
              :model="ruleForm"
              :rules="rules"
              ref="ruleForm"
              label-width="100px"
              class="demo-ruleForm"
            >
              <el-form-item label="名称" prop="username">
                <el-input v-model="ruleForm.username"></el-input>
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input type="password" v-model="ruleForm.password" auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="login">登录</el-button>
                <el-button @click="resetLoginForm">重置</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="注册" name="second">
            <Register/>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>
    import Register from "../Login/Register";
    import utilApi from "../api/utils"
    import API from "../../restful/api"

    export default {
        name: "Login",
        components: {
            Register
        },
        data() {

            // var validatePassword = (rule, value, callback) => {
            //     if (value === "") {
            //         callback(new Error("请输入密码"));
            //     } else {
            //         if (this.ruleForm.checkPassword !== "") {
            //             this.$refs.ruleForm.validateField("checkPassword");
            //         }
            //         callback();
            //     }
            // };
            return {
                user: {
                    userid: '',
                    username: '',
                    userpic: ''
                },
                activeName: "first",
                //登录表单的数据绑定对象
                ruleForm: {
                    username: "",
                    password: ""
                    // checkPassword: ""
                },
                //表单的验证规则对象
                rules: {
                    //验证用户名是否合法
                    username: [
                        {required: true, message: "请输入您的用户名", trigger: "blur"},
                        {min: 2, max: 5, message: "长度在2到5个字符", trigger: "blur"}
                    ],
                    //验证密码是否合法
                    password: [{required: true, message: '请输入密码', trigger: 'blur'}, {
                        min: 3,
                        max: 10,
                        message: '长度在 3 到 10 个字符',
                        trigger: 'blur'
                    }]
                }
            };
        },
        methods: {
            async refresh_user() {
                // 从sessionStorage中取出当前用户
                let activeUser = utilApi.getActiveUser()
                // 取出cookie中的令牌
                let uid = utilApi.getCookie('uid')
                console.log(' async refresh_user activeUser= ' + activeUser)
                console.log(' async refresh_user uid=  ' + uid)
                if (activeUser && uid && uid === activeUser.uid) {
                    console.log('insert into if (activeUser && uid && uid === activeUser.uid).....')
                    this.user = activeUser
                } else {
                    if (!uid) {
                        return
                    }
                    console.log('insert into else ...')
                    // 请求查询jwt
                    const {data: res} = await this.$http.get('/auth/userjwt')
                    console.log('res: ' + res)
                    if (res.success) {
                        let jwt = res.jwt
                        let activeUser = utilApi.getUserInfoFromJwt(jwt)
                        if (activeUser) {
                            this.user = activeUser
                            console.log('this.user: ' + this.user)
                            utilApi.setUserSession('activeUser', JSON.stringify(activeUser))
                        }
                    }
                }
            },
            handleClick(tab, event) {
            },
            //重置表单
            // resetForm(formName) {
            //     this.$refs[formName].resetFields();
            // },
            // 点击重置按钮, 重置登录表单
            resetLoginForm() {
                this.$refs.ruleForm.resetFields()
            },
            // submitForm(formName) {
            //     this.$refs[formName].validate(async valid => {
            //         if (valid) {
            //             //登录：访问user.json返回数据，
            //             var {data:res}=await this.$https.login('../../static/data/user.json');
            //             // var {data:res} = await this.$https.login(
            //             //   "http://127.0.0.1:8888/api/private/v1/login",
            //             //   this.ruleForm
            //             // );
            //             console.log(res);
            //             //弹出成功提示框
            //             this.$message({
            //                 type: "success",
            //                 message: "登录成功"
            //             });
            //             let data=res.data;
            //
            //             //存储cookie
            //             // for(let key in data){
            //             //   this.$cookies.set(key,data[key]);
            //             // }
            //             this.$cookies.set('username',data.username);
            //             this.$cookies.set('userId',data.id);
            //             this.$cookies.set('token',data.token);
            //             this.$cookies.set('email',data.email);
            //             this.$cookies.set('phone',data.mobile);
            //             this.$cookies.set('roleId',data.rid);
            //
            //             //分发actions中声明的方法，存储用户数据
            //             this.$store.dispatch('getUser',data);
            //             this.$router.go(-1);
            //         } else {
            //             console.log("error submit");
            //             return false;
            //         }
            //     });
            // }
            login() {
                this.$refs.ruleForm.validate(async isLoginFormRules => {
                    if (!isLoginFormRules) return
                    var qs = require('qs')
                    const {data: res} = await this.$http.post('/auth/userlogin', qs.stringify(this.ruleForm))
                    // const { data: res } = await API.login('/auth/userlogin', qs.stringify(this.ruleForm))
                    // const { data: res } = await API.login('/auth/userlogin', this.ruleForm)
                    if (!res.success) {
                        if (res.message) {
                            return this.$message.error(res.message)
                        } else {
                            this.$message.error('登陆失败')
                        }
                    }
                    this.$message.success('登录成功')
                    this.refresh_user()
                    this.$router.push('/home')

                })
            }
        }
    };
</script>
<style scoped>
  .box {
    width: 100%;
    position: relative;
    height: 600px;
  }

  .box .login {
    position: absolute;
    width: 500px;
    height: 400px;
    left: 50%;
    margin-left: -250px;
  }

  .login .login-title {
    width: 100%;
    text-align: center;
  }

  .login-title p {
    font-family: PingFangSC-Regular;
    font-size: 18px;
    letter-spacing: 0.29px;
    padding-top: 30px;
    padding-bottom: 20px;
  }

  .login_box {
    width: 400px;
    height: auto;
    background: #fff;
    box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.5);
    border-radius: 4px;
    margin-left: 20px;
    margin-top: 20px;
    padding: 20px;
  }
</style>
