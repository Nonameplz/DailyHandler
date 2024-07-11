<script setup lang="ts">
import { ref,onMounted } from 'vue'
import { ChatLineSquare } from '@element-plus/icons-vue'
import {useUserStore} from '@/stores/modules/user'

const isVisible = ref<boolean>(false)
const useUser = useUserStore()

onMounted(()=>{
    if(!useUser.seeAnnouncement){
        isVisible.value = true
    }
})

const open = () => {
    isVisible.value = true
}

const closeAnnouncement = (status:string) => {
    if (status === 'notAgain') {
        useUser.setSeeAnnouncement(true)
    }
    isVisible.value = false
}

defineExpose({
    open
})
</script>

<template>
    <el-dialog v-model="isVisible">
        <template #header>
            <div  class="header">
                <el-text type="info">
                    <el-icon>
                        <ChatLineSquare />
                    </el-icon>
                    <span class="ml-1">公告</span>
                </el-text>
            </div>
        </template>

        <div class="textContent">
            <el-text size="large" class="textBox">
                亲爱的用户们，<br>

                我们很高兴向大家介绍我们的全新计划管理平台！这个平台旨在帮助您更高效地组织和管理个人及团队的任务和项目。无论是日常任务的安排，还是复杂项目的管理，我们的平台都为您提供了强大的工具和功能。<br>

                主要特点包括：<br>

                任务列表和日历视图：清晰地列出所有任务，同时可以在日历视图中查看任务的时间安排和截止日期。<br>

                储蓄计划：轻松地进行储蓄计划，安排自己储蓄任务，清晰查看储蓄进度。<br>

                个性化设置：根据个人喜好和工作习惯，定制化界面和提醒设置，确保您能够高效地工作和生活。<br>

                如果想了解更多计划规划，请到扇贝学习
                <el-link href="https://web.shanbay.com/web/ask/answers/frjxe" type="primary">》》点我跳转《《</el-link>
                <br>

                我们诚挚地邀请您体验我们的平台，并分享您的反馈和建议。您的意见对我们非常重要，将帮助我们不断改进和完善服务。<br>

                谢谢您的支持！<br>

                最诚挚的问候。<br>
            </el-text>
        </div>

        <template #footer>
            <el-button @click="closeAnnouncement('notAgain')" type="danger" plain>不再显示</el-button>
            <el-button @click="closeAnnouncement('')" type="primary" plain>关闭公告</el-button>
        </template>

    </el-dialog>
</template>

<style scoped lang="scss">
    .header{
        padding-bottom: 1rem;
        border-bottom: black solid 1px;
        margin-bottom: 0.5rem;
    }
    .textContent{
        margin: 0 1rem;
        .textBox{
            line-height: 2.5rem;
        }
    }
</style>