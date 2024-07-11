import { defineStore } from 'pinia'
import { type Ref, ref } from 'vue'
import { getUserInfoService } from '@/api/user'

export const useUserStore = defineStore(
    'using-user',
    () => {

        const user:Ref<any> = ref({})
        const seeAnnouncement = ref<boolean>(false)

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

        const setSeeAnnouncement = (value : boolean) => {
            seeAnnouncement.value = value
        }

        return {
            user,
            seeAnnouncement,
            setUser,
            getUser,
            delUser,
            setSeeAnnouncement,
        }
    },
    {
        persist: true
    }
)
