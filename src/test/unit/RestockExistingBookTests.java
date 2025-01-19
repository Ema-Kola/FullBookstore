package test.unit;

import controller.RestockExistingBookController;
import controller.UserController;
import dao.BooksDAO;
import dao.RestockBillDAO;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import view.AddBookView;
import view.HomeView;
import view.RestockExistingBookView;

import static org.mockito.Mockito.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class RestockExistingBookTests extends ApplicationTest {

    private RestockExistingBookController controller;
    private RestockExistingBookView view;
    private BooksDAO mockBooksDAO;
    private RestockBillDAO mockRestockBillDAO;

    @Override
    public void start(Stage stage) {
        mockBooksDAO = mock(BooksDAO.class);
        mockRestockBillDAO = mock(RestockBillDAO.class);
        controller = new RestockExistingBookController(new AddBookView(new HomeView(new User(), new UserController(stage))));
        view = controller.getView();
        controller.setBooksDAO(mockBooksDAO);
        controller.setRestockBillDAO(mockRestockBillDAO);
        Scene scene = view.showView(stage);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setup() {
        interact(() -> {
            view.getIsbnTF().clear();
            view.getQuantityTF().clear();
            view.getPurchasedpriceTF().clear();
        });
        waitForFxEvents();
    }

    @Test
    public void testRestockButtonDisabled() {
        verifyThat(view.getRestockButton(), isDisabled());
    }

    @Test
    public void testRestockButtonEnabled() {
        interact(() -> {
            view.getIsbnTF().setText("123-45-67890-12-3");
            view.getQuantityTF().setText("10");
            view.getPurchasedpriceTF().setText("100.50");
        });
        waitForFxEvents();

        verifyThat(view.getRestockButton(), isEnabled());
    }

    @Test
    public void testRestockBookSuccessfully() {
//        Book mockBook = new Book();
        Book mockBook = new Book("123-45-77800-12-3","Net te bardha", "desc", "Pika Pa Siperfaqe",350,500,new Author("Fyodor", "Dostoevsky", Gender.MALE),5,"Novel");
        mockBook.setActive(true);


        when(mockBooksDAO.searchBook("123-45-77800-12-3")).thenReturn(mockBook);
        when(mockBooksDAO.updateAll()).thenReturn(true);
        when(mockRestockBillDAO.create(any(RestockBill.class))).thenReturn(true);

        interact(() -> {
            view.getIsbnTF().setText("123-45-77800-12-3");
            view.getQuantityTF().setText("5");
            view.getPurchasedpriceTF().setText("50.00");
        });
        waitForFxEvents();
        controller.setBooksDAO(mockBooksDAO);
        controller.setRestockBillDAO(mockRestockBillDAO);
        clickOn(view.getRestockButton());
        controller.setBooksDAO(mockBooksDAO);
        controller.setRestockBillDAO(mockRestockBillDAO);

        verify(mockBooksDAO).updateAll();
        verify(mockRestockBillDAO).create(any(RestockBill.class));
    }

    @Test
    public void testRestockBookFailsOnInvalidISBN() {
        when(mockBooksDAO.searchBook("123-12-12345-12-1")).thenReturn(null);

        interact(() -> {
            view.getIsbnTF().setText("123-12-12345-12-1");
            view.getQuantityTF().setText("5");
            view.getPurchasedpriceTF().setText("50.00");
        });
        waitForFxEvents();

        clickOn(view.getRestockButton());

        verify(mockBooksDAO, never()).updateAll();
        verify(mockRestockBillDAO, never()).create(any(RestockBill.class));
    }

    @Test
    public void testRestockBookFailsWhenBookInactive() {
        Book mockBook = new Book();
        mockBook.setActive(false);

        when(mockBooksDAO.searchBook("123-45-67890-12-3")).thenReturn(mockBook);

        interact(() -> {
            view.getIsbnTF().setText("123-45-67890-12-3");
            view.getQuantityTF().setText("5");
            view.getPurchasedpriceTF().setText("50.00");
        });
        waitForFxEvents();

        clickOn(view.getRestockButton());

        verify(mockBooksDAO, never()).updateAll();
        verify(mockRestockBillDAO, never()).create(any(RestockBill.class));
    }

    @Test
    public void testRestockFailsWhenUpdateFails() {
        //Book mockBook = new Book();
        Book mockBook = new Book("123-45-67890-12-3","Net te bardha", "desc", "Pika Pa Siperfaqe",350,500,new Author("Fyodor", "Dostoevsky", Gender.MALE),5,"Novel");
        mockBook.setActive(true);

        when(mockBooksDAO.searchBook("123-45-67890-12-3")).thenReturn(mockBook);
        when(mockBooksDAO.updateAll()).thenReturn(false);

        interact(() -> {
            view.getIsbnTF().setText("123-45-67890-12-3");
            view.getQuantityTF().setText("5");
            view.getPurchasedpriceTF().setText("50.00");
        });
        waitForFxEvents();

        clickOn(view.getRestockButton());

        verify(mockBooksDAO).updateAll();
        verify(mockRestockBillDAO, never()).create(any(RestockBill.class));
    }
}
