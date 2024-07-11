<script setup lang="ts">
import PageContainer from '@/components/pageContainer.vue'
import { ref } from 'vue'
import ReCAPTCHAcheck from '@/components/reCAPTCHAcheck.vue'
import { InfoFilled } from '@element-plus/icons-vue'
import { h } from 'vue'
import { ElNotification } from 'element-plus'
import { useRouter } from 'vue-router'
import { rePasswordService } from '@/api/user'

interface password {
    password: string,
    rePassword: string
}

const router = useRouter()

const form = ref<any>()
const formModel = ref<password>(
    {
        password: '',
        rePassword: ''
    }
)

const rules = {
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
    ]
}

const submitRePassword = async () => {
    await form.value.validate()
    await rePasswordService(formModel.value)
    router.push('/login')
    ElNotification({
        title: 'Success',
        message: h('i', { style: 'color: teal' }, '修改成功,请重新登录!'),
    })
}

const resetPassword = () => {
    formModel.value.password = ''
    formModel.value.rePassword = ''
}

</script>

<template>
    <page-container title="重置密码">

        <el-row class="rePasswordBox">
            <el-form
                ref="form"
                :rules="rules"
                :model="formModel"
                label-position="left"
                label-width="auto">
                <el-form-item prop="password" label="请输入密码:" style="width: 360px">
                    <el-input v-model="formModel.password" type="password" show-password></el-input>
                </el-form-item>
                <el-form-item prop="rePassword" label="请再次输入密码:" style="width: 360px">
                    <el-input v-model="formModel.rePassword" type="password" show-password></el-input>
                </el-form-item>
            </el-form>

        </el-row>
        <el-row class="operateButton">
            <el-popconfirm
                width="220"
                confirm-button-text="是的"
                cancel-button-text="我再想想"
                :icon="InfoFilled"
                icon-color="#626AEF"
                title="确定重置?"
                @confirm="submitRePassword"
            >
                <template #reference>
                    <ReCAPTCHAcheck>确认修改</ReCAPTCHAcheck>
                </template>
            </el-popconfirm>
            <el-button @click="resetPassword" type="danger" plain>重置</el-button>
        </el-row>

    </page-container>
</template>

<style lang="scss" scoped>
.rePasswordBox {
    margin-top: 3vh;
    margin-left: 6vh;

    .el-form-item{
        margin-bottom: 4vh;
    }

}

.operateButton {
    margin-top: 2vh;
    margin-left: 6vh;
}
</style>