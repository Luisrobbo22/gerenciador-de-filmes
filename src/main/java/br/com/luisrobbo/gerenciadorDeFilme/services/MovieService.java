package br.com.luisrobbo.gerenciadorDeFilme.services;

import br.com.luisrobbo.gerenciadorDeFilme.handler.GerenciadorFilmesException;
import br.com.luisrobbo.gerenciadorDeFilme.model.Movie;
import br.com.luisrobbo.gerenciadorDeFilme.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;

    public Page<List<Movie>> find(String title, Pageable pageable) {
        return repository.findAllByTitleContains(title, pageable);
    }

    public Slice<Movie> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    private void updateData(Movie newMovie, Movie movie) {
        newMovie.setTitle(movie.getTitle());
        newMovie.setYear(movie.getYear());
    }

    public Movie update(Movie movie, String imdbID) {
        Movie newMovie = repository.findByImdbID(imdbID);
        updateData(newMovie, movie);
        return repository.save(newMovie);
    }

    @Transactional
    public Movie insert(Movie movie) throws GerenciadorFilmesException {
        Movie nMovie = repository.findByImdbID(movie.getImdbID());
        if (nMovie == null) {
            movie = repository.saveAndFlush(movie);
        } else {
            throw new GerenciadorFilmesException("Filme já cadastrado");
        }
        return movie;
    }

    public void delete(String imdbID) throws GerenciadorFilmesException {
        Movie movie = repository.findByImdbID(imdbID);
        try {
            repository.deleteById(movie.getId());
        } catch (DataIntegrityViolationException e) {
            throw new GerenciadorFilmesException("Não há Filme para ser deletado");
        }
    }

    public Page<List<Movie>> findPage(String title, Integer page, Integer perPage) {
        PageRequest pageRequest = PageRequest.of(page, perPage);
        Page<List<Movie>> movies = find(title, pageRequest);

        return movies;
    }

}
