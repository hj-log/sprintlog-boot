package com.sprintlog.sprintlogboot.lifecycle;

import com.sprintlog.sprintlogboot.conpig.*;
import com.sprintlog.sprintlogboot.domain.*;
import com.sprintlog.sprintlogboot.repository.*;
import jakarta.annotation.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;


@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j // log라는 이름으로 SLF4J 로거 자동 생성, 출력객체
public class DataInitializer {

    private final ActivityRepository repository;
    private final SprintLogProperties properties;

    // 주입된 의존성 객체를 가지고 무언가 해야할 로직을 작성
    @PostConstruct
    public void loadSampleData() {

        log.info("[lifecycle] @PostConstruct — {}", properties.getWelcomeMessage());

        if (!properties.getSampleDate().isEnabled()) {
            log.info("[lifecycle] sample-date.enabled = false - 적재 건너뜀!");
            return;
        }

        log.info("[lifecycle] @PostConstruct — DataInitializer 가 샘플 데이터를 적재합니다.");

        repository.add(new LectureLog("Spring Bean Scope", 90, Visibility.PUBLIC, "이강사"));
        repository.add(new PracticeLog("@PostConstruct 실습", 60, Visibility.PUBLIC, 85));
        repository.add(new ReadingLog("스프링 인 액션", 75, Visibility.PUBLIC, "스프링 인 액션 5판"));
        repository.add(new LectureLog("Prototype vs Singleton", 45, Visibility.PRIVATE, "이강사"));

        log.info("[lifecycle] 샘플 데이터 적재 완료 — 총 {}개 ", repository.count());
    }

    @PreDestroy
    public void shutdown() {
        log.info("[lifecycle] @PreDestroy — DataInitializer 가 종료 정리를 합니다.");
        log.info("[lifecycle] 최종 활동 수: {}개, 총 학습 시간: {}분",
                repository.count(), repository.getTotalMinutes());
    }

}
