<script setup>
import UserMessage from '@/components/user/UserMessage.vue';
import { Document, User } from '@element-plus/icons-vue';
import { ElAside, ElMain, ElMenu, ElMenuItem, ElMenuItemGroup, ElSubMenu, ElIcon, ElContainer, ElCol, ElRow, ElAffix, } from 'element-plus';
import OrderView from './OrderView.vue';
import AddressManage from '@/components/user/AddressManage.vue';
import CartView from './CartView.vue';
import MessageBus from '@/utils/MessageBus';
</script>

<script>
export default {
    data() {
        return {
            isCollapse: false,
            currentPage: '1-1',
        }
    },
    components: {
        UserMessage,
    },
    methods: {
        handleSelect(index) {
            this.currentPage = index
        }
    },
    mounted() {
        const params = window.localStorage.getItem("navigationParams")
        if (typeof(params) == "string"){
            this.currentPage = params
        }
        MessageBus.on("menuChange",(params) => {
            this.currentPage = params
        })
    },
    beforeDestroy() {
        MessageBus.off()
    },
}
</script>

<template>
    <ElRow class="row-bg" justify="space-evenly" align="top" style="top: 3.5rem;">
        <ElCol :span="6" :xs="0">
        <h1>Menu</h1>
            <el-menu default-active="1-1" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose"
                :collapse="isCollapse" @select="handleSelect">
                <ElSubMenu index="1">
                    <template #title>
                        <el-icon>
                            <User />
                        </el-icon>
                        <span class="">My Profile</span>
                    </template>
                    <ElMenuItemGroup title="basic">
                        <el-menu-item index="1-1">
                            <el-icon>
                                <User />
                            </el-icon>
                            <span class="">basic profile</span>
                        </el-menu-item>
                        <el-menu-item index="1-2">
                            <el-icon>
                                <location />
                            </el-icon>
                            <span>My Address</span>
                        </el-menu-item>
                    </ElMenuItemGroup>
                </ElSubMenu>
                <el-menu-item index="2">
                    <el-icon>
                        <ShoppingTrolley />
                    </el-icon>
                    <span>(My Cart)</span>
                </el-menu-item>
                <el-menu-item index="3">
                    <el-icon>
                        <Document />
                    </el-icon>
                    <span>（My orders）</span>
                </el-menu-item>
            </el-menu>

        </ElCol>
        <ElCol :span="18" :xs="24">
            <UserMessage v-if="currentPage == '1-1'" />
            <AddressManage v-if="currentPage == '1-2'" />
            <CartView v-if="currentPage == '2'" />
            <OrderView v-if="currentPage == '3'" />
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