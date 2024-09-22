package com.hermez.farrot.payment.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TrackingInfoResponse {
    private String code;
    private String msg;
    private String completeYN;
    private boolean status;
    private TrackingDetail firstDetail;
    private TrackingDetail lastDetail;
    private List<TrackingDetail> trackingDetails;
}

@Getter
@Setter
class TrackingDetail {
    private String code;
    private String kind;
    private String level;
    private String manName;
    private String manPic;
    private String remark;
    private String telno;
    private String telno2;
    private String time;
    private String timeString;
    private String where;

}
