package pl.asbt.moviesfrontend.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.asbt.moviesfrontend.config.MoviesStorageConfig;
import pl.asbt.moviesfrontend.dto.WriterDto;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class WriterClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(WriterClient.class);
    private static WriterClient writerClient;
    private RestTemplate restTemplate = new RestTemplate();

    private WriterClient() {}

    public static WriterClient getInstance() {
        if (writerClient == null) {
            writerClient = new WriterClient();
        }
        return writerClient;
    }

    public void createWriter(WriterDto writerDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.WRITERS_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.postForObject(url, writerDto, WriterDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<WriterDto> getWriters() {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.WRITERS_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            WriterDto[] writersDto = restTemplate.getForObject(url, WriterDto[].class);
            return Arrays.asList(ofNullable(writersDto).orElse(new WriterDto[0]));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public void updateWriter(WriterDto writerDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.WRITERS_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.put(url, writerDto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteWriter(WriterDto writerDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.WRITERS_API_END_POINT)
                .pathSegment(writerDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.delete(url);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<WriterDto> filterWriters(String filter) {
        String filterLowerCase = filter.toLowerCase();
        List<WriterDto> writersDto = getWriters();
        List<WriterDto> writersFiltered = writersDto.stream()
                .filter(writerDto -> writerDto.getFirstname().toLowerCase().contains(filterLowerCase) ||
                        writerDto.getSurname().toLowerCase().contains(filterLowerCase))
                .collect(Collectors.toList());
        return writersFiltered;
    }
}
