package pl.asbt.moviesfrontend.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {

    private Long id;
    private String firstname;
    private String surname;
    private String email;
    private String password;
    private Boolean isAdmin;
    private Boolean isLogged;
    private CartDto cartDto;
    private List<OrderDto> ordersDto = new ArrayList<>();

    public UserDto(Long id, String firstname, String surname, String email,
                   String password, Boolean isAdmin, Boolean isLogged) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isLogged = isLogged;
    }
}
