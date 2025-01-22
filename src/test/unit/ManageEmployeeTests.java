package test.unit;

import controller.ManageEmployeeController;
import dao.UsersDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Gender;
import model.Role;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import view.HomeView;
import view.ManageEmployeeView;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class ManageEmployeeTests extends ApplicationTest {

    private ManageEmployeeController controller;
    private ManageEmployeeView view;
    private UsersDAO mockUsersDao;

    @Override
    public void start(Stage stage) {
        mockUsersDao = mock(UsersDAO.class);

        User user1 = new User("John", "Doe",new Date(),Gender.MALE,"john", "john@lib.com", "321",   Role.LIBRARIAN, "0695730257",500.0);
        User user2 = new User("Jane", "Doe",new Date(),Gender.MALE,"jane", "jane@lib.com", "321",   Role.LIBRARIAN, "0695730257",500.0);
        User user3 = new User("Jim", "Doe",new Date(),Gender.MALE,"jim", "jim@lib.com", "321",   Role.LIBRARIAN, "0695730257",500.0);
        user2.setActive(false);
        when(mockUsersDao.getAll()).thenReturn(FXCollections.observableArrayList(user1, user2, user3));
        controller = new ManageEmployeeController(stage, new HomeView(new User(), null), mockUsersDao);
        view = controller.getView();
        Scene scene = view.showView(stage);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setup() {
        controller.setUsersDao(mockUsersDao);
        interact(() -> {
            view.getSearch().clear();
            view.getChoiceBox().setValue("Active");
        });
        waitForFxEvents();
    }



    @Test
    public void testFilterActiveUsers() {
        User activeUser = mockUsersDao.getAll().getFirst();
        User activeUser2 = mockUsersDao.getAll().get(2);

        interact(() -> view.getChoiceBox().setValue("Active"));
        waitForFxEvents();

        ObservableList<User> tableItems = view.getTableView().getItems();
        Assertions.assertEquals(2, tableItems.size());
        assertTrue(tableItems.contains(activeUser));
        assertTrue(tableItems.contains(activeUser2));
    }

    @Test
    public void testFilterInactiveUsers() {
        User inactiveUser = mockUsersDao.getAll().get(1);

        interact(() -> view.getChoiceBox().setValue("Inactive"));
        waitForFxEvents();

        ObservableList<User> tableItems = view.getTableView().getItems();
        Assertions.assertEquals(1, tableItems.size());
        assertTrue(tableItems.contains(inactiveUser));
    }


    @Test
    public void testSearchFunctionality() {
        User matchingUser = mockUsersDao.getAll().get(0);
        User nonMatchingUser = mockUsersDao.getAll().get(2);

        interact(() -> {
            view.getChoiceBox().setValue("Active");
            waitForFxEvents();
            view.getSearch().setText("John");});
        waitForFxEvents();

        ObservableList<User> tableItems = view.getTableView().getItems();
        Assertions.assertEquals(1, tableItems.size());
        assertTrue(tableItems.contains(matchingUser));
        Assertions.assertFalse(tableItems.contains(nonMatchingUser));
    }

    @Test
    public void testFireEmployeeSuccess() {

        User activeUser = mockUsersDao.getAll().get(1);
        when(mockUsersDao.updateAll()).thenReturn(true);

        interact(() -> {
            view.getChoiceBox().setValue("Active");
            view.getTableView().getSelectionModel().select(activeUser);
        });
        waitForFxEvents();
        clickOn(controller.getView().getDeleteBtn());

        waitForFxEvents();
        verify(mockUsersDao, times(1)).updateAll();

        Platform.runLater(()->
        {
            assertTrue(controller.getAlertSuccess().isShowing());
        });
        waitForFxEvents();

    }

    @Test
    public void testFireEmployeeFail() {

        User activeUser = mockUsersDao.getAll().get(1);
        when(mockUsersDao.updateAll()).thenReturn(false);

        interact(() -> {
            view.getChoiceBox().setValue("Active");
            view.getTableView().getSelectionModel().select(activeUser);
        });
        waitForFxEvents();
        clickOn(controller.getView().getDeleteBtn());

        waitForFxEvents();
        verify(mockUsersDao, times(1)).updateAll();

        Platform.runLater(()->
        {
                assertTrue(controller.getAlertFail().isShowing());
        });
        waitForFxEvents();

    }









}
