package test.unit;

import javafx.scene.control.DialogPane;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VerifyAlertTest extends ApplicationTest {


    void verifyAlert(String expectedMsg) {
        DialogPane dialogPane = lookup(".alert").queryAs(DialogPane.class);
        assertNotNull(dialogPane);
        assertEquals(expectedMsg, dialogPane.getContentText(), "Alert message should match");
    }
}
