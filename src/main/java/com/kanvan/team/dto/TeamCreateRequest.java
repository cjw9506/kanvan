package com.kanvan.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Schema(description = "팀 생성 요청")
@Getter
@NoArgsConstructor
public class TeamCreateRequest {

    @Schema(description = "팀명")
    @NotBlank(message = "팀명은 필수입니다.")
    @Length(min = 2, max = 10)
    private String teamName;

    @Builder
    public TeamCreateRequest(String teamName) {
        this.teamName = teamName;
    }
}
