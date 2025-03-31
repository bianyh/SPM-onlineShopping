<template>
    <div class="body">
        <div class="header">
            <div class="wrapper">
                <!-- 当前所处位置 -->
                <div class="location">
                    <div class="location-style"><a href="#"></a>
                        <span @click="handlegobackHome">Home</span>
                    </div>
                </div>
            </div>
        </div>
        <vueQr :text="qrText" :size="200" colorDark="#000" colorLight="#fff">
        </vueQr>
        <ElResult></ElResult>
        <div class="paymentsuccess_body">
            <div class="paymentsuc">
                <div class="PAYMENTSUCCESSFUL">PAYMENT SUCCESSFUL!</div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ElResult } from 'element-plus';
import vueQr from 'vue-qr';
import { useRouter } from 'vue-router';
</script>
<style scoped>
.body {
    background-color: white;
    height: 800px;
}

.wrapper {
    margin: 0 auto;
    width: 1440px;
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
    left: 141px;
    width: 1172px;
    height: 500px;
    opacity: 1;
    background: rgba(229, 229, 229, 0.3);
    margin: 0 auto;
    margin-top: 80px;
    display: flex;
    justify-content: center;
    /* 水平居中 */
    align-items: center;
    /* 垂直居中 */
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