package org.mamute.providers;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.time.Clock;

@ApplicationScoped
public class SystemUtcClockProvider implements ClockProvider {
    @Override
    public Clock get() {
        return Clock.systemUTC();
    }
}
