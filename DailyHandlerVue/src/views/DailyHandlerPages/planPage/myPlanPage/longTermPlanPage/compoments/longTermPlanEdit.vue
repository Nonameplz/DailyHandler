<script setup lang="ts">
import { type Ref, ref, h } from 'vue'
import { DocumentAdd, EditPen } from '@element-plus/icons-vue'
import LabelSelect from '@/components/labelSelect.vue'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { addLongTermPlanService, modifyLongTermPlanService } from '@/api/plan'
import { dateToLocalTime, timeParseToday } from '@/utils/timeParse'
import { ElNotification } from 'element-plus'

const visibleDrawer: Ref<boolean> = ref(false)

const defaultModel = {
    planUUID: '',
    planLevel: 0,
    planLabels: { mainLabel: '', labels: [] },
    planTitle: '',
    planDescription: '',
    planStatus: '',
    planLocation: '',
    startTime: new Date(),
    endTime: new Date()
}

const form: Ref<any> = ref()
const formModel: Ref<any> = ref({})
const rules = {
    planTitle: [
        { required: true, message: '请输入计划标题', trigger: 'blur' },
        {
            min: 2,
            max: 50,
            message: '标题长度为2-50字',
            trigger: ['blur', 'change']
        }
    ]
}
const editorRef = ref()

const dateValue = ref([new Date(), new Date()])
const shortcuts = [
    {
        text: 'Last week',
        value: () => {
            const end = new Date()
            const start = new Date()
            start.setDate(start.getDate() - 7)
            return [start, end]
        }
    },
    {
        text: 'Last month',
        value: () => {
            const end = new Date()
            const start = new Date()
            start.setMonth(start.getMonth() - 1)
            return [start, end]
        }
    },
    {
        text: 'Last 3 months',
        value: () => {
            const end = new Date()
            const start = new Date()
            start.setMonth(start.getMonth() - 3)
            return [start, end]
        }
    }
]

const level: Array<{ value: number, label: string }> = [
    {
        value: 0,
        label: '无优先级'
    },
    {
        value: 1,
        label: '低优先级'
    },
    {
        value: 2,
        label: '中优先级'
    },
    {
        value: 3,
        label: '高优先级'
    }
]

const open = (row: any) => {
    formModel.value = {}
    visibleDrawer.value = true

    if (row.planUUID) {
        formModel.value = { ...row }
        formModel.value.startTime = new Date(row.startTime)
        formModel.value.endTime = new Date(row.endTime)

        dateValue.value = [
            new Date(JSON.parse(JSON.stringify(formModel.value.startTime))),
            new Date(JSON.parse(JSON.stringify(formModel.value.endTime)))
        ]

    } else {
        formModel.value = JSON.parse(JSON.stringify(defaultModel))
        if (editorRef.value) {
            editorRef.value.setHTML('')
        }
        dateValue.value = [new Date(), new Date()]
    }

}

defineExpose({
    open
})

const emit = defineEmits(['success'])

const onSubmit = async () => {
    await form.value.validate()

    dateValue.value[0].setHours(dateValue.value[0].getHours() + 8)
    dateValue.value[1].setHours(dateValue.value[1].getHours() + 8)

    formModel.value.startTime = dateValue.value[0].toISOString()
    formModel.value.endTime = dateValue.value[1].toISOString()

    if (!formModel.value.planUUID) {
        await addLongTermPlanService(formModel.value)
    } else if (formModel.value.planUUID) {
        await modifyLongTermPlanService(formModel.value)
    }

    visibleDrawer.value = false
    emit('success')
    ElNotification({
        title: 'Success',
        message: h('i', { style: 'color: teal' }, formModel.value.planUUID ? '修改成功' : '添加成功!'),
        type: 'success'
    })

}

const resetForm = () => {
    formModel.value = JSON.parse(JSON.stringify(defaultModel))
    editorRef.value.setHTML('')
}

</script>

<template>
    <el-drawer
        v-model="visibleDrawer"
        direction="rtl"
        size="50%"
        class="drawer"
    >
        <template #header="{ titleId, titleClass }">
            <div class="title">
                <el-icon v-if="!formModel.planUUID" class="beforeTitleIcon">
                    <DocumentAdd />
                </el-icon>
                <el-icon v-if="formModel.planUUID" class="beforeTitleIcon">
                    <EditPen />
                </el-icon>
                <h4 :id="titleId" :class="titleClass">{{ formModel.planUUID ? '编辑计划' : '添加计划' }}</h4>
            </div>
        </template>
        <el-form :model="formModel" :rules="rules" ref="form" label-position="left" label-width="150px">
            <el-form-item label="计划标题:" label-width="125" prop="planTitle">
                <el-input
                    v-model="formModel.planTitle"
                    style="width: 240px"
                    placeholder="请输入计划标题"
                    clearable
                />
            </el-form-item>
            <el-form-item label="任务优先级:" label-width="125">
                <el-select
                    v-model="formModel.planLevel"
                    placeholder="请选择计划优先级"
                    size="large"
                    style="width: 240px"
                    default-first-option
                >
                    <el-option
                        v-for="item in level"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="计划标签:" label-width="125">
                <label-select v-model:mainLabel="formModel.planLabels.mainLabel"
                              v-model:labels="formModel.planLabels.labels"></label-select>
            </el-form-item>

            <el-form-item label="计划详情:" label-width="125">
                <div class="editor">
                    <quill-editor
                        ref="editorRef"
                        theme="snow"
                        v-model:content="formModel.planDescription"
                        contentType="html"
                    >
                    </quill-editor>
                </div>
            </el-form-item>

            <el-form-item label="计划地点:" label-width="125">
                <el-input
                    v-model="formModel.planLocation"
                    style="width: 240px"
                    placeholder="请输入计划地点"
                    clearable></el-input>
            </el-form-item>

            <el-form-item label="计划时间:" label-width="125">
                <div class="datePicker">
                    <el-date-picker
                        v-model="dateValue"
                        type="datetimerange"
                        :shortcuts="shortcuts"
                        range-separator="To"
                        start-placeholder="Start date"
                        end-placeholder="End date"
                    />
                </div>
            </el-form-item>

        </el-form>

        <template #footer>
            <div class="footer">
                <div class="submit">
                    <el-button type="primary" plain @click="onSubmit()">
                        {{ formModel.planUUID ? '修改计划' : '添加计划' }}
                    </el-button>
                </div>
                <el-button type="danger" plain @click="resetForm">重置</el-button>
            </div>

        </template>
    </el-drawer>
</template>

<style lang="scss" scoped>
.drawer {
    .title {
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: black solid 1px;

        .beforeTitleIcon {
            margin-right: 5px;
        }

    }

    .editor {
        width: 100%;

        :deep(.ql-editor) {
            min-height: 200px;
        }
    }

    .datePicker {

    }

    .footer {
        display: flex;
        justify-content: space-between;
    }
}
</style>