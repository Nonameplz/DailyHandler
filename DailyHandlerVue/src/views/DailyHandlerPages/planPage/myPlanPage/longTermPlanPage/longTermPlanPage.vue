<script setup lang="ts">
import DisplayPanel from '@/components/displayPanel.vue'
import { onMounted, type Ref, ref } from 'vue'
import PageContainer from '@/components/pageContainer.vue'
import {
    deleteLongTermPlanService,
    getPlanLabelsService,
    getUserLongTermPlanService, longTermPlanDoneService
} from '@/api/plan'
import { ArrowLeftBold, Delete, Edit, QuestionFilled, Check } from '@element-plus/icons-vue'
import { timeParse } from '@/utils/timeParse'
import LongTermPlanEdit
    from '@/views/DailyHandlerPages/planPage/myPlanPage/longTermPlanPage/compoments/longTermPlanEdit.vue'
import { ElNotification } from 'element-plus'


const isDisplay: Ref<boolean> = ref(true)
const longTermPlanList: Ref<any> = ref({})
const longTermPlanListLess: Ref<Array<{
    planUUID: string,
    planTitle: string,
    planStatus: number
}>> = ref([])
const longTermPlanEditRef = ref()

const labelList: Ref<any> = ref()

const getLabels = async () => {
    const res = await getPlanLabelsService()
    labelList.value = res.data
    labelFilter()
}

const parseToName = (value: string | string[]) => {
    if (value && typeof value === 'string') {
        const index: any = labelList.value.find((v: any) => v.labelValue === value)
        return index.labelName
    }
    if (value && value.length > 0) {
        let labelsNameList = []
        for (const item of value) {
            const index: any = labelList.value.find((v: any) => v.labelValue === item)
            labelsNameList.push(index.labelName)
        }
        return labelsNameList.toString()
    }
}

const changeDisplay = () => {
    isDisplay.value = !isDisplay.value
}

const getLongTermPlanList = async () => {
    const res = await getUserLongTermPlanService()
    longTermPlanList.value = res.data

    for (let i = 0; i < longTermPlanList.value.length; i++) {
        longTermPlanListLess.value.push({
            planUUID: longTermPlanList.value[i].planUUID,
            planTitle: longTermPlanList.value[i].planTitle,
            planStatus: longTermPlanList.value[i].planStatus
        })
    }
}

onMounted(async () => {
    await getLongTermPlanList()
    await getLabels()
})

const indexMethod = (index: number) => {
    return index + 1
}

const onAddDailyPlan = () => {
    longTermPlanEditRef.value.open({})
}
const onEditDailyPlan = (row: any) => {
    longTermPlanEditRef.value.open(row)
}

const updateList = async () => {
    await getLongTermPlanList()
}

const deleteLongTermPlan = async (planUUID: string) => {
    await deleteLongTermPlanService(planUUID)
    ElNotification({
        title: 'Success',
        message: '删除成功!',
        type: 'success'
    })
    await getLongTermPlanList()
}

const planLevelColor = (
    {
        row,
        rowIndex
    }: {
        row: any
        rowIndex: number
    }
) => {
    if (row.planLevel === 1) {
        return 'info-row'
    } else if (row.planLevel === 2) {
        return 'warning-row'
    } else if (row.planLevel === 3) {
        return 'danger-row'
    }
    return ''
}

const labelFilterList = ref<Array<{
    text: string,
    value: string
}>>([])

const labelFilter = () => {
    for (let i = 0; i < labelList.value.length; i++) {
        labelFilterList.value[i] = {
            text: labelList.value[i].labelName,
            value: labelList.value[i].labelValue
        }
    }
}


const sortStatus = (a, b) => {
    return a.planStatus > b.planStatus ? -1 : 1
}

const filterLabel = (value: string, row: any) => {
    return row.planLabels.mainLabel === value
}

const filterStatus = (value: number, row: any) => {
    return row.planStatus === value
}

const LongTermPlanDone = (row: any) => {
    row.planStatus = 1
    longTermPlanDoneService(row.planUUID)
}
</script>

