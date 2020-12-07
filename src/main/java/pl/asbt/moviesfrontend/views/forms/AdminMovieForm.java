package pl.asbt.moviesfrontend.views.forms;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import pl.asbt.moviesfrontend.client.*;
import pl.asbt.moviesfrontend.dto.*;
import pl.asbt.moviesfrontend.views.AdminView;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "moviesShop/adminMovieForm")
public class AdminMovieForm extends VerticalLayout {

    private final MovieClient movieClient = MovieClient.getInstance();
    private final DirectorClient directorClient = DirectorClient.getInstance();
    private final WriterClient writerClient = WriterClient.getInstance();
    private final ActorClient actorClient = ActorClient.getInstance();
    private final GenreClient genreClient = GenreClient.getInstance();

    private HorizontalLayout movieFilterAddLayout = new HorizontalLayout();
    private HorizontalLayout movieFormButtonLayout = new HorizontalLayout();
    private FormLayout movieFormLayout = new FormLayout();
    private HorizontalLayout movieViewAndModificationLayout = new HorizontalLayout();

    private TextField movieFilter = new TextField();
    private Button movieAddNew = new Button("Add new movie");

    private TextField movieTitle = new TextField("Title");
    private IntegerField movieDuration = new IntegerField("Duration");
    private BigDecimalField moviePrice = new BigDecimalField("Price");
    private Button movieSave = new Button("Save");
    private Button movieUpdate = new Button("Update");
    private Button movieDelete = new Button("Delete");
    private Binder<MovieDto> movieBinder = new Binder<>(MovieDto.class);

    private Grid<MovieDto> movieGrid = new Grid<>(MovieDto.class);

    private HorizontalLayout directorCurrentMovieLayout = new HorizontalLayout();
    private Grid<DirectorDto> directorCurrentMovieGrid = new Grid<>(DirectorDto.class);
    private Button directorDelete = new Button("Delete director from movie");
    private HorizontalLayout writerCurrentMovieLayout = new HorizontalLayout();
    private Grid<WriterDto> writerCurrentMovieGrid = new Grid<>(WriterDto.class);
    private Button writerDelete = new Button("Delete writer from movie");
    private HorizontalLayout actorCurrentMovieLayout = new HorizontalLayout();
    private Grid<ActorDto> actorCurrentMovieGrid = new Grid<>(ActorDto.class);
    private Button actorDelete = new Button("Delete actor from movie");
    private HorizontalLayout genreCurrentMovieLayout = new HorizontalLayout();
    private Grid<GenreDto> genreCurrentMovieGrid = new Grid<>(GenreDto.class);
    private Button genreDelete = new Button("Delete genre from movie");

    private HorizontalLayout directorAllLayout = new HorizontalLayout();
    private Grid<DirectorDto> allDirectorGrid = new Grid<>(DirectorDto.class);
    private VerticalLayout directorAddMovieLayout = new VerticalLayout();
    private TextField directorFilter = new TextField();
    private Button directorAdd = new Button("Add director to selected movie");
    private HorizontalLayout writerAllLayout = new HorizontalLayout();
    private Grid<WriterDto> allWriterGrid = new Grid<>(WriterDto.class);
    private VerticalLayout writerAddMovieLayout = new VerticalLayout();
    private TextField writerFilter = new TextField();
    private Button writerAdd = new Button("Add writer to selected movie");
    private HorizontalLayout actorAllLayout = new HorizontalLayout();
    private Grid<ActorDto> allActorGrid = new Grid<>(ActorDto.class);
    private VerticalLayout actorAddMovieLayout = new VerticalLayout();
    private TextField actorFilter = new TextField();
    private Button actorAdd = new Button("Add actor to selected movie");
    private HorizontalLayout genreAllLayout = new HorizontalLayout();
    private Grid<GenreDto> allGenreGrid = new Grid<>(GenreDto.class);
    private VerticalLayout genreAddMovieLayout = new VerticalLayout();
    private TextField genreFilter = new TextField();
    private Button genreAdd = new Button("Add genre to selected movie");


