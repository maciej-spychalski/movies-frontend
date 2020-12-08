package pl.asbt.moviesfrontend.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.asbt.moviesfrontend.config.MoviesStorageConfig;
import pl.asbt.moviesfrontend.dto.PasswordDto;

import java.net.URI;

public class PasswordClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordClient.class);
    private static PasswordClient passwordClient;
    private RestTemplate restTemplate = new RestTemplate();

    private PasswordClient() {}

    public static PasswordClient getInstance() {
        if (passwordClient == null) {
            passwordClient = new PasswordClient();
        }
        return passwordClient;
    }

    public PasswordDto getPassword() {
        PasswordDto passwordDto;
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.PASSWORD_GENERATOR_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            passwordDto = restTemplate.getForObject(url, PasswordDto.class);
            return passwordDto;
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new PasswordDto();
        }
    }
}
