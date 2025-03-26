//NavigationBar
<script setup>
import { Search } from '@element-plus/icons-vue';
import { ElButton, ElCol, ElDrawer, ElIcon, ElInput, ElRow, ElDivider, ElLink, ElText, ElAvatar } from 'element-plus';
</script>

<script>
import PopupMenu from './PopupMenu.vue';
import MessageBus from '@/utils/MessageBus';
import ShopDropdown from './ShopDropdown.vue';

export default {
    data() {
        return {
            userProfileImg: '/img/icons/unknown_user.svg',
            searchContent: '',
            isLogined: false,
            drawerVisible: false,
            titleText: "SPM onlineShopping"
        }
    },
    components: {
        PopupMenu,
        ShopDropdown
    },
    methods: {
        navigateTo(path) {
            this.$router.push(path)
        },
        handleMenuClick(option) {
            //console.log(`点击了${option}`);
            this.$router.push(option)
            this.$refs.popupMenu.closeMenu()
        },
        handleLogout() { //退出账户处理流程，生成提示窗口
            this.$refs.popupMenu.closeMenu()
            var data = {
                code: "Info",
                type: 1,
                message: "提示",
                content: "Do you want to logout? You can login later.",
                callbacks: [
                    () => { },
                    this.handleLogoutConfirm
                ]
            }
            MessageBus.emit("box", data)
            MessageBus.off()
        },
        handleLogoutConfirm() { // 确认退出账户
            localStorage.setItem("token", "")
            localStorage.setItem("user", "")
            setTimeout(() => {
                MessageBus.emit("box", "You're already logout.")
            }, 200)
            MessageBus.emit("auth", { type: "logout" })
        },
        handleSearch() {
            localStorage.setItem('searchContent', this.searchContent)
            console.log(this.$route.name)
            if (this.$route.name == 'search') {
                MessageBus.emit("search", this.searchContent)
            }
            else
                this.$router.push("/search")
        },
        popup() { // 弹出菜单
            this.$refs.popupMenu.toggleMenu()
        }
    },
    mounted() {
        if (window.localStorage.getItem("token") != "")
            this.isLogined = true
        MessageBus.on("auth", (event) => {
            if (event.type == "login") {
                this.isLogined = true
            }
            else {
                this.isLogined = false
            }
        })
        MessageBus.on("routerChange", (newtext) => {
            this.titleText = newtext
        })
    },
    unmounted() {
        MessageBus.off()
    }

}
</script>

