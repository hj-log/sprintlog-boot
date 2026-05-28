package com.sprintlog.sprintlogboot.domain;

import java.io.*;

public class LectureLog extends LearningActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String instructorName;

    public LectureLog(String title, int minutes, Visibility visibility, String instructorName) {

        super(title, minutes, visibility, ActivityCategory.LECTURE);
        this.instructorName = normalizeInstructorName(instructorName);
    }

    public void method1() {
        return;
    }

    private String normalizeInstructorName(String instructorName) {
        if(instructorName == null || instructorName.isBlank()) {
            return "강사 미정";
        }

        return instructorName;

    }

    public String getInstructorName() {
        return instructorName;
    }
}

