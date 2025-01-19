package test.unit;

import controller.BookStatController;
import dao.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import view.BookStatView;
import view.HomeView;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class BookStatTests extends ApplicationTest {

    private BookStatController controller;
    private BookStatView view;
    private BooksDAO mockBooksDAO;
    private CustomerBillDAO mockCustomerBillDAO;
    private RestockBillDAO mockRestockBillDAO;
    private CategoryDAO mockCategoryDAO;
    private AuthorsDAO mockAuthorsDAO;
    private Book book1;
    private Book book2;

    @Override
    public void start(Stage stage) {

        mockBooksDAO = mock(BooksDAO.class);
        mockCustomerBillDAO = mock(CustomerBillDAO.class);
        mockRestockBillDAO = mock(RestockBillDAO.class);
        mockCategoryDAO = mock(CategoryDAO.class);
        mockAuthorsDAO = mock(AuthorsDAO.class);

        User employee = new User("John", "Doe", new Date(), Gender.MALE, "johndoe", "john@lib.com", "password", Role.LIBRARIAN, "0695730257", 500.0);
        book1 = new Book("123-12-12345-12-1", "Book One", "Description", "Author One", 350, 500, new Author("Fyodor", "Dostoevsky", Gender.MALE), 23, "Novel");
        book2 = new Book("123-12-12345-12-2", "Book Two", "Description", "Author Two", 300, 400, new Author("Leo", "Tolstoy", Gender.MALE), 22, "Fiction");

        Date jan2025 = parseDate("2025-01-10");
        Date feb2025 = parseDate("2025-02-15");
        Date dec2024 = parseDate("2024-12-05");
        Date jan2025_2 = parseDate("2025-01-05");

        BillRecord billRecord1 = new BillRecord(book1, 5);
        BillRecord billRecord2 = new BillRecord(book2, 10);

        CustomerBill janBill = new CustomerBill(employee, jan2025);
        janBill.setBillNo(1);
        janBill.getBillRecords().add(billRecord1);

        CustomerBill febBill = new CustomerBill(employee, feb2025);
        febBill.setBillNo(2);
        febBill.getBillRecords().add(billRecord2);

        CustomerBill decBill = new CustomerBill(employee, dec2024);
        decBill.setBillNo(3);
        decBill.getBillRecords().add(billRecord1);
        decBill.getBillRecords().add(billRecord2);

        RestockBill restockJan = new RestockBill(book1, employee, 15, jan2025);
        RestockBill restockJan2 = new RestockBill(book1, employee, 2, jan2025_2);
        RestockBill restockFeb = new RestockBill(book2, employee, 20, feb2025);
        RestockBill restockDec = new RestockBill(book1, employee, 10, dec2024);

        when(mockCustomerBillDAO.getAll()).thenReturn(FXCollections.observableArrayList(janBill, febBill, decBill));
        when(mockRestockBillDAO.getAll()).thenReturn(FXCollections.observableArrayList(restockJan, restockFeb, restockDec, restockJan2));
        when(mockBooksDAO.getAll()).thenReturn(FXCollections.observableArrayList(book1, book2));
        when(mockCategoryDAO.getAll()).thenReturn(FXCollections.observableArrayList("Novel", "Fiction", "Something"));

        when(mockAuthorsDAO.getAll()).thenReturn(FXCollections.observableArrayList(new Author("Fyodor", "Dostoevsky", Gender.MALE), new Author("Leo", "Tolstoy", Gender.MALE), new Author("Autor", "Tj", Gender.MALE) ));
        controller = new BookStatController(stage, new HomeView(employee, null));
        controller.setBooksDAO(mockBooksDAO);
        controller.setCustomerBillDAO(mockCustomerBillDAO);
        controller.setRestockBillDAO(mockRestockBillDAO);
        controller.setCategoryDAO(mockCategoryDAO);
        controller.setAuthorsDAO(mockAuthorsDAO);

        view = controller.getView();
        Scene scene = view.showView(stage);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setup() {
        interact(() -> {
            view.getSearchBar().clear();
            view.getChoiceBox().setValue("Book");
            view.getChoiceBoxPeriod().setValue("Daily");
        });
        waitForFxEvents();
    }

    @Test
    public void testGenerateCorrectBookStatRecordsFromBillsDailyFilter() {
        interact(() -> view.getChoiceBoxPeriod().setValue("Daily"));
        waitForFxEvents();

        clickOn(view.getBtnSearch());
        waitForFxEvents();

        BookStatRecord b1 = new BookStatRecord(book2, 10, 20, new Date());
        b1.setTimeForColumn("15 February 2025");
        BookStatRecord b2 = new BookStatRecord(book2, 10, 0, new Date());
        b2.setTimeForColumn("5 December 2024");
        BookStatRecord b3 = new BookStatRecord(book1, 5, 10, new Date());
        b3.setTimeForColumn("5 December 2024");
        BookStatRecord b4 = new BookStatRecord(book1, 5, 15, new Date());
        b4.setTimeForColumn("10 January 2025");
        BookStatRecord b5 = new BookStatRecord(book1, 0, 2, new Date());
        b5.setTimeForColumn("5 January 2025");

        ObservableList<BookStatRecord> expectedRecords = FXCollections.observableArrayList(b1, b2, b3, b4,b5);
        ObservableList<BookStatRecord> actualRecords = view.getTableView().getItems();

        assertEquals(expectedRecords.size(), actualRecords.size());

        for (int i = 0; i < expectedRecords.size(); i++) {
            BookStatRecord expected = expectedRecords.get(i);
            BookStatRecord actual = actualRecords.get(i);

            assertEquals(expected.getBookName(), actual.getBookName());
            assertEquals(expected.getBook().getIsbn(), actual.getBook().getIsbn());
            assertEquals(expected.getSold(), actual.getSold());
            assertEquals(expected.getBought(), actual.getBought());
            assertEquals(expected.getTimeForColumn(), actual.getTimeForColumn());
        }
    }

    @Test
    public void testGenerateCorrectBookStatRecordsFromBillsMonthlyFilter() {
        interact(() -> view.getChoiceBoxPeriod().setValue("Monthly"));
        waitForFxEvents();

        clickOn(view.getBtnSearch());
        waitForFxEvents();

        BookStatRecord b1 = new BookStatRecord(book2, 10, 20, new Date());
        b1.setTimeForColumn("February 2025");
        BookStatRecord b2 = new BookStatRecord(book2, 10, 0, new Date());
        b2.setTimeForColumn("December 2024");
        BookStatRecord b3 = new BookStatRecord(book1, 5, 17, new Date());
        b3.setTimeForColumn("January 2025");
        BookStatRecord b4 = new BookStatRecord(book1, 5, 10, new Date());
        b4.setTimeForColumn("December 2024");

        ObservableList<BookStatRecord> expectedRecords = FXCollections.observableArrayList(b1,b2,b3,b4);
        ObservableList<BookStatRecord> actualRecords = view.getTableView().getItems();

        assertEquals(expectedRecords.size(), actualRecords.size());

        for (int i = 0; i < expectedRecords.size(); i++) {
            BookStatRecord expected = expectedRecords.get(i);
            BookStatRecord actual = actualRecords.get(i);

            assertEquals(expected.getBookName(), actual.getBookName());
            assertEquals(expected.getBook().getIsbn(), actual.getBook().getIsbn());
            assertEquals(expected.getSold(), actual.getSold());
            assertEquals(expected.getBought(), actual.getBought());
            assertEquals(expected.getTimeForColumn(), actual.getTimeForColumn());
        }
    }

    @Test
    public void testGenerateCorrectBookStatRecordsFromBillsYearlyFilter() {
        interact(() -> view.getChoiceBoxPeriod().setValue("Total"));
        waitForFxEvents();

        clickOn(view.getBtnSearch());
        waitForFxEvents();


        BookStatRecord b1 = new BookStatRecord(book2, 10, 20, new Date());
        b1.setTimeForColumn("2025");
        BookStatRecord b2 = new BookStatRecord(book2, 10, 0, new Date());
        b2.setTimeForColumn("2024");
        BookStatRecord b3 = new BookStatRecord(book1, 5, 17, new Date());
        b3.setTimeForColumn("2025");
        BookStatRecord b4 = new BookStatRecord(book1, 5, 10, new Date());
        b4.setTimeForColumn("2024");

        ObservableList<BookStatRecord> expectedRecords = FXCollections.observableArrayList(b1,b2,b3,b4);
        ObservableList<BookStatRecord> actualRecords = view.getTableView().getItems();

        assertEquals(expectedRecords.size(), actualRecords.size());

        for (int i = 0; i < expectedRecords.size(); i++) {
            BookStatRecord expected = expectedRecords.get(i);
            BookStatRecord actual = actualRecords.get(i);

            assertEquals(expected.getBookName(), actual.getBookName());
            assertEquals(expected.getBook().getIsbn(), actual.getBook().getIsbn());
            assertEquals(expected.getSold(), actual.getSold());
            assertEquals(expected.getBought(), actual.getBought());
            assertEquals(expected.getTimeForColumn(), actual.getTimeForColumn());
        }
    }

    @Test
    public void testGenerateCorrectBookStatRecordsFromCategoryFilterAndSearchBar() {
        interact(() -> {
            view.getChoiceBoxPeriod().setValue("Total");
            view.getChoiceBox().setValue("Category");

        });
        waitForFxEvents();

        clickOn(view.getBtnSearch());
        waitForFxEvents();
        interact(() -> {
            view.getSearchBar().setText("Novel");
        });
        waitForFxEvents();

        BookStatRecord b1 = new BookStatRecord(book1, 5, 17, new Date());
        b1.setTimeForColumn("2025");
        BookStatRecord b2 = new BookStatRecord(book1, 5, 10, new Date());
        b2.setTimeForColumn("2024");

        ObservableList<BookStatRecord> expectedRecords = FXCollections.observableArrayList(b1, b2);
        ObservableList<BookStatRecord> actualRecords = view.getTableView().getItems();

        assertEquals(expectedRecords.size(), actualRecords.size());

        for (int i = 0; i < expectedRecords.size(); i++) {
            BookStatRecord expected = expectedRecords.get(i);
            BookStatRecord actual = actualRecords.get(i);

            assertEquals(expected.getBookName(), actual.getBookName());
            assertEquals(expected.getBook().getIsbn(), actual.getBook().getIsbn());
            assertEquals(expected.getSold(), actual.getSold());
            assertEquals(expected.getBought(), actual.getBought());
            assertEquals(expected.getTimeForColumn(), actual.getTimeForColumn());
        }
    }

    @Test
    public void testGenerateCorrectBookStatRecordsFromAuthorFilterAndSearchBar() {
        interact(() -> {
            view.getChoiceBox().setValue("Author");
            view.getChoiceBoxPeriod().setValue("Total");
        });
        waitForFxEvents();

        clickOn(view.getBtnSearch());
        waitForFxEvents();
        interact(() -> {
            view.getSearchBar().setText("Fyodor Dostoevsky");
        });
        waitForFxEvents();

        BookStatRecord b1 = new BookStatRecord(book1, 5, 17, new Date());
        b1.setTimeForColumn("2025");
        BookStatRecord b2 = new BookStatRecord(book1, 5, 10, new Date());
        b2.setTimeForColumn("2024");

        ObservableList<BookStatRecord> expectedRecords = FXCollections.observableArrayList(b1, b2);
        ObservableList<BookStatRecord> actualRecords = view.getTableView().getItems();

        assertEquals(expectedRecords.size(), actualRecords.size());

        for (int i = 0; i < expectedRecords.size(); i++) {
            BookStatRecord expected = expectedRecords.get(i);
            BookStatRecord actual = actualRecords.get(i);

            assertEquals(expected.getBookName(), actual.getBookName());
            assertEquals(expected.getBook().getIsbn(), actual.getBook().getIsbn());
            assertEquals(expected.getSold(), actual.getSold());
            assertEquals(expected.getBought(), actual.getBought());
            assertEquals(expected.getTimeForColumn(), actual.getTimeForColumn());
        }
    }




    private Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format", e);
        }
    }


}
