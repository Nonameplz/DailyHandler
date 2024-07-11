<script setup lang="ts">
import { useUserStore } from '@/stores/modules/user'

const useUser = useUserStore()

</script>

<template>
    <el-card class="mainMenu">
        <el-tabs tab-position="right" class="tabs">
            <el-tab-pane label="首页">
                <div class="helloDisplayPane">
                    欢迎回来,{{ useUser.user.userNickName || useUser.user.userName }}
                </div>
            </el-tab-pane>
            <el-tab-pane label="Config">Config</el-tab-pane>
            <el-tab-pane label="Role">Role</el-tab-pane>
            <el-tab-pane label="Task">Task</el-tab-pane>
        </el-tabs>
    </el-card>
</template>

<style lang="scss" scoped>
.mainMenu {
    height: 100%;

    .tabs {
        height: 100%;
        :deep(.el-tabs__content){
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
            .helloDisplayPane{
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
