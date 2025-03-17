<template>
   <div @click="handleClick">
     <!-- 这里后续可以添加购物车的具体内容 -->
     Carts
   </div>
 </template>
 
 <script setup>
 import { useRouter } from 'vue-router';
 
 const router = useRouter();
 
 const handleClick = () => {
   router.push('/Carts');
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