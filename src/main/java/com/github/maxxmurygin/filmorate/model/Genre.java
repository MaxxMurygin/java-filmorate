package com.github.maxxmurygin.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class Genre {
    @NotNull @Positive Integer id;
    @NotBlank String name;
}
