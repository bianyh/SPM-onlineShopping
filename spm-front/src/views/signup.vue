<template>
  <div class="login-main">
    <div class="login-bg"></div>
    <div class="login-container">
      <div class="title-row">
        <h2 class="login-title">SIGNUP</h2>
        <span class="login-tip"> Already have an account?</span>
        <router-link to="/login" class="login-link">
          Login in!
        </router-link>
      </div>
      <div class="login-form">
        <div class="form-item">
          <div class="input-wrapper">
            <el-icon class="input-icon">
              <User />
            </el-icon>
            <input class="input-word" type="text" v-model="signupForm.username" placeholder="please input username">
            <span class="required-mark">*</span>
          </div>
        </div>
        <div class="form-item">
          <div class="input-wrapper">
            <el-icon class="input-icon">
              <Lock />
            </el-icon>
            <input class="input-word" type="password" v-model="signupForm.password" placeholder="please input password">
            <span class="required-mark">*</span>
          </div>
        </div>
        <div class="form-item">
          <div class="input-wrapper">
            <el-icon class="input-icon">
              <Lock />
            </el-icon>
            <input class="input-word" type="password" v-model="signupForm.password_confirm" placeholder="confirm your password">
            <span class="required-mark">*</span>
          </div>
        </div>
        <div class="form-item">
          <div class="input-wrapper">
            <el-icon class="input-icon">
              <Message />
            </el-icon>
            <input class="input-word" type="email" v-model="signupForm.email" placeholder="please input e-mail">
          </div>
        </div>
        <div class="form-item">
          <div class="input-wrapper">
            <el-icon class="input-icon">
              <Cellphone />
            </el-icon>
            <input class="input-word" type="text" v-model="signupForm.phone" placeholder="please input phone-number">
          </div>
        </div>
        <div class="form-item">
          <button class="submit-button" @click="handleSignup">SIGNUP</button>
        </div>
        <!-- 移除原来的 return login -->
      </div>
    </div>
  </div>
</template>

<script>
import { userShow, userRegister } from '@/api/user';
import MessageBus from '@/utils/MessageBus';
import { User, Lock, Message, Cellphone } from '@element-plus/icons-vue'
import axios from 'axios'
export default {
  name: 'signup',
  components: {
    User,
    Lock,
    Message,
    Cellphone
  },
  data() {
    return {
      signupForm: {
        username: '',
        password: '',
        email: '',
        phone: '',
        password_confirm: ''
      }
    }
  },
  methods: {
    async handleSignup() {
      if (this.signupForm.password != this.signupForm.password_confirm) //检查密码码
      {
        MessageBus.emit('box', "The passwords entered are not the same.")
        return
      }
      if ( // 如果存在未填写的必需项目
        this.signupForm.password == "" ||
        this.signupForm.username == "" ||
        this.signupForm.phone == ""
      ) { // 拒绝提交
        MessageBus.emit('box', "There are required items that have not been filled in, please check.")
        return
      }
      userRegister(
        this.signupForm.username,
        this.signupForm.password,
        this.signupForm.email,
        this.signupForm.phoneNumber,
      ).then((result) => {
        result.callbacks = []
        if (result.code == 0) {
          result.callbacks[0] = this.goBack
        }
        MessageBus.emit('box', result)
      }, (err) => {
        MessageBus.emit('box', err)
      })
    },
    goBack(){
      this.$router.back()
    }
  }
}
</script>

<style scoped>
.login-main {
  width: 100%;
  height: 100vh;
  position: absolute;
  top: 0;
  left: 0;
}

.login-bg {
  width: 100%;
  height: 100vh;
  background-image: url(../assets/loginbg.jpg);
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: absolute;
  top: 0;
  left: 0;
  opacity: 0.5;
}

/** 原来的标题栏
.welcome-banner {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  background: linear-gradient(90deg, #409EFF, #66b1ff);
  padding: 12px 0;
  text-align: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 1;
}
.banner-content h1 {
  color: white;
  font-size: 20px;
  margin: 0;
  margin-bottom: 5px;
}
.banner-content p {
  color: rgba(255, 255, 255, 0.9);
  font-size: 14px;
  margin: 0;
}*/

.login-container {
  width: 500px;
  height: auto;
  min-height: 350px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  padding: 20px;
  z-index: 1;
  opacity: 0.9;
}

/* 新增 title-row 样式 */
.title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.login-title {
  color: #333;
  margin: 0;
  /* 去除原有的 margin-bottom */
}

.login-tip {
  font-size: 14px;
  opacity: 0.5;
  margin-left: 130px;
}

.login-link {
  color: #409EFF;
  text-decoration: none;
  transition: color 0.3s;
}

.login-link:hover {
  color: #66b1ff;
  text-decoration: underline;
}

.login-form {
  padding: 0 20px;
}

.input-wrapper {
  position: relative;
  width: 100%;
  max-width: 380px;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: -30px;
  /* 将图标移到输入框外部左侧 */
  color: #909399;
  font-size: 20px;
  z-index: 1;
}

.form-item {
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
}

.form-item input {
  width: 100%;
  max-width: 380px;
  height: 40px;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 0 30px 0 15px;
  /* 调整右侧padding为必填标记留出空间 */
  font-size: 14px;
  outline: none;
}

.form-item input:focus {
  border-color: #409EFF;
}

.required-mark {
  position: absolute;
  right: 10px;
  color: #ff4949;
  font-size: 16px;
}

.submit-button {
  width: 100%;
  max-width: 380px;
  height: 40px;
  background-color: #409EFF;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-button:hover {
  background-color: #66b1ff;
}

/* 移除原有的 form-footer 样式 */
</style>