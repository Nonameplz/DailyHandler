package com.nonameplz.dailyhandler.pojo;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class longTermPlan {
    private String PlanUUID;
    private String userUUID;
    private Short planLevel;
    private PlanLabels planLabels;
    private String planTitle;
    private String planDescription;
    private Short planStatus;
    private String planLocation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlanLabels {
        private String mainLabel;
        private List<String> labels;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class parseLabelJSON {
        private String planUUID;
        private String planLabels;
    }

    public void mergePlanLabels(longTermPlan.parseLabelJSON labelsJSON) {
        this.planLabels = JSON.parseObject(labelsJSON.getPlanLabels(), longTermPlan.PlanLabels.class);
    }

}
