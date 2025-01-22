package test.integration;

import controller.CheckOutBookController;
import controller.UserController;
import dao.BooksDAO;
import dao.CustomerBillDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;
import view.HomeView;

import java.io.File;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class CheckoutBookControllerIntegration extends ApplicationTest {
    List<CustomerBill> allBills;
    @TempDir
    static File tempFolder;
    static File tempFile;
    static File tempBooks;

    private CustomerBillDAO customerBillDAO;
    private BooksDAO booksDAO;
    private CheckOutBookController controller;


    @Override
    public void start(Stage stage){
        controller = new CheckOutBookController(stage, new HomeView(new User(), new UserController(stage)));
        Scene scene = controller.getView().showView(stage);
        stage.setScene(scene);
        stage.show();

    }

    @BeforeEach
    void caseSetup() throws Exception {
        tempBooks = new File(tempFolder, "TestBooks.dat");
        tempFile = new File(tempFolder, "TestBill.dat");
        customerBillDAO = new CustomerBillDAO(tempFile.getAbsolutePath());
        booksDAO = new BooksDAO(tempBooks.getAbsolutePath());

        controller.setCustomerBillDAO(customerBillDAO);
        controller.setDao(booksDAO);

        System.out.println(tempFile.createNewFile());
    }

    @Test
    public void testOnPrintBill() {
        Book book1 = new Book("123", "title", "desc", "b", 200.0,10.0, new Author("n","l", Gender.FEMALE), 10, "");
        booksDAO.create(book1);

        BillRecord bill1 = new BillRecord(book1, 5);

        User user = new User("John", "Doe", new Date(), Gender.FEMALE, "user","email", "pass", Role.ADMINISTRATOR, "123", 50.0);
        CustomerBill cbill = new CustomerBill(user);

        cbill.setBillNo(1);
        cbill.setDate(new Date());

        ObservableList<BillRecord> bill = FXCollections.observableArrayList();
        bill.add(bill1);
        cbill.setBillRecords(bill);


        controller.setCustomerBill(cbill);
        Platform.runLater(
                ()-> {controller.onPrintBill(tempFolder.getPath());
                    });
        waitForFxEvents();
        allBills = customerBillDAO.getAll();

        assertEquals(1, allBills.size());
        assertEquals(50, allBills.getFirst().getTotal());

    }


}
