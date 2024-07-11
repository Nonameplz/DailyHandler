<script setup lang="ts">
import type { Ref } from 'vue'
import { ref } from 'vue'

defineProps({
    displayPanelTitle: {
        required: true,
        type: String
    }
})

const isDisplay: Ref<boolean> = ref(true)
</script>

<template>
    <el-card class="display-panel" body-style="flex:auto" v-if="isDisplay">
        <template #header>
            <div class="header">
                <span>{{ displayPanelTitle }}</span>
                <div class="extra">
                    <slot name="extra"></slot>
                </div>
            </div>
        </template>
        <div class="displayBody">
            <slot></slot>
        </div>
    </el-card>
</template>

<style lang="scss" scoped>
.display-panel {
    height: 100%;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    justify-content: space-between;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .displayBody {
        height: 100%;
    }
}
</style>