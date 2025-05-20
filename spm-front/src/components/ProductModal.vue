<template>
  <!-- 模态框覆盖层，点击覆盖层关闭模态框 -->
  <div class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <!-- 左侧图片区和右侧详情区的容器 -->
      <div class="content-container">
        <!-- 左侧图片区，展示产品图片 -->
        <div class="modal-image">
          <img :src="product.image" alt="Product Image" />
        </div>
        
        <!-- 右侧详情区，展示产品名称、价格、介绍 -->
        <div class="modal-details">
          <div class="name-price-container">
            <div class="detail-row header-row">
              <span class="detail-name">Name: {{ product.name }}</span>
              <span class="detail-price">{{ product.price }}</span>
            </div>
          </div>
          
          <div class="description-container">
            <div class="detail-row description-row">
              <span>Goods Description:</span>
              <p>{{ product.description }}</p>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 独立的按钮区域，位于图片和详情下方 -->
      <div class="button-box">
        <div class="button-left" @click="buyNow">Buy now</div>
        <div class="button-right" @click="addToCart">Add to Cart</div>
      </div>
    </div>
  </div>
</template>

<script>
// 引入 axios 用于发送 HTTP 请求
import axios from 'axios';

export default {
  name: "ProductModal",
  props: {
    product: {
      type: Object,
      required: true,
    },
  },
  methods: {
    // 关闭模态框，触发自定义事件通知父组件
    close() {
      this.$emit("close");
    },
    async buyNow() {
      try {
        // 向 /product/favorite 接口发送 POST 请求，传递产品 ID
        const response = await axios.post('/product/favorite', {
          productId: this.product.id,
        });
        // 打印购买成功信息和响应数据
        console.log('购买成功', response.data);
        // 这里可以添加更多逻辑，如弹出提示框告知用户购买成功
        alert('购买成功！');
      } catch (error) {
        // 打印购买失败信息和错误详情
        console.error('购买失败:', error);
        // 这里可以添加更多逻辑，如弹出提示框告知用户购买失败
        alert('购买失败，请检查是否登录或是网络错误！');
      }
    },
    async addToCart() {
      try {
        // 修改为新的添加到购物车接口地址 /cart
        const response = await axios.post('/cart', {
          productId: this.product.id,
        });
        // 打印添加到购物车成功信息和响应数据
        console.log('添加到购物车成功:', response.data);
        // 这里可以添加更多逻辑，如弹出提示框告知用户添加成功
        alert('已成功添加到购物车！');
      } catch (error) {
        // 打印添加到购物车失败信息和错误详情
        console.error('添加到购物车失败:', error);
        // 这里可以添加更多逻辑，如弹出提示框告知用户添加失败
        alert('添加到购物车失败，请稍后重试！');
      }
    },
  },
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background: rgba(229, 229, 229, 0.3);
  z-index: 1000;
}

.modal-content {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 800px;
  height: 500px;
  background-color: #fdf7f7;
  display: flex;
  flex-direction: column; /* 垂直排列子元素 */
  box-shadow: 0 2px 10px rgba(0,0,0,0.3);
  border-radius: 8px;
  overflow: hidden;
}

/* 图片区和详情区的容器 */
.content-container {
  flex: 1; /* 占据除按钮外的所有空间 */
  display: flex; /* 水平排列内部元素 */
}

.modal-image {
  flex: 0 0 30%; /* 固定30%宽度 */
  margin-top: 80px;
  height: 50%;
  padding: 20px;
  box-sizing: border-box;
}

.modal-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}

.modal-details {
  flex: 1; /* 占据剩余70%宽度 */
  padding: 30px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.name-price-container {
  margin-top: 20px;
  margin-bottom: 70px;
}

.header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 26px;
  font-weight: bold;
  color: #333;
}

.description-container {
  flex-grow: 1;
  margin-bottom: 3px;
  line-height: 1.5;
  font-size: 20px;
}

.description-row span,
.description-row p {
  color: #666;
}

/* 按钮区域样式 */
.button-box {
  padding: 20px;
  display: flex;
  justify-content: center; /* 水平居中 */
  gap: 20px; /* 按钮间距 */
  background-color: #fdf7f7;
  border-top: 1px solid #eee; /* 顶部分割线 */
}

.button-left,
.button-right {
  width: 300px; /* 固定按钮宽度 */
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  border-radius: 20px;
  background-image: url('https://img.js.design/assets/img/67cfdb8607c4c2e467899865.png');
  background-size: cover;
  color: white;
  font-weight: bold;
  font-size: 24px;
  transition: transform 0.2s;
}

.button-left:hover,
.button-right:hover {
  transform: scale(1.03);
  color: rgb(255, 71, 90);
}
</style>