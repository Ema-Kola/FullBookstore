package test.unit;

import controller.UpdatePermissionsController;
import dao.PermissionsDAO;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import view.HomeView;
import view.UpdatePermissionsView;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class UpdatePermissionsTests extends ApplicationTest {

    private UpdatePermissionsController controller;
    private UpdatePermissionsView view;
    private PermissionsDAO mockPermissionsDao;

    @Override
    public void start(Stage stage) {
        mockPermissionsDao = mock(PermissionsDAO.class);

        HomeView mockHomeView = mock(HomeView.class);
        controller = new UpdatePermissionsController(stage, mockHomeView);
        controller.setPermissionsDAO(mockPermissionsDao);
        view = controller.getView();

        Scene scene = view.showView(stage);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setup() {
        waitForFxEvents();
    }

    @Test
    public void testUpdatePermissionsSuccess() {
        when(mockPermissionsDao.updateAll()).thenReturn(true);

        interact(() -> {
            clickOn(view.getUpdateBtn());
        });

        waitForFxEvents();

        verify(mockPermissionsDao, times(1)).updateAll();

        Platform.runLater(() -> {
            assertTrue(controller.getSuccessAlert().isShowing());
        });
        waitForFxEvents();
    }

    @Test
    public void testUpdatePermissionsFailure() {

        when(mockPermissionsDao.updateAll()).thenReturn(false);

        interact(() -> {
            clickOn(view.getUpdateBtn());
        });

        waitForFxEvents();

        verify(mockPermissionsDao, times(1)).updateAll();

        Platform.runLater(() -> {
            assertTrue(controller.getErrorAlert().isShowing());
        });
        waitForFxEvents();
    }
}

