package com.sprintlog.sprintlogboot.domain;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.*;

import java.time.*;

// 모든 엔터티가 공통으로 가지는 것 - 식별자(id)와 생성, 수정 시각 - 을 한곳에 모은 상위 클래스
@Getter
@MappedSuperclass // 이 클래스 자채는 테이블이 되지 않는다. 대신 이 클래스를 상속한 엔터티의 테이블에 여기 선언된 컬럼이 합쳐져서 들어간다.
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 생성 시각 - 처음 저장될 때 한번 채워지고, 이후 바뀌지 않는다.
    @CreatedDate
    @Column(updatable = false) // 변경을 막음.
    private LocalDateTime createdAt;

    // 수정 시각 - 저장될 때마다 시각으로 갱신
    @LastModifiedBy
    private LocalDateTime updatedAt;

}
