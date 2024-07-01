import { defineStore } from 'pinia'
import { type Ref, ref } from 'vue'

export const useTokenStore = defineStore(
    'using-token',
    () => {
        const token: Ref<string> = ref('')
        const reCAPTCHAToken: Ref<string> = ref('')

        const setToken = (value: string) => {
            token.value = value
        }

        const delToken = () => {
            token.value = ''
        }

        const setRECAPTCHAToken = (value: string) => {
            reCAPTCHAToken.value = value
        }

        const delRCAPTCHAToken = () => {
            reCAPTCHAToken.value = ''
        }

        return {
            token,
            reCAPTCHAToken,
            setToken,
            delToken,
            setRECAPTCHAToken,
            delRCAPTCHAToken,
        }
    },
    {
        persist: true
    }
)
