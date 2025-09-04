package top.huajieyu001.blog.util;

import cn.hutool.core.lang.UUID;

import java.time.LocalDateTime;

public class UniqueIdUtils {

    public static String generateUniqueIdByTime() {
        LocalDateTime now = LocalDateTime.now();
        String month = now.getMonthValue() < 10 ? "0" + now.getMonthValue() : "" + now.getMonthValue();
        String day = now.getDayOfMonth() < 10 ? "0" + now.getDayOfMonth() : "" + now.getDayOfMonth();
        String hour = now.getHour() < 10 ? "0" + now.getHour() : "" + now.getHour();
        String minute = now.getMinute() < 10 ? "0" + now.getMinute() : "" + now.getMinute();
        String second = now.getSecond() < 10 ? "0" + now.getSecond() : "" + now.getSecond();
        return now.getYear() + "/" + month + "/" + day + "/" + hour + "-" + minute + "-" + second + "-" + now.getNano() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
