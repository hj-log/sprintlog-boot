package com.sprintlog.sprintlogboot.repository;

import com.sprintlog.sprintlogboot.domain.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

@Repository // 이 클래스는 Repository 역할을 하는 클래스고, Bean으로 등록해 줘.
public class ActivityRepository {

    /** CSV 파일의 컬럼 순서. 헤더 행과 데이터 행 모두 이 순서를 따른다. */
    private static final String CSV_HEADER =
            "type,title,minutes,visibility,tags,instructorName,completionRate,bookTitle";

    private final List<LearningActivity> storage = new ArrayList<>();

    public void add(LearningActivity activity) {
        if(activity == null) {
            throw new IllegalArgumentException("저장할 활동은 null일 수 없습니다.");
        }
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
    public Optional<LearningActivity> findfirst(Predicate<LearningActivity> predicate) {
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

    public void saveToBinary(Path binaryPath) throws IOException {

        Path parent = binaryPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(binaryPath)))) {
            oos.writeObject(new ArrayList<>(storage));
        }
    }

    // Spring에서 달라진 점
    // 기존 코드는 static 메서드로 새 객체를 만들어서 리턴했다면
    // Spring에서는 ActivityRepository가 컨테이너가 관리하는 단일 Bean이기 때문에
    //새 객체를 직접 생성하는 게 아닌 기존 Bean에 데이터를 적재하는 패턴이 좀 더 자연스럽다.
    public void loadFromBinary(Path binaryPath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(binaryPath)))) {
            List<LearningActivity> list = (List<LearningActivity>) ois.readObject();
            for (LearningActivity a : list) {
               this.add(a);
            }
        }
    }
}


