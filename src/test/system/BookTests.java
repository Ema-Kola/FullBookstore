package test.system;


import controller.UserController;
import dao.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;

import view.AddBookView;
import view.CheckOutBookView;
import view.LogInView;

import java.io.File;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class BookTests extends ApplicationTest {

    private LogInView logInView;
    private UserController userController;
    private BooksDAO booksDAO;
    private RestockBillDAO restockBillDAO;
    private AuthorsDAO authorsDAO;
    private CustomerBillDAO customerBillDAO;

    @TempDir
    File tempDir;

    @Override
    public void start(Stage stage) {
        File usersFile = new File(tempDir, "users.dat");
        File authorsFile = new File(tempDir, "authors.dat");
        File customerBillFile = new File(tempDir, "customerBill.dat");
        customerBillDAO = new CustomerBillDAO(customerBillFile.getPath());
        UsersDAO usersDAO = new UsersDAO(usersFile.getPath());
        authorsDAO = new AuthorsDAO(authorsFile.getPath());
        authorsDAO.create(new Author("Autor", "Iri", Gender.FEMALE));

        File tempFileBooks = new File(tempDir, "books.dat");
        booksDAO = new BooksDAO(tempFileBooks.getPath());

        File tempFileRestock = new File(tempDir, "restock.dat");
        restockBillDAO = new RestockBillDAO(tempFileRestock.getAbsolutePath());


        usersDAO.create(new User("Njeriu", "Universal", new Date(), Gender.MALE, "tralala", "njeriu@lib.com", "password", Role.ADMINISTRATOR, "0695730257", 500.0));

        userController = new UserController(stage);
        userController.setUsersDAO(usersDAO);

        logInView = userController.getView();
        Scene scene = logInView.showView(stage);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setup() {
        waitForFxEvents();
        interact(() -> {
            logInView.getEmailF().clear();
            logInView.getPasswF().clear();
            logInView.getWrongUsernameF().setText("");
            logInView.getWrongPasswordF().setText("");
        });
    }


    @Test
    public void systemTest(){

        //login part
        interact(() -> {
            logInView.getEmailF().setText("tralala");
            logInView.getPasswF().setText("password");
        });
        waitForFxEvents();

        clickOn(logInView.getLoginBtn());

        assertNotNull(userController.loginUsername("tralala"));
        assertEquals("tralala", userController.loginUsername("tralala").getUsername());

        //home view part
        interact(() -> {
            clickOn(userController.getHomeViewController().getView().getAddBookButton());
        });
        waitForFxEvents();
        //add book part
        AddBookView addBookView = userController.getHomeViewController().getAddBookController().getView();
        addBookView.getIsbnTF().setText("777-77-77777-77-7");
        addBookView.getTitleTF().setText("Test Book");
        addBookView.getSupplierTF().setText("Supplier");
        addBookView.getSellingpriceTF().setText("10.0");
        addBookView.getPurchasedpriceTF().setText("5.0");
        addBookView.getStockTF().setText("50");
        addBookView.getDescriptionTA().setText("Description");

        Platform.runLater(() -> {
            addBookView.getAuthorComboBox().setValue(authorsDAO.getAll().getFirst());
            addBookView.getCategoryComboBox().setValue("Fiction");
        });
        userController.getHomeViewController().getAddBookController().setDao(booksDAO);
        userController.getHomeViewController().getAddBookController().setRestockDao(restockBillDAO);

        waitForFxEvents();
        clickOn(userController.getHomeViewController().getAddBookController().getView().getSubmitBtn());

        Book savedBook = booksDAO.searchBook("777-77-77777-77-7");

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        assertEquals("Supplier", savedBook.getSupplier());
        assertEquals(10.0, savedBook.getSellingPrice());
        assertEquals(50, savedBook.getStock());

        waitForFxEvents();
        interact(()->{
            userController.getHomeViewController().getAddBookController().getAlert2().close();
        });
        waitForFxEvents();

        //home view part
        clickOn(addBookView.getHomeBtn());
        waitForFxEvents();
        //checking db update
        ObservableList<Book> allBooks = booksDAO.getAllActive();
        assertEquals(1, allBooks.size(), "There should be exactly one book in the DAO.");
        Book onlyBook = allBooks.getFirst();
        assertEquals("777-77-77777-77-7", onlyBook.getIsbn(), "The ISBN should match the added book.");
        assertEquals("Test Book", onlyBook.getTitle(), "The title should match the added book.");
        assertEquals(10.0, onlyBook.getSellingPrice(), "The selling price should match the added book.");
        assertEquals(50, onlyBook.getStock(), "The stock should match the added book.");


        //search book part
        clickOn(userController.getHomeViewController().getView().getSearchBookButton());
        userController.getHomeViewController().getSearchBookController().setBooksDAO(booksDAO);
        waitForFxEvents();


        interact(() -> userController.getHomeViewController().getSearchBookController().getView().getSearch().clear());
        waitForFxEvents();

        //looking for book in search table
        ObservableList<Book> displayedBooks = userController.getHomeViewController().getSearchBookController().getView().getTableView().getItems();
        assertEquals(1, displayedBooks.size(), "There should be exactly one book displayed in the search results.");
        Book displayedBook = displayedBooks.getFirst();
        assertEquals("777-77-77777-77-7", displayedBook.getIsbn(), "The ISBN of the displayed book should match.");
        assertEquals("Test Book", displayedBook.getTitle(), "The title of the displayed book should match.");
        assertEquals(10.0, displayedBook.getSellingPrice(), "The selling price of the displayed book should match.");
        assertEquals(50, displayedBook.getStock(), "The stock of the displayed book should match.");

        //home view part
        clickOn(userController.getHomeViewController().getSearchBookController().getView().getHomeBtn());
        waitForFxEvents();

        //checkout book part
        clickOn(userController.getHomeViewController().getView().getCheckOutBookButton());

        userController.getHomeViewController().getCheckOutBookController().setDao(booksDAO);
        userController.getHomeViewController().getCheckOutBookController().setBillsFilePath(tempDir.getPath());
        userController.getHomeViewController().getCheckOutBookController().setCustomerBillDAO(customerBillDAO);
        waitForFxEvents();


        interact(() -> {
            CheckOutBookView checkOutView = userController.getHomeViewController().getCheckOutBookController().getView();
            checkOutView.getIsbnTF().setText("777-77-77777-77-7");
            checkOutView.getQuantityTf().setText("3");
        });
        waitForFxEvents();


        clickOn(userController.getHomeViewController().getCheckOutBookController().getView().getBtnAdd());
        waitForFxEvents();

        // validating table content
        ObservableList<BillRecord> tableItems = userController.getHomeViewController().getCheckOutBookController().getView().getTableView().getItems();
        assertEquals(1, tableItems.size(), "There should be exactly one record in the checkout table.");
        BillRecord record = tableItems.getFirst();
        assertEquals("Test Book", record.getBookName(), "The book name should match.");
        assertEquals(10.0, record.getPrice(), "The price should match the book's selling price.");
        assertEquals(3, record.getQuantity(), "The quantity should match the input.");
        assertEquals(30.0, record.getTotal(), "The total should match the calculated amount.");

        clickOn(userController.getHomeViewController().getCheckOutBookController().getView().getBtnPrint());
        waitForFxEvents();

// is the bill saved
        ObservableList<CustomerBill> bills = customerBillDAO.getAll();
        assertEquals(1, bills.size(), "There should be exactly one customer bill in the DAO.");
        CustomerBill bill = bills.getFirst();
        assertEquals(30.0, bill.getTotal(), "The bill total should match the calculated amount.");

//making sure the stock is updated in dao
        assertEquals(47, booksDAO.searchBook("777-77-77777-77-7").getStock());

// home view part
        clickOn(userController.getHomeViewController().getCheckOutBookController().getView().getBtnHome());
        waitForFxEvents();

        clickOn(userController.getHomeViewController().getView().getLogOutButton());


    }

}
