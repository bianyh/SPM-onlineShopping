import { createRouter, createWebHashHistory } from 'vue-router'
import login from '../views/login.vue'
import signup from '../views/signup.vue'
//import home from '../views/home.vue'
import homenew from '../views/homenew.vue'
import Category1 from '@/views/Category/Category1.vue'
import Category2 from '@/views/Category/Category2.vue'
import Category3 from '@/views/Category/Category3.vue'
import Carts from '@/views/Carts.vue'
import Myorders from '@/views/Myorders.vue'
import UserMessage from '@/components/user/UserMessage.vue'
import ProductSearchView from '@/views/ProductSearchView.vue'
import UserView from '@/views/UserView.vue'
import ProductView from '@/views/ProductView.vue'
import MessageBus from '@/utils/MessageBus'
const routes = [
  {
    path: '/',
    redirect: '/homenew',
  },
  {
    path: '/login',
    name: 'login',
    component: login,
    meta: {
      title: 'Log in'
    }
  },
  {
    path: '/signup',
    name: 'signup',
    component: signup,
    meta: {
      title: 'Sign up'
    }
  },
  {
    path:'/homenew',
    name:'home',
    component:homenew,
    meta: {
      title: 'Home Page'
    }
  },
  {
    path:'/search',
    name: 'search',
    component:ProductSearchView,
    meta: {
      title: 'Searching Goods'
    }
  },
  {
    path:'/product',
    name: 'product',
    component: ProductView,
    meta: {
      title: 'Product'
    }
  },

  /*Catorgies的分类*/ 
  {
    path: '/category1',
    name: 'Category1',
    component: Category1
  },
  {
    path: '/category2',
    name: 'Category2',
    component: Category2
  },
  {
    path: '/category3',
    name: 'Category3',
    component: Category3
  }
  ,{
    path:'/cart',
    name:'cart',
    component:Carts,
    meta: {
      title: 'Shopping Cart'
    }
  },
  {
    path:'/order',
    name:'order',
    component:Myorders,
    meta: {
      title: 'Orders'
    }
  },
  {
    path:'/user',
    name:'UserView',
    component:UserView,
    meta: {
      title: 'User Center'
    }
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title || 'SPMoS';
  MessageBus.emit("routerChange", to.meta.title || 'SPMoS')
  next();
});

export default router