<template>
    <display-panel display-panel-title="长期计划" v-if="isDisplay">
        <template #extra>
            <el-button type="primary" plain @click="changeDisplay">修改我的计划</el-button>
        </template>
        <div class="displayBox">
            <el-container>
                <el-aside class="aside">
                    <el-table :data="longTermPlanListLess" border max-height="500">
                        <el-table-column fixed label="ID" type="index" :index="indexMethod"
                                         width="50"></el-table-column>
                        <el-table-column prop="planTitle" label="计划名" show-overflow-tooltip
                                         width="100"></el-table-column>
                        <el-table-column
                            label="计划状态"
                            :filters="
                            [
                            {text:'已完成',value:1},
                            {text:'未完成',value:0},
                            ]"
                            :filter-method="filterStatus"
                            sortable
                            :sort-method="sortStatus"
                            width="100">
                            <template #default="props">
                                <span v-if="props.row.planStatus === 1" class="text-blue-300">已完成</span>
                                <span v-if="props.row.planStatus === 0" class="text-red-300">未完成</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" width="200">
                            <template #default="props">
                                <el-popconfirm
                                    v-if="props.row.planStatus === 0"
                                    confirm-button-text="Yes"
                                    cancel-button-text="No"
                                    :icon="QuestionFilled"
                                    icon-color="#626AEF"
                                    title="确认完成?"
                                    @confirm="LongTermPlanDone(props.row)"
                                >
                                    <template #reference>
                                        <el-link :icon="QuestionFilled">| 我已完成</el-link>
                                    </template>
                                </el-popconfirm>

                                <div v-if="props.row.planStatus === 1">
                                    <el-icon>
                                        <Check />
                                    </el-icon>
                                </div>
                            </template>
                        </el-table-column>
                        <template #empty>
                            <el-empty description="看起来你还没有自己的计划,快来制定一份你的计划吧!" />
                        </template>
                    </el-table>
                </el-aside>
                <el-main>这是统计图</el-main>
            </el-container>
        </div>
    </display-panel>

    <page-container title="修改长期计划" v-if="!isDisplay">
        <template #beforeTitle>
            <el-link @click.native="changeDisplay">
                <el-icon>
                    <ArrowLeftBold />
                </el-icon>
            </el-link>
        </template>
        <template #extra>
            <el-link @click="isDisplay =!isDisplay"></el-link>
            <el-button type="primary" plain>
                <el-icon>
                    <Edit />
                </el-icon>
                <span class="ml-1" @click="onAddDailyPlan">添加计划</span>
            </el-button>
        </template>

        <el-table :data="longTermPlanList" :row-class-name="planLevelColor" border max-height="500">
            <el-table-column fixed label="ID" type="index" :index="indexMethod" width="50"></el-table-column>
            <el-table-column prop="planTitle" label="计划名" width="200"></el-table-column>
            <el-table-column label="计划标签" width="350">
                <el-table-column
                    label="主标签"
                    :filters="labelFilterList"
                    :filter-method="filterLabel"
                    width="150">
                    <template #default="props">
                        {{ parseToName(props.row.planLabels.mainLabel) }}
                    </template>
                </el-table-column>
                <el-table-column label="次标签" width="200" show-overflow-tooltip>
                    <template #default="props">
                        {{ parseToName(props.row.planLabels.labels) }}
                    </template>
                </el-table-column>
            </el-table-column>
            <el-table-column
                label="计划描述"
                width="200"
                show-overflow-tooltip>
                <template #default="props">
                    <div v-html="props.row.planDescription"></div>
                </template>
            </el-table-column>
            <el-table-column
                label="计划状态"
                :filters="
                [
                {text:'已完成',value:1},
                {text:'未完成',value:0},
                ]"
                :filter-method="filterStatus"
                sortable
                :sort-method="sortStatus"
                width="200">
                <template #default="props">
                    <span v-if="props.row.planStatus === 1" class="text-blue-300">已完成</span>
                    <span v-if="props.row.planStatus === 0" class="text-red-300">未完成</span>
                </template>
            </el-table-column>
            <el-table-column prop="planLocation" label="计划地点" width="200"></el-table-column>
            <el-table-column label="起始时间" width="200">
                <template #default="props">
                    <span>{{ timeParse(props.row.startTime) }}</span>
                </template>
            </el-table-column>
            <el-table-column label="结束时间" width="200">
                <template #default="props">
                    <span>{{ timeParse(props.row.endTime) }}</span>
                </template>
            </el-table-column>
            <el-table-column label="计划创建时间" width="200">
                <template #default="props">
                    <span>{{ timeParse(props.row.createTime) }}</span>
                </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="150">
                <template #default="props">
                    <el-button type="primary" :icon="Edit" circle plain @click="onEditDailyPlan(props.row)" />
                    <el-popconfirm
                        confirm-button-text="是的"
                        cancel-button-text="我再考虑一下"
                        :icon="Delete"
                        width="220"
                        icon-color="#626AEF"
                        title="你确定要删除吗?"
                        @confirm="deleteLongTermPlan(props.row.planUUID)"
                    >
                        <template #reference>
                            <el-button type="danger" :icon="Delete" circle plain />
                        </template>
                    </el-popconfirm>

                </template>
            </el-table-column>

            <template #empty>
                <el-empty description="看起来你还没有自己的计划,快来制定一份你的计划吧!" />
            </template>
        </el-table>

    </page-container>

    <LongTermPlanEdit ref="longTermPlanEditRef" @success="updateList"></LongTermPlanEdit>

</template>

<style lang="scss" scoped>
.display-panel {
    height: 73vh;

    .displayBody {
        .displayBox {
            height: 100%;

            .aside {
                width: 45vh;
            }
        }
    }
}

.page-container {
    height: 73vh;
}

:deep(.el-table) {
    .warning-row {
        --el-table-tr-bg-color: var(--el-color-warning-light-9);
    }

    .info-row {
        --el-table-tr-bg-color: var(--el-color-info-light-9);
    }

    .danger-row {
        --el-table-tr-bg-color: var(--el-color-danger-light-9);
    }
}

</style>
