<template>
  <div class="seller-home">
    <main class="main-content">
      <div class="shop-info-card" @click="navigateToOrders">
        <div class="shop-avatar"></div>
        <div class="shop-text">
          <h1 class="shop-name">Query all pending orders
          </h1>
          <p class="shop-intro">The customer is waiting for you to ship the goods
          </p>
        </div>
      </div>
      <h2>Manage your stores</h2>
      <StoreCollectionView />
    </main>

    <div class="footer-bar"></div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import StoreView from './StoreView.vue'
import StoreCollectionView from './StoreCollectionView.vue'

const router = useRouter()
const navigateToOrders = () => {
  router.push('/seller/order')
}

</script>

<style scoped>
.seller-home {
  align-items: center;
  margin-top: 4rem;
}

.main-content {
  max-width: 1440px;
  padding: 0 20px;
}

.shop-info-card {
  display: flex;
  align-items: center;
  gap: 20px;
  background: linear-gradient(90deg, #f9b9b9, #d0f6fb);
  border-radius: 16px;
  padding: 40px;
  cursor: pointer;
  transition: transform 0.2s;
  margin-bottom: 40px;
  max-width: 1313px;
  margin-left: auto;
  margin-right: auto;
}

.shop-info-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.shop-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: white;
  border: 2px solid #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
}

.shop-text {
  display: flex;
  flex-direction: column;
}

.shop-name {
  font-size: 28px;
  font-weight: bold;
  color: #fff;
}

.shop-intro {
  font-size: 16px;
  color: #f0f0f0;
}

.action-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.action-box {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: 2px solid #f7d0de;
  border-radius: 16px;
  padding: 100px;
  width: 1313px;
  background: rgba(252, 244, 244, 0.6);
  position: relative;
}

.goods-title {
  position: absolute;
  top: 20px;
  left: 20px;
  font-size: 28px;
  font-weight: bold;
  background: linear-gradient(90deg, #f9b9b9, #d0f6fb);
  background-clip: text;
  /* 标准写法 */
  -webkit-background-clip: text;
  /* 兼容 WebKit 浏览器 */
  -webkit-text-fill-color: transparent;
  /* 让文本变透明，仅显示背景 */
}



.action-btn {
  padding: 20px 40px;
  border: none;
  border-radius: 24px;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s;
  color: #fff;
  ;
  text-transform: none;
  margin-top: 20px;
}

.release-btn {
  background: linear-gradient(90deg, #f9b9b9, #d0f6fb);
}

.release-btn:hover {
  background: linear-gradient(90deg, #f9b9b9, #d0f6fb);
}

.manage-btn {
  background: linear-gradient(90deg, #f9b9b9, #d0f6fb);
}

.manage-btn:hover {
  background: linear-gradient(90deg, #f9b9b9, #d0f6fb);
}

.footer-bar {
  width: 100%;
  height: 120px;
  /* 高度可调整 */
  background: linear-gradient(90deg, #f9b9b9, #d0f6fb);
  margin-top: 80px;
}
</style>
