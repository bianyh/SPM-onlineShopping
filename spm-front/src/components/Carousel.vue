<template>
    <div class="carousel">
      <swiper :modules="modules" :pagination="{ clickable: true }">
        <!-- <swiper-slide v-for="(image, index) in images" :key="index">
          <img :src="image" alt="">
        </swiper-slide> -->
      <swiper-slide>
        <img src="../assets/swiperpc/1.png" alt="">
      </swiper-slide>
      <swiper-slide>
        <img src="../assets/swiperpc/2.png" alt="">
      </swiper-slide>
      <swiper-slide>
        <img src="../assets/swiperpc/3.png" alt="">
      </swiper-slide>
      <swiper-slide>
        <img src="../assets/swiperpc/n.png" alt="">
      </swiper-slide>
      </swiper>
    </div>
</template>

<script >
import { ref,defineProps } from 'vue';
import { Pagination } from 'swiper/modules';
import { Swiper, SwiperSlide } from 'swiper/vue';
import 'swiper/css';
import 'swiper/css/pagination';

// const props=defineProps({
//     images:{
//         type:Array,
//         default:()=>[]
//     }
// });
// const modules=ref([Pagination]);
export default {
  name: 'HelloWorld',
  data(){
    return{
      modules: [ Pagination ]
    }
  },
  components:{
    Swiper,
    SwiperSlide
  }
}
</script>

<style scoped>
.carousel {
    width: 1400px;
    height: 420px;
    margin:0 auto;
}
.swiper-slide {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
}
.carousel img {
    height:420px;
    /* object-fit: cover; */
}
</style>