<script setup lang="ts">

import { getPlanLabelsService } from '@/api/plan'
import { type ModelRef, ref, type Ref } from 'vue'

const labelList: Ref<any> = ref()
const disableLabelID: Ref<number> = ref(-1)

const getLabels = async () => {
    const res = await getPlanLabelsService()
    labelList.value = res.data
}
getLabels()

const mainLabel: ModelRef<string | undefined, string> = defineModel('mainLabel')
const labels: ModelRef<string[] | undefined, string> = defineModel('labels')

const disableMainLabel = (labelID: number) => {
    if (disableLabelID.value !== -1) {
        labelList.value[disableLabelID.value].disabled = false
    }
    disableLabelID.value = labelID - 1
    if (labels.value !== undefined) {
        labels.value = labels.value.filter(item => item !== labelList.value[disableLabelID.value].labelValue)
    }
    labelList.value[disableLabelID.value].disabled = true
}

</script>

<template>
    <div class="select">
        <div class="mainLabelSelect">
            <span>主标签:</span>
            <el-select
                v-model="mainLabel"
                placeholder="请选择主标签"
                filterable
                class="mainLabel"
            >
                <el-option
                    v-for="item in labelList"
                    :key="item.labelValue"
                    :label="item.labelName"
                    :value="item.labelValue"
                    @click="disableMainLabel(item.id)"
                />
            </el-select>
        </div>
        <div class="LabelSelect">
            <span>次标签:</span>
            <el-select
                v-model="labels"
                multiple
                collapse-tags
                collapse-tags-tooltip
                clearable
                :max-collapse-tags="3"
                placeholder="请选择次标签"
                class="labelSelect"
            >
                <el-option
                    v-for="item in labelList"
                    :key="item.labelValue"
                    :label="item.labelName"
                    :value="item.labelValue"
                    :disabled="item.disabled"
                />
            </el-select>
        </div>
    </div>
</template>

<style lang="scss" scoped>
.select {
    display: flex;
    flex-direction: column;
    width: 100%;

    .mainLabelSelect {
        display: flex;
        margin-bottom: 10px;

        .mainLabel {
            width: 240px;
        }
    }

    .LabelSelect {
        display: flex;

        .labelSelect {
            width: 240px;
        }
    }

}
</style>