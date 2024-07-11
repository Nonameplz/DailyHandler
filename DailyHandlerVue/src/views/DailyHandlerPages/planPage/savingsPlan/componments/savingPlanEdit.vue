<script setup lang="ts">
import { type Ref, ref, h } from 'vue'
import { ElNotification } from 'element-plus'
import {
    addSavingPlanService,
    addSavingProcessService,
    modifySavingPlanService,
    modifySavingProcessService
} from '@/api/plan'

const isVisible = ref<boolean>(false)
const editTarget = ref<string>('')

interface savingPlan {
    targetUUID: string,
    userUUID: string,
    targetAmount: number,
    target: string,
    recentlyAmount: number,
    createTime: Date,
    modifyTime: Date
}

interface savingProcess {
    processUUID: string,
    targetUUID: string,
    userUUID: string,
    saving: number,
    outPut: number,
    createTime: Date,
    modifyTime: Date
}

const defaultSavingPlanForm: Array<savingPlan | savingProcess> = [
    {
        targetUUID: '',
        userUUID: '',
        targetAmount: 0,
        target: '',
        recentlyAmount: 0,
        createTime: new Date(),
        modifyTime: new Date()
    }, {
        processUUID: '',
        targetUUID: '',
        userUUID: '',
        saving: 0,
        outPut: 0,
        createTime: new Date(),
        modifyTime: new Date()
    }
]

const form = ref()
const formModel: Ref<any> = ref({})
const rules = {
    target: [
        { required: true, message: '目标名不能为空', trigger: 'blur' },
        {
            min: 2,
            max: 20,
            message: '目标名长度为2-20位字符',
            trigger: ['blur', 'change']
        }
    ]
}

const open = (data: any, target: string, targetUUID: string | null) => {
    isVisible.value = true
    editTarget.value = target

    if (data.targetUUID) {
        formModel.value = { ...data }
    } else {
        if (editTarget.value === 'savingPlan') {
            formModel.value = { ...defaultSavingPlanForm[0] }
        } else if (editTarget.value === 'savingProcess') {
            formModel.value = { ...defaultSavingPlanForm[1] }
            if (targetUUID) {
                formModel.value.targetUUID = targetUUID
            }
        }
    }
}

const showTitle = () => {
    if (editTarget.value === 'savingPlan') {
        if (formModel.value.targetUUID) {
            return '修改储蓄目标'
        }
        return '添加新的储蓄目标'
    } else if (editTarget.value === 'savingProcess') {
        if (formModel.value.processUUID) {
            return '修改储蓄进度'
        }
        return '添加新的储蓄进度'
    }
}

const emit = defineEmits(['success'])

const onSubmit = async () => {
    await form.value.validate()
    if (editTarget.value === 'savingPlan') {
        if (formModel.value.targetUUID) {
            await modifySavingPlanService(formModel.value)
        } else {
            await addSavingPlanService(formModel.value)
        }
    } else if (editTarget.value === 'savingProcess') {
        if (formModel.value.processUUID) {
            await modifySavingProcessService(formModel.value)
        } else {
            await addSavingProcessService(formModel.value)
        }
    }
    ElNotification({
        title: 'Success',
        message: h('i', { style: 'color: teal' }, formModel.value.userUUID ? '修改成功！' : '添加成功!')
    })
    emit('success')
    isVisible.value = false
}

const cancelSubmit = () => {
    formModel.value = {}
    isVisible.value = false
}

defineExpose({
    open
})
</script>

<template>
    <el-dialog v-model="isVisible" width="400">
        <template #header>
            <el-text type="primary" size="large">{{ showTitle() }}</el-text>
            <hr>
        </template>

        <el-form
            :model="formModel"
            :rules="rules"
            ref="form"
            label-position="left"
            label-width="auto">
            <div v-if="editTarget === 'savingPlan'">
                <el-form-item prop="target" label="目标名:">
                    <el-input v-model="formModel.target" placeholder="请输入目标名!" style="width: 200px;"></el-input>
                </el-form-item>
                <el-form-item label="目标金额:">
                    <el-input-number
                        v-model="formModel.targetAmount"
                        :precision="2"
                        :step="1"
                        :min="0"
                        controls-position="right"
                        style="width: 200px;" />
                </el-form-item>
            </div>
            <div v-if="editTarget === 'savingProcess'">
                <el-form-item label="存入:">
                    <el-input-number
                        v-model="formModel.saving"
                        :precision="2"
                        :step="1"
                        :min="0"
                        controls-position="right"
                        style="width: 200px;" />
                </el-form-item>
                <el-form-item label="支出:">
                    <el-input-number
                        v-model="formModel.outPut"
                        :precision="2"
                        :step="1"
                        :min="0"
                        controls-position="right"
                        style="width: 200px;" />
                </el-form-item>
            </div>
        </el-form>
        <template #footer>
            <el-button type="primary" plain @click="onSubmit()">确认</el-button>
            <el-button type="danger" plain @click="cancelSubmit()">取消</el-button>
        </template>
    </el-dialog>
</template>

<style scoped lang="scss">

</style>