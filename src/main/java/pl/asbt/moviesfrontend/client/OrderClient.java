package pl.asbt.moviesfrontend.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.asbt.moviesfrontend.config.MoviesStorageConfig;
import pl.asbt.moviesfrontend.dto.OrderDto;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class OrderClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderClient.class);
    private static OrderClient orderClient;
    private RestTemplate restTemplate = new RestTemplate();

    private OrderClient() {}

    public static OrderClient getInstance() {
        if (orderClient == null) {
            orderClient = new OrderClient();
        }
        return orderClient;
    }

    public void finalizeOrder(Long orderId) {
        OrderDto orderDto = new OrderDto();
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.ORDERS_API_END_POINT)
                .pathSegment("finalize")
                .pathSegment(orderId.toString())
                .build()
                .encode()
                .toUri();
        //        String url = moviesStorageConfig.getUsersApiEndpoint();
        try {
            restTemplate.postForObject(url, orderDto, OrderDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public OrderDto getOrder(Long orderId) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.ORDERS_API_END_POINT)
                .pathSegment(orderId.toString())
                .build()
                .encode()
                .toUri();
        try {
            OrderDto orderDto = restTemplate.getForObject(url, OrderDto.class);
            return orderDto;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new OrderDto();
    }

    public void updateOrder(OrderDto orderDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.ORDERS_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.put(url, orderDto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteOrder(OrderDto orderDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.ORDERS_API_END_POINT)
                .pathSegment(orderDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.delete(url);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

//    public List<OrderDto> filterOrders(String filter) {
//        String filterLowerCase = filter.toLowerCase();
//        List<OrderDto> ordersDto = getOrders();
//        List<OrderDto> ordersFiltered = ordersDto.stream()
//                .filter(orderDto -> orderDto.getIsFinalized().toString().toLowerCase().contains(filterLowerCase))
//                .collect(Collectors.toList());
//        return ordersFiltered;
//    }
}
