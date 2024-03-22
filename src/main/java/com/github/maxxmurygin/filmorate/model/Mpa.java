package com.github.maxxmurygin.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Mpa {
    @NotNull
    @Range(min = 1, max = 6)
    private int id;
    @NotBlank
    private String name;
}
