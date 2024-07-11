<script setup lang="ts">
import avatar from '@/assets/img/defaultAvatar.png'
import { useTokenStore } from '@/stores'
import { useUserStore } from '@/stores/modules/user'
import {
    ArrowDown,
    HomeFilled,
    List,
    PieChart,
    Sunny,
    Calendar,
    Collection,
    Money
} from '@element-plus/icons-vue'
import { ref, type Ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const useUser = useUserStore()
const useToken = useTokenStore()
const isCollapse: Ref<boolean> = ref(true)
const router = useRouter()

onMounted(() => {
    useUser.getUser()
})

const onCloseSlide = () => {
    setTimeout(() => {
        isCollapse.value = true
    }, 500)
}

const handleCommand = (command: string | number | object) => {
    if (command === 'logout') {
        ElMessageBox.confirm('确认退出?', 'Warning', {
            confirmButtonText: 'Yes',
            cancelButtonText: 'No',
            type: 'warning'
        }).then(() => {
            useToken.delToken()
            useUser.setUser({})
            router.push('/login')
            ElMessage({
                type: 'success',
                message: '成功退出!'
            })
        })
    } else {
        router.push(`/DailyHandler/${command}`)
    }
}
</script>
<template>
    <div class="layout-manage">
        <el-container>
            <el-header class="header">
                <div class="logo">DH</div>
                <div class="userMenu">
                    <el-dropdown @command="handleCommand">
                        <span class="el-dropdown-link">
                            <span class="mr-4"
                            >欢迎回来 :
                                {{
                                    useUser.user.userNickName ||
                                    useUser.user.userName
                                }}</span
                            >
                            <el-avatar
                                :size="50"
                                fit="fill"
                                :src="useUser.user.userAvatar || avatar"
                            />
                            <el-icon class="el-icon--right">
                                <arrow-down />
                            </el-icon>
                        </span>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item command="userInfo">
                                    &bigstar; 个人信息
                                </el-dropdown-item>
                                <el-dropdown-item command="rePassword"
                                >&bigstar; 重置密码
                                </el-dropdown-item>
                                <el-dropdown-item command="personalConfig">
                                    &bigstar; 系统设置
                                </el-dropdown-item>
                                <el-dropdown-item
                                    divided
                                    style="color: red"
                                    command="logout"
                                >
                                    &bigstar; 退出登录
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </el-header>
            <el-container class="layout_body">
                <el-aside
                    class="aside"
                    @click="isCollapse=false"
                    @mouseleave="onCloseSlide"
                >
                    <el-row class="mt-6">
                        <el-col>
                            <el-menu
                                :default-active="$route.path"
                                :collapse="isCollapse"
                                class="el-menu-vertical"
                                router
                            >
                                <el-menu-item index="/DailyHandler/mainMenu">
                                    <el-icon>
                                        <HomeFilled />
                                    </el-icon>
                                    <span>首页</span></el-menu-item
                                >
                                <el-sub-menu index="/DailyHandler/myPlain">
                                    <template #title>
                                        <el-icon>
                                            <List />
                                        </el-icon>
                                        <span>我的计划</span>
                                    </template>
                                    <el-menu-item
                                        index="/DailyHandler/myPlan/dailyPlan"
                                    >
                                        <el-icon>
                                            <Sunny />
                                        </el-icon>
                                        <span>每日计划</span>
                                    </el-menu-item>
                                    <el-menu-item
                                        index="/DailyHandler/myPlan/longTermPlan"
                                    >
                                        <el-icon>
                                            <Calendar />
                                        </el-icon>
                                        <span>长期计划</span>
                                    </el-menu-item>
                                    <el-menu-item
                                        index="/DailyHandler/myPlan/planStats"
                                    >
                                        <el-icon>
                                            <Collection />
                                        </el-icon>
                                        <span>计划统计</span>
                                    </el-menu-item>
                                </el-sub-menu>
                                <el-menu-item index="/DailyHandler/planReview">
                                    <el-icon>
                                        <PieChart />
                                    </el-icon>
                                    <span>计划回顾</span>
                                </el-menu-item>
                                <el-menu-item index="/DailyHandler/savingPlan">
                                    <el-icon>
                                        <Money />
                                    </el-icon>
                                    <span>储蓄计划</span>
                                </el-menu-item>
                            </el-menu>
                        </el-col>
                    </el-row>
                </el-aside>
                <el-container>
                    <el-main class="main">
                        <router-view></router-view>
                    </el-main>
                    <el-footer class="footer"
                    >Created by &boxV;
                        <p class="opacity-15">沈鑫海</p>
                        &boxV;李浩然
                    </el-footer>
                </el-container>
            </el-container>
        </el-container>
    </div>
</template>

<style lang="scss" scoped>
.layout-manage {
    height: 100vh;
    background: rgba(198, 195, 195, 0.7);

    .header {
        height: 8vh;
        background-color: rgba(255, 255, 255, 0.6);
        padding-left: 0;
        display: flex;
        align-items: center;
        position: relative;
        border-bottom: black solid 1px;

        .logo {
            height: 100%;
            width: 18vh;
            font-size: 0;
            background: url('@/assets/img/Logo2.svg') no-repeat center;
            background-size: cover;
            position: absolute;
            left: -2vh;
        }

        .userMenu {
            position: absolute;
            display: flex;
            right: 3vh;

            .el-dropdown-link {
                display: flex;
                justify-content: center;
                align-items: center;

                .el-icon--right {
                    margin-right: 3px;
                    rotate: 180deg;
                    transition: rotate 0.5s;
                }
            }

            .el-dropdown-link:hover {
                .el-icon--right {
                    rotate: 0deg;
                }
            }
        }
    }

    .layout_body {
        height: 92vh;
    }

    .aside {
        height: inherit;
        width: fit-content;
        background-color: rgba(255, 255, 255, 0.6);
        border-right: black solid 1px;

        .el-menu-vertical:not(.el-menu--collapse) {
            width: 200px;
        }
    }

    .main {
        padding: 4px;
    }

    .footer {
        height: 5vh;
        background-color: rgba(235, 235, 235, 0.6);
        border-top: black solid 1px;
        display: flex;
        align-items: center;
        justify-content: center;
    }
}

:global(.grecaptcha-badge) {
    visibility: hidden;
}
</style>
