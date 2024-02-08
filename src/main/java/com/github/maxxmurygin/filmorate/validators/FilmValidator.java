package com.github.maxxmurygin.filmorate.validators;

import com.github.maxxmurygin.filmorate.exeptions.FilmValidationException;
import com.github.maxxmurygin.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator implements Validator<Film> {

    @Override
    public void validate(Film film) {
        int DESCRIPTION_LENGTH = 200;
        LocalDate MINIMUM_RELEASE_DATE = LocalDate.of(1895, 12, 28);
        if (film.getDescription().length() > DESCRIPTION_LENGTH) {
            throw new FilmValidationException("Слишком длинное описание");
        }
        if (film.getReleaseDate().isBefore(MINIMUM_RELEASE_DATE)){
            throw new FilmValidationException("Братья Люмьер еще не сделали это");
        }
    }
}
