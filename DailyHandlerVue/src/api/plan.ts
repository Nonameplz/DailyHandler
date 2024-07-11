import request from '@/utils/request'

export const getPlanLabelsService = () => request.get('/plan/labels')

//这部分是每日计划相关的请求
export const getUserDailyPlanService = () => request.get('/plan/dailyPlan')

export const addDailyPlanService = (dailyPlanData: any) =>
    request.post('/plan/addDailyPlan', dailyPlanData)


export const modifyDailyPlanService = (dailyPlanData: any) =>
    request.put('/plan/modifyDailyPlan', dailyPlanData)

export const deleteDailyPlanService = (dailyPlanUUID: string) =>
    request.delete('/plan/deleteDailyPlan', { params: { planUUID: dailyPlanUUID } })

export const dailyPlanDoneService = (planUUID: string) => {
    const params = new URLSearchParams()
    params.append('planUUID', planUUID)
    request.put('/plan/dailyPlanDone', params)
}

//这部分是长期计划相关的请求
export const getUserLongTermPlanService = () => request.get('/plan/longTermPlan')

export const addLongTermPlanService = (LongTermPlanData: any) =>
    request.post('/plan/addLongTermPlan', LongTermPlanData)


export const modifyLongTermPlanService = (LongTermPlanData: any) =>
    request.put('/plan/modifyLongTermPlan', LongTermPlanData)

export const deleteLongTermPlanService = (LongTermPlanUUID: string) =>
    request.delete('/plan/deleteLongTermPlan', { params: { planUUID: LongTermPlanUUID } })

export const longTermPlanDoneService = (planUUID: string) => {
    const params = new URLSearchParams()
    params.append('planUUID', planUUID)
    request.put('/plan/longTermPlanDone', params)
}

//这部分是储蓄计划相关请求
export const getUserSavingPlanService = () => request.get('/plan/savingPlan')

export const addSavingPlanService = (savingPlanData: any) => request.post('/plan/addSavingPlan', savingPlanData)

export const addSavingProcessService = (savingProcessData: any) => request.post('/plan/addSavingProcess', savingProcessData)

export const modifySavingPlanService = (savingPlanData: string) => request.put('/plan/modifySavingPlan', savingPlanData)

export const modifySavingProcessService = (savingProcessData: any) => request.put('/plan/modifySavingProcess', savingProcessData)

export const delSavingPlanService = (targetUUID: string) => request.delete('/plan/delSavingPlan', { params: { targetUUID: targetUUID } })

export const delSavingProcessService = (processUUID: string) => request.delete('/plan/delSavingProcess', { params: { processUUID: processUUID } })