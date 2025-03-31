<template>
    <div style="margin-top: 3.5rem; background-color: #fff;" 
    v-loading.fullscreen.lock="isLoading">
        
        <el-collapse v-model="activeNames" accordion v-if="!isLoading">
            <!-- os 是订单数组 ，pid是商品id-->
            <el-collapse-item v-for="(oids, pid) in po" :key="pid" style="margin: 1rem;">
                <template #title>
                    <h2>
                        {{ products[pid]?.name }}
                        <el-icon class="header-icon">
                            <info-filled />
                        </el-icon>
                    </h2>
                </template>
                <el-card v-for="oid in oids" :key="oid" class="order-card" shadow="hover">
                    <div>
                        <p><strong>Order ID:</strong> {{ oid }}</p>
                        <p><strong>Quantity:</strong> {{ quantitys[pid][oid] }}</p>
                        <p><strong>Address:</strong> {{ orders[oid]?.address }}</p>
                        <p><strong>Created At:</strong> {{ orders[oid]?.createdAt }}</p>
                        <el-tag :type="getStatusType(orders[oid]?.status)">{{ orderStatus[orders[oid]?.status] }}</el-tag>
                    </div>
                    <div style="margin-top: 15px;">
                        <el-button @click="cancelOrder(orders[oid].id)" type="danger" plain>Cancel Order</el-button>
                        <el-button @click="confirmShipment(orders[oid].id)" type="success" plain>Confirm
                            Shipment</el-button>
                        <el-button @click="contactBuyer(orders[oid].id)" type="warning" plain>Contact Buyer</el-button>
                    </div>
                </el-card>
            </el-collapse-item>
        </el-collapse>
    </div>
</template>

<script setup>
import { ElButton, ElMessage } from 'element-plus';
</script>

<script>
import { orderCheckout, orderItem, orderSeller, orderShow, orderUpdate } from '@/api/order';
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
            orderStatus: ['Pending', 'Shipped', 'Completed'],
            isLoading: true,
        };
    },
    computed: {
    },
    methods: {
        getStatusType(status) {
            const statusMap = {
                0: 'warning',
                1: 'info',
                2: 'success'
            };
            return statusMap[status] || 'primary';
        },
        cancelOrder(orderId) {
            console.log(`Cancel order: ${orderId}`);
            // 实现取消订单逻辑
            delete this.orders.orderId
            ElMessage({message:"Delete Done."})
        },
        confirmShipment(orderId) {
            console.log(`Confirm shipment for order: ${orderId}`);
            this.orders[orderId].status = 2
            orderUpdate(orderId, 0).then((res) => {ElMessage({message:res})})
            // 实现确认发货逻辑
        },
        contactBuyer(orderId) {
            console.log(`Contact buyer for order: ${orderId}`);
            // 实现联系买家逻辑
        }
    },
    mounted() {
        this.isLoading = true
        orderSeller().then(async (res) => {
            this.products = [];
            this.po = res.data;
            let ordersArr = [];

            // 获取所有商品信息
            for (const [pid, orders] of Object.entries(res.data)) {
                let product;
                await productInfo(pid).then((p) => {
                    product = p.data;
                });
                this.products[pid] = product;
                ordersArr = [...ordersArr, ...orders];
                this.quantitys[pid] = {}
                for (const oid of orders) {
                    await orderItem(oid, pid).then((ores) => {
                        //console.log(ores.data[0])
                        this.quantitys[pid][oid] = ores.data[0].quantity
                    })
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
                await userAddressDetail(order.addressId).then((addrRes) => {
                    let item = addrRes.data
                    order.address = item.recipient + ", " + item.phone + ", " + item.province + item.city + item.district + item.street
                })
                this.orders[oid] = order;
            }
            this.isLoading = false
        });
    }
};
</script>

<style scoped>
.order-card {
    margin-bottom: 15px;
}
</style>