package test.integration;

import controller.HomeViewController;
import controller.UserController;
import dao.BooksDAO;
import dao.PaycheckBillDAO;
import dao.UsersDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.File;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class HomeViewIntegration extends ApplicationTest {
    private HomeViewController homeViewController;
    private BooksDAO booksDAO;
    private UsersDAO usersDAO;
    private PaycheckBillDAO paycheckBillDAO;
    @TempDir
    static File tempFolder;

    private File tempFileBooks;
    private File tempFileUsers;
    private File tempFilePaychecks;

    @Override
    public void start(Stage stage) throws Exception {

        User user = new User("name", "sur",new Date(), Gender.FEMALE,"emko", "emakola22@gmail.com", "321",   Role.ADMINISTRATOR, "0695730257",0);
        homeViewController = new HomeViewController(new Stage(), user, new UserController(stage));
        stage.setScene(homeViewController.getView().showView(stage));
        stage.show();
    }

    @BeforeEach
    void setup() {
        tempFileBooks = new File(tempFolder, "testBooks.dat");
        tempFileUsers = new File(tempFolder, "testUsers.dat");
        tempFilePaychecks = new File(tempFolder, "testPaychecks.dat");

        booksDAO = new BooksDAO(tempFileBooks.getAbsolutePath());
        usersDAO = new UsersDAO(tempFileUsers.getAbsolutePath());
        paycheckBillDAO = new PaycheckBillDAO(tempFilePaychecks.getAbsolutePath());

        User user1 = new User("name", "sur",new Date(),Gender.FEMALE,"emko", "emakola22@gmail.com", "321",   Role.ADMINISTRATOR, "0695730257",50000);
        User user2 = new User("name", "sur",new Date(),Gender.FEMALE,"emko", "emakola22@gmail.com", "321",   Role.ADMINISTRATOR, "0695730257",20000);

        Book b1 = new Book("12345", "Book 1", "Description", "Supplier", 50.0, 30.0, new Author("Author", "One", Gender.MALE), 3, "Comedy");
        Book b2 = new Book("67890", "Book 2", "Description", "Supplier", 60.0, 40.0, new Author("Author", "Two", Gender.FEMALE), 6, "Action");
        Book b3 = new Book("11223", "Book 3", "Description", "Supplier", 70.0, 50.0, new Author("Author", "Three", Gender.FEMALE), 2, "Drama");

        booksDAO.create(b1);
        booksDAO.create(b2);
        booksDAO.create(b3);
        usersDAO.create(user1);
        usersDAO.create(user2);

        homeViewController.setBooksdao(booksDAO);
        homeViewController.setUsersDAO(usersDAO);
        homeViewController.setPaycheckBillDAO(paycheckBillDAO);


    }

    @Test
    void test_releasePaychecks_method_success() {
        Optional<ButtonType> result = Optional.of(ButtonType.OK);
        homeViewController.releasePaychecks(result);
        waitForFxEvents();
        assertEquals(2, paycheckBillDAO.getAll().size());
        assertEquals(50000, paycheckBillDAO.getAll().getFirst().getTotal());
        assertEquals(20000, paycheckBillDAO.getAll().getLast().getTotal());

    }

}
