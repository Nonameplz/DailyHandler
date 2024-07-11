package com.nonameplz.dailyhandler.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class savingPlan {
    private String userUUID;
    private String targetUUID;
    private String target;
    private Double targetAmount;
    private Double recentlyAmount;
    private Short isDone;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class savingProcess{
        private String userUUID;
        private String targetUUID;
        private String processUUID;
        private Double saving;
        private Double outPut;
        private LocalDateTime createTime;
        private LocalDateTime modifyTime;
    }
}
