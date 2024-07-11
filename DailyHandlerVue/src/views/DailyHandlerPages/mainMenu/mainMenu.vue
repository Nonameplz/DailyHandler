<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/modules/user'
import Announcement from '@/views/DailyHandlerPages/mainMenu/components/announcement.vue'

const useUser = useUserStore()
const announcementRef = ref<any>()

onMounted(() => {
    // useUser.setSeeAnnouncement(false)
})

const openAnnouncement = () => {
    announcementRef.value.open()
}

</script>

<template>
    <el-card class="mainMenu">
        <el-tabs tab-position="right" class="tabs">
            <el-tab-pane label="首页">
                <div class="helloDisplayPane">
                    &starf; 欢迎回来,{{ useUser.user.userNickName || useUser.user.userName }} &starf;
                </div>
            </el-tab-pane>
            <el-tab-pane label="每日计划">敬请期待</el-tab-pane>
            <el-tab-pane label="长期计划">敬请期待</el-tab-pane>
            <el-tab-pane label="储蓄计划">敬请期待</el-tab-pane>
        </el-tabs>
    </el-card>
    <announcement ref="announcementRef"></announcement>
</template>

<style lang="scss" scoped>
.mainMenu {
    height: 100%;

    .tabs {
        height: 100%;

        :deep(.el-tabs__content) {
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;

            .helloDisplayPane {
                width: fit-content;
            }
        }
    }
}

:deep(.el-card__body) {
    height: 100%;
    padding: 10px;
}
</style>
