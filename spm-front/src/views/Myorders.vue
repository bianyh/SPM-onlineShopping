<template>
    <div @click="handleClick">
      <!-- 这里后续可以添加我的订单的具体内容 -->
      Myorders
    </div>
</template>
  
 <script setup>
  import { useRouter } from 'vue-router';
  
  const router = useRouter();
  
  const handleClick = () => {
    router.push('/Myorders');
  };
  </script>
  
  <style scoped>
  /* 可以根据需要添加样式 */
  div {
    color: white;
    cursor: pointer;
    font-size:22px;
  }
  </style>