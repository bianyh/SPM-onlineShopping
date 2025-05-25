<template>
  <div style="margin-top: 3.5rem;">
    <el-card shadow="hover">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-image :src="store.picturePath" fit="cover" style="width: 100%; height: 150px;"></el-image>
        </el-col>
        <el-col :span="16">
          <div>
            <h2 style="font-weight: 600;">{{ store.name }}</h2>
            <p>{{ store.description }}</p>
            <p>Address: {{ store.address }}</p>
            <p>Created At: {{ store.createAt }}</p>
            <el-tag :type="getStatusType(store.status)">{{ storeStatus[store.status] }}</el-tag>
          </div>
        </el-col>
      </el-row>

      <div style="margin-top: 15px;" v-if="isAdmin">
        <el-button type="primary" @click="openEditDialog">Edit Store</el-button>
        <el-button @click="toggleStoreStatus">{{ store.status === 0 ? 'Close Store' : 'Open Store' }}</el-button>
        <el-button type="danger" @click="deleteStore">Delete Store</el-button>
        <el-button type="success" @click="gotoNewProduct">Post New Product</el-button>
      </div>
    </el-card>

    <div style="width: 100%; height: 400px; margin: 0; background: #fff;">
      <ElText style="font-size: 2rem; font-weight: 500;margin: 0.2rem 2rem;">Sales statistics</ElText>
      <ElSegmented v-model="chartOption" :options="chartOptions" style="margin: 20px; float: right;"  @change="optionChange" />
      <div ref="chartRef" style="width: 100%; height: 400px; transform: translateY(-30px);"></div>
    </div>

    <Goodsmanage :products="products" @edit="(pid) => gotoEditProduct(pid)" :admin="isAdmin" />
    <ElPagination :current-page="page" :total="total" :default-page-size="10" layout="prev, pager, next">
    </ElPagination>

    <!-- 弹出编辑对话框 -->
    <el-dialog v-model="dialogVisible" title="Edit Store" width="90%">
      <el-form ref="editForm" :model="editStore" label-width="120px">
        <el-form-item label="Name">
          <el-input v-model="editStore.name"></el-input>
        </el-form-item>
        <el-form-item label="Description">
          <el-input type="textarea" v-model="editStore.description"></el-input>
        </el-form-item>
        <el-form-item label="Store Address">
          <el-input v-model="editStore.address"></el-input>
        </el-form-item>
        <el-form-item label="Picture Path">
          <div class="image-uploader">
            <input type="file" accept="image/*" @change="handleImageUpload" hidden ref="fileInput" />
            <div class="upload-area" @click="$refs.fileInput.click()">
              <span v-if="!previewImage" class="upload-text">Click to upload a new image</span>
              <template v-else>
                <img :src="previewImage" class="preview-image" alt="商品预览图" />
                <button class="remove-btn" @click.stop="removeMainImage">
                  ×
                </button>
              </template>
            </div>
          </div>
          <el-input v-model="editStore.picturePath" :disabled="isImageUpload" />
          <el-alert title="Note: You can directly enter the image URL, or upload the local image as usual." type="info"
            :closable="false" />
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

    <el-dialog v-model="detailDialogVisible" title="Sales Detail" width="90%">
      <ElTimePicker></ElTimePicker>
            <div ref="chart2Ref" style="width: 100%; height: 400px; transform: translateY(-30px);"></div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ElMessage, ElPagination, ElSegmented, ElText, ElTimePicker } from 'element-plus';
import * as echarts from 'echarts';
</script>

<script>
import { storeInfo, storeProducts, storeSalesDetail, storeUpdate } from '@/api/store';
import Goodsmanage from './Goodsmanage.vue';
import { uploadImage } from '@/api/imgio';

