package pl.asbt.moviesfrontend.views.forms;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import pl.asbt.moviesfrontend.client.UserClient;
import pl.asbt.moviesfrontend.dto.UserDto;
import pl.asbt.moviesfrontend.views.AdminView;

@Route(value = "moviesShop/adminUserForm")
public class AdminUserForm extends VerticalLayout {

    private UserClient userClient = UserClient.getInstance();

    private HorizontalLayout userFilterAddLayout = new HorizontalLayout();
    private HorizontalLayout userFormButtonLayout = new HorizontalLayout();
    private FormLayout userFormLayout = new FormLayout();
    private HorizontalLayout userViewAndModificationLayout = new HorizontalLayout();

    private TextField filterUser = new TextField();
    private Button modifyUser = new Button("Modify new user");

    private final TextField firstname = new TextField("Firstname");
    private final TextField surname = new TextField("Surname");
    private final TextField email = new TextField("Email");
    private final Button update = new Button("Update");
    private final Button delete = new Button("Delete");
    private final Binder<UserDto> binder = new Binder<>(UserDto.class);

    private Grid<UserDto> gridUser = new Grid<>(UserDto.class);

    public AdminUserForm() {
        // Dzięki temu przy uruchamianiu formularz dodwania użytkownikow (userFormLayout) nie bedzie widoczny
        setUserDto(null);

        modifyUser.addClickListener(action -> {
            gridUser.asSingleSelect().clear();
            setUserDto(new UserDto());
        });

//        HorizontalLayout userFilterAddLayout = new HorizontalLayout();
        filterUser.setPlaceholder("Filter users");
        filterUser.setClearButtonVisible(true);
        filterUser.setValueChangeMode(ValueChangeMode.EAGER);
        filterUser.addValueChangeListener(action -> updateUsersGrid());
        userFilterAddLayout.add(filterUser, modifyUser);

//        HorizontalLayout userFormButtonLayout = new HorizontalLayout(save, delete);
        userFormButtonLayout.add(update, delete);
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

//        FormLayout userFormLayout = new FormLayout();
        binder.forField(firstname).bind(UserDto::getFirstname, UserDto::setFirstname);
        binder.forField(surname).bind(UserDto::getSurname, UserDto::setSurname);
        binder.forField(email).bind(UserDto::getEmail, UserDto::setEmail);
        userFormLayout.add(firstname, surname, email, userFormButtonLayout);
        update.addClickListener(event -> updateUser());
        delete.addClickListener(event -> deleteUser());

//        HorizontalLayout userViewAndModificationLayout = new HorizontalLayout();
        gridUser.setColumns("firstname", "surname", "email", "isAdmin");
        gridUser.setSizeFull();
        userViewAndModificationLayout.add(gridUser, userFormLayout);
        userViewAndModificationLayout.setSizeFull();

        Button buttonBack = new Button("Back");
        buttonBack.addClickListener(action -> {
            UI.getCurrent().navigate(AdminView.class);
        });

        add(buttonBack, userFilterAddLayout, userViewAndModificationLayout);
        setSizeFull();
        refreshUsersGrid();


        // Metoda asSingleSelect() odpowiada za pojedyncze zaznaczenie wiersza (dostępny jest także multiselect).
        // Gdy wiersz został zaznaczony, tworzony jest event – wykorzystujemy metodę setUserDto, przekazując cały
        // zaznaczony wiersz.
        gridUser.asSingleSelect().addValueChangeListener(event -> setUserDto(gridUser.asSingleSelect().getValue()));
    }

    public void refreshUsersGrid() {
        gridUser.setItems(userClient.getUsers());
    }

    public void updateUsersGrid() {
        gridUser.setItems(userClient.filterUsers(filterUser.getValue()));
    }

    private void updateUser() {
        UserDto userDto = binder.getBean();
        userClient.updateUser(userDto);
        refreshUsersGrid();
        setUserDto(null);
    }

    private void deleteUser() {
        UserDto userDto = binder.getBean();
        userClient.deleteUser(userDto);
        refreshUsersGrid();
        setUserDto(null);
    }

    public void setUserDto(UserDto userDto) {
        binder.setBean(userDto);
        if (userDto == null) {
            userFormLayout.setVisible(false);
        } else {
            userFormLayout.setVisible(true);
            firstname.focus();
        }
    }
}
