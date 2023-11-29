package com.kanvan.team.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class TeamCreateRequest {

    @NotBlank(message = "팀명은 필수입니다.")
    @Length(min = 2, max = 10)
    private String teamName;
}
