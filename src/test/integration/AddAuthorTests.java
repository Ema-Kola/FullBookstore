package test.integration;

import controller.AddAuthorController;
import controller.UserController;
import dao.AuthorsDAO;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Author;
import model.Gender;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.framework.junit5.ApplicationTest;
import view.AddAuthorView;
import view.AddBookView;
import view.HomeView;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class AddAuthorTests extends ApplicationTest {

    private AddAuthorView addAuthorView;
    private AuthorsDAO authorsDAO;

    @TempDir
    File tempDir;

    @Override
    public void start(Stage stage) {
        File authorsFile = new File(tempDir, "authors.dat");
        authorsDAO = new AuthorsDAO(authorsFile.getPath());

        AddAuthorController addAuthorController = new AddAuthorController(new AddBookView(new HomeView(new User(), new UserController(new Stage()))));
        addAuthorController.setAuthorsDAO(authorsDAO);

        addAuthorView = addAuthorController.getView();
        Scene scene = addAuthorController.getView().showView(stage);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setup() {
        authorsDAO.getAll().clear();
        waitForFxEvents();

        interact(() -> {
            addAuthorView.getAuthorNameTf().clear();
            addAuthorView.getAuthorSurnameTf().clear();
            addAuthorView.getGendercomboBox().setValue(null);
        });
    }

    @Test
    public void testAddAuthorSuccessfully() {
        interact(() -> {
            addAuthorView.getAuthorNameTf().setText("Albert");
            addAuthorView.getAuthorSurnameTf().setText("Camus");
            addAuthorView.getGendercomboBox().setValue(Gender.FEMALE);
        });
        waitForFxEvents();

        clickOn(addAuthorView.getSubmitButton());

        Author addedAuthor = authorsDAO.searchAuthor("Albert", "Camus");
        assertNotNull(addedAuthor);
        assertEquals("Albert", addedAuthor.getFirstName());
        assertEquals("Camus", addedAuthor.getLastName());
        assertEquals(Gender.FEMALE, addedAuthor.getGender());
    }

    @Test
    public void testAddAuthorFailsWhenAuthorAlreadyExists() {
        authorsDAO.create(new Author("Ema", "Kola", Gender.FEMALE));

        interact(() -> {
            addAuthorView.getAuthorNameTf().setText("Ema");
            addAuthorView.getAuthorSurnameTf().setText("Kola");
            addAuthorView.getGendercomboBox().setValue(Gender.FEMALE);
        });
        waitForFxEvents();

        clickOn(addAuthorView.getSubmitButton());

        Author addedAuthor = authorsDAO.searchAuthor("Ema", "Kola");
        assertNotNull(addedAuthor);
        assertEquals(1, authorsDAO.getAll().size());
    }


}
