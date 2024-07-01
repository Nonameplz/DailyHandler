<script setup lang="ts">
import { ref } from 'vue'
import ReCAPTCHACheck from '@/components/reCAPTCHAcheck.vue'
import { userLoginService, userRegisterService } from '@/api/authentication'
import { ElMessage } from 'element-plus'
import { Check, Close } from '@element-plus/icons-vue'
import { useTokenStore } from '@/stores'
import { useRouter } from 'vue-router'

const useToken = useTokenStore()
const router = useRouter()

const isRegister = ref(false)
const rememberMe = ref(true)

const form = ref()
const defaultForm = {
    userName: '',
    password: '',
    rePassword: '',
    email: ''
}
const formModel = ref({
    userName: '',
    password: '',
    rePassword: '',
    email: ''
})

const rules = {
    userName: [
        { required: true, message: '清输入用户名', trigger: 'blur' },
        {
            min: 4,
            max: 20,
            message: '用户名长度必须为4 - 20位的 字符',
            trigger: ['blur', 'change']
        }
    ],
    password: [
        { required: true, message: '密码不能为空', trigger: 'blur' },
        {
            pattern:
                /^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-zA-Z0-9])[A-Za-z0-9!@#$%^&*()_+\-=\[\]{}|\\:;"'<>,.?/~]{6,20}$/,
            message:
                '密码长度必须为6 - 20位 数字 英文 特殊字符以及至少存在1个大写英文的组合字符串',
            trigger: ['blur', 'change']
        }
    ],
    rePassword: [
        {
            validator: (rule: any, value: any, callback: any) => {
                if (value === '') {
                    callback(new Error('请再次输入密码!'))
                } else if (value !== formModel.value.password) {
                    callback(new Error('两次密码不匹配!'))
                } else {
                    callback()
                }
            },
            trigger: ['blur', 'change']
        }
    ],
    email: [
        {
            required: true,
            message: '请输入邮箱地址',
            trigger: 'blur'
        },
        {
            type: 'email',
            message: '请输入正确的邮箱地址',
            trigger: ['blur', 'change']
        }
    ]
}

const login = async () => {
    const res: any = await userLoginService({
        userName: formModel.value.userName,
        password: formModel.value.password,
        rememberMe: rememberMe.value
    })
    useToken.setToken(res.data)
    ElMessage.success('登录成功!')
    setTimeout(() => {
        router.push('/')
    }, 500)
}
const register = async () => {
    await form.value.validate()
    await userRegisterService(formModel.value)
    ElMessage.success('注册成功')
    exchangeOption()
}
const exchangeOption = () =>{
    isRegister.value = !isRegister.value
    formModel.value = { ...defaultForm }
}
</script>

<template>
    <div class="login-layout">
        <el-container>
            <el-aside class="aside">
                <h1 class="logo">DailyHandler</h1>
            </el-aside>
            <el-main class="main">
                <!--                登录页面                -->
                <el-form
                    ref="form"
                    :label-position="'left'"
                    label-width="auto"
                    :model="formModel"
                    class="form-style"
                    v-if="!isRegister"
                >
                    <h2 class="title">DailyHandler - 登录</h2>
                    <el-form-item label="用户名:" prop="userName">
                        <el-input
                            placeholder="请输入用户名"
                            v-model="formModel.userName"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="密码:" prop="password">
                        <el-input
                            placeholder="请输入密码"
                            type="password"
                            v-model="formModel.password"
                        ></el-input>
                    </el-form-item>
                    <div class="option">
                        <p class="align-middle">
                            记住我:
                            <el-switch
                                v-model="rememberMe"
                                style="margin-left: 6px"
                                inline-prompt
                                :active-icon="Check"
                                :inactive-icon="Close"
                            />
                        </p>
                        <el-link type="danger">忘记密码?</el-link>
                    </div>
                    <div class="operate">
                        <ReCAPTCHACheck class="mr-10" @click="login"
                            >登录
                        </ReCAPTCHACheck>
                        <el-button
                            type="primary"
                            plain
                            @click="exchangeOption"
                            >去注册 →
                        </el-button>
                    </div>
                </el-form>
                <!--                注册页面                -->
                <el-form
                    ref="form"
                    :label-position="'left'"
                    label-width="auto"
                    :model="formModel"
                    :rules="rules"
                    class="form-style"
                    v-if="isRegister"
                >
                    <h2 class="title">DailyHandler - 注册</h2>
                    <el-form-item label="用户名:" prop="userName">
                        <el-input
                            placeholder="请输入用户名"
                            v-model="formModel.userName"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="密码:" prop="password">
                        <el-input
                            placeholder="请输入密码"
                            type="password"
                            v-model="formModel.password"
                            :show-password="true"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="请重复密码:" prop="rePassword">
                        <el-input
                            placeholder="请再次输入密码"
                            type="password"
                            v-model="formModel.rePassword"
                            :show-password="true"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱:" prop="email">
                        <el-input
                            placeholder="请输入邮箱"
                            type="text"
                            v-model="formModel.email"
                        ></el-input>
                    </el-form-item>
                    <div class="operate">
                        <el-button
                            type="primary"
                            plain
                            @click="exchangeOption"
                            class="mr-10"
                            >← 去登录
                        </el-button>
                        <ReCAPTCHACheck @click="register">
                            注册
                        </ReCAPTCHACheck>
                    </div>
                </el-form>
            </el-main>
        </el-container>
    </div>
</template>

<style lang="scss" scoped>
.login-layout {
    height: 100vh;
    background: pink url('@/assets/img/login_bg.webp') no-repeat center center
        fixed;
    background-size: cover;

    .el-container {
        height: 100%;

        .aside {
            width: 60%;
            background-color: rgba(255, 192, 203, 0.4);
            display: flex;
            justify-content: center;
            align-items: center;

            .logo {
                width: 80vh;
                height: 80vh;
                color: rgba(255, 192, 203, 0);
                background: url('@/assets/img/Logo2.svg') no-repeat center
                    center;
                background-size: cover;
            }
        }

        .main {
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 0;

            .form-style {
                display: flex;
                flex-direction: column;
                justify-content: center;
                height: 100%;
                width: 100%;
                background-color: rgba(169, 209, 255, 0.6);

                .title {
                    width: 100%;
                    margin: 0 0 50px;
                    text-align: center;
                    font-size: 2rem;
                }

                :deep(.el-form-item) {
                    padding: 0 15%;
                    margin-bottom: 50px;
                }

                :deep(.el-form-item__label) {
                    font-size: larger;
                    font-weight: bold;
                    color: black;
                }

                :deep(.el-form-item__error) {
                    font-size: large;
                }
            }

            .option {
                display: flex;
                align-content: center;
                justify-content: space-around;
                margin-bottom: 24px;
            }

            .operate {
                display: flex;
                justify-content: center;
                align-items: center;

                .el-button {
                    width: 100px;
                    height: 40px;
                }
            }
        }
    }
}
</style>
