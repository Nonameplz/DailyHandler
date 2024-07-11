package com.nonameplz.dailyhandler;

import com.alibaba.fastjson2.JSON;
import com.nonameplz.dailyhandler.mapper.planMapper;
import com.nonameplz.dailyhandler.pojo.dailyPlan;
import com.nonameplz.dailyhandler.service.planService;
import com.nonameplz.dailyhandler.utils.UUIDGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DailyHandlerApplicationTests {
    @Autowired
    private planMapper planMapper;
    @Autowired
    private planService planService;

    @Test
    void contextLoads() {
    }

    @Test
    void testUUID() {
        System.out.println(UUIDGenerator.generateUUID());
    }

    @Test
    void testGetDailyPlan() {
        String userUUID = "00000190-51d1-94ff-ffff-ffffe0f9bb06";
        List<dailyPlan> dailyPlanList = planService.getDailyPlanList(userUUID);
        System.out.println(JSON.toJSONString(dailyPlanList));
    }

    @Test
    void testToJSON() {
        List<String> labels = new ArrayList<String>();
        labels.add("playing");
        labels.add("reading");
        dailyPlan plan = new dailyPlan();

        plan.setPlanLabels(new dailyPlan.PlanLabels("learning", labels));
        System.out.println(JSON.toJSONString(plan.getPlanLabels()));
    }

    @Test
    void testParseLabelJSON() {
        String userUUID = "00000190-51d1-94ff-ffff-ffffe0f9bb06";
        String planUUID = "00000190-825c-3d6f-ffff-ffff9abae5b4";
        List<dailyPlan.parseLabelJSON> planLabelList = planMapper.getUserDailyPlanLabels(userUUID);

        List<dailyPlan.parseLabelJSON> collect = planLabelList.stream().filter(item -> planUUID.equals(item.getPlanUUID())).toList();
        dailyPlan.parseLabelJSON parseLabelJSON = collect.get(0);
        dailyPlan.PlanLabels planLabels = JSON.parseObject(parseLabelJSON.getPlanLabels(), dailyPlan.PlanLabels.class);

        System.out.println(planLabels);
    }
}
