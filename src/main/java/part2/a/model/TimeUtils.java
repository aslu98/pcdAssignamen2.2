package part2.a.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    public static LocalDateTime toDateTime(final long unixTime){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(unixTime), ZoneId.systemDefault());
    }

    public static String getStringTime(final long unixTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return toDateTime(unixTime).format(formatter);
    }
}
