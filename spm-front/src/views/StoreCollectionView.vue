<template>
    <div class="store-container" style="margin-top: 3.5rem;">
        <el-tabs v-model="activeStatus" class="demo-tabs" type="border-card">
            <el-tab-pane label="All Stores" name=-1></el-tab-pane>
            <el-tab-pane v-for="(status,index) in storeStatus" :key="status" :label="status" :name="index"></el-tab-pane>

            <el-card v-for="store in filteredStores" :key="store.id" class="store-card" shadow="hover">
                <el-row :gutter="20">
                    <el-col :span="8">
                        <el-image :src="store.picturePath" fit="cover" style="width: 100%; height: 150px;"></el-image>
                    </el-col>
                    <el-col :span="16">
                        <div>
                            <h2>{{ store.name }}</h2>
                            <h3>{{ store.description }}</h3>
                            <p>Address: {{ store.address }}</p>
                            <p>Created At: {{ store.createAt }}</p>
                            <el-tag :type="getStatusType(store.status)">{{ storeStatus[store.status] }}</el-tag>
                        </div>
                    </el-col>
                </el-row>
            </el-card>
        </el-tabs>
    </div>
</template>

<script>
import { storeAll } from '@/api/store';

export default {
    data() {
        return {
            stores: [
                {
                    id: 'ST001',
                    name: 'Store 1',
                    description: 'This is the first store.',
                    address: '123 Main St',
                    picturePath: 'https://via.placeholder.com/300',
                    status: 'Open'
                },
                {
                    id: 'ST002',
                    name: 'Store 2',
                    description: 'This is the second store.',
                    address: '456 Park Ave',
                    picturePath: 'https://via.placeholder.com/300',
                    status: 'Closed'
                },
                {
                    id: 'ST003',
                    name: 'Store 3',
                    description: 'This is the third store.',
                    address: '789 Oak Rd',
                    picturePath: 'https://via.placeholder.com/300',
                    status: 'Open'
                }
            ],
            storeStatus: { 0: 'Open', 1: 'Closed', },
            activeStatus: -1
        };
    },
    computed: {
        filteredStores() {
            if (this.activeStatus == -1) {
                return this.stores;
            }
            return this.stores.filter(store => store.status == this.activeStatus);
        }
    },
    methods: {
        getStatusType(status) {
            const statusMap = {
                0: 'success',
                1: 'info'
            };
            return statusMap[status] || 'default';
        }
    },
    mounted() {
        let username = window.localStorage.getItem('username')
        storeAll(username).then((res) => {
            this.stores = res.data
            console.log(res.data)
        })
    }
};
</script>

<style scoped>
.store-card {
    margin-bottom: 15px;
}
</style>