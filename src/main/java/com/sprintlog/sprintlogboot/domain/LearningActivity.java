package com.sprintlog.sprintlogboot.domain;


import com.sprintlog.sprintlogboot.exception.*;
import lombok.*;

import java.io.*;
import java.util.*;

@Getter
public abstract class LearningActivity implements Serializable {

    // 이 파일의 클래스 구조가 현재의 클래스와 같은지에 대한 버전 키 검사용 필드
    private static final long serialVersionUID = 1L;

    private static int totalCreateCount = 0;

    private final long id;
    private String title;
    private int minutes;
    private Visibility visibility;
    private final ActivityCategory category;
    private final Set<String> tags = new HashSet<>();

    public LearningActivity(String title, int minutes, Visibility visibility, ActivityCategory category) {
        validateTitle(title);
        validateMinutes(minutes);
        totalCreateCount++;
        this.id = totalCreateCount;
        this.title = title.trim(); // 좌우 공백 제거
        this.minutes = minutes;
        this.visibility = visibility;
        this.category = category;
    }

    public static int getTotalCreatedCount() {
        return totalCreateCount;
    }

    public void extendStudy(int additionalMinutes) {
        if (additionalMinutes <= 0) {
            throw new InvalidActivityException(
                    "추가 학습 시간은 1분 이상이어야 합니다. 입력값: " + additionalMinutes);
        }
        this.minutes += additionalMinutes;
    }

    // 등록된 태그를 제거한다.
    public boolean removeTag(String tag) {
        if (tag == null || tag.isBlank()) {
            return false;
        }
        return tags.remove(tag.trim().toLowerCase());
    }

    public void changeTitle(String newTitle) {
        validateTitle(newTitle);
        this.title = newTitle;
    }

    private void validateTitle(String newTitle) {
        if (newTitle == null || newTitle.isBlank()) {
            throw new InvalidActivityException("학습 제목은 비워둘 수 없습니다.");
        }
    }

    private void validateMinutes(int newMinutes) {
        if(newMinutes <= 0) {
            throw new InvalidActivityException("학습 시간은 1분 이상이여야 합니다. 입력값: " + newMinutes);
        }
    }

    public void addTag(String tag) {
        if (tag == null || tag.isBlank()) {
            throw new InvalidActivityException("태그는 비워둘 수 없습니다.");
        }
        tags.add(tag.trim().toLowerCase());
    }

    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public boolean hasTag(String tag) {
        if (tag == null) return false;
        return tags.contains(tag.trim().toLowerCase());
    }

    public void openToPublic() {
        this.visibility = Visibility.PUBLIC;
    }

    public void hideFromPublic() {
        this.visibility = Visibility.PRIVATE;
    }

}


