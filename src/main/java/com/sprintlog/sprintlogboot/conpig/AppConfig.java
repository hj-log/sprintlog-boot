package com.sprintlog.sprintlogboot.conpig;

import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;

import java.time.*;

@Configuration // 이 클래스는 Bean 정의 모음 설정 클래스임을 표시.
@EnableConfigurationProperties(SprintLogProperties.class)
public class AppConfig {

    // 반환 객체의 타입이 Bean 타입(Clock), 다른 곳에서 Clock clock으로 주입받으면 이 객체가 들어옴
    @Bean
    public Clock systemClock() {
        return Clock.systemDefaultZone();
    }



}
