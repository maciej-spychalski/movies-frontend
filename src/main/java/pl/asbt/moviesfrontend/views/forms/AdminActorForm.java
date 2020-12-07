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
import pl.asbt.moviesfrontend.client.ActorClient;
import pl.asbt.moviesfrontend.dto.ActorDto;
import pl.asbt.moviesfrontend.views.AdminView;


@Route(value = "moviesShop/adminActorForm")
public class AdminActorForm extends VerticalLayout {

    private ActorClient actorClient = ActorClient.getInstance();

    private HorizontalLayout actorFilterAddLayout = new HorizontalLayout();
    private HorizontalLayout actorFormButtonLayout = new HorizontalLayout();
    private FormLayout actorFormLayout = new FormLayout();
    private HorizontalLayout actorViewAndModificationLayout = new HorizontalLayout();

    private TextField filterActor = new TextField();
    private Button addNewActor = new Button("Add new actor");

    private final TextField firstname = new TextField("Firstname");
    private final TextField surname = new TextField("Surname");
    private final Button save = new Button("Save");
    private final Button update = new Button("Update");
    private final Button delete = new Button("Delete");
    private final Binder<ActorDto> binder = new Binder<>(ActorDto.class);

    private Grid<ActorDto> gridActor = new Grid<>(ActorDto.class);

    public AdminActorForm() {
        // Dzięki temu przy uruchamianiu formularz dodwania użytkownikow (actorFormLayout) nie bedzie widoczny
        setActorDto(null);

        addNewActor.addClickListener(action ->{
            gridActor.asSingleSelect().clear();
            setActorDto(new ActorDto());
        });

//        HorizontalLayout actorFilterAddLayout = new HorizontalLayout();
        filterActor.setPlaceholder("Filter actors");
        filterActor.setClearButtonVisible(true);
        filterActor.setValueChangeMode(ValueChangeMode.EAGER);
        filterActor.addValueChangeListener(action -> updateActorsGrid());
        actorFilterAddLayout.add(filterActor, addNewActor);

//        HorizontalLayout actorFormButtonLayout = new HorizontalLayout(save, update, delete);
        actorFormButtonLayout.add(save, update, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

//        FormLayout actorFormLayout = new FormLayout();
        binder.forField(firstname).bind(ActorDto::getFirstname, ActorDto::setFirstname);
        binder.forField(surname).bind(ActorDto::getSurname, ActorDto::setSurname);
        actorFormLayout.add(firstname, surname, actorFormButtonLayout);
        save.addClickListener(event -> saveActor());
        update.addClickListener(event -> updateActor());
        delete.addClickListener(event -> deleteActor());

//        HorizontalLayout actorViewAndModificationLayout = new HorizontalLayout();
        gridActor.setColumns("firstname", "surname");
        gridActor.setSizeFull();
        actorViewAndModificationLayout.add(gridActor, actorFormLayout);
        actorViewAndModificationLayout.setSizeFull();

        Button buttonBack = new Button("Back");
        buttonBack.addClickListener(action -> {
            UI.getCurrent().navigate(AdminView.class);
        });

        add(buttonBack, actorFilterAddLayout, actorViewAndModificationLayout);
        setSizeFull();
        refreshActorsGrid();

        // Metoda asSingleSelect() odpowiada za pojedyncze zaznaczenie wiersza (dostępny jest także multiselect).
        // Gdy wiersz został zaznaczony, tworzony jest event – wykorzystujemy metodę setActorDto, przekazując cały
        // zaznaczony wiersz.
        gridActor.asSingleSelect().addValueChangeListener(event -> setActorDto(gridActor.asSingleSelect().getValue()));
    }

    public void refreshActorsGrid() {
        gridActor.setItems(actorClient.getActors());
    }

    public void updateActorsGrid() {
        gridActor.setItems(actorClient.filterActors(filterActor.getValue()));
    }

    private void saveActor() {
        ActorDto actorDto = binder.getBean();
        actorDto.setId(0L);
        actorClient.createActor(actorDto);
        refreshActorsGrid();
        setActorDto(null);
    }

    private void updateActor() {
        ActorDto actorDto = binder.getBean();
        actorClient.updateActor(actorDto);
        refreshActorsGrid();
        setActorDto(null);
    }

    private void deleteActor() {
        ActorDto actorDto = binder.getBean();
        actorClient.deleteActor(actorDto);
        refreshActorsGrid();
        setActorDto(null);
    }

    public void setActorDto(ActorDto actorDto) {
        binder.setBean(actorDto);
        if (actorDto == null) {
            actorFormLayout.setVisible(false);
        } else {
            actorFormLayout.setVisible(true);
            firstname.focus();
        }
    }
}
