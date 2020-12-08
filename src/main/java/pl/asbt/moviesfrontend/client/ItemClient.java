package pl.asbt.moviesfrontend.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.asbt.moviesfrontend.config.MoviesStorageConfig;
import pl.asbt.moviesfrontend.dto.ItemDto;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class ItemClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemClient.class);
    private static ItemClient itemClient;
    private RestTemplate restTemplate = new RestTemplate();

    private ItemClient() {}

    public static ItemClient getInstance() {
        if (itemClient == null) {
            itemClient = new ItemClient();
        }
        return itemClient;
    }

    public ItemDto createItem(ItemDto itemDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.ITEMS_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            return restTemplate.postForObject(url, itemDto, ItemDto.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ItemDto();
    }

    public List<ItemDto> getItems() {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.ITEMS_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            ItemDto[] itemsDto = restTemplate.getForObject(url, ItemDto[].class);
            return Arrays.asList(ofNullable(itemsDto).orElse(new ItemDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public void updateItem(ItemDto itemDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.ITEMS_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.put(url, itemDto);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteItem(ItemDto itemDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.ITEMS_API_END_POINT)
                .pathSegment(itemDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.delete(url);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<ItemDto> filterItem(String filter) {
        String filterLowerCase = filter.toLowerCase();
        List<ItemDto> itemsDto = getItems();
        List<ItemDto> itemsFiltered = itemsDto.stream()
                .filter(itemDto -> itemDto.getMovieTitle().toLowerCase().contains(filterLowerCase))
                .collect(Collectors.toList());
        return itemsFiltered;
    }
}
