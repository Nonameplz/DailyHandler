package com.nonameplz.dailyhandler.pojo;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class reCAPTCHAResponse {
    private boolean success;
    private float score;
    private String action;
    private String challenge_ts;
    private String hostname;
    private List<String> errorCodes;

}
