package com.github.maxxmurygin.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
public class Genres {
    @NotNull
    @Positive
    private Integer id;
    @NotBlank
    private String name;
}
