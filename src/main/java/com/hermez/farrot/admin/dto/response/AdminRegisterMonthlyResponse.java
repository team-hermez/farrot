package com.hermez.farrot.admin.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AdminRegisterMonthlyResponse {
    Date signupDate;
    int signup_count;

    public AdminRegisterMonthlyResponse(Date signupDate, int signupCount) {
        this.signupDate = signupDate;
        this.signup_count = signupCount;
    }
}
