package com.github.maxxmurygin.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Component
@Scope("prototype")
@Data
@Builder
public class Genres {
    @NotNull
    @Positive
    private Integer id;
    @NotBlank
    private String name;
}
