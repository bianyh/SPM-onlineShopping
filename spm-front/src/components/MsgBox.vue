<script>
export default {
    data() {
        return {
            stateCode: '-1',
            msg: 'Unknown Message',
            type: 0,
            tempdata: null,
            visible: false,
            content: '',
            callbacks: [],
        }
    },
    methods: {
        update(result) {
            // 是对象，则进行完整填充
            if (typeof (result) == "object") {
                this.messageText = result.message
                this.updateMsg(result.code, result.message, result.type)
                this.tempdata = result.data
                if (result.content)
                    this.content = result.content
                //判断是否有回调函数
                if (result.callbacks)
                    this.callbacks = result.callbacks
                return
            }
            // 不是对象，则直接填充消息
            this.updateMsg(0, result, 0)
            return
        },
        updateMsg(stateCode, message, controlType) {
            this.msg = message
            this.stateCode = stateCode
            if (controlType)
                this.type = controlType
            else
                this.type = 0
            this.visible = true
            this.content = ''
            this.callbacks = []
            this.tempdata = null
        },
        runCallbacks(index, param) {
            if (this.callbacks)
                if (index > -1 && index < this.callbacks.length) {
                    const callback = this.callbacks[index]
                    if (typeof(callback) == 'function')
                        callback(param)
                }
            this.hideBox(param)
        },
        hideBox(emits) {
            this.$emit('back', emits)
            this.visible = false
        }
    }
}
</script>


<template>
    <Transition name="c">
        <div v-show="visible" class="bgforbid">
            <div class="msg-panel">
                <span class="row">
                    <!--img :src="imgurl"-->
                    <a class="divider">{{ '[' + stateCode + '] ' + msg }}</a>
                    <h3>{{ content }}</h3>
                </span>
                <div class="operabtn" v-show="type == 1">
                    <button @click="runCallbacks(1, true)">Confirm</button>
                    <button @click="runCallbacks(0, false)">Cancel</button>
                </div>
                <div class="operabtn" v-show="type == 0">
                    <button @click="runCallbacks(0, true)">OK</button>
                </div>
            </div>
        </div>
    </Transition>
</template>

<style scoped>
.bgforbid {
    display: flex;
    align-items: center;
    /* 垂直居中 */
    justify-content: center;
    /* 水平居中 */
    position: fixed;
    width: 100%;
    height: 100%;
    left: 0;
    top: 0;
    background-color: #00000040;
    transition: 0.2s;
}

.msg-panel {
    border-radius: 4px;
    display: block;
    position: relative;
    width: 80%;
    background-color: #ffffffcc;
    backdrop-filter: blur(10px);
    box-shadow: 0 10px 10px #00000040;
    padding: 1rem;
}

.row {
    position: relative;
    width: 90%;
    margin: auto;
    height: auto;
}

h3 {
    display: block;
    word-break: break-all;
}

img {
    width: 100%;
    height: auto;
    display: inline;
}

.c-enter-from {
    opacity: 0;
}

.c-enter-to {
    opacity: 1;
}

.c-leave-from {
    opacity: 1;
}

.c-leave-to {
    opacity: 0;
}

.operabtn {
    position: relative;
    display: inline;
    width: 100%;
    text-align: center;
    bottom: 2%;
}

.operabtn button {
    font-size: 1rem;
    background: none;
    border: 0px solid;
    border-radius: 4px;
    display: inline;
    margin: 0 0.2rem;
    padding: 1rem;
    transition: 0.3s;
    float: right;
}

.operabtn button:hover {
    background: #e072ff80;
    transition: 0.3s;
}

@media (min-width: 1024px) {
    .msg-panel {
        width: 50%;
    }
}
</style>