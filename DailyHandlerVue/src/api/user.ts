import request from '@/utils/request'

export const getUserInfoService = () => request.get('/user/getUserInfo')

export const updateUserInfoService = (userInfo: any) => {
    const formData = new FormData()
    for (const key in userInfo) {
        formData.append(key, userInfo[key])
    }

    return request.put('/user/updateUserInfo', formData)
}

export const rePasswordService = (passwordData: any) => {
    const params = new URLSearchParams()
    for (const key in passwordData) {
        params.append(key, passwordData[key])
    }
    return request.put('/user/rePassword', params)
}