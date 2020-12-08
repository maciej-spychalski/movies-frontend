package pl.asbt.moviesfrontend.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.asbt.moviesfrontend.config.MoviesStorageConfig;
import pl.asbt.moviesfrontend.dto.ActorDto;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class ActorClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActorClient.class);
    private static ActorClient actorClient;
    private RestTemplate restTemplate = new RestTemplate();

    private ActorClient() {}

    public static ActorClient getInstance() {
        if (actorClient == null) {
            actorClient = new ActorClient();
        }
        return actorClient;
    }

    public void createActor(ActorDto actorDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.ACTORS_API_EDN_POINT)
                .build()
                .encode()
                .toUri();

        try {
            restTemplate.postForObject(url, actorDto, ActorDto.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<ActorDto> getActors() {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.ACTORS_API_EDN_POINT)
                .build()
                .encode()
                .toUri();
        try {
            ActorDto[] actorsDto = restTemplate.getForObject(url, ActorDto[].class);
            return Arrays.asList(ofNullable(actorsDto).orElse(new ActorDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public void updateActor(ActorDto actorDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.ACTORS_API_EDN_POINT)
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.put(url, actorDto);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteActor(ActorDto actorDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.ACTORS_API_EDN_POINT)
                .pathSegment(actorDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.delete(url);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<ActorDto> filterActors(String filter) {
        String filterLowerCase = filter.toLowerCase();
        List<ActorDto> actorsDto = getActors();
        List<ActorDto> actorsFiltered = actorsDto.stream()
                .filter(actorDto -> actorDto.getFirstname().toLowerCase().contains(filterLowerCase) ||
                        actorDto.getSurname().toLowerCase().contains(filterLowerCase) )
                .collect(Collectors.toList());
        return actorsFiltered;
    }
}
