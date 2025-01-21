package test.integration;

import controller.UpdatePermissionsController;
import controller.UserController;
import dao.PermissionsDAO;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Role;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;
import view.HomeView;
import view.UpdatePermissionsView;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class UpdatePermissionsTests extends ApplicationTest {

    private UpdatePermissionsView updatePermissionsView;
    private PermissionsDAO permissionsDAO;

    @TempDir
    File tempDir;

    @Override
    public void start(Stage stage) {
        File permissionsFile = new File(tempDir, "permissions.dat");
        permissionsDAO = new PermissionsDAO(permissionsFile.getPath());

        HomeView homeView = new HomeView(new User(), new UserController(new Stage()));
        UpdatePermissionsController updatePermissionsController = new UpdatePermissionsController(stage, homeView);
        updatePermissionsController.setPermissionsDAO(permissionsDAO);

        updatePermissionsView = updatePermissionsController.getView();
        Scene scene = updatePermissionsView.showView(stage);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setup() {

        permissionsDAO.getAll().clear();
        permissionsDAO.create(Role.LIBRARIAN); // CheckOutBookPermission
        permissionsDAO.create(Role.LIBRARIAN); // AddBookPermission
        permissionsDAO.create(Role.LIBRARIAN); // BookStatsPermission
        permissionsDAO.create(Role.LIBRARIAN); // LibrarianStatsPermission
        permissionsDAO.create(Role.LIBRARIAN); // ManageEmployeesPermission
        permissionsDAO.create(Role.LIBRARIAN); // FinanceStatsPermission
        permissionsDAO.create(Role.LIBRARIAN); // ManageLibraryPermission

        waitForFxEvents();
    }

    @Test
    public void testUpdatePermissionsSuccess() {
        interact(() -> {
            updatePermissionsView.getAddBook().setValue(Role.ADMINISTRATOR);
            clickOn(updatePermissionsView.getUpdateBtn());
        });

        waitForFxEvents();

        boolean updateSuccessful = permissionsDAO.updateAll();
        assertTrue(updateSuccessful);
        assertEquals(Role.ADMINISTRATOR, permissionsDAO.getAddBookPermission());
    }


}
