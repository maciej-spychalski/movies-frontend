package pl.asbt.moviesfrontend.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import pl.asbt.moviesfrontend.client.*;
import pl.asbt.moviesfrontend.domain.User;
import pl.asbt.moviesfrontend.dto.*;
import pl.asbt.moviesfrontend.session.SessionVariables;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "moviesShop/shop")
public class ShopView extends VerticalLayout {

    private User currentUser = SessionVariables.getInstance().getCurrentUser();
    private UserClient userClient = UserClient.getInstance();
    private MovieClient movieClient = MovieClient.getInstance();
    private DescriptionClient descriptionClient = DescriptionClient.getInstance();
    private CartClient cartClient = CartClient.getInstance();
    private OrderClient orderClient = OrderClient.getInstance();
    private ItemClient itemClient = ItemClient.getInstance();
    private DirectorClient directorClient = DirectorClient.getInstance();
    private WriterClient writerClient = WriterClient.getInstance();
    private ActorClient actorClient = ActorClient.getInstance();
    private GenreClient genreClient = GenreClient.getInstance();

    //Movie
    private HorizontalLayout movieLayout = new HorizontalLayout();
    private FormLayout movieFormLayout = new FormLayout();
    private TextField movieFilter = new TextField();
    private IntegerField movieQuantity = new IntegerField("Quantity");
    private Button addToCart = new Button("Add to Cart");
    private Grid<MovieDto> movieGrid = new Grid<>(MovieDto.class);

    private TextArea movieDescription = new TextArea();

    //Cart-Items
    private HorizontalLayout cartLayout = new HorizontalLayout();
    private HorizontalLayout cartFormButtonLayout = new HorizontalLayout();
    private FormLayout cartFormLayout = new FormLayout();
    private TextField cartItemMovieTitle = new TextField("Movie title");
    private IntegerField cartItemQuantity = new IntegerField("Quantity");
    private Button updateItem = new Button("Update item");
    private Button deleteItem = new Button("Delete item");
    private Button makeOrder = new Button("Make order");
    private Binder<ItemDto> cartBinder = new Binder<>(ItemDto.class);
    private Grid<ItemDto> cartGrid = new Grid<>(ItemDto.class);

    //Order
    private HorizontalLayout orderLayout = new HorizontalLayout();
    private FormLayout orderFormLayout = new FormLayout();
    private Button deleteOrder = new Button("Delete order");
    private Button finalizeOrder = new Button("Pay order");
    private Grid<OrderDto> orderGrid = new Grid<>(OrderDto.class);

    public ShopView() {
        // Movies
        movieFilter.setPlaceholder("Filter movie");
        movieFilter.setClearButtonVisible(true);
        movieFilter.setValueChangeMode(ValueChangeMode.EAGER);
        movieFilter.addValueChangeListener(action -> updateMoviesGrid());
        movieQuantity.setHasControls(true);
        movieQuantity.setValue(1);
        movieQuantity.setMin(1);
        addToCart.addClickListener(action -> {
            ItemDto itemDto = new ItemDto();
            itemDto.setId(0L);
            itemDto.setMovieId(movieGrid.asSingleSelect().getValue().getId());
            itemDto.setMovieTitle(movieGrid.asSingleSelect().getValue().getTitle());
            itemDto.setQuantity(movieQuantity.getValue());
            itemDto.setPrice(new BigDecimal(0));
            itemDto = itemClient.createItem(itemDto);
            cartClient.addItem(currentUser.getCartId(), itemDto.getId());
            refreshCartGrid();
        });
        movieFormLayout.add(movieFilter, movieQuantity, addToCart);

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

        refreshMoviesGrid();
        movieLayout.add(movieGrid, movieFormLayout);
        movieLayout.setSizeFull();

        movieGrid.asSingleSelect().addValueChangeListener(event -> {
            String movieTitle = movieGrid.asSingleSelect().getValue().getTitle();
            DescriptionDto descriptionDto = descriptionClient.getDescription(movieTitle);
            movieDescription.setValue(descriptionDto.getPlot());
         });

        movieDescription.setSizeFull();

        //Cart
        refreshCartGrid();
        cartItemMovieTitle.setEnabled(false);
        cartItemQuantity.setHasControls(true);
        cartItemQuantity.setMin(1);
        updateItem.addClickListener(action -> {
            ItemDto itemDto = cartGrid.asSingleSelect().getValue();
            itemDto.setQuantity(cartItemQuantity.getValue());
            itemClient.updateItem(itemDto);
            // todo:
            cartClient.updateCarPrice(currentUser.getCartId());
            refreshCartGrid();
        });
        deleteItem.addClickListener(action -> {
            ItemDto itemDto = cartGrid.asSingleSelect().getValue();
            cartClient.deleteItem(currentUser.getCartId(), itemDto.getId());
            refreshCartGrid();
        });
        makeOrder.addClickListener(action -> {
            cartClient.createOrder(currentUser.getCartId(), currentUser.getUserId());
            refreshCartGrid();
            refreshOrderGrid();
        });
        cartFormButtonLayout.add(updateItem, deleteItem, makeOrder);
        cartBinder.forField(cartItemMovieTitle).bind(ItemDto::getMovieTitle, ItemDto::setMovieTitle);
        cartBinder.forField(cartItemQuantity).bind(ItemDto::getQuantity, ItemDto::setQuantity);

        cartFormLayout.add(cartItemMovieTitle, cartItemQuantity, cartFormButtonLayout);
        cartGrid.setColumns("movieTitle", "quantity", "price");
        cartGrid.setSizeFull();
        cartGrid.asSingleSelect().addValueChangeListener(event -> setCartItemDto(cartGrid.asSingleSelect().getValue()));
        cartLayout.add(cartGrid, cartFormLayout);
        cartLayout.setSizeFull();

        //Order
        orderGrid.setColumns("isFinalized", "price");
        deleteOrder.addClickListener(action -> {
            OrderDto orderDto = orderGrid.asSingleSelect().getValue();
            orderClient.deleteOrder(orderDto);
            refreshOrderGrid();
        });
        finalizeOrder.addClickListener(action -> {
            OrderDto orderDto = orderGrid.asSingleSelect().getValue();
            orderClient.finalizeOrder(orderDto.getId());
            refreshOrderGrid();
        });
        orderFormLayout.add(deleteOrder, finalizeOrder);
        orderLayout.add(orderGrid, orderFormLayout);
        orderLayout.setSizeFull();
        refreshOrderGrid();

        Button buttonLogout = new Button("Logout");
        buttonLogout.addClickListener(action -> {
            SessionVariables.getInstance().setCurrentUser(new User());
            UI.getCurrent().navigate(MainView.class);
        });
        add(buttonLogout, movieLayout, movieDescription, cartLayout, orderLayout);
        setSizeFull();
    }


    public void refreshMoviesGrid() {
        movieGrid.setItems(movieClient.getMovies());
    }

    public void updateMoviesGrid() {
        movieGrid.setItems(movieClient.filterMovies(movieFilter.getValue()));
    }

    public void refreshCartGrid() {
        List<ItemDto> itemsDto = cartClient.getCart(currentUser.getCartId()).getItemsDto();
        cartGrid.setItems(itemsDto);
    }

    public void refreshOrderGrid() {
        UserDto userDto = userClient.getUser(currentUser.getUserId());
        List<OrderDto> ordersDto = userDto.getOrdersDto();
        orderGrid.setItems(ordersDto);
    }

    public void setCartItemDto(ItemDto itemDto) {
        cartBinder.setBean(itemDto);
    }
}

