<template>
  <div class="order-container">
    <el-tabs v-model="activeStatus" class="demo-tabs" >
      <el-tab-pane label="All" name=-1></el-tab-pane>
      <el-tab-pane v-for="(status, index) in orderStatus" :key="index" :label="status" :name="index"></el-tab-pane>

      <ElCard v-for="order in filteredOrders" :key="order.id" class="order-card" @click="openOrderDetails(order)">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Order ID">{{ order.id }}</el-descriptions-item>
          <el-descriptions-item label="Amount">{{ order.totalAmount }}</el-descriptions-item>
          <el-descriptions-item label="Status">
            <el-tag :type="getStatusType(order.status)">{{ orderStatus[order.status] }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Created At">{{ order.createdAt }}</el-descriptions-item>
        </el-descriptions>
      </ElCard>
    </el-tabs>

    <!-- 弹出页面 -->
    <el-dialog v-model="dialogVisible" title="Order Details" width="60%">
      <div v-if="selectedOrder">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-card>
              <template #header>
                <div class="card-header">
                  <span>Order Summary</span>
                </div>
              </template>
              <div>
                <p><strong>Order ID:</strong> {{ selectedOrder.id }}</p>
                <p><strong>Total Amount:</strong> {{ selectedOrder.totalAmount }}</p>
                <p><strong>Status:</strong> {{ selectedOrder.status }}</p>
                <p><strong>Created At:</strong> {{ selectedOrder.createdAt }}</p>
              </div>
            </el-card>
          </el-col>
          <el-col :span="24">
            <el-card>
              <template #header>
                <div class="card-header">
                  <span>Products</span>
                </div>
              </template>
              <el-row v-for="product in products" :key="product.id" :gutter="10" class="product-item">
                <el-col :span="8">
                  <el-image :src="product.pictures" fit="cover" style="width: 100%; height: 100px;"></el-image>
                </el-col>
                <el-col :span="16">
                  <div>
                    <p><strong>Name:</strong> {{ product.name }}</p>
                    <p><strong>Status:</strong> {{ product.status }}</p>
                    <p><strong>Quantity:</strong> {{ product.quantity }}</p>
                  </div>
                </el-col>
              </el-row>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Close</el-button>
          <el-button type="primary" @click="handlePay">Pay Now</el-button>
          <el-button type="success" @click="handleConfirm">Confirm Receipt</el-button>
          <el-button type="danger" @click="handleCancel">Cancel Order</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { orderDetail, orderShow } from '@/api/order';
import { productInfo } from '@/api/product';
import { ElCard, ElDescriptions, ElDescriptionsItem, ElTag, ElTabs, ElTabPane, ElDialog, ElRow, ElCol, ElImage, ElButton } from 'element-plus';
</script>

<script>
export default {
  data() {
    return {
      orders: [],
      orderStatus: { 0: 'Pending payment', 1: 'shipped', 2: 'completed', 3: 'Canceled' },
      activeStatus: 0,
      dialogVisible: false,
      selectedOrder: null,
      products: []
    };
  },
  computed: {
    filteredOrders() {
      if (this.activeStatus == -1) {
        return this.orders;
      }
      return this.orders.filter(order => order.status == this.activeStatus);
    }
  },
  methods: {
    getStatusType(status) {
      const statusMap = {
        0: 'warning',
        1: 'primary',
        2: 'success',
        3: 'info'
      };
      return statusMap[status] || 'default';
    },
    openOrderDetails(order) {
      orderDetail(order.id).then(async (res) => {
        this.products = []
        await res.data.forEach(async (element) => { // 此处的element是orderitem的一个组
          let product = {}
          await productInfo(element.productId).then((pres) => {
            product = {...pres.data}
          })
          product.quantity = element.quantity
          this.products.push(product)
        });
        console.log(this.products)
        this.selectedOrder = order;
        this.dialogVisible = true;
      })
    },
    handlePay() {
      console.log('Pay Now');
      // 处理支付逻辑
    },
    handleConfirm() {
      console.log('Confirm Receipt');
      // 处理确认收货逻辑
    },
    handleCancel() {
      console.log('Cancel Order');
      // 处理取消订单逻辑
    }
  },
  mounted() {
    orderShow().then((result) => {
      this.orders = result.data
    })
  }
};
</script>

<style scoped>
.order-container {
  margin-top: 3.5rem;
}

.order-card {
  margin-bottom: 15px;
}
</style>