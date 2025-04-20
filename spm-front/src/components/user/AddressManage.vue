<template>
    <div class="address-container">
        <!-- 顶部按钮 -->
        <div class="action-buttons">
            <el-button type="primary" @click="showAddDrawer">Add Address</el-button>
            <el-button type="danger" @click="toggleDeleteMode">{{ isDeleteMode ? 'Cancel Delete' : 'Delete'
            }}</el-button>
        </div>

        <!-- 地址列表 -->
        <el-card v-for="address in addresses" :key="address.id" class="address-card"
            :class="{ 'delete-mode': isDeleteMode }" @click="handleAddressClick(address)">
            <div class="address-content">
                <div class="recipient-phone">
                    <div class="recipient">{{ address.recipient }}</div>
                    <div class="phone">{{ address.phone }}</div>
                </div>
                <div class="full-address">
                    {{ address.province }}, {{ address.city }}, {{ address.district }}, {{ address.street }}
                </div>
                <div class="address-actions">
                    <el-tag v-if="address.isDefault" type="success">Default Address</el-tag>
                </div>
            </div>
        </el-card>

        <!-- 编辑抽屉 -->
        <el-drawer v-model="drawerVisible" title="Edit Address" direction="btt" size="60%">
            <el-form ref="addressForm" :model="activeAddress" label-width="120px">
                <el-form-item label="Recipient">
                    <el-input v-model="activeAddress.recipient" placeholder="Enter recipient name"></el-input>
                </el-form-item>
                <el-form-item label="Phone">
                    <el-input v-model="activeAddress.phone" placeholder="Enter phone number"></el-input>
                </el-form-item>
                <el-form-item label="Province">
                    <el-input v-model="activeAddress.province" placeholder="Enter province"></el-input>
                </el-form-item>
                <el-form-item label="City">
                    <el-input v-model="activeAddress.city" placeholder="Enter city"></el-input>
                </el-form-item>
                <el-form-item label="District">
                    <el-input v-model="activeAddress.district" placeholder="Enter district"></el-input>
                </el-form-item>
                <el-form-item label="Address">
                    <el-input v-model="activeAddress.street" placeholder="Enter detailed address"></el-input>
                </el-form-item>
                <el-form-item label="Set as Default">
                    <el-button type="primary" @click="setDefaultAddress" :disabled="activeAddress.isDefault">
                        {{ activeAddress.isDefault ? 'Defaulted' :'Set Default' }}
                    </el-button>
                </el-form-item>
            </el-form>
            <div class="drawer-actions">
                <el-button @click="drawerVisible = false">Cancel</el-button>
                <el-button type="primary" @click="saveAddress">Save</el-button>
            </div>
        </el-drawer>
    </div>
</template>

<script>
import { userAddressAppend, userAddressDefault, userAddressDelete, userAddressShow, userAddressUpdate } from '@/api/user';
import { ElMessage } from 'element-plus';

export default {
    data() {
        return {
            addresses: [],
            drawerVisible: false,
            activeAddress: null,
            isDeleteMode: false,
            deleteTarget: null
        };
    },
    methods: {
        // 显示添加地址的抽屉
        showAddDrawer() {
            this.activeAddress = {
                id: -1,
                recipient: '',
                phone: '',
                province: '',
                city: '',
                district: '',
                street: '',
                isDefault: false,
                createdAt: new Date().toISOString()
            };
            this.drawerVisible = true;
        },
        // 切换删除模式
        toggleDeleteMode() {
            this.isDeleteMode = !this.isDeleteMode;
            this.deleteTarget = null;
        },
        // 处理地址点击事件
        handleAddressClick(address) {
            if (this.isDeleteMode) {
                if (this.deleteTarget === address.id) {
                    this.deleteAddress(address.id);
                    this.deleteTarget = null;
                    this.toggleDeleteMode();
                } else {
                    this.deleteTarget = address.id;
                }
            } else {
                this.activeAddress = { ...address };
                this.drawerVisible = true;
            }
        },
        // 删除地址
        deleteAddress(id) {
            userAddressDelete(id).then((res) => {
                ElMessage({ message: res.message })
                this.addresses = this.addresses.filter(address => address.id !== id);
            })
        },
        // 保存地址
        saveAddress() {
            const index = this.addresses.findIndex(address => address.id === this.activeAddress.id);
            if (index !== -1) {
                //更新模式
                this.addresses[index] = { ...this.activeAddress };
                var a = this.activeAddress
                userAddressUpdate(
                    a.id,
                    a.recipient,
                    a.phone,
                    a.province,
                    a.city,
                    a.district,
                    a.street,
                    a.isDefault
                ).then((res) => {
                    ElMessage({ message: res })
                })
            } else {
                //添加模式
                this.addresses.push(this.activeAddress);
                var a = this.activeAddress
                userAddressAppend(
                    a.recipient,
                    a.phone,
                    a.province,
                    a.city,
                    a.district,
                    a.street,
                    a.isDefault
                ).then((res) => {
                    ElMessage({ message: res })
                })
            }
            this.drawerVisible = false;
        },
        // 设置默认地址
        setDefaultAddress() {
            this.addresses.forEach(address => {
                address.isDefault = address.id === this.activeAddress.id;
            });
            this.activeAddress.isDefault = !this.activeAddress.isDefault;
            userAddressDefault(this.activeAddress.id).then((res) => {
                ElMessage({message: res})
            })
        }
    },
    mounted() {
        userAddressShow().then((res) => {
            this.addresses = res.data
            this.addresses.forEach(address => {address.isDefault = address.default})
            console.log(res.data)
        })
    }
};
</script>

<style scoped>
.address-container {
    padding: 20px;
    margin-top: 3.5rem;
}

.action-buttons {
    margin-bottom: 20px;
}

.address-card {
    margin-bottom: 15px;
    padding: 15px;
    cursor: pointer;
    transition: all 0.3s;
}

.address-card:hover {
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.address-card.delete-mode {
    cursor: pointer;
}

.address-card.delete-mode:hover {
    background-color: #fef0f0;
}

.recipient-phone {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
}

.recipient {
    font-weight: bold;
}

.phone {
    color: #67c23a;
}

.full-address {
    margin-bottom: 10px;
    color: #606266;
}

.address-actions {
    display: flex;
    justify-content: flex-end;
}

.drawer-actions {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
}
</style>