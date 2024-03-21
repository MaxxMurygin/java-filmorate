package com.github.maxxmurygin.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Component
@Scope("prototype")
@Data
@Builder
public class Mpa {
    @NotNull
    @Range(min = 1, max = 6)
    private int id;
    @NotBlank
    private String name;
}
