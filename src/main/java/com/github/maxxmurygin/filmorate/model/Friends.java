package com.github.maxxmurygin.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
public class Friends {
    private @NotNull @Positive int followingUserId;
    private @NotNull @Positive int followedUserid;
}
