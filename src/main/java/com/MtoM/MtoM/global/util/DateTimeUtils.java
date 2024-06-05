package com.MtoM.MtoM.global.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return dateTime.format(formatter);
    }

    public static String formatTimeAgo(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        if (duration.toMinutes() < 1) {
            return "방금 전";
        } else if (duration.toMinutes() < 60) {
            return duration.toMinutes() + "분 전";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + "시간 전";
        } else if (duration.toDays() < 2) {
            return "하루 전";
        } else if (duration.toDays() < 7) {
            return duration.toDays() + "일 전";
        } else if (duration.toDays() < 30) {
            return duration.toDays() / 7 + "주 전";
        } else if (duration.toDays() < 365) {
            return duration.toDays() / 30 + "달 전";
        } else {
            return duration.toDays() / 365 + "년 전";
        }
    }
}