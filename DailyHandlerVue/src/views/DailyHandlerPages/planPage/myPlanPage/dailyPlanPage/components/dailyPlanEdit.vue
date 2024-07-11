<script setup lang="ts">
import { type Ref, ref, h } from 'vue'
import { DocumentAdd, EditPen } from '@element-plus/icons-vue'
import LabelSelect from '@/components/labelSelect.vue'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { addDailyPlanService, modifyDailyPlanService } from '@/api/plan'
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
    endTime: new Date(),
    inuse: 0
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
const duringTime = ref<[Date, Date]>([
    new Date(),
    new Date()
])

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
    duringTime.value = [new Date(), new Date()]

    if (row.planUUID) {
        formModel.value = { ...row }
        formModel.value.startTime = timeParseToday(row.startTime)
        formModel.value.endTime = timeParseToday(row.endTime)

        duringTime.value = [new Date(JSON.parse(JSON.stringify(formModel.value.startTime))), new Date(JSON.parse(JSON.stringify(formModel.value.endTime)))]
    } else {
        formModel.value = JSON.parse(JSON.stringify(defaultModel))
        if (editorRef.value) {
            editorRef.value.setHTML('')
        }
    }

}

defineExpose({
    open
})

const emit = defineEmits(['success'])

const onSubmit = async (inuse: boolean) => {
    await form.value.validate()
    formModel.value.inuse = inuse ? 1 : 0

    formModel.value.startTime = dateToLocalTime(duringTime.value[0])
    formModel.value.endTime = dateToLocalTime(duringTime.value[1])

    if (!formModel.value.planUUID) {
        await addDailyPlanService(formModel.value)
    } else if (formModel.value.planUUID) {
        await modifyDailyPlanService(formModel.value)
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
                <div class="timePicker">
                    <el-time-picker
                        v-model="duringTime"
                        is-range
                        range-separator="To"
                        start-placeholder="Start time"
                        end-placeholder="End time"
                    />
                </div>
            </el-form-item>

        </el-form>

        <template #footer>
            <div class="footer">
                <div class="submit">
                    <el-button type="primary" plain @click="onSubmit(true)">
                        {{ formModel.planUUID ? '修改并启用' : '添加并启用'
                        }}
                    </el-button>
                    <el-button type="info" plain @click="onSubmit(false)">{{ formModel.planUUID ? '修改不启用' : '添加不启用'
                        }}
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

    .timePicker {
        .el-range-separator {
            box-sizing: content-box;
        }
    }

    .footer {
        display: flex;
        justify-content: space-between;
    }
}
</style>