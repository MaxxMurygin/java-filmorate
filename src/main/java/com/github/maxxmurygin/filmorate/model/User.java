package com.github.maxxmurygin.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Component
@Scope("prototype")
@Data
@Builder
public class User {
    private int id;
    private @NotBlank @Email String email;
    private @NotBlank String login;
    private String name;
    private @NotNull @PastOrPresent LocalDate birthday;
}
