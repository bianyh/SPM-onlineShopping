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
              <ElRow>
                <h3 style="font-weight: 500; padding-right: 0.5rem;">{{ products[pid].name }}</h3>
                <h3 style="font-weight: 400;"> × {{ order.quantities[pid] }}</h3>
              </ElRow>
              <p><strong>Status:</strong> {{ products[pid].status }}</p>
              <p><strong>Payment Amount:</strong> {{ products[pid].price * order.quantities[pid] }}</p>
              <ElAlert v-if="products[pid].trackingNumber != null" :title="products[pid].trackingNumber" type="success"
                show-icon />
            </div>
          </el-col>
        </el-row>
        <span class="dialog-footer">
          <!--el-button @click="dialogVisible = false">Close</el-button-->
          <el-button type="primary" @click="handlePay(order)" :plain="order.status == 1" :disabled="order.status != 0"
            v-if="order.status <= 2">
            {{ order.status == 0 ? "Pay Now" : "Pended" }}
          </el-button>
          <el-button type="success" @click="handleConfirm(order)" v-if="order.status == 2">Confirm
            Receipt</el-button>
          <el-button type="warning" @click="handleLogistics(order)"
            v-if="order.status > 1 && order.status < 4">Logistics
            inquiry</el-button>
          <el-button type="danger" @click="handleCancel(order)" v-if="order.status < 3">Cancel Order</el-button>
        </span>
      </ElCard>
    </el-tabs>

    <!-- 弹出页面：物流信息 -->
    <el-dialog v-model="dialogVisible" title="Logistics List" width="50%">
      <div v-for="(element, index) in logisticsData" :key="index" class="logistics-item">
        <h2 style="font-weight: 500;"> {{ products[element.productId].name }}</h2>
        <strong style="font-size: 1rem;">Tracking Number: {{ element.trackingNumber }}</strong>
        <a @click="copyToClipboard(element.trackingNumber)" style="cursor: pointer;">
          <el-icon style="width: 20px;">
            <DocumentCopy />
          </el-icon></a>
      </div>
      <ElDivider></ElDivider>
      <ElAlert
        title="The logistics waybill number is displayed here. You can go to the website to check the logistics information."
        type="success" show-icon :closable="false">
        <template #default>
          <a href="https://www.ickd.cn" target="_blank" style="font-size: 1rem;">
            <ElIcon>
              <Position />
            </ElIcon>
            www.ickd.cn
          </a>
        </template>
      </ElAlert>
    </el-dialog>

  </div>
</template>

<script setup>
import { orderConfirm, orderDetail, orderLogistics, orderShow, orderStateUpdate } from '@/api/order';
import { productInfo } from '@/api/product';
import Nodata from '@/components/Nodata.vue';
import { ElCard, ElDescriptions, ElDescriptionsItem, ElTag, ElTabs, ElTabPane, ElDialog, ElRow, ElCol, ElImage, ElButton, ElMessage, ElMessageBox, ElAlert, ElDivider } from 'element-plus';
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
      logisticsData: []
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
      orderConfirm(order.id).then((res) => {
      ElMessage({ message: 'Confmired.', type: 'success' })
      }).catch((err) => {
        ElMessage({ message: err, type: 'error' })
      })
      //orderStateUpdate(order.id, order.status)
    },
    handleLogistics(order) {
      // 处理物流查询逻辑
      orderLogistics(order.id).then((res) => {
        this.logisticsData = res.data
        this.dialogVisible = true
      })
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
    },
    //写入剪贴板
    copyToClipboard(text) {
      // 检查浏览器是否支持剪贴板 API
      if (navigator.clipboard) {
        navigator.clipboard.writeText(text)
          .then(() => {
            console.log('Content copied to clipboard successfully.');
          })
          .catch(err => {
            console.error('Failed to copy content to clipboard:', err);
          });
      } else {
        console.error('Clipboard API not supported in this browser.');
      }
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