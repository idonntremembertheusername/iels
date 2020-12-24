<template>
  <div class="login_container">
    <div class="login_box">
      <!-- 头像区域 -->
      <div class="avatar_box">
        <img src="../assets/photos/logo.png" />
      </div>
      <!-- 登录表单区域 -->
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginFormRules"
        label-width="0px"
        class="login_form"
      >
        <!-- 用户名 -->
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" prefix-icon="iconfont icon-user"></el-input>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            prefix-icon="iconfont icon-3702mima"
            type="password"
          ></el-input>
        </el-form-item>
        <!-- 按钮区域 -->
        <el-form-item class="btns">
          <el-button type="primary" @click="login">登录</el-button>
          <el-button type="info" @click="resetLoginForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import utilApi from '../api/utils'
export default {
  data() {
    return {
      user: {
        userid: '',
        username: '',
        userpic: ''
      },
      // 这是登录表单的数据绑定对象
      loginForm: {
        username: 'admin',
        password: 'admin'
      },
      // 这是表单的验证规则
      loginFormRules: {
        // 验证用户名是否合法
        username: [{ required: true, message: '请输入登录名称', trigger: 'blur' }, { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }],
        // 验证密码是否合法
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }]
      }
    }
  },
  methods: {
    async refresh_user() {
      // 从sessionStorage中取出当前用户
      let activeUser = utilApi.getActiveUser()
      // 取出cookie中的令牌
      let uid = utilApi.getCookie('uid')
      // console.log(activeUser)
      if (activeUser && uid && uid === activeUser.uid) {
        this.user = activeUser
      } else {
        if (!uid) {
          return
        }
        // 请求查询jwt
        const { data: res } = await this.$http.get('/api/auth/userjwt')
        if (res.success) {
          let jwt = res.jwt
          let activeUser = utilApi.getUserInfoFromJwt(jwt)
          if (activeUser) {
            this.user = activeUser
            utilApi.setUserSession('activeUser', JSON.stringify(activeUser))
          }
        }
      }
    },
    // 点击重置按钮, 重置登录表单
    resetLoginForm() {
      this.$refs.loginFormRef.resetFields()
    },
    login() {
      this.$refs.loginFormRef.validate(async isLoginFormRules => {
        if (!isLoginFormRules) return
        var qs = require('qs')
        const { data: res } = await this.$http.post('/api/auth/userlogin', qs.stringify(this.loginForm))
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
}
</script>

<style lang="less" scoped>
.login_container {
  background-color: #2b4b6b;
  height: 100%;
}

.login_box {
  width: 450px;
  height: 300px;
  background-color: #fff;
  /*  border-radius - 该属性允许您为元素添加圆角边框.
        border-radius 属性是一个最多可指定四个 border-*-radius 属性的复合属性
    */
  border-radius: 3px;
  position: absolute;
  left: 50%;
  top: 50%;
  /* transform 属性向元素应用 2D 或 3D 转换。该属性允许我们对元素进行旋转、缩放、移动或倾斜。
       transform: translate(x,y) 定义 2D 转换.
        translate(x,y) 括号里的值为百分数时，会以目前元素本身的宽高做参考，
        比如，目前元素本身的宽为100px，高为50px， 那填(50%,50%)，
        则表示就是向右移动50px、向下移动25px（正百分数），
        添加负号（负百分数）就是向着相反的方向移动，即左、上。
    */
  transform: translate(-50%, -50%);
}

.avatar_box {
  height: 130px;
  width: 130px;
  border: 1px solid #eee;
  border-radius: 50%;
  padding: 10px;
  // box-shadow属性向box添加一个或多个阴影
  /* box-shadow: offset-x   offset-y      blur          spread    color       inset;
       box-shadow: X轴偏移量   Y轴偏移量   [阴影模糊半径]  [阴影扩展]  [阴影颜色]  [投影方式];
        offset-x：必需，取值正负都可。offset-x水平阴影的位置。
        offset-y：必需，取值正负都可。offset-y垂直阴影的位置。
        blur:可选，只能取正值。blur-radius阴影模糊半径，0即无模糊效果，值越大阴影边缘越模糊。
        spread：可选，取值正负都可。spread代表阴影的周长向四周扩展的尺寸，正值，阴影扩大，负值阴影缩小。
        color:可选。阴影的颜色。如果不设置，浏览器会取默认颜色，通常是黑色，但各浏览器默认颜色有差异，建议不要省略。
        inset:可选。关键字，将外部投影(默认outset)改为内部投影。inset 阴影在背景之上，内容之下。
    */
  box-shadow: 0 0 10px #eee;
  position: absolute;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #eee;}
  img {
    width: 100%;
    height: 100%;
    border-radius: 50%;
    background-color: #eee;
  }


.login_form {
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 0 20px;
  /* 设置了 box-sizing:border-box;实际所占宽/高度 = 设置的高度（height）/ 设置的宽度（width）+ 外边距（margin） */
  box-sizing: border-box;
}

.btns {
  /* Flex是Flexible Box的缩写，意为"弹性布局"，用来为盒状模型提供最大的灵活性。
       设为Flex布局以后，子元素的float、clear和vertical-align属性将失效。
       flex有六个属性: 其中, justify-content属性: 决定元素在主轴（页面）上的排列.
        1：justify-content : center;元素在主轴（页面）上居中排列
        2：justify-content : flex-start;元素在主轴（页面）上由左或者上开始排列
        3：justify-content : flex-end;元素在主轴（页面）上由右或者下开始排列
        4：justify-content : space-between;元素在主轴（页面）上左右两端或者上下两端开始排列
        5：justify-content : space-around;每个元素两侧的间隔相等。所以，元素之间的间隔比元素与边框的间隔大一倍
    */
  display: flex;
  justify-content: flex-end;
}
</style>
