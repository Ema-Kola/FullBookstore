package test.unit;

import controller.AddBookController;
import controller.UserController;
import dao.BooksDAO;
import dao.RestockBillDAO;
import javafx.application.Platform;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import view.AddBookView;
import view.HomeView;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import org.testfx.framework.junit5.ApplicationTest;


public class AddBookControllerTest extends ApplicationTest {

    @Mock private BooksDAO mockBooksDao;
    @Mock private RestockBillDAO mockRestockBillDAO;
    private AddBookController addBookController;
    @Mock private AddBookView mockAddBookView;
    @Mock private Author mockAuthor;

    @BeforeEach
    public void setup() {
        addBookController.setDao(mockBooksDao);
        addBookController.setRestockDao(mockRestockBillDAO);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MockitoAnnotations.openMocks(this);
        addBookController = new AddBookController(new Stage(), new HomeView(new User(), new UserController(new Stage())));
        mockAddBookView = addBookController.getView();
        stage.setScene(addBookController.getView().showView(stage));
        stage.show();
    }

    @Test
    public void book_created() {
        mockAddBookView.getIsbnTF().setText("123-12-12345-12-8");
        mockAddBookView.getTitleTF().setText("Test Book");
        mockAddBookView.getSupplierTF().setText("sth");
        mockAddBookView.getSellingpriceTF().setText("10.0");
        mockAddBookView.getPurchasedpriceTF().setText("5.0");
        mockAddBookView.getStockTF().setText("50");
        mockAddBookView.getDescriptionTA().setText("description");

        Platform.runLater(() -> {
            mockAddBookView.getAuthorComboBox().setValue(mockAuthor);
            mockAddBookView.getCategoryComboBox().setValue("category");
        });

        when(mockBooksDao.searchBook("123-12-12345-12-8")).thenReturn(null);
        when(mockBooksDao.create(any(Book.class))).thenReturn(true);
        when(mockRestockBillDAO.create(any(RestockBill.class))).thenReturn(true);
        clickOn(addBookController.getView().getSubmitBtn());

        waitForFxEvents();
        //verify(mockBooksDao, times(1)).create(any(Book.class));
        //verify(mockBooksDao, times(1)).searchBook("123-12-12345-12-8");

        verifyAlert("Book saved successfully!");
    }

    @Test
    public void book_exists() {
        mockAddBookView.getIsbnTF().setText("123-12-12345-12-1");
        mockAddBookView.getTitleTF().setText("Test Book");
        mockAddBookView.getSupplierTF().setText("sth");
        mockAddBookView.getSellingpriceTF().setText("10.0");
        mockAddBookView.getPurchasedpriceTF().setText("5.0");
        mockAddBookView.getStockTF().setText("50");
        mockAddBookView.getDescriptionTA().setText("description");
        Platform.runLater(() -> {
            mockAddBookView.getAuthorComboBox().setValue(mockAuthor);
            mockAddBookView.getCategoryComboBox().setValue("category");
        });

        addBookController.setDao(mockBooksDao);
        Book mockBook = mock(Book.class);
        when(mockBooksDao.searchBook("123-12-12345-12-1")).thenReturn(mockBook);

        clickOn(addBookController.getView().getSubmitBtn());

        waitForFxEvents();

        verifyAlert("Book with this ISBN already exists in the library.\nGo to the restock button if you want to restock an existing book.");
    }

    private void verifyAlert(String expectedMsg) {
        DialogPane dialogPane = lookup(".alert").queryAs(DialogPane.class);
        assertNotNull(dialogPane);
        assertEquals(expectedMsg, dialogPane.getContentText(), "Alert message should match");
    }

    @Test
    public void test_no_Isbn() {
        //mockAddBookView.getIsbnTF().setText("");
        mockAddBookView.getTitleTF().setText("Test Book");
        mockAddBookView.getSupplierTF().setText("sth");
        mockAddBookView.getSellingpriceTF().setText("10.0");
        mockAddBookView.getPurchasedpriceTF().setText("5.0");
        mockAddBookView.getStockTF().setText("50");
        mockAddBookView.getDescriptionTA().setText("description");
        Platform.runLater(() -> {
            mockAddBookView.getAuthorComboBox().setValue(mockAuthor);
            mockAddBookView.getCategoryComboBox().setValue("category");
        });

        waitForFxEvents();
        assertTrue(mockAddBookView.getSubmitBtn().isDisabled());

    }

