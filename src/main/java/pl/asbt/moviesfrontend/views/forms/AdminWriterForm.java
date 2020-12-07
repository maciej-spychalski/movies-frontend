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
import pl.asbt.moviesfrontend.client.WriterClient;
import pl.asbt.moviesfrontend.dto.WriterDto;
import pl.asbt.moviesfrontend.views.AdminView;

@Route(value = "moviesShop/adminWriterForm")
public class AdminWriterForm extends VerticalLayout {

    private WriterClient writerClient = WriterClient.getInstance();

    private HorizontalLayout writerFilterAddLayout = new HorizontalLayout();
    private HorizontalLayout writerFormButtonLayout = new HorizontalLayout();
    private FormLayout writerFormLayout = new FormLayout();
    private HorizontalLayout writerViewAndModificationLayout = new HorizontalLayout();

    private TextField filterWriter = new TextField();
    private Button addNewWriter = new Button("Add new writer");

    private final TextField firstname = new TextField("Firstname");
    private final TextField surname = new TextField("Surname");
    private final Button save = new Button("Save");
    private final Button update = new Button("Update");
    private final Button delete = new Button("Delete");
    private final Binder<WriterDto> binder = new Binder<>(WriterDto.class);

    private Grid<WriterDto> gridWriter = new Grid<>(WriterDto.class);

    public AdminWriterForm() {
        // Dzięki temu przy uruchamianiu formularz dodwania użytkownikow (writerFormLayout) nie bedzie widoczny
        setWriterDto(null);

        addNewWriter.addClickListener(action ->{
            gridWriter.asSingleSelect().clear();
            setWriterDto(new WriterDto());
        });

//        HorizontalLayout writerFilterAddLayout = new HorizontalLayout();
        filterWriter.setPlaceholder("Filter writers");
        filterWriter.setClearButtonVisible(true);
        filterWriter.setValueChangeMode(ValueChangeMode.EAGER);
        filterWriter.addValueChangeListener(action -> updateWritersGrid());
        writerFilterAddLayout.add(filterWriter, addNewWriter);

//        HorizontalLayout writerFormButtonLayout = new HorizontalLayout(save, update, delete);
        writerFormButtonLayout.add(save, update, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

//        FormLayout writerFormLayout = new FormLayout();
        binder.forField(firstname).bind(WriterDto::getFirstname, WriterDto::setFirstname);
        binder.forField(surname).bind(WriterDto::getSurname, WriterDto::setSurname);
        writerFormLayout.add(firstname, surname, writerFormButtonLayout);
        save.addClickListener(event -> saveWriter());
        update.addClickListener(event -> updateWriter());
        delete.addClickListener(event -> deleteWriter());

//        HorizontalLayout writerViewAndModificationLayout = new HorizontalLayout();
        gridWriter.setColumns("firstname", "surname");
        gridWriter.setSizeFull();
        writerViewAndModificationLayout.add(gridWriter, writerFormLayout);
        writerViewAndModificationLayout.setSizeFull();

        Button buttonBack = new Button("Back");
        buttonBack.addClickListener(action -> {
            UI.getCurrent().navigate(AdminView.class);
        });

        add(buttonBack, writerFilterAddLayout, writerViewAndModificationLayout);
        setSizeFull();
        refreshWritersGrid();

        // Metoda asSingleSelect() odpowiada za pojedyncze zaznaczenie wiersza (dostępny jest także multiselect).
        // Gdy wiersz został zaznaczony, tworzony jest event – wykorzystujemy metodę setDirectorDto, przekazując cały
        // zaznaczony wiersz.
        gridWriter.asSingleSelect().addValueChangeListener(event -> setWriterDto(gridWriter.asSingleSelect().getValue()));
    }

    public void refreshWritersGrid() {
        gridWriter.setItems(writerClient.getWriters());
    }

    public void updateWritersGrid() {
        gridWriter.setItems(writerClient.filterWriters(filterWriter.getValue()));
    }

    private void saveWriter() {
        WriterDto writerDto = binder.getBean();
        writerDto.setId(0L);
        writerClient.createWriter(writerDto);
        refreshWritersGrid();
        setWriterDto(null);
    }

    private void updateWriter() {
        WriterDto writerDto  = binder.getBean();
        writerClient.updateWriter(writerDto);
        refreshWritersGrid();
        setWriterDto(null);
    }

    private void deleteWriter() {
        WriterDto writerDto  = binder.getBean();
        writerClient.deleteWriter(writerDto);
        refreshWritersGrid();
        setWriterDto(null);
    }

    public void setWriterDto(WriterDto writerDto) {
        binder.setBean(writerDto);
        if (writerDto == null) {
            writerFormLayout.setVisible(false);
        } else {
            writerFormLayout.setVisible(true);
            firstname.focus();
        }
    }
}
