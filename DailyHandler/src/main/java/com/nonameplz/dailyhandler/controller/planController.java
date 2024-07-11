package com.nonameplz.dailyhandler.controller;

import com.nonameplz.dailyhandler.pojo.Result;
import com.nonameplz.dailyhandler.pojo.dailyPlan;
import com.nonameplz.dailyhandler.pojo.labels;
import com.nonameplz.dailyhandler.pojo.longTermPlan;
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

    @PostMapping("/modifyDailyPlan")
    public Result<Object> modifyDailyPlan(@RequestBody dailyPlan dailyPlan) {
        planservice.modifyDailyPlan(dailyPlan);
        return Result.success();
    }

    @DeleteMapping("/deleteDailyPlan")
    public Result<Object> deleteDailyPlan(@RequestParam(name = "planUUID") String planUUID) {
        planservice.deleteDailyPlan(planUUID);
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

    @PostMapping("/modifyLongTermPlan")
    public Result<Object> modifyLongTermPlan(@RequestBody longTermPlan longTermPlan) {

        planservice.modifyLongTermPlan(longTermPlan);
        return Result.success();
    }

    @DeleteMapping("/deleteLongTermPlan")
    public Result<Object> deleteLongTermPlan(@RequestParam(name = "planUUID") String planUUID) {
        planservice.deleteLongTermPlan(planUUID);
        return Result.success();
    }

}
