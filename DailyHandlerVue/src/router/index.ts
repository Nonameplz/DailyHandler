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
                            '@/views/DailyHandlerPages/dailyPlanPage/myPlanPage/myPlanPage.vue'
                        ),
                    children: [
                        {
                            path: '/DailyHandler/myPlan/dailyPlan',
                            component: () =>
                                import(
                                    '@/views/DailyHandlerPages/dailyPlanPage/myPlanPage/dailyPlanPage.vue'
                                )
                        },
                        {
                            path: '/DailyHandler/myPlan/longTermPlan',
                            component: () =>
                                import(
                                    '@/views/DailyHandlerPages/dailyPlanPage/myPlanPage/longTermPlanPage.vue'
                                )
                        },
                        {
                            path: '/DailyHandler/myPlan/planStats',
                            component: () =>
                                import(
                                    '@/views/DailyHandlerPages/dailyPlanPage/myPlanPage/planStatsPage.vue'
                                )
                        }
                    ]
                },
                {
                    path: '/DailyHandler/planReview',
                    component: () =>
                        import(
                            '@/views/DailyHandlerPages/dailyPlanPage/planReviewPage/planReviewPage.vue'
                        )
                },
                {
                    path: '/DailyHandler/savingPlan',
                    component: () =>
                        import(
                            '@/views/DailyHandlerPages/dailyPlanPage/savingsPlan/savingPlanPage.vue'
                        )
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
