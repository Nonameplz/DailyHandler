<script setup lang="ts"></script>

<template>
    <el-card class="mainMenu">
        <el-tabs tab-position="right" class="tabs">
            <el-tab-pane label="User">User</el-tab-pane>
            <el-tab-pane label="Config">Config</el-tab-pane>
            <el-tab-pane label="Role">Role</el-tab-pane>
            <el-tab-pane label="Task">Task</el-tab-pane>
        </el-tabs>
    </el-card>
</template>

<style scoped>
.mainMenu{
    height: 100%;

    .el-tabs {
        height: 100%;
    }
}
:deep(.el-card__body) {
    height: 100%;
    padding: 10px;
}
</style>
