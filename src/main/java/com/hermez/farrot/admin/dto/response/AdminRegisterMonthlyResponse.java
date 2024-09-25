package com.hermez.farrot.admin.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AdminRegisterMonthlyResponse {
    String signupDate;
    int signup_count;

    public AdminRegisterMonthlyResponse(String signupDate, int signupCount) {
        this.signupDate = signupDate;
        this.signup_count = signupCount;
    }
}
