// import { createApp } from 'vue'
// import App from './App.vue'
// import './registerServiceWorker'
// import router from './router'
// import store from './store'
// import ElementPlus from 'element-plus'
// import * as icons from '@element-plus/icons-vue'

// // createApp(App).use(store).use(router).use(ElementPlus).mount('#app').component(key, icons[key])
// const app=createApp(App)
// Object.keys(icons).forEach(key => {
//     app.component(key, icons[key])
// })
// app.mount('#app').use(store).use(router).use(ElementPlus)

import './assets/main.css' // 导入全局css

import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'


const app = createApp(App)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(store)
   .use(router)
   .use(ElementPlus)
   .mount('#app')