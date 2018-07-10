package org.mamute.providers;

import java.time.Clock;

public class SystemUtcClockProvider implements ClockProvider {
    @Override
    public Clock get() {
        return Clock.systemUTC();
    }
}
