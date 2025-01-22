package test.integration;

import controller.UserController;
import dao.UsersDAO;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.Gender;
import model.Role;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.File;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class UserControllerIntegration extends ApplicationTest {

    private UserController userController;
    private UsersDAO usersDAO;


    @TempDir
    static File tempFolder;
    private File tempFileUsers;

    @Override
    public void start(Stage stage) throws Exception {
        userController = new UserController(stage);



        stage.setScene(userController.getView().showView(stage));
        stage.show();
    }

    @BeforeEach
    void setup() {
        tempFileUsers = new File(tempFolder, "testUsers.db");
        usersDAO = new UsersDAO(tempFileUsers.getAbsolutePath());
        userController.setUsersDAO(usersDAO);

        User userForLogin = new User("name", "sur",new Date(), Gender.FEMALE,"testSuccess", "emakola22@gmail.com", "321",   Role.LIBRARIAN, "0695730257",0);
        usersDAO.create(userForLogin);
    }

    @Test
    void testLoginSuccess() {
        Platform.runLater(() -> {
            userController.getView().getEmailF().setText("testSuccess");
            userController.getView().getPasswF().setText("321");

            clickOn(userController.getView().getLoginBtn());
        });

        assertEquals(1,usersDAO.getAll().size());
        waitForFxEvents();
        assertNotNull(userController.loginPassword("321", usersDAO.searchUser("testSuccess")));

    }

    @Test
    void testLoginPassFail() {
        Platform.runLater(() -> {
            userController.getView().getEmailF().setText("testSuccess");
            userController.getView().getPasswF().setText("wrong");

            clickOn(userController.getView().getLoginBtn());
        });

        waitForFxEvents();
        assertTrue(userController.getView().getWrongPasswordF().getText().contains("Wrong password."));
    }

    @Test
    void testLoginUserFail() {
        Platform.runLater(() -> {
            userController.getView().getEmailF().setText("wronguser");
            userController.getView().getPasswF().setText("not");

            clickOn(userController.getView().getLoginBtn());
        });

        waitForFxEvents();
        assertTrue(userController.getView().getWrongUsernameF().getText().contains("No user with this username"));
    }
}
