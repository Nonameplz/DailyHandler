package com.nonameplz.dailyhandler.controller;

import com.nonameplz.dailyhandler.pojo.*;
import com.nonameplz.dailyhandler.service.planService;
import com.nonameplz.dailyhandler.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/plan")
public class planController {

    @Autowired
    private planService planservice;

    @GetMapping("/labels")
    public Result<List<labels>> getLabels() {
        List<labels> labels = planservice.getPlanLabels();
        return Result.success(labels);
    }

    //这部分是每日计划相关的Controller
    @GetMapping("/dailyPlan")
    public Result<List<dailyPlan>> getDailyPlan() {
        Map<String, Object> user = ThreadLocalUtil.get();
        List<dailyPlan> dailyPlanList = planservice.getDailyPlanList(user.get("userUUID").toString());

        return Result.success(dailyPlanList);
    }

    @PostMapping("/addDailyPlan")
    public Result<Object> addDailyPlan(@RequestBody dailyPlan dailyPlan) {
        Map<String, Object> userInfo = ThreadLocalUtil.get();
        String userUUID = userInfo.get("userUUID").toString();

        planservice.addDailyPlan(dailyPlan, userUUID);

        return Result.success();
    }

    @PutMapping("/modifyDailyPlan")
    public Result<Object> modifyDailyPlan(@RequestBody dailyPlan dailyPlan) {
        planservice.modifyDailyPlan(dailyPlan);
        return Result.success();
    }

    @DeleteMapping("/deleteDailyPlan")
    public Result<Object> deleteDailyPlan(@RequestParam(name = "planUUID") String planUUID) {
        planservice.deleteDailyPlan(planUUID);
        return Result.success();
    }

    @PutMapping("/dailyPlanDone")
    public Result<Object> dailyPlanDone(@RequestParam String planUUID) {
        planservice.dailyPlanDone(planUUID);
        return Result.success();
    }

    //这部分是长期计划相关的Controller
    @GetMapping("/longTermPlan")
    public Result<List<longTermPlan>> getLongTermPlan() {
        Map<String, Object> user = ThreadLocalUtil.get();
        List<longTermPlan> longTermPlanPlanList = planservice.getLongTermPlanPlanList(user.get("userUUID").toString());

        return Result.success(longTermPlanPlanList);
    }

    @PostMapping("/addLongTermPlan")
    public Result<Object> addLongTermPlan(@RequestBody longTermPlan longTermPlan) {
        Map<String, Object> userInfo = ThreadLocalUtil.get();
        String userUUID = userInfo.get("userUUID").toString();

        planservice.addLongTermPlan(longTermPlan, userUUID);

        return Result.success();
    }

    @PutMapping("/modifyLongTermPlan")
    public Result<Object> modifyLongTermPlan(@RequestBody longTermPlan longTermPlan) {

        planservice.modifyLongTermPlan(longTermPlan);
        return Result.success();
    }

    @DeleteMapping("/deleteLongTermPlan")
    public Result<Object> deleteLongTermPlan(@RequestParam(name = "planUUID") String planUUID) {
        planservice.deleteLongTermPlan(planUUID);
        return Result.success();
    }

    @PutMapping("/longTermPlanDone")
    public Result<Object> longTermPlanDone(@RequestParam String planUUID) {
        planservice.longTermPlanDone(planUUID);
        return Result.success();
    }

    //这部分是储蓄计划相关的Controller
    @GetMapping("/savingPlan")
    public Result<Map<String, List<Object>>> getSavingPlan() {
        Map<String, Object> userInfo = ThreadLocalUtil.get();
        String userUUID = userInfo.get("userUUID").toString();

        Map<String, List<Object>> savingPlan = planservice.getSavingPlan(userUUID);

        return Result.success(savingPlan);
    }

    @PostMapping("/addSavingPlan")
    public Result<Object> addSavingPlan(@RequestBody savingPlan savingPlan) {
        Map<String, Object> userInfo = ThreadLocalUtil.get();
        String userUUID = userInfo.get("userUUID").toString();

        savingPlan.setUserUUID(userUUID);
        planservice.addSavingPlan(savingPlan);

        return Result.success();
    }

    @PostMapping("/addSavingProcess")
    public Result<Object> addSavingProcess(@RequestBody savingPlan.savingProcess savingProcess) {
        Map<String, Object> userInfo = ThreadLocalUtil.get();
        String userUUID = userInfo.get("userUUID").toString();

        savingProcess.setUserUUID(userUUID);
        planservice.addSavingProcess(savingProcess);

        return Result.success();
    }

    @PutMapping("/modifySavingPlan")
    public Result<Object> modifySavingPlan(@RequestBody savingPlan savingPlan) {
        planservice.modifySavingPlan(savingPlan);

        return Result.success();
    }

    @PutMapping("/modifySavingProcess")
    public Result<Object> modifySavingProcess(@RequestBody savingPlan.savingProcess savingProcess) {
        planservice.modifySavingProcess(savingProcess);

        return Result.success();
    }

    @DeleteMapping("/delSavingPlan")
    public Result<Object> deleteSavingPlan(@RequestParam(name = "targetUUID") String targetUUID) {
        planservice.deleteSavingPlan(targetUUID);

        return Result.success();
    }

    @DeleteMapping("/delSavingProcess")
    public Result<Object> deleteSavingProcess(@RequestParam(name = "processUUID") String processUUID) {
        planservice.deleteSavingProcess(processUUID);

        return Result.success();
    }
}
