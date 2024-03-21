package com.github.maxxmurygin.filmorate.service;

import com.github.maxxmurygin.filmorate.exeptions.FilmNotExistException;
import com.github.maxxmurygin.filmorate.exeptions.GenreNotExistException;
import com.github.maxxmurygin.filmorate.exeptions.UserNotExistException;
import com.github.maxxmurygin.filmorate.model.Film;
import com.github.maxxmurygin.filmorate.model.Genres;
import com.github.maxxmurygin.filmorate.model.Mpa;
import com.github.maxxmurygin.filmorate.repository.FilmRepository;
import com.github.maxxmurygin.filmorate.repository.GenreRepository;
import com.github.maxxmurygin.filmorate.repository.LikesRepository;
import com.github.maxxmurygin.filmorate.repository.MpaRepository;
import com.github.maxxmurygin.filmorate.validators.FilmValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultFilmService implements FilmService {
    private final FilmRepository filmRepository;
    private final LikesRepository likesRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;
    private final FilmValidator validator;
    private final UserService userService;


    @Override
    public Film create(Film film) {
        Mpa mpa = film.getMpa();

        if (mpa == null) {
            film.setMpa(Mpa.builder()
                    .id(1)
                    .name("G")
                    .build());
        }
        Film f = filmRepository.create(film);

        film.setId(f.getId());
        if (film.getGenres() != null) {
            if (!film.getGenres().isEmpty()) {
                genreRepository.addToFilm(film.getId(), film.getGenres());
            }
        }



        validator.validate(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Film stored = filmRepository.findById(film.getId());

        if (stored == null) {
            throw new FilmNotExistException(String.format(
                    "Фильма %s с ID %d не существует", film.getName(), film.getId()));
        }
        validator.validate(film);
        return filmRepository.update(film);
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        if (userService.findById(userId) == null) {
            throw new UserNotExistException(String.format(
                    "Пользователь c ID = %d не найден", userId));
        }
        if (filmRepository.findById(filmId) == null) {
            throw new UserNotExistException(String.format(
                    "Фильм c ID = %d не найден", filmId));
        }
        likesRepository.addLike(filmId, userId);
        return filmRepository.findById(filmId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        if (userService.findById(userId) == null) {
            throw new UserNotExistException(String.format(
                    "Пользователь c ID = %d не найден", userId));
        }
        if (filmRepository.findById(filmId) == null) {
            throw new FilmNotExistException(String.format(
                    "Фильм c ID = %d не найден", filmId));
        }
        likesRepository.removeLike(filmId, userId);
    }

    @Override
    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    @Override
    public Film findById(Integer id) {
        Film f = filmRepository.findById(id);

        if (f == null) {
            throw new FilmNotExistException(String.format(
                    "Фильм c ID = %d не найден", id));
        }
        f.setGenres(new HashSet<>(genreRepository.findByFilm(id)));
        return f;
    }

    @Override
    public Genres findGenreById(Integer id) {
        Genres g = genreRepository.findById(id);

        if (g == null) {
            throw new GenreNotExistException(String.format(
                    "Жанр c ID = %d не найден", id));
        }
        return g;
    }

    @Override
    public List<Genres> findGenreAll() {
        return genreRepository.findAll();
    }

    @Override
    public Mpa findMpaById(Integer id) {
        Mpa mpa = mpaRepository.findById(id);

        if (mpa == null) {
            throw new GenreNotExistException(String.format(
                    "Рейтинг MPA c ID = %d не найден", id));
        }
        return mpa;
    }

    @Override
    public List<Mpa> findMpaAll() {
        return mpaRepository.findAll();
    }

    @Override
    public List<Film> getPopular(Integer count) {
        return filmRepository.getPopular(count);
    }
}
