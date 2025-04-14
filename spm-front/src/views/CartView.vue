<script setup>
import { cartAppend, cartRemove, cartShow } from '@/api/cart';
import { productInfo } from '@/api/product';
import { storeInfo } from '@/api/store';
import { ElAffix, ElAvatar, ElButton, ElCard, ElCol, ElContainer, ElDivider, ElIcon, ElImage, ElInputNumber, ElMessage, ElRow, ElSkeleton, ElSkeletonItem, ElText } from 'element-plus';

</script>
<script>
export default {
  data() {
    return {
      cartLoading: true,
      cartDeleting: false,
      cartItems: [],
      productItems: [],
      storeItems: [],
      selectedItem: null, //当前已经选中的项目
    }
  },
  methods: {
    handleClick(index) {
      if (this.cartDeleting) {
        if (this.selectedItem != index) {
          this.selectedItem = index
          ElMessage({
            type: 'warning',
            message: 'Click Again to Confirm and Delete ' + this.productItems[index].name
          })
        } else {
          //传的物品id而不是购物车id
          cartRemove(this.productItems[index].id).then((result) => {
            this.updateCart()
            ElMessage({
              type: 'success',
              message: 'Done'
            })
          })
        }
      }
      else {
        this.$store.commit('setSharedData', { pid: this.cartItems[index].productId });
        this.$router.push("/product")
      }
    },
    updateCart() {
      this.cartLoading = true
      this.productItems = []
      this.storeItems = []
      cartShow().then(async (result) => {
        for (let item = 0; item < result.data.length; item++) {
          var product = await this.getProductInfo(result.data[item].productId)
          this.productItems.push(product)
          this.storeItems.push(await this.getStoreInfo(product.storeId))
        }
        this.cartItems = result.data
        this.cartLoading = false
      }).catch((err) => { ElMessage({ message: err }) })
    },
    async getProductInfo(pid) {
      var product = {
        "id": -1,
        "name": "[ unknown ] product",
        "description": "unknown",
        "price": 1.00,
        "stock": 0,
        "categoryId": 1,
        "pictures": "/img/icons/doubt.png",
        "status": 0,
      }
      await productInfo(pid).then((result) => {
        if (result.data != [])
          product = result.data
        console.log(result.data)
      })
      return product
    },
    async getStoreInfo(sid) {
      var store = {
        "id": -1,
        "name": "[ unknown ]",
        "description": "unknown store",
        "pictures": "/img/icons/doubt.png",
        "status": 0,
      }
      await storeInfo(sid).then((result) => {
        if (result.data != [])
          store = result.data
        console.log(result.data)
      })
      return store
    },
    handleChange(currentValue, oldValue, item) {
      cartAppend(item.productId, currentValue).catch((err) => { ElMessage({ message: err }) })
    },
    openCheckoutView(){
      for (let index = 0; index < this.productItems.length; index++) {
        this.productItems[index].quantity = this.cartItems[index].quantity;
      }
      this.$store.commit('setSharedData', { pi: this.productItems });
      this.$router.push("/checkout")
    }
  },
  mounted() {
    this.updateCart()
  }
}
</script>

