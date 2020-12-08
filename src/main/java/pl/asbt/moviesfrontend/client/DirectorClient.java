package pl.asbt.moviesfrontend.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.asbt.moviesfrontend.config.MoviesStorageConfig;
import pl.asbt.moviesfrontend.dto.DirectorDto;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class DirectorClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectorClient.class);
    private static DirectorClient directorClient;
    private RestTemplate restTemplate = new RestTemplate();

    private DirectorClient() {}

    public static DirectorClient getInstance() {
        if (directorClient == null) {
            directorClient = new DirectorClient();
        }
        return directorClient;
    }

    public void createDirector(DirectorDto directorDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.DIRECTORS_API_END_POINT)
                .build()
                .encode()
                .toUri();

        try {
            restTemplate.postForObject(url, directorDto, DirectorDto.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<DirectorDto> getDirectors() {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.DIRECTORS_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            DirectorDto[] directorsDto = restTemplate.getForObject(url, DirectorDto[].class);
            return Arrays.asList(ofNullable(directorsDto).orElse(new DirectorDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public void updateDirector(DirectorDto directorDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.DIRECTORS_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.put(url, directorDto);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteDirector(DirectorDto directorDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.DIRECTORS_API_END_POINT)
                .pathSegment(directorDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.delete(url);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<DirectorDto> filterDirectors(String filter) {
        String filterLowerCase = filter.toLowerCase();
        List<DirectorDto> directorsDto = getDirectors();
        List<DirectorDto> directorsFiltered = directorsDto.stream()
                .filter(directorDto -> directorDto.getFirstname().toLowerCase().contains(filterLowerCase) ||
                        directorDto.getSurname().toLowerCase().contains(filterLowerCase))
                .collect(Collectors.toList());
        return directorsFiltered;
    }
}
