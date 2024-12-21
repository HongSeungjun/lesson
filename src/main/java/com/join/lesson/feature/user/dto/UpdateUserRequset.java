package com.join.lesson.feature.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class UpdateUserRequset {


    @NotBlank
    private String comment;

    @NotBlank
    @Size(min = 13, max = 13)
    private String tel;

    @Builder
    public UpdateUserRequset(@NotBlank String comment, @NotBlank @Size(min = 13, max = 13) String tel) {
        this.comment = comment;
        this.tel = tel;
    }
}
