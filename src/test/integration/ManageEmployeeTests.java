package test.integration;

import controller.ManageEmployeeController;
import dao.UsersDAO;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Gender;
import model.Role;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;
import view.HomeView;
import view.ManageEmployeeView;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class ManageEmployeeTests extends ApplicationTest {

    private ManageEmployeeView view;
    private UsersDAO usersDao;
    @TempDir
    File tempDir;
    @Override
    public void start(Stage stage) throws IOException {

        File usersFile = new File(tempDir, "users.dat");
        usersDao = new UsersDAO(usersFile.getPath());

        User user1 = new User("John", "Doe", new Date(), Gender.MALE, "john", "john@lib.com", "321", Role.LIBRARIAN, "0695730257", 500.0);
        User user2 = new User("Jane", "Smith", new Date(), Gender.FEMALE, "jane", "jane@lib.com", "123", Role.LIBRARIAN, "0695730258", 600.0);
        usersDao.create(user1);
        usersDao.create(user2);

        ManageEmployeeController controller = new ManageEmployeeController(stage, new HomeView(new User(), null), usersDao);
        view = controller.getView();
        Scene scene = view.showView(stage);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setup() {
        interact(() -> {
            view.getSearch().clear();
            view.getChoiceBox().setValue("Active");
        });
        waitForFxEvents();
    }

    @Test
    public void testEditUserDetails() {
        TableView<User> tableView = view.getTableView();

        // manually moved the cursor to the cell I want to edit
        double xOffset = 50;
        double yOffset = 35;

        //then we have to double tap to select that cell to edit
        clickOn(tableView.localToScreen(xOffset, yOffset));
        clickOn(tableView.localToScreen(xOffset, yOffset));

        //writing the changed value
        write("Ema");

        //changes won't be commited unless we press enter
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        waitForFxEvents();


        clickOn(view.getUpdateBtn());
        waitForFxEvents();

        ObservableList<User> usersInDb = usersDao.getAll();
        User updatedUser = usersInDb.getFirst();
        assertEquals("Ema", updatedUser.getFirstName());
        assertEquals("Ema", usersDao.searchUser("john").getFirstName());
    }




    @Test
    public void testEditNotSavedInvalidInput() {
        TableView<User> tableView = view.getTableView();

        // manually moved the cursor to the cell I want to edit
        double xOffset = 50;
        double yOffset = 35;

        //then we have to double tap to select that cell to edit
        clickOn(tableView.localToScreen(xOffset, yOffset));
        clickOn(tableView.localToScreen(xOffset, yOffset));

        //writing the changed value
        write("1");

        press(KeyCode.ENTER).release(KeyCode.ENTER);
        waitForFxEvents();

        clickOn(view.getUpdateBtn());
        waitForFxEvents();

        //even though we click update the change should not be saved in db because the number as a name is invalid
        ObservableList<User> usersInDb = usersDao.getAll();
        User updatedUser = usersInDb.getFirst();
        assertEquals("John", updatedUser.getFirstName());
        assertEquals("John", usersDao.searchUser("john").getFirstName());
    }
}
