<template>
  <div id="Apply">
    <div class="container-Apply">
      <!-- 悬浮卡片内容，使用 v-if 控制显示隐藏 -->
      <div v-if="showContainerApply" class="create-store">
        <div class="text-area">
          <p>You don't have a shop yet.
            <br> Do you need to apply for one?
          </p>
        </div>
        <div class="bt-area">
          <button class="yes-apply" @click="hideContainerApply">YES</button>
          <button class="no-apply" @click="gobackhome">NO</button>
        </div>
      </div>
      <!-- 申请注册店铺界面，使用 v-if 控制显示隐藏 -->
      <div v-if="!showContainerApply" class="register-store">
        <h2>Store Registration</h2>
        <form>
          <label for="storeName">Store Name</label>
          <input type="text" id="storeName" placeholder="Enter store name" v-model="shopname">
          <label for="storeInfo">Store Information</label>
          <textarea id="storeInfo" placeholder="Enter store information" v-model="shopaddress"></textarea>
          <button type="submit" @click="applystore">Submit Application</button>
          <button class="giveup" @click="showContainerApplyAgain">Give up!</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { ref } from 'vue';
import { storeCreate } from '@/api/store';
import { ElMessage } from 'element-plus';

const showContainerApply = ref(false);

// 点击 YES 按钮时隐藏 container-Apply 并显示注册表单
const hideContainerApply = () => {
  showContainerApply.value = false;
};
// 点击 Give up! 按钮时回到悬浮卡片
const showContainerApplyAgain = () => {
  showContainerApply.value = true;
};
const router = useRouter();
const gobackhome = () => {
  router.push('/homenew');
};
</script>

<script>
export default {
  data() {
    return {
      shopname: '',
      shopaddress: '',
    }
  },
  methods: {
    applystore() {
      var username = window.localStorage.getItem('username')
      storeCreate(username, this.shopname, this.shopaddress).then((res) => {
        ElMessage({message:"Created.",type:'success'})
        this.$router.push('/seller')
      })
      //alert("Your store application has been received. Please wait for review")
    }
  }
}
</script>

<style scoped>
#Apply {
  /* background-color: #dedede; */
  font-family: Arial, sans-serif;
  padding: 20px;
  margin: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.container-Apply {
  /* width: 800px;
  height: 650px; */
  width: 100%;
  /* 设置为 100% 宽度 */
  height: 100vh;
  /* 设置为 100% 视口高度 */
  background: url(https://img.js.design/assets/img/67cfdb8607c4c2e467899865.png);
  background-repeat: no-repeat;
  background-size: cover;
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.create-store {
  width: 750px;
  height: 550px;
  margin: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  background-color: rgba(255, 255, 255, 0.2);
  /* 添加半透明背景，突出悬浮效果 */
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

.text-area p {
  color: white;
  font-size: 55px;
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
}

.bt-area button {
  font-size: 35px;
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
  background-color: rgb(153, 247, 247);
  border-radius: 10px;
  border: 1 solid outset;
}

.yes-apply {
  margin-top: 20px;
  margin-right: 40px;
}

.no-apply {
  margin-top: 20px;
  margin-left: 40px;
}

.register-store {
  width: 600px;
  padding: 20px;
  position: absolute;
  /* 相对于 container-Apply 定位 */
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  /* 居中 */
  background-color: rgba(255, 255, 255, 0.8);
  /* 添加半透明背景 */
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

.register-store h2 {
  margin-bottom: 10px;
}

.register-store label {
  display: block;
  margin-bottom: 5px;
}

.register-store input,
.register-store textarea {
  width: 100%;
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.register-store button {
  background-color: #007BFF;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.giveup {
  margin-left: 300px;
}
</style>