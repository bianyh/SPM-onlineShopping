<template>
  <div class="order-container">
    <el-tabs v-model="activeStatus" class="demo-tabs" type="border-card">
      <el-tab-pane label="All" name=-1></el-tab-pane>
      <el-tab-pane v-for="(status, index) in orderStatus" :key="index" :label="status" :name="index"></el-tab-pane>
      <Nodata v-if="filteredOrders.length == 0" />
      <ElCard v-for="order in filteredOrders" :key="order.id" class="order-card" @click="openOrderDetails(order)"
        v-loading="!isFullLoaded" element-loading-text="Loading..."
        element-loading-background="rgba(122, 122, 122, 0.8)">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Order ID">{{ order.id }}</el-descriptions-item>
          <el-descriptions-item label="Total Amount">{{ order.totalAmount }}</el-descriptions-item>
          <el-descriptions-item label="Status">
            <el-tag :type="getStatusType(order.status)">{{ orderStatus[order.status] }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Created At">{{ order.createdAt }}</el-descriptions-item>
        </el-descriptions>
        <el-row v-for="pid in order?.products" :key="pid" :gutter="10" class="product-item" v-if="isFullLoaded">
          <el-col :span="8">
            <el-image :src="products[pid].pictures" fit="cover" style="width: 100%; height: 100px;"
              @click="handleProductClick(pid)" />
          </el-col>
          <el-col :span="16">
            <div>
              <p><strong>Name:</strong> {{ products[pid].name }}</p>
              <p><strong>Status:</strong> {{ products[pid].status }}</p>
              <p><strong>Quantity:</strong> {{ order.quantities[pid] }}</p>
              <p><strong>Payment Amount:</strong> {{ products[pid].price * order.quantities[pid] }}</p>
            </div>
          </el-col>
        </el-row>
        <span class="dialog-footer" v-if="order.status != 3 && order.status != 4">
          <!--el-button @click="dialogVisible = false">Close</el-button-->
          <el-button type="primary" @click="handlePay(order)" :plain="order.status == 1" :disabled="order.status != 0">
            {{ order.status == 0 ? "Pay Now" : "Pended" }}
          </el-button>
          <el-button type="success" @click="handleConfirm(order)" :disabled="order.status != 2">Confirm
            Receipt</el-button>
          <el-button type="danger" @click="handleCancel(order)"
            :disabled="order.status == 3 || order.status == 4">Cancel Order</el-button>
        </span>
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
        </el-row>
      </div>
      <template #footer>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { orderDetail, orderShow, orderStateUpdate } from '@/api/order';
import { productInfo } from '@/api/product';
import Nodata from '@/components/Nodata.vue';
import { ElCard, ElDescriptions, ElDescriptionsItem, ElTag, ElTabs, ElTabPane, ElDialog, ElRow, ElCol, ElImage, ElButton, ElMessage, ElMessageBox } from 'element-plus';
</script>

<script>
export default {
  data() {
    return {
      orders: [],
      orderStatus: { 0: 'Pending payment', 1: 'waiting for shipping', 2: 'shipped', 3: 'completed', 4: 'Canceled' },
      activeStatus: 0,
      dialogVisible: false,
      selectedOrder: null,
      products: [],
      isFullLoaded: false,
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
        3: 'info',
        4: 'danger'
      };
      return statusMap[status] || 'default';
    },
    async getProductsByIds(ids) {
      let result = {}
      for (let element of ids) { // 使用 for...of 替代 forEach
        let product = {}
        await productInfo(element).then((pres) => {
          product = { ...pres.data }
        })
        result[product.id] = product
      }
      console.log("product end")
      return result
    },
    async openOrderDetails(order) { },
    async getOrderDetails(order) {
      if (order.isOpened) return
      await orderDetail(order.id).then(async (res) => {
        order.quantities = {}
        order.products = []
        for (let element of res.data) { // 使用 for...of 替代 forEach
          console.log("包含商品：", element.productId)
          this.products.push(element.productId)
          order.products.push(element.productId)
          order.quantities[element.productId] = element.quantity
        }
        order.isOpened = true
      })
      return
    },
    handlePay(order) {
      //console.log('Pay Now');
      window.localStorage.setItem("payOrderId", order.id)
      console.log(order)
      this.$router.push("/payment")
      //order.status = 1
      //orderStateUpdate(order.id, order.status)
      //ElMessage({ message: 'Done..', type: 'success' })
    },
    handleConfirm(order) {
      console.log('Confirm Receipt');
      // 处理确认收货逻辑
      order.status = 3
      orderStateUpdate(order.id, order.status)
      ElMessage({ message: 'Confmired.', type: 'success' })
    },
    handleCancel(order) {
      // 处理取消订单逻辑
      ElMessageBox.confirm(
        'The Order will be canceled and can NOT be recovered. Continue?',
        'Warning',
        {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'warning',
        }
      )
        .then(() => {
          ElMessage({
            type: 'success',
            message: 'Delete completed',
          })
          order.status = 4
          orderStateUpdate(order.id, order.status)
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: 'Delete canceled',
          })
        })
    },
    handleProductClick(pid) {
      this.$store.commit('setSharedData', { "pid": pid });
      this.$router.push("/product")
    }
  },
  mounted() {
    orderShow().then(async (result) => {
      this.orders = result.data
      this.products = []
      for (let i = 0; i < this.orders.length; i++) {
        await this.getOrderDetails(this.orders[i])
      }
      // 去重数组
      this.products = Array.from(new Set(this.products))
      this.products = await this.getProductsByIds(this.products)
      this.isFullLoaded = true
      console.log({ ...this.products })
      console.log("loaded")
    })
  }
};
</script>

<style scoped>
.order-card {
  margin-bottom: 15px;
}
</style>