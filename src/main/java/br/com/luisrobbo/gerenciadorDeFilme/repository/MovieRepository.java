package br.com.luisrobbo.gerenciadorDeFilme.repository;

import br.com.luisrobbo.gerenciadorDeFilme.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Page<List<Movie>> findAllByTitleContains(String title, Pageable pageable);

    Movie findByImdbID(String imdbID);

}
