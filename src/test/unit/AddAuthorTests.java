package test.unit;
import controller.AddAuthorController;
import controller.UserController;
import dao.AuthorsDAO;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Author;
import model.Gender;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import view.AddAuthorView;
import view.AddBookView;
import view.HomeView;
import javafx.application.Platform;
import static org.mockito.Mockito.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class AddAuthorTests extends ApplicationTest {

    private AddAuthorController addAuthorController;
    private AddAuthorView addAuthorView;

    @Override
    public void start(Stage stage) {
        addAuthorController = new AddAuthorController(new AddBookView(new HomeView(new User(), new UserController(new Stage()))));
        addAuthorView = addAuthorController.getView();
        Scene scene = addAuthorController.getView().showView(stage);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setup() {
        interact(() -> {
            addAuthorView.getAuthorNameTf().clear();
            addAuthorView.getAuthorSurnameTf().clear();
            addAuthorView.getGendercomboBox().setValue(null);
        });
        waitForFxEvents();
    }

    //Decision Table Testing, 9 combinations from the decision table

    @Test
    public void testSubmitButtonEnabled() {

        interact(() -> addAuthorView.getAuthorNameTf().setText("Ema"));

        interact(() -> addAuthorView.getAuthorSurnameTf().setText("Kola"));
        interact(() -> addAuthorView.getGendercomboBox().setValue(Gender.FEMALE));
        waitForFxEvents();
        verifyThat(addAuthorView.getSubmitButton(), isEnabled());


    }

    @Test
    public void testSubmitButtonDisabledWhenGenderEmpty() {

        interact(() -> addAuthorView.getAuthorNameTf().setText("Ema"));

        interact(() -> addAuthorView.getAuthorSurnameTf().setText("Kola"));
        interact(() -> addAuthorView.getGendercomboBox().setValue(null));
        waitForFxEvents();
        verifyThat(addAuthorView.getSubmitButton(), isDisabled());


    }

    @Test
    public void testSubmitButtonDisabledWhenSurnameEmpty() {

        interact(() -> addAuthorView.getAuthorNameTf().setText("Ema"));

        interact(() -> addAuthorView.getAuthorSurnameTf().setText(""));
        interact(() -> addAuthorView.getGendercomboBox().setValue(Gender.FEMALE));
        waitForFxEvents();
        verifyThat(addAuthorView.getSubmitButton(), isDisabled());


    }
    @Test
    public void testSubmitButtonDisabledWhenSurnameGenderEmpty() {

        interact(() -> addAuthorView.getAuthorNameTf().setText("Ema"));

        interact(() -> addAuthorView.getAuthorSurnameTf().setText(""));
        interact(() -> addAuthorView.getGendercomboBox().setValue(null));
        waitForFxEvents();
        verifyThat(addAuthorView.getSubmitButton(), isDisabled());
    }


    @Test
    public void testSubmitButtonDisabledWhenNameEmpty() {

        interact(() -> addAuthorView.getAuthorNameTf().setText(""));

        interact(() -> addAuthorView.getAuthorSurnameTf().setText("Kola"));
        interact(() -> addAuthorView.getGendercomboBox().setValue(Gender.FEMALE));
        waitForFxEvents();
        verifyThat(addAuthorView.getSubmitButton(), isDisabled());
    }

    @Test
    public void testSubmitButtonDisabledWhenNameGenderEmpty() {

        interact(() -> addAuthorView.getAuthorNameTf().setText(""));

        interact(() -> addAuthorView.getAuthorSurnameTf().setText(""));
        interact(() -> addAuthorView.getGendercomboBox().setValue(null));
        waitForFxEvents();
        verifyThat(addAuthorView.getSubmitButton(), isDisabled());
    }

    @Test
    public void testSubmitButtonDisabledWhenSurnameNameEmpty() {

        interact(() -> addAuthorView.getAuthorNameTf().setText(""));

        interact(() -> addAuthorView.getAuthorSurnameTf().setText(""));
        interact(() -> addAuthorView.getGendercomboBox().setValue(Gender.FEMALE));
        waitForFxEvents();
        verifyThat(addAuthorView.getSubmitButton(), isDisabled());
    }

    @Test
    public void testSubmitButtonDisabledWhenAllEmpty() {

        interact(() -> addAuthorView.getAuthorNameTf().setText(""));

        interact(() -> addAuthorView.getAuthorSurnameTf().setText(""));
        interact(() -> addAuthorView.getGendercomboBox().setValue(null));
        waitForFxEvents();
        verifyThat(addAuthorView.getSubmitButton(), isDisabled());
    }


    //Further tests to achieve controller coverage


    @Test
    public void testAuthorAlreadyExists() {

        AuthorsDAO mockDAO = mock(AuthorsDAO.class);
        Author existingAuthor = new Author("Ema", "Kola", Gender.FEMALE);
        when(mockDAO.searchAuthor("Ema", "Kola")).thenReturn(existingAuthor);
        addAuthorController.setAuthorsDAO(mockDAO);


        interact(() -> {
            addAuthorView.getAuthorNameTf().setText("Ema");
            addAuthorView.getAuthorSurnameTf().setText("Kola");
            addAuthorView.getGendercomboBox().setValue(Gender.FEMALE);
        });
        waitForFxEvents();


        clickOn(addAuthorView.getSubmitButton());

        waitForFxEvents();

        Platform.runLater(() -> {
            Assertions.assertTrue(addAuthorController.getAlert3().isShowing());
        });

        waitForFxEvents();
    }
    @Test
    public void testAuthorAddedSuccessfully() {


        AuthorsDAO mockDAO = mock(AuthorsDAO.class);
        when(mockDAO.searchAuthor("Ema", "Kola")).thenReturn(null);
        when(mockDAO.create(any(Author.class))).thenReturn(true);
        addAuthorController.setAuthorsDAO(mockDAO);


        interact(() -> {
            addAuthorView.getAuthorNameTf().setText("Ema");
            addAuthorView.getAuthorSurnameTf().setText("Kola");
            addAuthorView.getGendercomboBox().setValue(Gender.FEMALE);
        });
        waitForFxEvents();


        clickOn(addAuthorView.getSubmitButton());

        waitForFxEvents();

        Platform.runLater(() -> {
            Assertions.assertTrue(addAuthorController.getAlert1().isShowing());
        });

        waitForFxEvents();
    }

    @Test
    public void testAuthorNotAddedSuccessfully() {

        AuthorsDAO mockDAO = mock(AuthorsDAO.class);
        when(mockDAO.searchAuthor("Ema", "Kola")).thenReturn(null);
        when(mockDAO.create(any(Author.class))).thenReturn(false);
        addAuthorController.setAuthorsDAO(mockDAO);

        interact(() -> {
            addAuthorView.getAuthorNameTf().setText("Ema");
            addAuthorView.getAuthorSurnameTf().setText("Kola");
            addAuthorView.getGendercomboBox().setValue(Gender.FEMALE);
        });
        waitForFxEvents();


        clickOn(addAuthorView.getSubmitButton());

        waitForFxEvents();

        Platform.runLater(() -> {
            Assertions.assertTrue(addAuthorController.getAlert2().isShowing());
        });

        waitForFxEvents();
    }






}






