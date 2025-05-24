<template>
    <div class="body">
        <div class="header" v-if="!isPaymentResult">
            <h1>
                Jumping to payment page, please wait...
            </h1>
            <p></p>
            If the page has not been redirected for a long time, click this ->
            <ElButton @click="surePay">pay</ElButton>
        </div>
        <div class="header" v-if="isPaymentResult">
            <h1>Payment Details</h1>
        </div>
        <!--vueQr :text="qrText" :size="200" colorDark="#000" colorLight="#fff">
        </vueQr-->
        <ElResult v-if="isPaymentResult" icon="success" title="PAYMENT SUCCESS!" class="paymentsuccess_body">
            <template #extra>
                <div v-if="paymentParams">
                    <p><strong>Character Set:</strong> {{ paymentParams.charset }}</p>
                    <p><strong>Out Trade No:</strong> {{ paymentParams.out_trade_no }}</p>
                    <p><strong>Method:</strong> {{ paymentParams.method }}</p>
                    <p><strong>Total Amount:</strong> {{ paymentParams.total_amount }}</p>
                    <p><strong>Sign:</strong> {{ paymentParams.sign }}</p>
                    <p><strong>Trade No:</strong> {{ paymentParams.trade_no }}</p>
                    <p><strong>Auth App ID:</strong> {{ paymentParams.auth_app_id }}</p>
                    <p><strong>Version:</strong> {{ paymentParams.version }}</p>
                    <p><strong>App ID:</strong> {{ paymentParams.app_id }}</p>
                    <p><strong>Sign Type:</strong> {{ paymentParams.sign_type }}</p>
                    <p><strong>Seller ID:</strong> {{ paymentParams.seller_id }}</p>
                    <p><strong>Timestamp:</strong> {{ paymentParams.timestamp }}</p>
                </div>
                <el-button type="primary" @click="goBack">Back</el-button>
            </template>
        </ElResult>
    </div>
</template>

<script setup>
import { orderCheckout, orderDetail, orderStateUpdate } from '@/api/order';
import { payZFB } from '@/api/pay';
import { ElButton, ElMessage, ElResult } from 'element-plus';
import vueQr from 'vue-qr';
import { useRouter } from 'vue-router';
</script>

