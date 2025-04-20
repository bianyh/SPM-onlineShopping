<template>
    <div style="margin-top: 3.5rem; background-color: #fff;">
        <el-page-header @back="goBack" style="height: 4rem; padding: 1rem 2rem;">
            <template #content>
                <span class="font-600 mr-3"> All pending shipping orders </span>
            </template>
        </el-page-header>
        <el-collapse v-model="activeNames" accordion element-loading-text="Loading..."
            element-loading-background="rgba(122, 122, 122, 0.8)">
            <!-- os 是订单数组 ，pid是商品id，aid是地址id-->
            <el-collapse-item v-for="(oids, pid) in po" :key="pid" style="margin: 1rem;">
                <template #title>
                    <h2>
                        {{ products[pid]?.name }}
                        <el-icon class="header-icon">
                            <info-filled />
                        </el-icon>
                    </h2>
                </template>
                <el-card v-for="oid in oids" :key="oid" class="order-card" shadow="hover" v-loading="isLoading">
                    <div class="card-container" @click="dialogVisible = true; form.orderId = oid">
                        <el-row :gutter="20" v-if="oid in orders">
                            <el-col :span="18">
                                <el-descriptions :column="3" border width="150px">
                                    <el-descriptions-item label="Order ID" >
                                        {{ oid }}
                                    </el-descriptions-item>
                                    <el-descriptions-item label="Order time">
                                        {{ orders[oid]?.createdAt }}
                                    </el-descriptions-item>
                                    <ElDescriptionsItem label="Status">
                                        <el-tag :type="getStatusType(orders[oid]?.status)">
                                            {{ orderStatus[orders[oid]?.status] }}
                                        </el-tag>
                                    </ElDescriptionsItem>
                                    <el-descriptions-item label="Quantity">
                                        {{ quantitys[pid][oid] }}
                                    </el-descriptions-item>
                                    <el-descriptions-item label="Send to">
                                        {{ orders[oid]?.address }}
                                    </el-descriptions-item>
                                </el-descriptions>
                            </el-col>
                            <el-col :span="6">
                                <div class="button-group">
                                    <el-button type="primary" plain @click.stop="contactBuyer(orders[oid].id)"
                                        style="width: 40%; height: 50%; margin: 0;">
                                        <el-icon>
                                            <Message />
                                        </el-icon>
                                        <br>
                                        <span style="display: block; font-size: 0.8rem;">Contact<br>Buyer</span>
                                    </el-button>
                                    <el-button type="danger" plain @click.stop="cancelOrder(orders[oid].id)"
                                        :disabled="refuseOrderStatus.includes(orders[oid].status)"
                                        style="width: 40%; height: 50%; margin: 0;">
                                        <el-icon>
                                            <Close />
                                        </el-icon>
                                        <br>
                                        <span style="display: block; font-size: 0.8rem;">Cancel<br>Order</span>
                                    </el-button>
                                </div>
                                <br>
                                <el-button :type="orders[oid].status == 1 ? 'success' : 'warning'" plain
                                    @click.stop="dialogVisible = true; form.orderId = orders[oid].id"
                                    style="width: 100%; height: 50%; margin: 0;"
                                    :disabled="refuseOrderStatus.includes(orders[oid].status)">
                                    <el-icon>
                                        <Check v-if="orders[oid].status == 1" />
                                        <Setting v-if="orders[oid].status != 1" />
                                    </el-icon>
                                    <br>
                                    <span style="display: block; font-size: 0.8rem;">
                                        {{ orders[oid].status == 1 ? "ConfirmShipment" : "Change Tracking Number" }}
                                    </span>
                                </el-button>
                            </el-col>
                        </el-row>
                    </div>
                </el-card>
            </el-collapse-item>
        </el-collapse>

        <el-dialog v-model="dialogVisible" title="Enter Shipping Tracking Number" width="40%">
            <el-form ref="form" :model="form" label-width="120px">
                <el-form-item label="Tracking Number">
                    <el-input v-model="form.trackingNumber" placeholder="Please enter the tracking number"></el-input>
                </el-form-item>
            </el-form>
            <el-alert title="Note: Please ensure the tracking number is correct before submission." type="info"
                :closable="false"></el-alert>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="cancel">Cancel</el-button>
                    <el-button type="primary" @click="confirm()">Confirm</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ElButton, ElMessage, ElPageHeader, ElCollapseItem, ElDescriptionsItem } from 'element-plus';
