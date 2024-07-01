<script setup lang="ts">
import { useReCaptcha } from 'vue-recaptcha-v3'
import { useTokenStore } from '@/stores'

const useToken = useTokenStore()

const executeRecaptcha = useReCaptcha()?.executeRecaptcha
const recaptchaLoaded = useReCaptcha()?.recaptchaLoaded

const recaptcha = async () => {
    let token = ''

    if (recaptchaLoaded) {
        await recaptchaLoaded()
    }
    if (executeRecaptcha) {
        token = await executeRecaptcha('verify')
    }

    // console.log(token)

    useToken.setRECAPTCHAToken(token)
}
</script>

<template>
    <el-button type="primary" plain @click="recaptcha">
        <slot></slot>
    </el-button>
</template>

<style scoped></style>
