package com.github.maxxmurygin.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Set;

@Component
@Scope("prototype")
@Data
@Builder
public class Film {
    private int id;
    private @NotBlank String name;
    private String description;
    private @NotNull LocalDate releaseDate;
    private @NotNull @Positive int duration;
    private Set<Genre> genre;
    private Mpa rating;
}
