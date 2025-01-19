package test.unit;
import controller.ManageLibraryController;
import controller.UserController;
import dao.BooksDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Author;
import model.Book;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;
import view.HomeView;
import view.ManageLibraryView;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class ManageLibraryTest extends ApplicationTest {
    private final VerifyAlertTest verifyAlert = new VerifyAlertTest();
    @Mock private BooksDAO booksDao;
    @Mock private ManageLibraryView view;
    private ManageLibraryController manageLibraryController;
    private ObservableList<Book> books = FXCollections.observableArrayList();
    @Mock Book b1;
    @Mock Book b2;
    @Mock Book b3;

    @BeforeEach
    public void setup() {

        System.out.println("Before each");
        when(b1.getIsbn()).thenReturn("12345");
        Author author1 = mock(Author.class);
        when(author1.getFirstName()).thenReturn("Author1First");
        when(author1.getLastName()).thenReturn("Author1Last");
        when(b1.getAuthor()).thenReturn(author1);

        when(b2.getIsbn()).thenReturn("67890");
        Author author2 = mock(Author.class);
        when(author2.getFirstName()).thenReturn("Author2First");
        when(author2.getLastName()).thenReturn("Author2Last");
        when(b2.getAuthor()).thenReturn(author2);

        when(b3.getIsbn()).thenReturn("11223");
        Author author3 = mock(Author.class);
        when(author3.getFirstName()).thenReturn("Author3First");
        when(author3.getLastName()).thenReturn("Author3Last");
        when(b3.getAuthor()).thenReturn(author3);
    }

    @Override
    public void start(Stage stage) {
        //this has to be done here since dao is passed as constructor and filterTable is called within constructor
        MockitoAnnotations.openMocks(this);

        books.add(b1);
        books.add(b2);
        books.add(b3);
        when(b1.getTitle()).thenReturn("Book 1");
        when(b2.getTitle()).thenReturn("Book 2");
        when(b3.getTitle()).thenReturn("Book 3");

        when(b1.getDescription()).thenReturn("Something old");
        when(b1.getSellingPrice()).thenReturn(50.0);
        when(b1.getCategory()).thenReturn("Comedy");

        when(b1.isActive()).thenReturn(true);
        when(b2.isActive()).thenReturn(true);
        when(b3.isActive()).thenReturn(false);
        when(booksDao.getAll()).thenReturn(books);

        manageLibraryController = new ManageLibraryController(new Stage(), new HomeView(new User(), new UserController(new Stage())), booksDao);
        view = manageLibraryController.getView();

        stage.setScene(manageLibraryController.getView().showView(stage));
        stage.show();
        System.out.println("start");
    }


    @Test
    void test_filter_table_active(){

        TableView<Book> tableView = lookup(".table-view").queryAs(TableView.class);

        Platform.runLater(() -> view.getChoiceBox().setValue("Active"));

        waitForFxEvents();

        assertEquals(2, tableView.getItems().size());

    }

    @Test
    void test_filter_table_inactive(){

        TableView<Book> tableView = lookup(".table-view").queryAs(TableView.class);

        Platform.runLater(() -> view.getChoiceBox().setValue("Inactive"));
        System.out.println(manageLibraryController.getDao().getClass());
        System.out.println(tableView.getItems().size());

        waitForFxEvents();

        assertEquals(1, tableView.getItems().size());

    }

    @Test
    void test_search_filter_Author() {
        view.getSearchBook().setText("Author2First");

        waitForFxEvents();

        TableView<Book> tableView = lookup(".table-view").queryAs(TableView.class);

        assertEquals(1, tableView.getItems().size());
        assertEquals("Book 2", tableView.getItems().getFirst().getTitle());
    }

    @Test
    void test_search_filter_Author_Inactive() {
        view.getSearchBook().setText("Author3Last");
        Platform.runLater(() -> view.getChoiceBox().setValue("Inactive"));

        waitForFxEvents();

        TableView<Book> tableView = lookup(".table-view").queryAs(TableView.class);

        assertEquals(1, tableView.getItems().size());
        assertEquals("Book 3", tableView.getItems().getFirst().getTitle());
    }

    @Test
    public void testSetEditListeners_sellingPriceEdit() {

        TablePosition<Book, Double> tablePosition = new TablePosition<>(view.getTableView(), 0, view.getSellingPriceColumn());

        TableColumn.CellEditEvent<Book, Double> cellEditEvent = new TableColumn.CellEditEvent<>(
                view.getTableView(), tablePosition, TableColumn.editCommitEvent(), 30.0
        );

        Platform.runLater(() -> {
            view.getSellingPriceColumn().getOnEditCommit().handle(cellEditEvent);
            waitForFxEvents();
            verifyAlert.verifyAlert("Selling Price changed! 30.0");
        });


    }

    @Test
    public void testSetEditListeners_NullSellingPriceEdit() {

        TablePosition<Book, Double> tablePosition = new TablePosition<>(view.getTableView(), 0, view.getSellingPriceColumn());

        TableColumn.CellEditEvent<Book, Double> cellEditEvent = new TableColumn.CellEditEvent<>(
                view.getTableView(), tablePosition, TableColumn.editCommitEvent(), null
        );

        Platform.runLater(() -> {
            view.getSellingPriceColumn().getOnEditCommit().handle(cellEditEvent);

            waitForFxEvents();
            verifyAlert.verifyAlert("Selling Price cannot be empty");
        });
    }

    @Test
    public void testSetEditListeners_EditDescription() {

        TablePosition<Book, String> tablePosition = new TablePosition<>(view.getTableView(), 0, view.getDescriptionColumn());

        TableColumn.CellEditEvent<Book, String> cellEditEvent = new TableColumn.CellEditEvent<>(
                view.getTableView(),tablePosition,TableColumn.editCommitEvent(),"Something new"
        );

        Platform.runLater(() -> {
            view.getDescriptionColumn().getOnEditCommit().handle(cellEditEvent);

            waitForFxEvents();
            verifyAlert.verifyAlert("Description changed!");
        });
    }

    @Test
    public void testSetEditListeners_categoryEdit() {

        TablePosition<Book, String> tablePosition = new TablePosition<>(view.getTableView(), 0, view.getCategoryColumn());

        TableColumn.CellEditEvent<Book, String> cellEditEvent = new TableColumn.CellEditEvent<>(
                view.getTableView(), tablePosition, TableColumn.editCommitEvent(), "Action"
        );

        Platform.runLater(() -> {
            view.getCategoryColumn().getOnEditCommit().handle(cellEditEvent);
            waitForFxEvents();
            verifyAlert.verifyAlert("Category changed! Action");
        });


    }
}