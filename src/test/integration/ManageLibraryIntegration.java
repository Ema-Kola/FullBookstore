package test.integration;

import controller.ManageLibraryController;
import controller.UserController;
import dao.BooksDAO;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;
import test.unit.VerifyAlertTest;
import view.HomeView;
import view.ManageLibraryView;
import java.io.File;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class ManageLibraryIntegration extends ApplicationTest {

    VerifyAlertTest verifyAlert = new VerifyAlertTest();
    private static final String TEST_BOOK_FILE = "testBooks.dat";
    private BooksDAO booksDAO;
    private ManageLibraryController manageLibraryController;
    private ManageLibraryView manageLibraryView;

    @TempDir
    static File tempFolder;
    static File tempFileBooks;

    @Override
    public void start(Stage stage) throws Exception {
        User user = new User("name", "sur", new Date(), Gender.FEMALE, "emko", "emakola22@gmail.com", "321", Role.ADMINISTRATOR, "0695730257", 0);
        tempFileBooks = new File(tempFolder, TEST_BOOK_FILE);
        booksDAO = new BooksDAO(tempFileBooks.getAbsolutePath());

        manageLibraryController = new ManageLibraryController(stage, new HomeView(user, new UserController(stage)), booksDAO);
        Scene scene = manageLibraryController.getView().showView(stage);
        this.manageLibraryView = manageLibraryController.getView();
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    void setup() {
        booksDAO.getAll().clear();
        // 2 active 1 inactive
        Book b1 = new Book("12345", "Book 1", "Description", "Supplier", 50.0, 50.0, new Author("Author", "One", Gender.MALE), 100, "Comedy");
        Book b2 = new Book("67890", "Book 2", "Description", "Supplier", 60.0, 40.0, new Author("Author", "Two", Gender.FEMALE), 150, "Action");
        Book b3 = new Book("11223", "Book 3", "Description", "Supplier", 70.0, 50.0, new Author("Author", "Three", Gender.FEMALE), 200, "Drama");

        b1.setActive(true);
        b2.setActive(true);
        b3.setActive(false);
        booksDAO.create(b1);
        booksDAO.create(b2);
        booksDAO.create(b3);
    }

    @Test
    public void test_filter_table_active_integration() {
        Platform.runLater(() -> manageLibraryView.getChoiceBox().setValue("Active"));
        waitForFxEvents();

        TableView<Book> tableView = lookup(".table-view").queryAs(TableView.class);
        assertEquals(2, tableView.getItems().size());
    }

    @Test
    public void test_filter_table_inactive_integration() {
        Platform.runLater(() -> manageLibraryView.getChoiceBox().setValue("Inactive"));
        waitForFxEvents();

        TableView<Book> tableView = lookup(".table-view").queryAs(TableView.class);
        assertEquals(1, tableView.getItems().size());
    }

    @Test
    public void sellingPriceEdit() {
        TablePosition<Book, Double> tablePosition = new TablePosition<>(manageLibraryView.getTableView(), 0, manageLibraryView.getSellingPriceColumn());

        Book book = booksDAO.searchBook("12345");
        assertEquals(50.0, book.getSellingPrice());

        TableColumn.CellEditEvent<Book, Double> cellEditEvent = new TableColumn.CellEditEvent<>(
                manageLibraryView.getTableView(), tablePosition, TableColumn.editCommitEvent(), 30.0
        );

        Platform.runLater(() -> {
            manageLibraryView.getSellingPriceColumn().getOnEditCommit().handle(cellEditEvent);
            waitForFxEvents();
            verifyAlert.verifyAlert("Selling Price changed! 30.0");
            press(KeyCode.ENTER).release(KeyCode.ENTER);

        });
        waitForFxEvents();

        Platform.runLater(()->manageLibraryView.getTableView().refresh());

        double editedCell = manageLibraryView.getSellingPriceColumn().getCellData(0);
        assertEquals(30.0, editedCell);

        clickOn(manageLibraryView.getEditBtn());

        Book book1 = booksDAO.searchBook("12345");
        assertEquals(30.0, book1.getSellingPrice());

    }


    @Test
    public void NotAcceptableSellingPriceEdit() {
        TablePosition<Book, Double> tablePosition = new TablePosition<>(manageLibraryView.getTableView(), 0, manageLibraryView.getSellingPriceColumn());

        Book book = booksDAO.searchBook("12345");
        assertEquals(50.0, book.getSellingPrice());

        TableColumn.CellEditEvent<Book, Double> cellEditEvent = new TableColumn.CellEditEvent<>(
                manageLibraryView.getTableView(), tablePosition, TableColumn.editCommitEvent(), -1.0
        );

        Platform.runLater(() -> {
            manageLibraryView.getSellingPriceColumn().getOnEditCommit().handle(cellEditEvent);
            waitForFxEvents();
            verifyAlert.verifyAlert("Selling Price is not valid");
            press(KeyCode.ENTER).release(KeyCode.ENTER);

        });
        waitForFxEvents();

        Platform.runLater(()->manageLibraryView.getTableView().refresh());
        clickOn(manageLibraryView.getEditBtn());

        Book book1 = booksDAO.searchBook("12345");// should not change naturally
        assertEquals(50.0, book1.getSellingPrice());

    }
}