    public AdminMovieForm() {
        setMovieDto(null);

        movieAddNew.addClickListener(action -> {
            movieGrid.asSingleSelect().clear();
            setMovieDto(new MovieDto());
        });

        movieFilter.setPlaceholder("Filter movies");
        movieFilter.setClearButtonVisible(true);
        movieFilter.setValueChangeMode(ValueChangeMode.EAGER);
        movieFilter.addValueChangeListener(action -> updateMoviesGrid());
        movieFilterAddLayout.add(movieFilter, movieAddNew);

        movieFormButtonLayout.add(movieSave, movieUpdate, movieDelete);
        movieSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        movieBinder.forField(movieTitle).bind(MovieDto::getTitle, MovieDto::setTitle);
        movieBinder.forField(movieDuration).bind(MovieDto::getDuration, MovieDto::setDuration);
        movieBinder.forField(moviePrice).bind(MovieDto::getPrice, MovieDto::setPrice);
        movieFormLayout.add(movieTitle, movieDuration, moviePrice, movieFormButtonLayout);
        movieSave.addClickListener(event -> saveMovie());
        movieUpdate.addClickListener(event -> updateMovie());
        movieDelete.addClickListener(event -> deleteMovie());

        movieGrid.setColumns("title");
        movieGrid.addColumn(movieDto -> {
            DirectorDto directorDto = movieDto.getDirectorDto();
            return directorDto == null ? "-" : directorDto.getFirstname() + " " + directorDto.getSurname();
        }).setHeader("Director");
        movieGrid.addColumn(movieDto -> {
            List<WriterDto> writersDto = movieDto.getWritersDto();
            String writersList = writersDto.stream()
                    .map(w -> w.getFirstname() + " " + w.getSurname() + "; ")
                    .collect(Collectors.joining());
            return writersDto.size() == 0 ? "-" : writersList;
        }).setHeader("Writers");
        movieGrid.addColumn(movieDto -> {
            List<ActorDto> actorsDto = movieDto.getActorsDto();
            String actorsList = actorsDto.stream()
                    .map(a -> a.getFirstname() + " " + a.getSurname() + "; ")
                    .collect(Collectors.joining());
            return actorsDto.size() == 0 ? "-" : actorsList;
        }).setHeader("Actors");
        movieGrid.addColumn(movieDto -> {
            List<GenreDto> genresDto = movieDto.getGenresDto();
            String genresList = genresDto.stream()
                    .map(g -> g.getType() + "; ")
                    .collect(Collectors.joining());
            return genresDto.size() == 0 ? "-" : genresList;
        }).setHeader("Genres");
        movieGrid.addColumns("duration", "price");
        movieGrid.setSizeFull();
        movieViewAndModificationLayout.add(movieGrid, movieFormLayout);
        movieViewAndModificationLayout.setSizeFull();

        Button buttonBack = new Button("Back");
        buttonBack.addClickListener(action -> {
            UI.getCurrent().navigate(AdminView.class);
        });

        directorCurrentMovieGrid.setColumns("firstname", "surname");
        directorCurrentMovieLayout.add(directorCurrentMovieGrid/*, directorDelete*/);
        directorCurrentMovieLayout.setSizeFull();

        allDirectorGrid.setColumns("firstname", "surname");
        allDirectorGrid.setSizeFull();
        directorAdd.addClickListener(action -> {
            MovieDto movieDto = movieGrid.asSingleSelect().getValue();
            DirectorDto directorDto = allDirectorGrid.asSingleSelect().getValue();
            movieClient.addDirector(movieDto, directorDto);
            refreshMoviesGrid();
//            movieGrid.focus();
//            movieGrid.setItems(movieDto);
        });
        directorFilter.setPlaceholder("Filter directors");
        directorFilter.setClearButtonVisible(true);
        directorFilter.setValueChangeMode(ValueChangeMode.EAGER);
        directorFilter.addValueChangeListener(action -> updateAllDirectorsGrid());
        directorAddMovieLayout.add(directorFilter, directorAdd);
        directorAllLayout.add(allDirectorGrid, directorAddMovieLayout);
        directorAllLayout.setSizeFull();

        writerCurrentMovieGrid.setColumns("firstname", "surname");
        writerDelete.addClickListener(action -> {
            MovieDto movieDto = movieGrid.asSingleSelect().getValue();
            WriterDto writerDto = writerCurrentMovieGrid.asSingleSelect().getValue();
            movieClient.deleteWriter(movieDto, writerDto);
            refreshMoviesGrid();
            movieGrid.select(movieDto);
        });
        writerCurrentMovieLayout.add(writerCurrentMovieGrid, writerDelete);
        writerCurrentMovieLayout.setSizeFull();

        allWriterGrid.setColumns("firstname", "surname");
        allWriterGrid.setSizeFull();
        writerAdd.addClickListener(action -> {
            MovieDto movieDto = movieGrid.asSingleSelect().getValue();
            WriterDto writerDto = allWriterGrid.asSingleSelect().getValue();
            movieClient.addWriter(movieDto, writerDto);
            refreshMoviesGrid();
        });
        writerFilter.setPlaceholder("Filter writers");
        writerFilter.setClearButtonVisible(true);
        writerFilter.setValueChangeMode(ValueChangeMode.EAGER);
        writerFilter.addValueChangeListener(action -> updateAllWritersGrid());
        writerAddMovieLayout.add(writerFilter, writerAdd);
        writerAllLayout.add(allWriterGrid, writerAddMovieLayout);
        writerAllLayout.setSizeFull();

        actorCurrentMovieGrid.setColumns("firstname", "surname");
        actorDelete.addClickListener(action -> {
            MovieDto movieDto = movieGrid.asSingleSelect().getValue();
            ActorDto actorDto = actorCurrentMovieGrid.asSingleSelect().getValue();
            movieClient.deleteActor(movieDto, actorDto);
            refreshMoviesGrid();
        });
        actorCurrentMovieLayout.add(actorCurrentMovieGrid, actorDelete);
        actorCurrentMovieLayout.setSizeFull();

        allActorGrid.setColumns("firstname", "surname");
        allActorGrid.setSizeFull();
        actorAdd.addClickListener(action -> {
            MovieDto movieDto = movieGrid.asSingleSelect().getValue();
            ActorDto actorDto = allActorGrid.asSingleSelect().getValue();
            movieClient.addActor(movieDto, actorDto);
            refreshMoviesGrid();
        });
        actorFilter.setPlaceholder("Filter actors");
        actorFilter.setClearButtonVisible(true);
        actorFilter.setValueChangeMode(ValueChangeMode.EAGER);
        actorFilter.addValueChangeListener(action -> updateAllActorsGrid());
        actorAddMovieLayout.add(actorFilter, actorAdd);
        actorAllLayout.add(allActorGrid, actorAddMovieLayout);
        actorAllLayout.setSizeFull();

        genreCurrentMovieGrid.setColumns("type");
        genreDelete.addClickListener(action -> {
            MovieDto movieDto = movieGrid.asSingleSelect().getValue();
            GenreDto genreDto = genreCurrentMovieGrid.asSingleSelect().getValue();
            movieClient.deleteGenre(movieDto, genreDto);
            refreshMoviesGrid();
        });
        genreCurrentMovieLayout.add(genreCurrentMovieGrid, genreDelete);
        genreCurrentMovieLayout.setSizeFull();

        allGenreGrid.setColumns("type");
        allGenreGrid.setSizeFull();
        genreAdd.addClickListener(action -> {
            MovieDto movieDto = movieGrid.asSingleSelect().getValue();
            GenreDto genreDto = allGenreGrid.asSingleSelect().getValue();
            movieClient.addGenre(movieDto, genreDto);
            refreshMoviesGrid();
        });
        genreFilter.setPlaceholder("Filter genres");
        genreFilter.setClearButtonVisible(true);
        genreFilter.setValueChangeMode(ValueChangeMode.EAGER);
        genreFilter.addValueChangeListener(action -> updateAllGenresGrid());
        genreAddMovieLayout.add(genreFilter, genreAdd);
        genreAllLayout.add(allGenreGrid, genreAddMovieLayout);
        genreAllLayout.setSizeFull();

        add(buttonBack, movieFilterAddLayout, movieViewAndModificationLayout);
        add(directorCurrentMovieLayout, directorAllLayout);
        add(writerCurrentMovieLayout, writerAllLayout);
        add(actorCurrentMovieLayout, actorAllLayout);
        add(genreCurrentMovieLayout, genreAllLayout);

        movieGrid.addItemClickListener(action -> {
            if (movieGrid.asSingleSelect().getValue().getDirectorDto() != null) {
                DirectorDto directorDto = movieGrid.asSingleSelect().getValue().getDirectorDto();
                directorCurrentMovieGrid.setItems(directorDto);
            }
            if (movieGrid.asSingleSelect().getValue().getWritersDto() != null) {
                List<WriterDto> writersDto = movieGrid.asSingleSelect().getValue().getWritersDto();
                writerCurrentMovieGrid.setItems(writersDto);
            }
            if (movieGrid.asSingleSelect().getValue().getActorsDto() != null) {
                List<ActorDto> actorsDto = movieGrid.asSingleSelect().getValue().getActorsDto();
                actorCurrentMovieGrid.setItems(actorsDto);
            }
            if (movieGrid.asSingleSelect().getValue().getGenresDto() != null) {
                List<GenreDto> genresDto = movieGrid.asSingleSelect().getValue().getGenresDto();
                genreCurrentMovieGrid.setItems(genresDto);
            }
        });

        setSizeFull();
        refreshMoviesGrid();

        movieGrid.asSingleSelect().addValueChangeListener(event -> setMovieDto(movieGrid.asSingleSelect().getValue()));
    }

