package com.sprintlog.sprintlogboot.repository;

import com.sprintlog.sprintlogboot.domain.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface AuditLogRepository extends JpaRepository<ActivityAuditLog, Long> {

    // 최근 이력부터 (id 내림차순) 변경내역 조회
    List<ActivityAuditLog> findAllByOrderByIdDesc();

}