export default {
  components: {
    Goodsmanage,
  },
  data() {
    return {
      store: {
        id: '-1',
        name: 'Loading...',
        description: 'description',
        address: 'address',
        picturePath: '',
        createAt: new Date(),
        status: '0'
      },
      isImageUpload: false,
      storeStatus: ['Open', 'Closed'],
      dialogVisible: false,
      detailDialogVisible: false,
      imageUploading: false,
      previewImage: null,
      fileInput: null,
      editStore: {},
      products: [],
      page: 1,
      limit: 10,
      total: 0,
      chartInstance: null,
      chartData: [],
      chartOptions: ['Total','Ordered', 'Success', 'Canceled'],
      chartOption: 'Total',
    };
  },
  methods: {
    getStatusType(status) {
      const statusMap = {
        0: 'success',
        1: 'info'
      };
      return statusMap[status] || 'primary';
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
      const nextIndex = (this.store.status + 1) % this.storeStatus.length;
      this.store.status = nextIndex;
    },
    deleteStore() {
      console.log('Delete store function called');
      // 实现删除逻辑
    },
    gotoNewProduct() {
      this.$store.commit('setSharedData', { s: this.store, pid: null, sid: this.store.id });
      this.$router.push('/product/create')
    },
    gotoEditProduct(pid) {
      console.log(pid)
      this.$store.commit('setSharedData', { s: this.store, pid: pid, sid: this.store.id });
      this.$router.push('/product/edit')
    },
    handleImageUpload(event) {
      const file = event.target.files[0];
      this.imageUploading = true
      if (file) {
        this.isImageUpload = true
        uploadImage(file, "store", this.store.id, 0).then((res) => {
          ElMessage({ message: "Picture Upload Success.", type: 'success' })
          //console.log(res)
          this.imageUploading = false
          this.editStore.picturePath = 'http://127.0.0.1:8080/image/get/' + res.data.filePath
        }).catch((err) => {
          ElMessage({ message: err })
          this.imageUploading = false
        })
        const reader = new FileReader();
        reader.onload = (e) => {
          this.previewImage = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },
    removeMainImage() {
      this.previewImage = null;
      // 清空文件输入框的值
      if (this.fileInput) {
        this.fileInput.value = "";
      }
      this.isImageUpload = false
    },
    initChart() {
      this.chartInstance = echarts.init(this.$refs.chartRef);
      this.optionChange(this.chartOption)
      // 添加点击事件监听器
      this.chartInstance.on('click', (params) => {
        const storeId = this.stores[params.dataIndex].storeId;
        this.handleClickNavigateToStore(storeId);
      });
    },
    optionChange(value) {
      this.chartOption = value
      let productName = this.chartData.map(store => store.productName);
      let volume

      if (this.chartOption == 'Ordered') {
        volume = this.chartData.map(store => store.orderVolume);
      } else if (this.chartOption == 'Success') {
        volume = this.chartData.map(store => store.achieveVolume);
      } else if (this.chartOption == 'Canceled') {
        volume = this.chartData.map(store => store.cancelVolume);
      } else {
        volume = this.chartData.map(store => store.salesVolume);
      }

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
        },
        legend: {
          data: ['Sales'],
        },
        xAxis: {
          type: 'value',
        },
        yAxis: {
          type: 'category',
          data: productName,
        },
        series: [
          {
            name: 'Sales',
            type: 'bar',
            data: volume,
          },
        ],
      };

      this.chartInstance.setOption(option);
    },
    handleClickNavigateToStore(id) {
      this.$store.commit('setSharedData', { sid: id });
      this.$router.push("/store")
    }
  },
  mounted() {
    if (this.$store.state.sharedData?.sid) {
      window.localStorage.setItem("storeview_sid", this.$store.state.sharedData.sid)
    }
    let sid = window.localStorage.getItem("storeview_sid")
    if (sid == null) {
      console.error('解析商品数据失败');
      //this.$router.back();
    } else {
      storeInfo(sid).then((res) => {
        this.store = res.data
        storeProducts(this.store.id, this.page, this.limit).then((res) => {
          this.products = res.data.data
          this.total = res.data.total
          console.log(res.data)
        })
      })
      storeSalesDetail(sid).then((res) => {
        this.chartData = res.data
        this.initChart()
      }).catch((err) => {
        console.error(err)
      })
    }


  },
  computed: {
    isAdmin() {
      if (window.localStorage.getItem("uid") == this.store.userId)
        return true
      if (window.localStorage.getItem("admin") == "true")
        return true
      return false
    }
  }
};
</script>

<style scoped>
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
  border-color: #f9b9b9;
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
</style>