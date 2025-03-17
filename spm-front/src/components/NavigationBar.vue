//NavigationBar

<script>
import { RouterLink } from 'vue-router'
import PopupMenu from './PopupMenu.vue';
import MessageBus from '@/utils/MessageBus';
import CategoryDropdown from './CategoryDropdown.vue';

export default {
    data() {
        return {
            userProfileImg: '/img/icons/unknown_user.svg',
            searchContent: ''
        }
    },
    components: {
        PopupMenu,
        CategoryDropdown
    },
    methods: {
        goToLogin() {
            this.$router.push('/login')
        },
        goToHome() {
            this.$router.push('/')
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
                    () => {},
                    this.handleLogoutConfirm
                ]
            }
            MessageBus.emit("box", data)
        },
        handleLogoutConfirm() { // 确认退出账户
            localStorage.setItem("token", "")
            localStorage.setItem("user", "")
            setTimeout(() => {
                MessageBus.emit("box", "You're already logout.")
            },200)
        },
        popup() { // 弹出菜单
            this.$refs.popupMenu.toggleMenu()
        }
    }
}
</script>

<template>
    <Transition name="pages">
        <div class="header">
            <div class="mid-container">
                <img alt="logo" class="left-logo-aligner" @click="goToHome" src="/img/icons/icon.svg" />
                <div id="title" class="left-logo-aligner" @click="goToHome">SPMoS</div>
                <nav class="left-logo-aligner">
                    <CategoryDropdown />
                    <RouterLink to="/Shop">Shops</RouterLink>
                </nav>
                <span class="placeholder" />
                <div class="right-state-aligner">
                    <button @click="goToLogin" class="right-state-aligner">Sign in</button>
                    <popup-menu ref="popupMenu">
                        <template #trigger>
                            <img alt="user-avatar" :src="userProfileImg" class="right-state-aligner" ref="userImg" />
                        </template>
                        <div>
                            <a class="popup-menu-item" @click="handleMenuClick('/Myaccount')">My Account</a>
                            <a class="popup-menu-item" @click="handleMenuClick('/UserMseeage')">Messages</a>
                            <a class="popup-menu-item" @click="handleMenuClick('/Myorders')">My Orders</a>
                            <a class="popup-menu-item" @click="handleLogout">Logout</a>
                        </div>
                    </popup-menu>
                </div>
                <div class="right-state-aligner">
                    <input id="search" v-model="searchContent" placeholder="Search..." class="search-input">
                    <img src="/img/icons/search.svg" class="search-text" v-show="searchContent">
                </div>

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
    height: 3cap;
    border: 0;
    box-shadow: 0px 5px 5px #888888;
    font: 1.5em sans-serif;
    background-color: rgb(249, 249, 249);
    z-index: 100;
}

/* 标题栏居中容器 */
.mid-container {
    margin: auto;
    position: relative;
    width: 100%;
    padding: 0% 1rem;
    margin-top: 0.5rem;
    display: inline-block;
    /*作为垂直居中的父元素*/
    line-height: 2rem;
}

/* 左侧标题容器 */
.left-logo-aligner {
    float: left;
}

/* 右侧标题容器 */
.right-state-aligner {
    float: right;
}

/* 两边容器共有属性 */
.left-logo-aligner,
.right-state-aligner {
    vertical-align: middle;
    display: inline-block;
    text-align: center;
    height: 2rem;
    margin-left: 1rem;
    cursor: pointer;
}

.placeholder {
    margin: auto;
}

/* 按钮 */
button {
    border: 1px solid #888;
    border-radius: 4px;
    box-shadow: 0px 1px 1px #888888;
    font: 1rem sans-serif;
    transition: 0.2s;
}

button:hover {
    background-color: #cfcfcf;
}

/* 搜索框 */
.search-input {
    float: right;
    display: block;
    width: 100%;
    padding: 0.4rem;
    background-color: #ebebeb;
    border-width: 1px 1px 1px 1px;
    border-radius: 0.2rem;
    font-size: medium;
}

/* 搜索悬浮图标 */
.search-text {
    display: flexbox;
    position: relative;
    height: 100%;
    right: -50%;
    transform: translateX(-60%);
    object-fit: contain;
    top: -100%;
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