package test.integration;

import controller.AddUserController;
import controller.UserController;
import dao.UsersDAO;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Gender;
import model.Role;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;
import view.AddUserView;
import view.HomeView;

import java.io.File;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class AddUserTests extends ApplicationTest {

    private AddUserView addUserView;
    private UsersDAO usersDAO;

    @TempDir
    File tempDir;

    @Override
    public void start(Stage stage) {
        File usersFile = new File(tempDir, "users.dat");
        usersDAO = new UsersDAO(usersFile.getPath());

        AddUserController addUserController = new AddUserController(stage, new HomeView(new User(), new UserController(stage)));
        addUserController.setUserDao(usersDAO);

        addUserView = addUserController.getView();
        Scene scene = addUserView.showView(stage);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setup() {
        usersDAO.getAll().clear();
        waitForFxEvents();

        interact(() -> {
            addUserView.getFirstnameTF().clear();
            addUserView.getLastnameTF().clear();
            addUserView.getbirthdayTF().clear();
            addUserView.getGender().setValue(null);
            addUserView.getUsername().clear();
            addUserView.getphoneTF().clear();
            addUserView.getemailTF().clear();
            addUserView.getPasswTF().clear();
            addUserView.getsalaryTF().clear();
            addUserView.getaccessLevelTF().setValue(null);
        });
    }

    @Test
    public void testAddUserSuccessfully() {
        interact(() -> {
            addUserView.getFirstnameTF().setText("Ema");
            addUserView.getLastnameTF().setText("Kola");
            addUserView.getbirthdayTF().setText("24-07-2003");
            addUserView.getphoneTF().setText("+355695730257");
            addUserView.getemailTF().setText("ema@lib.com");
            addUserView.getPasswTF().setText("ema123");
            addUserView.getsalaryTF().setText("100");
            addUserView.getUsername().setText("ema");
            addUserView.getGender().setValue(Gender.FEMALE);
            addUserView.getaccessLevelTF().setValue(Role.LIBRARIAN);
        });
        waitForFxEvents();

        clickOn(addUserView.getSubmitBtn());

        User addedUser = usersDAO.searchUser("ema");
        assertNotNull(addedUser);
        assertEquals("Ema", addedUser.getFirstName());
        assertEquals("Kola", addedUser.getLastName());
    }

    @Test
    public void testAddUserFailsWhenUserAlreadyExists() {
        usersDAO.create(new User("Ema", "Kola", new Date(), Gender.FEMALE, "ema", "ema@lib.com", "ema123", Role.LIBRARIAN, "+355695730257", 100));

        interact(() -> {
            addUserView.getFirstnameTF().setText("Ema");
            addUserView.getLastnameTF().setText("Kola");
            addUserView.getbirthdayTF().setText("24-07-2003");
            addUserView.getphoneTF().setText("+355695730257");
            addUserView.getemailTF().setText("ema@lib.com");
            addUserView.getPasswTF().setText("ema123");
            addUserView.getsalaryTF().setText("100");
            addUserView.getUsername().setText("ema");
            addUserView.getGender().setValue(Gender.FEMALE);
            addUserView.getaccessLevelTF().setValue(Role.LIBRARIAN);
        });
        waitForFxEvents();

        clickOn(addUserView.getSubmitBtn());

        User addedUser = usersDAO.searchUser("ema");
        assertNotNull(addedUser);
        assertEquals(1, usersDAO.getAll().size());
    }


}
