package com.sprintlog.sprintlogboot.monitoring;

import io.micrometer.core.aop.*;
import io.micrometer.core.instrument.*;
import org.springframework.context.annotation.*;

@Configuration
public class TimedConfig {

    @Bean
    public TimedAspect timedAspect(MeterRegistry meterRegistry) {
        return new TimedAspect(meterRegistry);
    }
}
