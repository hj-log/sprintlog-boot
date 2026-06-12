package com.sprintlog.sprintlogboot.repository;

import com.sprintlog.sprintlogboot.domain.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.function.*;

@Repository // 이 클래스는 Repository 역할을 하는 클래스고, Bean으로 등록해 줘.
public class ActivityRepository {

    private final List<LearningActivity> storage = new ArrayList<>();

    public void add(LearningActivity activity) {
        if(activity == null) {
            throw new IllegalArgumentException("저장할 활동은 null일 수 없습니다.");
        }
        storage.add(activity);
    }

    public void update (LearningActivity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("수정할 활동은 null일 수 없습니다.");
        }
        storage.remove(activity);
        storage.add(activity);
    }

    // 저장된 모든 활동을 반환한다.
    public List<LearningActivity> findAll() {
        return Collections.unmodifiableList(storage); // 외부에서 함부러 수정못하게하기 위해서
    }

    // 조건에 맞는 활동들만 골라 변환한다.
    public List<LearningActivity> filter(Predicate<LearningActivity> predicate) {
       List<LearningActivity> result = new ArrayList<>();
        for (LearningActivity activity : storage) {
            if(predicate.test(activity)) {
                result.add(activity);
            }
        }
        return result;
    }

    // 조건에 맞는 첫번째 활동만 골라 변환한다.
    // Optional로 포장해서 줌 -> 그래서 포장한걸 확인하고 값을 꺼내기 -> null을 보내지 않기 위해서 Optional 사용
    public Optional<LearningActivity> findFirst(Predicate<LearningActivity> predicate) {
        for (LearningActivity activity : storage) {
            if(predicate.test(activity)) {
                return Optional.of(activity);
            }
        }
        return Optional.empty();
    }

    // 저장된 활동 수를 반환한다.
    public int count() {
        return storage.size();
    }

    // 저장된 모든 활동의 총 학습 시간(분)을 반환한다.
    public int getTotalMinutes() {
        int total = 0;
        for (LearningActivity activity : storage) {
            total += activity.getMinutes();
        }
        return total;
    }

    // removeIf: 조건에 맞는 객체를 리스트에서 삭제 후 true 리턴, 해당 id를 가진 활동이 없다면 false를 리턴
    public boolean removeById(Long id) {
        return storage.removeIf(activity -> activity.getId() == id);
    }
}


