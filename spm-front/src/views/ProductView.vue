<script setup>
import { cartAppend } from '@/api/cart';
import { productInfo } from '@/api/product';
import { storeInfo } from '@/api/store';
import MessageBus from '@/utils/MessageBus';
import { ElAffix, ElAside, ElAvatar, ElButton, ElCard, ElCol, ElContainer, ElDescriptions, ElDescriptionsItem, ElDivider, ElDrawer, ElFooter, ElHeader, ElIcon, ElImage, ElInputNumber, ElMain, ElMessage, ElMessageBox, ElPageHeader, ElRate, ElRow, ElTag, ElText, } from 'element-plus';
</script>

<script>
export default {
    data() {
        return {
            product: null,
            productDescription: "",
            productRate: 0,
            store: null,
            drawerVisible: false,
            drawerLoading: false,
            drawerItemDirection: 'horizontal',
            purchaseQuantity: 1,
        }
    },
    computed: {
        productPicture() {
            if (this.product)
                return this.product.pictures
            else
                return ''
        },
        productRateText() {
            if (this.productRate > 4) {
                return "Perfect!"
            } else if (this.productRate > 3) {
                return "Great!"
            } else if (this.productRate > 2) {
                return "Good"
            } else if (this.productRate > 1) {
                return "Normal"
            } else {
                return "Miss"
            }
        },
        productDescriptionText() {

        }
    },
    methods: {
        handleAddtoCart() {
            this.drawerLoading = true
            var gotoCart = false
            cartAppend(this.product.id, this.purchaseQuantity).then(async (result) => {
                this.drawerVisible = false
                await ElMessageBox.confirm("Your product has been successfully added to your shopping cart. Do you want to go and check now?", 'Everything has been settled!', {
                    //autofocus: false,
                    confirmButtonText: 'Check My Cart',
                    cancelButtonText: 'Stay Here',
                }).then(() => {
                    gotoCart = true
                })
                if (gotoCart) {
                    this.$router.push('/cart')
                }
                this.drawerLoading = false
            }, (err) => {
                MessageBus.emit('box', err)
                this.drawerLoading = false
            })
        },
        handlePurchase() {
            this.drawerLoading = true
        },
        openPurchaseDrawer() {
            this.drawerVisible = true
        },
        handleResize() {
            // 根据屏幕宽度设置抽屉物件方向
            if (window.innerWidth >= 768) {
                this.drawerItemDirection = 'horizontal'; // 大于等于 768px 时横向显示
            } else {
                this.drawerItemDirection = 'vertical'; // 小于 768px 时纵向显示
            }
        }
    },
    mounted() {
        if (this.$store.state.sharedData?.pid)
            productInfo(this.$store.state.sharedData?.pid).then((result) => {
                this.product = result.data
                this.productRate = this.product.rate
                storeInfo(result.data.storeId).then((storeinfo) => {
                    this.store = storeinfo.data
                })
            }, (err) => { ElMessage({ message: err }) })
        else
            this.$router.back()
        // 监听窗口大小变化
        window.addEventListener('resize', this.handleResize);
        this.handleResize(); // 初始化检查
    },
    beforeDestroy() {
        // 组件销毁时移除事件监听
        window.removeEventListener('resize', this.handleResize);
    },
}
</script>

