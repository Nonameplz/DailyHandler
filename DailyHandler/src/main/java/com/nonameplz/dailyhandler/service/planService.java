package com.nonameplz.dailyhandler.service;

import com.nonameplz.dailyhandler.pojo.dailyPlan;
import com.nonameplz.dailyhandler.pojo.labels;
import com.nonameplz.dailyhandler.pojo.longTermPlan;
import com.nonameplz.dailyhandler.pojo.savingPlan;

import java.util.List;
import java.util.Map;

public interface planService {

    //获取所有的计划标签
    List<labels> getPlanLabels();

    //通过userUUID获取用户每日计划
    List<dailyPlan> getDailyPlanList(String userUUID);

    //添加新的每日计划
    void addDailyPlan(dailyPlan dailyPlan, String userUUID);

    //修改每日计划
    void modifyDailyPlan(dailyPlan dailyPlan);

    //删除每日计划
    void deleteDailyPlan(String planUUID);

    //每日计划完成,修改完成状态
    void dailyPlanDone(String planUUID);

    //每日计划状态重置
    void dailyPlanReset();

    //通过userUUID获取用户长期计划
    List<longTermPlan> getLongTermPlanPlanList(String userUUID);

    //添加新的长期计划
    void addLongTermPlan(longTermPlan longTermPlan, String userUUID);

    //修改长期计划
    void modifyLongTermPlan(longTermPlan longTermPlan);

    //删除长期计划
    void deleteLongTermPlan(String planUUID);

    //长期计划完成,修改完成状态
    void longTermPlanDone(String planUUID);

    //通过用户ID获取所有的储蓄计划
    Map<String, List<Object>> getSavingPlan(String userUUID);

    //添加新的储蓄计划
    void addSavingPlan(savingPlan savingPlan);

    //添加新的储蓄进度
    void addSavingProcess(savingPlan.savingProcess savingProcess);

    //修改储蓄计划
    void modifySavingPlan(savingPlan savingPlan);

    //修改储蓄进度
    void modifySavingProcess(savingPlan.savingProcess savingProcess);

    //删除储蓄计划
    void deleteSavingPlan(String targetUUID);

    //删除储蓄进度
    void deleteSavingProcess(String processUUID);
}