    @Test
    public void selling_invalid() {
        mockAddBookView.getIsbnTF().setText("123-12-12345-12-1");
        mockAddBookView.getTitleTF().setText("Test Book");
        mockAddBookView.getSupplierTF().setText("sth");
        mockAddBookView.getSellingpriceTF().setText("-10.0");
        mockAddBookView.getPurchasedpriceTF().setText("5.0");
        mockAddBookView.getStockTF().setText("50");
        mockAddBookView.getDescriptionTA().setText("description");
        Platform.runLater(() -> {
            mockAddBookView.getAuthorComboBox().setValue(mockAuthor);
            mockAddBookView.getCategoryComboBox().setValue("category");
        });

        waitForFxEvents();
        assertTrue(mockAddBookView.getSubmitBtn().isDisabled());
    }

    @Test
    public void stock_invalid() {
        mockAddBookView.getIsbnTF().setText("123-12-12345-12-1");
        mockAddBookView.getTitleTF().setText("Test Book");
        mockAddBookView.getSupplierTF().setText("sth");
        mockAddBookView.getSellingpriceTF().setText("10.0");
        mockAddBookView.getPurchasedpriceTF().setText("5.0");
        mockAddBookView.getStockTF().setText("-50");
        mockAddBookView.getDescriptionTA().setText("description");
        Platform.runLater(() -> {
            mockAddBookView.getAuthorComboBox().setValue(mockAuthor);
            mockAddBookView.getCategoryComboBox().setValue("category");
        });

        waitForFxEvents();
        assertTrue(mockAddBookView.getSubmitBtn().isDisabled());
    }

    @Test
    public void purchase_invalid() {
        mockAddBookView.getIsbnTF().setText("123-12-12345-12-1");
        mockAddBookView.getTitleTF().setText("Test Book");
        mockAddBookView.getSupplierTF().setText("sth");
        mockAddBookView.getSellingpriceTF().setText("10.0");
        mockAddBookView.getPurchasedpriceTF().setText("-5.0");
        mockAddBookView.getStockTF().setText("50");
        mockAddBookView.getDescriptionTA().setText("description");
        Platform.runLater(() -> {
            mockAddBookView.getAuthorComboBox().setValue(mockAuthor);
            mockAddBookView.getCategoryComboBox().setValue("category");
        });

        waitForFxEvents();
        assertTrue(mockAddBookView.getSubmitBtn().isDisabled());
    }

    @Test
    public void no_author() {
        mockAddBookView.getIsbnTF().setText("123-12-12345-12-1");
        mockAddBookView.getTitleTF().setText("Test Book");
        mockAddBookView.getSupplierTF().setText("sth");
        mockAddBookView.getSellingpriceTF().setText("10.0");
        mockAddBookView.getPurchasedpriceTF().setText("5.0");
        mockAddBookView.getStockTF().setText("50");
        mockAddBookView.getDescriptionTA().setText("description");
        Platform.runLater(() -> mockAddBookView.getCategoryComboBox().setValue("category"));


        waitForFxEvents();
        assertTrue(mockAddBookView.getSubmitBtn().isDisabled());
    }
    @Test
    public void no_category() {
        mockAddBookView.getIsbnTF().setText("123-12-12345-12-1");
        mockAddBookView.getTitleTF().setText("Test Book");
        mockAddBookView.getSupplierTF().setText("sth");
        mockAddBookView.getSellingpriceTF().setText("10.0");
        mockAddBookView.getPurchasedpriceTF().setText("5.0");
        mockAddBookView.getStockTF().setText("50");
        mockAddBookView.getDescriptionTA().setText("description");
        Platform.runLater(() -> mockAddBookView.getAuthorComboBox().setValue(mockAuthor));

        waitForFxEvents();
        assertTrue(mockAddBookView.getSubmitBtn().isDisabled());
    }
}

