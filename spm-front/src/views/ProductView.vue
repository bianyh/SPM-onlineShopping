<script setup>
import { productInfo } from '@/api/product';
import { storeInfo } from '@/api/store';
import { ElAffix, ElAside, ElAvatar, ElButton, ElCard, ElCol, ElContainer, ElDivider, ElFooter, ElHeader, ElIcon, ElImage, ElMain, ElMessage, ElPageHeader, ElRate, ElRow, ElText } from 'element-plus';
</script>

<script>
export default {
    data() {
        return {
            product: null,
            productRate: 0,
            store: null
        }
    },
    computed: {
        productPicture() {
            if (this.product)
                return this.product.pictures
            else
                return ''
        }
    },
    methods: {
        handleAddToCart() {
            ElMessage({
                message: '没做喵。',
                type: 'warning',
            })
        },
        updateProduct() {

        }
    },
    mounted() {
        if (this.$store.state.sharedData?.pid)
            productInfo(this.$store.state.sharedData?.pid).then((result) => {
                this.product = result.data
                this.productRate = this.product.rate
                storeInfo(result.data.storeId).then((storeinfo) => {
                    console.log(storeinfo)
                    this.store = storeinfo.data
                })
            }, (err) => { ElMessage({ message: err }) })
        else
            this.$router.back()
    }
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
                        该商品还在审核中，无法购买 / The product is still under review and cannot be purchased.
                    </ElButton>
                </ElRow>
                <ElRow>
                    <ElText tag="b" class="product-title" line-clamp="2" wrap>
                        {{ product?.name }}
                    </ElText>

                </ElRow>
                <ElDivider></ElDivider>
                <ElRow justify="space-between" class="price-text">
                    <ElRate v-model="productRate" disabled text-color="#ff9900" size="large" />
                    ${{ product?.price }}
                </ElRow>
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
        <ElAffix position="bottom" class="custom-card" style="background-color: #000;">
            <ElButton @click="handleAddToCart" :loading="product?.status == 0 ? true : false"
                style="width: 768px; max-width: 100%; height: 3.5rem; font-size: 1.2rem;" type="danger">
                <ElIcon class="el-icon--left" style="margin-right: 1rem;">
                    <ShoppingTrolley />
                </ElIcon>
                忍不了了，立即选购！ <br> PURCHASE NOW!
            </ElButton>

        </ElAffix>

        </div>

    </ElRow>
</template>

<style scoped>
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