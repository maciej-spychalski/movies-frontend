package pl.asbt.moviesfrontend.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.asbt.moviesfrontend.config.MoviesStorageConfig;
import pl.asbt.moviesfrontend.dto.UserDto;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class UserClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserClient.class);
    private static UserClient userClient;
    private RestTemplate restTemplate = new RestTemplate();

    private UserClient() {
    }

    public static UserClient getInstance() {
        if (userClient == null) {
            userClient = new UserClient();
        }
        return userClient;
    }

    public void createUser(UserDto userDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.USERS_API_END_POINT)
                .build()
                .encode()
                .toUri();

        try {
            restTemplate.postForObject(url, userDto, UserDto.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public UserDto getUser(Long userId) {
        UserDto userDto = new UserDto();
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.USERS_API_END_POINT)
                .pathSegment(userId.toString())
                .build()
                .encode()
                .toUri();

        try {
            userDto = restTemplate.getForObject(url, UserDto.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return userDto;
    }

    public List<UserDto> getUsers() {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.USERS_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            UserDto[] usersDto = restTemplate.getForObject(url, UserDto[].class);
            return Arrays.asList(ofNullable(usersDto).orElse(new UserDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public void updateUser(UserDto userDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.USERS_API_END_POINT)
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.put(url, userDto);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteUser(UserDto userDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.USERS_API_END_POINT)
                .pathSegment(userDto.getId().toString())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.delete(url);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<UserDto> filterUsers(String filter) {
        String filterLowerCase = filter.toLowerCase();
        List<UserDto> usersDto = getUsers();
        List<UserDto> usersFiltered = usersDto.stream()
                .filter(userDto -> userDto.getFirstname().toLowerCase().contains(filterLowerCase) ||
                        userDto.getSurname().toLowerCase().contains(filterLowerCase) ||
                        userDto.getEmail().toLowerCase().contains(filterLowerCase))
                .collect(Collectors.toList());
        return usersFiltered;
    }

    public UserDto loginUser(String email, String password) {
        URI url = UriComponentsBuilder.fromHttpUrl(MoviesStorageConfig.USERS_API_END_POINT)
                .pathSegment("login")
                .pathSegment(email)
                .pathSegment(password)
                .build()
                .encode()
                .toUri();
        try {
            UserDto userDto = restTemplate.postForObject(url, null, UserDto.class);
            return userDto;
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new UserDto();
    }
}
