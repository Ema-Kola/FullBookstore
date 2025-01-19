package test.unit;

import controller.UserController;
import dao.UsersDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

public class UserControllerTest extends ApplicationTest {
    private UserController userController;
    @Mock
    UsersDAO usersDAO;
    @Mock User user;

    @Override
    public void start(Stage stage){
        MockitoAnnotations.openMocks(this);
        userController = new UserController(new Stage());

        userController.setUsersDAO(usersDAO);
        stage.setScene(userController.getView().showView(stage));
        stage.show();
    }

    @BeforeEach
    void user(){
        ObservableList<User> users = FXCollections.observableArrayList();
        users.add(user);
        when(usersDAO.getAllActive()).thenReturn(users);
        when(user.getUsername()).thenReturn("admin");
        when(user.getPassword()).thenReturn("pass");
    }

    @Test
    void test_loginUsername_success(){
        assertEquals(user, userController.loginUsername("admin"));
    }

    @Test
    void test_loginUsername_fail(){
        assertNull(userController.loginUsername("notUsername"));
    }

    @Test
    void test_loginPassword_success(){
        assertEquals(user, userController.loginPassword("pass", user));
    }

    @Test
    void test_logiPassword_fail(){
        assertNull(userController.loginPassword("notValidPass", user));
    }
}




