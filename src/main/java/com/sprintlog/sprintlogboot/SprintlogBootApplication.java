package com.sprintlog.sprintlogboot;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.*;

// BaseEntity의 @CreatedDate / @LastModifiedDate 자동 채움 기능을 켠다.
// 이거 없으면 둘 다 null 들어감.
@EnableJpaAuditing
@SpringBootApplication
public class SprintlogBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SprintlogBootApplication.class, args);
    }

}
