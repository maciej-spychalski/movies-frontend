package pl.asbt.moviesfrontend.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.asbt.moviesfrontend.config.MoviesStorageConfig;
import pl.asbt.moviesfrontend.dto.StorageItemDto;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class StorageItemClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageItemClient.class);
    private static StorageItemClient storageItemClient;
    private RestTemplate restTemplate = new RestTemplate();

    private StorageItemClient() {}

    public static StorageItemClient getInstance() {
        if (storageItemClient == null) {
            storageItemClient = new StorageItemClient();
        }
        return storageItemClient;
    }

    public void createStorageItem(StorageItemDto storageItemDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.STORAGE_ITEM_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.postForObject(url, storageItemDto, StorageItemDto.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<StorageItemDto> getStorageItems() {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.STORAGE_ITEM_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            StorageItemDto[] storageItemsDto = restTemplate.getForObject(url, StorageItemDto[].class);
            return Arrays.asList(ofNullable(storageItemsDto).orElse(new StorageItemDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public void updateStorageItem(StorageItemDto storageItemDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.STORAGE_ITEM_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.put(url, storageItemDto);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteStorageItem(StorageItemDto storageItemDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.STORAGE_ITEM_API_END_POINT)
                .pathSegment(storageItemDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.delete(url);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<StorageItemDto> filterStorageItems(String filter) {
        String filterLowerCase = filter.toLowerCase();
        List<StorageItemDto> storageItemsDto = getStorageItems();
        List<StorageItemDto> storageItemFiltered = storageItemsDto.stream()
                .filter(storageItemDto -> storageItemDto.getMovieTitle().toLowerCase().contains(filterLowerCase))
                .collect(Collectors.toList());
        return storageItemFiltered;
    }
}
