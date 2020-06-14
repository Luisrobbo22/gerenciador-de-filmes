package br.com.luisrobbo.gerenciadorDeFilme.services;

import br.com.luisrobbo.gerenciadorDeFilme.model.Movie;
import br.com.luisrobbo.gerenciadorDeFilme.repository.MovieRepository;
import br.com.luisrobbo.gerenciadorDeFilme.services.exeptions.DataIntegretyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;

    public List<Movie> find(String title) {
        return repository.findAllByTitle(title);
    }

    public List<Movie> findAllMovies(String title,Integer page,Integer perPage,Integer total, Integer totalPages,String sortBy ){
        Pageable paging = PageRequest.of(page,perPage, Sort.by(sortBy));
        Page<Movie> pageResult = repository.findAll(paging);

        if (pageResult.hasContent()){
            return pageResult.getContent();
        }else{
            return new ArrayList<Movie>();
        }
    }

    public List<Movie> findAll() {
        return repository.findAll();
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
        } else if (!movie.getImdbID().equals(nMovie.getImdbID())){
            movie = repository.save(movie);
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

    public Page<Movie> findPage(String title,Integer page, Integer perPage){
        List<Movie> movies = repository.findAllByTitle(title);
        PageRequest pageRequest = PageRequest.of(page, perPage);
        return repository.findAll(pageRequest);
    }

}
