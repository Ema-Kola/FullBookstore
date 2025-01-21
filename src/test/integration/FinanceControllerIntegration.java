package test.integration;

import controller.FinanceController;
import controller.UserController;
import dao.CustomerBillDAO;
import dao.PaycheckBillDAO;
import dao.RestockBillDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;
import view.HomeView;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Date;

public class FinanceControllerIntegration extends ApplicationTest {

    private static final String TEST_CUSTOMER_BILL_FILE = "testCustomerBills.db";
    private static final String TEST_RESTOCK_BILL_FILE = "testRestockBills.db";
    private static final String TEST_PAYCHECK_BILL_FILE = "testPaycheckBills.db";

    private CustomerBillDAO customerBillDAO;
    private RestockBillDAO restockBillDAO;
    private PaycheckBillDAO paycheckBillDAO;
    private FinanceController financeController;
    User user = new User("name", "sur", new Date(), Gender.FEMALE, "emko", "emakola22@gmail.com", "321", Role.ADMINISTRATOR, "0695730257", 0);

    private ObservableList<FinanceStatRecord> cacheMonthly = FXCollections.observableArrayList();
    private ObservableList<FinanceStatRecord> cacheYearly = FXCollections.observableArrayList();

    @TempDir
    static File tempFolder;
    static File tempFileCustomerBills;
    static File tempFileRestockBills;
    static File tempFilePaycheckBills;

    @Override
    public void start(Stage stage) throws Exception {
        financeController = new FinanceController(stage, new HomeView(user, new UserController(stage)));
        stage.setScene(financeController.getView().showView(stage));
        stage.show();
    }

    @BeforeEach
    void setup() {
        tempFileCustomerBills = new File(tempFolder, TEST_CUSTOMER_BILL_FILE);
        tempFileRestockBills = new File(tempFolder, TEST_RESTOCK_BILL_FILE);
        tempFilePaycheckBills = new File(tempFolder, TEST_PAYCHECK_BILL_FILE);

        customerBillDAO = new CustomerBillDAO(tempFileCustomerBills.getAbsolutePath());
        restockBillDAO = new RestockBillDAO(tempFileRestockBills.getAbsolutePath());
        paycheckBillDAO = new PaycheckBillDAO(tempFilePaycheckBills.getAbsolutePath());

        financeController.setCustomerBillDAO(customerBillDAO);
        financeController.setRestockBillDAO(restockBillDAO);
        financeController.setPaycheckBillDAO(paycheckBillDAO);
        financeController.setCacheMonthly(cacheMonthly);
        financeController.setCacheYearly(cacheYearly);
    }

    @Test
    public void cacheMonthly_filterDate_integration() {
        Book x = new Book("123-12-12345-12-1","liber te lutemm", "desc", "Pika Pa Siperfaqe",350,10,new Author("sa ", "letrare",Gender.MALE),23,"Novel");
        BillRecord cb1 = new BillRecord(x, 1);
        ObservableList<BillRecord> bills = FXCollections.observableArrayList();
        bills.add(cb1);

        BillRecord cb2 = new BillRecord(x, 2);
        ObservableList<BillRecord> bills2 = FXCollections.observableArrayList();
        bills2.add(cb2);

        CustomerBill cBill1 = new CustomerBill(user, new Date(2004, 2, 1));
        cBill1.setBillRecords(bills);
        CustomerBill cBill2 = new CustomerBill(user, new Date(2004, 3, 1));
        cBill2.setBillRecords(bills2);

        Book existingBook = new Book("444-44-44444-44-4", "Existing Book", "Description", "Supplier", 20.0, 10.0, new Author("Jane", "Doe", Gender.FEMALE), 100, "Category");

        RestockBill rBill1 = new RestockBill(existingBook, user,1, new Date(2004, 2, 1));
        RestockBill rBill2 = new RestockBill(existingBook, user,1,new Date(2004, 3, 1));

        PaycheckBill pBill1 = new PaycheckBill(user, 50.0, new Date(2004, 2, 1));
        PaycheckBill pBill2 = new PaycheckBill(user, 100.0, new Date(2004, 3, 1));

        // Save them using DAOs
        customerBillDAO.create(cBill1);
        customerBillDAO.create(cBill2);

        restockBillDAO.create(rBill1);
        restockBillDAO.create(rBill2);

        paycheckBillDAO.create(pBill1);
        paycheckBillDAO.create(pBill2);

        // Populate cacheMonthly based on saved data
        ObservableList<FinanceStatRecord> records = financeController.filterDate("Monthly");

        //income 1*10 March 2004, cost 1*20+1*50,income 2*10 April 2024, cost 1*20+1*100
        assertEquals(2, records.size());
        FinanceStatRecord recordMarch2004 = records.getFirst();
        assertEquals("March 3904", recordMarch2004.getTimeColumn());
        assertEquals(10.0, recordMarch2004.getIncome());
        assertEquals(70.0, recordMarch2004.getCost());
        assertEquals(-60.0, recordMarch2004.getNetBalance());

        FinanceStatRecord recordApril2004 = records.get(1);
        assertEquals("April 3904", recordApril2004.getTimeColumn());
        assertEquals(20.0, recordApril2004.getIncome());
        assertEquals(120.0, recordApril2004.getCost());
        assertEquals(-100.0, recordApril2004.getNetBalance());
    }

