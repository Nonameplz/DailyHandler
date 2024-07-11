package com.nonameplz.dailyhandler.service.impl;

import com.alibaba.fastjson2.JSON;
import com.nonameplz.dailyhandler.mapper.planMapper;
import com.nonameplz.dailyhandler.pojo.dailyPlan;
import com.nonameplz.dailyhandler.pojo.labels;
import com.nonameplz.dailyhandler.pojo.longTermPlan;
import com.nonameplz.dailyhandler.pojo.savingPlan;
import com.nonameplz.dailyhandler.service.planService;
import com.nonameplz.dailyhandler.utils.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class planServiceImpl implements planService {

    @Autowired
    private planMapper planMapper;

    @Override
    public List<labels> getPlanLabels() {
        return planMapper.getPlanLabels();
    }

    @Override
    public List<dailyPlan> getDailyPlanList(String userUUID) {
        List<dailyPlan> dailyPlanList = planMapper.getUserDailyPlan(userUUID);
        List<dailyPlan.parseLabelJSON> dailyPlanLabelsList = planMapper.getUserDailyPlanLabels(userUUID);

        for (dailyPlan dailyPlan : dailyPlanList) {
            Optional<dailyPlan.parseLabelJSON> dailyPlanLabelsJSON = dailyPlanLabelsList.stream()
                    .filter(element -> element.getPlanUUID().equals(dailyPlan.getPlanUUID()))
                    .findFirst();
            dailyPlanLabelsJSON.ifPresent(dailyPlan::mergePlanLabels);
        }

        return dailyPlanList;
    }

    @Override
    public void addDailyPlan(dailyPlan dailyPlan, String userUUID) {
        dailyPlan.setPlanUUID(UUIDGenerator.generateUUID());
        dailyPlan.setUserUUID(userUUID);
        dailyPlan.setPlanStatus((short) 0);
        dailyPlan.setCreateTime(LocalDateTime.now());
        dailyPlan.setModifyTime(LocalDateTime.now());

        planMapper.addDailyPlan(dailyPlan);
        planMapper.updateDailyPlanLabels(dailyPlan.getPlanUUID(), JSON.toJSONString(dailyPlan.getPlanLabels()));
    }

    @Override
    public void modifyDailyPlan(dailyPlan dailyPlan) {
        dailyPlan.setModifyTime(LocalDateTime.now());
        planMapper.modifyDailyPlan(dailyPlan);
        planMapper.updateDailyPlanLabels(dailyPlan.getPlanUUID(), JSON.toJSONString(dailyPlan.getPlanLabels()));
    }

    @Override
    public void deleteDailyPlan(String planUUID) {
        planMapper.deleteDailyPlan(planUUID);
    }

    @Override
    public void dailyPlanDone(String planUUID) {
        planMapper.dailyPlanDone(planUUID);
    }

    @Override
    public void dailyPlanReset() {
        planMapper.dailyPlanReset();
    }

    @Override
    public List<longTermPlan> getLongTermPlanPlanList(String userUUID) {
        List<longTermPlan> longTermPlanList = planMapper.getUserLongTermPlan(userUUID);
        List<longTermPlan.parseLabelJSON> longTermPlanLabelsList = planMapper.getUserLongTermPlanLabels(userUUID);

        for (longTermPlan longTermPlan : longTermPlanList) {
            Optional<longTermPlan.parseLabelJSON> longTermPlanLabelsJSON = longTermPlanLabelsList.stream()
                    .filter(element -> element.getPlanUUID().equals(longTermPlan.getPlanUUID()))
                    .findFirst();
            longTermPlanLabelsJSON.ifPresent(longTermPlan::mergePlanLabels);
        }

        return longTermPlanList;
    }

    @Override
    public void addLongTermPlan(longTermPlan longTermPlan, String userUUID) {
        longTermPlan.setPlanUUID(UUIDGenerator.generateUUID());
        longTermPlan.setUserUUID(userUUID);
        longTermPlan.setPlanStatus((short) 0);
        longTermPlan.setCreateTime(LocalDateTime.now());
        longTermPlan.setModifyTime(LocalDateTime.now());

        planMapper.addLongTermPlan(longTermPlan);
        planMapper.updateLongTermPlanLabels(longTermPlan.getPlanUUID(), JSON.toJSONString(longTermPlan.getPlanLabels()));
    }

    @Override
    public void modifyLongTermPlan(longTermPlan longTermPlan) {
        longTermPlan.setModifyTime(LocalDateTime.now());
        planMapper.modifyLongTermPlan(longTermPlan);
        planMapper.updateLongTermPlanLabels(longTermPlan.getPlanUUID(), JSON.toJSONString(longTermPlan.getPlanLabels()));

    }

    @Override
    public void deleteLongTermPlan(String planUUID) {
        planMapper.deleteLongTermPlan(planUUID);
    }

    @Override
    public void longTermPlanDone(String planUUID) {
        planMapper.longTermPlanDone(planUUID);
    }

    @Override
    public Map<String, List<Object>> getSavingPlan(String userUUID) {
        List<savingPlan> savingPlan = planMapper.getSavingPlan(userUUID);
        List<savingPlan.savingProcess> savingProcess = planMapper.getSavingProcess(userUUID);

        Map<String, List<Object>> savingPlanMap = new HashMap<>();
        savingPlanMap.put("savingPlan", Collections.singletonList(savingPlan));
        savingPlanMap.put("savingProcess", Collections.singletonList(savingProcess));
        return savingPlanMap;
    }

    @Override
    public void addSavingPlan(savingPlan savingPlan) {
        savingPlan.setTargetUUID(UUIDGenerator.generateUUID());
        savingPlan.setIsDone((short) 0);
        savingPlan.setCreateTime(LocalDateTime.now());
        savingPlan.setModifyTime(LocalDateTime.now());

        planMapper.addSavingPlan(savingPlan);
    }

    @Override
    public void addSavingProcess(savingPlan.savingProcess savingProcess) {
        savingProcess.setProcessUUID(UUIDGenerator.generateUUID());
        savingProcess.setCreateTime(LocalDateTime.now());
        savingProcess.setModifyTime(LocalDateTime.now());

        planMapper.addSavingProcess(savingProcess);
    }

    @Override
    public void modifySavingPlan(savingPlan savingPlan) {
        savingPlan.setModifyTime(LocalDateTime.now());

        planMapper.modifySavingPlan(savingPlan);
    }

    @Override
    public void modifySavingProcess(savingPlan.savingProcess savingProcess) {
        savingProcess.setModifyTime(LocalDateTime.now());

        planMapper.modifySavingProcess(savingProcess);
    }

    @Override
    public void deleteSavingPlan(String targetUUID) {
        planMapper.deleteSavingPlan(targetUUID);
    }

    @Override
    public void deleteSavingProcess(String processUUID) {
        planMapper.deleteSavingProcess(processUUID);
    }
}
