package org.mamute.util;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class ClockUtils {
    public static Clock fixedClock(LocalDateTime localDateTime) {
        return Clock.fixed(localDateTime.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
    }
}
