package pl.asbt.moviesfrontend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long userId;
    private String firstname;
    private String surname;
    private String email;
    private String password;
    private Boolean isAdmin;
    private Boolean isLogged;
    private Long cartId;
    private List<Long> ordersID = new ArrayList<>();

}