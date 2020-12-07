package pl.asbt.moviesfrontend.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.asbt.moviesfrontend.config.MoviesStorageConfig;
import pl.asbt.moviesfrontend.dto.CartDto;
import pl.asbt.moviesfrontend.dto.OrderDto;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class CartClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartClient.class);
    private static CartClient cartClient;
    private RestTemplate restTemplate = new RestTemplate();

    private CartClient() {
    }

    public static CartClient getInstance() {
        if (cartClient == null) {
            cartClient = new CartClient();
        }
        return cartClient;
    }

//    public void createCart(CartDto cartDto) {
//        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.CARTS_API_END_POINT)
//                .build()
//                .encode()
//                .toUri();
//        try {
//            restTemplate.postForObject(url, cartDto, CartDto.class);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//        }
//    }

    public CartDto getCart(Long cartId) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.CARTS_API_END_POINT)
                .pathSegment(cartId.toString())
                .build()
                .encode()
                .toUri();
        try {
            CartDto cartDto = restTemplate.getForObject(url, CartDto.class);
            return cartDto;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new CartDto();
    }

    public void addItem(Long cartId, Long itemId) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.CARTS_API_END_POINT)
                .pathSegment("add-item")
                .pathSegment(cartId.toString())
                .pathSegment(itemId.toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.postForObject(url, new CartDto(), CartDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteItem(Long cartId, Long itemId) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.CARTS_API_END_POINT)
                .pathSegment("delete-item")
                .pathSegment(cartId.toString())
                .pathSegment(itemId.toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.postForObject(url, new CartDto(), CartDto.class);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void updateCarPrice(Long cartId) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.CARTS_API_END_POINT)
                .pathSegment("update-price")
                .path(cartId.toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.postForObject(url, new CartDto(), CartDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteCart(CartDto cartDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.CARTS_API_END_POINT)
                .pathSegment(cartDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.delete(url);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void createOrder(Long cardId, Long userId) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.CARTS_API_END_POINT)
                .pathSegment("create-order")
                .pathSegment(cardId.toString())
                .pathSegment(userId.toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.postForObject(url, new OrderDto(), OrderDto[].class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

/*    public List<CartDto> filterCarts(String filter) {
        String filterLowerCase = filter.toLowerCase();
        List<CartDto> cartsDto = getCarts();
        List<CartDto> cartsFiltered = cartsDto.stream()
                .filter(cartDto -> cartDto.getId().toString().equals(filter))
                .collect(Collectors.toList());
        return cartsFiltered;
    }*/
}
