package pl.asbt.moviesfrontend.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.asbt.moviesfrontend.config.MoviesStorageConfig;
import pl.asbt.moviesfrontend.dto.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class MovieClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieClient.class);
    private static MovieClient movieClient;
    private RestTemplate restTemplate = new RestTemplate();

    private MovieClient() {}

    public static MovieClient getInstance() {
        if (movieClient == null) {
            movieClient = new MovieClient();
        }
        return movieClient;
    }

    public void createMovie(MovieDto movieDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.MOVIES_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.postForObject(url, movieDto, MovieDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<MovieDto> getMovies() {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.MOVIES_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            MovieDto[] moviesDto = restTemplate.getForObject(url, MovieDto[].class);
            return Arrays.asList(ofNullable(moviesDto).orElse(new MovieDto[0]));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public void updateMovie(MovieDto movieDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.MOVIES_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.put(url, movieDto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteMovie(MovieDto movieDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.MOVIES_API_END_POINT)
                .pathSegment(movieDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.delete(url);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<MovieDto> filterMovies(String filter) {
        String filterLowerCase = filter.toLowerCase();
        List<MovieDto> moviesDto = getMovies();
        List<MovieDto> moviesFiltered = moviesDto.stream()
                .filter(movieDto -> movieDto.getTitle().toLowerCase().contains(filterLowerCase) ||
                        movieDto.getDirectorDto().getSurname().toLowerCase().contains(filterLowerCase))
                .collect(Collectors.toList());
        return moviesFiltered;
    }

    public void addDirector(MovieDto movieDto, DirectorDto directorDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.MOVIES_API_END_POINT)
                .pathSegment(movieDto.getId().toString())
                .pathSegment("add-director")
                .pathSegment(directorDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.postForObject(url, directorDto, DirectorDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteWriter(MovieDto movieDto, WriterDto writerDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.MOVIES_API_END_POINT)
                .pathSegment(movieDto.getId().toString())
                .pathSegment("remove-writer")
                .pathSegment(writerDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.postForObject(url, writerDto, WriterDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void addWriter(MovieDto movieDto, WriterDto writerDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.MOVIES_API_END_POINT)
                .pathSegment(movieDto.getId().toString())
                .pathSegment("add-writer")
                .pathSegment(writerDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.postForObject(url, writerDto, WriterDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteActor(MovieDto movieDto, ActorDto actorDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.MOVIES_API_END_POINT)
                .pathSegment(movieDto.getId().toString())
                .pathSegment("remove-actor")
                .pathSegment(actorDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.postForObject(url, actorDto, ActorDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void addActor(MovieDto movieDto, ActorDto actorDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.MOVIES_API_END_POINT)
                .pathSegment(movieDto.getId().toString())
                .pathSegment("add-actor")
                .pathSegment(actorDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.postForObject(url, actorDto, ActorDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteGenre(MovieDto movieDto, GenreDto genreDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.MOVIES_API_END_POINT)
                .pathSegment(movieDto.getId().toString())
                .pathSegment("remove-genre")
                .pathSegment(genreDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.postForObject(url, genreDto, GenreDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void addGenre(MovieDto movieDto, GenreDto genreDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.MOVIES_API_END_POINT)
                .pathSegment(movieDto.getId().toString())
                .pathSegment("add-genre")
                .pathSegment(genreDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.postForObject(url, genreDto, GenreDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