<template>
    <Transition name="pages">
        <div class="header">
            <div class="mid-container">
                <img alt="logo" class="aligner" @click="drawerVisible = true" src="/img/icons/icon.svg" />
                <ElText id="title" class="aligner" @click="drawerVisible = true" style="font-size: x-large;">{{
                    titleText }}</ElText>
                <ElDrawer v-model="drawerVisible" :show-close=false direction="ttb" :with-header=false :z-index=1
                    size="30rem">
                    <img class="drawer-bg" src="/img/drawerbg.svg">
                    <img class="drawer-text" src="/img/icons/spmos.svg">
                    <ElRow class="row-bg" justify="space-evenly" align="top" @click="drawerVisible = false">
                        <ElCol v-loading=false :sm="7" :xs="24">
                            <el-divider content-position="left">Explore what you want</el-divider>
                            <ElRow>
                                <ElLink :underline="false" class="el-row-expand" @click="navigateTo('/')">商店主页</ElLink>
                            </ElRow>
                            <ElRow>
                                <ElLink :underline="false" class="el-row-expand" @click="navigateTo('/')">热卖商品</ElLink>
                            </ElRow>
                            <ElRow>
                                <ElLink :underline="false" class="el-row-expand" @click="navigateTo('/search')">分类查找
                                </ElLink>
                            </ElRow>
                        </ElCol>
                        <!--ElCol :sm="2" :xs="0"><el-divider direction="vertical" class="hidden-xs-only"/></ElCol-->
                        <ElCol v-loading=false :sm="7" :xs="24">
                            <el-divider content-position="left">Find what you got</el-divider>
                            <ElRow>
                                <ElLink :underline="false" class="el-row-expand" @click="navigateTo('/user')">用户中心
                                </ElLink>
                            </ElRow>
                            <ElRow>
                                <ElLink :underline="false" class="el-row-expand" @click="navigateTo('/cart')">我的购物车
                                </ElLink>
                            </ElRow>
                            <ElRow>
                                <ElLink :underline="false" class="el-row-expand" @click="navigateTo('/order')">我的订单
                                </ElLink>
                            </ElRow>
                        </ElCol>
                        <!--ElCol :sm="2" :xs="0"><el-divider direction="vertical" class="hidden-xs-only"/></ElCol-->
                        <ElCol v-loading=true :sm="7" :xs="24">
                            <el-divider content-position="left">Manage what you built</el-divider>
                            <ElRow>
                                <ElLink :underline="false" class="el-row-expand" @click="navigateTo('/seller/product')">
                                    我的商品</ElLink>
                            </ElRow>
                            <ElRow>
                                <ElLink :underline="false" class="el-row-expand" @click="navigateTo('/seller')">我的店铺
                                </ElLink>
                            </ElRow>
                        </ElCol>
                    </ElRow>
                </ElDrawer>
                <nav class="left-logo-aligner">
                    <ShopDropdown />
                </nav>
                <span class="placeholder" />
                <ElButton :icon="Search" circle class="hidden-sm-and-up" @click.stop="handleSearch()" style="margin: auto 0.5rem;"/>
                <ElInput class="aligner hidden-xs-only" id="search" v-model="searchContent" placeholder="Search...">
                    <template #append>
                        <ElButton icon="Search" @click.stop="handleSearch()" />
                    </template>
                </ElInput>
                       
                    <popup-menu ref="popupMenu" v-if="isLogined">
                    <template #trigger>
                        <ElAvatar alt="user-avatar" :src="userProfileImg" style="height: 2rem; width: 2rem;"
                            fit="contain" ref="userImg" />
                    </template>
                    <div>
                        <a class="popup-menu-item" @click="handleMenuClick('/user')">My Account</a>
                        <a class="popup-menu-item" @click="handleMenuClick('/notifiction')">Messages</a>
                        <a class="popup-menu-item" @click="handleMenuClick('/order')">My Orders</a>
                        <a class="popup-menu-item" @click="handleLogout">Logout</a>
                    </div>
                </popup-menu>
                <ElButton @click.stop="navigateTo('/login')" v-if="!isLogined">Sign in</ElButton>

            </div>
        </div>
    </Transition>
</template>

<style scoped>
.header {
    transition: 1.0s;
    top: 0px;
    left: 0%;
    position: fixed;
    width: 100%;
    height: 3rem;
    border: 0;
    box-shadow: 0px 5px 5px #888888;
    font: 1.5em sans-serif;
    background-color: rgb(249, 249, 249);
    z-index: 100;
}

.drawer-bg {
    position: absolute;
    display: flex;
    width: 100%;
    height: 100%;
    object-fit: fill;
    transform: translate(-1em, -1em);
}

.drawer-text {
    position: absolute;
    display: flex;
    width: 100%;
    height: 100%;
    object-fit: contain;
    transform: translate(-1em, -1em);
}

/* 标题栏居中容器 */
.mid-container {
    margin: auto;
    position: relative;
    width: 100%;
    padding: 0% 1rem;
    margin-top: 0.5rem;
    display: flex;
    justify-content: center;
    /*作为垂直居中的父元素*/
    line-height: 2rem;
}

.el-row-expand {
    width: 100%;
    padding: 0.4rem;
    font-size: large;
}

/* 两边容器约束 */
.aligner {
    display: flex;
    text-align: center;
    height: 2rem;
    margin: 0 0.5rem;
    cursor: pointer;
    white-space: nowrap;
    max-width: 50%;
}

.placeholder {
    flex: 1;
    margin: auto;
    min-width: 0;
    background-color: #000000;
}

/* 按钮 */
button {
    transition: 0.2s;
    font-size: medium;
}

button:hover {
    background-color: #cfcfcf;
}


/* 搜索框 deleted*/

/* 搜索悬浮图标 */
.search-text {
    margin-left: auto;
    position: relative;
    flex: 0 1 auto;
    height: 2rem;
    object-fit: contain;
    /*border: 1px solid #8a8a8a;*/
}


/* 弹出菜单项目 */
.popup-menu-item {
    padding: 0.2rem;
    margin: 0;
    display: inline-block;
    white-space: nowrap;
}

/* 动画 */
.pages-enter-active,
.pages-leave-active {
    transition: 0.5s;
    transition-delay: 1.0s;
}

.pages-enter-to {
    opacity: 1;
}

.pages-leave-to {
    opacity: 0;
}

img:hover {
    border-radius: 8px;
}
</style>