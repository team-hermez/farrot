package com.hermez.farrot.notification.dto;

import lombok.Builder;

@Builder
public record NotificationRequest(String token, String title, String message) {

}
