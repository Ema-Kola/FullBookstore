package test.unit;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import controller.CheckOutBookController;
import controller.UserController;
import dao.BooksDAO;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import model.Book;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.testfx.framework.junit5.ApplicationTest;
import view.CheckOutBookView;
import view.HomeView;

class CheckOutBookControllerTest extends ApplicationTest {

    //@Mock private HomeView mockPrevView;
    @Mock private CheckOutBookView mockCheckOutBookView;
    @Mock private BooksDAO mockBooksDAO;
    private CheckOutBookController checkOutBookController;

    @Override
    public void start(Stage stage) {
        MockitoAnnotations.openMocks(this);

        checkOutBookController = new CheckOutBookController(stage, new HomeView(new User(), new UserController(new Stage())));
        mockCheckOutBookView = checkOutBookController.getView();

        Scene scene = checkOutBookController.getView().showView(stage);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setup() {
        interact(() -> {
            mockCheckOutBookView.getIsbnTF().clear();
            mockCheckOutBookView.getQuantityTf().clear();
        });
    }


    @ParameterizedTest
    @CsvSource({
            "1",
            "2",
            "5",
            "9",
            "10"
    })
    void BVA_normal_onRecordAdd(String quantityString) {
        int quantity = Integer.parseInt(quantityString);

        mockCheckOutBookView.getIsbnTF().setText("123-12-12345-12-1");
        mockCheckOutBookView.getQuantityTf().setText(quantityString);

        checkOutBookController.setDao(mockBooksDAO);
        Book mockBook = mock(Book.class);

        when(mockBooksDAO.searchBook("123-12-12345-12-1")).thenReturn(mockBook);
        when(mockBook.getStock()).thenReturn(10);
        when(mockBook.getSellingPrice()).thenReturn(100.0);  //the price is 100

        // this is to call the onRecordAdd method
        clickOn(mockCheckOutBookView.getBtnAdd());
        //verify(mockBooksDAO, times(1)).searchBook("123-12-12345-12-1");

        //System.out.println(mockCheckOutBookView.getTotalTf().getText());
        //System.out.println("Total:\t " + (quantity * 100.0) +" leke");
        assertTrue(mockCheckOutBookView.getTotalTf().getText().contains("Total:\t " + (quantity * 100.0) +" leke"));

    }

    @Test
    void BVA_robust_belowMin_onRecordAdd() {
        mockCheckOutBookView.getIsbnTF().setText("123-12-12345-12-1");
        mockCheckOutBookView.getQuantityTf().setText("0");

        checkOutBookController.setDao(mockBooksDAO);
        Book mockBook = mock(Book.class);
        when(mockBooksDAO.searchBook("123-12-12345-12-1")).thenReturn(mockBook);
        when(mockBook.getStock()).thenReturn(10);
        clickOn(mockCheckOutBookView.getBtnAdd());
        waitForFxEvents();

        
        DialogPane dialogPane = lookup(".dialog-pane").query();
        assertNotNull(dialogPane, "is dialog shown");
        //System.out.println(dialogPane.getContentText());
        assertEquals("Enter a valid quantity.", dialogPane.getContentText());
    }

    @Test
    void BVA_robust_aboveMax_onRecordAdd() {
        mockCheckOutBookView.getIsbnTF().setText("123-12-12345-12-1");
        mockCheckOutBookView.getQuantityTf().setText("11");

        checkOutBookController.setDao(mockBooksDAO);
        Book mockBook = mock(Book.class);
        when(mockBooksDAO.searchBook("123-12-12345-12-1")).thenReturn(mockBook);
        when(mockBook.getStock()).thenReturn(10);
        clickOn(mockCheckOutBookView.getBtnAdd());

        waitForFxEvents();
        DialogPane dialogPane = lookup(".dialog-pane").query();
        assertNotNull(dialogPane);
        assertEquals("Not enough books in stock. Only " + mockBook.getStock() + " left.", dialogPane.getContentText());
    }
    @Test
    void BVA_worstcase_onRecordAdd() {
        mockCheckOutBookView.getIsbnTF().setText("123-12-12345-12-1");
        mockCheckOutBookView.getQuantityTf().setText("str");
        Button addButton = mockCheckOutBookView.getBtnAdd();
        assertTrue(addButton.isDisabled());
    }
}


