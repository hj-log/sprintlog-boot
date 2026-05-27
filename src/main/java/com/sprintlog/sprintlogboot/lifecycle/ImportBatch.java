package com.sprintlog.sprintlogboot.lifecycle;

import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//@Scope("prototype"): 필요할때마다 생성해서 가져다준다.
public class ImportBatch {

    private final String batchId;
    public final LocalDateTime startedAt;

    public ImportBatch() {
        this.batchId = UUID.randomUUID().toString().substring(0, 8);
        this.startedAt = LocalDateTime.now();
    }

    public String getBatchId() {
        return batchId;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    @Override
    public String toString() {
        return "ImportBatch{" +
                "batchId='" + batchId + '\'' +
                ", startedAt=" + startedAt +
                '}';
    }


}
