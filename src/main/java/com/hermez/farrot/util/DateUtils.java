package com.hermez.farrot.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String formatLocalDateTime(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    public static String timeAgoOrFormatted(LocalDateTime createdTime, String pattern) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdTime, now);

        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return seconds + "초 전";
        }

        long minutes = duration.toMinutes();
        if (minutes < 60) {
            return minutes + "분 전";
        }

        // 조건에 해당하지 않으면 기존 포맷된 날짜 반환
        return formatLocalDateTime(createdTime, pattern);
    }
}