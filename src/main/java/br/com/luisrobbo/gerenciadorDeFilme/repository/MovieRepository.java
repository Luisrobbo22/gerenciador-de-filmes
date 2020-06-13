package br.com.luisrobbo.gerenciadorDeFilme.repository;

import br.com.luisrobbo.gerenciadorDeFilme.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    @Query("SELECT m FROM Movie m WHERE m.title like %:title% ")
    List<Movie> findAllByTitle(String title);

    Movie findByImdbID(String imdbID);
}
