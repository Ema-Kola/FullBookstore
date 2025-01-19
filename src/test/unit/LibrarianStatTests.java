package test.unit;

import controller.LibrarianStatController;
import dao.CustomerBillDAO;
import dao.UsersDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import view.HomeView;
import view.LibrarianStatView;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class LibrarianStatTests extends ApplicationTest {

    private LibrarianStatView view;

    @Override
    public void start(Stage stage) {
        CustomerBillDAO mockCustomerBillDAO = mock(CustomerBillDAO.class);
        UsersDAO mockUsersDAO = mock(UsersDAO.class);

        User librarian1 = new User("John", "Doe", new Date(), Gender.MALE, "john", "john@example.com", "password", Role.LIBRARIAN, "1234567890", 500.0);
        User librarian2 = new User("Jane", "Doe", new Date(), Gender.FEMALE, "jane", "jane@example.com", "password", Role.LIBRARIAN, "0987654321", 600.0);

        CustomerBill bill1 = new CustomerBill(librarian1, new Date(1672531200000L)); // Jan 1, 2023
        bill1.getBillRecords().add(new BillRecord(new Book("123", "Book1", "Desc1", "Pub1", 10, 15, new Author("Auth1", "Last1", Gender.MALE), 5, "Novel"), 3));
        bill1.getBillRecords().add(new BillRecord(new Book("456", "Book2", "Desc2", "Pub2", 12, 18, new Author("Auth2", "Last2", Gender.FEMALE), 4, "Fiction"), 5));

        CustomerBill bill2 = new CustomerBill(librarian2, new Date(1675219200000L)); // Feb 1, 2023
        bill2.getBillRecords().add(new BillRecord(new Book("789", "Book3", "Desc3", "Pub3", 8, 12, new Author("Auth3", "Last3", Gender.MALE), 2, "Drama"), 2));

        when(mockUsersDAO.getAllActive()).thenReturn(FXCollections.observableArrayList(librarian1, librarian2));
        when(mockCustomerBillDAO.getAll()).thenReturn(FXCollections.observableArrayList(bill1, bill2));

        LibrarianStatController controller = new LibrarianStatController(stage, new HomeView(librarian1, null));
        controller.setUsersDAO(mockUsersDAO);
        controller.setCustomerBillDAO(mockCustomerBillDAO);

        view = controller.getView();
        stage.setScene(view.showView(stage));
        stage.show();
    }

    @BeforeEach
    public void setup() {
        interact(() -> {
            view.getFromTF().clear();
            view.getToTF().clear();
            view.getSearchTF().clear();
        });
        waitForFxEvents();
    }

    @Test
    public void testGenerateCorrectLibrarianStatRecordsWithValidDates() {
        interact(() -> {
            view.getFromTF().setText("01-01-2023");
            view.getToTF().setText("02-02-2023");
            view.getSearch().fire();
        });
        waitForFxEvents();

        ObservableList<LibrarianStatRecord> actualRecords = view.getTableView().getItems();
        assertEquals(2, actualRecords.size());

        LibrarianStatRecord record1 = actualRecords.get(0);
        LibrarianStatRecord record2 = actualRecords.get(1);

        assertEquals("John Doe", record1.getEmployeeName());
        assertEquals(1, record1.getBills());
        assertEquals(8, record1.getBooks());
        assertEquals(135.0, record1.getTotal());

        assertEquals("Jane Doe", record2.getEmployeeName());
        assertEquals(1, record2.getBills());
        assertEquals(2, record2.getBooks());
        assertEquals(24.0, record2.getTotal());
    }

    @Test
    public void testFilterByEmployeeName() {
        interact(() -> {
            view.getFromTF().setText("01-01-2023");
            view.getToTF().setText("02-02-2023");
            view.getSearch().fire();
            view.getSearchTF().setText("John");
        });
        waitForFxEvents();

        ObservableList<LibrarianStatRecord> actualRecords = view.getTableView().getItems();
        assertEquals(1, actualRecords.size());

        LibrarianStatRecord record = actualRecords.get(0);
        assertEquals("John Doe", record.getEmployeeName());
    }

    @Test
    public void testInvalidDateOrder() {
        interact(() -> {
            view.getFromTF().setText("01-02-2023");
            view.getToTF().setText("01-01-2023");
            view.getSearch().fire();
        });
        waitForFxEvents();

        ObservableList<LibrarianStatRecord> actualRecords = view.getTableView().getItems();
        assertTrue(actualRecords.isEmpty());
    }

    @Test
    public void testEmptyDateFields() {
        interact(() -> {
            view.getSearch().fire();
        });
        waitForFxEvents();

        ObservableList<LibrarianStatRecord> actualRecords = view.getTableView().getItems();
        assertEquals(2, actualRecords.size());
    }
}
