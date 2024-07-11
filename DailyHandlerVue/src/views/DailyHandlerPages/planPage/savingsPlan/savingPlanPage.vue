<script setup lang="ts">
import PageContainer from '@/components/pageContainer.vue'
import { onMounted, ref, h } from 'vue'
import { delSavingPlanService, delSavingProcessService, getUserSavingPlanService } from '@/api/plan'
import { timeParse } from '@/utils/timeParse'
import SavingPlanEdit from '@/views/DailyHandlerPages/planPage/savingsPlan/componments/savingPlanEdit.vue'
import { Edit, Delete, Warning } from '@element-plus/icons-vue'
import { ElNotification } from 'element-plus'

const savingPlanList = ref<Array<{
    targetUUID: string,
    userUUID: string,
    targetAmount: number,
    target: string,
    recentlyAmount: number,
    createTime: Date,
    modifyTime: Date,
    savingPlanProcess: Array<{
        processUUID: string,
        targetUUID: string,
        userUUID: string,
        saving: number,
        outPut: number,
        createTime: Date,
        modifyTime: Date
    }>
}>>([])

const savingPlanEditRef = ref<any>()

const getSavingPlan = async () => {
    const res = await getUserSavingPlanService()
    savingPlanList.value = res.data.savingPlan[0]
    mergeSavingPlan(res.data.savingProcess[0])
    getProgressPercent()
}

const updateSavingPlanList = async () => {
    await getSavingPlan()
}

const mergeSavingPlan = (data: Array<{
    processUUID: string,
    targetUUID: string,
    userUUID: string,
    saving: number,
    outPut: number,
    createTime: Date,
    modifyTime: Date
}>) => {
    savingPlanList.value.forEach(function(listItem, listIndex, listArray) {
        savingPlanList.value[listIndex].savingPlanProcess = []
        data.forEach(function(processItem, processIndex, processArray) {
            if (listItem.targetUUID === processItem.targetUUID) {
                savingPlanList.value[listIndex].savingPlanProcess.push(processItem)
            }
        })
    })
}

const savingProgress = ref<{
    wholeTargetAmount: number,
    wholeRecentlyAmount: number,
    progressPercent: number
}>({
    wholeTargetAmount: 0,
    wholeRecentlyAmount: 0,
    progressPercent: 0
})

const getProgressPercent = () => {
    for (let i = 0; i < savingPlanList.value.length; i++) {
        savingProgress.value.wholeTargetAmount += savingPlanList.value[i].targetAmount
        savingProgress.value.wholeRecentlyAmount += savingPlanList.value[i].recentlyAmount
    }
    savingProgress.value.progressPercent = Math.round((savingProgress.value.wholeRecentlyAmount / savingProgress.value.wholeTargetAmount) * 100)
}

onMounted(async () => {
    await getSavingPlan()
})

const indexMethod = (index: number) => {
    return index + 1
}

const addNewSavingPlan = () => {
    savingPlanEditRef.value.open({}, 'savingPlan')
}

const addNewSavingProcess = (targetUUID: string) => {
    savingPlanEditRef.value.open({}, 'savingProcess', targetUUID)
}

const EditSavingPlan = (row: any) => {
    savingPlanEditRef.value.open(row, 'savingPlan')
}

const EditSavingProcess = (row: any) => {
    savingPlanEditRef.value.open(row, 'savingProcess', row.targetUUID)
}

const delSavingPlan = async (targetUUID: string) => {
    await delSavingPlanService(targetUUID)
    await updateSavingPlanList()
    ElNotification({
        title: 'Success',
        message: h('i', { style: 'color: teal' }, '删除成功')
    })
}
const delSavingProcess = async (processUUID: string) => {
    await delSavingProcessService(processUUID)
    await updateSavingPlanList()
    ElNotification({
        title: 'Success',
        message: h('i', { style: 'color: teal' }, '删除成功')
    })
}
</script>

