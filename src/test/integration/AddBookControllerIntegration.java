package test.integration;
import controller.AddBookController;
import controller.UserController;
import dao.BooksDAO;
import dao.RestockBillDAO;
import javafx.application.Platform;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;
import view.AddBookView;
import view.HomeView;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;


import java.io.File;
import java.util.Date;


import static org.junit.jupiter.api.Assertions.*;

public class AddBookControllerIntegration extends ApplicationTest {

    private static final String TEST_BOOK_FILE = "testBooks.db"; // Example database file name
    private BooksDAO booksDAO;
    private RestockBillDAO restockBillDAO;
    private AddBookController addBookController;
    private AddBookView addBookView;

    @TempDir
    static File tempFolder;
    static File tempFileBooks;
    static File tempFileRestock;

    @Override
    public void start(Stage stage) throws Exception {
        User user = new User("name", "sur",new Date(),Gender.FEMALE,"emko", "emakola22@gmail.com", "321",   Role.ADMINISTRATOR, "0695730257",0);
        addBookController = new AddBookController(new Stage(), new HomeView(user, new UserController(new Stage())));

        Scene scene = addBookController.getView().showView(stage);
        this.addBookView = addBookController.getView();
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    void setup(){
        tempFileBooks = new File(tempFolder, TEST_BOOK_FILE);
        booksDAO = new BooksDAO(tempFileBooks.getAbsolutePath());

        tempFileRestock = new File(tempFolder, TEST_BOOK_FILE);
        restockBillDAO = new RestockBillDAO(tempFileRestock.getAbsolutePath());

        addBookController.setDao(booksDAO);
        addBookController.setRestockDao(restockBillDAO);
    }

    @Test
    public void book_created_integration_test() {

        addBookView.getIsbnTF().setText("777-77-77777-77-7");
        addBookView.getTitleTF().setText("Test Book");
        addBookView.getSupplierTF().setText("Supplier");
        addBookView.getSellingpriceTF().setText("10.0");
        addBookView.getPurchasedpriceTF().setText("5.0");
        addBookView.getStockTF().setText("50");
        addBookView.getDescriptionTA().setText("Description");

        Platform.runLater(() -> {
            Author author = new Author("John", "Doe", Gender.MALE);
            addBookView.getAuthorComboBox().setValue(author);
            addBookView.getCategoryComboBox().setValue("Fiction");
        });

        waitForFxEvents();
        clickOn(addBookController.getView().getSubmitBtn());

        waitForFxEvents();

        Book savedBook = booksDAO.searchBook("777-77-77777-77-7");

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        assertEquals("Supplier", savedBook.getSupplier());
        assertEquals(10.0, savedBook.getSellingPrice());
        assertEquals(50, savedBook.getStock());
    }

    @Test
    public void book_exists_integration_test() {
        // First, insert a book with the same ISBN to simulate existing book
        Book existingBook = new Book("444-44-44444-44-4", "Existing Book", "Description", "Supplier", 20.0, 10.0, new Author("Jane", "Doe", Gender.FEMALE), 100, "Category");
        booksDAO.create(existingBook);

        addBookView.getIsbnTF().setText("444-44-44444-44-4");
        addBookView.getTitleTF().setText("New Book");
        addBookView.getSupplierTF().setText("Supplier");
        addBookView.getSellingpriceTF().setText("15.0");
        addBookView.getPurchasedpriceTF().setText("8.0");
        addBookView.getStockTF().setText("10");
        addBookView.getDescriptionTA().setText("Description");

        Platform.runLater(() -> {
            Author author = new Author("John", "Doe", Gender.MALE);
            addBookView.getAuthorComboBox().setValue(author);
            addBookView.getCategoryComboBox().setValue("Fiction");
        });

        waitForFxEvents();

        clickOn(addBookController.getView().getSubmitBtn());

        waitForFxEvents();

        Book foundBook = booksDAO.searchBook("444-44-44444-44-4");
        assertNotNull(foundBook);
        assertEquals("Existing Book", foundBook.getTitle());
    }

}