<script>
export default {
    data() {
        return {
            paymentParams: null,
            isPaymentResult: false,
            order: null,
            amountVal: '',
            disabled: false,
            // 同步跳转页面地址
            returnUrl: 'http://localhost:5174/#/payment',
            //充值参数
            rechargeParams: {
                "totalAmt": '', //金额
                "paymentType": "0", //支付方式[0:微信,1:支付宝,2:余额,3:活动]
                "transType": "0" //交易类型[0:充值,1:消费]
            }
        }
    },
    created() {
        this.extractPaymentParams();
    },
    mounted() {
        orderCheckout(window.localStorage.getItem("payOrderId")).then((res) => {
            this.order = res.data
            this.rechargeParams.totalAmt = this.order.totalAmount
            this.rechargeParams.paymentType = "1" //this.order.paymentMethod
            this.rechargeParams.transType = 1
            console.log(res.data)
            if (!this.isPaymentResult)
                this.surePay()
        }).catch()
    },
    methods: {
        extractPaymentParams() {
            this.paymentParams = { ...this.$route.query };
            console.log(this.paymentParams)
            if (this.paymentParams.sign) {
                this.isPaymentResult = true
                if (window.localStorage.getItem("outTradeNo") == this.paymentParams?.out_trade_no) {
                    orderStateUpdate(window.localStorage.getItem("outTradeOrderId"), 1)
                    ElMessage({ message: 'Payment Success', type: 'success' })
                }
            }
        },
        //充值金额
        amountChange(val) {
            this.rechargeParams.totalAmt = val;
            if (val === '') {
                this.disabled = false
            } else {
                this.disabled = true
            }
        },
        //支付方式
        paymentTypeChange(val) {
            this.rechargeParams.paymentType = val
        },
        //确认支付
        async surePay() {
            if (this.rechargeParams.totalAmt === '') {
                this.$message.warning('请输入金额');
                return;
            }
            if (this.rechargeParams.paymentType === '0') {

            } else if (this.rechargeParams.paymentType === '1') {
                let param = {
                    // 生成一个随机的订单号 方便测试
                    outTradeNo: this.getProjectNum() + Math.floor(Math.random() * 10000),
                    // 传递支付金额
                    totalAmount: this.rechargeParams.totalAmt,
                    // 传递同步跳转地址
                    returnUrl: this.returnUrl,
                    // 商品名称
                    subject: '支付金额',
                    // 支付类型
                    productCode: 'FAST_INSTANT_TRADE_PAY'
                }
                console.log(param)
                payZFB(param).then(code => {
                    if (code.code === 0) {
                        window.localStorage.setItem("outTradeNo", param.outTradeNo)
                        window.localStorage.setItem("outTradeOrderId", this.order.id)
                        // 支付方式跳转
                        this.$message.success('支付宝支付')
                        const payDiv = document.getElementById('payDiv');
                        if (payDiv) {
                            document.body.removeChild(payDiv);
                        }
                        const div = document.createElement('div');
                        div.id = 'payDiv';
                        div.innerHTML = code.data;
                        document.body.appendChild(div);
                        document.getElementById('payDiv').getElementsByTagName('form')[0].submit();
                    }
                })
            }
        },
        // 获取当前日期的方法
        getProjectNum() {
            const projectTime = new Date() // 当前中国标准时间
            const Year = projectTime.getFullYear() // 获取当前年份 支持IE和火狐浏览器.
            const Month = projectTime.getMonth() + 1 // 获取中国区月份
            const Day = projectTime.getDate() // 获取几号
            var CurrentDate = Year
            if (Month >= 10) { // 判断月份和几号是否大于10或者小于10
                CurrentDate += Month
            } else {
                CurrentDate += '0' + Month
            }
            if (Day >= 10) {
                CurrentDate += Day
            } else {
                CurrentDate += '0' + Day
            }
            return CurrentDate
        },
        goBack() {
            window.localStorage.setItem("navigationParams", 3)
            this.$router.push("/user")
        }
    }
}
</script>

<style scoped>
.body {
    background-color: white;
    margin-top: 3.5rem;
}

.header {
    height: 66px;
    opacity: 1;
    background: url(https://img.js.design/assets/img/67cfdb8607c4c2e467899865.png);
    background-size: cover;
    /* 使背景图覆盖整个元素 */
    background-repeat: no-repeat;
    /* 防止背景图重复 */
}

.header .wrapper {
    padding-top: 0;
    display: flex;
    /* justify-content: space-between; 让元素两端对齐，userMessage 会在最右侧 */
    align-items: center;
    /* 使子元素垂直居中 */
    height: 100%;
    /* 确保 wrapper 高度与 header 一致 */
}

.location {
    /* margin-right: auto; 使当前位置信息在左侧显示 */
    width: 120px;
    display: table-cell;
    vertical-align: middle;
    text-align: center;
}

.location-style {
    width: 87px;
    height: 45px;
    display: flex;
    justify-content: center;
    align-items: center;
}

.location-style span {
    color: white;
    text-decoration: none;
    font-size: 22px;
}

.paymentsuccess_body {
    overflow: hidden;
    width: calc(100% - 4rem);
    height: calc(100% - 16rem);
    background: rgba(229, 229, 229, 0.3);
    margin: 2rem;
    display: flex;
    justify-content: center;
    /* 水平居中 */
    align-items: center;
    /* 垂直居中 */
    border-radius: 8px;
}

.paymentsuc {
    display: flex;
    justify-content: center;
    /* 水平居中 */
    align-items: center;
    /* 垂直居中 */
    background: url(https://img.js.design/assets/img/67cfdb8607c4c2e467899865.png);
    background-size: cover;
    background-repeat: no-repeat;
    width: 811px;
    height: 240px;
    opacity: 1;
    border-radius: 50px;
    margin: 0 auto;
}

.PAYMENTSUCCESSFUL {
    width: 531px;

    opacity: 1;
    font-size: 48px;
    font-weight: 700;
    letter-spacing: 0px;
    line-height: 59.52px;
    color: rgba(255, 255, 255, 1);
    text-align: center;
    /* 改为居中 */

}
</style>