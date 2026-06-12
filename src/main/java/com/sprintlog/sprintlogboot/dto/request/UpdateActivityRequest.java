package com.sprintlog.sprintlogboot.dto.request;

import com.sprintlog.sprintlogboot.domain.*;
import jakarta.validation.constraints.*;

public record UpdateActivityRequest(

        @NotBlank(message = "제목은 비워둘 수 없습니다.")
        String title,

        @NotNull(message = "공개 유형(type)은 필수입니다.")
        Visibility visibility
) {
}
