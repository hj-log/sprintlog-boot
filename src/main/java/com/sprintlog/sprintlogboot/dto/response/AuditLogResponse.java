package com.sprintlog.sprintlogboot.dto.response;

import com.sprintlog.sprintlogboot.domain.*;

import java.time.*;

public record AuditLogResponse(
        long id,
        String action,
        String detail,
        LocalDateTime at
) {

    public static AuditLogResponse from(ActivityAuditLog log) {
        return new AuditLogResponse(log.getId(), log.getAction(), log.getDetail(), log.getCreatedAt());
    }
}