<template>
    <page-container title="储蓄计划">
        <div class="savingPage">
            <el-container>
                <el-header class="header">
                    <el-row>
                        <el-col>
                            <el-text type="info" size="large">当前储蓄进度:</el-text>
                        </el-col>
                    </el-row>
                    <el-progress
                        :percentage="savingProgress.progressPercent"
                        :stroke-width="15"
                        striped
                        striped-flow
                    />

                </el-header>
                <el-main class="main">
                    <el-row>
                        <el-col class="tabHeader">
                            <el-text type="primary" size="large">当前有
                                <el-text type="success" size="large">
                                    {{ savingPlanList.length }}
                                </el-text>
                                条目标
                            </el-text>
                            <el-button type="primary" plain @click="addNewSavingPlan()">添加新的目标</el-button>
                        </el-col>
                    </el-row>
                    <el-table
                        :data="savingPlanList"
                        border
                        stripe
                        max-height="1000">
                        <el-table-column
                            fixed
                            label="ID"
                            type="index"
                            :index="indexMethod"
                            width="50"></el-table-column>
                        <el-table-column prop="target" label="目标" width="100"></el-table-column>
                        <el-table-column prop="targetAmount" label="总目标金额" width="100"></el-table-column>
                        <el-table-column prop="recentlyAmount" label="目前金额" width="100"></el-table-column>
                        <el-table-column label="目标状态" width="100">
                            <template #default="props">
                                <el-text v-if="props.row.isDone === 0" type="primary">正在进行</el-text>
                                <el-text v-if="props.row.isDone === 1" type="success">已完成</el-text>
                            </template>
                        </el-table-column>
                        <el-table-column label="创建时间" width="200">
                            <template #default="props">
                                {{ timeParse(props.row.createTime) }}
                            </template>
                        </el-table-column>
                        <el-table-column></el-table-column>
                        <el-table-column label="操作" width="150" fixed="right">
                            <template #default="props">
                                <el-button
                                    type="primary"
                                    plain
                                    circle
                                    :icon="Edit"
                                    @click="EditSavingPlan(props.row)"></el-button>
                                <el-popconfirm
                                    confirm-button-text="Yes"
                                    cancel-button-text="No"
                                    :icon="Warning"
                                    icon-color="#626AEF"
                                    title="确认删除?这会一起将进度清空"
                                    @confirm="delSavingPlan(props.row.targetUUID)"
                                >
                                    <template #reference>
                                        <el-button
                                            type="danger"
                                            plain
                                            circle
                                            :icon="Delete"
                                        ></el-button>
                                    </template>
                                </el-popconfirm>
                            </template>
                        </el-table-column>
                        <el-table-column type="expand" fixed="right" width="50">
                            <template #default="props">
                                <el-row>
                                    <el-col class="tabHeader">
                                        <el-text type="primary" size="large">当前有
                                            <el-text type="success" size="large">
                                                {{ props.row.savingPlanProcess.length }}
                                            </el-text>
                                            条进度
                                        </el-text>
                                        <el-button
                                            type="primary"
                                            plain
                                            @click="addNewSavingProcess(props.row.targetUUID)">
                                            添加新的进度
                                        </el-button>
                                    </el-col>
                                </el-row>
                                <el-table :data="props.row.savingPlanProcess" border class="m-4">
                                    <el-table-column
                                        fixed
                                        label="ID"
                                        type="index"
                                        :index="indexMethod"
                                        width="50"></el-table-column>
                                    <el-table-column prop="saving" label="存入" width="100"></el-table-column>
                                    <el-table-column prop="outPut" label="支出" width="100"></el-table-column>
                                    <el-table-column label="创建时间" width="200">
                                        <template #default="props">
                                            {{ timeParse(props.row.createTime) }}
                                        </template>
                                    </el-table-column>
                                    <el-table-column></el-table-column>
                                    <el-table-column label="操作" width="150" fixed="right">
                                        <template #default="props">
                                            <el-button
                                                type="primary"
                                                plain
                                                circle
                                                :icon="Edit"
                                                @click="EditSavingProcess(props.row)"
                                            ></el-button>
                                            <el-popconfirm
                                                confirm-button-text="Yes"
                                                cancel-button-text="No"
                                                :icon="Warning"
                                                icon-color="#626AEF"
                                                title="确认删除?"
                                                @confirm="delSavingProcess(props.row.processUUID)"
                                            >
                                                <template #reference>
                                                    <el-button
                                                        type="danger"
                                                        plain
                                                        circle
                                                        :icon="Delete"
                                                    ></el-button>
                                                </template>

                                            </el-popconfirm>
                                        </template>
                                    </el-table-column>
                                    <template #empty>
                                        <el-empty description="无储蓄进度!"></el-empty>
                                    </template>
                                </el-table>
                            </template>
                        </el-table-column>
                        <template #empty>
                            <el-empty description="看起来你还没有自己的储蓄计划,快来制定一份你的储蓄计划吧!"></el-empty>
                        </template>
                    </el-table>
                </el-main>
            </el-container>
        </div>
    </page-container>
    <SavingPlanEdit ref="savingPlanEditRef" @success="updateSavingPlanList"></SavingPlanEdit>
</template>

<style scoped>
.savingPage {
    .header {
        height: 10vh;
    }

    .main {
        .tabHeader {
            margin-bottom: 1rem;
            padding-top: 1rem;
            padding-bottom: 1rem;
            border-top: black solid 1px;
            border-bottom: black solid 1px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
    }
}

</style>
