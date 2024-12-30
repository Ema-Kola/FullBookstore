package test.unit;
import controller.AddCategoryController;
import controller.UserController;
import dao.CategoryDAO;
import javafx.application.Platform;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;
import view.AddBookView;
import view.AddCategoryView;
import view.HomeView;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class AddCategoryControllerTest extends ApplicationTest {

    private AddCategoryController addCategoryController;
    @Mock CategoryDAO mockCategoryDao;
    @Mock AddCategoryView mockAddCategoryView;

    @BeforeEach
    public void setup() {
        interact(()-> mockAddCategoryView.getCategoryTf().clear());
    }

    @Override
    public void start(Stage stage) throws Exception {
        MockitoAnnotations.openMocks(this);
        addCategoryController = new AddCategoryController(new AddBookView(new HomeView(new User(), new UserController(new Stage()))));
        mockAddCategoryView = addCategoryController.getView();
        stage.setScene(addCategoryController.getView().showView(stage));
        stage.show();
    }

    //trying to achieve full statement and branch coverage of the AddCategory constructor
    @Test
    void coverage_success(){
        addCategoryController.setDao(mockCategoryDao);
        mockAddCategoryView.getCategoryTf().setText("valid");
        when(mockCategoryDao.searchCategory("valid")).thenReturn(null);
        when(mockCategoryDao.create("valid")).thenReturn(true);
        clickOn(mockAddCategoryView.getSubmitButton());
        waitForFxEvents();
        verifyAlert("New category added!");

    }
    private void verifyAlert(String expectedMsg) {
        DialogPane dialogPane = lookup(".alert").queryAs(DialogPane.class);
        assertNotNull(dialogPane);
        assertEquals(expectedMsg, dialogPane.getContentText(), "Alert message should match");
    }

    @Test
    void coverage_fail(){
        addCategoryController.setDao(mockCategoryDao);
        mockAddCategoryView.getCategoryTf().setText("valid");
        when(mockCategoryDao.searchCategory("valid")).thenReturn(null);
        when(mockCategoryDao.create("valid")).thenReturn(false);
        clickOn(mockAddCategoryView.getSubmitButton());
        waitForFxEvents();

        verifyAlert("Failed to save author.");
    }

    @Test
    void coverage_already_exists(){
        addCategoryController.setDao(mockCategoryDao);
        mockAddCategoryView.getCategoryTf().setText("valid");
        when(mockCategoryDao.searchCategory("valid")).thenReturn("valid");

        clickOn(mockAddCategoryView.getSubmitButton());
        waitForFxEvents();

        verifyAlert("This category already exists. ");
    }

}
