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
import pl.asbt.moviesfrontend.client.DirectorClient;
import pl.asbt.moviesfrontend.dto.DirectorDto;
import pl.asbt.moviesfrontend.views.AdminView;

@Route(value = "moviesShop/adminDirectorForm")
public class AdminDirectorForm extends VerticalLayout {

    private final DirectorClient directorClient = DirectorClient.getInstance();

    private HorizontalLayout directorFilterAddLayout = new HorizontalLayout();
    private HorizontalLayout directorFormButtonLayout = new HorizontalLayout();
    private FormLayout directorFormLayout = new FormLayout();
    private HorizontalLayout directorViewAndModificationLayout = new HorizontalLayout();

    private TextField filterDirector = new TextField();
    private Button addNewDirector = new Button("Add new director");

    private TextField firstname = new TextField("Firstname");
    private TextField surname = new TextField("Surname");
    private Button save = new Button("Save");
    private Button update = new Button("Update");
    private Button delete = new Button("Delete");
    private Binder<DirectorDto> binder = new Binder<>(DirectorDto.class);

    private Grid<DirectorDto> gridDirector = new Grid<>(DirectorDto.class);

    public AdminDirectorForm() {
        // Dzięki temu przy uruchamianiu formularz dodwania użytkownikow (directorFormLayout) nie bedzie widoczny
        setDirectorDto(null);

        addNewDirector.addClickListener(action ->{
            gridDirector.asSingleSelect().clear();
            setDirectorDto(new DirectorDto());
        });

//        HorizontalLayout directorFilterAddLayout = new HorizontalLayout();
        filterDirector.setPlaceholder("Filter directors");
        filterDirector.setClearButtonVisible(true);
        filterDirector.setValueChangeMode(ValueChangeMode.EAGER);
        filterDirector.addValueChangeListener(action -> updateDirectorsGrid());
        directorFilterAddLayout.add(filterDirector, addNewDirector);

//        HorizontalLayout directorFormButtonLayout = new HorizontalLayout(save, update, delete);
        directorFormButtonLayout.add(save, update, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

//        FormLayout directorFormLayout = new FormLayout();
        binder.forField(firstname).bind(DirectorDto::getFirstname, DirectorDto::setFirstname);
        binder.forField(surname).bind(DirectorDto::getSurname, DirectorDto::setSurname);
        directorFormLayout.add(firstname, surname, directorFormButtonLayout);
        save.addClickListener(event -> saveDirector());
        update.addClickListener(event -> updateDirector());
        delete.addClickListener(event -> deleteDirector());

//        HorizontalLayout directorViewAndModificationLayout = new HorizontalLayout();
        gridDirector.setColumns("firstname", "surname");
        gridDirector.setSizeFull();
        directorViewAndModificationLayout.add(gridDirector, directorFormLayout);
        directorViewAndModificationLayout.setSizeFull();

        Button buttonBack = new Button("Back");
        buttonBack.addClickListener(action -> {
            UI.getCurrent().navigate(AdminView.class);
        });

        add(buttonBack, directorFilterAddLayout, directorViewAndModificationLayout);
        setSizeFull();
        refreshDirectorsGrid();

        // Metoda asSingleSelect() odpowiada za pojedyncze zaznaczenie wiersza (dostępny jest także multiselect).
        // Gdy wiersz został zaznaczony, tworzony jest event – wykorzystujemy metodę setDirectorDto, przekazując cały
        // zaznaczony wiersz.
        gridDirector.asSingleSelect().addValueChangeListener(event -> setDirectorDto(gridDirector.asSingleSelect().getValue()));
    }

    public void refreshDirectorsGrid() {
        gridDirector.setItems(directorClient.getDirectors());
    }

    public void updateDirectorsGrid() {
        gridDirector.setItems(directorClient.filterDirectors(filterDirector.getValue()));
    }

    private void saveDirector() {
        DirectorDto directorDto = binder.getBean();
        directorDto.setId(0L);
        directorClient.createDirector(directorDto);
        refreshDirectorsGrid();
        setDirectorDto(null);
    }

    private void updateDirector() {
        DirectorDto directorDto = binder.getBean();
        directorClient.updateDirector(directorDto);
        refreshDirectorsGrid();
        setDirectorDto(null);
    }

    private void deleteDirector() {
        DirectorDto directorDto = binder.getBean();
        directorClient.deleteDirector(directorDto);
        refreshDirectorsGrid();
        setDirectorDto(null);
    }

    public void setDirectorDto(DirectorDto directorDto) {
        binder.setBean(directorDto);
        if (directorDto == null) {
            directorFormLayout.setVisible(false);
        } else {
            directorFormLayout.setVisible(true);
            firstname.focus();
        }
    }
}
