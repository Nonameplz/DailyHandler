package com.nonameplz.dailyhandler.mapper;

import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.nonameplz.dailyhandler.pojo.dailyPlan;
import com.nonameplz.dailyhandler.pojo.labels;
import com.nonameplz.dailyhandler.pojo.longTermPlan;
import com.nonameplz.dailyhandler.pojo.savingPlan;
import org.apache.ibatis.annotations.*;

@Mapper
public interface planMapper {
    //获取所有的计划标签
    @Select("select id, labelName, labelValue, createTime from planlabels")
    List<labels> getPlanLabels();

    //获取用户每日计划
    @Select("select planUUID, userUUID, planLevel, planTitle, planDescription, planStatus, planLocation, startTime, endTime, createTime, modifyTime,inuse " +
            "from dailyplan where userUUID = #{userUUID}")
    List<dailyPlan> getUserDailyPlan(String userUUID);

    //获取用户每日计划的标签
    @Select("select planUUID,planLabels from dailyplan where userUUID = #{userUUID}")
    List<dailyPlan.parseLabelJSON> getUserDailyPlanLabels(String userUUID);

    //添加新的每日计划
    @Insert("insert into dailyplan(" +
            "planUUID, useruuid, " +
            "planlevel,plantitle, " +
            "plandescription,planstatus, " +
            "planlocation,starttime, " +
            "endtime,inuse, " +
            "createtime, modifytime) " +
            "value (" +
            "#{planUUID},#{userUUID}," +
            "#{planLevel},#{planTitle}," +
            "#{planDescription},#{planStatus}," +
            "#{planLocation},#{startTime}," +
            "#{endTime},#{inuse}," +
            "now(),now())")
    void addDailyPlan(dailyPlan plan);

    //更新用户每日计划标签
    @Update("update dailyplan set planLabels = #{dailyPlanLabels} where planUUID = #{planUUID}")
    void updateDailyPlanLabels(String planUUID, String dailyPlanLabels);

    //更新每日计划
    @Update("update dailyplan set " +
            "planLevel=#{planLevel},planTitle=#{planTitle}," +
            "planDescription=#{planDescription},planStatus=#{planStatus}," +
            "planLocation=#{planLocation},inuse=#{inuse}," +
            "startTime=#{startTime},endTime=#{endTime}," +
            "modifyTime=now() " +
            "where planUUID=#{planUUID}")
    void modifyDailyPlan(dailyPlan plan);

    //删除每日计划
    @Delete("delete from dailyplan where planUUID = #{planUUID}")
    void deleteDailyPlan(String planUUID);

    //每日计划完成,修改每日计划状态
    @Update("update dailyplan set planStatus = 1 where planUUID = #{planUUID}")
    void dailyPlanDone(String planUUID);

    //每日计划重置
    @Update("update dailyplan set planStatus = 0 where planStatus = 1")
    void dailyPlanReset();

    //获取用户长期计划
    @Select("select planUUID, userUUID, planLevel, planTitle, planDescription, planStatus, planLocation, startTime, endTime, createTime, modifyTime " +
            "from longtermplan where userUUID = #{userUUID}")
    List<longTermPlan> getUserLongTermPlan(String userUUID);

    //获取用户长期计划标签
    @Select("select planUUID,planLabels from longtermplan where userUUID = #{userUUID}")
    List<longTermPlan.parseLabelJSON> getUserLongTermPlanLabels(String userUUID);

    //添加新的长期计划
    @Insert("insert into longtermplan(" +
            "planUUID, useruuid, " +
            "planlevel,plantitle, " +
            "plandescription,planstatus, " +
            "planlocation,starttime, " +
            "endtime, " +
            "createtime, modifytime) " +
            "value (" +
            "#{planUUID},#{userUUID}," +
            "#{planLevel},#{planTitle}," +
            "#{planDescription},#{planStatus}," +
            "#{planLocation},#{startTime}," +
            "#{endTime}," +
            "now(),now())")
    void addLongTermPlan(longTermPlan longTermPlan);

    //更新用户长期计划标签
    @Update("update longtermplan set planLabels = #{longTermPlanLabels} where planUUID = #{planUUID}")
    void updateLongTermPlanLabels(String planUUID, String longTermPlanLabels);

    //更新长期计划
    @Update("update longtermplan set " +
            "planLevel=#{planLevel},planTitle=#{planTitle}," +
            "planDescription=#{planDescription},planStatus=#{planStatus}," +
            "planLocation=#{planLocation}," +
            "startTime=#{startTime},endTime=#{endTime}," +
            "modifyTime=now() " +
            "where planUUID=#{planUUID}")
    void modifyLongTermPlan(longTermPlan longTermPlan);

    //删除长期计划
    @Delete("delete from longtermplan where planUUID = #{planUUID}")
    void deleteLongTermPlan(String planUUID);

    //长期计划完成,修改长期计划状态
    @Update("update longtermplan set planStatus = 1 where planUUID = #{planUUID}")
    void longTermPlanDone(String planUUID);

    //获取用户储蓄计划
    @Select("select targetUUID, userUUID, targetAmount, target, recentlyAmount, isDone, createTime, modifyTime from savingplan where userUUID = #{userUUID}")
    List<savingPlan> getSavingPlan(String userUUID);

    //获取用户储蓄进度
    @Select("select userUUID, targetUUID, processUUID, saving, outPut, createTime, modifyTime from savingprocess where userUUID = #{userUUID}")
    List<savingPlan.savingProcess> getSavingProcess(String userUUID);

    //添加新的储蓄计划
    @Insert("insert savingplan(" +
            "targetUUID, userUUID, " +
            "targetAmount, target, " +
            "recentlyAmount, isDone, " +
            "createTime, modifyTime) " +
            "value (" +
            "#{targetUUID},#{userUUID}," +
            "#{targetAmount},#{target}," +
            "#{recentlyAmount},#{isDone}," +
            "now(),now())")
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
