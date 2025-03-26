<template>

  <div class="goods-publish-container">
    <!-- 左侧切换选项 -->
    <div class="sidebar">
      <button
        @click="activeTab = 'goods'"
        :class="{ active: activeTab === 'goods' }"
      >
        Goods Information
      </button>
      <button
        @click="activeTab = 'image'"
        :class="{ active: activeTab === 'image' }"
      >
        Image Information
      </button>
    </div>

    <!-- 右侧内容区域 -->
    <div class="content">
      <!-- 商品信息模块 -->
      <div v-if="activeTab === 'goods'" class="form-section">
        <h2 class="section-title">Goods Information</h2>
        <div class="form-grid">
          <div class="form-item">
            <label class="form-label">Goods Name</label>
            <input
              v-model="formData.name"
              type="text"
              class="form-input"
              placeholder="Please enter the product name"
            />
          </div>
          <div class="form-item">
            <label class="form-label">Goods Price</label>
            <div class="price-input-group">
              <span class="currency-symbol">¥</span>
              <input
                v-model="formData.price"
                type="number"
                class="input"
                placeholder="0.00"
              />
            </div>
          </div>
        </div>

        <h2 class="section-title">Goods Category</h2>
        <select v-model="selectedCategory" class="form-input">
          <option disabled value="">Please select product category</option>
          <option
            v-for="(category, index) in categories"
            :key="index"
            :value="category.value"
          >
            {{ category.label }}
          </option>
        </select>
      </div>

      <!-- 图片上传模块 -->
      <div v-if="activeTab === 'image'" class="form-section">
        <h2 class="section-title">Goods Main Image</h2>
        <div class="image-uploader">
          <input
            type="file"
            accept="image/*"
            @change="handleImageUpload"
            hidden
            ref="fileInput"
          />
          <div class="upload-area" @click="$refs.fileInput.click()">
            <span v-if="!previewImage" class="upload-text"
              >Click to upload the main image</span
            >
            <template v-else>
              <img :src="previewImage" class="preview-image" alt="商品预览图" />
              <button class="remove-btn" @click.stop="removeMainImage">
                ×
              </button>
            </template>
          </div>
        </div>
        
        <!-- 细节信息部分 -->
        <h2 class="section-title" style="margin-top: 40px">Goods Detail Image</h2>
        <div class="detail-images">
          <!-- 预览已上传的细节图片，并添加删除按钮 -->
          <div
            class="detail-image"
            v-for="(img, index) in detailImages"
            :key="index"
          >
            <img :src="img" class="preview-image" alt="细节预览图" />
            <button class="remove-btn" @click="removeDetailImage(index)">
              ×
            </button>
          </div>
          <!-- 上传框始终保留一个用于继续上传 -->
          <div class="image-uploader detail-uploader">
            <input
              type="file"
              accept="image/*"
              @change="handleDetailImageUpload"
              hidden
              ref="detailFileInput"
              multiple
            />
            <div class="upload-area" @click="$refs.detailFileInput.click()">
              <span class="upload-text">Click to upload the detail image</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 提交按钮 -->
      <button class="submit-btn" @click="handleSubmit">Release Goods</button>
    </div>
  </div>

</template>

<script setup>
import { ref } from "vue";

const activeTab = ref("goods");
const formData = ref({
  name: "",
  price: null,
});

const categories = ref([
  { label: "Electronic Product", value: "electronics" },
  { label: "Home Life", value: "home" },
  { label: "Clothing, Shoes & Bags", value: "clothing" },
  { label: "Beauty & Personal Care", value: "beauty" },
]);

const selectedCategory = ref("");
const previewImage = ref(null);
const fileInput = ref(null);
const detailImages = ref([]); // 用于存储细节图片
const detailFileInput = ref(null);

const handleImageUpload = (event) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      previewImage.value = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

const handleSubmit = () => {
  const payload = {
    ...formData.value,
    category: selectedCategory.value,
    image: previewImage.value,
  };
  console.log("提交数据:", payload);
  // 这里添加实际提交逻辑
};

const handleDetailImageUpload = (event) => {
  const files = event.target.files;
  if (files.length) {
    // 逐个文件读取并存储到 detailImages 数组中
    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      const reader = new FileReader();
      reader.onload = (e) => {
        detailImages.value.push(e.target.result);
      };
      reader.readAsDataURL(file);
    }
  }
};

const removeDetailImage = (index) => {
  detailImages.value.splice(index, 1);
};

const removeMainImage = () => {
  previewImage.value = null;
  // 清空文件输入框的值
  if (fileInput.value) {
    fileInput.value.value = "";
  }
};
</script>


<style scoped>


/* 主容器左右布局 */
.goods-publish-container {
  background: white;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  height: 100vh;
  
  margin: 0; /* 移除居中，使其靠左 */
  padding: 40px 20px ;
  gap: 100px;
}

.sidebar {
  
  width: 200px;
  display: flex;
  flex-direction: column;
  margin-left: 100px;
  gap: 12px;
}
.sidebar button {
  padding: 12px 16px;
  border: none;
  border-radius: 8px;
  background: #f0f0f0;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
  text-align: left;
}

.sidebar button.active {
  background: linear-gradient(90deg, #f9b9b9, #d0f6fb);
  color: white;
}

.content {
  flex-grow: 1;
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  margin-right: 80px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

/* 表单样式 */
.form-section {
  margin-bottom: 40px;
}

.section-title {
  margin-bottom: 24px;
  font-size: 18px;
  color: #333;
  border-left: 4px solid #f9b9b9;
  padding-left: 12px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
}

.form-item {
  margin-bottom: 25px;
}

.form-label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: #666;
}

.form-input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  transition: border-color 0.3s;
}

.form-input:focus {
  border-color:  #f9b9b9;
  outline: none;
}

/* 价格输入框 */
.price-input-group {
  position: relative;
}

.price-input-group .input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  transition: border-color 0.3s;
  padding-left: 25px;
}

.currency-symbol {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #666;
}

/* 图片上传 */
.image-uploader {
  margin-top: 16px;
}

.upload-area {
  width: 300px;
  height: 180px;
  border: 2px dashed #ddd;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: border-color 0.3s;
  position: relative;
}

.upload-area:hover {
  border-color:  #f9b9b9;
}

.upload-text {
  color: #999;
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 6px;
}

/* 商品类别 */
.category-container {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.category-card {
  padding: 12px 24px;
  border: 1px solid #eee;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s;
}

.category-card.active {
  border-color: #007bff;
  background: #e6f3ff;
  color: #007bff;
}

/* 提交按钮 */
.submit-btn {
  display: block;
  width: 200px;
  margin: 10px auto 0;
  padding: 12px;
  background: linear-gradient(90deg, #f9b9b9, #d0f6fb);;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  cursor: pointer;
  transition: opacity 0.3s;
}

.submit-btn:hover {
  opacity: 0.9;
}

.detail-images {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: flex-start;
  margin-top: 16px;
}

.detail-image {
  width: 300px;
  height: 180px;
  position: relative; /* 确保删除按钮正确定位 */
}

.remove-btn {
  position: absolute;
  top: 5px;
  right: 5px;
  background: rgba(0, 0, 0, 0.5);
  color: #fff;
  border: none;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  line-height: 18px;
  text-align: center;
  cursor: pointer;
  font-size: 14px;
}

.detail-image .preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}

.detail-uploader .upload-area {
  width: 300px;
  height: 180px;
  margin-top: -15px;
}
</style>
