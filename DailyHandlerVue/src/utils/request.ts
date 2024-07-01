//定制请求的实例

//导入axios  npm install axios
import axios from 'axios'
import { ElMessage } from 'element-plus'

//定义一个变量,记录公共的前缀  ,  baseURL
// const baseURL = 'http://localhost:8080';
const baseURL = '/api'
const instance = axios.create({ baseURL })

import { useTokenStore } from '@/stores'

const useToken = useTokenStore()
//添加请求拦截器
instance.interceptors.request.use(
    (config) => {
        // 请求前回调
        // 判断有没有token
        if (useToken.token) {
            config.headers.Authorization = useToken.token
        }
        if(useToken.reCAPTCHAToken) {
            config.headers.Reauthorization = useToken.reCAPTCHAToken
        }
        return config
    },
    (err) => {
        //请求错误的回调
        return Promise.reject(err)
    }
)

import router from '@/router'
//添加响应拦截器
instance.interceptors.response.use(
    (result) => {
        //判断业务状态码
        if (result.data.code === 0) {
            return result.data
        }
        ElMessage.error(result.data.message ? result.data.message : '服务异常')
        return Promise.reject(result.data)
    },
    (err) => {
        console.log(err)
        //判断响应状态码，如果为401，则证明未登录，提示请登录，屏跳转登录页面
        if (err.response.status === 401) {
            // alert('请登录');
            ElMessage.error('请先登录')
            //跳转到登录页面
            router.push('/login').then()
        } else {
            ElMessage.error('服务异常')
        }
        return Promise.reject(err) //异步的状态转化成失败的状态
    }
)

export default instance
