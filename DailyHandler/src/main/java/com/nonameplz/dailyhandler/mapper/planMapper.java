package com.nonameplz.dailyhandler.mapper;

import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.nonameplz.dailyhandler.pojo.dailyPlan;
import com.nonameplz.dailyhandler.pojo.labels;
import com.nonameplz.dailyhandler.pojo.longTermPlan;
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
    void updateDailyPlanLabels(String planUUID,String dailyPlanLabels);
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
    //更新每日计划
    @Update("update longtermplan set " +
            "planLevel=#{planLevel},planTitle=#{planTitle}," +
            "planDescription=#{planDescription},planStatus=#{planStatus}," +
            "planLocation=#{planLocation}," +
            "startTime=#{startTime},endTime=#{endTime}," +
            "modifyTime=now() " +
            "where planUUID=#{planUUID}")
    void modifyLongTermPlan(longTermPlan longTermPlan);
    //删除每日计划
    @Delete("delete from longtermplan where planUUID = #{planUUID}")
    void deleteLongTermPlan(String planUUID);
}
