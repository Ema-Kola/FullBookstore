package test.unit;

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
import model.Book;
import model.PaycheckBill;
import model.Role;
import model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class HomeViewControllerTest extends ApplicationTest{
    private final VerifyAlertTest verifyAlert = new VerifyAlertTest();

    private HomeViewController homeViewController;
    @Mock private BooksDAO booksDao;

    @Override
    public void start(Stage stage){
        MockitoAnnotations.openMocks(this);
        User user = mock(User.class);
        homeViewController = new HomeViewController(new Stage(), user, new UserController(new Stage()));
        when(user.getFirstName()).thenReturn("John");
        when(user.getLastName()).thenReturn("Doe");
        when(user.getRole()).thenReturn(Role.LIBRARIAN);

        homeViewController.setBooksdao(booksDao);
        stage.setScene(homeViewController.getView().showView(stage));
        stage.show();
        System.out.println("start");
    }

    @Test
    void test_lowInStock_method(){
        Book b1 = mock(Book.class);
        Book b2 = mock(Book.class);
        Book b3 = mock(Book.class);
        ObservableList<Book> listOfBooks = FXCollections.observableArrayList();
        when(b1.getStock()).thenReturn(3);
        when(b3.getStock()).thenReturn(2);
        when(b2.getStock()).thenReturn(6);

        when(b1.getTitle()).thenReturn("Book 1");
        when(b3.getTitle()).thenReturn("Book 2");
        when(b2.getTitle()).thenReturn("Book 3");

        listOfBooks.add(b1);
        listOfBooks.add(b2);
        listOfBooks.add(b3);
        when(booksDao.getAllActive()).thenReturn(listOfBooks);
        Platform.runLater(
                ()-> homeViewController.lowInStockWarning()
        );
        waitForFxEvents();

        verifyAlert.verifyAlert("These books are low in stock: "+"\n"+b1.getTitle()+", stock: "+b1.getStock()+"\n"+b3.getTitle()+", stock: "+b3.getStock());

    }

    @Test
    void test_releasePaychecks_method_success() {
        UsersDAO usersDAO = mock(UsersDAO.class);
        PaycheckBillDAO paycheckBillDAO = mock(PaycheckBillDAO.class);

        homeViewController.setUsersDAO(usersDAO);
        homeViewController.setPaycheckBillDAO(paycheckBillDAO);

        Optional<ButtonType> result = Optional.of(ButtonType.OK);

        User user1 = mock(User.class);
        User user2 = mock(User.class);
        when(user1.getSalary()).thenReturn(1000.0);
        when(user2.getSalary()).thenReturn(2000.0);

        ObservableList<User> users = FXCollections.observableArrayList();
        users.add(user1);
        users.add(user2);

        when(usersDAO.getAllActive()).thenReturn(users);

        when(paycheckBillDAO.create(any(PaycheckBill.class))).thenReturn(true);

        Platform.runLater(
                ()->  homeViewController.releasePaychecks(result)
        );
        waitForFxEvents();

        verify(usersDAO).getAllActive();
        verify(paycheckBillDAO, times(2)).create(any(PaycheckBill.class));
    }

    @Test
    void test_releasePaychecks_method_fail() {
        UsersDAO usersDAO = mock(UsersDAO.class);
        PaycheckBillDAO paycheckBillDAO = mock(PaycheckBillDAO.class);
        homeViewController.setUsersDAO(usersDAO);
        homeViewController.setPaycheckBillDAO(paycheckBillDAO);
        Optional<ButtonType> result = Optional.of(ButtonType.OK);

        User user1 = mock(User.class);
        when(user1.getSalary()).thenReturn(1000.0);

        ObservableList<User> users = FXCollections.observableArrayList();
        users.add(user1);

        when(usersDAO.getAllActive()).thenReturn(users);
        when(paycheckBillDAO.create(any(PaycheckBill.class))).thenReturn(false).thenReturn(false);
        System.out.println(homeViewController.getPaycheckBillDAO());

        Platform.runLater(
                ()->  homeViewController.releasePaychecks(result)
        );
        waitForFxEvents();

        verify(usersDAO).getAllActive();
        verify(paycheckBillDAO, times(1)).create(any(PaycheckBill.class));
        assertFalse(paycheckBillDAO.create(any(PaycheckBill.class)));
    }
}
