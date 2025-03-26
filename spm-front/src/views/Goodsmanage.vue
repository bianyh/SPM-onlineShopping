<template>
  <div class="body">
    <!-- 复用的导航栏 -->

    <hr class="divider" />

    <!-- 返回按钮 -->
    <button class="backBtn" @click="goBack">Back</button>

    <div class="middle">
      <div class="product">
        <div class="product-container">
          <!-- 状态筛选栏 -->
          <div class="status-filter">
            <button
              class="filter-btn"
              v-for="(filter, index) in filters"
              :key="index"
              :class="{ active: activeFilter === filter.value }"
              @click="activeFilter = filter.value"
            >
              {{ filter.label }}
            </button>
          </div>

          <!-- 竖向商品列表 -->
          <div class="product-list">
            <div
              v-for="(item, index) in paginatedProducts"
              :key="index"
              class="product-item"
            >
              <img
                :src="item.image"
                alt="Product Image"
                class="product-image"
              />
              <div class="product-details">
                <h3 class="product-name">{{ item.name }}</h3>
                <p class="product-price">¥ {{ item.price }}</p>
              </div>
              <button
                v-if="item.status !== 'in_review'"
                class="status-btn"
                :class="item.status"
                @click="toggleProductStatus(item)"
              >
                {{ item.status === "in_stock" ? "Out of Stock" : "In Stock" }}
              </button>
              <button class="status-btn" @click="edit(item.id)">Edit</button>
            </div>
          </div>

          <!-- Pagination buttons -->
          <div class="pagination">
            <button
              @click="goToPage(currentPage - 1)"
              :disabled="currentPage === 1"
            >
              Previous
            </button>
            <span>Page {{ currentPage }} of {{ totalPages }}</span>
            <button
              @click="goToPage(currentPage + 1)"
              :disabled="currentPage === totalPages"
            >
              Next
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { ref, computed } from "vue";
import sampleImage from "@/assets/test2.png";

const router = useRouter();
const activeFilter = ref("in_stock");

// 状态筛选配置
const filters = ref([
  { label: "in stock", value: "in_stock" },
  { label: "in review", value: "in_review" },
  { label: "out of stock", value: "out_of_stock" },
]);

// 模拟商品数据
const products = ref([
  {
    id: 1,
    name: "Goods 1",
    price: "100",
    image: sampleImage,
    status: "in_review",
  },
  {
    id: 2,
    name: "Goods 2",
    price: "120",
    image: sampleImage,
    status: "out_of_stock",
  },
  {
    id: 3,
    name: "Goods 3",
    price: "150",
    image: sampleImage,
    status: "out_of_stock",
  },
  {
    id: 4,
    name: "Goods 4",
    price: "200",
    image: sampleImage,
    status: "in_stock",
  },
  {
    id: 5,
    name: "Goods 5",
    price: "180",
    image: sampleImage,
    status: "in_stock",
  },
  {
    id: 6,
    name: "Goods 6",
    price: "130",
    image: sampleImage,
    status: "in_stock",
  },
  {
    id: 7,
    name: "Goods 7",
    price: "160",
    image: sampleImage,
    status: "in_stock",
  },
  {
    id: 8,
    name: "Goods 8",
    price: "140",
    image: sampleImage,
    status: "out_of_stock",
  },
]);

// 过滤商品
const filteredProducts = computed(() => {
  return products.value.filter((item) => item.status === activeFilter.value);
});

// 分页数据
const currentPage = ref(1);
const itemsPerPage = 5; // 每页显示3个商品

// 计算当前页的商品
const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredProducts.value.slice(start, start + itemsPerPage);
});

// 计算总页数
const totalPages = computed(() =>
  Math.ceil(filteredProducts.value.length / itemsPerPage)
);

// 翻页操作
const goToPage = (page) => {
  if (page > 0 && page <= totalPages.value) {
    currentPage.value = page;
  }
};

// 返回按钮
const goBack = () => {
  router.push("/SellerHome");
};

