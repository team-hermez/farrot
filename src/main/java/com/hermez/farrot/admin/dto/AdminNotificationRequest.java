package com.hermez.farrot.admin.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminNotificationRequest {
    List<Integer> selectedIds;
    String content;
    String priority;
    String date;
}
