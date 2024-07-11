<script setup lang="ts">
import PageContainer from '@/components/pageContainer.vue'
import { useUserStore } from '@/stores/modules/user'
import { type Ref, ref, onMounted } from 'vue'
import avatar from '@/assets/img/defaultAvatar.png'
import { EditPen } from '@element-plus/icons-vue'
import UploadAvatar from '@/views/DailyHandlerPages/user/components/uploadAvatar.vue'
import { updateUserInfoService } from '@/api/user'
import { ElMessage } from 'element-plus'

interface user {
    userUUID: string,
    userName: string,
    userAvatar: string | File,
    userNickName: string,
    email: string,
    phoneNumber: string,
    createTime: Date,
}

const dialog: Ref<any> = ref()

const useUser = useUserStore()
const isEdit: Ref<boolean> = ref(false)

const form: Ref<any> = ref()
const formModel: Ref<user> = ref({
    userUUID: '',
    userName: '',
    userAvatar: '',
    userNickName: '',
    email: '',
    phoneNumber: '',
    createTime: new Date()
})

const rules = {
    userNickName: [{
        pattern:
            /^\S{2,20}$/,
        message:
            '用户昵称长度为2-20位,且不能包含空格!',
        trigger: ['blur', 'change']
    }],
    email: [{
        required: true,
        message: '邮箱不能为空!',
        trigger: ['blur', 'change']
    }, {
        type: 'email',
        message: '请输入正确的邮箱地址',
        trigger: ['blur', 'change']
    }],
    phoneNumber: [{
        pattern: /^\+?[1-9]\d{1,14}$/,
        message: '请输入正确的手机号',
        trigger: ['blur', 'change']
    }]
}

onMounted(async () => {
    await initUserInfo()
})

const initUserInfo = async () => {
    await useUser.getUser()
    formModel.value = { ...useUser.user }
}

const userAvatarCheck = () => {
    const fileType = ['image/png', 'image/jpg', 'image/jpeg', 'image/webp']
    if (formModel.value.userAvatar) {
        if (typeof formModel.value.userAvatar === 'string') {
            return formModel.value.userAvatar
        } else if (fileType.includes(formModel.value.userAvatar.type)) {
            return URL.createObjectURL(formModel.value.userAvatar)
        }
    }
    return avatar
}

const changeEditStatus = () => {
    isEdit.value = !isEdit.value
}

const modifyAvatar = () => {
    dialog.value.open()
}

const onSubmit = async () => {
    await form.value.validate()
    await updateUserInfoService(formModel.value)
    ElMessage.success('修改成功!')
    changeEditStatus()
    await initUserInfo()
}

const cancelModify = () => {
    formModel.value = { ...useUser.user }
    changeEditStatus()
}

</script>

<template>
    <page-container title="个人信息页">
        <el-form
            ref="form"
            :model="formModel"
            :rules="rules"
            label-position="left"
            label-width="100px"
            class="userInfo">
            <el-form-item label="用户名:" prop="userName" style="width: 360px">
                <el-input disabled v-model="formModel.userName"></el-input>
            </el-form-item>

            <el-form-item label="用户昵称:" prop="userNickName" style="width: 360px">
                <el-input :disabled="!isEdit" v-model="formModel.userNickName"></el-input>
            </el-form-item>

            <el-form-item label="用户头像:" style="width: 360px" class="userAvatarBox">
                <el-avatar :size="50" :src="userAvatarCheck()" />
                <el-button v-if="isEdit" type="primary" plain class="ml-8" @click="modifyAvatar">
                    <el-icon>
                        <EditPen />
                    </el-icon>
                    <span>修改头像</span>
                </el-button>
            </el-form-item>

            <el-form-item label="用户邮箱:" prop="email" style="width: 360px">
                <el-input :disabled="!isEdit" v-model="formModel.email"></el-input>
            </el-form-item>

            <el-form-item label="手机号:" prop="phoneNumber" style="width: 360px">
                <el-input :disabled="!isEdit" v-model="formModel.phoneNumber"></el-input>
            </el-form-item>

            <el-button type="primary" plain v-if="!isEdit" @click="changeEditStatus">修改个人信息</el-button>
            <el-button type="primary" plain v-if="isEdit" @click="onSubmit">提交修改</el-button>
            <el-button type="danger" plain v-if="isEdit" @click="cancelModify">取消修改</el-button>
        </el-form>
    </page-container>

    <upload-avatar ref="dialog" v-model:userAvatar="formModel.userAvatar"></upload-avatar>
</template>

<style lang="scss" scoped>
.page-container {
    .userInfo {

        margin: 3vh 0 0 6vh;

        .userAvatarBox {
            display: flex;
            align-items: center;
            justify-content: center;
        }
    }

}
</style>