import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import '@/assets/main.scss'
import '@/assets/tailwindcss.css'
import App from './App.vue'
import router from './router'
import pinia from '@/stores'
import { VueReCaptcha } from 'vue-recaptcha-v3'

const app = createApp(App)

app.use(pinia)
app.use(router)
app.use(ElementPlus)
app.use(VueReCaptcha, {
    siteKey: 'reCAPTCHA v3 key',
    loaderOptions: {
        useRecaptchaNet: true,
        autoHideBadge: false
    }
})

app.mount('#app')