    public void refreshMoviesGrid() {
        movieGrid.setItems(movieClient.getMovies());
        allDirectorGrid.setItems(directorClient.getDirectors());
        allWriterGrid.setItems((writerClient.getWriters()));
        allActorGrid.setItems(actorClient.getActors());
        allGenreGrid.setItems(genreClient.getGenres());
    }

    public void updateMoviesGrid() {
        movieGrid.setItems(movieClient.filterMovies(movieFilter.getValue()));
    }

    private void saveMovie() {
        MovieDto movieDto = movieBinder.getBean();
        movieDto.setId(0L);
        movieClient.createMovie(movieDto);
        refreshMoviesGrid();
//        setMovieDto(null);
    }

    private void updateMovie() {
        MovieDto movieDto = movieBinder.getBean();
        movieClient.updateMovie(movieDto);
        refreshMoviesGrid();
//        setMovieDto(null);
    }

    private void deleteMovie() {
        MovieDto movieDto = movieBinder.getBean();
        movieClient.deleteMovie(movieDto);
        refreshMoviesGrid();
//        setMovieDto(null);
    }

    public void setMovieDto(MovieDto movieDto) {
        movieBinder.setBean(movieDto);
//        if (movieDto == null) {
//            movieFormLayout.setVisible(false);
//
//        } else {
//            movieFormLayout.setVisible(true);
//            movieTitle.focus();
//        }
    }

    private void updateAllDirectorsGrid() {
        allDirectorGrid.setItems(directorClient.filterDirectors(directorFilter.getValue()));
    }

    private void updateAllWritersGrid() {
        allWriterGrid.setItems(writerClient.filterWriters(writerFilter.getValue()));
    }

    private void updateAllActorsGrid() {
        allActorGrid.setItems(actorClient.filterActors(actorFilter.getValue()));
    }

    private void updateAllGenresGrid() {
        allGenreGrid.setItems(genreClient.filterGenres(genreFilter.getValue()));
    }
}