<template>
  <ElRow class="card-container" style="margin-top: 3.5rem;">
    <ElCard class="custom-card">
      <div style="width: 100%; display: flex;">
        <ElButton @click="updateCart">
          <el-icon>
            <RefreshLeft />
          </el-icon>
          Fresh
        </ElButton>
        <div style="flex: 1;"></div>
        <ElButton type="danger" :plain="!cartDeleting" @click="cartDeleting = !cartDeleting">
          <el-icon>
            <Delete />
          </el-icon>
          {{ cartDeleting ? 'Deleting' : 'Delete Mode' }}
        </ElButton>
      </div>
      <ElRow>
        <ElSkeleton :loading="cartLoading" animated :count="1" v-for="(item, index) in cartItems" :key="index"
          class="card-item">
          <template #template>
            <ElSkeletonItem variant="text" style="margin-right: 16px" />
            <el-skeleton-item variant="text" style="width: 30%" />
          </template>
          <template #default>
            <ElRow class="card-box" :gutter="16" @click="handleClick(index)">
              <ElImage :src="productItems[index].pictures" fit="cover" style="height: 8rem;border-radius: 0.5rem;" />
              <ElCol style="flex:1;">
                <ElRow>
                  <ElText class="card-item-title" line-clamp="2">{{ productItems[index].name }}</ElText> <!--卡片：商品名称-->
                </ElRow>
                <ElRow class="card-store-line"> <!--卡片：商品所属商店名称、商品加入购物车事件-->
                  <ElAvatar :src="storeItems[index].picturePath"></ElAvatar>
                  <ElText class="card-store-name">{{ storeItems[index].name }}</ElText>
                  <ElText style="flex: 1;">{{ item.createdAt }}</ElText>
                </ElRow>
              </ElCol>
              <div style="justify-self: right; justify-items: flex-end;" @click.stop><!--商品价格和商品购买数量-->
                <ElText class="price-text">
                  ${{ productItems[index].price * item.quantity }}
                </ElText><br>
                <ElInputNumber v-model="item.quantity" :min="1" :max="productItems[index].stock"
                  class="card-quantity-input"
                  @change="(currentValue, oldValue) => handleChange(currentValue, oldValue, item)"
                  :disabled="cartDeleting"></ElInputNumber>
              </div>
            </ElRow>
          </template>
        </ElSkeleton>
      </ElRow>
      <ElDivider>Operations</ElDivider>
      <ElRow class="card-container">
        <ElAffix position="bottom" class="custom-card" :z-index="2" style="display: flex;">
          <ElButton @click="openCheckoutView" :disabled="cartItems == [] ? true : false" type="danger"
            style="width: 100%;">
            <ElIcon class="el-icon--left" style="margin-right: 1rem;">
              <ShoppingTrolley />
            </ElIcon>
            Let's Order
          </ElButton>
        </ElAffix>
      </ElRow>
    </ElCard>
  </ElRow>
</template>

<style scoped>
.card-item {
  height: 8rem;
  width: 100%;
}

.card-store-line {
  position: absolute;
  bottom: 0.5rem;
  line-height: 1.5rem;
  display: flex;
  width: 100%;
}

.card-store-name {
  height: 100%;
  margin: 0 1rem;
  font-weight: 500;
  font-size: 1rem;
}

.card-box {
  width: 100%;
  margin: .3rem;
  padding: 0.5rem;
  border-bottom: 1px solid var(--el-border-color);
  display: flex;
  justify-content: space-around;
  cursor: pointer;
}

.card-box:last-child {
  border-bottom: 0px none;
}

.card-box:hover {
  background-color: #ebebeb;
}

.card-item-title {
  font-size: 1.4rem;
  font-weight: 600;
}

.price-text {
  width: 100%;
  justify-self: right;
  font-size: 2.5rem;
  font-weight: bolder;
  color: #e36a00;
}

.card-quantity-input {
  position: absolute;
  right: .5rem;
  bottom: .5rem;
}

@media (min-width: 768px) {
  .card-container {
    display: flex;
    justify-content: center;
    /* 居中放置 */
    width: 100%;
  }

  .custom-card {
    width: 768px;
    max-width: 768px;
    /* 确保最大宽度为 md */
    flex: 1;
    /* 扩展 */
  }

}

/* 屏幕宽度小于 md 时 */
/* 宽度为屏幕宽度 */
@media (max-width: 767px) {
  .card-container {
    width: 100%;
  }

  .custom-card {
    width: 100%;
  }
}

@media (max-width: 512px) {
  .card-store-line {
    visibility: hidden;
  }

  .card-quantity-input {
    position: relative;
  }

}
</style>