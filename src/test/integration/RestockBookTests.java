package test.integration;

import controller.RestockExistingBookController;
import controller.UserController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;
import view.AddBookView;
import view.HomeView;
import view.RestockExistingBookView;
import java.io.File;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;
import dao.BooksDAO;
import dao.RestockBillDAO;

public class RestockBookTests extends ApplicationTest {

    private RestockExistingBookView view;
    private BooksDAO booksDAO;

    @TempDir
    File tempDir;

    @Override
    public void start(Stage stage) {
        File booksFile = new File(tempDir, "books.dat");
        File restockBillsFile = new File(tempDir, "restockBills.dat");
        booksDAO = new BooksDAO(booksFile.getPath());
        RestockBillDAO restockBillDAO = new RestockBillDAO(restockBillsFile.getPath());

        RestockExistingBookController controller = new RestockExistingBookController(new AddBookView(new HomeView(new User("Ema", "Kola", new Date(), Gender.FEMALE, "emko", "emakola22@gmail.com", "321", Role.LIBRARIAN, "0695730257", 0), new UserController(stage))));
        controller.setBooksDAO(booksDAO);
        controller.setRestockBillDAO(restockBillDAO);

        view = controller.getView();
        Scene scene = view.showView(stage);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setup() {
        booksDAO.getAll().clear();
        booksDAO.create(new Book("123-45-67890-12-3","Net te bardha", "desc", "Pika Pa Siperfaqe",350,500,new Author("Fyodor", "Dostoevsky", Gender.MALE),5,"Novel"));
        waitForFxEvents();

        interact(() -> {
            view.getIsbnTF().clear();
            view.getQuantityTF().clear();
            view.getPurchasedpriceTF().clear();
        });
    }


    @Test
    public void testRestockBookSuccessfully() {
        interact(() -> {
            view.getIsbnTF().setText("123-45-67890-12-3");
            view.getQuantityTF().setText("5");
            view.getPurchasedpriceTF().setText("50.00");
        });
        waitForFxEvents();

        clickOn(view.getRestockButton());

        Book restockedBook = booksDAO.searchBook("123-45-67890-12-3");
        assertNotNull(restockedBook);
        assertEquals(10, restockedBook.getStock());
    }

    @Test
    public void testRestockFailsOnInvalidISBN() {
        interact(() -> {
            view.getIsbnTF().setText("000-00-00000-00-0");
            view.getQuantityTF().setText("5");
            view.getPurchasedpriceTF().setText("50.00");
        });
        waitForFxEvents();

        clickOn(view.getRestockButton());

        assertNull(booksDAO.searchBook("000-00-00000-00-0"));
    }

    @Test
    public void testRestockFailsWhenBookInactive() {
        Book inactiveBook = new Book("111-11-11111-11-1", "Inactive Book", "desc", "Publisher", 200, 400,
                new Author("Inactive", "Author", Gender.MALE), 0, "Novel");
        booksDAO.create(inactiveBook);
        booksDAO.searchBook("111-11-11111-11-1").setActive(false);
        booksDAO.updateAll();

        interact(() -> {
            view.getIsbnTF().setText("111-11-11111-11-1");
            view.getQuantityTF().setText("5");
            view.getPurchasedpriceTF().setText("50.00");
        });
        waitForFxEvents();

        clickOn(view.getRestockButton());

        Book retrievedBook = booksDAO.searchBook("111-11-11111-11-1");
        assertNotNull(retrievedBook);
        assertEquals(0, retrievedBook.getStock());
        assertFalse(retrievedBook.isActive());
    }
}
