<template>
  <div style="margin-top: 3.5rem;">
    <el-card class="store-card" shadow="hover">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-image :src="store.picturePath" fit="cover" style="width: 100%; height: 150px;"></el-image>
        </el-col>
        <el-col :span="16">
          <div>
            <h3>{{ store.name }}</h3>
            <p>{{ store.description }}</p>
            <p>Address: {{ store.address }}</p>
            <p>Created At: {{ store.createAt }}</p>
            <el-tag :type="getStatusType(store.status)">{{ store.status }}</el-tag>
          </div>
        </el-col>
      </el-row>

      <div style="margin-top: 15px;">
        <el-button @click="openEditDialog">Edit Store</el-button>
        <el-button @click="toggleStoreStatus">{{ store.status === 'Open' ? 'Close Store' : 'Open Store' }}</el-button>
        <el-button type="danger" @click="deleteStore">Delete Store</el-button>
        <el-button @click="gotoNewProduct">Post New Product</el-button>
      </div>
    </el-card>

    <Goodsmanage :products="products" @edit="(pid) => gotoEditProduct(pid)"/>
    <ElPagination :current-page="page" :total="total" :default-page-size="10" layout="prev, pager, next">
    </ElPagination>

    <!-- 弹出编辑对话框 -->
    <el-dialog v-model="dialogVisible" title="Edit Store" width="50%">
      <el-form ref="editForm" :model="editStore" label-width="120px">
        <el-form-item label="Name">
          <el-input v-model="editStore.name"></el-input>
        </el-form-item>
        <el-form-item label="Description">
          <el-input type="textarea" v-model="editStore.description"></el-input>
        </el-form-item>
        <el-form-item label="Address">
          <el-input v-model="editStore.address"></el-input>
        </el-form-item>
        <el-form-item label="Picture Path">
          <el-input v-model="editStore.picturePath"></el-input>
        </el-form-item>
        <el-form-item label="Create At">
          <el-date-picker v-model="editStore.createAt" type="datetime" disabled></el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="saveEdit">Save</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ElMessage, ElPagination } from 'element-plus';
</script>

<script>
import { storeInfo, storeProducts, storeUpdate } from '@/api/store';
import Goodsmanage from './Goodsmanage.vue';

export default {
  components: {
    Goodsmanage,
  },
  data() {
    return {
      store: {
        id: 'ST001',
        name: 'Store 1',
        description: 'This is the first store.',
        address: '123 Main St',
        picturePath: 'https://via.placeholder.com/300',
        createAt: new Date(),
        status: 'Open'
      },
      storeStatus: ['Open', 'Closed'],
      dialogVisible: false,
      editStore: {},
      products: [],
      page: 1,
      limit: 10,
      total: 0,

    };
  },
  methods: {
    getStatusType(status) {
      const statusMap = {
        'Open': 'success',
        'Closed': 'info'
      };
      return statusMap[status] || 'default';
    },
    openEditDialog() {
      this.editStore = { ...this.store };
      this.dialogVisible = true;
    },
    saveEdit() {
      this.store = { ...this.editStore };
      storeUpdate(
        this.store.name,
        this.store.description,
        this.store.picturePath,
        this.store.id,
        this.store.status,
        this.store.address,
      ).then((res) => {
        ElMessage({ message: res })
      })
      this.dialogVisible = false;
    },
    toggleStoreStatus() {
      const currentIndex = this.storeStatus.indexOf(this.store.status);
      const nextIndex = (currentIndex + 1) % this.storeStatus.length;
      this.store.status = this.storeStatus[nextIndex];
    },
    deleteStore() {
      console.log('Delete store function called');
      // 实现删除逻辑
    },
    gotoNewProduct() {
      this.$store.commit('setSharedData', { s: this.store, pid: null });
      this.$router.push('/product/create')
    },
    gotoEditProduct(pid) {
      console.log(pid)
      this.$store.commit('setSharedData', { s: this.store, pid: pid });
      this.$router.push('/product/edit')
    }
  },
  mounted() {
    if (this.$store.state.sharedData?.sid) {
      storeInfo(this.$store.state.sharedData.sid).then((res) => {
        this.store = res.data
        storeProducts(this.store.id, this.page, this.limit).then((res) => {
          this.products = res.data.data
          this.total = res.data.total
          console.log(res.data)
        })
      })
    } else {
      console.error('解析商品数据失败');
      this.$router.back();
    }
  }
};
</script>

<style scoped>
.store-card {
  margin-bottom: 15px;
}
</style>