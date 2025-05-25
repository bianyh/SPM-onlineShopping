<template>
    <ElRow style="width: 100%; padding: 4rem 1rem; " justify="center">
        <!-- shopping online标语 -->
        <ElText class="title-text">
            SPM onlineShopping
        </ElText>
        <ElText>Simple, Perfect, Multifarious</ElText>
        <!-- search输入框 -->
        <div class='searchF' >
            <div class="searchbox">
                <input type="text" v-model="searchQuery" @input="filterKeywords"
                    placeholder="Search for your favorite products！" style="width: 90%;padding-left: 1rem;" />
                <ElButton class="search-btn" style="width: 9%; margin-left: 1%; height: 2.5rem ;" @click="handleSearch"><template #default>
                        <ElIcon>
                            <Search />
                        </ElIcon>
                    </template>
                </ElButton>
            </div>
        </div>
        <!-- keywords推荐词 -->
        <div class="keyword-area">
            <span class="keyword-label">Keywords:</span>
            <span v-for="(keyword, index) in allKeywords" :key="index" class="keyword-item">{{ keyword }}
                <span v-if="index < allKeywords.length - 1"> </span>
            </span>
        </div>
        <!-- 轮播图区域 :images="swiperImages" -->
        <Carousel />
        <!-- 商品展示区域 -->
        <ProductList type="hot-products"/>

    </ElRow>

</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import Carousel from '@/components/Carousel.vue';
import ProductList from '../components/ProductList.vue';
import { ElButton, ElCol, ElContainer, ElHeader, ElIcon, ElInput, ElMain, ElRow, ElText } from 'element-plus';
import { Search } from '@element-plus/icons-vue';

const router = useRouter();

const searchQuery = ref('');
const allKeywords = ref(['Phone', 'Computer', 'Tablet', 'Camera', 'Earphone', '...']);
const filteredKeywords = ref([]);

const filterKeywords = () => {
    if (searchQuery.value === '') {
        filteredKeywords.value = [];
    } else {
        filteredKeywords.value = allKeywords.value.filter(keyword =>
            keyword.includes(searchQuery.value)
        );
    }
};

const selectKeyword = (keyword) => {
    searchQuery.value = keyword;
    filteredKeywords.value = [];
    handleSearch();
};

const handleSearch = () => {
    if (searchQuery.value) {
        localStorage.setItem('searchContent', searchQuery.value);
        router.push({ name: 'search', query: { keyword: searchQuery.value } });
    }
};

</script>



<style scoped>
.title-text {
    top: 3.5rem;
    width: 100%;
    font-size: 32px;
    font-weight: 700;
    letter-spacing: 0px;
    line-height: 59.52px;
    text-align: center;
    background: linear-gradient(to right, #965869, #458089);
    background-clip: text;
    -webkit-text-fill-color: transparent;
}


.keyword-area {
    text-align: center;
    color: #6699cc;
    width: 100%;
    font-size: 18px;
    margin-top: 10px;
}

.keyword-label {
    color: #ff6699;
}

.keyword-item {
    margin-right: 5px;
}

.searchF{
   /*style="width: 100%; background-color: #fff; display: flex;" */
   width:100%;
   background-color: #fde9ef;
   display: flex;
   border-radius: 10px;
}
.searchbox {
    /*保留定位，以便关键词列表定位 */
    width: 700px;
    margin: 1rem auto;
    justify-self: center;
}

.searchbox input {
    height: 2.5rem;
    display: inline;
    border: 1px solid #ccc;
    border-radius: 10px;
    font-size: 16px;
    opacity: 0.8;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
    /* border: 1px solid #ddd;
    border-radius: 20px;
    padding: 0.5rem 1rem;
     */
}
.searchbox .el-button {
    background-image: url('https://img.js.design/assets/img/67cfdb8607c4c2e467899865.png');
    border: none;
    color: white;
    font-size: 1.2rem;
    border-radius: 20px;
    margin-left: 0.5rem;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}
.el-button:hover{
    transform: scale(1.03);
}
@media (max-width:767px) {
    .searchbox {
        width: 100%;
    }
}
</style>