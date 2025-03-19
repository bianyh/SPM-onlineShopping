//ProductSearchView.vue

<script>
import { searchProduct } from '@/api/product';
import ProductCard from '@/components/ProductCard.vue';
import MessageBus from '@/utils/MessageBus';
import { ElAside, ElCol, ElContainer, ElInput, ElMain, ElRow } from 'element-plus';
export default {
    data() {
        return {
            content: localStorage.getItem('searchContent'),//ⓘⓘ
            products: [],
            page: 1,
            limit: 20,
        }
    },
    mounted() {
        MessageBus.on("search", (content) => {
            this.updateSearch(content)
        })
        this.handleSearch()
    },
    methods: {
        handleSearch(){
            searchProduct(this.content, this.page, 20).then((result) => {
                this.products = result.data.products
                this.page = result.data.page
            }, (err) => {
                MessageBus.emit("box",err)
            })
        },
        updateSearch(content){
            this.content = content
            this.page = 1
            this.handleSearch()
        }
    },
}
</script>


<template>
    <ElContainer class="main">
        <ElAside>
            <ElContainer>
                侧边栏
            </ElContainer>
        </ElAside>
        <ElMain>
            <ElContainer>
                <ElRow>
                <ElCol class="search-input-container">
                    Search
                    <ElInput v-model="content">
                    </ElInput>
                </ElCol>
                <ElCol class="search-result-container" v-for="product in products">
                    <ProductCard>{{ product }}</ProductCard>
                </ElCol>
            </ElRow>
            </ElContainer>
        </ElMain>
    </ElContainer>
</template>

<style scoped>
.main {
    top: 4rem;
    height: 100%;
}

.search-input-container {
    width: 100%;
}
</style>