</script>

<script>
import { orderCheckout, orderItem, orderSeller, orderSend, orderShow, orderStateUpdate, orderUpdate } from '@/api/order';
import { productInfo } from '@/api/product';
import { userAddressDetail } from '@/api/user';

export default {
    data() {
        return {
            activeNames: [],
            products: {},
            orders: {},
            po: {},
            quantitys: {},
            orderStatus: ['Pending payment', 'waiting for shipping', 'Shipped', 'Completed', 'Canceled'],
            refuseOrderStatus: [0, 3, 4],
            isLoading: true,
            dialogVisible: false,
            form: {
                orderId: '',
                trackingNumber: ''
            }
        };
    },
    computed: {
    },
    methods: {
        getStatusType(status) {
            const statusMap = {
                0: 'info',
                1: 'warning',
                2: 'success',
                3: 'primary',
                4: 'info,'
            };
            return statusMap[status] || 'primary';
        },
        cancelOrder(orderId) {
            console.log(`Cancel order: ${orderId}`);
            // 实现取消订单逻辑
            delete this.orders.orderId
            orderStateUpdate(orderId, 4).then((res) => {
                ElMessage({ message: "Canceled." })
            })
        },
        confirmShipment(orderId) {
            console.log(`Confirm shipment for order: ${orderId}`);
            this.orders[orderId].status = 2
            orderStateUpdate(orderId, 2).then((res) => { ElMessage({ message: res }) })
            // 实现确认发货逻辑
        },
        contactBuyer(orderId) {
            console.log(`Contact buyer for order: ${orderId}`);
            // 实现联系买家逻辑
        },
        goBack() {
            this.$router.back()
        },
        cancel() {
            this.dialogVisible = false;
            this.form.trackingNumber = '';
        },
        confirm() {
            console.log('Tracking Number:', this.form.trackingNumber);
            if (this.form.orderId != '')
                this.orders[this.form.orderId].status = 2
                orderSend(this.form.orderId, this.form.trackingNumber).then((res) => {
                    console.log(res)
                    this.dialogVisible = false;
                    this.form.trackingNumber = '';
                    orderStateUpdate(this.form.orderId, 2)
                    ElMessage({message:"Product shipped.",type:"success"})
                }).catch((err) => {
                    ElMessage({ message: err })
                })
        }
    },
    mounted() {
        this.isLoading = true;
        orderSeller()
            .then(async (res) => {
                this.products = [];
                this.po = res.data;
                let ordersArr = [];
                const addressCache = {}; // 缓存已查询的 address 数据

                // 获取所有商品信息
                for (const [pid, orders] of Object.entries(res.data)) {
                    let product;
                    await productInfo(pid).then((p) => {
                        product = p.data;
                    });
                    this.products[pid] = product;
                    ordersArr = [...ordersArr, ...orders];
                    this.quantitys[pid] = {};
                    for (const oid of orders) {
                        await orderItem(oid, pid).then((ores) => {
                            this.quantitys[pid][oid] = ores.data[0].quantity;
                        });
                    }
                }

                // 去重订单数组
                ordersArr = Array.from(new Set(ordersArr));

                // 为每个订单号获取订单详情
                this.orders = {}; // 初始化 orders 对象
                for (const oid of ordersArr) {
                    let order = [];
                    await orderCheckout(oid).then((orderRes) => {
                        order = orderRes.data;
                    });

                    // 从缓存中获取 address 数据，如果不存在则查询并缓存
                    const fetchAddress = async (addressId) => {
                        if (!addressCache[addressId]) {
                            await userAddressDetail(addressId).then((addrRes) => {
                                addressCache[addressId] = addrRes.data;
                            });
                        }
                        return addressCache[addressId];
                    };

                    const addressData = await fetchAddress(order.addressId);
                    order.address = `${addressData.recipient}, ${addressData.phone}, ${addressData.province}${addressData.city}${addressData.district}${addressData.street}`;

                    this.orders[oid] = order;
                }

                this.isLoading = false;
            })
            .catch((error) => {
                console.error('Error fetching data:', error);
                this.isLoading = false;
            });
    }
};
</script>

<style scoped>
.order-card {
    margin-bottom: 15px;
}

.button-group {
    display: flex;
    justify-content: space-between;
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
}
</style>