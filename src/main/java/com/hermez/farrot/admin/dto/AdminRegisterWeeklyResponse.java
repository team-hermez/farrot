package com.hermez.farrot.admin.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class AdminRegisterWeeklyResponse {
    private Date signupDate;
    private int signupCount;

    public AdminRegisterWeeklyResponse(Date signupDate, int signupCount) {
        this.signupDate = signupDate;
        this.signupCount = signupCount;
    }
}
