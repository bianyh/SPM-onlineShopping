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
    <ElCard>
        <ElContainer>
            <ElImage :src="img2url"></ElImage>
            <ElCol :span="14">{{ product.name }}</ElCol>
            <ElCol :span="10">{{ product.id }}</ElCol>
        </ElContainer>
    </ElCard>
</template>