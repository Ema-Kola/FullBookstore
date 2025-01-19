package test.unit;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import controller.CheckOutBookController;
import controller.UserController;
import dao.BooksDAO;
import dao.CustomerBillDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import model.BillRecord;
import model.Book;
import model.CustomerBill;
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
    private final VerifyAlertTest verifyAlert = new VerifyAlertTest();
    //@Mock private HomeView mockPrevView;
    @Mock private CheckOutBookView mockCheckOutBookView;
    @Mock private BooksDAO mockBooksDAO;
    private CheckOutBookController checkOutBookController;
    @Mock private CustomerBill customerBill;
    @Mock private CustomerBillDAO customerBillDao;
    ObservableList<BillRecord> alrAddedCustomerBills;


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

        checkOutBookController.setDao(mockBooksDAO);

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

        Book mockBook = mock(Book.class);

        when(mockBooksDAO.searchBook("123-12-12345-12-1")).thenReturn(mockBook);
        when(mockBook.getStock()).thenReturn(10);
        when(mockBook.getSellingPrice()).thenReturn(100.0);  //the price is 100

        // this is to call the onRecordAdd method
        clickOn(mockCheckOutBookView.getBtnAdd());

        //verify(mockBooksDAO, times(1)).searchBook("123-12-12345-12-1");
        waitForFxEvents();

        //System.out.println(mockCheckOutBookView.getTotalTf().getText());
        //System.out.println("Total:\t " + (quantity * 100.0) +" leke");
        assertTrue(mockCheckOutBookView.getTotalTf().getText().contains("Total:\t " + (quantity * 100.0) +" leke"));

    }

    @Test
    void BVA_robust_belowMin_onRecordAdd() {
        mockCheckOutBookView.getIsbnTF().setText("123-12-12345-12-1");
        mockCheckOutBookView.getQuantityTf().setText("0");

        Book mockBook = mock(Book.class);
        when(mockBooksDAO.searchBook("123-12-12345-12-1")).thenReturn(mockBook);
        when(mockBook.getStock()).thenReturn(10);
        clickOn(mockCheckOutBookView.getBtnAdd());
        waitForFxEvents();

        verifyAlert.verifyAlert("Enter a valid quantity.");
    }

    @Test
    void BVA_robust_aboveMax_onRecordAdd() {
        mockCheckOutBookView.getIsbnTF().setText("123-12-12345-12-1");
        mockCheckOutBookView.getQuantityTf().setText("11");

        Book mockBook = mock(Book.class);
        when(mockBooksDAO.searchBook("123-12-12345-12-1")).thenReturn(mockBook);
        when(mockBook.getStock()).thenReturn(10);
        clickOn(mockCheckOutBookView.getBtnAdd());

        waitForFxEvents();

        verifyAlert.verifyAlert("Not enough books in stock. Only " + mockBook.getStock() + " left.");
    }
    @Test
    void BVA_worstcase_onRecordAdd() {
        mockCheckOutBookView.getIsbnTF().setText("123-12-12345-12-1");
        mockCheckOutBookView.getQuantityTf().setText("str");
        Button addButton = mockCheckOutBookView.getBtnAdd();
        assertTrue(addButton.isDisabled());
    }

    @Test
    void test_onRecordDelete(){

        Book mockBook = mock(Book.class);
        mockCheckOutBookView.getIsbnTF().setText("123-12-12345-12-1");
        mockCheckOutBookView.getQuantityTf().setText("2");
        when(mockBooksDAO.searchBook("123-12-12345-12-1")).thenReturn(mockBook);
        when(mockBook.getStock()).thenReturn(10);
        when(mockBook.getSellingPrice()).thenReturn(100.0);
        clickOn(mockCheckOutBookView.getBtnAdd());
        when(customerBill.getTotal()).thenReturn(200.0);

        waitForFxEvents();

        clickOn(mockCheckOutBookView.getBtnDelete());

        waitForFxEvents();

        verifyAlert.verifyAlert("Deleted successfully");

        assertTrue(mockCheckOutBookView.getTotalTf().getText().contains("Total:\t " + (customerBill.getTotal()) +" leke"));

    }

    @Test
    void test_onRecordAdd_alreadyadded(){
        checkOutBookController.setCustomerBill(customerBill);
        checkOutBookController.setCustomerBillDAO(customerBillDao);
        alrAddedCustomerBills = FXCollections.observableArrayList();
        when(customerBill.getBillRecords()).thenReturn(alrAddedCustomerBills);

        Book mockBook = mock(Book.class);
        mockCheckOutBookView.getIsbnTF().setText("123-12-12345-12-1");
        mockCheckOutBookView.getQuantityTf().setText("2");
        when(mockBooksDAO.searchBook("123-12-12345-12-1")).thenReturn(mockBook);

        BillRecord billRecord = mock(BillRecord.class);
        alrAddedCustomerBills.add(billRecord);

        when(customerBill.getBillRecords()).thenReturn(alrAddedCustomerBills);
        when(billRecord.getBook()).thenReturn(mockBook);
        System.out.println(billRecord.getBook().equals(mockBook));
        when(mockBook.getStock()).thenReturn(10);
        when(mockBook.getSellingPrice()).thenReturn(100.0);

        clickOn(mockCheckOutBookView.getBtnAdd());

        waitForFxEvents();

        verifyAlert.verifyAlert("You already added this book.");

    }
}


