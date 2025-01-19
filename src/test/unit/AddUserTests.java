package test.unit;
import controller.AddUserController;
import controller.UserController;
import dao.UsersDAO;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Gender;
import model.Role;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import view.AddUserView;
import view.HomeView;
import javafx.application.Platform;
import static org.mockito.Mockito.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class AddUserTests extends ApplicationTest {

    private AddUserController addUserController;
    private AddUserView addUserView;

    @Override
    public void start(Stage stage) {
        addUserController = new AddUserController(stage, new HomeView(new User(), new UserController(new Stage())));
        addUserView = addUserController.getView();
        Scene scene = addUserController.getView().showView(stage);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setup() {
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
        waitForFxEvents();
    }

    @Test
    public void testSubmitButtonEnabled() {

        interact(() -> addUserView.getFirstnameTF().setText("Ema"));
        interact(() -> addUserView.getLastnameTF().setText("Kola"));
        interact(() -> addUserView.getbirthdayTF().setText("24-07-2003"));
        interact(() -> addUserView.getphoneTF().setText("+355695730257"));
        interact(() -> addUserView.getemailTF().setText("ema@lib.com"));
        interact(() -> addUserView.getPasswTF().setText("ema123"));
        interact(() -> addUserView.getsalaryTF().setText("100"));
        interact(() -> addUserView.getUsername().setText("ema"));
        interact(() -> addUserView.getGender().setValue(Gender.FEMALE));
        interact(() -> addUserView.getaccessLevelTF().setValue(Role.LIBRARIAN));
        waitForFxEvents();
        verifyThat(addUserView.getSubmitBtn(), isEnabled());

    }


    @Test
    public void testSubmitButtonDisabled() {
        interact(() -> addUserView.getFirstnameTF().setText("Ema"));
        interact(() -> addUserView.getLastnameTF().setText("Kola"));
        interact(() -> addUserView.getbirthdayTF().setText("24-07-2003"));
        waitForFxEvents();
        verifyThat(addUserView.getSubmitBtn(), isDisabled());

    }


    @Test
    public void testUserAlreadyExists() {

        UsersDAO mockDAO = mock(UsersDAO.class);
        User existingUser = new User();
        when(mockDAO.searchUser("ema")).thenReturn(existingUser);
        addUserController.setUserDao(mockDAO);

        interact(() -> {
            interact(() -> addUserView.getFirstnameTF().setText("Ema"));
            interact(() -> addUserView.getLastnameTF().setText("Kola"));
            interact(() -> addUserView.getbirthdayTF().setText("24-07-2003"));
            interact(() -> addUserView.getphoneTF().setText("+355695730257"));
            interact(() -> addUserView.getemailTF().setText("ema@lib.com"));
            interact(() -> addUserView.getPasswTF().setText("ema123"));
            interact(() -> addUserView.getsalaryTF().setText("100"));
            interact(() -> addUserView.getUsername().setText("ema"));
            interact(() -> addUserView.getGender().setValue(Gender.FEMALE));
            interact(() -> addUserView.getaccessLevelTF().setValue(Role.LIBRARIAN));
        });
        waitForFxEvents();

        clickOn(addUserView.getSubmitBtn());

        waitForFxEvents();

        Platform.runLater(() -> {
            Assertions.assertTrue(addUserController.getAlert3().isShowing());
        });

        waitForFxEvents();
    }


    @Test
    public void testUserAddedSuccessfully() {
        UsersDAO mockDAO = mock(UsersDAO.class);
        when(mockDAO.searchUser("ema")).thenReturn(null);
        when(mockDAO.create(any(User.class))).thenReturn(true);
        addUserController.setUserDao(mockDAO);

        interact(() -> {
            interact(() -> addUserView.getFirstnameTF().setText("Ema"));
            interact(() -> addUserView.getLastnameTF().setText("Kola"));
            interact(() -> addUserView.getbirthdayTF().setText("24-07-2003"));
            interact(() -> addUserView.getphoneTF().setText("+355695730257"));
            interact(() -> addUserView.getemailTF().setText("ema@lib.com"));
            interact(() -> addUserView.getPasswTF().setText("ema123"));
            interact(() -> addUserView.getsalaryTF().setText("100"));
            interact(() -> addUserView.getUsername().setText("emakoooooo"));
            interact(() -> addUserView.getGender().setValue(Gender.FEMALE));
            interact(() -> addUserView.getaccessLevelTF().setValue(Role.LIBRARIAN));
        });
        waitForFxEvents();

        clickOn(addUserView.getSubmitBtn());

        waitForFxEvents();

        Platform.runLater(() -> {
            Assertions.assertTrue(addUserController.getAlert1().isShowing());
        });

        waitForFxEvents();

    }

    @Test
    public void testUserNotAddedSuccessfully() {
        UsersDAO mockDAO = mock(UsersDAO.class);
        when(mockDAO.searchUser("ema")).thenReturn(null);
        when(mockDAO.create(any(User.class))).thenReturn(false);
        addUserController.setUserDao(mockDAO);

        interact(() -> {
            interact(() -> addUserView.getFirstnameTF().setText("Ema"));
            interact(() -> addUserView.getLastnameTF().setText("Kola"));
            interact(() -> addUserView.getbirthdayTF().setText("24-07-2003"));
            interact(() -> addUserView.getphoneTF().setText("+355695730257"));
            interact(() -> addUserView.getemailTF().setText("ema@lib.com"));
            interact(() -> addUserView.getPasswTF().setText("ema123"));
            interact(() -> addUserView.getsalaryTF().setText("100"));
            interact(() -> addUserView.getUsername().setText("emaaaaa"));
            interact(() -> addUserView.getGender().setValue(Gender.FEMALE));
            interact(() -> addUserView.getaccessLevelTF().setValue(Role.LIBRARIAN));
        });
        waitForFxEvents();

        clickOn(addUserView.getSubmitBtn());

        waitForFxEvents();

        Platform.runLater(() -> {
            Assertions.assertTrue(addUserController.getAlert2().isShowing());
        });

        waitForFxEvents();

    }





}






