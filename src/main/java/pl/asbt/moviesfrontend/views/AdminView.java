package pl.asbt.moviesfrontend.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import pl.asbt.moviesfrontend.domain.User;
import pl.asbt.moviesfrontend.session.SessionVariables;
import pl.asbt.moviesfrontend.views.forms.*;

@Route(value = "moviesShop/adminView")
public class AdminView extends VerticalLayout {

    public AdminView() {
        Button buttonLogout = new Button("Logout");
        buttonLogout.addClickListener(action -> {
            SessionVariables.getInstance().setCurrentUser(new User());
            UI.getCurrent().navigate(MainView.class);
        });

        Button buttonUser = new Button("Users");
        buttonUser.addClickListener(action -> {
            UI.getCurrent().navigate(AdminUserForm.class);
        });

        Button buttonDirector = new Button("Directors");
        buttonDirector.addClickListener(action -> {
            UI.getCurrent().navigate(AdminDirectorForm.class);
        });

        Button buttonWriter = new Button("Writers");
        buttonWriter.addClickListener(action -> {
            UI.getCurrent().navigate(AdminWriterForm.class);
        });

        Button buttonActor = new Button("Actors");
        buttonActor.addClickListener(action -> {
            UI.getCurrent().navigate(AdminActorForm.class);
        });

        Button buttonGenre = new Button("Genres");
        buttonGenre.addClickListener(action -> {
            UI.getCurrent().navigate(AdminGenreForm.class);
        });

        Button buttonMovie = new Button("Movies");
        buttonMovie.addClickListener(action -> {
            UI.getCurrent().navigate(AdminMovieForm.class);
        });

        Button buttonStorageItem = new Button("Storage items");
        buttonStorageItem.addClickListener(action -> {
            UI.getCurrent().navigate(AdminStorageItemForm.class);
        });

        add(buttonLogout, buttonUser, buttonDirector, buttonWriter, buttonActor, buttonGenre, buttonMovie, buttonStorageItem);
    }
}
