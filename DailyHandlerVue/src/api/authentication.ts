import request from '@/utils/request'

export const reCAPTCHACheckService = (token: FormData) =>
    request.post('authentication/reCAPTCHACheck', token)

export const userLoginService = (loginData: any) => {
    //借助于UrlSearchParams完成传递
    const params = new URLSearchParams()
    for (const key in loginData) {
        params.append(key, loginData[key])
    }
    return request.post('authentication/login', params)
}

export const userRegisterService = (registerData: any) => {
    //借助于UrlSearchParams完成传递
    const params = new URLSearchParams()
    for (const key in registerData) {
        params.append(key, registerData[key])
    }

    return request.post('authentication/register', params)
}
