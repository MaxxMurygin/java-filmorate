package com.github.maxxmurygin.filmorate.validators;

import com.github.maxxmurygin.filmorate.exeptions.FilmValidationException;
import com.github.maxxmurygin.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator implements Validator<Film> {
    private static final int DESCRIPTION_LENGTH = 200;
    private static final LocalDate MINIMUM_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public void validate(Film film) {

        if (film.getDescription().length() > DESCRIPTION_LENGTH) {
            throw new FilmValidationException("Слишком длинное описание");
        }
        if (film.getReleaseDate().isBefore(MINIMUM_RELEASE_DATE)) {
            throw new FilmValidationException("Братья Люмьер еще не сделали это");
        }
    }
}
