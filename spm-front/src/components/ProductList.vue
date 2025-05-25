<template>
  <div class="product-list">
    <h2>hot products</h2>
    <ElRow class="product-container">
      <ElCol class="product-item" v-for="product in getProducts(0)" :key="product.id" :span="5">
        <!-- 添加点击事件 -->
        <ElImage :src="product.pictures" alt="Product Image" @click="openModal(product)" fit="cover" style="width: 100%;" />
        <div class="custom-text">
          <p style="font-size: 1.5rem; font-weight: 400;">{{ product.name }}</p>
          <p>＄{{ product.price }}</p>
        </div>
      </ElCol>
    </ElRow>
    <h2>discount zone</h2>
    <ElRow class="product-container">
      <ElCol class="product-item" v-for="product in getProducts(1)" :key="product.id" :span="5">
        <!-- 添加点击事件 -->
        <ElImage :src="product.pictures" alt="Product Image" @click="openModal(product)" fit="cover"
          style="width: 100%;" />
        <div class="custom-text">
          <p style="font-size: 1.5rem; font-weight: 400;">{{ product.name }}</p>
          <p>＄{{ product.price }}</p>
        </div>
      </ElCol>
    </ElRow>
    <!-- 当 selectedProduct 不为空时显示模态框 -->
    <ProductModal v-if="selectedProduct" :product="selectedProduct" @close="closeModal" />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import ProductModal from './ProductModal.vue';
import sampleImage from '@/assets/test2.png';
import { productRandom } from '@/api/product';
import { ElCol, ElImage, ElRow } from 'element-plus';

const props = defineProps({
  type: {
    type: String,
    default: 'hot-products'
  }
});

var hotProducts = ref([
]);

var discountProducts = ref([
]);

const getProducts = (index) => {
  if (index === 0) {
    return hotProducts.value.slice(0, 4); // 截取前四个热门商品
  } else {
    return discountProducts.value.slice(0, 4); // 截取前四个打折商品
  }
};

// 定义一个响应式变量用于保存点击的当前商品
const selectedProduct = ref(null);

const openModal = (product) => {
  selectedProduct.value = product;
};

const closeModal = () => {
  selectedProduct.value = null;
};

onMounted(() => {
  productRandom(4).then((res) => {
    hotProducts.value = res.data
  })
  productRandom(4).then((res) => {
    discountProducts.value = res.data
  })
})
</script>


<style scoped>
.product-list {
  padding: 20px;
}

.product-container {
  display: flex;
  overflow-x: auto;
  gap: 20px;
  width: 100%;
  overflow-y: hidden;
  overflow-x: hidden;
}

.product-item {
  min-width: 140px;
  text-align: center;
  transition: transform 0.3s;
  /* 给容器添加过渡，使动画更流畅 */
  cursor: pointer;
}

.product-item img {
  margin-top: 2px;
  width: 95%;
  height: auto;
  border-radius: 10px;
  transition: all 0.3s ease;
  /* 统一过渡效果 */
  border: 2px solid transparent;
  /* 初始透明边框，避免悬停时位移 */
}

/* 图片悬停效果 */
.product-item img:hover {
  border-color: rgb(244, 107, 127);
  /* 边框颜色 */
  transform: scale(1.03);
  /* 放大效果 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  /* 更明显的阴影 */
}

/* 优化容器悬停效果（可选：整个商品项响应鼠标事件） */
.product-item:hover {
  transform: translateY(2px);
  /* 轻微下移，增强悬浮感 */
}

/* 文字样式优化 */
.product-item p {
  margin-top: 8px;
  font-size: 14px;
  color: #333;
}

.product-item .price {
  color: #ff6b6b;
  font-weight: 500;
  margin-bottom: 4px;
}

img {
  width: 100px;
  height: 100px;
}

.custom-text {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 0 5px #aaa;
  margin-bottom: 0.5rem;
}
</style>