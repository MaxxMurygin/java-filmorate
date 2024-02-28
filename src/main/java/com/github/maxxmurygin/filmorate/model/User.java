package com.github.maxxmurygin.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;
    private @NotBlank @Email String email;
    private @NotBlank String login;
    private String name;
    private @NotNull @PastOrPresent LocalDate birthday;
    private Set<Integer> friends;
}
