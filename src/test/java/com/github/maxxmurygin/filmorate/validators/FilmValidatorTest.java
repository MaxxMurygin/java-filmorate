package com.github.maxxmurygin.filmorate.validators;

import com.github.maxxmurygin.filmorate.exeptions.FilmValidationException;
import com.github.maxxmurygin.filmorate.model.Film;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmValidatorTest {
    private final FilmValidator validator = new FilmValidator();
    private final Film film = Film.builder()
            .name("Film")
            .description("Адъ, трэшъ и содомiя")
            .releaseDate(LocalDate.of(2022, 1, 1))
            .duration(90)
            .build();

    @Test
    void validateWithEarlyAndBorderReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        validator.validate(film);

        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        FilmValidationException ex = assertThrows(FilmValidationException.class,
                () -> validator.validate(film));
        assertEquals("Братья Люмьер еще не сделали это", ex.getMessage());

    }

    @Test
    void validateWith200And201SymbolsDescription() {
        film.setDescription("200 symbols description 200 symbols description 200 symbols description " +
                "200 symbols description 200 symbols description 200 symbols description 200 symbols description " +
                "200 symbols description 200 symb");
        validator.validate(film);
        film.setDescription("200 symbols description 200 symbols description 200 symbols description " +
                "200 symbols description 200 symbols description 200 symbols description 200 symbols description " +
                "200 symbols description 200 symb+");
        FilmValidationException ex = assertThrows(FilmValidationException.class,
                () -> validator.validate(film));
        assertEquals("Слишком длинное описание", ex.getMessage());
    }

}