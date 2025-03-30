<script setup>
import UserMessage from '@/components/user/UserMessage.vue';
import { Document, User } from '@element-plus/icons-vue';
import { ElAside, ElMain, ElMenu, ElMenuItem, ElMenuItemGroup, ElSubMenu, ElIcon, ElContainer, ElCol, ElRow, ElAffix, } from 'element-plus';
</script>

<script>
export default {
    data() {
        return {
            isCollapse: false,
            currentPage: '1',
        }
    },
    components: {
        UserMessage,
    },
    methods: {
        handleSelect(index) {
            this.currentPage = index
        }
    }
}
</script>

<template>
    <ElRow style="top: 3.5rem;">
        <h3 class="hidden-xs-only">用户信息管理</h3>
    </ElRow>
    <ElRow class="row-bg" justify="space-evenly" align="top" style="top: 1rem;">

        <ElCol :span="6" :xs="0">
            <h5 class="mb-2">Default colors</h5>
            <el-menu default-active="1" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose"
                :collapse="isCollapse" @select="handleSelect">
                <el-menu-item index="1">
                    <el-icon>
                        <User />
                    </el-icon>
                    <span class="">（profile）</span>
                </el-menu-item>
                <el-menu-item index="2" @click="this.$router.push('/order')">
                    <el-icon>
                        <Document />
                    </el-icon>
                    <span>（My orders）</span>
                </el-menu-item>
                <el-menu-item index="3" @click="this.$router.push('/address')">
                    <el-icon>
                        <location />
                    </el-icon>
                    <span>（My Address）</span>
                </el-menu-item>
                <el-menu-item index="4" @click="this.$router.push('/cart')">
                    <el-icon>
                        <ShoppingTrolley />
                    </el-icon>
                    <span>(My Cart)</span>
                </el-menu-item>
            </el-menu>

        </ElCol>
        <ElCol :span="18" :xs="24">
            <UserMessage v-if="currentPage == '1'" />
        </ElCol>

    </ElRow>
    <!--ElAffix position="bottom" :offset="4">

        <el-dropdown placement="top">
            <el-button> top </el-button>
            <template #dropdown>
                <el-dropdown-menu>
                    <el-dropdown-item>The Action 1st</el-dropdown-item>
                    <el-dropdown-item>The Action 2st</el-dropdown-item>
                    <el-dropdown-item>The Action 3st</el-dropdown-item>
                </el-dropdown-menu>
            </template>
</el-dropdown>
</ElAffix-->
</template>

<style scoped></style>