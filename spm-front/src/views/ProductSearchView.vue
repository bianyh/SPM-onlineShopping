//ProductSearchView.vue

<script>
import { searchProduct } from '@/api/product';
import ProductCard from '@/components/ProductCard.vue';
import MessageBus from '@/utils/MessageBus';
import { ElAside, ElContainer, ElInput, ElMain } from 'element-plus';
export default {
    data() {
        return {
            content: localStorage.getItem('searchContent'),//ⓘⓘ
            products: [],
            page: 0,
            limit: 20,
        }
    },
    mounted() {
        this.handleSearch()
    },
    methods: {
        handleSearch(){
            searchProduct(this.content, 0, 20).then((result) => {
                this.products = result.data.products
                this.page = result.data.page
            }, (err) => {
                MessageBus.emit("box",err)
            })
        }
    },
}
</script>


<template>
    <ElContainer>
        <ElAside>
            <ElContainer>

            </ElContainer>
        </ElAside>
        <ElMain>
            <ElContainer>
                <ElContainer class="search-input-container">
                    <ElInput v-model="content">

                    </ElInput>
                </ElContainer>
                <ElContainer class="search-result-container" v-for="product in products">
                    <ProductCard>{{ product }}</ProductCard>
                </ElContainer>
            </ElContainer>
        </ElMain>
    </ElContainer>
</template>

<style scoped>

</style>