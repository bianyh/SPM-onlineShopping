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
import UserMessage from '@/views/UserMessage.vue'
import ProductSearchView from '@/views/ProductSearchView.vue'
const routes = [
  {
    path: '/',
    redirect: '/homenew'
  },
  {
    path: '/login',
    name: 'login',
    component: login
  },
  {
    path: '/signup',
    name: 'signup',
    component: signup
  },
  {
    path:'/homenew',
    name:'homenew',
    component:homenew
  },
  {
    path:'/search',
    component:ProductSearchView,
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
    path:'/Carts',
    name:'Carts',
    component:Carts
  },
  {
    path:'/Myorders',
    name:'Myorders',
    component:Myorders
  },
  {
    path:'/UserMseeage',
    name:'UserMessage',
    component:UserMessage
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
