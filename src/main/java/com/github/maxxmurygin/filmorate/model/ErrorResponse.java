package com.github.maxxmurygin.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String error;

}
