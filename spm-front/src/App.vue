<script setup>
import { RouterView } from 'vue-router';
import 'element-plus/theme-chalk/display.css'
</script>

<script>
import NavigationBar from './components/NavigationBar.vue';
import MsgBox from './components/MsgBox.vue';
import MessageBus from './utils/MessageBus';
export default {
  name:'APP',
  components: {
    NavigationBar,
    MsgBox
  },
  data() {
    return {
      pageState: 0, //页面状态
      currentUser: null, //当前用户
    }
  },
  mounted() {
    //接收消息弹出信号
    MessageBus.on('box', (result) => {
      //进行消息盒子展示
      this.$refs.msgbox.update(result);
    })
  },
  unmounted() {
    MessageBus.off()
  },
  methods: {
    PageSwitch(target, user) {
      this.pageState = target
      this.currentUser = user
    }
  }

}
</script>



<template>  
  <div class="bgcolor"></div>
  <NavigationBar></NavigationBar>
  <RouterView></RouterView>
  <MsgBox ref="msgbox" class="top-element" />
</template>





<style scoped>
*{
  margin:0;
  padding:0;
  box-sizing: border-box;
}
.bgcolor {
  top: 0;
  position: fixed;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -100;
  background-size: 100% 100%;
  background-attachment: fixed;
  background-color: #c3a8a8;
/*   background-image: linear-gradient(to top left, rgb(240, 208, 255) 0%, rgb(255, 247, 225) 100%); */ 
  background-image: url(https://img.js.design/assets/img/67cfdb8607c4c2e467899865.png);
  opacity: 0.2;
}

.main {
  display: block;
  position: absolute;
  width: 100%;
  height: auto;
  padding: 0rem;
  margin: 0%;
  right: 0;
  top: 0;
}

/* 响应式布局 - 小于 1000 px 时改为上下布局 */
@media screen and (max-width: 1024px) {
  .main {
    display: block;
    position: absolute;
    margin: 0;
    width: 100%;
  }
}

.top-element {
  z-index: 1;
}


.tipactive {
  /* tip动画效果 */
  animation: tip-focus-active 0.5s both;
}

.tips-enter-active {
  animation: tip-enter 0.25s
}

.tips-leave-active {
  animation: tip-leave 0.5s
}

@keyframes tip-enter {
  0% { opacity: 0;}
  100% { opacity: 1;}
}

@keyframes tip-leave {
  0% { opacity: 1;}
  100% { opacity: 0;}
}

@keyframes tip-focus-active {
  100%, 0% { background-color: rgba(255, 255, 255, 0.5);}
  30% { background-color: rgba(255, 255, 255, 1);}
}
</style>

