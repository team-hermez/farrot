package com.hermez.farrot.admin.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class AdminRegisterWeeklyResponse {
    private int signupCount;
    private Date signupDate;

    public AdminRegisterWeeklyResponse(int signupCount, Date signupDate) {
        this.signupCount = signupCount;
        this.signupDate = signupDate;
    }
}
