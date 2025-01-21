package test.integration;

import controller.AddCategoryController;
import controller.UserController;
import dao.CategoryDAO;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Gender;
import model.Role;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;
import view.AddBookView;
import view.AddCategoryView;
import view.HomeView;

import java.io.File;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class AddCategoryControllerIntegration extends ApplicationTest {

    private static final String TEST_CATEGORY_FILE = "testCategories.db";
    private CategoryDAO categoryDAO;
    private AddCategoryController addCategoryController;
    private AddCategoryView addCategoryView;

    @TempDir
    static File tempFolder;
    static File tempCategoriesDao;

    @Override
    public void start(Stage stage) throws Exception {
        User user = new User("name", "sur",new Date(), Gender.FEMALE,"emko", "emakola22@gmail.com", "321",   Role.ADMINISTRATOR, "0695730257",0);
        addCategoryController = new AddCategoryController(new AddBookView(new HomeView(user, new UserController(new Stage()))));

        Scene scene = addCategoryController.getView().showView(stage);
        this.addCategoryView = addCategoryController.getView();
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    void setup() {
        tempCategoriesDao = new File(tempFolder, TEST_CATEGORY_FILE);
        categoryDAO = new CategoryDAO(tempCategoriesDao.getAbsolutePath());
        addCategoryController.setDao(categoryDAO);
    }

    @Test
    public void category_added_integration_test() {
        addCategoryView.getCategoryTf().setText("Science Fiction");

        Platform.runLater(() -> {
            clickOn(addCategoryView.getSubmitButton());
        });

        waitForFxEvents();

        String savedCategory = categoryDAO.searchCategory("Science Fiction");

        assertNotNull(savedCategory);
        assertEquals("Science Fiction", savedCategory);
    }

    @Test
    public void category_exists_integration_test() {
        categoryDAO.create("Science Fiction");

        addCategoryView.getCategoryTf().setText("Science Fiction");

        Platform.runLater(() -> {
            clickOn(addCategoryView.getSubmitButton());
        });

        waitForFxEvents();

        String existingCategory = categoryDAO.searchCategory("Science Fiction");

        System.out.println(categoryDAO.FILE_PATH);

        assertNotNull(existingCategory);
        assertEquals("Science Fiction", existingCategory);
    }
}
