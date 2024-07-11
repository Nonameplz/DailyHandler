import request from '@/utils/request'

export const getPlanLabelsService = () => request.get('/plan/labels')

//这部分是每日计划相关的请求
export const getUserDailyPlanService = () => request.get('/plan/dailyPlan')

export const addDailyPlanService = (dailyPlanData: any) =>
    request.post('/plan/addDailyPlan', dailyPlanData)


export const modifyDailyPlanService = (dailyPlanData: any) =>
    request.post('/plan/modifyDailyPlan', dailyPlanData)

export const deleteDailyPlanService = (dailyPlanUUID: string) =>
    request.delete('/plan/deleteDailyPlan', { params: { planUUID: dailyPlanUUID } })

//这部分是长期计划相关的请求
export const getUserLongTermPlanService = () => request.get('/plan/longTermPlan')

export const addLongTermPlanService = (LongTermPlanData: any) =>
    request.post('/plan/addLongTermPlan', LongTermPlanData)


export const modifyLongTermPlanService = (LongTermPlanData: any) =>
    request.post('/plan/modifyLongTermPlan', LongTermPlanData)

export const deleteLongTermPlanService = (LongTermPlanUUID: string) =>
    request.delete('/plan/deleteLongTermPlan', { params: { planUUID: LongTermPlanUUID } })