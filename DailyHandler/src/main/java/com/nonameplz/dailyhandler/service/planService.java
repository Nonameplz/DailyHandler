package com.nonameplz.dailyhandler.service;

import com.nonameplz.dailyhandler.pojo.dailyPlan;
import com.nonameplz.dailyhandler.pojo.labels;
import com.nonameplz.dailyhandler.pojo.longTermPlan;

import java.util.List;

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

    //通过userUUID获取用户长期计划
    List<longTermPlan> getLongTermPlanPlanList(String userUUID);

    //添加新的长期计划
    void addLongTermPlan(longTermPlan longTermPlan, String userUUID);

    //修改长期计划
    void modifyLongTermPlan(longTermPlan longTermPlan);

    //删除长期计划
    void deleteLongTermPlan(String planUUID);
}
