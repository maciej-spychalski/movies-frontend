package pl.asbt.moviesfrontend.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.asbt.moviesfrontend.config.MoviesStorageConfig;
import pl.asbt.moviesfrontend.dto.GenreDto;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class GenreClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenreClient.class);
    private static GenreClient genreClient;
    private RestTemplate restTemplate = new RestTemplate();

    private GenreClient() {}

    public static GenreClient getInstance() {
        if (genreClient == null) {
            genreClient = new GenreClient();
        }
        return genreClient;
    }

    public void createGenre(GenreDto genreDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.GENRES_API_END_POINT)
                .build()
                .encode()
                .toUri();

        try {
            restTemplate.postForObject(url, genreDto, GenreDto.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<GenreDto> getGenres() {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.GENRES_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            GenreDto[] genresDto = restTemplate.getForObject(url, GenreDto[].class);
            return Arrays.asList(ofNullable(genresDto).orElse(new GenreDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public void updateGenre(GenreDto genreDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.GENRES_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.put(url, genreDto);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteGenre(GenreDto genreDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.GENRES_API_END_POINT)
                .pathSegment(genreDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.delete(url);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<GenreDto> filterGenres(String filter) {
        String filterLowerCase = filter.toLowerCase();
        List<GenreDto> genresDto = getGenres();
        List<GenreDto> genresFiltered = genresDto.stream()
                .filter(genreDto -> genreDto.getType().toLowerCase().contains(filterLowerCase))
                .collect(Collectors.toList());
        return genresFiltered;
    }
}
