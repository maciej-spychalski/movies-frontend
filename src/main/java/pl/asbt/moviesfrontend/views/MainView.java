package pl.asbt.moviesfrontend.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import pl.asbt.moviesfrontend.client.UserClient;
import pl.asbt.moviesfrontend.domain.User;
import pl.asbt.moviesfrontend.dto.UserDto;
import pl.asbt.moviesfrontend.session.SessionVariables;

import java.util.List;
import java.util.stream.Collectors;

@Route("moviesShop")
public class MainView extends VerticalLayout {

    private UserClient userClient = UserClient.getInstance();

    public MainView() {
        SessionVariables.getInstance().setCurrentUser(new User());
        add(new Label("Movie Storage Application"));
        add(createUserLoginLayout());
    }

    private VerticalLayout createUserLoginLayout() {

        VerticalLayout userLoginLayout = new VerticalLayout();

        TextField textFieldLogin = new TextField();
        textFieldLogin.setLabel("User email");
        textFieldLogin.setPlaceholder("email");

        PasswordField passwordField = new PasswordField("Password");
        passwordField.setPlaceholder("password");

        Button buttonLogin = new Button("Login");
        buttonLogin.addClickListener(action -> {
            UserDto userDto = userClient.loginUser(textFieldLogin.getValue(), passwordField.getValue());
            if (userDto.getIsLogged() && userDto.getIsAdmin()) {
                setCurrentUser(userDto);
                UI.getCurrent().navigate(AdminView.class);
            } else if (userDto.getIsLogged() && !userDto.getIsAdmin()) {
                setCurrentUser(userDto);
                UI.getCurrent().navigate(ShopView.class);
            } else {
                setCurrentUser(new UserDto());
                Notification.show("Invalid login or password! Please try again.",
                        3000, Notification.Position.MIDDLE);
            }
        });

        Button buttonCreateNewUser = new Button("Create new user");
        buttonCreateNewUser.addClickListener(action -> UI.getCurrent().navigate(CreateNewUserView.class));

        userLoginLayout.add(textFieldLogin);
        userLoginLayout.add(passwordField);
        userLoginLayout.add(buttonLogin);
        userLoginLayout.add(buttonCreateNewUser);

        return userLoginLayout;
    }

    public void setCurrentUser(UserDto userDto) {
        Long userId = userDto.getId();
        String firstname = userDto.getFirstname();
        String surname = userDto.getSurname();
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        Boolean isAdmin = userDto.getIsAdmin();
        Boolean isLogged = userDto.getIsLogged();
        Long cartId = userDto.getCartDto().getId();
        List<Long> ordersID = userDto.getOrdersDto().stream()
                .map(order -> order.getId())
                .collect(Collectors.toList());

        SessionVariables.getInstance().setCurrentUser(new User(userId, firstname, surname,
                email, password, isAdmin, isLogged, cartId, ordersID));
    }
}
