package pl.asbt.moviesfrontend.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.asbt.moviesfrontend.config.MoviesStorageConfig;
import pl.asbt.moviesfrontend.dto.DescriptionDto;

import java.net.URI;

public class DescriptionClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DescriptionClient.class);
    private static DescriptionClient descriptionClient;
    private RestTemplate restTemplate = new RestTemplate();

    private DescriptionClient() {}

    public static DescriptionClient getInstance() {
        if (descriptionClient == null) {
            descriptionClient = new DescriptionClient();
        }
        return descriptionClient;
    }

    public DescriptionDto getDescription(String movieTitle) {
        DescriptionDto descriptionDto;
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.DESCRIPTION_API_END_POINT)
                .pathSegment(movieTitle)
                .build()
                .encode()
                .toUri();
        try {
            descriptionDto = restTemplate.getForObject(url, DescriptionDto.class);
            return descriptionDto;
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new DescriptionDto();
        }
    }
}
