package test.integration;

import controller.LibrarianStatController;
import dao.CustomerBillDAO;
import dao.UsersDAO;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;
import view.LibrarianStatView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class LibrarianStatTests extends ApplicationTest {

    private LibrarianStatView view;
    private CustomerBillDAO customerBillDAO;
    private UsersDAO usersDAO;

    @TempDir
    File tempDir;

    @Override
    public void start(Stage stage) {
        File customerBillsFile = new File(tempDir, "customerBills.dat");
        File usersFile = new File(tempDir, "users.dat");

        customerBillDAO = new CustomerBillDAO(customerBillsFile.getPath());
        usersDAO = new UsersDAO(usersFile.getPath());

        LibrarianStatController controller = new LibrarianStatController(stage, null);
        controller.setCustomerBillDAO(customerBillDAO);
        controller.setUsersDAO(usersDAO);

        view = controller.getView();
        Scene scene = view.showView(stage);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setup() {
        customerBillDAO.getAll().clear();
        usersDAO.getAll().clear();

        User librarian1 = new User("John", "Doe", new Date(), Gender.MALE, "john", "john@example.com", "password", Role.LIBRARIAN, "1234567890", 500.0);
        User librarian2 = new User("Jane", "Doe", new Date(), Gender.FEMALE, "jane", "jane@example.com", "password", Role.LIBRARIAN, "0987654321", 600.0);

        usersDAO.create(librarian1);
        usersDAO.create(librarian2);


        Date jan2023 = parseDate("2023-01-01");
        Date feb2023 = parseDate("2023-02-01");

        Book book1 = new Book("123", "Book1", "Desc1", "Pub1", 10, 15, new Author("Auth1", "Last1", Gender.MALE), 5, "Novel");
        Book book2 = new Book("456", "Book2", "Desc2", "Pub2", 12, 18, new Author("Auth2", "Last2", Gender.FEMALE), 4, "Fiction");
        Book book3 = new Book("789", "Book3", "Desc3", "Pub3", 8, 12, new Author("Auth3", "Last3", Gender.MALE), 2, "Drama");

        CustomerBill bill1 = new CustomerBill(librarian1, jan2023);
        bill1.getBillRecords().add(new BillRecord(book1, 3));
        bill1.getBillRecords().add(new BillRecord(book2, 5));

        CustomerBill bill2 = new CustomerBill(librarian2, feb2023);
        bill2.getBillRecords().add(new BillRecord(book3, 2));

        customerBillDAO.create(bill1);
        customerBillDAO.create(bill2);

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


    private Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format", e);
        }
    }
}
