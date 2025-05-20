<template>
  <div class="carousel">
    <swiper :modules="modules" :pagination="{ clickable: true }" :autoplay="{ delay: 2000, disableOnInteraction: false }">
      <swiper-slide>
        <img src="../assets/swiperpc/1.jpg" alt="">
      </swiper-slide>
      <swiper-slide>
        <img src="../assets/swiperpc/2.jpg" alt="">
      </swiper-slide>
      <swiper-slide>
        <img src="../assets/swiperpc/3.jpg" alt="">
      </swiper-slide>
      <swiper-slide>
        <img src="../assets/swiperpc/4.jpg" alt="">
      </swiper-slide>
    </swiper>
  </div>
</template>

<script>
import { Pagination, Autoplay } from 'swiper/modules';
import { Swiper, SwiperSlide } from 'swiper/vue';
import 'swiper/css';
import 'swiper/css/pagination';
import 'swiper/css/autoplay';

export default {
  name: 'HelloWorld',
  data() {
    return {
      modules: [Pagination, Autoplay]
    }
  },
  components: {
    Swiper,
    SwiperSlide
  }
}
</script>

<style scoped>
.carousel {
  height: 420px;
  margin: 0 auto;
  max-width: 100%;
}
.swiper-slide {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}
.carousel img {
  height: 420px;
  /* object-fit: cover; */
}
</style>  