<template>
    <ElRow style="position: relative; width: 100%; top: 4rem;">
        <div class="card-container">
            <ElCard class="custom-card">
                <ElImage class="el-img" :src="product?.pictures" fit="contain" show-progress
                    :preview-src-list="[product?.pictures]">
                </ElImage>
                <ElRow>
                    <ElButton type="warning" plain v-if="product?.status == 0"
                        style="white-space: normal; height: fit-content;">
                        <ElIcon>
                            <WarningFilled />
                        </ElIcon>
                        The product is still under review and cannot be purchased.
                    </ElButton>
                </ElRow>
                <ElRow>
                    <ElText tag="b" class="product-title" line-clamp="2" wrap>
                        {{ product?.name }}
                    </ElText>

                </ElRow>
                <ElDivider>Price and rating</ElDivider>
                <ElRow justify="space-between">
                    <div style="color: #d66; font-size: 1.5rem;">
                        <ElRate v-model="productRate" disabled text-color="#ff9900" size="large" />
                        <br>
                        Rating: {{ productRate }} {{ productRateText }}
                    </div>
                    <div class="price-text">
                        ${{ product?.price }}
                    </div>
                </ElRow>
                <ElDivider>Description</ElDivider>
                <ElText style="margin: 1rem; font-size: 1.2rem; white-space: pre-line; width: 100%;" wrap>
                    {{ product?.description }}
                </ElText>
                <ElDivider>From Store</ElDivider>
                <ElCard>
                    <ElContainer style="justify-items: center;">
                        <ElAside width="4rem" style="margin-right: 1rem;">
                            <ElAvatar size="large" :src="store?.picturePath" alt=""><img src="/img/icons/doubt.png" />
                            </ElAvatar>
                        </ElAside>
                        <ElContainer direction="vertical">
                            <h2>{{ store?.name }}</h2>
                            <h4 style="color: darkgrey;">{{ store?.description }}</h4>
                        </ElContainer>
                    </ElContainer>
                </ElCard>
            </ElCard>

        </div>
        <div class="card-container">
            <ElAffix position="bottom" class="custom-card" style="background-color: #000;" :z-index="2">
                <ElButton @click="openPurchaseDrawer" :disabled="product?.status == 0 ? true : false" type="danger"
                    class="custom-card-button">
                    <ElIcon class="el-icon--left" style="margin-right: 1rem;">
                        <ShoppingTrolley />
                    </ElIcon>
                    忍不了了，立即选购！ <br> PURCHASE NOW!
                </ElButton>
            </ElAffix>
        </div>
    </ElRow>
    <ElDrawer v-model="drawerVisible" :show-close="true" direction="btt" :with-header=true :z-index=3 size="30rem"
        :title="product?.name" :loading="drawerLoading">
        <el-descriptions class="margin-top" title="Product specifications" :column="1" size="large" border
            :direction="drawerItemDirection">
            <el-descriptions-item>
                <template #label>
                    <div class="cell-item">
                        <el-icon>
                            <Document />
                        </el-icon>
                        Description
                    </div>
                </template>
                {{ product?.description }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <div class="cell-item">
                        <el-icon>
                            <iphone />
                        </el-icon>
                        Appeared in stores
                    </div>
                </template>
                {{ product?.createdAt }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <div class="cell-item">
                        <el-icon>
                            <Files />
                        </el-icon>
                        Category
                    </div>
                </template>
                <div>
                    <ElTag>{{ product?.categoryId }}</ElTag>
                </div>
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <div class="cell-item">
                        <el-icon>
                            <Van />
                        </el-icon>
                        Stock
                    </div>
                </template>
                {{ product?.stock }}
            </el-descriptions-item>
            <el-descriptions-item>
                <template #label>
                    <div class="cell-item">
                        <el-icon>
                            <office-building />
                        </el-icon>
                        Stock in
                    </div>
                </template>
                {{ store?.address }}
            </el-descriptions-item>
        </el-descriptions>
        <div style=" font-size: 1.2rem; width: 95%; padding: 1rem; background: #00000000;" :z-index="3">
            <div style="justify-self: left;">
                Purchase quantity
                <ElInputNumber v-model="purchaseQuantity" :min="1" :max="product?.stock" size="large">
                    <template #suffix>
                        <span>Item</span>
                    </template>
                </ElInputNumber>
            </div>
            <div style="justify-self: right;">
                <ElText size="large">Price: </ElText>
                <ElText class="price-text">
                    ${{ product?.price * purchaseQuantity }}
                </ElText>
            </div>
            <ElDivider></ElDivider>
            <ElRow style="width: 100%; height: 100%;" justify="space-evenly">
                <ElCol :span="11">
                    <ElButton class="card-button" type="info" @click="handleAddtoCart">Add to Cart</ElButton>
                </ElCol>
                <ElCol :span="11">
                    <ElButton class="card-button" type="primary" @click="handlePurchase">Buy Now</ElButton>
                </ElCol>
            </ElRow>
        </div>
    </ElDrawer>
</template>

<style scoped>
div * {
    font-size: 1rem;
}

.product-title {
    font-size: 2rem;
}

.price-text {
    font-size: 2.5rem;
    font-weight: bolder;
    color: #e36a00;
}

.el-img {
    height: 20rem;
    display: flex;
    width: 700px;
    margin-bottom: 0.2rem;
}

.custom-card-button {
    width: 768px;
    max-width: 100%;
    height: 3.5rem;
    font-size: 1.2rem;
}

.card-button {
    width: 100%;
    height: 3.5rem;
    font-size: 1.2rem;
}

/* 定义断点，例如 md 为 768px */
@media (min-width: 768px) {
    .card-container {
        display: flex;
        justify-content: center;
        /* 居中放置 */
        width: 100%;
    }

    .custom-card {
        width: 768px;
        /* 设置宽度为 md */
        max-width: 768px;
        /* 确保最大宽度为 md */
        display: flex;
        justify-content: space-around;
        /* 水平居中 */
        /* align-items: center;  垂直居中 */
        /* height: 200px;  设置内容区域的高度 */
    }
}

/* 屏幕宽度小于 md 时 */
@media (max-width: 767px) {
    .card-container {
        width: 100%;
    }

    .el-img {
        width: 100%;
    }

    .custom-card {
        width: 100%;
        /* 宽度为屏幕宽度 */
    }
}
</style>