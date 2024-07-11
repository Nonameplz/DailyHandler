package com.nonameplz.dailyhandler.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class labels {
    private Integer id;
    private String labelName;
    private String labelValue;
    private String createTime;
}
