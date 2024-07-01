import { defineStore } from 'pinia'
import { type Ref, ref } from 'vue'
import { getUserInfoService } from '@/api/user'

export const useUserStore = defineStore(
    'using-user',
    () => {

        const user:Ref<any> = ref({})

        const setUser = (value : any) => {
            user.value = value
        }

        const getUser = async () => {
            const res = await getUserInfoService()
            user.value = res.data
        }

        const delUser = () => {
            user.value = ref({})
        }

        return {
            user,
            setUser,
            getUser,
            delUser
        }
    },
    {
        persist: true
    }
)
