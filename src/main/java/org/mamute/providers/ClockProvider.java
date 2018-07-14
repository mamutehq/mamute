package org.mamute.providers;

import java.time.Clock;

public interface ClockProvider {
    Clock get();
}
