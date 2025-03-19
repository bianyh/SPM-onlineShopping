//ProductSearchView.vue

<script>
import { searchProduct } from '@/api/product';
import ProductCard from '@/components/ProductCard.vue';
import MessageBus from '@/utils/MessageBus';
<<<<<<< HEAD
import { ElAside, ElContainer, ElInput, ElMain } from 'element-plus';
=======
import { ElAside, ElCol, ElContainer, ElInput, ElMain, ElRow } from 'element-plus';
>>>>>>> main
export default {
    data() {
        return {
            content: localStorage.getItem('searchContent'),//ⓘⓘ
            products: [],
<<<<<<< HEAD
            page: 0,
=======
            page: 1,
>>>>>>> main
            limit: 20,
        }
    },
    mounted() {
<<<<<<< HEAD
=======
        MessageBus.on("search", (content) => {
            this.updateSearch(content)
        })
>>>>>>> main
        this.handleSearch()
    },
    methods: {
        handleSearch(){
<<<<<<< HEAD
            searchProduct(this.content, 0, 20).then((result) => {
=======
            searchProduct(this.content, this.page, 20).then((result) => {
>>>>>>> main
                this.products = result.data.products
                this.page = result.data.page
            }, (err) => {
                MessageBus.emit("box",err)
            })
<<<<<<< HEAD
=======
        },
        updateSearch(content){
            this.content = content
            this.page = 1
            this.handleSearch()
>>>>>>> main
        }
    },
}
</script>


<template>
<<<<<<< HEAD
    <ElContainer>
        <ElAside>
            <ElContainer>

=======
    <ElContainer class="main">
        <ElAside>
            <ElContainer>
                侧边栏
>>>>>>> main
            </ElContainer>
        </ElAside>
        <ElMain>
            <ElContainer>
<<<<<<< HEAD
                <ElContainer class="search-input-container">
                    <ElInput v-model="content">

                    </ElInput>
                </ElContainer>
                <ElContainer class="search-result-container" v-for="product in products">
                    <ProductCard>{{ product }}</ProductCard>
                </ElContainer>
=======
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
>>>>>>> main
            </ElContainer>
        </ElMain>
    </ElContainer>
</template>

<style scoped>
<<<<<<< HEAD

=======
.main {
    top: 4rem;
    height: 100%;
}

.search-input-container {
    width: 100%;
}
>>>>>>> main
</style>