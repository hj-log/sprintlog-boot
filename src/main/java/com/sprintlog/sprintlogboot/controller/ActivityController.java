package com.sprintlog.sprintlogboot.controller;

import com.sprintlog.sprintlogboot.domain.*;
import com.sprintlog.sprintlogboot.repository.*;
import com.sprintlog.sprintlogboot.service.*;
import com.sprintlog.sprintlogboot.service.ActivityDashboard.Summary;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController // @ResponseBody 내장되어 있음
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityRepository repository;
    private final ActivityDashboard dashboard;

    // 모든 활동 목록
    @GetMapping
    public ResponseEntity<List<LearningActivity>> getAll() {
        List<LearningActivity> list = repository.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningActivity> getById(@PathVariable Long id) {
        Optional<LearningActivity> first = repository.findfirst(activity -> activity.getId() == id);
        if(first.isPresent()) {
          return ResponseEntity.ok().body(first.get());
        }
        return ResponseEntity.notFound().build();
    }

    // 카테고리별로 그룹화된 활동 목록
    @GetMapping("/dashboard")
    public ResponseEntity<Map<ActivityCategory, List<LearningActivity>>> getDashborad() {
        Map<ActivityCategory, List<LearningActivity>> map = dashboard.groupByCategory();
        return ResponseEntity.ok().body(map);
    }


    // 활동 수 요약 정보(전체 / 강의 / 실습 / 독서) - ActivityDashborad
    @GetMapping("/summary")
    public ResponseEntity<Summary> getSummary() {
        return ResponseEntity.ok().body(dashboard.summarize());
    }

    // 태그로 활동을 필터링
    @GetMapping("/search")
    public ResponseEntity<List<LearningActivity>> searchByTag(@RequestParam String tag,
                                                              @RequestParam String name,
                                                              @RequestParam int age) {

        log.info("RequestPara을 통해 얻어낸 값: {}, {}, {}", tag, name, age);

        List<LearningActivity> list = dashboard.filterByTag(tag);
        return ResponseEntity.ok().body(list);
    }

}
