package pl.asbt.moviesfrontend.views.forms;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import pl.asbt.moviesfrontend.client.MovieClient;
import pl.asbt.moviesfrontend.client.StorageItemClient;
import pl.asbt.moviesfrontend.dto.MovieDto;
import pl.asbt.moviesfrontend.dto.StorageItemDto;
import pl.asbt.moviesfrontend.views.AdminView;

@Route(value = "moviesShop/adminStorageItemForm")
public class AdminStorageItemForm extends VerticalLayout {

    private StorageItemClient storageItemClient = StorageItemClient.getInstance();
    private MovieClient movieClient = MovieClient.getInstance();

    private HorizontalLayout storageItemFilterAddLayout = new HorizontalLayout();
    private HorizontalLayout storageItemFormButtonLayout = new HorizontalLayout();
    private FormLayout storageItemFormLayout = new FormLayout();
    private HorizontalLayout storageItemViewAndModificationLayout = new HorizontalLayout();

    private TextField filterStorageItem = new TextField();
    private Button addNewStorageItem = new Button("Add new storage item");

    private TextField movieTitle = new TextField("Movie title");
    private IntegerField quantity = new IntegerField("Quantity");
    private Button save = new Button("Save");
    private Button update = new Button("Update");
    private Button delete = new Button("Delete");
    private Binder<StorageItemDto> binder = new Binder<>(StorageItemDto.class);

    private Grid<StorageItemDto> gridStorageItem = new Grid<>(StorageItemDto.class);

    private Binder<MovieDto> binderMovie = new Binder<>(MovieDto.class);
    private Grid<MovieDto> gridMovie = new Grid<>(MovieDto.class);

    public AdminStorageItemForm() {
        setStorageItemDto(null);

        addNewStorageItem.addClickListener(action -> {
            gridStorageItem.asSingleSelect().clear();
            setStorageItemDto(new StorageItemDto());
        });

        filterStorageItem.setPlaceholder("Filter storage items");
        filterStorageItem.setClearButtonVisible(true);
        filterStorageItem.setValueChangeMode(ValueChangeMode.EAGER);
        filterStorageItem.addValueChangeListener(action -> updateStorageItem());
        storageItemFilterAddLayout.add(filterStorageItem, addNewStorageItem);

        storageItemFormButtonLayout.add(save, update, delete);
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        binder.forField(movieTitle).bind(StorageItemDto::getMovieTitle, StorageItemDto::setMovieTitle);
        binder.forField(quantity).bind(StorageItemDto::getQuantity, StorageItemDto::setQuantity);
        quantity.setHasControls(true);
        quantity.setMin(1);
        storageItemFormLayout.add(movieTitle, quantity, storageItemFormButtonLayout);
        save.addClickListener(event -> saveStorageItem());
        update.addClickListener(event -> updateStorageItem());
        delete.addClickListener(event -> deleteStorageItem());

        gridStorageItem.setColumns("movieTitle", "quantity");
        gridStorageItem.setSizeFull();
        storageItemViewAndModificationLayout.add(gridStorageItem, storageItemFormLayout);
        storageItemViewAndModificationLayout.setSizeFull();

        Button buttonBack = new Button("Back");
        buttonBack.addClickListener(action -> {
            UI.getCurrent().navigate(AdminView.class);
        });

        gridMovie.setColumns("title", "duration", "price");
        gridMovie.setSizeFull();
        gridMovie.addItemClickListener(action -> {
            String Title = gridMovie.asSingleSelect().getValue().getTitle();
            movieTitle.setValue(Title);
            quantity.setValue(1);
        });
//        binderMovie.forField(movieTitle).bind(MovieDto::getTitle, MovieDto::setTitle);
        add(buttonBack, storageItemFilterAddLayout, storageItemViewAndModificationLayout, gridMovie);
        setSizeFull();
        refreshStorageItemsGrid();

        gridStorageItem.asSingleSelect().addValueChangeListener(event -> setStorageItemDto(gridStorageItem.asSingleSelect().getValue()));
    }

    public void refreshStorageItemsGrid() {
        gridStorageItem.setItems(storageItemClient.getStorageItems());
        gridMovie.setItems(movieClient.getMovies());
    }

    public void updateStorageItemsGrid() {
        gridStorageItem.setItems(storageItemClient.filterStorageItems(filterStorageItem.getValue()));
    }

    private void saveStorageItem() {
        StorageItemDto storageItemDto = binder.getBean();
        storageItemDto.setId(0L);
        storageItemDto.setMovieId(0L);
        storageItemClient.createStorageItem(storageItemDto);
        refreshStorageItemsGrid();
        setStorageItemDto(null);
    }

    private void updateStorageItem() {
        StorageItemDto storageItemDto = binder.getBean();
        storageItemClient.updateStorageItem(storageItemDto);
        refreshStorageItemsGrid();
        setStorageItemDto(null);
    }

    private void deleteStorageItem() {
        StorageItemDto storageItemDto = binder.getBean();
        storageItemClient.deleteStorageItem(storageItemDto);
        refreshStorageItemsGrid();
        setStorageItemDto(null);
    }

    public void setStorageItemDto(StorageItemDto storageItemDto) {
        binder.setBean(storageItemDto);
        if (storageItemDto == null) {
            storageItemFormLayout.setVisible(false);
        } else {
            storageItemFormLayout.setVisible(true);
            quantity.focus();
        }
    }
}
