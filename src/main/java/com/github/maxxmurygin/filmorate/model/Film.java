package com.github.maxxmurygin.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;
    private @NotBlank String name;
    private String description;
    private @NotNull LocalDate releaseDate;
    private @Positive int duration;
    private Set<Integer> likes;
}
