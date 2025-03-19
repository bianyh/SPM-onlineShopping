<template>
    <div class="popup-menu-container">
        <!-- 触发弹出的元素 -->
        <div @click="toggleMenu">
            <slot name="trigger"></slot>
        </div>

        <!-- 弹出菜单内容 -->
        <div class="popup-menu" :class="{ 'popup-menu-show': isMenuVisible }" @click.stop>
            <slot>默认菜单内容</slot>
        </div>
    </div>
</template>

<script>

export default {
    data() {
        return {
            isMenuVisible: false, // 控制菜单的显示和隐藏
        };
    },
    mounted() {
        // 监听文档点击事件，点击其他区域时关闭菜单
        document.addEventListener("click", this.closeMenu);
    },
    beforeDestroy() {
        // 组件销毁时移除事件监听
        document.removeEventListener("click", this.closeMenu);
    },
    methods: {
        toggleMenu() {
            this.isMenuVisible = !this.isMenuVisible;
        },
        closeMenu() {
            this.isMenuVisible = false;
            document.removeEventListener("click", this.closeMenu);
        }
    }
};
</script>

<style scoped>
.popup-menu-container {
    position: relative;
    display: inline-block;
}

.popup-trigger {
    cursor: pointer;
    padding: 8px 16px;
    background-color: #f0f0f0;
    border-radius: 4px;
}

.popup-menu {
    position: absolute;
    display: block;
    width: fit-content;
    top: 3rem;
    right: 0;
    background-color: white;
    border-radius: 4px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
    transform: scaleY(0);
    transform-origin: top center;
    transition: transform 0.2s ease-out;
    z-index: 1000;
    padding: 0.1rem;
}

.popup-menu-show {
    transform: scaleY(1);
}

.popup-menu ::v-deep(*) {
    padding: 0.5rem 16px;
    cursor: pointer;
}

</style>