    @Test
    public void cacheYearly_filterDate_integration() {
        Book x = new Book("123-12-12345-12-1","liber te lutemm", "desc", "Pika Pa Siperfaqe",350,10,new Author("sa ", "letrare",Gender.MALE),23,"Novel");
        BillRecord cb1 = new BillRecord(x, 1);
        ObservableList<BillRecord> bills = FXCollections.observableArrayList();
        bills.add(cb1);

        BillRecord cb2 = new BillRecord(x, 2);
        ObservableList<BillRecord> bills2 = FXCollections.observableArrayList();
        bills2.add(cb2);

        // Create some real data for testing
        CustomerBill cBill1 = new CustomerBill(user, new Date(2004, 2, 1));
        cBill1.setBillRecords(bills);
        CustomerBill cBill2 = new CustomerBill(user, new Date(2004, 3, 1));
        cBill2.setBillRecords(bills2);

        Book existingBook = new Book("444-44-44444-44-4", "Existing Book", "Description", "Supplier", 20.0, 10.0, new Author("Jane", "Doe", Gender.FEMALE), 100, "Category");

        RestockBill rBill1 = new RestockBill(existingBook, user,1, new Date(2004, 2, 1));
        RestockBill rBill2 = new RestockBill(existingBook, user,1,new Date(2005, 3, 1));

        PaycheckBill pBill1 = new PaycheckBill(user, 50.0, new Date(2005, 2, 1));
        PaycheckBill pBill2 = new PaycheckBill(user, 100.0, new Date(2006, 3, 1));

        // Save them using DAOs
        customerBillDAO.create(cBill1);
        customerBillDAO.create(cBill2);
        restockBillDAO.create(rBill1);
        restockBillDAO.create(rBill2);
        paycheckBillDAO.create(pBill1);
        paycheckBillDAO.create(pBill2);

        // Populate cacheYearly based on saved data
        ObservableList<FinanceStatRecord> records = financeController.filterDate("Yearly");


        //2024 income: 10+2*10; cost:20
        //2025 income: 0; cost: 20+50
        //2026 income: 0; cost: 100
        assertEquals(3, records.size());
        FinanceStatRecord record2004 = records.get(2);
        assertEquals("3904", record2004.getTimeColumn());
        assertEquals(30.0, record2004.getIncome());
        assertEquals(20.0, record2004.getCost());
        assertEquals(10.0, record2004.getNetBalance());

        FinanceStatRecord record2005 = records.get(1);
        assertEquals("3905", record2005.getTimeColumn());
        assertEquals(0.0, record2005.getIncome());
        assertEquals(70.0, record2005.getCost());
        assertEquals(-70.0, record2005.getNetBalance());

        FinanceStatRecord record2006 = records.getFirst();
        assertEquals("3906", record2006.getTimeColumn());
        assertEquals(0.0, record2006.getIncome());
        assertEquals(100.0, record2006.getCost());
        assertEquals(-100.0, record2006.getNetBalance());
    }
}
