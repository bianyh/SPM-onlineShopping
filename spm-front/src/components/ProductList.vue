<template>
    <div class="product-list">
      <h2>热门商品</h2>
      <div class="product-container">
        <div class="product-item" v-for="product in products" :key="product.id">
          <img :src="product.image" alt="Product Image" />
          <p>{{ product.name }}</p>
          <p>{{ product.price }}</p>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const products = ref([
    {
      id: 1,
      name: '商品 1',
      price: '￥99.00',
      image: require('@/assets/test2.png')
    },
    {
      id: 2,
      name: '商品 2',
      price: '￥199.00',
      image: require('@/assets/test2.png')
    },
    {
      id: 3,
      name: '商品 3',
      price: '￥299.00',
      image: require('@/assets/test2.png')
    },
    {
      id: 4,
      name: '商品 4',
      price: '￥399.00',
      image: require('@/assets/test2.png')
    }
  ]);
  </script>
  
  <style scoped>
  .product-list {
    padding: 20px;
  }
  
  .product-container {
    display: flex;
    overflow-x: auto;
    gap: 20px;
  }
  
  .product-item {
    min-width: 150px;
    text-align: center;
  }
  
  .product-item img {
    width: 100%;
    height: auto;
  }
  </style>