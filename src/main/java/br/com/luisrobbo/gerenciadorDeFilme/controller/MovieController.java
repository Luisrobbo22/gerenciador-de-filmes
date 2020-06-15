package br.com.luisrobbo.gerenciadorDeFilme.controller;

import br.com.luisrobbo.gerenciadorDeFilme.model.Movie;
import br.com.luisrobbo.gerenciadorDeFilme.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/movies")
public class MovieController {

    @Autowired
    private MovieService service;


    @GetMapping(value = "/{title}")
    public ResponseEntity<Page<List<Movie>>> find(@PathVariable String title, Pageable pageable) {
        return ResponseEntity.ok().body(service.find(title,pageable));

    }

    @GetMapping(value = "/")
    public ResponseEntity<Slice<Movie>> findAll(Pageable pageable) {
        return ResponseEntity.ok().body(service.findAll(pageable));
    }


    @PutMapping(value = "/{imdbID}")
    public ResponseEntity<Void> update(@PathVariable String imdbID, @RequestBody Movie newMovie) {
        Movie movie = new Movie(newMovie.getTitle(), newMovie.getImdbID(), newMovie.getYear());
        movie = service.update(movie, imdbID);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody Movie newMovie) {
        Movie movie = new Movie(newMovie.getTitle(), newMovie.getImdbID(), newMovie.getYear());
        movie = service.insert(movie);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{title}").buildAndExpand(movie.getTitle()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(value = "/{imdbID}")
    public ResponseEntity<Void> delete(@PathVariable String imdbID) {
        service.delete(imdbID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/search/")
    public ResponseEntity<Page<List<Movie>>> findPage(
            @RequestParam(value = "title", defaultValue = "") String title,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "per_page", defaultValue = "10") Integer perPage,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        Page<List<Movie>> list = service.findPage(title, page, perPage);

        return ResponseEntity.ok().body(list);
    }

}
