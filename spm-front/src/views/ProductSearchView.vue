//ProductSearchView.vue
<script setup>
import { ElCard, ElCol, ElConfigProvider, ElContainer, ElInput, ElButton, ElPagination, ElRow, ElSkeleton, ElSkeletonItem, ElSpace, ElTable, ElImage, ElSwitch, ElMessage, ElText, ElRate } from 'element-plus';
</script>

<script>
import { searchProduct } from '@/api/product';
import MessageBus from '@/utils/MessageBus';
export default {
    data() {
        return {
            content: localStorage.getItem('searchContent'),//ⓘⓘ
            products: [],
            page: 1,
            limit: 20,
            loading: true
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
            searchProduct(this.content, this.page, 20).then((result) => {
                this.products = result.data.products
                this.page = result.data.page
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
}
</script>


<template>
    <ElContainer class="main">
        <!--div class="main-background"></div-->
        <div class="search-input-container">
            Search
            <ElInput v-model="content">
            </ElInput>
            <ElSwitch v-model="loading"></ElSwitch>
        </div>
        <div style="padding: 1rem;" class="card-style">
            <ElSpace :size="large" alignment="start" wrap>
                <ElSkeleton :count="1" :throttle="{ leading: 500, initVal: true }" style="width: 25%" :loading="loading"
                    animated v-for="product in products">
                    <template #template>
                        <ElCard :body-style="{ padding: '0px', marginBottom: '1px' }">
                            <ElSkeletonItem variant="image" style="width: 240px; height: 240px" />
                            <div style="padding: 14px">
                                <el-skeleton-item variant="h3" style="width: 50%" />
                                <div style="el-skeleton-item-style">
                                    <el-skeleton-item variant="text" />
                                </div>
                            </div>
                        </ElCard>
                    </template>
                    <template #default>
                        <el-card :body-style="{ padding: '0px', marginBottom: '1px'}" shadow="hover"
                            @click="showProduct(product)" class="card-style"> 
                            <div></div>
                            <ElImage fit="cover" :src="product.pictures" class="image"
                                style="width: 240px; height: 240px" />
                            <div style="padding: 12px">
                                <el-row style="width: 210px;">
                                    <el-text truncated style="font-size: large;">{{ product.name }}</el-text>
                                </el-row>
                                <ElRow justify="space-between" class="price-text">
                                    <ElRate v-model="product.rate" disabled text-color="#ff9900" size="large"/>
                                    ${{ product.price }}
                                </ElRow>
                            </div>
                        </el-card>
                    </template>

                </ElSkeleton>
            </ElSpace>
        </div>

        <ElConfigProvider :locale="locale">
            <ElTable mb-1 :data="[]" />
            <ElPagination :total="100" />
        </ElConfigProvider>
    </ElContainer>
</template>

<style scoped>
.main {
    top: 3rem;
    height: 100%;
    padding: 2rem;
}

.main-background {
  width: 100%;
  height: 100%;
  background-image: url(../assets/loginbg.jpg);
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: fixed;
  display: flex;
  top: 0;
  left: 0;
  opacity: 0.1;
}

.search-input-container {
    width: 100%;
}

.image {
    width: 100%;
}

.card-style {
    background-color: #fff;
    
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