package org.mamute.util;

import org.mamute.providers.ClockProvider;

import java.time.Clock;

public class MockClockProvider implements ClockProvider {
    private Clock currentClock;

    @Override
    public Clock get() {
        return currentClock;
    }

    public void set(Clock currentClock) {
        this.currentClock = currentClock;
    }
}
