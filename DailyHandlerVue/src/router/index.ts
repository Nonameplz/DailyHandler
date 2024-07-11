import { createRouter, createWebHistory } from 'vue-router'
import { useTokenStore } from '@/stores'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/login',
            component: () => import('@/views/login/loginPage.vue')
        },
        {
            path: '/',
            component: () =>
                import(
                    '@/views/DailyHandlerPages/layoutManage/layoutManage.vue'
                    ),
            redirect: '/DailyHandler/mainMenu',
            children: [
                {
                    path: '/DailyHandler/mainMenu',
                    component: () =>
                        import(
                            '@/views/DailyHandlerPages/mainMenu/mainMenu.vue'
                            )
                },
                {
                    path: '/DailyHandler/myPlan',
                    component: () =>
                        import(
                            '@/views/DailyHandlerPages/planPage/myPlanPage/myPlanPage.vue'
                            ),
                    children: [
                        {
                            path: '/DailyHandler/myPlan/dailyPlan',
                            component: () =>
                                import(
                                    '@/views/DailyHandlerPages/planPage/myPlanPage/dailyPlanPage/dailyPlanPage.vue'
                                    )
                        },
                        {
                            path: '/DailyHandler/myPlan/longTermPlan',
                            component: () =>
                                import(
                                    '@/views/DailyHandlerPages/planPage/myPlanPage/longTermPlanPage/longTermPlanPage.vue'
                                    )
                        },
                        {
                            path: '/DailyHandler/myPlan/planStats',
                            component: () =>
                                import(
                                    '@/views/DailyHandlerPages/planPage/myPlanPage/planStatsPage.vue'
                                    )
                        }
                    ]
                },
                {
                    path: '/DailyHandler/planReview',
                    component: () =>
                        import(
                            '@/views/DailyHandlerPages/planPage/planReviewPage/planReviewPage.vue'
                            )
                },
                {
                    path: '/DailyHandler/savingPlan',
                    component: () =>
                        import(
                            '@/views/DailyHandlerPages/planPage/savingsPlan/savingPlanPage.vue'
                            )
                },
                {
                    path: '/DailyHandler/userInfo',
                    component: () => import('@/views/DailyHandlerPages/user/userInfoPage.vue')
                },
                {
                    path: '/DailyHandler/rePassword',
                    component: () => import('@/views/DailyHandlerPages/user/rePasswordPage.vue')
                },
                {
                    path: '/DailyHandler/personalConfig',
                    component: () => import('@/views/DailyHandlerPages/user/personalConfigPage.vue')
                }
            ]
        },
        {
            path: '/test',
            component: () => import('@/views/testPage.vue')
        },
        {
            path: '/:pathMatch(.*)*',
            redirect: '/'
        }
    ]
})

router.beforeEach((to: any) => {
    const useToken = useTokenStore()
    if (!useToken.token && to.path !== '/login') {
        return '/login'
    }
})

export default router
