<script setup lang="ts">
import 'vue-cropper/dist/index.css'
import { VueCropper } from 'vue-cropper'
import { reactive, type Ref, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'

interface IStyle {
    width: number | string,
    height: number | string
}

interface IProps {
    type: string // 上传类型, 企业logo / 浏览器logo
    allowTypeList: string[] // 接收允许上传的图片类型
    limitSize: number // 限制大小
    fixedNumber: number[] // 截图框的宽高比例
    fixedNumberAider?: number[] // 侧边栏收起截图框的宽高比例
    previewWidth: number // 预览宽度
    title?: string // 裁剪标题
}


const dialogVisible: Ref<boolean> = ref(false)

const userAvatar = defineModel('userAvatar')
const cropper: Ref<any> = ref()
const cropperOption = ref({
    img: '',
    outputSize: 1,
    outputType: 'webp',
    mode: 'cover',
    canMove: true,
    centerBox: true,
    autoCrop: true,
    fixed: true,
    canScale: false,
    limitMinSize: 100
})

const previews: any = ref({})
const previewFileStyle = ref({})
const tempScale = ref<number>(0)

const onUploadFile = (uploadFile: any) => {
    const fileType = ['image/png', 'image/jpg', 'image/jpeg', 'image/webp']
    const rawFile: File = uploadFile.raw
    if (!fileType.includes(rawFile.type)) {
        ElMessage.error('文件必须是图片!')
        return false
    } else if (rawFile.size / 1024 / 1024 > 2) {
        ElMessage.error('上传图片不能超过2MB!')
        return false
    }
    cropperOption.value.img = URL.createObjectURL(rawFile)
}

const open = () => {
    dialogVisible.value = true
}

const submitAvatar = async () => {

    if (!cropperOption.value.img) {
        ElMessage.error('你还没有上传任何图片!')
        return false
    }

    await cropper.value.getCropBlob((data: any) => {
        userAvatar.value = new File([data], 'cropper.' + cropperOption.value.outputType, {
            type: data.type
        })
        cropperOption.value.img = ''
    })
    dialogVisible.value = false
}

const cancelCropper = () => {
    cropperOption.value.img = ''
    dialogVisible.value = false
}

defineExpose({
    open
})


// 裁剪之后的数据
const previewHandle = (data: any) => {
    previews.value = data // 预览img图片
    tempScale.value = props.previewWidth / data.w
    previewFileStyle.value = {
        width: data.w + 'px',
        height: data.h + 'px',
        margin: 0,
        overflow: 'hidden',
        zoom: tempScale.value,
        position: 'relative',
        border: '1px solid #e8e8e8',
        'border-radius': '2px'
    }
}

const getStyle = ref<IStyle>({
    width: '',
    height: ''
})

const props = reactive<any>({
    limitSize: 1,
    fixedNumber: 1,
    previewWidth: 228
})

watch(
    () => props,
    () => {
        /* 预览样式 */
        getStyle.value = {
            width: props.previewWidth + 'px', // 预览宽度
            height: props.previewWidth / props.fixedNumber[0] + 'px' // 预览高度
        }
    }, {
        deep: true
    }
)

</script>

<template>
    <el-dialog v-model="dialogVisible" title="修改头像" width="800">
        <el-row>
            <el-col class="displayAvatar">
                <div class="cropperBox">
                    <VueCropper
                        ref="cropper"
                        :img="cropperOption.img"
                        :outputSize="cropperOption.outputSize"
                        :outputType="cropperOption.outputType"
                        :mode="cropperOption.mode"
                        :canMove="cropperOption.canMove"
                        :centerBox="cropperOption.centerBox"
                        :autoCrop="cropperOption.autoCrop"
                        :fixed="cropperOption.fixed"
                        :canscale="cropperOption.canScale"
                        :limitMinSize="cropperOption.limitMinSize"
                        @realTime="previewHandle"
                    />
                </div>

                <div class="previewAvatar">
                    <div class="title">
                        预览:
                    </div>
                    <div
                        :style="getStyle"
                        class="previewImg"
                    >
                        <div :style="previewFileStyle">
                            <img
                                :style="previews.img"
                                :src="previews.url"
                                alt=""
                                style="max-width: initial"
                            >
                        </div>
                    </div>
                </div>

            </el-col>
        </el-row>

        <template #footer>
            <el-row>
                <el-col class="operation">
                    <el-upload
                        :on-change="onUploadFile"
                        :show-file-list="false"
                        :auto-upload="false"
                    >
                        <el-button type="primary" plain>上传图片</el-button>
                    </el-upload>

                    <div>
                        <el-button type="primary" plain @click="submitAvatar">确认</el-button>
                        <el-button type="danger" plain @click="cancelCropper">取消</el-button>
                    </div>

                </el-col>
            </el-row>
        </template>

    </el-dialog>
</template>

<style lang="scss" scoped>

.displayAvatar {
    display: flex;
    align-items: center;

    .cropperBox {
        width: 400px;
        height: 400px;
    }


    .previewAvatar {
        display: flex;
        flex-direction: column;
        margin-left: 75px;
        height: 100%;

        .title {
            font-family: Arial, sans-serif;
            font-size: large;
            margin-bottom: 50px;
        }

        .previewImg {
            min-width: 200px;
            min-height: 200px;
            overflow: hidden;
            border: black double 8px;
            border-radius: 50%;
            background-color: gray;
            zoom: 50%;
        }

    }

}

.operation {
    display: flex;
    justify-content: space-between;
}

</style>