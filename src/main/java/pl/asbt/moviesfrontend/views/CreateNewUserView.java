package pl.asbt.moviesfrontend.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import pl.asbt.moviesfrontend.client.PasswordClient;
import pl.asbt.moviesfrontend.client.UserClient;
import pl.asbt.moviesfrontend.config.MoviesStorageConfig;
import pl.asbt.moviesfrontend.dto.PasswordDto;
import pl.asbt.moviesfrontend.dto.UserDto;

@Route(value = "moviesShop/createUser")
public class CreateNewUserView extends VerticalLayout {

    private UserClient userClient = UserClient.getInstance();
    private PasswordClient passwordClient = PasswordClient.getInstance();

    public CreateNewUserView() {
        TextField textFieldFirstname = new TextField();
        textFieldFirstname.setLabel("Firstname");
        textFieldFirstname.setPlaceholder("firstname");

        TextField textFieldSurname = new TextField();
        textFieldSurname.setLabel("Surname");
        textFieldSurname.setPlaceholder("surname");

        TextField textFieldEmail = new TextField();
        textFieldEmail.setLabel("Email");
        textFieldEmail.setPlaceholder("email");

        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Password");
        passwordField.setPlaceholder("password");

        Button generatePassword = new Button("Generate password");
        generatePassword.addClickListener(action -> {
            PasswordDto passwordDto = passwordClient.getPassword();
            passwordField.setValue(passwordDto.getPasswords().get(0));
        });

        TextField textFieldEmailAdminCode = new TextField();
        textFieldEmailAdminCode.setLabel("Admin code");
        textFieldEmailAdminCode.setPlaceholder("enter admin code");

        Button buttonRegister = new Button();
        buttonRegister.setText("Register");
        buttonRegister.addClickListener(action -> {
            if (textFieldFirstname.getValue().isEmpty() ||
                    textFieldSurname.getValue().isEmpty() ||
                    textFieldEmail.getValue().isEmpty() ||
                    passwordField.getValue().isEmpty()) {
                Notification.show("Please valid values for all fields.");
            } else {
                Boolean isAdmin = false;
                isAdmin = textFieldEmailAdminCode.getValue().equals(MoviesStorageConfig.ADMIN_CODE)? true : false;

                userClient.createUser(new UserDto(
                        0L,
                        textFieldFirstname.getValue(),
                        textFieldSurname.getValue(),
                        textFieldEmail.getValue(),
                        passwordField.getValue(),
                        isAdmin,
                        false));

                Notification.show("User has been created", 3000, Notification.Position.MIDDLE);
                UI.getCurrent().navigate(MainView.class);
            }
        });

        add(textFieldFirstname, textFieldSurname, textFieldEmail, passwordField, generatePassword,
                textFieldEmailAdminCode, buttonRegister);
    }
}
