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
import pl.asbt.moviesfrontend.client.GenreClient;
import pl.asbt.moviesfrontend.dto.GenreDto;
import pl.asbt.moviesfrontend.views.AdminView;

@Route(value = "moviesShop/adminGenreForm")
public class AdminGenreForm extends VerticalLayout {

    private GenreClient genreClient = GenreClient.getInstance();

    private HorizontalLayout genreFilterAddLayout = new HorizontalLayout();
    private HorizontalLayout genreFormButtonLayout = new HorizontalLayout();
    private FormLayout genreFormLayout = new FormLayout();
    private HorizontalLayout genreViewAndModificationLayout = new HorizontalLayout();

    private TextField filterGenre = new TextField();
    private Button addNewGenre = new Button("Add new genre");

    private final TextField type = new TextField("Type");
    private final Button save = new Button("Save");
    private final Button update = new Button("Update");
    private final Button delete = new Button("Delete");
    private final Binder<GenreDto> binder = new Binder<>(GenreDto.class);

    private Grid<GenreDto> gridGenre = new Grid<>(GenreDto.class);

    public AdminGenreForm() {
        // Dzięki temu przy uruchamianiu formularz dodwania użytkownikow (genreFormLayout) nie bedzie widoczny
        setGenreDto(null);

        addNewGenre.addClickListener(action ->{
            gridGenre.asSingleSelect().clear();
            setGenreDto(new GenreDto());
        });

//        HorizontalLayout genreFilterAddLayout = new HorizontalLayout();
        filterGenre.setPlaceholder("Filter genres");
        filterGenre.setClearButtonVisible(true);
        filterGenre.setValueChangeMode(ValueChangeMode.EAGER);
        filterGenre.addValueChangeListener(action -> updateGenresGrid());
        genreFilterAddLayout.add(filterGenre, addNewGenre);

//        HorizontalLayout genreFormButtonLayout = new HorizontalLayout(save, update, delete);
        genreFormButtonLayout.add(save, update, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

//        FormLayout genreFormLayout = new FormLayout();
        binder.forField(type).bind(GenreDto::getType, GenreDto::setType);
        genreFormLayout.add(type, genreFormButtonLayout);
        save.addClickListener(event -> saveGenre());
        update.addClickListener(event -> updateGenre());
        delete.addClickListener(event -> deleteGenre());

//        HorizontalLayout genreViewAndModificationLayout = new HorizontalLayout();
        gridGenre.setColumns("type");
        gridGenre.setSizeFull();
        genreViewAndModificationLayout.add(gridGenre, genreFormLayout);
        genreViewAndModificationLayout.setSizeFull();

        Button buttonBack = new Button("Back");
        buttonBack.addClickListener(action -> {
            UI.getCurrent().navigate(AdminView.class);
        });

        add(buttonBack, genreFilterAddLayout, genreViewAndModificationLayout);
        setSizeFull();
        refreshGenresGrid();

        // Metoda asSingleSelect() odpowiada za pojedyncze zaznaczenie wiersza (dostępny jest także multiselect).
        // Gdy wiersz został zaznaczony, tworzony jest event – wykorzystujemy metodę setGenreDto, przekazując cały
        // zaznaczony wiersz.
        gridGenre.asSingleSelect().addValueChangeListener(event -> setGenreDto(gridGenre.asSingleSelect().getValue()));
    }

    public void refreshGenresGrid() {
        gridGenre.setItems(genreClient.getGenres());
    }

    public void updateGenresGrid() {
        gridGenre.setItems(genreClient.filterGenres(filterGenre.getValue()));
    }

    private void saveGenre() {
        GenreDto genreDto = binder.getBean();
        genreDto.setId(0L);
        genreClient.createGenre(genreDto);
        refreshGenresGrid();
        setGenreDto(null);
    }

    private void updateGenre() {
        GenreDto genreDto = binder.getBean();
        genreClient.updateGenre(genreDto);
        refreshGenresGrid();
        setGenreDto(null);
    }

    private void deleteGenre() {
        GenreDto genreDto = binder.getBean();
        genreClient.deleteGenre(genreDto);
        refreshGenresGrid();
        setGenreDto(null);
    }

    public void setGenreDto(GenreDto genreDto) {
        binder.setBean(genreDto);
        if (genreDto == null) {
            genreFormLayout.setVisible(false);
        } else {
            genreFormLayout.setVisible(true);
            type.focus();
        }
    }
}
