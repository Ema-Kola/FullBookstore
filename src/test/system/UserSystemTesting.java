package test.system;

import controller.AddUserController;
import controller.HomeViewController;
import controller.ManageEmployeeController;
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
import view.LogInView;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class UserSystemTesting extends ApplicationTest{

        private UserController userController;
        private HomeViewController homeViewController;
        private AddUserController addUserController;
        private ManageEmployeeController manageEmployeeController;
        private UsersDAO usersDAO;
        private LogInView logInView;
        private AddUserView addUserView;
        private HomeView homeView;

        private Stage stage;
        @TempDir
        File tempFolder;
        File tempUsers;

        @Override
        public void start(Stage stage) throws Exception {
            this.stage = stage;
            userController = new UserController(stage);
            logInView = userController.getView();
            Scene scene = logInView.showView(stage);
            stage.setScene(scene);
            stage.show();
        }

        @BeforeEach
        public void setup() {
            tempUsers = new File(tempFolder, "users.dat");
            usersDAO = new UsersDAO(tempUsers.getAbsolutePath());

            User testUser = new User("testuser", "Test", new java.util.Date(), model.Gender.FEMALE, "testuser", "test@example.com", "password123", Role.ADMINISTRATOR, "1234567890", 0);
            usersDAO.create(testUser);

            userController.setUsersDAO(usersDAO);
        }

        @Test
        public void testValidAddAndLogin() {
            logInView.getEmailF().setText("testuser");  //username act
            logInView.getPasswF().setText("password123");

            clickOn(logInView.getLoginBtn());

            waitForFxEvents();

            assertNotNull(userController.getHomeViewController());
            assertNotNull(userController.getHomeViewController().getView());

            homeViewController = userController.getHomeViewController();

            homeView = homeViewController.getView();
            clickOn(homeView.getAddUserButton());

            addUserController = homeViewController.getAddUserController();

            addUserView = addUserController.getView();
            waitForFxEvents();
            addUserController.setUserDao(usersDAO);

            interact(() -> {
                addUserView.getFirstnameTF().setText("Name");
                addUserView.getLastnameTF().setText("Surname");
                addUserView.getbirthdayTF().setText("24-07-2003");
                addUserView.getphoneTF().setText("+355695730257");
                addUserView.getemailTF().setText("ema@lib.com");
                addUserView.getPasswTF().setText("name123");
                addUserView.getsalaryTF().setText("100");
                addUserView.getUsername().setText("name123");
                addUserView.getGender().setValue(Gender.FEMALE);
                addUserView.getaccessLevelTF().setValue(Role.LIBRARIAN);
            });
            waitForFxEvents();

            clickOn(addUserView.getSubmitBtn());
            addUserController.setUserDao(usersDAO);

            User addedUser = usersDAO.searchUser("name123");
            assertNotNull(addedUser);

            interact(() -> {
                if (addUserController.getAlert1() != null) {
                    addUserController.getAlert1().close();
                }
            });

            waitForFxEvents();

            clickOn(addUserView.getHomeBtn());
            waitForFxEvents();



            clickOn(homeViewController.getView().getManageEmployeeButton());
            manageEmployeeController = homeViewController.getManageEmployeeController();

            manageEmployeeController.setUsersDao(usersDAO);
            waitForFxEvents();

            assertEquals(1, manageEmployeeController.getView().getTableView().getItems().size());
            assertEquals("name123", manageEmployeeController.getView().getTableView().getItems().getFirst().getUsername());

            waitForFxEvents();
            clickOn(manageEmployeeController.getView().getHomeBtn());
            waitForFxEvents();


            clickOn(homeViewController.getView().getLogOutButton());
            UserController uc = homeViewController.getUserController();

            waitForFxEvents();
            logInView = uc.getView();
            uc.setUsersDAO(usersDAO);
            waitForFxEvents();

            interact(()->{
                logInView.getEmailF().setText("name123");
                logInView.getPasswF().setText("name123");
            });

            waitForFxEvents();
            clickOn(logInView.getLoginBtn());

            waitForFxEvents();

            assertNotNull(uc.getHomeViewController());
            assertNotNull(uc.getHomeViewController().getView());

            assertTrue(uc.getHomeViewController().getView().getAddBookButton().isVisible());
            assertFalse(uc.getHomeViewController().getView().getManageEmployeeButton().isVisible());
            assertFalse(uc.getHomeViewController().getView().getUpdatePermissionsButton().isVisible());
            assertFalse(uc.getHomeViewController().getView().getPaycheckButton().isVisible());
            assertTrue(uc.getHomeViewController().getView().getSearchBookButton().isVisible());
            assertTrue(uc.getHomeViewController().getView().getCheckOutBookButton().isVisible());


        }


    }