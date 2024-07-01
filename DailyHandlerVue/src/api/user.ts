import request from '@/utils/request'

export const getUserInfoService = () => request.get('/user/getUserInfo')
