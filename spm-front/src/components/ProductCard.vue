<script>
import { ElCard, ElCol, ElContainer, ElImage } from 'element-plus';

export default {
    data() {
        return {}
    },
    props: {
        product: Object
    },
    computed: {
        img2url() {
            if (this.product)
                return 'data:image/png;base64,' + this.product.preview
            else
                return ''
        }
    }
}
</script>

<template>
    <div class="card">
        asd
    </div>
</template>

<style>
.card {
    display: flex;
    position: absolute;
    background-color: #474747;
    min-width: 100px;
    min-height: 100px;
}
</style>