<template>
  <div class="login-main">
    <div class="login-bg"></div>
    <div class="login-container">
      <h2 class="login-title">LOGIN</h2>
      <div class="login-form">
        <div class="form-item">
          <div class="input-wrapper">
            <el-icon class="input-icon">
              <User />
            </el-icon>
            <input class="input-word" type="text" v-model="loginForm.name" placeholder="please input username">
          </div>
        </div>
        <div class="form-item">
          <div class="input-wrapper">
            <el-icon class="input-icon">
              <Lock />
            </el-icon>
            <input class="input-word" type="password" v-model="loginForm.pwd" placeholder="please input password">
          </div>
        </div>
        <div class="form-item">
          <button class="submit-button" @click="handleLogin">LOGIN</button>
        </div>
        <div class="form-footer">
          <span class="text-account">No account yet?</span>
          <router-link to="/signup" class="signup-link">
            register now!
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { User, Lock } from '@element-plus/icons-vue'
import { userLogin } from '@/api/user'
import axios from 'axios'
import MessageBus from '@/utils/MessageBus'
import { decodeJwt } from '@/utils/jwtUtils'
export default {
  name: 'login',
  components: {
    User,
    Lock
  },
  data() {
    return {
      loginForm: {
        name: '',
        pwd: ''
      }
    }
  },
  methods: {
    async handleLogin() {
      userLogin(this.loginForm.name, this.loginForm.pwd).then((result) => {
        if (result.code == 0) {
          result.wait = true // 等待执行结果
          result.callbacks = [this.goToMainPage]
          console.log(result.data)
          window.localStorage.setItem("token", result.data) //储存token
          var claims = decodeJwt(result.data)[1].claims
          window.localStorage.setItem("username", claims.username)
          window.localStorage.setItem("uid", claims.id)
          result.content = "Welcome! " + claims.username
          MessageBus.emit('box', result) // 登陆成功消息框
          MessageBus.emit("auth", { type: "login" })
        }
        else {
          MessageBus.emit('box', result);
        }
      }, (err) => {
        MessageBus.emit('box', "无法连接至后端服务")
      })

    }
    //handleLogin() {
      //this.$router.push('/home');
    //  this.$router.push('/homenew');
    //}
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

.login-title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
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

.input-word {
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

.form-footer {
  text-align: center;
  margin-top: 15px;
}

.text-account {
  opacity: 0.5;
}

.signup-link {
  color: #409EFF;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s;
}

.signup-link:hover {
  color: #66b1ff;
  text-decoration: underline;
}
</style>