<script setup>
import { cartAppend } from '@/api/cart';
import { productCommentCount, productCommentDelete, productCommentGet, productCommentPost, productInfo } from '@/api/product';
import { storeInfo } from '@/api/store';
import MessageBus from '@/utils/MessageBus';
import { ElAffix, ElAside, ElAvatar, ElButton, ElCard, ElCol, ElContainer, ElDescriptions, ElDescriptionsItem, ElDialog, ElDivider, ElDrawer, ElFooter, ElHeader, ElIcon, ElImage, ElInputNumber, ElMain, ElMessage, ElMessageBox, ElPageHeader, ElPagination, ElRate, ElRow, ElTag, ElText, } from 'element-plus';
</script>

<script>
export default {
    data() {
        return {
            uid: -1,
            product: null,
            productDescription: "",
            productRate: 5,
            store: null,
            commentVisible: false,
            commentLoading: false,
            commentText: "",
            commentRate: 5,
            drawerVisible: false,
            drawerLoading: false,
            drawerItemDirection: 'horizontal',
            purchaseQuantity: 1,
            comments: [],
            total: 1,
            page: 1,
            limit: 3,
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
            return this.getRateText(this.productRate)
        },
        commentRateText() {
            return this.getRateText(this.commentRate)
        },
        productDescriptionText() {

        }
    },
    methods: {
        handleComment() {
            this.commentLoading = true
            //后端其实能自动获取uid的
            let uid = window.localStorage.getItem("uid")
            productCommentPost(uid, this.product.id, this.commentRate, this.commentText).then((result) => {
                this.commentLoading = false
                this.commentVisible = false
                ElMessage({ message: "Comment successfully submitted!" })
            }, (err) => {
                MessageBus.emit('box', err)
                this.commentLoading = false
            })
        },
        getRateText(rate) {
            if (rate > 4) {
                return "Perfect!"
            } else if (rate > 3) {
                return "Great!"
            } else if (rate > 2) {
                return "Good"
            } else if (rate > 1) {
                return "Not bad"
            } else {
                return "Bad"
            }
        },
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
                    window.localStorage.setItem("navigationParams", "2")
                    this.$router.push('/user')
                }
                this.drawerLoading = false
            }, (err) => {
                MessageBus.emit('box', err)
                this.drawerLoading = false
            })
        },
        handlePurchase() {
            this.drawerLoading = true
            var productItems = this.product
            productItems.quantity = this.purchaseQuantity
            this.$store.commit('setSharedData', { pi: [productItems] });
            this.$router.push("/checkout")
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
        },
        handleClickStore() {
            this.$store.commit('setSharedData', { sid: this.store.id });
            this.$router.push("/store")
        },
        handlePageChange(page) {
            this.page = page
            productCommentGet(this.product.id, this.page, this.limit).then(res => {
                this.comments = res.data
            })
        },
        handleCommentDelete(id) {
            this.commentLoading = true
            productCommentDelete(id).then((result) => {
                this.commentLoading = false
                ElMessage({ message: "Comment successfully deleted!" })
                this.comments = this.comments.filter(comment => comment.id !== id)
            }, (err) => {
                MessageBus.emit('box', err)
                this.commentLoading = false
            })
        }
    },
    mounted() {
        this.uid = window.localStorage.getItem("uid")
        if (this.$store.state.sharedData?.pid) {
            window.localStorage.setItem("productview_pid", this.$store.state.sharedData.pid)
        }
        let pid = window.localStorage.getItem("productview_pid")
        if (pid) {
            productInfo(pid).then((result) => {
                this.product = result.data
                this.productRate = this.product.rate
                storeInfo(result.data.storeId).then((storeinfo) => {
                    this.store = storeinfo.data
                })
            }, (err) => { ElMessage({ message: err }) })
            productCommentCount(pid).then(res => {
                if (res.data) {
                    this.total = res.data
                    productCommentGet(pid, this.page, this.limit).then(res2 => {
                        this.comments = res2.data
                    })
                }
            })
        }
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
                        <ElRate v-model="productRate" disabled text-color="#ff9900" size="large"
                            style="scale: 2;margin-left: 4rem;" />
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
                <ElCard @click="handleClickStore" style="cursor: pointer;">
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
                <ElDivider>Comments</ElDivider>
                <div style="cursor: pointer;" v-for="(comment, index) in comments" :key="index" class="comment-card">
                    <ElContainer style="justify-items: center;">
                        <div style="font-size: 1.8rem; display: flex; justify-content: end; color:orange; margin-right: 1rem;">
                            {{ comment.rating }}
                                <Star style="width: 2rem; height: 2rem; margin-top: 0.5rem;" />
                        </div>
                        <ElContainer direction="vertical">
                            <h2>{{ comment?.content }}</h2>
                            <h4 style="color: darkgrey;">{{ comment?.createdAt }}</h4>
                        </ElContainer>
                        <div>
                            <h4 style="color: gray;">UID: {{ comment?.userId }}</h4>
                            <a style="width: 100%; display: flex; justify-content: right;" v-if="uid == comment?.userId" @click="handleCommentDelete(comment.id)">
                                <el-icon>
                                    <Delete />
                                </el-icon></a>
                        </div>
                    </ElContainer>
                    <div class="divider"></div>
                </div>
                <ElPagination :page-size="limit" v-model="page" :total="total" layout="total, sizes, prev, pager, next"
                    @current-change="handlePageChange">
                </ElPagination>
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
        <div style="position: fixed; right:1rem; bottom: 5rem; width: 5rem; height: 1rem;"
            @click="commentVisible = true">
            <ElCard style="border-radius: 4px; font-size: 2rem;">
                <Comment style="width: 100%;" />
            </ElCard>
        </div>
    </ElRow>
    <ElDialog v-model="commentVisible" class="card-container" :show-close="true" :close-on-click-modal="false"
        :close-on-press-escape="false" :title="'Comment for ' + product?.name">
        <ElRate v-model="commentRate" text-color="#ff9900" class="card-container" style="scale: 2;" />
        <h3 class="card-container" style="font-size: 2rem;">{{ commentRateText }}</h3>
        <ElInput v-model="commentText" type="textarea" :rows="4" placeholder="Please leave your comment here"
            style="width: 100%; margin: 1rem 0;" />
        <ElButton type="primary" @click="handleComment">Submit</ElButton>
    </ElDialog>
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

.comment-card {
    width: 100%;
    font-size: 1.2rem;
    border: 0px 0px 0px 0.2rem solid #434343;
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