//ProductSearchView.vue
<script setup>
import { ElCard, ElContainer, ElInput, ElButton, ElPagination, ElRow, ElSkeleton, ElSkeletonItem, ElSpace, ElImage, ElSwitch, ElMessage, ElText, ElRate, ElOption, ElSelect, ElDivider, ElTable, ElAvatar, ElCol } from 'element-plus';
</script>

<script>
import { searchProduct } from '@/api/product';
import MessageBus from '@/utils/MessageBus';
export default {
    data() {
        return {
            content: window.localStorage.getItem('searchContent'),//ⓘⓘ
            products: [],
            defaultProducts: [],
            page: 1,
            limit: 20,
            total: 1,
            loading: true,
            searchSelect: 'Products',
        }
    },
    mounted() {
        MessageBus.on("search", (content) => {
            this.updateSearch(content)
        })
        this.handleSearch()
    },
    unmounted() {
        MessageBus.off("search")
    },
    methods: {
        handleSearch() {
            this.loading = true
            if (this.content == '') {
                this.products = []
                searchProduct('', 1, 4).then((result) => {
                    this.defaultProducts = result.data.products
                })
                this.page = 1
                this.total = 0
            }
            else
                searchProduct(this.content, this.page, 20).then((result) => {
                    this.products = result.data.products
                    this.page = result.data.page
                    this.total = result.data.total
                    this.loading = false
                }, (err) => {
                    MessageBus.emit("box", err)
                })
        },
        updateSearch(content) {
            this.content = content
            this.page = 1
            this.handleSearch()
        },
        showProduct(product) {
            this.$store.commit('setSharedData', { pid: product.id });
            this.$router.push("/product")
        }
    },
    computed: {
    }
}
</script>


<template>
    <ElContainer class="main">
        <!--div class="main-background"></div-->
        <div class="search-input-container">
            <ElRow style="display: flex;justify-content: center;">
                <ElText class="search-text">Search</ElText>
            </ElRow>
            <ElRow style="display: flex;">
                <ElInput v-model="content" style="flex: 1;" placeholder="Enter Product keywords..."
                    @keyup.native.enter="handleSearch">
                    <template #prepend>
                        <ElSelect v-model="searchSelect" placeholder="Select" style="width: 6rem">
                            <ElOption label="商品 / Products" value="1" />
                            <ElOption label="店铺 / Shops" value="2" />
                        </ElSelect>
                    </template>
                </ElInput>
                <ElButton style="min-width: 4rem;" @click="handleSearch">search</ElButton>
            </ElRow>
            <ElSwitch v-model="loading"></ElSwitch>
        </div>
        <div style="padding: 1rem;" class="card-panel-style">
            <ElSpace alignment="start" wrap class="grid-container">
                <ElSkeleton :count="1" :throttle="{ leading: 500, initVal: true }" :loading="loading" animated
                    v-for="product in products">
                    <template #template>
                        <ElCard :body-style="{ padding: '0px', marginBottom: '1px' }">
                            <ElSkeletonItem variant="image" style="width: 100%" />
                            <div style="padding: 14px">
                                <el-skeleton-item variant="h3" style="width: 50%" />
                                <div style="el-skeleton-item-style">
                                    <el-skeleton-item variant="text" />
                                </div>
                            </div>
                        </ElCard>
                    </template>
                    <template #default>
                        <el-card :body-style="{ padding: '0px', marginBottom: '1px' }" shadow="hover"
                            @click="showProduct(product)">
                            <div style="padding: 12px;">
                                <ElRow>
                                    <ElImage fit="contain" :src="product.pictures" style="min-width: 100%;" />
                                </ElRow>
                                <el-row style="max-width: 100%;">
                                    <el-text style="font-size: large;" :line-clamp="1">{{ product.name
                                    }}</el-text>
                                </el-row>
                                <ElRow justify="space-between" class="price-text">
                                    <ElRate v-model="product.rate" disabled text-color="#ff9900" size="large" />
                                    ${{ product.price }}
                                </ElRow>
                            </div>
                        </el-card>
                    </template>

                </ElSkeleton>
            </ElSpace>
            <div v-if="total == 0">
                <ElImage src="https://pic1.imgdb.cn/item/67e2b98d0ba3d5a1d7e351e0.png" fit="scale-down"
                    style="width: 100%; height: 10rem;"></ElImage>
                <ElDivider content-position="left">Something that may interest you...</ElDivider>
                <div v-for="(item, index) in defaultProducts" :key="index">
                    <ElRow class="card-item-style" @click.stop="showProduct(item)">
                        <ElCol :span="12" style="height: 5rem;">
                            <ElText :truncated="true" class="card-text-style">
                                <div style="font-size: 1.5rem;"> {{ item.name }}</div>
                                <br>
                                <div style="font-size: 1rem;"> {{ item.description }}</div>
                            </ElText>
                        </ElCol>
                        <ElCol :span="12">
                            <img :src="item.pictures" class="card-image-style">
                        </ElCol>
                    </ElRow>
                    <!--div style="width: 100%; height: 1px; background-color: gray;" /-->
                </div>
            </div>
            <ElPagination :total="total" v-model="page" />
        </div>

    </ElContainer>
</template>

<style scoped>
.main {
    top: 3rem;
    height: 100%;
    padding: 1rem;
}

.grid-container {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    /* 每行四个列，等分父容器宽度 */
    gap: 10px;
    /* 设置子元素之间的间距 */
}

@media (max-width:1024px) {
    .grid-container {
        grid-template-columns: repeat(3, 1fr);
    }
}

@media (max-width:768px) {
    .grid-container {
        grid-template-columns: repeat(2, 1fr);
    }
}

@media (max-width:512px) {
    .grid-container {
        grid-template-columns: repeat(1, 1fr);
    }
}

.search-input-container {
    width: 100%;
}

.search-text {
    font-size: 2rem;
}

.card-item-style {
    height: 5rem;
    box-shadow: 0px 0px 10px #00000020;
    border-radius: 4px;
    margin: 0.3rem;
    cursor: pointer;
    transition: 0.3s;
}

.card-item-style:hover {
    box-shadow: 0px 0px 10px #00000080;
}

.card-text-style {
    vertical-align: center;
    margin: 1rem;
    line-height: 1rem;
}

.card-image-style {
    top: 0;
    object-fit: cover;
    height: 5rem;
    width: 100%;
    mask-image: linear-gradient(to left, #000, #ffffff20, #ffffff00);
}


.card-panel-style {
    width: 100%;
    background-color: #fff;
    border: none;
    border-radius: 8px;
    box-shadow: 0px 0px 1rem #00000050;
}


.el-skeleton-item-style {
    display: flex;
    align-items: center;
    justify-items: space-between;
    margin-top: 16px;
    height: 16px;
}

.price-text {
    font-size: x-large;
    font-weight: bolder;
    color: #e36a00;
}
</style>