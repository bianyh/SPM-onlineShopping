import { createRouter, createWebHashHistory } from 'vue-router'
import login from '../views/login.vue'
import signup from '../views/signup.vue'
//import home from '../views/home.vue'
import homenew from '../views/homenew.vue'
import Category1 from '@/views/Category/Category1.vue'
import Category2 from '@/views/Category/Category2.vue'
import Category3 from '@/views/Category/Category3.vue'
import Myorders from '@/views/OrderView.vue'
import UserMessage from '@/components/user/UserMessage.vue'
import ProductSearchView from '@/views/ProductSearchView.vue'
import SellerHome from '@/views/SellerHome.vue'  
import Editgood from '@/views/Editgood.vue'
import Releasegood from '@/views/Releasegood.vue'
import Goodsmanage from '@/views/Goodsmanage.vue'
import Application from '@/views/Application.vue'
import UserView from '@/views/UserView.vue'
import ProductView from '@/views/ProductView.vue'
import MessageBus from '@/utils/MessageBus'
import CartView from '@/views/CartView.vue'
import CheckoutView from '@/views/CheckoutView.vue'
import AddressManage from '@/components/user/AddressManage.vue'
import PaymentView from '@/views/PaymentView.vue'
import StoreView from '@/views/StoreView.vue'
import StoreCollectionView from '@/views/StoreCollectionView.vue'
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
    component:CartView,
    meta: {
      title: 'Shopping Cart'
    }
  },
  {
    path:'/checkout',
    name:'checkout',
    component: CheckoutView,
    meta: {
      title: 'Checkout'
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
    path:'/payment',
    name:'payment',
    component:PaymentView,
    meta: {
      title: 'Payment'
    }
  },
  {
    path:'/user',
    name:'UserView',
    component:UserView,
    meta: {
      title: 'User Center'
    }
  },
  {
    path:'/address',
    name:'address',
    component:AddressManage,
    meta: {
      title: 'Address'
    }
  },
  {
    path:'/UserMessage',
    name:'UserMessage',
    component:UserMessage
  },
  {
    path: '/seller',
    name: 'seller',
    component: SellerHome,
    meta: {
      title: 'Seller Center'
    }
  },
  {
    path: '/application',
    name: 'application',
    component: Application
  },
  {
    path: '/store',
    name: 'store',
    component: StoreView,
    meta: {
      title: 'Seller Center'
    }
  },
  {
    path: '/mystores',
    name: 'mystores',
    component: StoreCollectionView,
    meta: {
      title: 'My Stores'
    }
  },
  {
    path: "/product/create",
    name: "Releasegood",
    component: Releasegood,
    meta: {
      title: 'New Product'
    }
  },
  {
    path: "/product/edit", 
    name: "Editgood",
    component: Editgood,
    meta: {
      title: 'Product Edit'
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
