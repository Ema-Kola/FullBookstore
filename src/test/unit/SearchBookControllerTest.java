package test.unit;

import controller.FinanceController;
import controller.ManageLibraryController;
import controller.SearchBookController;
import controller.UserController;
import dao.BooksDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Author;
import model.Book;
import model.FinanceStatRecord;
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

public class SearchBookControllerTest extends ApplicationTest {
    private SearchBookController searchBookController;
    private final VerifyAlertTest verifyAlert = new VerifyAlertTest();
    @Mock private BooksDAO booksDao;
    private ObservableList<Book> books = FXCollections.observableArrayList();
    @Mock Book b1;
    @Mock Book b2;
    @Mock Book b3;

    @BeforeEach
    public void setup() {
        Author author1 = mock(Author.class);
        when(author1.getFirstName()).thenReturn("Author1First");
        when(author1.getLastName()).thenReturn("Author1Last");
        when(b1.getCategory()).thenReturn("Comedy");
        when(b1.getAuthor()).thenReturn(author1);

        Author author2 = mock(Author.class);
        when(author2.getFirstName()).thenReturn("Author2First");
        when(author2.getLastName()).thenReturn("Author2Last");
        when(b2.getCategory()).thenReturn("Thriller");
        when(b2.getAuthor()).thenReturn(author2);

        Author author3 = mock(Author.class);
        when(author3.getFirstName()).thenReturn("Author3First");
        when(author3.getLastName()).thenReturn("Author3Last");
        when(b3.getCategory()).thenReturn("Thriller");
        when(b3.getAuthor()).thenReturn(author3);
        System.out.println("Before each");

    }

    @Override
    public void start(Stage stage) {
        //this has to be done here since dao is passed as constructor and filterTable is called within constructor
        MockitoAnnotations.openMocks(this);
        when(b1.getIsbn()).thenReturn("12345");
        when(b2.getIsbn()).thenReturn("67890");
        when(b3.getIsbn()).thenReturn("11223");

        books.add(b1);
        books.add(b2);
        books.add(b3);
        when(b1.getTitle()).thenReturn("Book 1");
        when(b2.getTitle()).thenReturn("Book 2");
        when(b3.getTitle()).thenReturn("Book 3");

        when(b1.isActive()).thenReturn(true);
        when(b2.isActive()).thenReturn(true);
        when(b3.isActive()).thenReturn(true);
        when(booksDao.getAllActive()).thenReturn(books);

        searchBookController = new SearchBookController(new Stage(), new HomeView(new User(), new UserController(new Stage())),booksDao);
        stage.setScene(searchBookController.getView().showView(stage));
        stage.show();

        stage.show();
        System.out.println("start");
    }
    @Test
    void test_search_filter_isbn() {
        searchBookController.getView().getSearch().setText("Book 1");

        waitForFxEvents();

        TableView<Book> tableView = lookup(".table-view").queryAs(TableView.class);

        assertEquals(1, tableView.getItems().size());
        assertEquals("Book 1", tableView.getItems().getFirst().getTitle());
    }

    @Test
    void test_search_filter_Title() {
        searchBookController.getView().getSearch().setText("67890");

        waitForFxEvents();

        TableView<Book> tableView = lookup(".table-view").queryAs(TableView.class);

        assertEquals(1, tableView.getItems().size());
        assertEquals("Book 2", tableView.getItems().getFirst().getTitle());
    }

    @Test
    void test_search_filter_Author() {
        searchBookController.getView().getSearch().setText("Author2First");

        waitForFxEvents();

        TableView<Book> tableView = lookup(".table-view").queryAs(TableView.class);

        assertEquals(1, tableView.getItems().size());
        assertEquals("Book 2", tableView.getItems().getFirst().getTitle());
    }

    @Test
    void test_search_filter_Category() {
        searchBookController.getView().getSearch().setText("Thriller");

        waitForFxEvents();

        TableView<Book> tableView = lookup(".table-view").queryAs(TableView.class);

        assertEquals(2, tableView.getItems().size());
        assertEquals("Book 2", tableView.getItems().getFirst().getTitle());
        assertEquals("Book 3", tableView.getItems().get(1).getTitle());

    }
}
