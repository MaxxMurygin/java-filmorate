package com.github.maxxmurygin.filmorate.service;

import com.github.maxxmurygin.filmorate.exeptions.FilmAlreadyExistException;
import com.github.maxxmurygin.filmorate.exeptions.FilmNotExistException;
import com.github.maxxmurygin.filmorate.exeptions.UserNotExistException;
import com.github.maxxmurygin.filmorate.model.Film;
import com.github.maxxmurygin.filmorate.repository.FilmRepository;
import com.github.maxxmurygin.filmorate.repository.LikesRepository;
import com.github.maxxmurygin.filmorate.validators.FilmValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultFilmService implements FilmService {
    private final FilmRepository filmRepository;
    private final LikesRepository likesRepository;
    private final FilmValidator validator;
    private final UserService userService;


    @Override
    public Film create(Film film) {
        Film existing = filmRepository.findById(film.getId());

        if (existing != null) {
            throw new FilmAlreadyExistException(String.format(
                    "Фильм %s с ID %d уже существует", film.getName(), film.getId()));
        }
        validator.validate(film);
        return filmRepository.create(film);
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
            throw new UserNotExistException(String.format(
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
            throw new FilmNotExistException("Фильм не найден");
        }
        return f;
    }

    @Override
    public List<Film> getPopular(Integer count) {
        List<Integer> likesFromRepository = likesRepository.getPopular(count);

        if (likesFromRepository.isEmpty()) {
            return filmRepository.findAll()
                    .stream()
                    .limit(count)
                    .collect(Collectors.toList());
        }
        return likesFromRepository
                .stream()
                .map(filmRepository::findById)
                .collect(Collectors.toList());
    }
}