// 切换商品状态
const toggleProductStatus = (item) => {
  item.status = item.status === "in_stock" ? "out_of_stock" : "in_stock";
};

const edit = (id) => {
  router.push(`/Editgood`);
};
</script>

<style scoped>
/* 状态筛选栏 */
.body {
  background: white;
}



.status-filter {
  display: flex;
  justify-content: space-between;
  width: 100%;
  max-width: 800px;
  margin: 0 auto 30px;
}

.filter-btn {
  flex: 1;
  min-width: 120px;
  padding: 12px 0;
  border: none;
  background: transparent;
  cursor: pointer;
  font-size: 16px;
  color: #666;
  position: relative;
  transition: all 0.3s;
  text-align: center;
}

.filter-btn.active {
  color: linear-gradient(90deg, #f9b9b9, #d0f6fb);
  font-weight: 500;
}

.filter-btn.active::after {
  content: "";
  position: absolute;
  left: 50%;
  bottom: -4px;
  width: 80%;
  height: 2px;
  background: linear-gradient(90deg, #f9b9b9, #d0f6fb);
  transform: translateX(-50%);
  animation: underline 0.3s ease-out;
}

@keyframes underline {
  from {
    width: 0;
  }
  to {
    width: 80%;
  }
}

/* 返回按钮 */
.backBtn {
  background: none;
  border: none;
  color: rgb(0, 0, 0);
  font-size: 22px;
  cursor: pointer;
  position: absolute;
  top: 90px;
  left: 280px;
  z-index: 10;
}

.divider {
  width: 70%;
  margin: 130px auto 30px;
  border: 0;
  border-top: 2px solid #ddd;
}

.middle {
  display: flex;
  justify-content: center;
  min-height: 100vh;
}

.product {
  width: 1200px;
  height: auto;
  border-radius: 59.49px;
  background: rgba(229, 229, 229, 0.3);
}

.product-container {
  max-width: 1200px;
  margin: 40px auto;
  padding: 0 20px;
}

/* 商品列表样式 */
.product-list {
  display: flex;
  flex-direction: column;
  align-items: center; /* 新增：强制所有子项水平居中 */
  gap: 25px;
  width: 100%; /* 确保列表占满父容器宽度 */
}

/* 单个商品项 */
.product-item {
  display: flex;
  align-items: center;
  background: #fff;
  width: 90%; /* 保持 80% 相对宽度 */
  height: 120px;
  padding: 15px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
}

.product-item:hover {
  transform: scale(1.02);
}

.product-image {
  width: 90px;
  height: 90px;
  border-radius: 8px;
  margin-right: 20px;
}

.product-details {
  flex-grow: 1;
  display: flex; /* 新增 */
  flex-direction: column; /* 新增 */
  justify-content: flex-start; /* 控制垂直方向顶部对齐 */
  gap: 4px; /* 控制名称和价格间距（可选） */
  margin-top: -30px; /* 整体上移补偿（根据实际情况调整） */
}

.product-name {
  font-size: 18px;
  font-weight: bold;
  /* 新增 */
  margin-bottom: 0; /* 移除默认间距 */
}

.product-price {
  font-size: 16px;
  color: #666;
  /* 新增 */
  margin-top: 0; /* 移除默认间距 */
}

.product-price {
  font-size: 16px;
  color: #666;
  margin: 0; /* 移除默认 margin */
}

/* 状态按钮 */
.status-btn {
  padding: 8px 12px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  cursor: default;
  background: linear-gradient(90deg, #f9b9b9, #d0f6fb);
  color: #fcfbfb;
  margin-right: 30px;
  margin-top: auto;
  width: 120px; /* 固定宽度 */
  min-width: 120px; /* 防止内容过短 */
  text-align: center; /* 文字居中 */
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin-top: 20px;
  position: absolute;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  max-width: 1200px;
}

.pagination button {
  padding: 6px 12px;
  border: none;
  background-color: #f9b9b9;
  color: white;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.pagination span {
  font-size: 16px;
}
</style>
