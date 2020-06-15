package br.com.luisrobbo.gerenciadorDeFilme.services;

import br.com.luisrobbo.gerenciadorDeFilme.model.Movie;
import br.com.luisrobbo.gerenciadorDeFilme.repository.MovieRepository;
import br.com.luisrobbo.gerenciadorDeFilme.services.exeptions.DataIntegretyException;
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
    public Movie insert(Movie movie) {
        Movie nMovie = repository.findByImdbID(movie.getImdbID());
        if (nMovie == null) {
            movie = repository.saveAndFlush(movie);
        } else {
            throw new DataIntegretyException("Filme já cadastrado");
        }
        return movie;
    }

    public void delete(String imdbID) {
        Movie movie = repository.findByImdbID(imdbID);
        try {
            repository.deleteById(movie.getId());
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegretyException("Não há Filme para ser deletado");
        }
    }

    public Page<List<Movie>> findPage(String title, Integer page, Integer perPage) {
        PageRequest pageRequest = PageRequest.of(page, perPage);
        Page<List<Movie>> movies = find(title, pageRequest);

        return movies;
    }

}
