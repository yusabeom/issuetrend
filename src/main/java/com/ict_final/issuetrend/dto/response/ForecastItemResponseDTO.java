package com.ict_final.issuetrend.dto.response;

import lombok.*;

import java.io.Serializable;


@Setter
@Getter
@ToString

public class ForecastItemResponseDTO implements Serializable {
    private String category;
    private String fcstTime;
    private String fcstValue;
    public ForecastItemResponseDTO() {
    }
    public ForecastItemResponseDTO(String category, String fcstTime, String fcstValue) {
        this.category = category;
        this.fcstTime = fcstTime;
        this.fcstValue = fcstValue;
    }
}
