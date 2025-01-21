package test.integration;

import controller.BookStatController;
import dao.*;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;
import view.BookStatView;
import view.HomeView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class BookStatTests extends ApplicationTest {

    private BookStatView view;
    private BooksDAO booksDAO;
    private CustomerBillDAO customerBillDAO;
    private RestockBillDAO restockBillDAO;
    private CategoryDAO categoryDAO;
    private AuthorsDAO authorsDAO;

    @TempDir
    File tempDir;

    @Override
    public void start(Stage stage) {
        File booksFile = new File(tempDir, "books.dat");
        File customerBillsFile = new File(tempDir, "customerBills.dat");
        File restockBillsFile = new File(tempDir, "restockBills.dat");
        File categoriesFile = new File(tempDir, "categories.dat");
        File authorsFile = new File(tempDir, "authors.dat");

        booksDAO = new BooksDAO(booksFile.getPath());
        customerBillDAO = new CustomerBillDAO(customerBillsFile.getPath());
        restockBillDAO = new RestockBillDAO(restockBillsFile.getPath());
        categoryDAO = new CategoryDAO(categoriesFile.getPath());
        authorsDAO = new AuthorsDAO(authorsFile.getPath());

        User employee = new User("John", "Doe", new Date(), Gender.MALE, "johndoe", "john@lib.com", "password", Role.LIBRARIAN, "0695730257", 500.0);
        BookStatController controller = new BookStatController(stage, new HomeView(employee, null));

        controller.setBooksDAO(booksDAO);
        controller.setCustomerBillDAO(customerBillDAO);
        controller.setRestockBillDAO(restockBillDAO);
        controller.setCategoryDAO(categoryDAO);
        controller.setAuthorsDAO(authorsDAO);

        view = controller.getView();
        Scene scene = view.showView(stage);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setup() {
        booksDAO.getAll().clear();
        customerBillDAO.getAll().clear();
        restockBillDAO.getAll().clear();
        categoryDAO.getAll().clear();
        authorsDAO.getAll().clear();

        categoryDAO.create("Novel");
        categoryDAO.create("Tjeter");

        Book book1 = new Book("123-12-12345-12-1", "Book One", "Description", "Author One", 350, 500, new Author("Fyodor", "Dostoevsky", Gender.MALE), 23, "Novel");
        Book book2 = new Book("123-12-12345-12-2", "Book Two", "Description", "Author Two", 300, 400, new Author("Leo", "Tolstoy", Gender.MALE), 22, "Novel");

        booksDAO.create(book1);
        booksDAO.create(book2);
        User user = new User("Ema", "Kola", new Date(), Gender.FEMALE, "emko", "emakola22@gmail.com", "321", Role.LIBRARIAN, "0695730257", 0);

        Date jan2025 = parseDate("2025-01-10");
        Date feb2025 = parseDate("2025-02-15");

        BillRecord billRecord1 = new BillRecord(book1, 5);
        BillRecord billRecord2 = new BillRecord(book2, 10);

        CustomerBill janBill = new CustomerBill(user, jan2025);
        janBill.getBillRecords().add(billRecord1);

        CustomerBill febBill = new CustomerBill(user, feb2025);
        febBill.getBillRecords().add(billRecord2);

        customerBillDAO.create(janBill);
        customerBillDAO.create(febBill);

        RestockBill restockJan = new RestockBill(book1, user, 15, jan2025);
        RestockBill restockFeb = new RestockBill(book2, user, 20, feb2025);

        restockBillDAO.create(restockJan);
        restockBillDAO.create(restockFeb);

        interact(() -> {
            view.getSearchBar().clear();
            view.getChoiceBox().setValue("Book");
            view.getChoiceBoxPeriod().setValue("Daily");
        });
        waitForFxEvents();
    }

    @Test
    public void testGenerateCorrectBookStatRecordsMonthly() {
        interact(() -> view.getChoiceBoxPeriod().setValue("Monthly"));
        waitForFxEvents();

        clickOn(view.getBtnSearch());
        waitForFxEvents();

        ObservableList<BookStatRecord> actualRecords = view.getTableView().getItems();
        assertEquals(2, actualRecords.size());

        BookStatRecord record1 = actualRecords.get(0);
        BookStatRecord record2 = actualRecords.get(1);

        assertEquals("Book Two", record1.getBookName());
        assertEquals(10, record1.getSold());
        assertEquals(20,record1.getBought());
        assertEquals("Book One", record2.getBookName());
        assertEquals(5, record2.getSold());
        assertEquals(15,record2.getBought());

    }

    @Test
    public void testGenerateCorrectBookStatRecordsYearly() {
        interact(() -> view.getChoiceBoxPeriod().setValue("Total"));
        interact(() -> view.getChoiceBox().setValue("Category"));
        waitForFxEvents();

        clickOn(view.getBtnSearch());
        waitForFxEvents();

        ObservableList<BookStatRecord> actualRecords = view.getTableView().getItems();
        assertEquals(1, actualRecords.size());

        BookStatRecord record1 = actualRecords.getFirst();


        assertEquals("Novel", record1.getCategory());
        assertEquals(15, record1.getSold());
        assertEquals(35,record1.getBought());


    }

    private Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format", e);
        }
    }
}
